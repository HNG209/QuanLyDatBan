package org.login.quanlydatban.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.login.quanlydatban.entity.*;

import java.util.Properties;

public class HibernateUtils {
    private static final SessionFactory FACTORY;

    static {
        Configuration configuration = new Configuration();

        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        properties.put(Environment.URL, "jdbc:mysql://localhost/nhahangtobo");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "29092004");
        properties.put(Environment.HBM2DDL_AUTO, "update");
        properties.put(Environment.SHOW_SQL, "true");

        configuration.setProperties(properties);
        configuration.addAnnotatedClass(TaiKhoan.class);
        configuration.addAnnotatedClass(NhanVien.class);
        configuration.addAnnotatedClass(LoaiMonAn.class);
        configuration.addAnnotatedClass(Ban.class);
        configuration.addAnnotatedClass(MonAn.class);
        configuration.addAnnotatedClass(KhachHang.class);
        configuration.addAnnotatedClass(PhienThongKe.class);
        configuration.addAnnotatedClass(HoaDon.class);

        ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())   
                .build();

        FACTORY = configuration.buildSessionFactory(registry);
    }

    public static SessionFactory getFactory(){
        return FACTORY;
    }
}
