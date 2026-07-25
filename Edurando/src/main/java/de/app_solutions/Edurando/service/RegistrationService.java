package de.app_solutions.Edurando.service;


import de.app_solutions.Edurando.model.ConfirmationToken;
import de.app_solutions.Edurando.model.RegistrationRequest;
import de.app_solutions.Edurando.model.Role;
import de.app_solutions.Edurando.model.UserProfile;
import de.app_solutions.Edurando.repository.UserProfileRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.Tuple;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static de.app_solutions.Edurando.service.UserProfileService.USER_NOT_FOUND;

@Service
@Data
public class RegistrationService {
    private final static String USER_NOT_FOUND = "User with Email %s was not found.";
    @Value("${app.base-url}")
    private String appBaseUrl;

    private final UserProfileService userProfileService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final PasswordValidator passwordValidator;
    private final UserProfileRepository userProfileRepository;
    private static final Duration EMAIL_SEND_INTERVAL = Duration.ofMinutes(5);
    private Map<String, LocalDateTime> lastEmailSentTimes = new HashMap<>();

    public Pair<Boolean, String> register(RegistrationRequest request) {

        //Password valid Test
        Pair<Boolean, String> pwTest = passwordValidator.passwordTest(request.getPassword(), request.getPasswordRepeat());
        Pair<Boolean, String> emailTest = emailValidator.testMail(request.getEmail());
        StringBuilder sb = new StringBuilder();
        sb.append(pwTest.getSecond());
        sb.append(emailTest.getSecond());
        Pair<Boolean, String> result;

        boolean valid = pwTest.getFirst() && emailTest.getFirst();

        if (!request.getTermsAgreed()) {
            sb.append("Terms of Service not agreed.");
            valid = false;
        }
        if (!request.getPrivacyAgreed()) {
            sb.append("Privacy Policy not agreed.");
            valid = false;
        }
        if (valid) {
            String token = userProfileService.signUpUser(new UserProfile(
                            request.getRole(),
                            request.getFirstName(),
                            request.getLastName(),
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            String link = String.format("%s/api/v1/confirm/?token=%s", appBaseUrl, token);
            emailSender.send(request.getEmail(), buildEmail(request, link), "Confirm your Email");
            result = Pair.of(true, "Registration was successful.");
        } else {
            result = Pair.of(false, sb.toString());
        }
        System.err.println(result);
        return result;
    }

    public Pair<Boolean, String> resendConfirmationEmail(String email) {
        LocalDateTime currentTime = LocalDateTime.now();

        Optional<UserProfile> userOpt = userProfileRepository.findUserProfileByUsername(email);
        if (userOpt.isEmpty()) {
            return Pair.of(false, String.format(USER_NOT_FOUND, email));
        }
        UserProfile user = userOpt.get();

        if (user.isEnabled()) {
            return Pair.of(false, "This account is already verified.");
        }

        LocalDateTime lastEmailSentTime = lastEmailSentTimes.getOrDefault(user.getUsername(), LocalDateTime.MIN);
        if (lastEmailSentTime.plus(EMAIL_SEND_INTERVAL).isAfter(currentTime)) {
            return Pair.of(false, "Please wait before requesting another confirmation-mail.");
        }

        confirmationTokenService.deleteByUsername(user.getUsername());
        String newToken = userProfileService.signUpUser(user);
        String link = String.format("%s/api/v1/confirm/?token=%s", appBaseUrl, newToken);
        emailSender.send(email, buildEmail(user.getUsername(), link), "Confirm your Email");
        // Aktualisiere lastEmailSentTime für den aktuellen Benutzer
        lastEmailSentTimes.put(email, LocalDateTime.now());
        return Pair.of(true, "Confirmation email has been resent.");
    }


    @Transactional
    public Pair<Boolean, String> confirmToken(String token) {
        Optional<ConfirmationToken> tokenOpt = confirmationTokenService.getToken(token);
        if (tokenOpt.isEmpty()) {
            return Pair.of(false, "token not found");
        }
        ConfirmationToken confirmationToken = tokenOpt.get();

        if (confirmationToken.getConfirmedAt() != null) {
            return Pair.of(false, "email already confirmed");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            return Pair.of(false, "token expired");
        }
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        //confirmationTokenService.setConfirmationAt(token);
        userProfileService.enableAppUser(confirmationToken.getUser().getUsername());
        return Pair.of(true, "Verification successful");
    }


    private String buildEmail(RegistrationRequest user, String link) {
        return buildEmailHtml(user.getFirstName(), user.getLastName(), link);
    }

    private String buildEmail(String email, String link) {
        UserProfile user = userProfileRepository.findUserProfileByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
        return buildEmailHtml(user.getFirstName(), user.getLastName(), link);
    }

    private static String buildEmailHtml(String firstName, String lastName, String link) {
        return EMAIL_TEMPLATE
                .replace("{firstName}", firstName)
                .replace("{lastName}",  lastName)
                .replace("{link}",      link);
    }

    private static final String EMAIL_TEMPLATE = """
        <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
        <html lang="en">
        <head>
          <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
          <meta name="viewport" content="width=device-width,initial-scale=1.0">
          <title>Confirm your Email</title>
        </head>
        <body style="margin:0;padding:0;background-color:#0c0a1e;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,sans-serif;">

          <span style="display:none;font-size:1px;color:#0c0a1e;max-height:0;overflow:hidden;">
            Verify your email address to activate your Edurando account.
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
                      <h1 style="margin:0;font-size:26px;font-weight:700;color:#ffffff;line-height:1.25;">Confirm your Email</h1>
                    </td>
                  </tr>

                  <!-- Body -->
                  <tr>
                    <td style="padding:40px 48px 36px;">

                      <p style="margin:0 0 20px;font-size:20px;font-weight:600;color:#ede9fe;line-height:1.3;">
                        Hello {firstName} {lastName},
                      </p>
                      <p style="margin:0 0 32px;font-size:15px;line-height:1.7;color:#c4b5fd;">
                        Thank you for registering at Edurando! To activate your account, please verify your email address.
                      </p>

                      <table cellpadding="0" cellspacing="0" border="0" role="presentation" width="100%">
                        <tr>
                          <td align="center" style="padding-bottom:36px;">
                            <a href="{link}" target="_blank"
                               style="display:inline-block;background-color:#8b5cf6;background-image:linear-gradient(135deg,#6366f1,#8b5cf6,#a855f7);color:#ffffff;font-size:16px;font-weight:700;text-decoration:none;padding:16px 48px;border-radius:12px;letter-spacing:0.02em;line-height:1;">
                              Verify Now &rarr;
                            </a>
                          </td>
                        </tr>
                      </table>

                      <table cellpadding="0" cellspacing="0" border="0" role="presentation" width="100%">
                        <tr>
                          <td style="border-top:1px solid #2a1f5e;padding-top:24px;">
                            <p style="margin:0 0 6px;font-size:13px;color:#7c6db8;line-height:1.6;">
                              This link will expire in <span style="color:#c4b5fd;font-weight:600;">15&nbsp;minutes</span>.
                            </p>
                            <p style="margin:0;font-size:13px;color:#7c6db8;line-height:1.6;">
                              If you didn&#x2019;t create this account, you can safely ignore this email.
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

