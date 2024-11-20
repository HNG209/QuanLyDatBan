package org.login.quanlydatban.entity.enums;

public enum LoaiTiec {
    TIEC_THUONG("Tiệc thường"),
    TIEC_CUOI("Tiệc cưới");

    private String name;

    LoaiTiec(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return name;
    }
}
