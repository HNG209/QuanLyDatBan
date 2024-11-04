package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.login.quanlydatban.entity.Ban;
import org.login.quanlydatban.entity.MonAn;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class MonAnDAO {
    private List<MonAn> listMonAn;

    public List<MonAn> readAll() {
        Session session = HibernateUtils.getFactory().openSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<MonAn> query = builder.createQuery(MonAn.class);
        Root root = query.from(MonAn.class);

        query = query.select(root);

        Query q = session.createQuery(query);
        listMonAn = q.getResultList();

        session.close();

        return listMonAn;
    }

    public MonAn getMonAnByID(String id) {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();

        MonAn monAn = session.get(MonAn.class, id);

        session.getTransaction().commit();
        session.close();

        return monAn;
    }
    public List<MonAn> getListMonAn() {
        return this.listMonAn;
    }
}
