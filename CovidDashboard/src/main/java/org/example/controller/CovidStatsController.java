package org.example.controller;

import org.example.service.CovidApiService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/covid")
public class CovidStatsController {

    private final CovidApiService apiService;

    public CovidStatsController(CovidApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/stats/{ulkeAdi}")
    public JsonNode getCovidStats(@PathVariable String ulkeAdi) {
        return apiService.getCovidStats(ulkeAdi);
    }
}


