package org.login.quanlydatban.entity;

import org.login.quanlydatban.entity.enums.TrangThaiNhanVien;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table
public class NhanVien implements Serializable {

    @Id
    private String maNhanVien;

    @Column(nullable = false)
    private String tenNhanVien;

    @Column(nullable = false)
    private String sdt;

    @Column(nullable = false)
    private String cccd;

    @Column(nullable = false)
    private String diaChi;

    @Column(nullable = false)
    private boolean gioiTinh;

    @Column(nullable = false)
    private LocalDate ngaySinh;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrangThaiNhanVien trangThaiNhanVien;
    public NhanVien(){

    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public TrangThaiNhanVien getTrangThaiNhanVien() {
        return trangThaiNhanVien;
    }

    public void setTrangThaiNhanVien(TrangThaiNhanVien trangThaiNhanVien) {
        this.trangThaiNhanVien = trangThaiNhanVien;
    }


    @Override
    public String toString() {
        return "NhanVien{" +
                "maNhanVien='" + maNhanVien + '\'' +
                ", tenNhanVien='" + tenNhanVien + '\'' +
                ", sdt='" + sdt + '\'' +
                ", cccd='" + cccd + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", gioiTinh=" + gioiTinh +
                ", ngaySinh=" + ngaySinh +
                ", trangThaiNhanVien=" + trangThaiNhanVien +
                '}';
    }
}
