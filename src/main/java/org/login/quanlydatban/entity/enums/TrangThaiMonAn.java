package org.login.quanlydatban.entity.enums;

import java.io.Serializable;

public enum TrangThaiMonAn implements Serializable {
    CO_SAN("Có sẵn"),
    TAM_HET("Tạm hết"),
    NGUNG_BAN("Ngưng bán");

    private String name;

    private TrangThaiMonAn(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
