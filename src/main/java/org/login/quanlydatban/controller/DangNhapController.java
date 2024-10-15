    package org.login.quanlydatban.controller;

    import javafx.beans.binding.Bindings;
    import javafx.fxml.FXML;
    import javafx.fxml.Initializable;
    import javafx.scene.control.PasswordField;
    import javafx.scene.control.TextField;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.input.KeyCode;
    import org.hibernate.Session;
    import org.login.quanlydatban.entity.TaiKhoan;
    import org.login.quanlydatban.hibernate.HibernateUtils;

    import javax.naming.Binding;
    import java.net.URL;
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
                    dangNhap();
                    password.requestFocus();

                }
            });

            password.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    dangNhap();
                }
            });
            showPasswordField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    dangNhap();
                }
            });

        }
        @FXML
        public void dangNhap(){
            Session session = HibernateUtils.getFactory().openSession();
            session.getTransaction().begin();

            TaiKhoan taiKhoan = session.get(TaiKhoan.class, username.getText());
            if(taiKhoan != null){
                if(password.getText().equals("")) {
                    System.out.println("Vui long nhap mat khau");
                }
                else if(taiKhoan.getPassword().equals(password.getText())){
                    System.out.println("dang nhap thanh cong");
                    System.out.println(taiKhoan);
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

    }
