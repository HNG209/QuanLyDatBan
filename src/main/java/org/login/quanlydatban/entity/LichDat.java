package org.login.quanlydatban.entity;

import org.login.quanlydatban.entity.enums.LoaiTiec;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public LichDat() {}
}
