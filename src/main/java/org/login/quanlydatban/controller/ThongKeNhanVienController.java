package org.login.quanlydatban.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.util.converter.LocalDateStringConverter;
//import org.login.quanlydatban.dao.HoaDonDAO;
import org.login.service.HoaDonService;
import org.login.entity.TaiKhoan;
import org.login.entity.enums.TrangThaiHoaDon;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private ComboBox<String> namThongKeBieuDoTron;
    @FXML
    private ComboBox<String> quyThongKeBieuDoTron;
    @FXML
    private ComboBox<String> thangThongKeBieuDoTron;
    @FXML
    private BarChart<String, Number> bieuDoCotDoanhThu;
    @FXML
    private DatePicker chooseDate;

    @FXML
    private CategoryAxis xDoanhThu;

    @FXML
    private CategoryAxis xHoaDon;

    @FXML
    private BarChart<String, Number> bieuDoCotHoaDon;

    @FXML
    private PieChart bieuDoTronTrangThaiHoaDon;

    private HoaDonService hoaDonService;
    private ObservableList<Object[]> bxh = FXCollections.observableArrayList();
    private TaiKhoan taiKhoan;
    private final DecimalFormat df = new DecimalFormat("#,### VND");
    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    @FXML
    public void initialize() throws RemoteException, MalformedURLException, NotBoundException {
        String host = System.getenv("HOST_NAME");
        hoaDonService = (HoaDonService) Naming.lookup("rmi://"+ host + ":2909/hoaDonService");
        String maNV = TrangChuController.taiKhoan.getNhanVien().getMaNhanVien();
        capNhatComboBoxNamThongKe(maNV);
        tieuChiThongKeBieuDoCot.getSelectionModel().select("Theo tháng");
        namThongKeBieuDoCot.getSelectionModel().select(0);
        namThongKeBieuDoTron.getSelectionModel().select(0);
        capNhatDuLieuDoanhThuVaHoaDon(maNV);
        capNhatDuLieuThongKeTrangThaiHoaDon();
        capNhatDuLieuChoBieuDoCot(maNV);
        tieuChiThongKeBieuDoCot.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Theo tháng".equals(newValue) || "Theo quý".equals(newValue)) {
                namThongKeBieuDoCot.setVisible(true);
            } else {
                namThongKeBieuDoCot.setVisible(false);
            }
            try {
                capNhatDuLieuChoBieuDoCot(maNV);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        namThongKeBieuDoCot.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                capNhatDuLieuChoBieuDoCot(maNV);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
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
            chooseDate.setValue(null);
            try {
                capNhatDuLieuThongKeTrangThaiHoaDon();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        thangThongKeBieuDoTron.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            quyThongKeBieuDoTron.getSelectionModel().select(0);
            thangThongKeBieuDoTron.getSelectionModel().select(newValue);
            try {
                capNhatDuLieuThongKeTrangThaiHoaDon();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        quyThongKeBieuDoTron.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            thangThongKeBieuDoTron.getSelectionModel().select(0);
            quyThongKeBieuDoTron.getSelectionModel().select(newValue);
            try {
                capNhatDuLieuThongKeTrangThaiHoaDon();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        chooseDate.setConverter(new LocalDateStringConverter(formatter, formatter));
        chooseDate.setPromptText("dd/MM/yyyy");
        chooseDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                namThongKeBieuDoTron.getSelectionModel().select("Tất cả"); // Reset năm về "Tất cả"
                try {
                    capNhatDuLieuThongKeTrangThaiHoaDon();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void capNhatDuLieuDoanhThuVaHoaDon(String maNV) throws RemoteException {
        Object[] tongDoanhThuVaHoaDon = hoaDonService.layDoanhThuVaSoHoaDon(maNV, null);
        Object[] tongDoanhThuVaHoaDonTheoNgay = hoaDonService.layDoanhThuVaSoHoaDon(maNV, LocalDate.now());

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

    private void capNhatDuLieuDoanhThuTheoThangHoacQuy(String maNV, int nam, String donVi) throws RemoteException {
        List<Object[]> doanhThuVaHoaDon;
        if ("Tháng".equals(donVi)) {
            doanhThuVaHoaDon = hoaDonService.layDoanhThuVaSoHoaDonTheoThang(maNV, nam);
        } else if ("Quý".equals(donVi)) {
            doanhThuVaHoaDon = hoaDonService.layDoanhThuVaSoHoaDonTheoQuy(maNV, nam);
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

    private void capNhatDuLieuHoaDonTheoThangHoacQuy(String maNV, int nam, String donVi) throws RemoteException {
        List<Object[]> doanhThuVaHoaDon;
        if ("Tháng".equals(donVi)) {
            doanhThuVaHoaDon = hoaDonService.layDoanhThuVaSoHoaDonTheoThang(maNV, nam);
        } else if ("Quý".equals(donVi)) {
            doanhThuVaHoaDon = hoaDonService.layDoanhThuVaSoHoaDonTheoQuy(maNV, nam);
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

    private void capNhatDuLieuHoaDonTheoNam(String maNV) throws RemoteException {
        List<Object[]> hoaDonVaSoHoaDon = hoaDonService.layDoanhThuVaSoHoaDonTheoNam(maNV);
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

    private void capNhatDuLieuDoanhThuTheoNam(String maNV) throws RemoteException {
        List<Object[]> hoaDonVaSoHoaDon = hoaDonService.layDoanhThuVaSoHoaDonTheoNam(maNV);
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

    private void capNhatDuLieuChoBieuDoCot(String maNV) throws RemoteException {

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
    private void capNhatDuLieuThongKeTrangThaiHoaDon() throws RemoteException {
        int nam = 0, quy = 0, thang = 0;
        LocalDate ngay = null;
        String maNV = TrangChuController.taiKhoan.getNhanVien().getMaNhanVien();
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
            dsTrangThai = hoaDonService.laySoHoaDonTheoTrangThaiVaNgay(maNV, ngay);
        } else {
            dsTrangThai = hoaDonService.laySoHoaDonTheoTrangThaiHoaDon(maNV, nam, quy, thang);
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

    private void capNhatComboBoxNamThongKe(String maNV) throws RemoteException {
        List<Integer> dsNam = hoaDonService.layCacNamLapHoaDon(maNV);
        namThongKeBieuDoCot.getItems().clear();
        namThongKeBieuDoTron.getItems().clear();
        namThongKeBieuDoTron.getItems().add("Tất cả");
        if (dsNam != null && !dsNam.isEmpty()) {
            for (Integer nam : dsNam) {
                namThongKeBieuDoCot.getItems().add(nam.toString());
                namThongKeBieuDoTron.getItems().add(nam.toString());
            }
        }

    }



}
