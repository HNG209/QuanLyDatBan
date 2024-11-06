package org.login.quanlydatban.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.login.quanlydatban.entity.TaiKhoan;
import org.login.quanlydatban.hibernate.HibernateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TaiKhoanDAO {
    private TaiKhoan taiKhoan;
    public TaiKhoan getTaiKhoan(String username){
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();
        if(taiKhoan != null){
            if(taiKhoan.getUserName().equals(username)){
                return taiKhoan;
            }
        }
        taiKhoan = session.get(TaiKhoan.class, username);
        session.getTransaction().commit();
        session.close();
        return taiKhoan;
    }


    // lay ra bang danh sach tai khoan load len trang QuanLyTaiKhoan

}
