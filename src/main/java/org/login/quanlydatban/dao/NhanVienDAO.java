package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.mapping.Array;
import org.hibernate.query.Query;
import org.login.quanlydatban.entity.NhanVien;
import org.login.quanlydatban.hibernate.HibernateUtils;

import java.util.List;

public class NhanVienDAO {
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

}
