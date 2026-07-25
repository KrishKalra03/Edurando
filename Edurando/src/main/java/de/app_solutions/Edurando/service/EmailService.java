package de.app_solutions.Edurando.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EmailService implements EmailSender {

    private static final String RESEND_URL = "https://api.resend.com/emails";
    private static final String FROM       = "noreply@mail.devvault.de";

    @Value("${resend.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public EmailService(RestTemplateBuilder builder) {
        this.restTemplate = builder
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(15))
                .build();
    }

    @Override
    @Async
    public void send(String to, String html, String subject) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = Map.of(
                "from",    FROM,
                "to",      List.of(to),
                "subject", subject,
                "html",    html
        );

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    RESEND_URL, new HttpEntity<>(body, headers), String.class);
            log.info("Email sent to {} — status {}", to, response.getStatusCode());
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new IllegalStateException("Failed to send email", e);
        }
    }
}
