module org.login.quanlydatban {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires java.persistence;
    requires java.naming;
    requires java.sql;
    requires java.desktop;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens org.login.quanlydatban.entity.keygenerator to org.hibernate.orm.core;
    opens org.login.quanlydatban.entity to org.hibernate.orm.core, javafx.base;
    opens org.login.quanlydatban to javafx.fxml;
    opens org.login.quanlydatban.controller to javafx.fxml;
    opens org.login.quanlydatban.uicomponents to javafx.fxml;
    opens org.login.quanlydatban.icons to javafx.fxml;
    opens org.login.quanlydatban.stylesheets to javafx.fxml;
    exports org.login.quanlydatban;
    exports org.login.quanlydatban.controller;
    opens org.login.quanlydatban.entity.enums to org.hibernate.orm.core;
    opens org.login.quanlydatban.views to javafx.fxml;
    exports org.login.quanlydatban.utilities;
    opens org.login.quanlydatban.utilities to javafx.fxml;
}