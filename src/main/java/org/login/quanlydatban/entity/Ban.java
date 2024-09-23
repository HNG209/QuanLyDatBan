package org.login.quanlydatban.entity;

import javax.persistence.*;

@Entity
@Table
public class Ban {
    @Id
    private String maBan;

    @Column(nullable = false)
    @Enumerated (EnumType.STRING)
    private LoaiBan loaiBan;

    @Column(nullable = false)
    @Enumerated (EnumType.STRING)
    private TrangThaiBan trangThaiBan;

    @Column(nullable = false)
    @Enumerated (EnumType.STRING)
    private KhuVuc khuVuc;

    public Ban() {}

    public Ban(String maBan, LoaiBan loaiBan, TrangThaiBan trangThaiBan, KhuVuc khuVuc) {
        this.maBan = maBan;
        this.loaiBan = loaiBan;
        this.trangThaiBan = trangThaiBan;
        this.khuVuc = khuVuc;
    }

    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
        this.maBan = maBan;
    }

    public LoaiBan getLoaiBan() {
        return loaiBan;
    }

    public void setLoaiBan(LoaiBan loaiBan) {
        this.loaiBan = loaiBan;
    }

    public TrangThaiBan getTrangThaiBan() {
        return trangThaiBan;
    }

    public void setTrangThaiBan(TrangThaiBan trangThaiBan) {
        this.trangThaiBan = trangThaiBan;
    }

    public KhuVuc getKhuVuc() {
        return khuVuc;
    }

    public void setKhuVuc(KhuVuc khuVuc) {
        this.khuVuc = khuVuc;
    }
}
