package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.login.quanlydatban.entity.LichDat;
import org.login.quanlydatban.hibernate.HibernateUtils;

public class LichDatDAO {
    public void taoLichDat(LichDat lichDat){
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();

        session.save(lichDat);

        session.getTransaction().commit();
        session.close();
    }
}
