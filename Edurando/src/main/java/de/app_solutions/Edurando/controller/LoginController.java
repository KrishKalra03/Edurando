package de.app_solutions.Edurando.controller;

import de.app_solutions.Edurando.config.security.JwtUtil;
import de.app_solutions.Edurando.model.LoginRequest;
import de.app_solutions.Edurando.model.LoginResponse;
import de.app_solutions.Edurando.model.UserProfile;
import de.app_solutions.Edurando.repository.UserProfileRepository;
import de.app_solutions.Edurando.service.LoginAttemptService;
import de.app_solutions.Edurando.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final LoginAttemptService loginAttemptService;
    private final JwtUtil jwtUtil;
    private final UserProfileRepository userProfileRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        String ip = resolveClientIp(httpRequest);

        if (loginAttemptService.isBlocked(ip)) {
            long retryAfter = loginAttemptService.secondsUntilUnblocked(ip);
            long minutes    = (retryAfter + 59) / 60;
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .header("Retry-After", String.valueOf(retryAfter))
                    .body("Too many failed login attempts. Please try again in " + minutes + " minute(s).");
        }

        Pair<Boolean, String> result = loginService.login(request);

        if (!result.getFirst()) {
            loginAttemptService.registerFailure(ip);
            return ResponseEntity.badRequest().body(result.getSecond());
        }

        loginAttemptService.resetFailures(ip);
        String token = jwtUtil.generateToken(request.getEmail());
        UserProfile user = userProfileRepository.findUserProfileByUsername(request.getEmail())
                .orElseThrow();
        return ResponseEntity.ok(new LoginResponse(token, user.getId()));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            jwtUtil.invalidate(header.substring(7));
        }
        return ResponseEntity.ok("Logged out.");
    }

    /** Reads the real client IP, respecting reverse-proxy X-Forwarded-For headers. */
    private static String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
