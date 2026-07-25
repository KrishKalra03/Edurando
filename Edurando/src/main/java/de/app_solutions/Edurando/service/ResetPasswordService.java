package de.app_solutions.Edurando.service;

import de.app_solutions.Edurando.model.ConfirmationCode;
import de.app_solutions.Edurando.model.ResetPasswordRequest;
import de.app_solutions.Edurando.model.UserProfile;
import de.app_solutions.Edurando.repository.ConfirmationCodeRepository;
import de.app_solutions.Edurando.repository.UserProfileRepository;
import lombok.Data;
import org.springframework.data.util.Pair;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static de.app_solutions.Edurando.service.UserProfileService.USER_NOT_FOUND;

@Service
@Data
public class ResetPasswordService {
    private final UserProfileService userProfileService;
    private final EmailValidator emailValidator;
    private final UserProfileRepository userProfileRepository;
    private final EmailSender emailSender;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PasswordValidator passwordValidator;
    private final ConfirmationCodeRepository confirmationCodeRepository;

    private static final Duration EMAIL_SEND_INTERVAL = Duration.ofMinutes(5);
    private Map<String, LocalDateTime> lastEmailSentTimes = new HashMap<>();


    @Transactional
    public Pair<Boolean, String> resetPassword(ResetPasswordRequest request) {
        Optional<UserProfile> userOpt = userProfileRepository.findUserProfileByUsername(request.getEmail());
        if (userOpt.isEmpty()) {  //!user.isPresent()
            return Pair.of(false, String.format(USER_NOT_FOUND, request.getEmail()));
        }
        UserProfile user = userOpt.get();
        Optional<ConfirmationCode> confirmationCode = confirmationCodeRepository.findByUser_Username(user.getUsername());
        if (confirmationCode.isEmpty()) {
            return Pair.of(false, "Passwordrequest-Token does not exist.");
        }
        if (!confirmationCode.get().isConfirmed()) {
            return Pair.of(false, "Passwordrequest-Token is not confirmed.");
        }
        if (confirmationCode.get().getExpiresAt().isBefore(LocalDateTime.now())) {
            confirmationCodeRepository.deleteByUser_Username(user.getUsername());
            return Pair.of(false, "Password reset request has expired. Please start over.");
        }
        String currentUserPw = user.getPassword();
        Pair<Boolean, String> newPwTuple = passwordValidator.passwordTest(request.getNewPassword(), request.getNewPasswordRepeat());
        if (bCryptPasswordEncoder.matches(request.getNewPassword(), currentUserPw)) {
            return Pair.of(false, "Password could not be changed because the new password is the same as the previous password.");
        }
        if (!newPwTuple.getFirst()) {
            return newPwTuple;
        }
        user.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
        userProfileRepository.save(user);
        confirmationCodeRepository.deleteByUser_Username(user.getUsername());

        return Pair.of(true, "Password reset was successful.");
    }


    public Pair<Boolean, String> confirmCode(String email, String enteredConfirmCode) {
        Optional<ConfirmationCode> confirmationCodeOpt = confirmationCodeRepository.findByUser_Username(email);
        if (confirmationCodeOpt.isEmpty()) {
            return Pair.of(false, "Passwordrequest-Token does not exist.");
        }
        ConfirmationCode confirmationCode = confirmationCodeOpt.get();
        Optional<UserProfile> user = userProfileRepository.findUserProfileByUsername(confirmationCode.getUser().getUsername());
        if (user.isEmpty()) {
            return Pair.of(false, String.format(USER_NOT_FOUND, confirmationCode.getUser().getUsername()));
        }
        if (confirmationCode.isConfirmed()) {
            return Pair.of(false, "Request not valid.");
        }
        if (confirmationCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            return Pair.of(false, "Code has expired. Please request a new password reset.");
        }
        if (bCryptPasswordEncoder.matches(enteredConfirmCode, confirmationCode.getCode())) {
            confirmationCode.setConfirmed(true);
            confirmationCodeRepository.save(confirmationCode);
            return Pair.of(true, "Confirmation was successful.");
        } else {
            return Pair.of(false, "The entered confirmationcode is not correct.");
        }
    }


    @Transactional
    public Pair<Boolean, String> forgotPassword(String email) {
        LocalDateTime currentTime = LocalDateTime.now();
        Optional<UserProfile> userOpt = userProfileRepository.findUserProfileByUsername(email);
        if (userOpt.isPresent()) {
            UserProfile user = userOpt.get();
            if (!user.isEnabled()) {
                return Pair.of(false, "Your account is not verified.");
            }
            if (user.isLocked()) {
                return Pair.of(false, "Your account is locked. Please contact the support.");
            }
            Optional<ConfirmationCode> oldConfirmationCodesOpt = confirmationCodeRepository.findByUser_Username(user.getUsername());
            if(oldConfirmationCodesOpt.isPresent()) {
                ConfirmationCode confirmationCode = oldConfirmationCodesOpt.get();
                confirmationCodeRepository.deleteByUser_Username(confirmationCode.getUser().getUsername());
            }
            ConfirmationCode confirmationCode = new ConfirmationCode(
                    generateRandomCode(),
                    false,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    user
            );
            LocalDateTime lastEmailSentTime = lastEmailSentTimes.getOrDefault(user.getUsername(), LocalDateTime.MIN);
            if (lastEmailSentTime.plus(EMAIL_SEND_INTERVAL).isAfter(currentTime)) {
                return Pair.of(false, "Please wait before requesting another password reset.");
            }

            //user.setConfirmationCode(confirmationCode);
            emailSender.send(user.getUsername(), buildEmail(confirmationCode.getUser().getUsername(), confirmationCode.getCode()), "Reset password request");

            // Aktualisiere lastEmailSentTime für den aktuellen Benutzer
            lastEmailSentTimes.put(email, LocalDateTime.now());

            String encodedConfirmCode = bCryptPasswordEncoder.encode(confirmationCode.getCode());
            confirmationCode.setCode(encodedConfirmCode);
            //confirmationCode.setUser(user);
            confirmationCodeRepository.save(confirmationCode);
            //  userProfileRepository.save(user);
            return Pair.of(true, "Email sent successfully.");
        } else {
            return Pair.of(false, String.format(USER_NOT_FOUND, email));
        }
    }

