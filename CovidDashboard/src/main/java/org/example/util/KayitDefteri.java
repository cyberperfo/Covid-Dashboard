package org.example.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class KayitDefteri {

    private static final String LOG_KLASORU = "covid_logs";
    private static final DateTimeFormatter TARIH_FORMATI = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static void kayitEkle(String ulkeAdi, int vakaSayisi, int kritikEsik) {
        // Klasör oluştur (eğer yoksa)
        File klasor = new File(LOG_KLASORU);
        if (!klasor.exists() && !klasor.mkdirs()) {
            System.err.println("⚠️ Kayıt klasörü oluşturulamadı!");
            return;
        }

        // Log dosyasını oluştur
        String dosyaAdi = LOG_KLASORU + "/covid_kayit_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".log";
        File logDosyasi = new File(dosyaAdi);

        try {
            // Dosya yoksa oluştur
            if (!logDosyasi.exists() && !logDosyasi.createNewFile()) {
                System.err.println("⚠️ Log dosyası oluşturulamadı!");
                return;
            }

            // Log mesajını yaz
            try (BufferedWriter yazici = new BufferedWriter(new FileWriter(logDosyasi, true))) {
                String zamanDamgasi = LocalDateTime.now().format(TARIH_FORMATI);
                String kayitMesaji = String.format("[%s] %s - Günlük Vaka: %d, Kritik Eşik: %d",
                        zamanDamgasi, ulkeAdi, vakaSayisi, kritikEsik);

                yazici.write(kayitMesaji);
                yazici.newLine();

                System.out.println("✅ Log kaydı başarıyla eklendi: " + kayitMesaji);
            }
        } catch (IOException e) {
            System.err.println("⚠️ Log kaydı yazılamadı! Hata: " + e.getMessage());
            e.printStackTrace(); // Daha fazla hata detayını göster
        }
    }
}
