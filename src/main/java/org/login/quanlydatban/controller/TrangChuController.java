package org.login.quanlydatban.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.login.quanlydatban.entity.TaiKhoan;

public class TrangChuController {
    @FXML
    private Label tenNhanVien;

    private TaiKhoan taiKhoan;

    public void setTaiKhoan(TaiKhoan taiKhoan){
        this.taiKhoan = taiKhoan;
        if(taiKhoan != null){
            if(taiKhoan.getNhanVien() != null)
                tenNhanVien.setText(taiKhoan.getNhanVien().getTenNhanVien());
        }
    }
}
