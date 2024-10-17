    package org.login.quanlydatban.controller;

    import javafx.beans.binding.Bindings;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.fxml.Initializable;
    import javafx.scene.Node;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.PasswordField;
    import javafx.scene.control.TextField;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.input.KeyCode;
    import javafx.stage.Stage;
    import javafx.stage.StageStyle;
    import org.hibernate.Session;
    import org.login.quanlydatban.entity.TaiKhoan;
    import org.login.quanlydatban.hibernate.HibernateUtils;

    import javax.naming.Binding;
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
        @FXML
        public void nhanEnterDeDangNhap(){
            username.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    try {
                        dangNhap();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    password.requestFocus();

                }
            });

            password.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    try {
                        dangNhap();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            showPasswordField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    try {
                        dangNhap();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        @FXML
        public void dangNhap() throws IOException {
            Session session = HibernateUtils.getFactory().openSession();
            session.getTransaction().begin();

            TaiKhoan taiKhoan = session.get(TaiKhoan.class, username.getText());
            if(taiKhoan != null){
                if(password.getText().equals("")) {
                    System.out.println("Vui long nhap mat khau");
                }
                else if(taiKhoan.getPassword().equals(password.getText())){
                    System.out.println("dang nhap thanh cong");
                    showTrangChu();
                }
                else
                    System.out.println("sai mat khau");
            }
            else System.out.println("khong ton tai");

            session.getTransaction().commit();
            session.close();
        }
        @FXML
        public void hienMatKhau() {
            String currentImageUrl = img.getImage().getUrl();
            String showImageUrl = getClass().getResource("/org/login/quanlydatban/show.png").toString();
            String hideImageUrl = getClass().getResource("/org/login/quanlydatban/hide.png").toString();

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

        }

        public void showTrangChu() throws IOException {
            FXMLLoader loader = new FXMLLoader(DangNhapController.class.getResource("/org/login/quanlydatban/TrangChu.fxml"));
            Parent root = loader.load();

            //close current stage, stage with UNDECORATED style
            Stage stage = (Stage) username.getScene().getWindow();
            stage.close();

            //init a new stage with no style add on
            stage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/org/login/quanlydatban/style.css")).toExternalForm());

            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        }
    }
