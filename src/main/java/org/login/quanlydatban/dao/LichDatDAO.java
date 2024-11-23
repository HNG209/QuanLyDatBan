package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.login.quanlydatban.entity.LichDat;
import org.login.quanlydatban.hibernate.HibernateUtils;

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

    public LichDat getLichDat(String maLichDat) {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();

        LichDat lichDat = session.get(LichDat.class, maLichDat);

        session.getTransaction().commit();
        session.close();

        return lichDat;
    }
}
