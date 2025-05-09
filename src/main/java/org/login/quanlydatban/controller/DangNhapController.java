    package org.login.quanlydatban.controller;

    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.fxml.Initializable;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.Alert;
    import javafx.scene.control.PasswordField;
    import javafx.scene.control.TextField;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.input.KeyCode;
    import javafx.scene.layout.AnchorPane;
    import javafx.stage.Stage;
//    import org.login.quanlydatban.TaiKhoanDAO;
    import org.login.quanlydatban.utilities.RMIServiceUtils;
    import org.login.service.TaiKhoanService;
    import org.login.quanlydatban.encryptionUtils.EncryptionUtils;
    import org.login.entity.TaiKhoan;
    import org.login.quanlydatban.notification.Notification;

    import java.io.IOException;
    import java.net.MalformedURLException;
    import java.net.URL;
    import java.rmi.Naming;
    import java.rmi.NotBoundException;
    import java.rmi.RemoteException;
    import java.util.Objects;
    import java.util.ResourceBundle;

    public class DangNhapController implements Initializable {
        @FXML
        private PasswordField password;
        @FXML
        private TextField showPasswordField;
        @FXML
        private TextField username;
        @FXML
        private ImageView img;

        public static boolean isAdmin;
        private TaiKhoanService taiKhoanService;

        @FXML
        public void nhanEnterDeDangNhap() {
            showPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!password.getText().equals(newValue)) {
                    password.setText(newValue);
                }
            });
            password.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!showPasswordField.getText().equals(newValue)) {
                    showPasswordField.setText(newValue);
                }
            });
            username.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    password.requestFocus();

                }
            });

            password.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    try {
                        dangNhap();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            showPasswordField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    try {
                        dangNhap();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        @FXML
        public void dangNhap() throws Exception {
            try {
                String inputUsername = username.getText().trim();
                String inputPassword = password.getText().trim();

                if (inputUsername.isEmpty() && inputPassword.isEmpty()) {
                    throw new IllegalArgumentException("Vui lòng nhập thông tin đăng nhập");
                }

                if ("admin".equalsIgnoreCase(inputUsername)) {
                    if ("admin".equals(inputPassword)) {
                        isAdmin = true;
                        showTrangChu(null);
                    } else {
                        throw new IllegalArgumentException("Mật khẩu không chính xác");
                    }
                    return;
                }
                TaiKhoan taiKhoan;

                taiKhoan = taiKhoanService.getTaiKhoan(inputUsername);

                if (taiKhoan == null) {
                    throw new IllegalArgumentException("Tài khoản không tồn tại");
                } else {
                    String encryptedPassword = EncryptionUtils.encrypt(inputPassword, System.getenv("ENCRYPTION_KEY"));
                    if (encryptedPassword.equals(taiKhoan.getPassword()) || (taiKhoan.getPassword().equals("1111") && password.getText().equals("1111"))) {
                        showTrangChu(taiKhoan);
                    } else
                        throw new IllegalArgumentException("Sai mật khẩu");
                }
            } catch (Exception e) {
                Notification.thongBao(e.getMessage(), Alert.AlertType.ERROR);
            }
        }

        @FXML
        public void hienMatKhau() {
            String currentImageUrl = img.getImage().getUrl();
            String showImageUrl = getClass().getResource("/org/login/quanlydatban/icons/show.png").toString();
            String hideImageUrl = getClass().getResource("/org/login/quanlydatban/icons/hide.png").toString();

            if (currentImageUrl.equals(hideImageUrl)) {
                showPasswordField.setText(password.getText());
                showPasswordField.setVisible(true);
                password.setVisible(false);
                img.setImage(new Image(showImageUrl));
                img.toFront();
            } else {
                password.setText(showPasswordField.getText());
                showPasswordField.setVisible(false);
                password.setVisible(true);
                img.setImage(new Image(hideImageUrl));
                img.toFront();
            }
        }

        @FXML
        public void thoat() {
            System.exit(0);
        }

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            try {
                taiKhoanService = RMIServiceUtils.useTaiKhoanService();
            } catch (NotBoundException | MalformedURLException | RemoteException e) {
                Notification.thongBao("Không thể kết nối tới server", e.getMessage(), Alert.AlertType.WARNING);
                throw new RuntimeException(e);
            }
        }

        public void showTrangChu(TaiKhoan taiKhoan) throws IOException {
            FXMLLoader loader = new FXMLLoader(DangNhapController.class.getResource("/org/login/quanlydatban/views/TrangChu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) username.getScene().getWindow();
            stage.close();

            stage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/org/login/quanlydatban/stylesheets/style.css")).toExternalForm());

            TrangChuController trangChuController = loader.getController();
            if (taiKhoan != null) {
                trangChuController.setTaiKhoan(taiKhoan);
            } else {
                FXMLLoader loaderNhanVien = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/QuanLyNhanVien_XemDS.fxml"));
                AnchorPane anchorPaneNhanVien = loaderNhanVien.load();
                trangChuController.showTrangNhanVien(anchorPaneNhanVien);
            }
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        }
    }