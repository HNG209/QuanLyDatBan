package org.login.quanlydatban.entity;

import org.login.quanlydatban.entity.enums.TrangThaiHoaDon;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
public class HoaDon {
    @Id
    private String maHoaDon;

    @Column(nullable = false)
    private LocalDate ngayLap;

    @ManyToOne
    @JoinColumn(name = "maBan")
    private Ban ban;

    @ManyToOne
    @JoinColumn(name = "maKhachHang")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "maNhanVien")
    private NhanVien nhanVien;

    @Enumerated(EnumType.STRING)
    private TrangThaiHoaDon trangThaiHoaDon = TrangThaiHoaDon.CHUA_THANH_TOAN;

    @Column
    private double phuThu;

    public HoaDon() {}
}
