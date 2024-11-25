package org.login.quanlydatban.entity;

import org.login.quanlydatban.entity.enums.TrangThaiMonAn;

import javax.persistence.*;
import javax.xml.namespace.QName;
import java.io.Serializable;

@Entity
@Table
public class MonAn implements Serializable {

    @Id
    private String maMonAn;

    @ManyToOne
    @JoinColumn(name = "maLoaiMonAn")
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


    public MonAn(String maMonAn, LoaiMonAn loaiMonAn, String tenMonAn, double donGia, String donViTinh, String hinhAnh, TrangThaiMonAn trangThaiMonAn) {
        this.maMonAn = maMonAn;
        this.loaiMonAn = loaiMonAn;
        this.tenMonAn = tenMonAn;
        this.donGia = donGia;
        this.donViTinh = donViTinh;
        this.hinhAnh = hinhAnh;
        this.trangThaiMonAn = trangThaiMonAn;
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

    public void setMaMonAn(String maMonAn) {
        this.maMonAn = maMonAn;
    }

    public void setLoaiMonAn(LoaiMonAn loaiMonAn) {
        this.loaiMonAn = loaiMonAn;
    }

    public void setTenMonAn(String tenMonAn) {
        this.tenMonAn = tenMonAn;
    }

    public void setDonGia(double donGia) {
        if (donGia > 0) {
            this.donGia = donGia;
        } else {
            throw new IllegalArgumentException("Giá phải lớn hơn 0");
        }
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public void setTrangThaiMonAn(TrangThaiMonAn trangThaiMonAn) {
        this.trangThaiMonAn = trangThaiMonAn;
    }
}
