package org.example;

import org.example.service.CovidApiService;
import org.example.util.EpostaServisi;
import org.example.util.KayitDefteri;
import org.example.util.RaporOlusturucu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    public void testCovidApiService_getCovidStats() {
        CovidApiService service = new CovidApiService();
        assertNotNull(service.getCovidStats("turkey"), "API'den gelen veri null olmamalı");
    }

    @Test
    public void testEpostaServisi_uyariEpostasiGonder() {
        assertDoesNotThrow(() ->
                        EpostaServisi.uyariEpostasiGonder("test@example.com", "Türkiye", 15000, 10000),
                "E-posta gönderimi hata verdi"
        );
    }

    @Test
    public void testKayitDefteri_kayitEkle() {
        assertDoesNotThrow(() ->
                        KayitDefteri.kayitEkle("Türkiye", 15000, 10000),
                "Log yazımı başarısız"
        );
    }

    @Test
    public void testRaporOlusturucu_pdfRaporOlustur() {
        assertDoesNotThrow(() ->
                        RaporOlusturucu.pdfRaporOlustur("Türkiye", 15000, 10000),
                "PDF rapor oluşturulamadı"
        );
    }
}
