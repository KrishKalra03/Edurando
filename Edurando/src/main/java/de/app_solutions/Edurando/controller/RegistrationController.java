package de.app_solutions.Edurando.controller;

import de.app_solutions.Edurando.model.RegistrationRequest;
import de.app_solutions.Edurando.service.RegistrationService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1")
@Data
@CrossOrigin(origins = "http://localhost:5173")
public class RegistrationController {

    @Value("${frontend.url}")
    private String frontendUrl;

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
        Pair<Boolean, String> response = registrationService.register(request);
        if (response.getFirst()) return ResponseEntity.ok(response.getSecond());
        else return ResponseEntity.badRequest().body(response.getSecond());
    }

    @GetMapping("/confirm")
    public ResponseEntity<Void> confirm(@RequestParam("token") String token) {
        Pair<Boolean, String> response = registrationService.confirmToken(token);
        String redirect;
        if (response.getFirst()) {
            redirect = frontendUrl + "/verify?status=success";
        } else {
            String msg = response.getSecond();
            String reason = msg.contains("expired") ? "expired"
                          : msg.contains("already")  ? "already-confirmed"
                          : "invalid";
            redirect = frontendUrl + "/verify?status=error&reason=" + reason;
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, redirect)
                .build();
    }

    @PostMapping("/reconfirm")
    public ResponseEntity<String> resendConfirmationMail(@RequestParam("email") String email) {
        Pair<Boolean,String> response = registrationService.resendConfirmationEmail(email);
        if (response.getFirst()) return ResponseEntity.ok(response.getSecond());
        else return ResponseEntity.badRequest().body(response.getSecond());
    }

}
