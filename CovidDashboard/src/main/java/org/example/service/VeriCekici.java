package org.example.service;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class VeriCekici {
    private static final String API_BASE_URL = "https://disease.sh/v3/covid-19/historical/";
    private static final ObjectMapper jsonIsleyici = new ObjectMapper();
    private static final DateTimeFormatter tarihFormati = DateTimeFormatter.ofPattern("M/d/yy"); // Tarih formatı düzeltildi

    public static JsonNode ulkeninTarihselVerileriniGetir(String ulkeAdi, int yil) {
        try {
            // 2021 verilerini almak için tarih aralığı belirle
            LocalDate baslangicTarihi = LocalDate.of(yil, 01, 01);
            LocalDate bitisTarihi = LocalDate.of(yil, 12, 31);

            // API'ye istek gönder
            String tamUrl = API_BASE_URL + ulkeAdi + "?lastdays=all";
            Response yanit = Request.get(tamUrl)
                    .connectTimeout(Timeout.ofSeconds(15))
                    .execute();

            String jsonYaniti = yanit.returnContent().asString(StandardCharsets.UTF_8);
            JsonNode tumVeriler = jsonIsleyici.readTree(jsonYaniti);
            JsonNode tarihselVeriler = tumVeriler.path("timeline").path("cases");

            // Sadece istenen yıla ait verileri filtrele
            JsonNode filtreliVeriler = jsonIsleyici.createObjectNode();

            tarihselVeriler.fields().forEachRemaining(entry -> {
                try {
                    LocalDate veriTarihi = LocalDate.parse(entry.getKey(), tarihFormati);

                    if (!veriTarihi.isBefore(baslangicTarihi) && !veriTarihi.isAfter(bitisTarihi)) {
                        ((com.fasterxml.jackson.databind.node.ObjectNode) filtreliVeriler)
                                .put(veriTarihi.format(DateTimeFormatter.ISO_DATE), entry.getValue().asInt());
                    }
                } catch (DateTimeParseException e) {
                    System.err.println("Geçersiz tarih formatı: " + entry.getKey());
                }
            });

            return filtreliVeriler;
        } catch (IOException e) {
            System.err.println("API hatası: " + e.getMessage());
            return null;
        }
    }
}
