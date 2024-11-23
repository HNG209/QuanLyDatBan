package org.login.quanlydatban.entity;

import org.login.quanlydatban.dao.HoaDonDAO;
import org.login.quanlydatban.dao.LichDatDAO;
import org.login.quanlydatban.entity.enums.LoaiTiec;

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
    private String maLichDat;

    @Column(nullable = false)
    private LocalDateTime thoiGianDat;

    @Column(nullable = false)
    private LocalDateTime thoiGianNhanBan;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "maKhachHang")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "maNhanVien")
    private NhanVien nhanVien;

    @Column(nullable = false)
    private int soLuongNguoi;

    @OneToOne
    private HoaDon hoaDon;

    @Column
    @Enumerated(EnumType.STRING)
    private LoaiTiec loaiTiec;

    @Transient
    private LichDatDAO lichDatDAO;

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
    public LichDat() {
        lichDatDAO = new LichDatDAO();
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
        List<LichDat> list = lichDatDAO.getDSLichDat();
        for (LichDat i : list) {
            if (i.getHoaDon().getBan().getMaBan().equals(this.getHoaDon().getBan().getMaBan())) {
                if (Math.abs(ChronoUnit.MINUTES.between(thoiGianNhanBan.toLocalTime(), i.getThoiGianNhanBan().toLocalTime())) < 180)
                    throw new IllegalArgumentException("Khoảng cách cho các lần đặt khác nhau trong cùng 1 bàn phải trên 3 giờ");
            }
        }
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
