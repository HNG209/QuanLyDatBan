package org.login.quanlydatban.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table
public class BaoCao implements Serializable {

    @Id
    private String maBaoCao;

    @Column
    private String thoiGianVaoCa;


    @Column
    private String thoiGianKetCa;

    @Column
    private double tienVaoCa;

    @Column
    private double tongDoanhThu;

    @Column
    private double tienBanGiao;
    @OneToOne
    private NhanVien nhanVien;

    @PrePersist
    public void generateId() {
        if (this.maBaoCao == null) {
            this.maBaoCao = generateCustomId();
        }
    }

    public BaoCao(String maBaoCao, String thoiGianVaoCa, String thoiGianKetCa, double tienVaoCa, double tongDoanhThu, double tienBanGiao, NhanVien nhanVien) {
        this.maBaoCao = maBaoCao;
        this.thoiGianVaoCa = thoiGianVaoCa;
        this.thoiGianKetCa = thoiGianKetCa;
        this.tienVaoCa = tienVaoCa;
        this.tongDoanhThu = tongDoanhThu;
        this.tienBanGiao = tienBanGiao;
        this.nhanVien = nhanVien;
    }

    private String generateCustomId() {
        // Define your starting prefix (e.g., "ID-")
        String prefix = "BC" + nhanVien.getMaNhanVien();

        // Get the current date-time series
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String dateTimeSeries = LocalDateTime.now().format(formatter);

        // Combine prefix and date-time series
        return prefix + dateTimeSeries;
    }

    public String getMaBaoCao() {
        return maBaoCao;
    }

    public void setMaBaoCao(String maBaoCao) {
        this.maBaoCao = maBaoCao;
    }

    public String getThoiGianVaoCa() {
        return thoiGianVaoCa;
    }

    public void setThoiGianVaoCa(String thoiGianVaoCa) {
        this.thoiGianVaoCa = thoiGianVaoCa;
    }

    public String getThoiGianKetCa() {
        return thoiGianKetCa;
    }

    public void setThoiGianKetCa(String thoiGianKetCa) {
        this.thoiGianKetCa = thoiGianKetCa;
    }

    public double getTienVaoCa() {
        return tienVaoCa;
    }

    public void setTienVaoCa(double tienVaoCa) {
        this.tienVaoCa = tienVaoCa;
    }

    public double getTongMenhGia() {
        return tongDoanhThu;
    }

    public void setTongMenhGia(double tongMenhGia) {
        this.tongDoanhThu = tongMenhGia;
    }

    public double getTienBanGiao() {
        return tienBanGiao;
    }

    public void setTienBanGiao(double tienBanGiao) {
        this.tienBanGiao = tienBanGiao;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }


}
