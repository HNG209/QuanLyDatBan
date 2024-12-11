package org.login.quanlydatban;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.Session;
import org.login.quanlydatban.dao.LichDatDAO;
import org.login.quanlydatban.hibernate.HibernateUtils;
import java.time.LocalDate;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/org/login/quanlydatban/views/TrangDangNhap.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Session session = HibernateUtils.getFactory().openSession();

          session.close();

        LichDatDAO lichDatDAO = new LichDatDAO();
        lichDatDAO.getDSLichDatBy("", LocalDate.of(2024, 12, 4), null, null).forEach(System.out::println);

        stage.setTitle("Đăng nhập");
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/org/login/quanlydatban/stylesheets/style.css")).toExternalForm());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}