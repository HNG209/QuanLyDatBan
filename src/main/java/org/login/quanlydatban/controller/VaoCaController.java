package org.login.quanlydatban.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.login.quanlydatban.notification.Notification;
import org.login.quanlydatban.utilities.Clock;

import java.text.DecimalFormat;

public class VaoCaController {
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
    private Label tongMenhGia;
    private static boolean isBaoCaoSaved = false;
    public static String tongTienVaoCa;
    private DecimalFormat df = new DecimalFormat("#,### VND");;
    @FXML
    public void initialize() {
        Clock clock = new Clock();
        clock.startClock(thoiGianHienTai);
        tenNhanVien.setText(TrangChuController.getTaiKhoan().getNhanVien().getTenNhanVien());
        menhGia1K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia2K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia5K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia10K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia20K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia50K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia100K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia200K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia500K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
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
        tongMenhGia.setText("");
    }
        // Biến kiểm tra xem báo cáo đã được lưu hay chưa


        @FXML
        private void luuBaoCaoVaoCa() {
            // Kiểm tra nếu báo cáo đã được lưu rồi
            if (isBaoCaoSaved) {
                Notification.thongBao("Báo cáo vào ca đã được lưu rồi. Bạn không thể lưu lại.", Alert.AlertType.INFORMATION);
                dongCuaSo();
                return;
            }

            // Hiển thị thông báo xác nhận
            boolean confirm = Notification.xacNhan("Bạn có muốn lưu báo cáo vào ca không?");

            if (confirm) {
                // Nếu người dùng nhấn "OK", lưu báo cáo
                tongTienVaoCa = tongMenhGia.getText(); // Lưu tổng tiền vào ca

                // Đánh dấu báo cáo đã được lưu
                isBaoCaoSaved = true;

                Notification.thongBao("Bạn đã lưu báo cáo vào ca thành công.", Alert.AlertType.INFORMATION);
                dongCuaSo();
            } else {
                // Nếu người dùng nhấn "Cancel", không lưu báo cáo
                Notification.thongBao("Bạn đã hủy lưu báo cáo.", Alert.AlertType.INFORMATION);
            }
        }
    private void dongCuaSo() {
        Stage stage = (Stage) Window.getWindows().stream()
                .filter(Window::isFocused)
                .findFirst()
                .orElse(null);
        if (stage != null) {
            stage.close();
        }
    }


}
