package org.login.quanlydatban.entity;

import org.login.quanlydatban.dao.HoaDonDAO;
import org.login.quanlydatban.dao.LichDatDAO;
import org.login.quanlydatban.entity.enums.LoaiTiec;
import org.login.quanlydatban.entity.enums.TrangThaiHoaDon;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@Table
public class LichDat implements Serializable {
    @Id
    @Column(name = "ma_lich_dat")
    private String maLichDat;

    @Column(nullable = false, name = "thoi_gian_dat")
    private LocalDateTime thoiGianDat;

    @Column(nullable = false, name = "thoi_gian_nhan_ban")
    private LocalDateTime thoiGianNhanBan;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_khach_hang")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "ma_nhan_vien")
    private NhanVien nhanVien;

    @Column(nullable = false, name = "so_luong_nguoi")
    private int soLuongNguoi;

    @OneToOne
    @JoinColumn(name = "ma_hoa_don")
    private HoaDon hoaDon;

    @Column(name = "tien_coc")
    private double tienCoc;

    @Column(name = "ghi_chu")
    private String ghiChu;


    public LichDat() {
    }

    public void setMaLichDat(String maLichDat) {
        this.maLichDat = maLichDat;
    }

    public String getMaLichDat() {
        return maLichDat;
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
        this.thoiGianNhanBan= thoiGianNhanBan;
    }
    public void setThoiGianNhanBan(LocalDateTime thoiGianNhanBan, Ban ban) {

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

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public double getTienCoc() {
        return tienCoc;
    }

    public void setTienCoc(double tienCoc) {
        this.tienCoc = tienCoc;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

}
