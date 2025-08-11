# Covid-19 İstatistik Dashboard

Bu proje, ülkelere göre günlük COVID-19 istatistiklerini analiz eder, kritik eşikler aşıldığında uyarı üretir ve günlük PDF raporları ile kayıt altına alır.

## 🛠️ Proje Özellikleri

- Disease.sh API ile ülke bazlı COVID-19 verisi çekilir.
- Kritik eşik aşımı tespit edilir.
- E-posta uyarısı gönderilir (mock veya gerçek SMTP ile).
- PDF rapor üretilir (iText ile).
- Günlük log dosyasına yazılır.
- Terminal tabanlı grafik çizimi yapılır.
- JUnit ile test edilmiştir.

## 🧪 Testler

`AppTest.java` içinde toplam 4 adet test yazılmıştır:

- API veri çekimi
- E-posta uyarı sistemi
- Loglama sistemi
- PDF rapor oluşturma

## 🚀 Projeyi Çalıştırmak

```bash
mvn clean install
mvn test
mvn spring-boot:run
```

## 📁 Proje Yapısı

- `src/main/java/` – Uygulama kodları
- `src/test/java/` – Unit testler
- `covid_logs/` – Loglar ve PDF raporlar
- `documents/` – Kullanım ve test dökümantasyonu
- `postman/` – API test koleksiyonu
- `reports/` – Rapor ekran çıktıları

## 📦 Bağımlılıklar

- Spring Boot
- iText
- Jackson
- JFreeChart
- JUnit 5
- Jakarta Mail

---

*Oluşturulma: 2025-05-14 12:22*
