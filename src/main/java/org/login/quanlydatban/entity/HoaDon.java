package org.login.quanlydatban.entity;

import org.login.quanlydatban.entity.enums.TrangThaiHoaDon;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table
public class HoaDon implements Serializable {
    @Id
    private String maHoaDon;

    @Column(nullable = false)
    private LocalDate ngayLap;

    @ManyToOne
    @JoinColumn(name = "maBan")
    private Ban ban;

    @OneToMany
    @JoinColumn(name = "maHoaDon")
    private List<ChiTietHoaDon> chiTietHoaDon;
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
