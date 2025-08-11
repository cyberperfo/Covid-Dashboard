package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.service.VeriCekici;
import org.example.util.EpostaServisi;
import org.example.util.GrafikOlusturucu;
import org.example.util.KayitDefteri;
import org.example.util.RaporOlusturucu;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner okuyucu = new Scanner(System.in)) {
            System.out.println("COVID-19 İstatistik Dashboard");
            System.out.println("=============================");

            // Ülke adı al
            System.out.print("İncelemek istediğiniz ülke adını girin (Örnek: Turkey): ");
            String ulkeAdi = okuyucu.nextLine().trim();
            if (ulkeAdi.isEmpty()) {
                throw new IllegalArgumentException("Ülke adı boş olamaz!");
            }

            // Tarih al
            LocalDate secilenTarih = null;
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while (secilenTarih == null) {
                try {
                    System.out.print("Analiz etmek istediğiniz tarihi girin veriler 2021 yılına aittir(2021-MM-DD): ");
                    String tarihStr = okuyucu.nextLine().trim();
                    secilenTarih = LocalDate.parse(tarihStr, format);
                } catch (DateTimeParseException e) {
                    System.out.println("Geçersiz tarih formatı! Lütfen YYYY-MM-DD formatında tekrar girin.");
                }
            }

            // Kritik eşik değeri al
            int kritikEsik = 0;
            while (true) {
                try {
                    System.out.print("Kritik eşik değerini girin: ");
                    kritikEsik = Integer.parseInt(okuyucu.nextLine().trim());
                    if (kritikEsik <= 0) {
                        System.out.println("Kritik eşik değeri pozitif olmalıdır!");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Lütfen geçerli bir sayı girin!");
                }
            }

            // Alıcı e-posta adresi al
            String aliciEposta;
            do {
                System.out.print("Uyarılar için e-posta adresi girin: ");
                aliciEposta = okuyucu.nextLine().trim();
                if (!aliciEposta.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    System.out.println("Geçersiz e-posta formatı! Tekrar deneyin.");
                }
            } while (!aliciEposta.matches("^[A-Za-z0-9+_.-]+@(.+)$"));

            // Veri çekme ve analiz
            System.out.println("\n" + ulkeAdi + " için 2021 verileri çekiliyor...");
            JsonNode vakaVerileri = VeriCekici.ulkeninTarihselVerileriniGetir(ulkeAdi, 2021);

            // Veri bulunmazsa programı sonlandır!
            if (vakaVerileri == null || !vakaVerileri.has(secilenTarih.toString())) {
                System.out.println("\n⚠️ Hata: " + ulkeAdi + " için " + secilenTarih + " tarihinde veri bulunamadı!");
                return;
            }

            int bugununVakaSayisi = vakaVerileri.get(secilenTarih.toString()).asInt();
            LocalDate oncekiGun = secilenTarih.minusDays(1);

            int gunlukVaka = bugununVakaSayisi;
            if (vakaVerileri.has(oncekiGun.toString())) {
                int oncekiGunVakaSayisi = vakaVerileri.get(oncekiGun.toString()).asInt();
                gunlukVaka = bugununVakaSayisi - oncekiGunVakaSayisi;
            }

            System.out.println("\nSeçilen tarih (" + secilenTarih + ") için **günlük vaka sayısı**: " + gunlukVaka);

            // Eğer günlük vaka kritik eşik değerini aşarsa, e-posta gönder!
            if (gunlukVaka > kritikEsik) {
                System.out.println("\n⚠️ UYARI: Günlük vaka sayısı kritik eşiği aştı!");
                EpostaServisi.uyariEpostasiGonder(aliciEposta, ulkeAdi, gunlukVaka, kritikEsik);
            } else {
                System.out.println("\n✅ Bilgi: Günlük vaka sayısı kritik eşiğin altında.");
            }

            // **Grafik oluştur**
            String grafikDosyaYolu = GrafikOlusturucu.gunlukVakaGrafigiOlustur(vakaVerileri, ulkeAdi);

            // **Kayıt defterine veri ekle**
            KayitDefteri.kayitEkle(ulkeAdi, gunlukVaka, kritikEsik);

            // **PDF rapor oluştur**
            RaporOlusturucu.pdfRaporOlustur(ulkeAdi, gunlukVaka, kritikEsik, grafikDosyaYolu);

        } catch (Exception e) {
            System.err.println("⚠️ Beklenmeyen bir hata oluştu: " + e.getMessage());
            e.printStackTrace(); // Daha detaylı hata mesajı görmek için!
        }
    }
}
