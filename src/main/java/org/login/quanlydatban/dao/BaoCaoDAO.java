package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.login.quanlydatban.entity.BaoCao;
import org.login.quanlydatban.hibernate.HibernateUtils;

public class BaoCaoDAO {
    public boolean themBaoCao(BaoCao baoCao) {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();
        session.persist(baoCao);
        session.getTransaction().commit();
        session.close();
        return true;
    }
}
