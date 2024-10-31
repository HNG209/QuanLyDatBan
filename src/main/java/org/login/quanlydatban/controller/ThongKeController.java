package org.login.quanlydatban.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import org.login.quanlydatban.dao.HoaDonDAO;
import org.login.quanlydatban.entity.TaiKhoan;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private BarChart<String, Number> bieuDoCotDoanhThu; // Thay đổi tên thành bieuDoCotDoanhThu

    @FXML
    private CategoryAxis xDoanhThu;
    @FXML
    private NumberAxis yDoanhThu;

    @FXML
    private CategoryAxis xHoaDon;
    @FXML
    private NumberAxis yHoaDon;
    @FXML
    private BarChart<String, Number> bieuDoCotHoaDon;

    @FXML
    private PieChart bieuDoTronMonAn;

    private HoaDonDAO hoaDonDAO;

    private TaiKhoan taiKhoan;

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    @FXML
    public void initialize() {
        hoaDonDAO = new HoaDonDAO();
        capNhatComboBoxNamThongKe("NV01");
        tieuChiThongKe.getSelectionModel().select("Theo tháng");
        namThongKe.getSelectionModel().select(0);

        // Listener cho ComboBox tieuChiThongKe
        tieuChiThongKe.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Theo tháng".equals(newValue) || "Theo quý".equals(newValue)) {
                namThongKe.setVisible(true);
            } else {
                namThongKe.setVisible(false);
            }
            // Cập nhật lại dữ liệu khi thay đổi tiêu chí
            capNhatDuLieuChoBieuDoCot("NV01");
        });

        // Listener cho ComboBox namThongKe
        namThongKe.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Cập nhật lại dữ liệu khi thay đổi năm
            capNhatDuLieuChoBieuDoCot("NV01");
        });

        capNhatDuLieuDoanhThuVaHoaDon("NV01");
        capNhatDuLieuChoBieuDoTron("NV01"); // Gọi ngay khi khởi tạo
    }



    private void capNhatDuLieuDoanhThuVaHoaDon(String maNV) {
        Object[] tongDoanhThuVaHoaDon = hoaDonDAO.layDoanhThuVaSoHoaDonTheoMaNV(maNV,null);
        Object[] tongDoanhThuVaHoaDonTheoNgay = hoaDonDAO.layDoanhThuVaSoHoaDonTheoMaNV(maNV, LocalDate.now());
        DecimalFormat df = new DecimalFormat("#,### VND");
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
            return; // Nếu không phải tháng hoặc quý thì trả về
        }

        // Tạo mảng mặc định với giá trị 0 cho từng tháng (12 tháng) hoặc quý (4 quý)
        int soPhanTu = "Tháng".equals(donVi) ? 12 : 4;
        Number[] doanhThuTheoThoiGian = new Number[soPhanTu];
        Arrays.fill(doanhThuTheoThoiGian, 0); // Điền mặc định giá trị 0

        // Lặp qua dữ liệu và cập nhật mảng theo tháng hoặc quý có dữ liệu
        for (Object[] data : doanhThuVaHoaDon) {
            int index = ((Number) data[0]).intValue() - ( "Tháng".equals(donVi) ? 1 : 1); // Lấy chỉ số tháng hoặc quý và điều chỉnh
            doanhThuTheoThoiGian[index] = (Number) data[1]; // Lấy doanh thu
        }

        // Hiển thị dữ liệu trên biểu đồ
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
            return; // Nếu không phải tháng hoặc quý thì trả về
        }

        // Tạo mảng mặc định với giá trị 0 cho từng tháng (12 tháng) hoặc quý (4 quý)
        int soPhanTu = "Tháng".equals(donVi) ? 12 : 4;
        Number[] hoaDonTheoThoiGian = new Number[soPhanTu];
        Arrays.fill(hoaDonTheoThoiGian, 0); // Điền mặc định giá trị 0

        // Lặp qua dữ liệu và cập nhật mảng theo tháng hoặc quý có dữ liệu
        for (Object[] data : doanhThuVaHoaDon) {
            int index = ((Number) data[0]).intValue() - ( "Tháng".equals(donVi) ? 1 : 1); // Lấy chỉ số tháng hoặc quý và điều chỉnh
            hoaDonTheoThoiGian[index] = (Number) data[2]; // Lấy số hóa đơn
        }

        // Hiển thị dữ liệu trên biểu đồ
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Số hóa đơn theo " + donVi);
        for (int i = 0; i < hoaDonTheoThoiGian.length; i++) {
            series.getData().add(new XYChart.Data<>(donVi + " " + (i + 1), hoaDonTheoThoiGian[i]));
        }

        bieuDoCotHoaDon.getData().clear();
        bieuDoCotHoaDon.getData().add(series);
        xHoaDon.setAnimated(false);
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

        // Đảm bảo trục Y luôn hiển thị số nguyên
        NumberAxis yAxis = (NumberAxis) bieuDoCotHoaDon.getYAxis();
        yAxis.setLowerBound(0);
        yAxis.setTickUnit(1);
        yAxis.setMinorTickVisible(false);

        // Thiết lập giá trị tối đa cho trục Y
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

        // Đảm bảo trục Y luôn hiển thị số nguyên
        NumberAxis yAxis = (NumberAxis) bieuDoCotDoanhThu.getYAxis();
        yAxis.setLowerBound(0);
        yAxis.setTickUnit(1);
        yAxis.setMinorTickVisible(false);
    }

    private void capNhatDuLieuChoBieuDoCot(String maNV) {
        String tieuChi = tieuChiThongKe.getSelectionModel().getSelectedItem();
        int nam = Integer.parseInt(namThongKe.getSelectionModel().getSelectedItem());
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
    }


    private void capNhatDuLieuChoBieuDoTron(String maNV) {
        List<Object[]> dsMonAn = hoaDonDAO.layTop5MonAnCoDoanhThuCaoNhatTheoMaNV(maNV);

        // Xóa dữ liệu cũ trước khi thêm dữ liệu mới
        bieuDoTronMonAn.getData().clear();

        double tongDoanhThu = 0;

        // Tính tổng doanh thu
        for (Object[] loaiMonAnData : dsMonAn) {
            Number doanhThu = (Number) loaiMonAnData[1]; // Doanh thu
            tongDoanhThu += doanhThu.doubleValue(); // Cộng dồn doanh thu
        }

        // Thêm dữ liệu vào biểu đồ tròn với tỷ lệ phần trăm
        for (Object[] loaiMonAnData : dsMonAn) {
            String tenMonAn = (String) loaiMonAnData[0]; // Tên món ăn
            Number doanhThu = (Number) loaiMonAnData[1]; // Doanh thu

            // Tính tỷ lệ phần trăm
            double phanTram = (doanhThu.doubleValue() / tongDoanhThu) * 100;

            // Tạo một phần dữ liệu cho biểu đồ tròn
            PieChart.Data slice = new PieChart.Data(tenMonAn + " (" + String.format("%.1f", phanTram) + "%)", doanhThu.doubleValue());
            bieuDoTronMonAn.getData().add(slice); // Thêm vào biểu đồ

            // Tạo tooltip cho mỗi phần
            Tooltip tooltip = new Tooltip();
            slice.getNode().setOnMouseEntered(event -> {
                // Hiển thị tooltip khi rê chuột vào
                tooltip.setText(tenMonAn + ": " + String.format("%.1f", phanTram) + "%");
                tooltip.show(slice.getNode(), event.getScreenX(), event.getScreenY() + 10); // Hiển thị tooltip gần chuột
            });

            slice.getNode().setOnMouseExited(event -> {
                // Ẩn tooltip khi không còn rê chuột
                tooltip.hide(); // Ẩn tooltip khi chuột rời khỏi
            });
        }

        // Phóng to biểu đồ
        bieuDoTronMonAn.setPrefWidth(400); // Kích thước chiều rộng
        bieuDoTronMonAn.setPrefHeight(400); // Kích thước chiều cao

        // Thiết lập kiểu chữ cho ghi chú
        for (PieChart.Data data : bieuDoTronMonAn.getData()) {
            data.getNode().setStyle("-fx-font-size: 10px;"); // Thiết lập kích thước chữ ghi chú
        }

        // Ẩn các mũi tên chỉa vào biểu đồ tròn
        bieuDoTronMonAn.setLabelsVisible(false); // Ẩn nhãn và mũi tên
    }







    private void capNhatComboBoxNamThongKe(String maNV) {
        List<Integer> dsNam = hoaDonDAO.layCacNamLapHoaDonTheoMaNV(maNV); // Lấy danh sách năm từ DAO
        namThongKe.getItems().clear();
        if (dsNam != null && !dsNam.isEmpty()) {
            for (Integer nam : dsNam) {
                namThongKe.getItems().add(nam.toString()); // Chuyển đổi Integer sang String
            }
        }
    }

}
