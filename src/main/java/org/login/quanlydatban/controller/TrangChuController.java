package org.login.quanlydatban.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.login.quanlydatban.entity.TaiKhoan;

import java.io.IOException;

public class TrangChuController {
    @FXML
    private Label tenNhanVien;

    private TaiKhoan taiKhoan;

    @FXML
    private BorderPane borderPane;

    public void setTaiKhoan(TaiKhoan taiKhoan){
        this.taiKhoan = taiKhoan;
        if(taiKhoan != null){
            if(taiKhoan.getNhanVien() != null)
                tenNhanVien.setText(taiKhoan.getNhanVien().getTenNhanVien());
        }
    }

    @FXML
    public void datMon() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangDatMon.fxml"));
        AnchorPane anchorPane = loader.load();

        borderPane.setCenter(anchorPane);

        anchorPane.prefWidthProperty().bind(borderPane.widthProperty());
        anchorPane.prefHeightProperty().bind(borderPane.heightProperty());
    }
}
