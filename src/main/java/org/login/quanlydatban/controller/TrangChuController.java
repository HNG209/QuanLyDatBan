package org.login.quanlydatban.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.login.quanlydatban.entity.TaiKhoan;
import org.login.quanlydatban.entity.enums.ChucVu;

import java.io.IOException;

public class TrangChuController {
    @FXML
    private Label tenNhanVien;

    public static TaiKhoan taiKhoan;

    @FXML
    private BorderPane borderPane;

    private Stage ketCaStage;
    public void setTaiKhoan(TaiKhoan taiKhoan){
        this.taiKhoan = taiKhoan;
        if(taiKhoan != null){
            if(taiKhoan.getNhanVien() != null)
                tenNhanVien.setText(taiKhoan.getNhanVien().getTenNhanVien());
        }
    }

    @FXML
    public void datMon() throws IOException {
        //first go to chonBan -> datMon
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangChonBan.fxml"));
        AnchorPane anchorPane = loader.load();

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
        String phanQuyen = taiKhoan.getNhanVien().getChucVuNhanVien().toString();
        String duongDan;
        Object controller;
        if (phanQuyen.equalsIgnoreCase(ChucVu.NHAN_VIEN.toString())) {
            duongDan = "/org/login/quanlydatban/views/TrangThongKeNhanVien.fxml";
            controller = new ThongKeNhanVienController();
        } else {
            duongDan = "/org/login/quanlydatban/views/TrangThongKeQuanLy.fxml";
            controller = new ThongKeQuanLyController();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(duongDan));
        AnchorPane anchorPane = loader.load();

        if (controller instanceof ThongKeNhanVienController) {
            ThongKeNhanVienController thongKeController = (ThongKeNhanVienController) controller;
            thongKeController.setTaiKhoan(this.taiKhoan);
        } else {
            ThongKeQuanLyController thongKeController = (ThongKeQuanLyController) controller;
            thongKeController.setTaiKhoan(this.taiKhoan);
        }

        borderPane.setCenter(anchorPane);

        anchorPane.prefWidthProperty().bind(borderPane.widthProperty());
        anchorPane.prefHeightProperty().bind(borderPane.heightProperty());
    }

    @FXML
    public void ketCa() throws IOException {

        if (ketCaStage == null || !ketCaStage.isShowing()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangBaoCaoKetCa.fxml"));
            AnchorPane anchorPane = loader.load();
            KetCaController ketCa = loader.getController();
            ketCa.setTaiKhoan(taiKhoan);

            Scene scene = new Scene(anchorPane);
            ketCaStage = new Stage();
            ketCaStage.setScene(scene);
            ketCaStage.setTitle("Báo Cáo Kết Ca");
            ketCaStage.setOnCloseRequest(e -> ketCaStage = null);
            ketCaStage.setResizable(false);
            ketCaStage.show();
        } else {
            ketCaStage.toFront();
        }
    }

    @FXML
    public void thucDon() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangThucDon.fxml"));
        AnchorPane anchorPane = loader.load();
        borderPane.setCenter(anchorPane);

        anchorPane.prefWidthProperty().bind(borderPane.widthProperty());
        anchorPane.prefHeightProperty().bind(borderPane.heightProperty());
    }
    @FXML
    public void xemHoaDon() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangHoaDon.fxml"));
        AnchorPane anchorPane = loader.load();

        borderPane.setCenter(anchorPane);

        anchorPane.prefWidthProperty().bind(borderPane.widthProperty());
        anchorPane.prefHeightProperty().bind(borderPane.heightProperty());
    }
}
