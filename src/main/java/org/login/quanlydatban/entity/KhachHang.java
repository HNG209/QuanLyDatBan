package org.login.quanlydatban.entity;

import org.hibernate.Session;
import org.login.quanlydatban.entity.keygenerator.DailyCustomerCounter;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table
public class KhachHang implements Serializable {

    @Id
    @Column(name = "ma_khach_hang")
    private String maKhachHang;

    @Column(nullable = false, name = "ten_khach_hang")
    private String tenKhachHang;

    @Column
    private String cccd;

    private String sdt;

    @Column(name = "dia_chi")
    private String diaChi;

    private String email;

    @Column(name = "diem_tich_luy")
    private int diemTichLuy = 0;

    public KhachHang() {

    }

    public KhachHang(String maKhachHang, String tenKhachHang, String sdt, String cccd, String diaChi, String email, int diemTichLuy) {
        this.maKhachHang = maKhachHang;
        this.tenKhachHang = tenKhachHang;
        setSdt(sdt);
        setCccd(cccd);
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
        if(cccd != null) {
            if(cccd.matches("^\\d{3}[0-9][0-9]\\d{7}$")) {
                this.cccd = cccd;
            }
            else throw new IllegalArgumentException("Căn cước công dân không hợp lệ");
        }
        else throw new IllegalArgumentException("Căn cước công dân rỗng");
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        if(sdt != null){
            if (sdt.matches("^(09|03|02|04|08)\\d{8}$") || sdt.equals(""))
                this.sdt = sdt;
            else throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        }
    }

}
