package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.login.quanlydatban.entity.Ban;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class BanDAO {
    private List<Ban> listBan;

    public List<Ban> readAll() {
        Session session = HibernateUtils.getFactory().openSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Ban> query = builder.createQuery(Ban.class);
        Root root = query.from(Ban.class);

        query = query.select(root);

        Query q = session.createQuery(query);

        listBan = q.getResultList();

        session.close();

        return this.listBan;
    }

    public List<Ban> getListBan() {
        return this.listBan;
    }
}
