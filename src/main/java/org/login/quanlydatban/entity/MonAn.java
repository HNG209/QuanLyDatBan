package org.login.quanlydatban.entity;

import org.hibernate.annotations.ValueGenerationType;

import javax.persistence.*;

@Entity
@Table
public class MonAn {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "loaiMonAn_id")
    private LoaiMonAn loaiMonAn;
    @Id
    private String maMonAn;
    @Column(nullable = false)
    private String tenMonAn;
    @Column(nullable = false)
    private double donGia;
    @Column(nullable = false)
    private String donViTinh;
    @Column(nullable = false)
    private String hinhAnh;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TrangThaiMonAn trangThaiMonAn;
    public MonAn() {

    }

    public MonAn(LoaiMonAn loaiMonAn, String maMonAn, String tenMonAn, double donGia, String donViTinh, String hinhAnh, TrangThaiMonAn trangThaiMonAn) {
        this.loaiMonAn = loaiMonAn;
        this.maMonAn = maMonAn;
        this.tenMonAn = tenMonAn;
        this.donGia = donGia;
        this.donViTinh = donViTinh;
        this.hinhAnh = hinhAnh;
        this.trangThaiMonAn = trangThaiMonAn;
    }

    public LoaiMonAn getLoaiMonAn() {
        return loaiMonAn;
    }

    public void setLoaiMonAn(LoaiMonAn loaiMonAn) {
        this.loaiMonAn = loaiMonAn;
    }

    public String getMaMonAn() {
        return maMonAn;
    }

    public void setMaMonAn(String maMonAn) {
        this.maMonAn = maMonAn;
    }

    public String getTenMonAn() {
        return tenMonAn;
    }

    public void setTenMonAn(String tenMonAn) {
        this.tenMonAn = tenMonAn;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
