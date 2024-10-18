package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.login.quanlydatban.entity.TaiKhoan;
import org.login.quanlydatban.hibernate.HibernateUtils;

public class TaiKhoanDAO {
    private TaiKhoan taiKhoan;
    public TaiKhoan getTaiKhoan(String username){
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();
        if(taiKhoan != null){
            if(taiKhoan.getUserName().equals(username)){
                return taiKhoan;
            }
        }
        taiKhoan = session.get(TaiKhoan.class, username);
        session.getTransaction().commit();
        session.close();
        return taiKhoan;
    }
}
