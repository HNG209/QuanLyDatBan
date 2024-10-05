package org.login.quanlydatban;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.login.quanlydatban.entity.*;
import org.login.quanlydatban.hibernate.HibernateUtils;

import java.io.IOException;
import java.time.LocalDate;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TrangDangNhap.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        SessionFactory sessionFactory = HibernateUtils.getFactory();

        Session session = HibernateUtils.getFactory().openSession();

        session.getTransaction().begin();

        Ban ban = new Ban();
        ban.setMaBan("1235");
        ban.setLoaiBan(LoaiBan.BAN_2_NGUOI);
        ban.setKhuVuc(KhuVuc.A);
        ban.setTrangThaiBan(TrangThaiBan.BAN_TRONG);

        session.save(ban);//persistent
        session.getTransaction().commit();
        session.close();

        stage.setTitle("Đăng nhập");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}