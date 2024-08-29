package com.sol.snappick.util.fin;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

// 정적 팩토리 메서드 패턴
@Getter
@Component
public class FinCommonHeader {

    private static final AtomicInteger sequence = new AtomicInteger(0);
    private final String institutionCode = "00100";
    private final String fintechAppNo = "001";
    @Value("${finopenapi.apikey}")
    private String apiKey;
    private String apiName;
    private String transmissionDate;
    private String transmissionTime;
    private String apiServiceCode;
    private String institutionTransactionUniqueNo;

    private FinCommonHeader() {
    }

    private FinCommonHeader(String apiName, ZonedDateTime now, String apiKey) {
        this.apiKey = apiKey;
        this.apiName = apiName;
        this.transmissionDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        this.transmissionTime = now.format(DateTimeFormatter.ofPattern("HHmmss"));
        this.apiServiceCode = apiName;
        this.institutionTransactionUniqueNo = generateUniqueNo(transmissionDate + transmissionTime);
    }

    private static String generateUniqueNo(String now) {
        int sequenceNumber = sequence.getAndIncrement() % 1000000;
        return now + String.format("%06d", sequenceNumber);
    }


    // 팩토리 메서드
    public FinCommonHeader createHeader(String apiName) {
        LocalDateTime now = LocalDateTime.now();
        ZoneId koreaZoneId = ZoneId.of("Asia/Seoul");
        ZonedDateTime koreaTime = now.atZone(ZoneId.systemDefault()).withZoneSameInstant(koreaZoneId);

        return new FinCommonHeader(apiName, koreaTime, this.apiKey);
    }


}