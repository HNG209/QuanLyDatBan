package org.login.quanlydatban.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table
public class KhachHang implements Serializable {

    @Id
    private String maKhachHang;

    @Column(nullable = false)
    private String tenKhachHang;

    @Column(nullable = false)
    private String sdt;

    @Column(nullable = false)
    private String cccd;

    @Column(nullable = false)
    private String diaChi;

    @Column(nullable = false)
    private String email;

    private int diemTichLuy = 0;

    public KhachHang() {

    }

    public KhachHang(String maKhachHang, String tenKhachHang, String sdt, String cccd, String diaChi, String email, int diemTichLuy) {
        this.maKhachHang = maKhachHang;
        this.tenKhachHang = tenKhachHang;
        this.sdt = sdt;
        this.cccd = cccd;
        this.diaChi = diaChi;
        this.email = email;
        this.diemTichLuy = diemTichLuy;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

}
