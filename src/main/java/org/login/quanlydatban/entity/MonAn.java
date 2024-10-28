package org.login.quanlydatban.entity;

import org.login.quanlydatban.entity.enums.TrangThaiMonAn;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
public class MonAn implements Serializable {

    @Id
    private String maMonAn;

    @OneToOne
    private LoaiMonAn loaiMonAn;

    @Column
    private String tenMonAn;

    @Column
    private double donGia;

    @Column
    private String donViTinh;

    @Column
    private String hinhAnh;

    @Column
    @Enumerated(EnumType.STRING)
    private TrangThaiMonAn trangThaiMonAn;

    public MonAn() {}

    @Override
    public String toString() {
        return "MonAn{" +
                "maMonAn='" + maMonAn + '\'' +
                ", loaiMonAn=" + loaiMonAn +
                ", tenMonAn='" + tenMonAn + '\'' +
                ", donGia=" + donGia +
                ", donViTinh='" + donViTinh + '\'' +
                ", hinhAnh='" + hinhAnh + '\'' +
                ", trangThaiMonAn=" + trangThaiMonAn +
                '}';
    }

    public String getMaMonAn() {
        return maMonAn;
    }

    public LoaiMonAn getLoaiMonAn() {
        return loaiMonAn;
    }

    public String getTenMonAn() {
        return tenMonAn;
    }

    public double getDonGia() {
        return donGia;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public TrangThaiMonAn getTrangThaiMonAn() {
        return trangThaiMonAn;
    }
}
