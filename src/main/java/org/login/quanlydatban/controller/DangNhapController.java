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
        private  TrangChuController trangchuctr;

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
            TaiKhoan taiKhoan = taiKhoanDAO.getTaiKhoan(username.getText());
            if(taiKhoan != null){
                if(password.getText().equals("")) {
                    System.out.println("Vui long nhap mat khau");
                }
                else if(EncryptionUtils.encrypt(password.getText(), System.getenv("ENCRYPTION_KEY")).equals(taiKhoan.getPassword())){
                    System.out.println("dang nhap thanh cong");
                    showTrangChu(taiKhoan);
                }
                else
                    System.out.println("sai mat khau");
            }
            else System.out.println("khong ton tai");
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

            //close current stage, stage with UNDECORATED style
            Stage stage = (Stage) username.getScene().getWindow();
            stage.close();

            //init a new stage with no style add on
            stage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/org/login/quanlydatban/stylesheets/style.css")).toExternalForm());

            //passing information to TrangChu
            TrangChuController trangChuController = loader.getController();
            trangChuController.setTaiKhoan(taiKhoan);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        }
    }
