package org.login.quanlydatban.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.login.quanlydatban.entity.TaiKhoan;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class TrangChuController implements Initializable {
    @FXML
    private Label tenNhanVien;

    @FXML
    private Label chucVu;

    public static TaiKhoan taiKhoan;

    @FXML
    private BorderPane borderPane;

    @FXML
    private ImageView setting;

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

    public void dangXuat(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangDangNhap.fxml"));
        Scene scene = new Scene(loader.load());
        stage = new Stage();

        if (stage != null){
            stage.setTitle("Đăng nhập");
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/org/login/quanlydatban/stylesheets/style.css")).toExternalForm());
            stage.show();
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ContextMenu contextMenu = new ContextMenu();

        // Add MenuItems to the ContextMenu
        MenuItem item1 = new MenuItem("Tài khoản");
        MenuItem item2 = new MenuItem("Option 2");
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        MenuItem item3 = new MenuItem("Đăng xuất");
        item3.setOnAction(event -> {
            Stage stage = (Stage) setting.getScene().getWindow();
            stage.close();
            try {
                dangXuat(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        contextMenu.getItems().addAll(item1, item2, separatorMenuItem, item3);


        setting.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                Bounds boundsInScreen = setting.localToScreen(setting.getBoundsInLocal());
                contextMenu.show(setting, boundsInScreen.getMinX() - 30, boundsInScreen.getMaxY() + 5);
            }
        });
    }
}
