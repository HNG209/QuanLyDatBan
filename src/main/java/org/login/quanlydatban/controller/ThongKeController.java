package org.login.quanlydatban.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class ThongKeController {
    @FXML
    private Label doanhThuTrongNgay;

    @FXML
    private Label soHDTrongNgay;

    @FXML
    private Label tongDoanhThu;

    @FXML
    private Label tongHoaDon;

    @FXML
    private ComboBox<String> tieuChiThongKe;

    @FXML
    private ComboBox<String> namThongKe;

    @FXML
    private LineChart<String, Double> bieuDoDuongDoanhThu;

    @FXML
    private LineChart<String, Integer> bieuDoDuongHoaDon;

    @FXML
    private PieChart bieuDoTronMonAn;

    @FXML
    public void initialize() {
        // Listener for doanh thu ComboBox
        tieuChiThongKe.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Theo tháng".equals(newValue) || "Theo quý".equals(newValue)) {
                namThongKe.setVisible(true);
            } else {
                namThongKe.setVisible(false);
            }
        });
        loadDataForToday();
        loadLineChartData();  // Change this to loadLineChartData
        loadPieChartData();
    }

    private void loadDataForToday() {
        // Simulated data; in a real application, replace this with actual data fetching logic
        double todayRevenue = 150000; // Example revenue for today
        int todayInvoiceCount = 10; // Example invoice count for today
        double totalRevenue = 5000000; // Example total revenue
        int totalInvoiceCount = 300; // Example total invoice count

        // Update labels with data
        doanhThuTrongNgay.setText(todayRevenue + " VND");
        soHDTrongNgay.setText(String.valueOf(todayInvoiceCount));
        tongDoanhThu.setText(totalRevenue + " VND");
        tongHoaDon.setText(String.valueOf(totalInvoiceCount));
    }

    private void loadLineChartData() {
        // Revenue Data
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        double[] revenueValues2022 = {120000, 150000, 180000, 200000, 220000, 250000, 270000, 300000, 320000, 350000, 370000, 400000};
        double[] revenueValues2023 = {130000, 160000, 190000, 210000, 230000, 260000, 280000, 310000, 330000, 360000, 380000, 410000};

        // Create series for revenue
        XYChart.Series<String, Double> revenueSeries2022 = new XYChart.Series<>();
        revenueSeries2022.setName("2022");
        for (int i = 0; i < months.length; i++) {
            revenueSeries2022.getData().add(new XYChart.Data<>(months[i] + " 2022", revenueValues2022[i]));
        }

        XYChart.Series<String, Double> revenueSeries2023 = new XYChart.Series<>();
        revenueSeries2023.setName("2023");
        for (int i = 0; i < months.length; i++) {
            revenueSeries2023.getData().add(new XYChart.Data<>(months[i] + " 2023", revenueValues2023[i]));
        }

        bieuDoDuongDoanhThu.getData().addAll(revenueSeries2022, revenueSeries2023); // Add both series to the chart

        // Invoice Data
        int[] invoiceValues2022 = {8, 12, 15, 20, 18, 22, 30, 35, 28, 25, 30, 40};
        int[] invoiceValues2023 = {10, 14, 17, 23, 19, 25, 32, 37, 29, 28, 32, 45};

        // Create series for invoices
        XYChart.Series<String, Integer> invoiceSeries2022 = new XYChart.Series<>();
        invoiceSeries2022.setName("2022");
        for (int i = 0; i < months.length; i++) {
            invoiceSeries2022.getData().add(new XYChart.Data<>(months[i] + " 2022", invoiceValues2022[i]));
        }

        XYChart.Series<String, Integer> invoiceSeries2023 = new XYChart.Series<>();
        invoiceSeries2023.setName("2023");
        for (int i = 0; i < months.length; i++) {
            invoiceSeries2023.getData().add(new XYChart.Data<>(months[i] + " 2023", invoiceValues2023[i]));
        }

        bieuDoDuongHoaDon.getData().addAll(invoiceSeries2022, invoiceSeries2023); // Add both series to the chart
    }

    private void loadPieChartData() {
        PieChart.Data slice1 = new PieChart.Data("Món A", 25);
        PieChart.Data slice2 = new PieChart.Data("Món B", 30);
        PieChart.Data slice3 = new PieChart.Data("Món C", 20);
        PieChart.Data slice4 = new PieChart.Data("Món D", 15);
        PieChart.Data slice5 = new PieChart.Data("Món E", 10);

        bieuDoTronMonAn.getData().addAll(slice1, slice2, slice3, slice4, slice5);
    }
}
