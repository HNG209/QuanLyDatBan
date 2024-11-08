package org.login.quanlydatban.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import org.login.quanlydatban.dao.HoaDonDAO;
import org.login.quanlydatban.entity.TaiKhoan;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class ThongKeNhanVienController {
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
    private ComboBox<String> namThongKeMonAn;
    @FXML
    private ComboBox<String> quyThongKeMonAn;
    @FXML
    private ComboBox<String> thangThongKeMonAn;
    @FXML
    private BarChart<String, Number> bieuDoCotDoanhThu; // Thay đổi tên thành bieuDoCotDoanhThu

    @FXML
    private CategoryAxis xDoanhThu;

    @FXML
    private CategoryAxis xHoaDon;

    @FXML
    private BarChart<String, Number> bieuDoCotHoaDon;

    @FXML
    private PieChart bieuDoTronMonAn;

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
        String maNV = TrangChuController.taiKhoan.getNhanVien().getMaNhanVien();
        capNhatComboBoxNamThongKe(maNV);
        tieuChiThongKeBieuDoCot.getSelectionModel().select("Theo tháng");
        namThongKeBieuDoCot.getSelectionModel().select(0);
        capNhatDuLieuDoanhThuVaHoaDon(maNV);
        capNhatDuLieuThongKeMonAnVaLoaiMonAn();
        capNhatDuLieuChoBieuDoCot(maNV);
        tieuChiThongKeBieuDoCot.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Theo tháng".equals(newValue) || "Theo quý".equals(newValue)) {
                namThongKeBieuDoCot.setVisible(true);
            } else {
                namThongKeBieuDoCot.setVisible(false);
            }
            capNhatDuLieuChoBieuDoCot(maNV);
        });
        namThongKeBieuDoCot.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            capNhatDuLieuChoBieuDoCot(maNV);
        });
        namThongKeMonAn.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            capNhatDuLieuThongKeMonAnVaLoaiMonAn();
        });
        thangThongKeMonAn.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            quyThongKeMonAn.getSelectionModel().select(0);
            capNhatDuLieuThongKeMonAnVaLoaiMonAn();
        });
        quyThongKeMonAn.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            thangThongKeMonAn.getSelectionModel().select(0);
            capNhatDuLieuThongKeMonAnVaLoaiMonAn();
        });
    }

    private void capNhatDuLieuDoanhThuVaHoaDon(String maNV) {
        Object[] tongDoanhThuVaHoaDon = hoaDonDAO.layDoanhThuVaSoHoaDon(maNV, null);
        Object[] tongDoanhThuVaHoaDonTheoNgay = hoaDonDAO.layDoanhThuVaSoHoaDon(maNV, LocalDate.now());

        if (tongDoanhThuVaHoaDonTheoNgay != null && tongDoanhThuVaHoaDonTheoNgay.length >= 2) {
            doanhThuTrongNgay.setText(df.format(tongDoanhThuVaHoaDonTheoNgay[0]));
            soHDTrongNgay.setText(String.valueOf(tongDoanhThuVaHoaDonTheoNgay[1]));
        } else {
            doanhThuTrongNgay.setText(df.format(0));
            soHDTrongNgay.setText("0");
        }
        if (tongDoanhThuVaHoaDon != null && tongDoanhThuVaHoaDon.length >= 2) {
            tongDoanhThu.setText(df.format(tongDoanhThuVaHoaDon[0]));
            tongHoaDon.setText(String.valueOf(tongDoanhThuVaHoaDon[1]));
        } else {
            tongDoanhThu.setText(df.format(0));
            tongHoaDon.setText("0");
        }
    }

    private void capNhatDuLieuDoanhThuTheoThangHoacQuy(String maNV, int nam, String donVi) {
        List<Object[]> doanhThuVaHoaDon;
        if ("Tháng".equals(donVi)) {
            doanhThuVaHoaDon = hoaDonDAO.layDoanhThuVaSoHoaDonTheoThang(maNV, nam);
        } else if ("Quý".equals(donVi)) {
            doanhThuVaHoaDon = hoaDonDAO.layDoanhThuVaSoHoaDonTheoQuy(maNV, nam);
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
        xDoanhThu.setAnimated(false);
    }

    private void capNhatDuLieuHoaDonTheoThangHoacQuy(String maNV, int nam, String donVi) {
        List<Object[]> doanhThuVaHoaDon;
        if ("Tháng".equals(donVi)) {
            doanhThuVaHoaDon = hoaDonDAO.layDoanhThuVaSoHoaDonTheoThang(maNV, nam);
        } else if ("Quý".equals(donVi)) {
            doanhThuVaHoaDon = hoaDonDAO.layDoanhThuVaSoHoaDonTheoQuy(maNV, nam);
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
        xHoaDon.setAnimated(false);
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
        xHoaDon.setAnimated(false);
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
        xHoaDon.setAnimated(false);
        NumberAxis yAxis = (NumberAxis) bieuDoCotDoanhThu.getYAxis();
        yAxis.setLowerBound(0);
        yAxis.setTickUnit(1);
        yAxis.setMinorTickVisible(false);

    }

    private void capNhatDuLieuChoBieuDoCot(String maNV) {

        String tieuChi = tieuChiThongKeBieuDoCot.getSelectionModel().getSelectedItem();
        int nam = 0;
        try {
            nam = Integer.parseInt(namThongKeBieuDoCot.getSelectionModel().getSelectedItem());
        }
        catch (NumberFormatException e){
            nam = 0;
        }
        if ("Theo tháng".equals(tieuChi)) {
            capNhatDuLieuDoanhThuTheoThangHoacQuy(maNV, nam, "Tháng");
            capNhatDuLieuHoaDonTheoThangHoacQuy(maNV, nam, "Tháng");
        } else if ("Theo quý".equals(tieuChi)) {
            capNhatDuLieuDoanhThuTheoThangHoacQuy(maNV, nam, "Quý");
            capNhatDuLieuHoaDonTheoThangHoacQuy(maNV, nam, "Quý");
        } else if ("Theo năm".equals(tieuChi)) {
            capNhatDuLieuDoanhThuTheoNam(maNV);
            capNhatDuLieuHoaDonTheoNam(maNV);
        }
        bieuDoCotHoaDon.setAnimated(false);
        bieuDoCotDoanhThu.setAnimated(false);
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

    private void capNhatDuLieuThongKeMonAnVaLoaiMonAn() {
        List<Object[]> dsMonAn;
        int nam = 0, thang = 0, quy = 0;
        try {
            nam = Integer.parseInt(namThongKeMonAn.getSelectionModel().getSelectedItem());
            try {
                thang = Integer.parseInt(thangThongKeMonAn.getSelectionModel().getSelectedItem());

            } catch (NumberFormatException e) {
                thang = 0;
            }
            try {

                quy = Integer.parseInt(quyThongKeMonAn.getSelectionModel().getSelectedItem());
            } catch (NumberFormatException e) {
                quy = 0;
            }
        } catch (NumberFormatException e) {
            nam = 0;
        }
        dsMonAn = hoaDonDAO.layDoanhThuTheoLoaiMonAn(nam, quy, thang);
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
            Label ghiChu = new Label(tenMonAn);
            ghiChu.setStyle("-fx-font-size: 10px;");

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


    private void capNhatComboBoxNamThongKe(String maNV) {
        List<Integer> dsNam = hoaDonDAO.layCacNamLapHoaDon(maNV);
        namThongKeBieuDoCot.getItems().clear();
        namThongKeMonAn.getItems().clear();
        namThongKeMonAn.getItems().add("Tất cả");
        if (dsNam != null && !dsNam.isEmpty()) {
            for (Integer nam : dsNam) {
                namThongKeBieuDoCot.getItems().add(nam.toString());
                namThongKeMonAn.getItems().add(nam.toString());
            }
        }

    }



}
