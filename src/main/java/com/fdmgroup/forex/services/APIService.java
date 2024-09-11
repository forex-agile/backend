package com.fdmgroup.forex.services;

import java.io.*;
import java.net.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class APIService {

    @Value("${fxrates.api.base.url}")
    private String fxAPIBaseURL;

    @Value("${fxrates.api.key}")
    private String fxAPIKey;

    @Value("${fxrates.api.usd.url}")
    private String fxApiUsdUrl;
    
    public APIService() {}
    
    public String getCurrencyCodes() throws IOException {
        String url = fxAPIBaseURL + fxAPIKey + "/codes";
        try {
            return fetchFromAPI(url);
        } catch (Exception e) {
            throw new IOException("Error fetching currencies from API: " + e.getMessage());
        }
    }

    public String getFxRates() throws IOException {
        try {
            return fetchFromAPI(fxApiUsdUrl);
        } catch (Exception e) {
            throw new IOException("Error fetching exchange rates from API: " + e.getMessage());
        }
    }

    private String fetchFromAPI(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            return processResponse(con);

        } else {
            throw new IOException();
        }
    }

    private String processResponse(HttpURLConnection con) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = input.readLine()) != null) {
            response.append(inputLine);
        }
        input.close();
        return response.toString();
    }

}
