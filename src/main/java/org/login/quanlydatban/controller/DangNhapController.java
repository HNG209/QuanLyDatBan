    package org.login.quanlydatban.controller;

    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.fxml.Initializable;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.PasswordField;
    import javafx.scene.control.TextField;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.input.KeyCode;
    import javafx.scene.layout.AnchorPane;
    import javafx.stage.Stage;
    import org.login.quanlydatban.dao.TaiKhoanDAO;
    import org.login.quanlydatban.encryptionUtils.EncryptionUtils;
    import org.login.quanlydatban.entity.TaiKhoan;

    import java.io.IOException;
    import java.net.URL;
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
        private TaiKhoanDAO taiKhoanDAO;

        @FXML
        public void nhanEnterDeDangNhap(){
             showPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!password.getText().equals(newValue)) {
                    password.setText(newValue);
                }
            });

            // Khi có thay đổi ở trường password, cập nhật trường pwd
            password.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!showPasswordField.getText().equals(newValue)) {
                    showPasswordField.setText(newValue);
                }
            });
            username.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    try {
                        dangNhap();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
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
            String inputUsername = username.getText().trim();
            String inputPassword = password.getText().trim();

            if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                System.out.println("Vui lòng nhập tài khoản và mật khẩu");
                return;
            }

            // Kiểm tra đặc biệt cho tài khoản admin
            if ("admin".equalsIgnoreCase(inputUsername)) {
                if ("admin".equals(inputPassword)) { // So sánh trực tiếp với defaultAccount
                    System.out.println("Đăng nhập admin thành công");
                    isAdmin = true;
                    showTrangChu(null); // Hoặc truyền null nếu không cần thông tin tài khoản
                } else {
                    System.out.println("Mật khẩu admin không đúng");
                }
                return;
            }

            // Kiểm tra tài khoản từ cơ sở dữ liệu
            TaiKhoan taiKhoan;
            try {
                taiKhoan = taiKhoanDAO.getTaiKhoan(inputUsername);
            } catch (Exception e) {
                System.out.println("Lỗi khi truy vấn dữ liệu: " + e.getMessage());
                return;
            }

            if (taiKhoan == null) {
                System.out.println("Tài khoản không tồn tại");
            } else {
                String encryptedPassword = EncryptionUtils.encrypt(inputPassword, System.getenv("ENCRYPTION_KEY"));
                if (encryptedPassword.equals(taiKhoan.getPassword()) || (taiKhoan.getPassword().equals("1111") && inputPassword.equals("1111"))) {
                    System.out.println("Đăng nhập thành công");
                    showTrangChu(taiKhoan);
                } else {
                    System.out.println("Sai mật khẩu");
                }
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
        public void thoat(){
            System.exit(0);
        }

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {

            taiKhoanDAO = new TaiKhoanDAO();
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
            if(taiKhoan != null) {
                trangChuController.setTaiKhoan(taiKhoan);
            }
            else {
                FXMLLoader loaderNhanVien = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/QuanLyNhanVien_XemDS.fxml"));
                AnchorPane anchorPaneNhanVien = loaderNhanVien.load();
                trangChuController.showTrangNhanVien(anchorPaneNhanVien);
            }
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        }
    }
