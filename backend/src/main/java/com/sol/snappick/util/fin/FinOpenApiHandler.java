package com.sol.snappick.util.fin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
    private final FinCommonHeader finCommonHeaderTemplate;

    @Value("${finopenapi.url}")
    private String BASE_URL;
    @Value("${finopenapi.apikey}")
    private String apiKey;

    public FinOpenApiHandler(RestTemplate restTemplate, ObjectMapper objectMapper, FinCommonHeader finCommonHeader) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.finCommonHeaderTemplate = finCommonHeader;
    }

    // 공통 헤더 필요없는 경우(사용자 계정 생성)
    public JsonNode apiRequest(String url, HttpMethod httpMethod, Map<String, Object> requestBody) {
        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HTTP 바디에 공통 apiKey 추가
        requestBody.put("apiKey", apiKey);

        // 요청보내고 응답받아오기
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_URL + url, httpMethod, requestEntity, String.class);

        return parseJsonResponse(responseEntity);
    }

    public JsonNode apiRequest(String url, String apiName, HttpMethod httpMethod, Map<String, Object> requestBody, String userKey) {
        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HTTP 바디에 공통 Header 추가
        String headerJson = null;
        JsonNode headerNode = null;
        try {
            headerJson = objectMapper.writeValueAsString(finCommonHeaderTemplate.createHeader(apiName));
            headerNode = objectMapper.readTree(headerJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // for debug TODO 나중에 삭제
        System.out.println("======header of request message=======");
        printJson(headerNode);
        System.out.println("======================================");

        // UserKey가 필요한 api라면
        if (userKey != null) {
            ((ObjectNode) headerNode).put("userKey", userKey);
        }

        requestBody.put("Header", headerNode);


        // 요청보내고 응답받아오기
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_URL + url, httpMethod, requestEntity, String.class);

        return parseJsonResponse(responseEntity);
    }


    //////////////////////////////////////////////////////////////////////
    // ResponseEntity<String> response -> JsonNode
    @SneakyThrows
    public JsonNode parseJsonResponse(ResponseEntity<String> response) {
        // 응답이 200번대가 아니면 에러
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
    public void printJson(JsonNode jsonNode) {
        System.out.println("======================================");
        System.out.println(jsonNode.toPrettyString());
        System.out.println("======================================");
    }

}
