module org.login.quanlydatban {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires java.persistence;
    requires java.naming;
    requires java.sql;

    opens org.login.quanlydatban.entity to org.hibernate.orm.core;
    opens org.login.quanlydatban to javafx.fxml;
    exports org.login.quanlydatban;
    opens org.login.quanlydatban.entity.enums to org.hibernate.orm.core;
}