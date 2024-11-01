package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.mapping.Array;
import org.hibernate.query.Query;
import org.login.quanlydatban.entity.NhanVien;
import org.login.quanlydatban.entity.TaiKhoan;
import org.login.quanlydatban.hibernate.HibernateUtils;

import java.util.List;

public class NhanVienDAO {
    private NhanVien nhanVien;
    public List<NhanVien> getAllTaiKhoan() {
        List<NhanVien> taiKhoanList = null;
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            // Sử dụng HQL để lấy tất cả nhân viên
            Query<NhanVien> query = session.createQuery("FROM NhanVien", NhanVien.class);
            taiKhoanList = query.list(); // Nhớ lưu kết quả vào danh sách
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace(); // Xử lý ngoại lệ nếu có lỗi
        } finally {
            session.close();
        }
        return taiKhoanList;
    }

    public NhanVien getNhanVien(String tenString){
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();
        if(nhanVien != null){
            if(nhanVien.getTenNhanVien().equals(tenString)){
                return nhanVien;
            }
        }
        nhanVien = session.get(NhanVien.class, tenString);
        session.getTransaction().commit();
        session.close();
        return nhanVien;
    }

    // them nhan vien xuong co so du lieu
    public void addNhanVien(NhanVien nhanVien) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction(); // Bắt đầu giao dịch
            session.save(nhanVien); // Lưu nhân viên vào cơ sở dữ liệu
            transaction.commit(); // Cam kết giao dịch
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Hoàn tác giao dịch nếu có lỗi
            }
            e.printStackTrace(); // In ra lỗi
        } finally {
            session.close(); // Đảm bảo đóng session
        }
    }



}
