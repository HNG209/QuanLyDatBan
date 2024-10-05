package org.login.quanlydatban.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
public class LichDat {
    @Id
    private String maLichDat;

    @Column(nullable = false)
    private LocalDate thoiGianDat;

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

    public LichDat() {
    }

    public LichDat(String maLichDat, LocalDate thoiGianDat, KhachHang khachHang, NhanVien nhanVien, int soLuongNguoi, Ban ban, HoaDon hoaDon) {
        this.maLichDat = maLichDat;
        this.thoiGianDat = thoiGianDat;
        this.khachHang = khachHang;
        this.nhanVien = nhanVien;
        this.soLuongNguoi = soLuongNguoi;
        this.ban = ban;
        this.hoaDon = hoaDon;
    }
}
