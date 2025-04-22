package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.login.quanlydatban.entity.KhachHang;
import org.login.quanlydatban.entity.keygenerator.DailyCustomerCounter;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {
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
                    rootKH.get("cccd"),
                    rootKH.get("diaChi"),
                    rootKH.get("email"),
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

        khachHang.setMaKhachHang(this.generateCustomId());
        session.persist(khachHang);

        session.getTransaction().commit();
        session.close();
        return khachHang;
    }

    public KhachHang getKHBySDT(String sdt) {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();

        KhachHang khachHang = session.createQuery("SELECT kh FROM KhachHang kh WHERE kh.sdt LIKE :sdt", KhachHang.class)
                .setParameter("sdt", sdt)
                .getSingleResult();

        session.getTransaction().commit();
        session.close();

        return khachHang;
    }

    public KhachHang getKHByCCCD(String cccd) {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();

        KhachHang khachHang = session.createQuery("SELECT kh FROM KhachHang kh WHERE kh.cccd LIKE :cccd", KhachHang.class)
                .setParameter("cccd", cccd)
                .getSingleResult();

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

    public KhachHang timKhachHangTheoSDT(String sdt) {
        Session session = HibernateUtils.getFactory().openSession();

        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<KhachHang> query = builder.createQuery(KhachHang.class);

            Root<KhachHang> root = query.from(KhachHang.class);
            query.select(root).where(builder.equal(root.get("sdt"), sdt));

            List<KhachHang> resultList = session.createQuery(query).getResultList();

            if (!resultList.isEmpty()) {
                return resultList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return null;
    }

    private String generateCustomId() {
        String prefix = "KH";
        int currentYear = LocalDate.now().getYear();
        int counterValue = getAndUpdateDailyCounter(currentYear);
        return prefix + currentYear + String.format("%04d", counterValue);
    }

    private int getAndUpdateDailyCounter(int currentYear) {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();
        DailyCustomerCounter dailyCounter = session.find(DailyCustomerCounter.class, currentYear);
        int counterValue;

        if (dailyCounter != null) {
            counterValue = dailyCounter.getCounterValue() + 1;
            dailyCounter.setCounterValue(counterValue);
        } else {
            counterValue = 1;
            dailyCounter = new DailyCustomerCounter();
            dailyCounter.setCounterDate(currentYear);
            dailyCounter.setCounterValue(counterValue);
            session.persist(dailyCounter);
        }

        session.getTransaction().commit();
        session.close();
        return counterValue;
    }
}
