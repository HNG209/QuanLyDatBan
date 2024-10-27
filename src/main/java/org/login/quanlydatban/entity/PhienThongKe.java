package org.login.quanlydatban.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table
public class PhienThongKe implements Serializable {

    @Id
    private String maPhienThongKe;

    @Column
    private String chiTiet;

    @CreationTimestamp
    private LocalDate thoiGianThongKe;

    @OneToOne
    private NhanVien nhanVien;

    public PhienThongKe() {}

    @PrePersist
    public void generateId() {
        if (this.maPhienThongKe == null) {
            this.maPhienThongKe = generateCustomId();
        }
    }

    private String generateCustomId() {
        // Define your starting prefix (e.g., "ID-")
        String prefix = "ID-";

        // Get the current date-time series
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String dateTimeSeries = LocalDateTime.now().format(formatter);

        // Combine prefix and date-time series
        return prefix + dateTimeSeries;
    }

    public String getChiTiet() {
        return chiTiet;
    }

    public void setChiTiet(String chiTiet) {
        this.chiTiet = chiTiet;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    @Override
    public String toString() {
        return "PhienThongKe{" +
                "maPhienThongKe=" + maPhienThongKe +
                ", chiTiet='" + chiTiet + '\'' +
                ", thoiGianThongKe=" + thoiGianThongKe +
                '}';
    }
}
