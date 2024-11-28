package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.login.quanlydatban.entity.LichDat;
import org.login.quanlydatban.entity.enums.TrangThaiHoaDon;
import org.login.quanlydatban.hibernate.HibernateUtils;

import java.time.LocalDate;
import java.util.List;

public class LichDatDAO {
    public void taoLichDat(LichDat lichDat){
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();

        session.persist(lichDat);

        session.getTransaction().commit();
        session.close();
    }

    public List<LichDat> getDSLichDat() {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();

        List<LichDat> list = session.createNativeQuery("SELECT * FROM lichDat", LichDat.class).getResultList();

        session.getTransaction().commit();
        session.close();

        return list;
    }

    public List<LichDat> getDSLichDatFrom(LocalDate from, LocalDate to) {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();

        List<LichDat> list = session.createNativeQuery("SELECT * FROM lichDat " +
                        "WHERE DATE(thoiGianNhanBan) BETWEEN :from AND :to " +
                        "ORDER BY thoiGianNhanBan ASC", LichDat.class)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();

        session.getTransaction().commit();
        session.close();

        return list;
    }

    public List<LichDat> getDSLichDatByStatus(TrangThaiHoaDon trangThaiHoaDon) {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();

        List<LichDat> list = session.createNativeQuery("SELECT l.* FROM lichDat AS l " +
                "INNER JOIN hoaDon ON hoaDon.maHoaDon = l.hoaDon_maHoaDon " +
                "WHERE hoaDon.trangThaiHoaDon LIKE :trangThai", LichDat.class)
                .setParameter("trangThai", trangThaiHoaDon.name())
                .getResultList();

        session.getTransaction().commit();
        session.close();

        return list;
    }

    public LichDat getLichDat(String maLichDat) {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();

        LichDat lichDat = session.get(LichDat.class, maLichDat);

        session.getTransaction().commit();
        session.close();

        return lichDat;
    }
}
