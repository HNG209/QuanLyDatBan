package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.login.quanlydatban.entity.Ban;
import org.login.quanlydatban.entity.LichDat;
import org.login.quanlydatban.entity.enums.LoaiBan;
import org.login.quanlydatban.entity.enums.TrangThaiHoaDon;
import org.login.quanlydatban.hibernate.HibernateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LichDatDAO {

    public void taoLichDat(LichDat lichDat){
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();

        session.persist(lichDat);

        session.getTransaction().commit();
        session.close();
    }

    public void capNhatLichDat(LichDat lichDat){
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();

        session.update(lichDat);

        session.getTransaction().commit();
        session.close();
    }

    public List<LichDat> getDSLichDat() {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();

        List<LichDat> list = session.createQuery("SELECT ld FROM LichDat ld", LichDat.class).getResultList();

        session.getTransaction().commit();
        session.close();

        return list;
    }

    public List<LichDat> getDSLichDatFrom(LocalDate from, LocalDate to, TrangThaiHoaDon trangThaiHoaDon) {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();
        String hql = "SELECT ld FROM LichDat ld " +
                "INNER JOIN HoaDon hd ON hd.maHoaDon = ld.hoaDon.maHoaDon " +
                "WHERE (:trangThai IS NULL OR hd.trangThaiHoaDon = :trangThai) AND " +
                "ld.thoiGianNhanBan BETWEEN DATE(:from) AND DATE(:to) " +
                "ORDER BY ld.thoiGianNhanBan ASC";

        List<LichDat> list = session.createQuery(hql, LichDat.class)
                .setParameter("trangThai", trangThaiHoaDon == null ? null : trangThaiHoaDon.name())
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
        String hql = "SELECT ld FROM LichDat ld " +
                "INNER JOIN ld.hoaDon hd " +
                "WHERE (:trangThai IS NULL OR hd.trangThaiHoaDon = :trangThai)";

        List<LichDat> list = session.createQuery(hql, LichDat.class)
                .setParameter("trangThai", trangThaiHoaDon.name())
                .getResultList();

        session.getTransaction().commit();
        session.close();

        return list;
    }

    public List<LichDat> getDSLichDatBy(String maLichDat, LocalDate ngayNhanBan, TrangThaiHoaDon trangThaiHoaDon, String cccd){
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();
        String hql = "SELECT ld FROM LichDat ld " +
                "INNER JOIN ld.hoaDon hd " +
                "INNER JOIN ld.khachHang kh " +
                "WHERE (:maLichDat IS NULL OR ld.maLichDat LIKE :maLichDat) AND " +
                "(:cccd IS NULL OR kh.cccd LIKE :cccd) AND " +
                "(:trangThai IS NULL OR hd.trangThaiHoaDon = :trangThai) AND " +
                "(:ngayNhanBan IS NULL OR ld.thoiGianNhanBan = :ngayNhanBan)";
        List<LichDat> list = session.createQuery(hql, LichDat.class)
                .setParameter("maLichDat", maLichDat)
                .setParameter("cccd", cccd)
                .setParameter("trangThai", trangThaiHoaDon == null ? null : trangThaiHoaDon.name())
                .setParameter("ngayNhanBan", ngayNhanBan)
                .getResultList();

        session.getTransaction().commit();
        session.close();

        return list;
    }

    public List<LichDat> getDSLichDatBy(LocalDateTime thoiGianNhanBan, Ban ban){
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();

        String hql = "SELECT ld FROM LichDat ld " +
                "INNER JOIN ld.hoaDon hd " +
                "WHERE ld.thoiGianNhanBan = :thoiGianNhanBan AND " +
                "hd.ban.maBan = :maBan";
        List<LichDat> list = session.createQuery(hql, LichDat.class)
                .setParameter("thoiGianNhanBan", thoiGianNhanBan)
                .setParameter("maBan", ban.getMaBan())
                .getResultList();

        session.getTransaction().commit();
        session.close();

        return list;
    }

    public List<LichDat> getLichDatIf(Ban ban) {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();
        String hql = "SELECT ld FROM LichDat ld " +
                "INNER JOIN ld.hoaDon hd " +
                "WHERE hd.trangThaiHoaDon = :trangThai AND " +
                "hd.ban.maBan = :maBan AND " +
                "ld.thoiGianNhanBan < :gioHienTai AND " +
                "DATE(ld.thoiGianNhanBan) = DATE(:gioHienTai)";
        List<LichDat> list = session.createQuery(hql, LichDat.class)
                .setParameter("trangThai", TrangThaiHoaDon.DA_DAT)
                .setParameter("maBan", ban.getMaBan())
                .setParameter("gioHienTai", LocalDateTime.now())
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
