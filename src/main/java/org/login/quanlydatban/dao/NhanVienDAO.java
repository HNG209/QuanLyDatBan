package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.mapping.Array;
import org.hibernate.query.Query;
import org.login.quanlydatban.entity.NhanVien;
import org.login.quanlydatban.entity.TaiKhoan;
import org.login.quanlydatban.hibernate.HibernateUtils;

import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {
    private NhanVien nhanVien;
    private TaiKhoanDAO taiKhoan;


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

    public void updateNhanVien(String maNhanVienCu, NhanVien nhanVienMoi) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;
        taiKhoan = new TaiKhoanDAO();

        try {
            transaction = session.beginTransaction(); // Bắt đầu giao dịch

            // Tìm nhân viên theo mã cũ
            NhanVien nhanVienCu = session.createQuery("FROM NhanVien WHERE maNhanVien = :maNhanVien", NhanVien.class)
                    .setParameter("maNhanVien", maNhanVienCu)
                    .uniqueResult();

            if (nhanVienCu != null) {
                if (nhanVienMoi == null) {
                    System.out.println("Thông tin nhân viên mới không hợp lệ.");
                    return;
                }

                nhanVienCu.setTenNhanVien(nhanVienMoi.getTenNhanVien());
                // Cập nhật thông tin nhân viên mà không thay đổi mã nhân viên
                nhanVienCu.setTenNhanVien(nhanVienMoi.getTenNhanVien());
                nhanVienCu.setNgaySinh(nhanVienMoi.getNgaySinh());
                nhanVienCu.setDiaChi(nhanVienMoi.getDiaChi());
                nhanVienCu.setGioiTinh(nhanVienMoi.isGioiTinh());
                nhanVienCu.setHinhAnh(nhanVienMoi.getHinhAnh());
                nhanVienCu.setSdt(nhanVienMoi.getSdt());
                nhanVienCu.setCccd(nhanVienMoi.getCccd());
                nhanVienCu.setTrangThaiNhanVien(nhanVienMoi.getTrangThaiNhanVien()); // Cập nhật trạng thái
                nhanVienCu.setChucVuNhanVien(nhanVienMoi.getChucVuNhanVien());
                System.out.println(nhanVienCu.getSdt());
                // Cập nhật tài khoản liên kết
                TaiKhoan tktim = taiKhoan.getTaiKhoanNhanVien(maNhanVienCu);
                if (tktim != null) {
                    tktim.setNhanVien(nhanVienCu); // Cập nhật nhân viên liên kết
                    tktim.setUserName(tktim.getUserName()); // Cập nhật nếu cần
                    tktim.setPassword(tktim.getPassword()); // Cập nhật nếu cần
                }

                // Lưu cập nhật
                session.update(nhanVienCu); // Sử dụng update để cập nhật nhân viên cũ
                transaction.commit(); // Cam kết giao dịch
                System.out.println("Cập nhật thành công." + nhanVienCu.getMaNhanVien());
            } else {
                System.out.println("Không tìm thấy nhân viên với mã: " + maNhanVienCu);
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
    public List<NhanVien> getNhanVienWithTaiKhoan() {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;
        List<NhanVien> nhanViens = new ArrayList<>();

        try {
            transaction = session.beginTransaction();
            String hql = "SELECT nv, tk FROM NhanVien nv JOIN nv.taiKhoan tk";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            // ND chinh sua
            List<Object[]> results = query.getResultList();
            for (Object[] result : results) {
                NhanVien nv = (NhanVien) result[0];
                TaiKhoan tk = (TaiKhoan) result[1];
//                nv.setTaiKhoan(tk);
//                nv.setTenTaiKhoan(tk.getUserName());
                nv.setTaiKhoan(tk); // Giả sử bạn có phương thức setTaiKhoan trong NhanVien
                nhanViens.add(nv);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return nhanViens; // Trả về danh sách nhân viên cùng với tài khoản
    }
}
