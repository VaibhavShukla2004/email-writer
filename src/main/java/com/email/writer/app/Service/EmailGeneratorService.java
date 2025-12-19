package com.email.writer.app.Service;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.email.writer.app.DTO.EmailRequest;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import reactor.core.publisher.Mono;

@Service
public class EmailGeneratorService {

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public Mono<@Nullable String> generateEmailReply(EmailRequest emailRequest) {

        // Build the prompt
        String prompt = buildPrompt(emailRequest);

        // verify environment variables
        System.out.println("URL = " + geminiApiUrl);
        System.out.println("KEY = " + geminiApiKey);

        // build Gemini client
        Client client = Client.builder()
                .apiKey(System.getenv("GEMINI_KEY"))
                .build();

        //get Response from Gemini
        GenerateContentResponse response = client.models.generateContent(
                "gemini-2.5-flash",
                prompt,
                null);                                       

        // return reply from Gemini
        return Mono.just(response.text());
    }

    public Mono<String> extractText(GenerateContentResponse response) {
        String content = response.text();
        return Mono.just(content);
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
