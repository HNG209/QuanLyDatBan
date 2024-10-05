package org.login.quanlydatban.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
public class PhienThongKe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maPhienThongKe;

    private String chiTiet;

    @CreationTimestamp
    private LocalDate thoiGianThongKe;

    @OneToOne
    private NhanVien nhanVien;

    public PhienThongKe(){}

    public int getMaPhienThongKe() {
        return maPhienThongKe;
    }

    public void setMaPhienThongKe(int maPhienThongKe) {
        this.maPhienThongKe = maPhienThongKe;
    }

    public String getChiTiet() {
        return chiTiet;
    }

    public void setChiTiet(String chiTiet) {
        this.chiTiet = chiTiet;
    }

    public LocalDate getThoiGianThongKe() {
        return thoiGianThongKe;
    }

    public void setThoiGianThongKe(LocalDate thoiGianThongKe) {
        this.thoiGianThongKe = thoiGianThongKe;
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
