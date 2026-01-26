package com.email.writer.app.Service;

import org.springframework.stereotype.Service;
import com.email.writer.app.DTO.EmailRequest;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class EmailGeneratorService {

    private final Client client = Client.builder()
            .apiKey(System.getenv("AIzaSyCmPNUHGlS0fVZy4M2o3d0Civb-yJtqCaY"))
            .build();

    public Mono<String> generateEmailReply(EmailRequest emailRequest) {

        // build the prompt
        String prompt = buildPrompt(emailRequest);

        // call Gemini on a bounded elastic thread
        return Mono.fromCallable(() -> {
            // generate response
            GenerateContentResponse response = client.models.generateContent("gemini-2.5-flash", prompt, null);

            // extract text
            return response.text();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private String buildPrompt(EmailRequest emailRequest) {

        StringBuilder prompt = new StringBuilder();
        prompt.append(
                "Generate a professional email reply for the following email content. Please don't generate a subject line :\n");
        if (emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()) {
            prompt.append("Tone: ").append(emailRequest.getTone()).append("\n");
        }

        prompt.append("Original email: ").append(emailRequest.getEmailContent()).append("\n");

        return prompt.toString();
    }
}
