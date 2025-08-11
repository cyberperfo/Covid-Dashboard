package org.example.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.category.DefaultCategoryDataset;
import com.fasterxml.jackson.databind.JsonNode;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;

public class GrafikOlusturucu {
    public static String gunlukVakaGrafigiOlustur(JsonNode vakaVerileri, String ulkeAdi) {
        DefaultCategoryDataset veriSeti = new DefaultCategoryDataset();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

        LocalDate oncekiTarih = null;
        int oncekiVaka = 0;

        Iterator<Map.Entry<String, JsonNode>> it = vakaVerileri.fields();
        while (it.hasNext()) {
            Map.Entry<String, JsonNode> entry = it.next();
            LocalDate tarih = LocalDate.parse(entry.getKey(), formatter);
            int toplamVaka = entry.getValue().asInt();

            // Günlük vaka hesaplaması yapılıyor (negatif ya da sıfırsa çizim yapılmaz)
            int gunlukVaka = (oncekiTarih == null) ? toplamVaka : toplamVaka - oncekiVaka;
            if (oncekiTarih != null && gunlukVaka > 0) {
                veriSeti.addValue(gunlukVaka, "Günlük Vakalar", tarih.format(DateTimeFormatter.ofPattern("dd MMM")));
            }

            // Önceki değerleri güncelle
            oncekiTarih = tarih;
            oncekiVaka = toplamVaka;
        }

        JFreeChart grafik = ChartFactory.createLineChart(
                ulkeAdi + " 2021 Günlük COVID-19 Vakaları",
                "Tarih",
                "Günlük Vaka Sayısı",
                veriSeti
        );

        // Y ekseni 0 - 1 milyon aralığında sınırlandırıldı
        NumberAxis yEkseni = (NumberAxis) grafik.getCategoryPlot().getRangeAxis();
        yEkseni.setRange(0, 1_000_000);

        // Grafik için Swing penceresi açılıyor
        JFrame pencere = new JFrame("Covid-19 Günlük Vakalar");
        pencere.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pencere.add(new ChartPanel(grafik));
        pencere.pack();
        pencere.setVisible(true);
        return ulkeAdi;
    }
}
