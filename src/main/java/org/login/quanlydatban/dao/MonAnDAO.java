package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.login.quanlydatban.entity.MonAn;
import org.login.quanlydatban.entity.NhanVien;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class MonAnDAO {
    private MonAn monAn;
    private List<MonAn> listMonAn;

    public MonAn getOneMonAn(String maMon) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;
        MonAn monAn = new MonAn();
        try {
            transaction = session.beginTransaction();
            monAn = session.createQuery("FROM MonAn WHERE maMonAn = :maMonAn", MonAn.class)
                    .setParameter("maMonAn", maMon)
                    .uniqueResult();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace(); // Xử lý ngoại lệ nếu có lỗi
        } finally {
            session.close();
        }
        return monAn;
    }

    public List<MonAn> readAll() {
        Session session = HibernateUtils.getFactory().openSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<MonAn> query = builder.createQuery(MonAn.class);
        Root root = query.from(MonAn.class);

        query = query.select(root);

        Query q = session.createQuery(query);
        listMonAn = q.getResultList();
        session.close();

        return listMonAn;
    }

    public List<MonAn> getAllMonAn() {
        List<MonAn> monAnList = null;
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            // Sử dụng HQL để lấy tất cả mon an
            org.hibernate.query.Query<MonAn> query = session.createQuery("FROM MonAn", MonAn.class);
            monAnList  = query.list(); // Nhớ lưu kết quả vào danh sách
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace(); // Xử lý ngoại lệ nếu có lỗi
        } finally {
            session.close();
        }
        return monAnList ;
    }



    public void themMonAn(MonAn monAn) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            session.save(monAn);
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


    public void xoaMonAn(String maMonAn) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            MonAn monAn = session.createQuery("FROM MonAn WHERE maMonAn = :maMonAn", MonAn.class)
                    .setParameter("maMonAn", maMonAn)
                    .uniqueResult();

            if (monAn != null) {
                session.delete(monAn); // Delete the object
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }



    public void capNhatMonAn(MonAn monAn) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            session.update(monAn);
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

    public void capNhatMonAn(MonAn monCu, MonAn monAnMoi) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            monAnMoi.setHinhAnh("haha");
            session.update(monAnMoi);

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

    public List<MonAn> getListMonAn() {
        return this.listMonAn;
    }
}
