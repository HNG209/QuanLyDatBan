module org.login.quanlydatban {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires java.persistence;
    requires java.naming;
    requires java.sql;

    opens org.login.quanlydatban.entity to org.hibernate.orm.core;
    opens org.login.quanlydatban to javafx.fxml;
    opens org.login.quanlydatban.controller to javafx.fxml;
    opens org.login.quanlydatban.uicomponents to javafx.fxml;
    opens org.login.quanlydatban.icons to javafx.fxml;
    opens org.login.quanlydatban.stylesheets to javafx.fxml;

    exports org.login.quanlydatban;
    exports org.login.quanlydatban.controller;
    opens org.login.quanlydatban.entity.enums to org.hibernate.orm.core;
    opens org.login.quanlydatban.views to javafx.fxml;
}