    public String generateRandomCode() {
        // Erstelle eine Instanz der Random-Klasse
        Random random = new Random();

        // Generiere eine zufällige 4-stellige Zahl
        int code = random.nextInt(9000) + 1000;

        // Konvertiere die Zahl in einen String und gib sie zurück
        return String.valueOf(code);
    }

    private String buildEmail(String email, String confirmCode) {
        UserProfile user = userProfileRepository.findUserProfileByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
        return RESET_EMAIL_TEMPLATE
                .replace("{firstName}", user.getFirstName())
                .replace("{lastName}",  user.getLastName())
                .replace("{code}",      confirmCode);
    }

    private static final String RESET_EMAIL_TEMPLATE = """
        <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
        <html lang="en">
        <head>
          <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
          <meta name="viewport" content="width=device-width,initial-scale=1.0">
          <title>Reset your Password</title>
        </head>
        <body style="margin:0;padding:0;background-color:#0c0a1e;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,sans-serif;">

          <span style="display:none;font-size:1px;color:#0c0a1e;max-height:0;overflow:hidden;">
            Your Edurando password reset code is inside.
          </span>

          <table width="100%" cellpadding="0" cellspacing="0" border="0" role="presentation"
                 style="background-color:#0c0a1e;padding:48px 16px;">
            <tr>
              <td align="center">

                <table cellpadding="0" cellspacing="0" border="0" role="presentation"
                       style="width:100%;max-width:560px;background-color:#12102a;border-radius:16px;border:1px solid #2a1f5e;overflow:hidden;">

                  <!-- Header -->
                  <tr>
                    <td align="center"
                        style="padding:36px 48px 30px;text-align:center;background-color:#7c3aed;background-image:linear-gradient(135deg,#6366f1 0%,#8b5cf6 60%,#a855f7 100%);">
                      <p style="margin:0 0 4px;font-size:11px;font-weight:700;color:rgba(255,255,255,0.65);letter-spacing:0.12em;text-transform:uppercase;">EDURANDO</p>
                      <h1 style="margin:0;font-size:26px;font-weight:700;color:#ffffff;line-height:1.25;">Reset your Password</h1>
                    </td>
                  </tr>

                  <!-- Body -->
                  <tr>
                    <td style="padding:40px 48px 36px;">

                      <p style="margin:0 0 20px;font-size:20px;font-weight:600;color:#ede9fe;line-height:1.3;">
                        Hello {firstName} {lastName},
                      </p>
                      <p style="margin:0 0 28px;font-size:15px;line-height:1.7;color:#c4b5fd;">
                        We received a request to reset your Edurando password. Use the code below to continue. It expires in <span style="color:#ede9fe;font-weight:600;">15&nbsp;minutes</span>.
                      </p>

                      <!-- Code block -->
                      <table cellpadding="0" cellspacing="0" border="0" role="presentation" width="100%">
                        <tr>
                          <td align="center" style="padding-bottom:32px;">
                            <div style="display:inline-block;background-color:#1a1836;border:2px solid #4c3d8a;border-radius:16px;padding:20px 48px;">
                              <span style="font-family:'Courier New',Courier,monospace;font-size:42px;font-weight:700;color:#ede9fe;letter-spacing:0.18em;">{code}</span>
                            </div>
                          </td>
                        </tr>
                      </table>

                      <table cellpadding="0" cellspacing="0" border="0" role="presentation" width="100%">
                        <tr>
                          <td style="border-top:1px solid #2a1f5e;padding-top:24px;">
                            <p style="margin:0 0 6px;font-size:13px;color:#7c6db8;line-height:1.6;">
                              If you didn&#x2019;t request a password reset, you can safely ignore this email.
                            </p>
                            <p style="margin:0;font-size:13px;color:#7c6db8;line-height:1.6;">
                              Your password will <span style="color:#c4b5fd;font-weight:600;">not</span> be changed unless you use this code.
                            </p>
                          </td>
                        </tr>
                      </table>

                    </td>
                  </tr>

                  <!-- Footer -->
                  <tr>
                    <td align="center"
                        style="background-color:#0c0a1e;border-top:1px solid #1a1836;padding:18px 48px;text-align:center;">
                      <p style="margin:0;font-size:12px;color:#7c6db8;">
                        &copy; 2024 Edurando &mdash; The learning platform for students
                      </p>
                    </td>
                  </tr>

                </table>
              </td>
            </tr>
          </table>

        </body>
        </html>
        """;
}
