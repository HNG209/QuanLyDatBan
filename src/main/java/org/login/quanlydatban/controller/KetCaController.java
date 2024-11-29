package org.login.quanlydatban.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import org.login.quanlydatban.dao.HoaDonDAO;
import org.login.quanlydatban.entity.TaiKhoan;
import org.login.quanlydatban.utilities.Clock;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class KetCaController {

    @FXML
    private Label chenhLech;

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
    private Label tenNhanVien;

    @FXML
    private Label thoiGianHienTai;

    @FXML
    private Label tienCuoiCa;

    @FXML
    private TextField tienVaoCa;

    @FXML
    private Label tongDoanhThu;

    @FXML
    private Label tongMenhGia;

    @FXML
    private Label tongSoHoaDon;
    private TaiKhoan taiKhoan;
    private HoaDonDAO hoaDonDAO;
    private DecimalFormat df = new DecimalFormat("#,### VND");;
    public static boolean isKetCa = false;
    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    @FXML
    public void initialize() {
        Clock clock = new Clock();
        clock.startClock(thoiGianHienTai);
        hoaDonDAO = new HoaDonDAO();
        loadDuLieu();
        loadTienCuoiCaVaChenhLech();
        menhGia1K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia2K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia5K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia10K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia20K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia50K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia100K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia200K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia500K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        tienVaoCa.textProperty().addListener((observable, oldValue, newValue) -> loadTienCuoiCaVaChenhLech());

}
    public void loadDuLieu() {
        String maNV = TrangChuController.taiKhoan.getNhanVien().getMaNhanVien();
        tenNhanVien.setText(TrangChuController.taiKhoan.getNhanVien().getTenNhanVien());
        Object[] doanhThuVaSoHD;
        doanhThuVaSoHD = hoaDonDAO.layDoanhThuVaSoHoaDon(maNV, LocalDate.now());
        tienVaoCa.setText(VaoCaController.tongTienVaoCa.toString());
        if (doanhThuVaSoHD.length == 0) {
            tongSoHoaDon.setText("0");
            tongDoanhThu.setText("0");
        } else {
            tongDoanhThu.setText(doanhThuVaSoHD[0].toString());
            tongSoHoaDon.setText(doanhThuVaSoHD[1].toString());
        }
    }
    @FXML
    private void tinhTongMenhGia() {
        int tien1K = parseTextField(menhGia1K);
        int tien2K = parseTextField(menhGia2K);
        int tien5K = parseTextField(menhGia5K);
        int tien10K = parseTextField(menhGia10K);
        int tien20K = parseTextField(menhGia20K);
        int tien50K = parseTextField(menhGia50K);
        int tien100K = parseTextField(menhGia100K);
        int tien200K = parseTextField(menhGia200K);
        int tien500K = parseTextField(menhGia500K);

        double tongMenhGiaTien = tien1K * 1000 + tien2K * 2000 + tien5K * 5000
                + tien10K * 10000 + tien20K * 20000 + tien50K * 50000
                + tien100K * 100000 + tien200K * 200000 + tien500K * 500000;

        tongMenhGia.setText(df.format(tongMenhGiaTien));
        double tienChenhLech = tongMenhGiaTien - Double.parseDouble(tienCuoiCa.getText().replace(" VND", "").replace(",",""));
        if(tienChenhLech < 0) {
            chenhLech.setTextFill(Color.RED);
        }
        else chenhLech.setTextFill(Color.BLACK);
        chenhLech.setText(df.format(Math.abs(tienChenhLech)));
    }
    private int parseTextField(TextField textField) {
        try {
            return textField.getText().isEmpty() ? 0 : Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
            Platform.runLater(() -> textField.setText("0"));
            return 0;
        }
    }
    @FXML
    private void loadTienCuoiCaVaChenhLech() {
        double tienVao = 0.0;
        try {
            tienVao = tienVaoCa.getText().isEmpty() ? 0.0 : Double.parseDouble(tienVaoCa.getText().replace(" VND", "").replace(",",""));
        } catch (NumberFormatException e) {
            Platform.runLater(() -> tienVaoCa.setText("0"));
            tienVao = 0.0;
        }
        double doanhThu = tongDoanhThu.getText().isEmpty() ? 0.0 : Double.parseDouble(tongDoanhThu.getText().replace(" VND", "").replace(",",""));
        double tienCuoiCaValue = tienVao + doanhThu;
        double tongMenhGiaTien = tongMenhGia.getText().isEmpty() ? 0.0 : Double.parseDouble(tongMenhGia.getText().replace(" VND", "").replace(",",""));
        double tienChenhLech = tongMenhGiaTien - tienCuoiCaValue;
        if(tienChenhLech < 0.0){
            chenhLech.setTextFill(Color.RED);
        }
        else {
            chenhLech.setTextFill(Color.BLACK);
        }
        chenhLech.setText(df.format(Math.abs(tienChenhLech)));
        if(tienCuoiCaValue == 0.0){
            tienCuoiCa.setText("");
        }
        else {
            tienCuoiCa.setText(df.format(tienCuoiCaValue));
        }
    }
    @FXML
    private void thoat() {
        int option = JOptionPane.showConfirmDialog(
                null,
                "Bạn có chắc chắn muốn hủy kết ca không?",
                "Thông báo",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            Stage stage = (Stage) Window.getWindows().stream()
                    .filter(Window::isFocused)
                    .findFirst()
                    .orElse(null);
            if (stage != null) {
                stage.close();
            }
        }
    }



    @FXML
    private void lamMoi(){
        menhGia1K.setText("");
        menhGia2K.setText("");
        menhGia5K.setText("");
        menhGia10K.setText("");
        menhGia20K.setText("");
        menhGia50K.setText("");
        menhGia100K.setText("");
        menhGia200K.setText("");
        menhGia500K.setText("");
        tienVaoCa.setText("");
        tongMenhGia.setText("");
        chenhLech.setText("");

    }
    @FXML
    private void xuatBaoCaoKetCa() {
        String maNhanVien = taiKhoan.getNhanVien().getMaNhanVien();
        String tenNhanVien = taiKhoan.getNhanVien().getTenNhanVien();
        String ngayLap = thoiGianHienTai.getText();
        String tienVaoCaText = null;
        String tongSoHoaDonText = tongSoHoaDon.getText();
        String tongDoanhThuText = tongDoanhThu.getText();
        String tongMenhGiaText = tongMenhGia.getText();
        String chenhLechText = chenhLech.getText();

        if (maNhanVien.isEmpty() || tenNhanVien.isEmpty() || ngayLap.isEmpty() || tienVaoCa.getText().isEmpty() ||
                tongSoHoaDonText.isEmpty() || tongDoanhThuText.isEmpty() || tongMenhGiaText.isEmpty() || chenhLechText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Dữ liệu không đầy đủ. Vui lòng kiểm tra lại.");
            return;
        } else {
            tienVaoCaText = df.format(Double.parseDouble(tienVaoCa.getText().replace(" VND", "").replace(",","")));
        }

        if (chenhLech.getTextFill() == Color.RED) {
            int option = JOptionPane.showConfirmDialog(null, "Có chênh lệch giữa số tiền thực tế và bàn giao. Bạn có muốn tiếp tục?", "Thông báo", JOptionPane.YES_NO_OPTION);
            if (option != JOptionPane.YES_OPTION) {
                return;
            }
        }

        try {
            String folderPath = "src/main/resources/org/login/quanlydatban/baoCaoKetCa/";
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();  // Create the directory if it does not exist
            }

            String fileName = "BaoCaoKetCa_" + maNhanVien + "_" + ngayLap.replace("/", "_").replace(":", "_").replace(" ", "") + ".txt";
            File file = new File(folderPath + fileName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                String title = "BÁO CÁO KẾT CA";
                int width = 78;
                int padding = (width - title.length()) / 2;
                String centeredTitle = " ".repeat(padding) + title + " ".repeat(padding);
                writer.write(centeredTitle);
                writer.newLine();
                writer.write("-----------------------------------------------------------------------");
                writer.newLine();
                writer.newLine();

                writer.write("Mã nhân viên: " + maNhanVien);
                writer.newLine();
                writer.write("Tên nhân viên: " + tenNhanVien);
                writer.newLine();
                writer.write("Ngày lập báo cáo: " + ngayLap);
                writer.newLine();
                writer.write("-----------------------------------------------------------------------");
                writer.newLine();
                writer.write("Tiền vào ca: " + tienVaoCaText);
                writer.newLine();
                writer.write("Tổng số hóa đơn: " + tongSoHoaDonText);
                writer.newLine();
                writer.write("Tổng doanh thu: " + tongDoanhThuText);
                writer.newLine();
                writer.write("-----------------------------------------------------------------------");
                writer.newLine();
                writer.write("Tiền bàn giao thực tế: " + tongMenhGiaText);
                writer.newLine();
                writer.write("Chênh lệch: " + chenhLechText);
                writer.newLine();

                JOptionPane.showMessageDialog(null, "Xuất báo cáo kết ca thành công");
                isKetCa = true;
                for (Window window : Window.getWindows()) {
                    if (window instanceof Stage) {
                        ((Stage) window).close();
                    }
                }
                Stage currentStage = (Stage) chenhLech.getScene().getWindow();
                currentStage.close();
                TrangChuController.dangXuat();

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Xuất báo cáo kết ca không thành công");
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Có lỗi trong việc chuyển đổi số liệu. Vui lòng kiểm tra lại dữ liệu nhập.");
            e.printStackTrace();
        }
    }









}
