package com.sol.snappick.util.fin;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

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

    public static FinCommonHeader createHeader(String apiName) {
        FinCommonHeader header = new FinCommonHeader();
        LocalDateTime now = LocalDateTime.now();

        return header.setDynamicFields(apiName, now);
    }

    private static String generateUniqueNo() {
        LocalDateTime now = LocalDateTime.now();
        String dateTime = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        int sequenceNumber = sequence.getAndIncrement() % 1000000;
        return dateTime + String.format("%06d", sequenceNumber);
    }

    private FinCommonHeader setDynamicFields(String apiName, LocalDateTime now) {
        this.apiName = apiName;
        this.transmissionDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        this.transmissionTime = now.format(DateTimeFormatter.ofPattern("HHmmss"));
        this.apiServiceCode = apiName;
        this.institutionTransactionUniqueNo = generateUniqueNo();
        return this;
    }

}