package org.login.quanlydatban;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.login.quanlydatban.entity.*;
import org.login.quanlydatban.hibernate.HibernateUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TrangDangNhap.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Session session = HibernateUtils.getFactory().openSession();
//        session.getTransaction().begin();
//
//
//        session.getTransaction().commit();
        session.close();

        stage.setTitle("Đăng nhập");
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}