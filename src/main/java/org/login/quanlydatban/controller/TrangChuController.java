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

    @FXML
    private Label chucVu;

    public static TaiKhoan taiKhoan;

    @FXML
    private BorderPane borderPane;


    public void setTaiKhoan(TaiKhoan taiKhoan){
        this.taiKhoan = taiKhoan;
        if(taiKhoan != null){
            if(taiKhoan.getNhanVien() != null){
                tenNhanVien.setText(taiKhoan.getNhanVien().getTenNhanVien());
                switch (taiKhoan.getNhanVien().getChucVuNhanVien()){
                    case NHAN_VIEN -> chucVu.setText("Nhân viên");
                    default -> chucVu.setText("Quản lý");
                }
            }

        }
    }

    @FXML
    public void datMon() throws IOException {
        //first go to chonBan -> datMon
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangChonBan.fxml"));
        AnchorPane anchorPane = loader.load();

        ChonBanController controller = loader.getController();
        controller.setNhanVien(taiKhoan.getNhanVien());

        borderPane.setCenter(anchorPane);

        anchorPane.prefWidthProperty().bind(borderPane.widthProperty());
        anchorPane.prefHeightProperty().bind(borderPane.heightProperty());
    }


    @FXML
    public void quanlynhanvien() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/QuanLyNhanVien_XemDS.fxml"));
        AnchorPane anchorPane = loader.load();
        TrangQuanLyNhanVien nv = loader.getController();
        //System.out.println(taiKhoan.getUserName().toString());
        nv.setNhanvien(taiKhoan.getUserName().toString());
        borderPane.setCenter(anchorPane);
        anchorPane.prefWidthProperty().bind(borderPane.widthProperty());
        anchorPane.prefHeightProperty().bind(borderPane.heightProperty());

    }
    @FXML
    public void thongKe() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangThongKe.fxml"));
        AnchorPane anchorPane = loader.load();
        ThongKeController thongKeController = loader.getController();
        thongKeController.setTaiKhoan(this.taiKhoan);
        borderPane.setCenter(anchorPane);

        anchorPane.prefWidthProperty().bind(borderPane.widthProperty());
        anchorPane.prefHeightProperty().bind(borderPane.heightProperty());
    }

    public void thucDon() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangThucDon.fxml"));
        AnchorPane anchorPane = loader.load();
        borderPane.setCenter(anchorPane);

        anchorPane.prefWidthProperty().bind(borderPane.widthProperty());
        anchorPane.prefHeightProperty().bind(borderPane.heightProperty());
    }
    @FXML
    public void xemHoaDon() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangHoaDon.fxml"));
            AnchorPane anchorPane = loader.load();
            borderPane.setCenter(anchorPane);

            anchorPane.prefWidthProperty().bind(borderPane.widthProperty());
            anchorPane.prefHeightProperty().bind(borderPane.heightProperty());
        } catch (IOException e) {
            // Hiển thị thông báo lỗi cho người dùng
            System.err.println("Không thể tải giao diện Hoa Đơn: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
