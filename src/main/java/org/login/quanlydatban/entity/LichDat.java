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
}
