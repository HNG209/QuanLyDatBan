package org.login.quanlydatban.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.login.quanlydatban.entity.NhanVien;
import org.login.quanlydatban.entity.TaiKhoan;
import org.login.quanlydatban.hibernate.HibernateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TaiKhoanDAO {
    private NhanVienDAO nhanVienDAO;
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

    public TaiKhoan getTaiKhoanNhanVien(String maNhanVien) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;
        TaiKhoan taiKhoan = null;

        try {
            transaction = session.beginTransaction();

            // Lấy NhanVien theo maNhanVien
            NhanVien nhanVien = session.get(NhanVien.class, maNhanVien);
            if (nhanVien != null) {
                // Lấy TaiKhoan liên quan đến NhanVien
                taiKhoan = nhanVien.getTaiKhoan();
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace(); // Xử lý lỗi ở đây
        } finally {
            session.close();
        }

        return taiKhoan;
    }

    public void addNhanVien(TaiKhoan taiKhoan) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(taiKhoan);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


}
