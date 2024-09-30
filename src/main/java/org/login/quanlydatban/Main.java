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
        ban.setMaBan("1234");
        ban.setLoaiBan(LoaiBan.BAN_2_NGUOI);
        ban.setKhuVuc(KhuVuc.A);
        ban.setTrangThaiBan(TrangThaiBan.BAN_TRONG);
        LoaiMonAn loaiMonAn = new LoaiMonAn("001", "Lẩu", "Lẩu thái hải sản");
        MonAn monAn = new MonAn(loaiMonAn, "002", "Lẩu thái hải sản full topping", 9.0, "Nồi", "lau.png", TrangThaiMonAn.CO_SAN);
        session.save(monAn);
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