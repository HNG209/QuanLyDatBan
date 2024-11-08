package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.login.quanlydatban.entity.LoaiMonAn;
import org.login.quanlydatban.entity.MonAn;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class LoaiMonDAO {
    private LoaiMonAn loaiMonAn;
    private List<LoaiMonAn> listLoai;

    public void themLoaiMonAn(LoaiMonAn loaiMonAn) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            session.save(loaiMonAn);
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

    public List<LoaiMonAn> getListLoai() {
        List<LoaiMonAn> listLoai = null;
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;
        try  {
            transaction = session.beginTransaction();
            // Sử dụng HQL để lấy tất cả mon an
            org.hibernate.query.Query<LoaiMonAn> query = session.createQuery("FROM LoaiMonAn", LoaiMonAn.class);
            listLoai  = query.list(); // Nhớ lưu kết quả vào danh sách
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception if needed
            listLoai = null;
        }
        return listLoai;
    }

    public LoaiMonAn getMaLoaiMon(String id) {
        LoaiMonAn loaiMonAn = null;
        try (Session session = HibernateUtils.getFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            loaiMonAn = session.get(LoaiMonAn.class, id); // Retrieve LoaiMonAn by primary key (id)

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception if needed
        }
        return loaiMonAn;
    }


    public LoaiMonAn getLoaiMonByName(String tenLoai) {
        Session session = HibernateUtils.getFactory().openSession();
        try {
            return session.createQuery("from LoaiMonAn where tenLoaiMonAn = :tenLoai", LoaiMonAn.class)
                    .setParameter("tenLoai", tenLoai)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }
}
