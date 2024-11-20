package org.login.quanlydatban.entity.enums;

public enum TrangThaiBan {
    DANG_PHUC_VU("ĐANG PHỤC VỤ"),
    TAM_NGUNG_PHUC_VU("TẠM NGƯNG PHỤC VỤ"),
    BAN_TRONG("BÀN TRỐNG");

    private String name;

    private TrangThaiBan(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
