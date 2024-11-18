package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.login.quanlydatban.entity.KhachHang;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {
    private List<Object[]> dsKhachHang;
    public List<Object[]> layDSKhachHang() {
        List<Object[]> resultList = new ArrayList<>();
        Session session = HibernateUtils.getFactory().openSession();

        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);

            Root<KhachHang> rootKH = query.from(KhachHang.class);
            query.multiselect(
                    rootKH.get("maKhachHang"),
                    rootKH.get("tenKhachHang"),
                    rootKH.get("sdt"),
                    rootKH.get("email"),
                    rootKH.get("diaChi"),
                    rootKH.get("cccd"),
                    rootKH.get("diemTichLuy")
            );

            resultList = session.createQuery(query).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return resultList;
    }
    public KhachHang themKhachHang(KhachHang khachHang) {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();
        session.persist(khachHang);
        session.getTransaction().commit();
        session.close();
        return khachHang;
    }
    public boolean suaKhachHang(KhachHang khachHang) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.update(khachHang);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
    public KhachHang layKhachHangDauTienTheoNam() {
        Session session = HibernateUtils.getFactory().openSession();
        int year = LocalDate.now().getYear();
        KhachHang khachHang = null;

        try {
            String yearPattern = "%" + year + "%";

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<KhachHang> query = builder.createQuery(KhachHang.class);
            Root<KhachHang> root = query.from(KhachHang.class);

            query.select(root)
                    .where(builder.like(root.get("maKhachHang"), yearPattern))
                    .orderBy(builder.desc(root.get("maKhachHang")));

            List<KhachHang> result = session.createQuery(query)
                    .setMaxResults(1)
                    .getResultList();

            if (!result.isEmpty()) {
                khachHang = result.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return khachHang;
    }



}
