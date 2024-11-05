package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.login.quanlydatban.entity.LoaiMonAn;
import org.login.quanlydatban.entity.MonAn;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class LoaiMonDAO {
    private LoaiMonAn loaiMonAn;
    private List<LoaiMonAn> listLoai;

    public List<LoaiMonAn> getListLoai() {
        List<LoaiMonAn> listLoai;
        try (Session session = HibernateUtils.getFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<LoaiMonAn> query = builder.createQuery(LoaiMonAn.class);
            query.from(LoaiMonAn.class);

            listLoai = session.createQuery(query).getResultList();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception if needed
            listLoai = null;
        }
        return listLoai;
    }

    public LoaiMonAn getMaLoaiMon(String id) {
        LoaiMonAn loaiMonAn = null;
        try (Session session = HibernateUtils.getFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            loaiMonAn = session.get(LoaiMonAn.class, id); // Retrieve LoaiMonAn by primary key (id)

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception if needed
        }
        return loaiMonAn;
    }

}
