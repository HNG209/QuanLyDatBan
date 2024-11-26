package org.login.quanlydatban.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.login.quanlydatban.entity.TaiKhoan;
import org.login.quanlydatban.entity.enums.ChucVu;
import org.login.quanlydatban.utilities.Clock;

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
    private Label time;


    @FXML
    private ImageView avatar;

    @FXML
    private BorderPane borderPane;

    private static BorderPane borderPaneStatic;

    @FXML
    private ImageView setting;

    private Stage ketCaStage;
    public void setTaiKhoan(TaiKhoan taiKhoan){
        this.taiKhoan = taiKhoan;
        if(taiKhoan != null){
            if(taiKhoan.getNhanVien() != null){
                tenNhanVien.setText(taiKhoan.getNhanVien().getTenNhanVien());
                switch (taiKhoan.getNhanVien().getChucVuNhanVien()){
                    case NHAN_VIEN -> chucVu.setText("Nhân viên");
                    default -> chucVu.setText("Quản lý");
                }
                Clock clock = new Clock();
                clock.startClock(time);
//                showTooltipForAvatar();
            }

        }
    }

    @FXML
    public void datMon() throws IOException {
        //first go to chonBan -> datMon
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangChonBan.fxml"));
//        AnchorPane anchorPane = loader.load();
//
//        ChonBanController controller = loader.getController();

        borderPane.setCenter(ChonBanController.getInstance().getRoot());

        ChonBanController.getInstance().refresh();

        AnchorPane pane = (AnchorPane) ChonBanController.getInstance().getRoot();

        pane.prefWidthProperty().bind(borderPane.widthProperty());
        pane.prefHeightProperty().bind(borderPane.heightProperty());
    }


    @FXML
    public void quanlynhanvien() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/QuanLyNhanVien_XemDS.fxml"));
        AnchorPane anchorPane = loader.load();
        TrangQuanLyNhanVienController nv = loader.getController();
        nv.setNhanvien(taiKhoan.getUserName().toString());
        borderPane.setCenter(anchorPane);
        anchorPane.prefWidthProperty().bind(borderPane.widthProperty());
        anchorPane.prefHeightProperty().bind(borderPane.heightProperty());

    }

    @FXML
    public void quanlytaikhoan() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangTaiKhoan.fxml"));
        AnchorPane anchorPane = loader.load();
        TrangQuanLyTaiKhoanController nv = loader.getController();
        nv.setTenNhanVien(taiKhoan.getUserName().toString());
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
    @FXML
    public void khachHang() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangKhachHang.fxml"));
        AnchorPane anchorPane = loader.load();
        borderPane.setCenter(anchorPane);
        anchorPane.prefWidthProperty().bind(borderPane.widthProperty());
        anchorPane.prefHeightProperty().bind(borderPane.heightProperty());
    }

    public static void dangXuat() throws IOException {
        FXMLLoader loader = new FXMLLoader(TrangChuController.class.getResource("/org/login/quanlydatban/views/TrangDangNhap.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();

        if (stage != null){
            stage.setTitle("Đăng nhập");
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            scene.getStylesheets().add(Objects.requireNonNull(TrangChuController.class.getResource("/org/login/quanlydatban/stylesheets/style.css")).toExternalForm());
            stage.show();
        }
    }

    @FXML
    void datLich(ActionEvent event) throws IOException {
        DatLichController.getInstance().refreshBang();
        DatLichController.getInstance().refreshViewBan();
        borderPane.setCenter(DatLichController.getInstance().getRoot());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ContextMenu contextMenu = new ContextMenu();
        setBorderPaneStatic(borderPane);
        MenuItem itemTaiKhoan = new MenuItem("Tài khoản");
        itemTaiKhoan.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangThongTinCaNhan.fxml"));
            try {
                AnchorPane anchorPane = loader.load();
                TrangChuController.getBorderPaneStatic().setCenter(anchorPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        MenuItem itemDangXuat = new MenuItem("Đăng xuất");
        itemDangXuat.setOnAction(event -> {
            Stage stage = (Stage) setting.getScene().getWindow();
            stage.close();
            try {
                dangXuat();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        contextMenu.getItems().addAll(itemTaiKhoan, separatorMenuItem, itemDangXuat);


        setting.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                Bounds boundsInScreen = setting.localToScreen(setting.getBoundsInLocal());
                contextMenu.show(setting, boundsInScreen.getMinX() - 30, boundsInScreen.getMaxY() + 5);
            }
        });

    }

    public static TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public static BorderPane getBorderPaneStatic() {
        return borderPaneStatic;
    }

    public static void setBorderPaneStatic(BorderPane borderPaneStatic) {
        TrangChuController.borderPaneStatic = borderPaneStatic;
    }
    @FXML
    public void xemQuanLyBan() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangQuanLyBan.fxml"));
            AnchorPane anchorPane = loader.load();
            borderPane.setCenter(anchorPane);

            anchorPane.prefWidthProperty().bind(borderPane.widthProperty());
            anchorPane.prefHeightProperty().bind(borderPane.heightProperty());
        } catch (IOException e) {
            // Hiển thị thông báo lỗi cho người dùng
            System.err.println("Không thể tải giao diện Quản lý bàn: " + e.getMessage());
            e.printStackTrace();
        }
    }
//    @FXML
//    public void showTooltipForAvatar() {
//        Tooltip tooltip = new Tooltip();
//        if (taiKhoan != null && taiKhoan.getNhanVien() != null) {
//            String url = taiKhoan.getNhanVien().getHinhAnh();
//            avatar.setImage(new Image(getClass().getResource(url).toExternalForm()));
//            String ten = taiKhoan.getNhanVien().getTenNhanVien();
//            String chucVuText = taiKhoan.getNhanVien().getChucVuNhanVien() == ChucVu.NHAN_VIEN ? "Nhân viên" : "Quản lý";
//            tooltip.setText("Tên: " + ten + "\nChức vụ: " + chucVuText);
//        } else {
//            tooltip.setText("Không có thông tin nhân viên");
//        }
//        Tooltip.install(avatar, tooltip);
//        avatar.setOnMouseEntered(event -> tooltip.show(avatar, event.getScreenX(), event.getScreenY() + 15));
//        avatar.setOnMouseExited(event -> tooltip.hide());
//    }

}
