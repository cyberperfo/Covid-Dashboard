package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CovidApiService {

    private static final String API_URL = "https://disease.sh/v3/covid-19/countries/";
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    public CovidApiService() {
        this.restTemplate = new RestTemplate();
        this.mapper = new ObjectMapper();
    }

    public JsonNode getCovidStats(String ulkeAdi) {
        try {
            String tamUrl = API_URL + ulkeAdi;
            String jsonResponse = restTemplate.getForObject(tamUrl, String.class);
            return mapper.readTree(jsonResponse);
        } catch (Exception e) {
            System.err.println("API hatası: " + e.getMessage());
            return null;
        }
    }
}
