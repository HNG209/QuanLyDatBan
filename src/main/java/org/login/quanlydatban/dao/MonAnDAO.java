package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.login.quanlydatban.entity.MonAn;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class MonAnDAO {
    private List<MonAn> listMonAn;

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

    public List<MonAn> getAllMonAn(int index, int limit) {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();
        List<MonAn> monAnList = new ArrayList<>();
        String hql = "FROM MonAn";

        monAnList = session.createQuery(hql, MonAn.class)
                .setFirstResult(index * limit) // OFFSET
                .setMaxResults(limit)          // LIMIT
                .getResultList();

        session.getTransaction().commit();
        session.close();

        return monAnList;
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


    public void capNhatMonAn(MonAn monCu, MonAn monAnMoi) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            //monAnMoi.setHinhAnh("haha");
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

    public List<String> getListDon() {
        List<String> listDon = null;
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;
        try  {
            transaction = session.beginTransaction();
            // Sử dụng HQL để lấy tất cả mon an
            org.hibernate.query.Query<String> query = session.createQuery("SELECT DISTINCT m.donViTinh FROM MonAn m", String.class);
            listDon  = query.list(); // Nhớ lưu kết quả vào danh sách
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception if needed
            listDon = null;
        }
        return listDon;
    }


    public List<MonAn> getMonAnBy(String ten, double giaTT, double giaTD, String loai, int index, int limit) {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();
        String hql = "SELECT m FROM MonAn m " +
                "INNER JOIN LoaiMonAn l ON l.maLoaiMonAn = m.loaiMonAn.maLoaiMonAn " +
                "WHERE (:loai IS NULL OR l.tenLoaiMonAn LIKE :loai) AND " +
                "(:ten IS NULL OR m.tenMonAn LIKE :ten) AND " +
                "(:giaTT = 0.0 OR m.donGia >= :giaTT) AND " +
                "(:giaTD = 0.0 OR m.donGia <= :giaTD)";
        List<MonAn> monAnList = session.createQuery(hql, MonAn.class)
                .setParameter("loai", "%" + loai + "%")
                .setParameter("ten", "%" + ten + "%")
                .setParameter("giaTT", giaTT)
                .setParameter("giaTD", giaTD)
                .setFirstResult(index * limit)
                .setMaxResults(limit)
                .getResultList();


        session.getTransaction().commit();
        session.close();

        return monAnList;
    }

    public int countMonAnBy(String ten, double giaTT, double giaTD, String loai){
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();
        String hql = "SELECT m FROM MonAn m " +
                "INNER JOIN m.loaiMonAn l " +
                "WHERE (:loai IS NULL OR l.tenLoaiMonAn LIKE :loai) AND " +
                "(:ten IS NULL OR m.tenMonAn LIKE :ten) AND " +
                "(:giaTT = 0.0 OR m.donGia >= :giaTT) AND " +
                "(:giaTD = 0.0 OR m.donGia <= :giaTD)";
        List<MonAn> monAnList = session.createQuery(hql, MonAn.class)
                .setParameter("loai", "%" + loai + "%")
                .setParameter("ten", "%" + ten + "%")
                .setParameter("giaTT", giaTT)
                .setParameter("giaTD", giaTD)
                .getResultList();

        session.getTransaction().commit();
        session.close();

        return monAnList.size();
    }

    public List<MonAn> getListMonAn() {
        return this.listMonAn;
    }
}
