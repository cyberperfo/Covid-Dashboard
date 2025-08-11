package org.example.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.io.image.ImageDataFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RaporOlusturucu {

    public static void pdfRaporOlustur(String ulkeAdi, int gunlukVaka, int kritikEsik) {
        pdfRaporOlustur(ulkeAdi, gunlukVaka, kritikEsik, null); // Grafik olmadan çalıştır
    }

    public static void pdfRaporOlustur(String ulkeAdi, int gunlukVaka, int kritikEsik, String grafikDosyaYolu) {
        String dosyaAdi = "covid_logs/covid_rapor_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";

        try {
            File klasor = new File("covid_logs");
            if (!klasor.exists()) {
                klasor.mkdirs(); // Eğer klasör yoksa oluştur
            }

            File pdfDosyasi = new File(dosyaAdi);
            PdfWriter writer = new PdfWriter(pdfDosyasi);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("COVID-19 Günlük Rapor"));
            document.add(new Paragraph("Ülke: " + ulkeAdi));
            document.add(new Paragraph("Günlük Vaka Sayısı: " + gunlukVaka));
            document.add(new Paragraph("Kritik Eşik: " + kritikEsik));

            // Grafik dosyasını ekle (Eğer varsa)
            if (grafikDosyaYolu != null) {
                File grafikDosyasi = new File(grafikDosyaYolu);
                if (grafikDosyasi.exists()) {
                    Image grafikResmi = new Image(ImageDataFactory.create(grafikDosyaYolu));
                    document.add(grafikResmi);
                } else {
                    document.add(new Paragraph("⚠️ Grafik eklenemedi, dosya bulunamadı!"));
                }
            }

            document.close();
            System.out.println("✅ PDF raporu başarıyla oluşturuldu: " + dosyaAdi);
        } catch (IOException e) {
            System.err.println("⚠️ PDF raporu oluşturulamadı! Hata: " + e.getMessage());
        }
    }
}
