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


    public void updateNhanVien(NhanVien nhanVien) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction(); // Bắt đầu giao dịch
            session.update(nhanVien); // Lưu nhân viên vào cơ sở dữ liệu
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



    public void updateNhanVien(String maNhanVien, NhanVien nhanVienMoi) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction(); // Bắt đầu giao dịch

            // Tìm nhân viên theo mã
            NhanVien nhanVienCu = session.createQuery("FROM NhanVien WHERE maNhanVien = :maNhanVien", NhanVien.class)
                    .setParameter("maNhanVien", maNhanVien)
                    .uniqueResult();

            if (nhanVienCu != null) {
                nhanVienCu.setMaNhanVien(nhanVienMoi.getMaNhanVien());
                // Cập nhật thông tin nhân viên cũ từ nhân viên mới
                nhanVienCu.setTenNhanVien(nhanVienMoi.getTenNhanVien());
                nhanVienCu.setNgaySinh(nhanVienMoi.getNgaySinh());
                nhanVienCu.setDiaChi(nhanVienMoi.getDiaChi());
                nhanVienCu.setGioiTinh(nhanVienMoi.isGioiTinh());
               // nhanVienCu.setHinhAnh(nhanVienMoi.getHinhAnh());
                nhanVienCu.setSdt(nhanVienMoi.getSdt());
                nhanVienCu.setCccd(nhanVienMoi.getCccd());
//              nhanVienCu.setTrangThaiNhanVien(nhanVienMoi.getTrangThaiNhanVien());
//              nhanVienCu.setChucVuNhanVien(nhanVienMoi.getChucVuNhanVien());
                session.update(nhanVienCu); // Cập nhật nhân viên vào cơ sở dữ liệu
                transaction.commit(); // Cam kết giao dịch
            } else {
                System.out.println("Không tìm thấy nhân viên với mã: " + maNhanVien);
            }
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
