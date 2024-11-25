package org.login.quanlydatban;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.Session;

import org.hibernate.SessionFactory;
import org.login.quanlydatban.encryptionUtils.EncryptionUtils;
import org.login.quanlydatban.entity.*;
import org.login.quanlydatban.entity.enums.TrangThaiHoaDon;

import org.login.quanlydatban.hibernate.HibernateUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/org/login/quanlydatban/views/TrangDangNhap.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        //test ne bay
        Session session = HibernateUtils.getFactory().openSession();
        String s = EncryptionUtils.encrypt("12345", System.getenv("ENCRYPTION_KEY"));
        System.out.println(s);
//        LoaiMonDAO lm = new LoaiMonDAO();
//        lm.themLoaiMonAn(new LoaiMonAn("LM02", "Chie", "asadsa"));
//        session.getTransaction().begin();
//
//        Ban ban = session.get(Ban.class, "B1");
//
//        KhachHang khachHang = session.get(KhachHang.class, "KH12345");
//
//        NhanVien nhanVien = session.get(NhanVien.class, "NV001");
//
//        HoaDon hoaDon = new HoaDon();
//        hoaDon.setBan(ban);
//        hoaDon.setKhachHang(khachHang);
//        hoaDon.setNhanVien(nhanVien);
//        hoaDon.setTrangThaiHoaDon(TrangThaiHoaDon.CHUA_THANH_TOAN);
//        hoaDon.setPhuThu(10_000.0);
//        hoaDon.setNgayLap(LocalDate.now());
//
//        ChiTietHoaDon ch
//        iTietHoaDon = new ChiTietHoaDon();
//
//        session.save(hoaDon);
//        chiTietHoaDon.setHoaDon(hoaDon);
//        chiTietHoaDon.setMonAn(session.get(MonAn.class, "Mon1"));
//        chiTietHoaDon.setSoLuong(10);
//
//        session.save(chiTietHoaDon);
//
//        session.getTransaction().commit();
        session.close();
        //chay roi mo database len coi bang hoadon
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