package com.email.writer.app.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.email.writer.app.DTO.EmailRequest;
import com.email.writer.app.Service.EmailGeneratorService;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/email")
@AllArgsConstructor
public class EmailGeneratorController {

    private final EmailGeneratorService emailGeneratorService;

    @PostMapping("/generate")
    public Mono<String> generateEmail(@RequestBody EmailRequest emailRequest) {
        return emailGeneratorService.generateEmailReply(emailRequest);
    }

    @PostMapping("/test-gemini")
    public ResponseEntity<String> testGemini() {

        Client client = Client.builder()
                .apiKey(System.getenv("GEMINI_KEY"))
                .build();
        
        System.out.println("Gemini Key = " + System.getenv("GEMINI_KEY"));

        GenerateContentResponse response = client.models.generateContent(
                "gemini-2.5-flash",
                "Explain how AI works in a few words",
                null);

        return ResponseEntity.ok(response.text());
    }
}