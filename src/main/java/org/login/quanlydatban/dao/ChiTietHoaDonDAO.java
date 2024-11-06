package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.login.quanlydatban.entity.ChiTietHoaDon;
import org.login.quanlydatban.hibernate.HibernateUtils;

import java.util.List;

public class ChiTietHoaDonDAO {

    public List<ChiTietHoaDon> getChiTietHoaDonByMaHoaDon(String maHoaDon) {
        List<ChiTietHoaDon> chiTietList = null;


        try (Session session = HibernateUtils.getFactory().openSession()) {


            String hql = "FROM ChiTietHoaDon WHERE hoaDon.maHoaDon = :maHoaDon";
            Query<ChiTietHoaDon> query = session.createQuery(hql, ChiTietHoaDon.class);
            query.setParameter("maHoaDon", maHoaDon);

            chiTietList = query.list();


        } catch (Exception e) {

            e.printStackTrace();
        }

        return chiTietList;
    }
}

