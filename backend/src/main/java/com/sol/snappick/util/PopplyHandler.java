package com.sol.snappick.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sol.snappick.store.dto.storeAPI.StoreAPIRes;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class PopplyHandler {
    private final String BASE_URL = "https://api.popply.co.kr/api/store/";

    public StoreAPIRes searchStore() throws IOException, InterruptedException {
        String fromDate = LocalDate.now().toString();
        System.out.println("fromDate = " + fromDate);
        String toDate = LocalDate.now().toString();
        System.out.println("toDate = " + toDate);

        String search_url = BASE_URL + "?query=&status=all&area=all&fromDate=2024-08-22&toDate=2024-08-22";

        URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                                      .queryParam("query", "")
                                      .queryParam("status", "all")
                                      .queryParam("area", "all")
//                                      .queryParam("fromDate", fromDate)
//                                      .queryParam("toDate", toDate)
                                      .queryParam("fromDate", "2024-08-22")
                                      .queryParam("toDate", "2024-08-22")
                                      .build()
                                      .toUri();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            StoreAPIRes originResponse = mapper.readValue(response.body(), StoreAPIRes.class);
            if(originResponse.getIsSuccess())
                return originResponse;
            else
                return null;
        }
        return null;
    }
}
