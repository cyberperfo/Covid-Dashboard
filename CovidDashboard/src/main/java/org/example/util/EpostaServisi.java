package org.example.util;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EpostaServisi {

    private static final String SMTP_SUNUCU = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SMTP_KULLANICI = "yusufsayilgan31@gmail.com";
    private static final String SMTP_SIFRE = "ziln stpp quav onhc";

    public static void uyariEpostasiGonder(String aliciEposta, String ulkeAdi, int vakaSayisi, int kritikEsik) {
        // SMTP ayarları
        Properties ozellikler = new Properties();
        ozellikler.put("mail.smtp.auth", "true");
        ozellikler.put("mail.smtp.starttls.enable", "true");
        ozellikler.put("mail.smtp.host", SMTP_SUNUCU);
        ozellikler.put("mail.smtp.port", SMTP_PORT);
        ozellikler.put("mail.smtp.ssl.protocols", "TLSv1.2");
        ozellikler.put("mail.smtp.ssl.trust", SMTP_SUNUCU);

        // Oturum oluştur
        Session oturum = Session.getInstance(ozellikler, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_KULLANICI, SMTP_SIFRE);
            }
        });

        try {
            // E-posta mesajını oluştur
            Message mesaj = new MimeMessage(oturum);
            mesaj.setFrom(new InternetAddress(SMTP_KULLANICI));
            mesaj.setRecipients(Message.RecipientType.TO, InternetAddress.parse(aliciEposta));
            mesaj.setSubject("COVID-19 Kritik Uyarı: " + ulkeAdi);

            String icerik = String.format("DİKKAT! %s için günlük vaka sayısı %d olarak bildirilmiştir.Bu değer, belirlediğiniz kritik eşik olan %d değerini aşmıştır.Sağlık Bakanlığı önerilerine uymayı unutmayınız.", ulkeAdi, vakaSayisi, kritikEsik);

            mesaj.setText(icerik);

            // Gönder
            Transport.send(mesaj);
            System.out.println("Uyarı e-postası gönderildi: " + aliciEposta);

        } catch (MessagingException e) {
            System.err.println("E-posta gönderilemedi: " + e.getMessage());
        }
    }
}