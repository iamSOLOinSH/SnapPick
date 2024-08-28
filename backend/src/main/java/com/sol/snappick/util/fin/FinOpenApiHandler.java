package com.sol.snappick.util.fin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
//@Service
public class FinOpenApiHandler {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    @Value("${finopenapi.url}")
    private String BASE_URL;
    @Value("${finopenapi.apikey}")
    private String apiKey;

    public FinOpenApiHandler(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public JsonNode apiRequest(String url, HttpMethod httpMethod, Map<String, String> requestBody) {
        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HTTP 바디에 apiKey 추가
        requestBody.put("apiKey", apiKey);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_URL + url, httpMethod, requestEntity, String.class);

        return parseJsonResponse(responseEntity);
    }


    //////////////////////////////////////////////////////////////////////
    // ResponseEntity<String> response -> JsonNode
    @SneakyThrows
    public JsonNode parseJsonResponse(ResponseEntity<String> response) {
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalArgumentException();
        }

        return objectMapper.readTree(response.getBody());
    }

    // JsonNode + key -> value
    public String getValueByKey(JsonNode responseJson, String key) {
        return responseJson.get(key).asText();
    }

    // JsonNode에 있는 모든 값 목록 출력
    public void printJson(JsonNode responseJson) {
        System.out.println("=== request body ===");
        responseJson.fields().forEachRemaining(entry ->
                System.out.println(entry.getKey() + ": " + entry.getValue().asText()));
        System.out.println("====================");
    }

}
