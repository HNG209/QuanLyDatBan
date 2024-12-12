package org.login.quanlydatban.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.util.converter.LocalDateStringConverter;
import org.login.quanlydatban.dao.HoaDonDAO;
import org.login.quanlydatban.entity.TaiKhoan;
import org.login.quanlydatban.entity.enums.TrangThaiHoaDon;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class ThongKeQuanLyController {
    @FXML
    private Label doanhThuTrongNgay;

    @FXML
    private Label soHDTrongNgay;

    @FXML
    private Label tongDoanhThu;

    @FXML
    private Label tongHoaDon;

    @FXML
    private ComboBox<String> tieuChiThongKeBieuDoCot;

    @FXML
    private ComboBox<String> namThongKeBieuDoCot;

    @FXML
    private ComboBox<String> namThongKeBieuDoTron;
    @FXML
    private ComboBox<String> quyThongKeBieuDoTron;
    @FXML
    private ComboBox<String> thangThongKeBieuDoTron;
    @FXML
    private BarChart<String, Number> bieuDoCotDoanhThu; // Thay đổi tên thành bieuDoCotDoanhThu

    @FXML
    private DatePicker chooseDate;


    @FXML
    private BarChart<String, Number> bieuDoCotHoaDon;

    @FXML
    private PieChart bieuDoTronMonAn;
    @FXML
    private PieChart bieuDoTronTrangThaiHoaDon;
    private HoaDonDAO hoaDonDAO;
    private ObservableList<Object[]> bxh = FXCollections.observableArrayList();
    private TaiKhoan taiKhoan;
    private final DecimalFormat df = new DecimalFormat("#,### VND");
    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    @FXML
    public void initialize() {
        hoaDonDAO = new HoaDonDAO();
        capNhatComboBoxNamThongKe();
        tieuChiThongKeBieuDoCot.getSelectionModel().select("Theo tháng");
        namThongKeBieuDoCot.getSelectionModel().select(0);
        namThongKeBieuDoTron.getSelectionModel().select(0);
        capNhatDuLieuDoanhThuVaHoaDon();
        capNhatDuLieuChoBieuDoCot();
        capNhatDuLieuThongKeLoaiMonAn();
        capNhatDuLieuThongKeTrangThaiHoaDon();

        tieuChiThongKeBieuDoCot.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Theo tháng".equals(newValue) || "Theo quý".equals(newValue)) {
                namThongKeBieuDoCot.setVisible(true);
            } else {
                namThongKeBieuDoCot.setVisible(false);
            }
            capNhatDuLieuChoBieuDoCot();
        });

        namThongKeBieuDoCot.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            capNhatDuLieuChoBieuDoCot();
        });

        if ("Tất cả".equals(namThongKeBieuDoTron.getSelectionModel().getSelectedItem())) {
            quyThongKeBieuDoTron.setVisible(false);
            thangThongKeBieuDoTron.setVisible(false);
            thangThongKeBieuDoTron.getSelectionModel().select(0);
            quyThongKeBieuDoTron.getSelectionModel().select(0);
        } else {
            quyThongKeBieuDoTron.setVisible(true);
            thangThongKeBieuDoTron.setVisible(true);
        }

        namThongKeBieuDoTron.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Tất cả".equals(newValue)) {
                quyThongKeBieuDoTron.setVisible(false);
                thangThongKeBieuDoTron.setVisible(false);
                thangThongKeBieuDoTron.getSelectionModel().select(0);
                quyThongKeBieuDoTron.getSelectionModel().select(0);
            } else {
                quyThongKeBieuDoTron.setVisible(true);
                thangThongKeBieuDoTron.setVisible(true);
            }
            chooseDate.setValue(null); // Reset DatePicker khi chọn năm
            capNhatDuLieuThongKeTrangThaiHoaDon();
            capNhatDuLieuThongKeLoaiMonAn();
        });

        thangThongKeBieuDoTron.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            quyThongKeBieuDoTron.getSelectionModel().select(0);
            thangThongKeBieuDoTron.getSelectionModel().select(newValue);
            capNhatDuLieuThongKeLoaiMonAn();
            capNhatDuLieuThongKeTrangThaiHoaDon();
        });

        quyThongKeBieuDoTron.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            thangThongKeBieuDoTron.getSelectionModel().select(0);
            quyThongKeBieuDoTron.getSelectionModel().select(newValue);
            capNhatDuLieuThongKeTrangThaiHoaDon();
            capNhatDuLieuThongKeLoaiMonAn();
        });
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        chooseDate.setConverter(new LocalDateStringConverter(formatter, formatter));
        chooseDate.setPromptText("dd/MM/yyyy");
        chooseDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                namThongKeBieuDoTron.getSelectionModel().select("Tất cả"); // Reset năm về "Tất cả"
                capNhatDuLieuThongKeTrangThaiHoaDon();
                capNhatDuLieuThongKeLoaiMonAn();
            }
        });
    }


    private void capNhatDuLieuDoanhThuVaHoaDon() {
        Object[] tongDoanhThuVaHoaDon = hoaDonDAO.layDoanhThuVaSoHoaDon(null, null);
        Object[] tongDoanhThuVaHoaDonTheoNgay = hoaDonDAO.layDoanhThuVaSoHoaDon(null, LocalDate.now());
        if (tongDoanhThuVaHoaDonTheoNgay != null && tongDoanhThuVaHoaDonTheoNgay.length >= 2) {
            doanhThuTrongNgay.setText(tongDoanhThuVaHoaDonTheoNgay[0] == null ? df.format(0): df.format(tongDoanhThuVaHoaDonTheoNgay[0]));
            soHDTrongNgay.setText(String.valueOf(tongDoanhThuVaHoaDonTheoNgay[1]));
        } else {
            doanhThuTrongNgay.setText(df.format(0));
            soHDTrongNgay.setText("0");
        }
        if (tongDoanhThuVaHoaDon[0] != null && tongDoanhThuVaHoaDon[1] != null) {
            System.out.println(tongDoanhThuVaHoaDon[0]);
            tongDoanhThu.setText(df.format(Double.parseDouble(tongDoanhThuVaHoaDon[0].toString())));
            tongHoaDon.setText(String.valueOf(tongDoanhThuVaHoaDon[1]));
        } else {
            tongDoanhThu.setText(df.format(0));
            tongHoaDon.setText("0");
        }
    }

    private void capNhatDuLieuDoanhThuTheoThangHoacQuy(int nam, String donVi) {
        List<Object[]> doanhThuVaHoaDon;
        if ("Tháng".equals(donVi)) {
            doanhThuVaHoaDon = hoaDonDAO.layDoanhThuVaSoHoaDonTheoThang(null, nam);
        } else if ("Quý".equals(donVi)) {
            doanhThuVaHoaDon = hoaDonDAO.layDoanhThuVaSoHoaDonTheoQuy(null, nam);
        } else {
            return;
        }
        int soPhanTu = "Tháng".equals(donVi) ? 12 : 4;
        Number[] doanhThuTheoThoiGian = new Number[soPhanTu];
        Arrays.fill(doanhThuTheoThoiGian, 0);
        for (Object[] data : doanhThuVaHoaDon) {
            int index = ((Number) data[0]).intValue() - ("Tháng".equals(donVi) ? 1 : 1);
            doanhThuTheoThoiGian[index] = (Number) data[1]; // Lấy doanh thu
        }
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu theo " + donVi);
        for (int i = 0; i < doanhThuTheoThoiGian.length; i++) {
            series.getData().add(new XYChart.Data<>(donVi + " " + (i + 1), doanhThuTheoThoiGian[i]));
        }
        bieuDoCotDoanhThu.getData().clear();
        bieuDoCotDoanhThu.getData().add(series);
    }

    private void capNhatDuLieuHoaDonTheoThangHoacQuy(int nam, String donVi) {
        List<Object[]> doanhThuVaHoaDon;
        if ("Tháng".equals(donVi)) {
            doanhThuVaHoaDon = hoaDonDAO.layDoanhThuVaSoHoaDonTheoThang(null, nam);
        } else if ("Quý".equals(donVi)) {
            doanhThuVaHoaDon = hoaDonDAO.layDoanhThuVaSoHoaDonTheoQuy(null, nam);
        } else {
            return;
        }
        int soPhanTu = "Tháng".equals(donVi) ? 12 : 4;
        long[] hoaDonTheoThoiGian = new long[soPhanTu];
        Arrays.fill(hoaDonTheoThoiGian, 0);
        for (Object[] data : doanhThuVaHoaDon) {
            int index = ((Number) data[0]).intValue() - ("Tháng".equals(donVi) ? 1 : 1);
            hoaDonTheoThoiGian[index] = (long) data[2];
        }
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Số hóa đơn theo " + donVi);
        for (int i = 0; i < hoaDonTheoThoiGian.length; i++) {
            series.getData().add(new XYChart.Data<>(donVi + " " + (i + 1), hoaDonTheoThoiGian[i]));
        }
        bieuDoCotHoaDon.getData().clear();
        bieuDoCotHoaDon.getData().add(series);
        NumberAxis yAxis = (NumberAxis) bieuDoCotHoaDon.getYAxis();
        yAxis.setTickUnit(1);
        yAxis.setMinorTickVisible(false);
        long maxSoHoaDon = Arrays.stream(hoaDonTheoThoiGian).max().orElse(0);
        yAxis.setUpperBound(maxSoHoaDon + 1);
        yAxis.setAutoRanging(false);
    }

    private void capNhatDuLieuHoaDonTheoNam(String maNV) {
        List<Object[]> hoaDonVaSoHoaDon = hoaDonDAO.layDoanhThuVaSoHoaDonTheoNam(maNV);
        long[] soHoaDonTheoNam = new long[hoaDonVaSoHoaDon.size()];
        for (int i = 0; i < hoaDonVaSoHoaDon.size(); i++) {
            soHoaDonTheoNam[i] = (long) hoaDonVaSoHoaDon.get(i)[2];
        }
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < hoaDonVaSoHoaDon.size(); i++) {
            series.getData().add(new XYChart.Data<>(String.valueOf(hoaDonVaSoHoaDon.get(i)[0]), soHoaDonTheoNam[i]));
        }
        bieuDoCotHoaDon.getData().clear();
        bieuDoCotHoaDon.getData().add(series);
        NumberAxis yAxis = (NumberAxis) bieuDoCotHoaDon.getYAxis();
        yAxis.setTickUnit(1);
        yAxis.setMinorTickVisible(false);
        long maxSoHoaDon = Arrays.stream(soHoaDonTheoNam).max().orElse(0);
        yAxis.setUpperBound(maxSoHoaDon + 1);
        yAxis.setAutoRanging(false);
    }

    private void capNhatDuLieuDoanhThuTheoNam(String maNV) {
        List<Object[]> hoaDonVaSoHoaDon = hoaDonDAO.layDoanhThuVaSoHoaDonTheoNam(maNV);
        Double[] soHoaDonTheoNam = new Double[hoaDonVaSoHoaDon.size()];
        for (int i = 0; i < hoaDonVaSoHoaDon.size(); i++) {
            soHoaDonTheoNam[i] = (Double) hoaDonVaSoHoaDon.get(i)[1];
        }
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < hoaDonVaSoHoaDon.size(); i++) {
            series.getData().add(new XYChart.Data<>(String.valueOf(hoaDonVaSoHoaDon.get(i)[0]), soHoaDonTheoNam[i]));
        }
        bieuDoCotDoanhThu.getData().clear();
        bieuDoCotDoanhThu.getData().add(series);


    }

    private void capNhatDuLieuChoBieuDoCot() {
        String tieuChi = tieuChiThongKeBieuDoCot.getSelectionModel().getSelectedItem();
        int nam = 0;
        try {
            nam = Integer.parseInt(namThongKeBieuDoCot.getSelectionModel().getSelectedItem());
        }
        catch (NumberFormatException e){
            nam = 0;
        }
        if ("Theo tháng".equals(tieuChi)) {
            capNhatDuLieuDoanhThuTheoThangHoacQuy(nam, "Tháng");
            capNhatDuLieuHoaDonTheoThangHoacQuy(nam, "Tháng");
        } else if ("Theo quý".equals(tieuChi)) {
            capNhatDuLieuDoanhThuTheoThangHoacQuy(nam, "Quý");
            capNhatDuLieuHoaDonTheoThangHoacQuy(nam, "Quý");
        } else if ("Theo năm".equals(tieuChi)) {
            capNhatDuLieuDoanhThuTheoNam(null);
            capNhatDuLieuHoaDonTheoNam(null);
        }
        bieuDoCotHoaDon.setAnimated(false);
        bieuDoCotDoanhThu.setAnimated(false);
        NumberAxis yAxis = (NumberAxis) bieuDoCotDoanhThu.getYAxis();
        yAxis.setLowerBound(0);
        yAxis.setMinorTickVisible(false);
        bieuDoCotHoaDon.setLegendVisible(false);
        bieuDoCotDoanhThu.setLegendVisible(false);
        bieuDoCotDoanhThu.setHorizontalGridLinesVisible(false);
        bieuDoCotDoanhThu.setVerticalGridLinesVisible(false);
        bieuDoCotHoaDon.setHorizontalGridLinesVisible(false);
        bieuDoCotHoaDon.setVerticalGridLinesVisible(false);
    }

    private void capNhatDuLieuThongKeLoaiMonAn() {
        List<Object[]> dsMonAn;
        LocalDate ngay;
        int nam = 0, quy = 0, thang = 0;
        try {
            nam = Integer.parseInt(namThongKeBieuDoTron.getSelectionModel().getSelectedItem());
        } catch (NumberFormatException e) {
            nam = 0;
        }
        try {
            thang = Integer.parseInt(thangThongKeBieuDoTron.getSelectionModel().getSelectedItem());
        } catch (NumberFormatException e) {
            thang = 0;
        }
        try {
            quy = Integer.parseInt(quyThongKeBieuDoTron.getSelectionModel().getSelectedItem());
        } catch (NumberFormatException e) {
            quy = 0;
        }
        try {
            ngay = chooseDate.getValue();
        } catch (Exception e) {
            ngay = null;
        }
        if(ngay != null) {
            dsMonAn = hoaDonDAO.layDoanhThuLoaiMonAnTheoNgay(null, ngay);
        } else {
            dsMonAn = hoaDonDAO.layDoanhThuTheoLoaiMonAn(nam, quy, thang);
        }

        bieuDoTronMonAn.getData().clear();
        double tongDoanhThu = 0;
        for (Object[] loaiMonAnData : dsMonAn) {
            Number doanhThu = (Number) loaiMonAnData[1];
            tongDoanhThu += doanhThu.doubleValue();
        }

        for (Object[] loaiMonAnData : dsMonAn) {
            String tenMonAn = (String) loaiMonAnData[0];
            Number doanhThu = (Number) loaiMonAnData[1];
            double phanTram = (tongDoanhThu > 0) ? (doanhThu.doubleValue() / tongDoanhThu) * 100 : 0;
            PieChart.Data slice = new PieChart.Data(tenMonAn, doanhThu.doubleValue());
            bieuDoTronMonAn.getData().add(slice);
            slice.getNode().setStyle("-fx-border-color: transparent;");
            Tooltip tooltip = new Tooltip();
            slice.getNode().setOnMouseEntered(event -> {
                tooltip.setText(tenMonAn + " (" + String.format("%.1f", phanTram) + "%)");
                tooltip.show(slice.getNode(), event.getScreenX(), event.getScreenY() + 10);
            });

            slice.getNode().setOnMouseExited(event -> {
                tooltip.hide();
            });
        }
        bieuDoTronMonAn.setLabelsVisible(false);
    }
    private void capNhatDuLieuThongKeTrangThaiHoaDon() {
        int nam = 0, quy = 0, thang = 0;
        LocalDate ngay = null;

        try {
            nam = Integer.parseInt(namThongKeBieuDoTron.getSelectionModel().getSelectedItem());
        } catch (NumberFormatException e) {
            nam = 0;
        }
        try {
            thang = Integer.parseInt(thangThongKeBieuDoTron.getSelectionModel().getSelectedItem());
        } catch (NumberFormatException e) {
            thang = 0;
        }
        try {
            quy = Integer.parseInt(quyThongKeBieuDoTron.getSelectionModel().getSelectedItem());
        } catch (NumberFormatException e) {
            quy = 0;
        }

        try {
            ngay = chooseDate.getValue();
        } catch (Exception e) {
            ngay = null;
        }

        List<Object[]> dsTrangThai;

        if (ngay != null) {
            dsTrangThai = hoaDonDAO.laySoHoaDonTheoTrangThaiVaNgay(null, ngay);
        } else {
            dsTrangThai = hoaDonDAO.laySoHoaDonTheoTrangThaiHoaDon(null, nam, quy, thang);
        }

        bieuDoTronTrangThaiHoaDon.getData().clear();

        int soLuongHD = 0;
        for (Object[] trangThai : dsTrangThai) {
            Number soHoaDon = (Number) trangThai[1];
            soLuongHD += soHoaDon.intValue();
        }

        for (Object[] hoaDon : dsTrangThai) {
            String tenTrangThai = hoaDon[0].toString();
            Number soHoaDon = (Number) hoaDon[1];
            double phanTram = (soLuongHD > 0) ? (soHoaDon.doubleValue() / soLuongHD) * 100 : 0;
            String tt;

            if (tenTrangThai.equalsIgnoreCase(TrangThaiHoaDon.DA_THANH_TOAN.toString())) {
                tt = "Đã thanh toán";
            } else if (tenTrangThai.equalsIgnoreCase(TrangThaiHoaDon.CHUA_THANH_TOAN.toString())) {
                tt = "Chưa thanh toán";
            } else if (tenTrangThai.equalsIgnoreCase(TrangThaiHoaDon.DA_HUY.toString())) {
                tt = "Đã hủy";
            } else {
                tt = "Đã đặt";
            }
            PieChart.Data slice = new PieChart.Data(tt, soHoaDon.doubleValue());

            bieuDoTronTrangThaiHoaDon.getData().add(slice);
            slice.getNode().setStyle("-fx-border-color: transparent;");

            // Tooltip hiển thị thông tin
            Tooltip tooltip = new Tooltip();
            slice.getNode().setOnMouseEntered(event -> {
                tooltip.setText(tt + " (" + String.format("%.1f", phanTram) + "%)");
                tooltip.show(slice.getNode(), event.getScreenX(), event.getScreenY() + 10);
            });

            slice.getNode().setOnMouseExited(event -> {
                tooltip.hide();
            });
        }
        bieuDoTronTrangThaiHoaDon.setLabelsVisible(false);
    }


    private void capNhatComboBoxNamThongKe() {
        List<Integer> dsNam = hoaDonDAO.layCacNamLapHoaDon(null);
        namThongKeBieuDoCot.getItems().clear();
        namThongKeBieuDoTron.getItems().clear();
        namThongKeBieuDoTron.getItems().add("Tất cả");

        if (dsNam != null && !dsNam.isEmpty()) {
            dsNam.stream()
                    .sorted()
                    .forEach(nam -> {
                        namThongKeBieuDoCot.getItems().add(nam.toString());
                        namThongKeBieuDoTron.getItems().add(nam.toString());
                    });
        }

    }


}
