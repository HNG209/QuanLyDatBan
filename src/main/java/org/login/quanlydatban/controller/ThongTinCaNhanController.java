package org.login.quanlydatban.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
//import org.login.quanlydatban.dao.TaiKhoanDAO;

import org.login.service.TaiKhoanService;
import org.login.quanlydatban.encryptionUtils.EncryptionUtils;
import org.login.entity.NhanVien;
import org.login.entity.TaiKhoan;
import org.login.entity.enums.ChucVu;
import org.login.quanlydatban.notification.Notification;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class ThongTinCaNhanController implements Initializable {
    @FXML
    private TextField cccd;

    @FXML
    private TextField maNV;

    @FXML
    private TextField ngaySinh;

    @FXML
    private TextField sdt;

    @FXML
    private TextField tenNV;

    @FXML
    private TextField trangThaiNV;

    @FXML
    private Label textRole;

    @FXML
    private TextField tenDangNhap;

    @FXML
    private PasswordField matKhau;

    @FXML
    private TextField nhapLaiMK;

    @FXML
    private TextField mkMoi;

    @FXML
    private TextField nhapLaiMKmoi;

    private TaiKhoanService taiKhoanService;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String host = System.getenv("HOST_NAME");
        try {
            taiKhoanService = (TaiKhoanService) Naming.lookup("rmi://"+ host + ":2909/taiKhoanService");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            throw new RuntimeException(e);
        }

        NhanVien nhanVien = TrangChuController.getTaiKhoan().getNhanVien();

        TaiKhoan taiKhoan = TrangChuController.getTaiKhoan();

        tenDangNhap.setText(taiKhoan.getUserName());
        if(taiKhoan.getPassword().equals("1111"))
            matKhau.setText(taiKhoan.getPassword());
        else {
            try {
                matKhau.setText(EncryptionUtils.decrypt(taiKhoan.getPassword(), System.getenv("ENCRYPTION_KEY")));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if(nhanVien.getChucVuNhanVien().equals(ChucVu.QUAN_LY))
            textRole.setText("Quản lý");
        maNV.setText(nhanVien.getMaNhanVien());
        sdt.setText(nhanVien.getSdt());
        ngaySinh.setText(nhanVien.getNgaySinh().toString());
        tenNV.setText(nhanVien.getTenNhanVien());
        trangThaiNV.setText(nhanVien.getTrangThaiNhanVien().toString());
        cccd.setText(nhanVien.getCccd());
    }

    @FXML
    void doiMatKhau(ActionEvent event) throws Exception {
        TaiKhoan taiKhoan = TrangChuController.getTaiKhoan();

        if(nhapLaiMK.getText().equals(matKhau.getText())) {
            if(nhapLaiMKmoi.getText().equals(mkMoi.getText())){
                if(!matKhau.getText().equals(mkMoi.getText())){
                    taiKhoan.setPassword(EncryptionUtils.encrypt(mkMoi.getText(), System.getenv("ENCRYPTION_KEY")));
                    TrangChuController.taiKhoan = taiKhoanService.updateTaiKhoan(taiKhoan);

                    matKhau.setText(mkMoi.getText());
                    nhapLaiMK.clear();
                    mkMoi.clear();
                    nhapLaiMKmoi.clear();

                    Notification.thongBao("Cập nhật mật khẩu thành công", Alert.AlertType.INFORMATION);
                    Stage currentStage = (Stage) sdt.getScene().getWindow();
                    currentStage.close();
                    TrangChuController.dangXuat();
                }
                else Notification.thongBao("Mật khẩu mới không được trùng mật khẩu hiện tại", Alert.AlertType.WARNING);
            }
            else Notification.thongBao("Nhập lại mật khẩu mới không trùng khớp", Alert.AlertType.WARNING);
        }
        else Notification.thongBao("Nhập lại mật khẩu hiện tại không trùng khớp", Alert.AlertType.WARNING);
    }
}
