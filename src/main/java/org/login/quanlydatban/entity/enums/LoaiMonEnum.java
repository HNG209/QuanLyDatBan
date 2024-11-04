package org.login.quanlydatban.entity.enums;

public enum LoaiMonEnum {
        MON_CHIEN_GION("Món Chiên Giòn"),
        TRANG_MIENG("Món Tráng Miệng"),
        KHAI_VI("Khai Vị"),
        NUOC_GIAI_KHAT("Nước Giải Khát"),
        MON_XAO("Món Xào"),
        MON_HAI_SAN("Món Hải Sản"),
        MON_TRUYEN_THONG("Món Truyền Thống");

        private final String value;

        LoaiMonEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

}

