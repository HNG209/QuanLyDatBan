package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.login.quanlydatban.entity.Ban;
import org.login.quanlydatban.entity.ChiTietHoaDon;
import org.login.quanlydatban.entity.HoaDon;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonDAO {
    public ChiTietHoaDon luuCTHD(ChiTietHoaDon chiTietHoaDon) {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();

        session.save(chiTietHoaDon);

        session.getTransaction().commit();
        session.close();
        return chiTietHoaDon;
    }

    public List<ChiTietHoaDon> getCTHDfromHD(HoaDon hoaDon) {
        List<ChiTietHoaDon> list = new ArrayList<>();

        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ChiTietHoaDon> query = builder.createQuery(ChiTietHoaDon.class);
        Root<ChiTietHoaDon> chiTietHoaDonRoot = query.from(ChiTietHoaDon.class);

        Join<ChiTietHoaDon, HoaDon> joinHD = chiTietHoaDonRoot.join("hoaDon");

        Predicate predicate = builder.equal(joinHD.get("maHoaDon"), hoaDon.getMaHoaDon());

        query.select(chiTietHoaDonRoot).where(predicate);

        list = session.createQuery(query).getResultList();

        session.getTransaction().commit();
        session.close();

        return list;
    }
}
