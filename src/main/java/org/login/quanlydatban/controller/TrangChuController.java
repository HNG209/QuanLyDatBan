package org.login.quanlydatban.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.login.quanlydatban.dao.HoaDonDAO;
import org.login.quanlydatban.entity.TaiKhoan;
import org.login.quanlydatban.entity.enums.ChucVu;
import org.login.quanlydatban.notification.Notification;
import org.login.quanlydatban.utilities.Clock;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class TrangChuController implements Initializable {

    @FXML
    private ImageView logo;

    @FXML
    private VBox rankingBoard;
    public static TaiKhoan taiKhoan;

    @FXML
    private Label time;

    @FXML
    private Menu khachHangMenu;

    @FXML
    private Menu lichDatMenu;

    @FXML
    private Menu nhaHangMenu;

    @FXML
    private Menu baoCaoMenu;
    @FXML
    private Menu nhanVienMenu;

    @FXML
    private Menu thongKeMenu;

    @FXML
    private Menu thucDonMenu;

    @FXML
    private Menu trangChuMenu;

    @FXML
    private TilePane tilePane;

    @FXML
    private ImageView avatar;

    @FXML
    private ImageView imageView;
    @FXML
    private BorderPane borderPane;

    private static BorderPane borderPaneStatic;

    @FXML
    private ImageView setting;

    private Stage stage;

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        TrangChuController.taiKhoan = taiKhoan;
        showTooltipForAvatar();


        if (stage == null || !stage.isShowing()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangBaoCaoVaoCa.fxml"));
                AnchorPane anchorPane = loader.load();

                Scene scene = new Scene(anchorPane);
                stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Báo Cáo Vào Ca");
                stage.setResizable(false);

                stage.setOnCloseRequest(e -> stage = null);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.showAndWait();
            } catch (IOException e) {
                Notification.thongBao("Hiển thị báo cáo vào ca không thành công", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        } else {
            stage.toFront();
        }
        if(TrangChuController.taiKhoan.getNhanVien().getChucVuNhanVien().equals(ChucVu.NHAN_VIEN)) {
            nhanVienMenu.setVisible(false);
            thucDonMenu.setVisible(false);
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
        borderPane.setCenter(anchorPane);
        anchorPane.prefWidthProperty().bind(borderPane.widthProperty());
        anchorPane.prefHeightProperty().bind(borderPane.heightProperty());

    }

    @FXML
    public void quanlytaikhoan() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangTaiKhoan.fxml"));
        AnchorPane anchorPane = loader.load();
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
            thongKeController.setTaiKhoan(TrangChuController.taiKhoan);
        } else {
            ThongKeQuanLyController thongKeController = (ThongKeQuanLyController) controller;
            thongKeController.setTaiKhoan(TrangChuController.taiKhoan);
        }

        borderPane.setCenter(anchorPane);

        anchorPane.prefWidthProperty().bind(borderPane.widthProperty());
        anchorPane.prefHeightProperty().bind(borderPane.heightProperty());
    }

    @FXML
    public void ketCa() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangBaoCaoKetCa.fxml"));
        AnchorPane anchorPane = loader.load();
        KetCaController ketCa = loader.getController();
        ketCa.setTaiKhoan(taiKhoan);

        Scene scene = new Scene(anchorPane);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Báo Cáo Kết Ca");
        stage.setOnCloseRequest(e -> stage = null);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

    }

    @FXML
    public void vaoCa() throws IOException {
        try {
            // Kiểm tra nếu báo cáo đã được lưu rồi
            if (VaoCaController.isBaoCaoSaved) {
                Notification.thongBao("Báo cáo vào ca đã được lưu rồi. Bạn không thể lưu lại.", Alert.AlertType.INFORMATION);
                return;
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangBaoCaoVaoCa.fxml"));
            AnchorPane anchorPane = loader.load();

            Scene scene = new Scene(anchorPane);
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Báo Cáo Vào Ca");
            stage.setOnCloseRequest(e -> stage = null);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            Notification.thongBao("Hiển thị báo cáo vào ca không thành công", Alert.AlertType.ERROR);
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
    public void khachHang() throws IOException {
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
        VaoCaController.setIsBaoCaoSaved(false);
        KetCaController.setIsKetCa(false);
        if (stage != null) {
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

    @FXML
    void xemLich(ActionEvent event) throws IOException {
        XemLichDatController.getInstance().loadLichDatFromCurrentWeek(null);
        borderPane.setCenter(XemLichDatController.getInstance().getRoot());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Clock clock = new Clock();
        clock.startClock(time);
        logo.setImage(new Image(String.valueOf(getClass().getResource("/org/login/quanlydatban/icons/tobologo.png"))));

        logo.setFitWidth(250); // Chiều rộng
        logo.setFitHeight(375); // Chiều cao
        logo.setPreserveRatio(true);
        Circle clip = new Circle(125, 125, 125); // Tọa độ và bán kính
        logo.setClip(clip);
        try {
            loadRankingBoard();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        showTooltipForAvatar();
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
            if(DangNhapController.isAdmin) {
                Stage stage = (Stage) setting.getScene().getWindow();
                stage.close();
                try {
                    dangXuat();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else if (KetCaController.isKetCa) {
                Stage stage = (Stage) setting.getScene().getWindow();
                stage.close();

                try {
                    dangXuat();
                } catch (IOException e) {

                    throw new RuntimeException(e);
                }
            } else Notification.thongBao("Bạn chưa làm báo cáo kết ca", Alert.AlertType.INFORMATION);
        });

        contextMenu.getItems().addAll(itemTaiKhoan, separatorMenuItem, itemDangXuat);


        setting.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                Bounds boundsInScreen = setting.localToScreen(setting.getBoundsInLocal());
                contextMenu.show(setting, boundsInScreen.getMinX() - 30, boundsInScreen.getMaxY() + 5);
            }
        });

        if(DangNhapController.isAdmin) {
            trangChuMenu.setVisible(false);
            baoCaoMenu.setVisible(false);
            lichDatMenu.setVisible(false);
            thucDonMenu.setVisible(false);
            thongKeMenu.setVisible(false);
            khachHangMenu.setVisible(false);
            nhaHangMenu.setVisible(false);
        }




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

    @FXML
    public void showTooltipForAvatar() {
        Tooltip tooltip = new Tooltip();

        if (taiKhoan != null && taiKhoan.getNhanVien() != null) {
            String url = taiKhoan.getNhanVien().getHinhAnh();
            avatar.setImage(new Image(getClass().getResource(url).toExternalForm()));
            avatar.setFitWidth(40); // Chiều rộng
            avatar.setFitHeight(60); // Chiều cao
            avatar.setPreserveRatio(true);
            Circle clip = new Circle(20, 20, 20); // Tọa độ và bán kính
            avatar.setClip(clip);
            String ten = taiKhoan.getNhanVien().getTenNhanVien();
            String chucVuText = taiKhoan.getNhanVien().getChucVuNhanVien() == ChucVu.NHAN_VIEN ? "Nhân viên" : "Quản lý";
            tooltip.setText(ten + "\n" + chucVuText);
            tooltip.setStyle("-fx-font-size: 16px; -fx-padding: 10;");
        } else {
            tooltip.setText("Không có thông tin nhân viên");
        }
        Tooltip.install(avatar, tooltip);
        avatar.setOnMouseEntered(event -> tooltip.show(avatar, event.getScreenX(), event.getScreenY() + 15));
        avatar.setOnMouseExited(event -> tooltip.hide());
    }

    @FXML
    public void trangChu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangChu.fxml"));
        BorderPane newBorderPane = loader.load();
        borderPane.setCenter(newBorderPane.getCenter());
        initialize(null, null);
    }

    public void loadRankingBoard() throws IOException {
        HoaDonDAO hoaDonDAO = new HoaDonDAO();

        List<Object[]> rankingFood = hoaDonDAO.layTop10MonAnTheoDoanhThuVaSoLuongBan();
        FXMLLoader titleLoader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/RankingCardTitle.fxml"));
        Node rankingTitleCard = titleLoader.load();
        tilePane.getChildren().add(rankingTitleCard);
        tilePane.setHgap(10);
        tilePane.setVgap(10);
        tilePane.setAlignment(Pos.CENTER); // Căn giữa
        tilePane.setPrefWidth(Region.USE_COMPUTED_SIZE); // Đảm bảo tự động tính toán kích thước
        tilePane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        int rank = 1;
        for (Object[] object : rankingFood) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/RankingCard.fxml"));
            AnchorPane rankingCard = loader.load();
            RankingCardController controller = loader.getController();
            if (rank % 2 != 0) {
                rankingCard.getStyleClass().remove("gradient1"); // Xóa class cũ
                rankingCard.getStyleClass().add("gradient4");    // Thêm class mới
            }
            controller.setMonAn(rank, object[0].toString(), Double.parseDouble(object[1].toString()), Integer.parseInt(object[2].toString()));
            tilePane.getChildren().add(rankingCard);
            rank++;
        }

    }

    public void showTrangNhanVien(AnchorPane anchorPane) {
        borderPane.setCenter(anchorPane);
        anchorPane.prefWidthProperty().bind(borderPane.widthProperty());
        anchorPane.prefHeightProperty().bind(borderPane.heightProperty());
    }

}
