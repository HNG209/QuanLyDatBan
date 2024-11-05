package org.login.quanlydatban.controller;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class TrangQuanLyTaiKhoanController implements Initializable {
    // lay ra ten nhan vien
    private String tenNhanVien;

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
