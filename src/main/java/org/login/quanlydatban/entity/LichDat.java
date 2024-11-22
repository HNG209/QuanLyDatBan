package org.login.quanlydatban.entity;

import org.login.quanlydatban.entity.enums.LoaiTiec;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table
public class LichDat implements Serializable {
    @Id
    private String maLichDat;

    @Column(nullable = false)
    private LocalDateTime thoiGianDat;

    @Column(nullable = false)
    private LocalDateTime thoiGianNhanBan;
    @ManyToOne
    @JoinColumn(name = "maKhachHang")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "maNhanVien")
    private NhanVien nhanVien;

    @Column(nullable = false)
    private int soLuongNguoi;

    @OneToOne
    private Ban ban;

    @OneToOne
    private HoaDon hoaDon;

    @Column
    @Enumerated(EnumType.STRING)
    private LoaiTiec loaiTiec;

    @PrePersist
    public void generateId() {
        if (this.maLichDat == null) {
            this.maLichDat = generateCustomId();
        }
    }

    private String generateCustomId() {
        String prefix = "LD";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
        String dateTimeSeries = LocalDateTime.now().format(formatter);

        return prefix + dateTimeSeries;
    }
    public LichDat() {}

    public String getMaLichDat() {
        return maLichDat;
    }

    public void setMaLichDat(String maLichDat) {
        this.maLichDat = maLichDat;
    }

    public LocalDateTime getThoiGianDat() {
        return thoiGianDat;
    }

    public void setThoiGianDat(LocalDateTime thoiGianDat) {
        this.thoiGianDat = thoiGianDat;
    }

    public LocalDateTime getThoiGianNhanBan() {
        return thoiGianNhanBan;
    }

    public void setThoiGianNhanBan(LocalDateTime thoiGianNhanBan) {
        this.thoiGianNhanBan = thoiGianNhanBan;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public int getSoLuongNguoi() {
        return soLuongNguoi;
    }

    public void setSoLuongNguoi(int soLuongNguoi) {
        this.soLuongNguoi = soLuongNguoi;
    }

    public Ban getBan() {
        return ban;
    }

    public void setBan(Ban ban) {
        this.ban = ban;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public LoaiTiec getLoaiTiec() {
        return loaiTiec;
    }

    public void setLoaiTiec(LoaiTiec loaiTiec) {
        this.loaiTiec = loaiTiec;
    }
}
