package org.login.quanlydatban.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.login.quanlydatban.dao.HoaDonDAO;
import org.login.quanlydatban.entity.TaiKhoan;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class KetCaController {

    @FXML
    private Label chenhLech;

    @FXML
    private Button huyKetCa;

    @FXML
    private TextField menhGia100K;

    @FXML
    private TextField menhGia10K;

    @FXML
    private TextField menhGia1K;

    @FXML
    private TextField menhGia200K;

    @FXML
    private TextField menhGia20K;

    @FXML
    private TextField menhGia2K;

    @FXML
    private TextField menhGia500K;

    @FXML
    private TextField menhGia50K;

    @FXML
    private TextField menhGia5K;

    @FXML
    private Button nutKetCa;

    @FXML
    private Label tenNhanVien;

    @FXML
    private Label thoiGianHienTai;

    @FXML
    private Label tienCuoiCa;

    @FXML
    private HBox tienCuoica;

    @FXML
    private Label tongDoanhThu;

    @FXML
    private Label tongMenhGia;

    @FXML
    private Label tongSoHoaDon;
    private TaiKhoan taiKhoan;
    private HoaDonDAO hoaDonDAO;
    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    @FXML
    public void initialize() {
        hoaDonDAO = new HoaDonDAO();
        loadDuLieu();
        startClock();
    }
    public void loadDuLieu() {
        String maNV = TrangChuController.taiKhoan.getNhanVien().getMaNhanVien();
        tenNhanVien.setText(TrangChuController.taiKhoan.getNhanVien().getTenNhanVien());
        Object[] doanhThuVaSoHD;
        doanhThuVaSoHD = hoaDonDAO.layDoanhThuVaSoHoaDon(maNV, LocalDate.now());
        if (doanhThuVaSoHD.length == 0) {
            tongSoHoaDon.setText("0");
            tongDoanhThu.setText("0");
        } else {
            tongDoanhThu.setText(doanhThuVaSoHD[0].toString());
            tongSoHoaDon.setText(doanhThuVaSoHD[1].toString());
        }
    }
    private void startClock() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss"); // Customize as needed
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            // Update the label with the current time
            thoiGianHienTai.setText(LocalDateTime.now().format(formatter));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE); // Run indefinitely
        timeline.play(); // Start the timeline
    }
}
