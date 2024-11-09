package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.login.quanlydatban.entity.Ban;
import org.login.quanlydatban.entity.enums.TrangThaiBan;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class BanDAO {
    private List<Ban> listBan;

    public List<Ban> readAll() {
        Session session = HibernateUtils.getFactory().openSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Ban> query = builder.createQuery(Ban.class);
        Root root = query.from(Ban.class);
        query = query.select(root);

        Query q = session.createQuery(query);

        listBan = q.getResultList();

        session.close();

        return this.listBan;
    }

    public List<Ban> readByStatus(TrangThaiBan trangThaiBan){
        Session session = HibernateUtils.getFactory().openSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Ban> query = builder.createQuery(Ban.class);
        Root root = query.from(Ban.class);
        query = query.select(root);

        Predicate p1;
        switch (trangThaiBan) {
            case BAN_TRONG -> p1 = builder.like(root.get("trangThaiBan").as(String.class), "BAN_TRONG");
            case DANG_PHUC_VU -> p1 = builder.like(root.get("trangThaiBan").as(String.class), "DANG_PHUC_VU");
            case DA_DAT -> p1 = builder.like(root.get("trangThaiBan").as(String.class), "DA_DAT");
            default -> p1 = builder.like(root.get("trangThaiBan").as(String.class), "TAM_NGUNG_PHUC_VU");
        }

        query = query.where(p1);
        Query q = session.createQuery(query);

        listBan = q.getResultList();

        session.close();

        return this.listBan;
    }

    public Ban updateBan(Ban ban, TrangThaiBan trangThaiBan) {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();

        Ban currentBan = session.get(Ban.class, ban.getMaBan());
        currentBan.setTrangThaiBan(trangThaiBan);
        session.save(currentBan);

        session.getTransaction().commit();
        session.close();

        return currentBan;
    }

    public List<Ban> getListBan() {
        return this.listBan;
    }
    public Ban themBan(Ban ban) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(ban);
            transaction.commit();
            System.out.println("Thêm bàn mới thành công!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.out.println("Thêm bàn thất bại.");
        } finally {
            session.close();
        }
        return ban;
    }


    public Ban capnhatBan(Ban ban) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            // Lấy đối tượng `Ban` từ cơ sở dữ liệu
            Ban currentBan = session.get(Ban.class, ban.getMaBan());
            if (currentBan != null) {
                // Cập nhật các thuộc tính
                currentBan.setTrangThaiBan(ban.getTrangThaiBan());
                currentBan.setKhuVuc(ban.getKhuVuc());
                currentBan.setLoaiBan(ban.getLoaiBan());

                session.update(currentBan); // Lưu thay đổi vào database
                transaction.commit();       // Commit để xác nhận cập nhật
                System.out.println("Cập nhật bàn thành công!");
            } else {
                System.out.println("Không tìm thấy bàn để cập nhật.");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback trong trường hợp có lỗi
            }
            e.printStackTrace();
            System.out.println("Cập nhật bàn thất bại.");
        } finally {
            session.close();
        }
        return ban; // Trả về đối tượng đã cập nhật
    }

    public boolean deleteBan(String maBan) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;
        boolean isDeleted = false;

        try {
            transaction = session.beginTransaction();


            Ban ban = session.get(Ban.class, maBan);
            if (ban != null) {
                session.delete(ban);
                transaction.commit();
                System.out.println("Xóa bàn thành công!");
                isDeleted = true;
            } else {
                System.out.println("Không tìm thấy bàn để xóa.");
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.out.println("Xóa bàn thất bại.");
        } finally {
            session.close();
        }
        return isDeleted;
    }
}
