package org.aadi.short_bulletin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LlmService {
    @Value("${llm.base-url}")
    private String baseUrl;
    @Value("${llm.model}")
    private String model;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public LlmService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String generateSummary(String text) {
        String prompt = "Extract top news from this newspaper text. Limit to top 20. For each: title (10-15 words), summary (30-40 words). Output as JSON array: [{'title': '...', 'summary': '...'}]. Text: " + text.substring(0, Math.min(text.length(), 8000));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = "{\"model\":\"" + model + "\", \"messages\":[{\"role\":\"user\",\"content\":\"" + prompt + "\"}], \"max_tokens\":2000}";
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, entity, String.class);
            return parseLlmResponse(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Failed to call LLM API: " + e.getMessage(), e);
        }
    }

    private String parseLlmResponse(String response) {
        try {
            // Assuming response contains JSON array in choices[0].message.content
            return objectMapper.readTree(response).get("choices").get(0).get("message").get("content").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse LLM response: " + e.getMessage(), e);
        }
    }
}