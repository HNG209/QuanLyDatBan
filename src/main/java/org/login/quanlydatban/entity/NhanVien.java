package org.login.quanlydatban.entity;

import org.login.quanlydatban.entity.enums.ChucVu;
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

    @OneToOne(mappedBy = "nhanVien", fetch = FetchType.EAGER)
    private TaiKhoan taiKhoan; // Mối quan hệ với TaiKhoan
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

    @Column(nullable = false)
    private String hinhAnh;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrangThaiNhanVien trangThaiNhanVien;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChucVu chucVuNhanVien;
    private String tenTaiKhoan;
    public NhanVien(){

    }

    public String getTenTaiKhoan() {
        return tenTaiKhoan;
    }

    public void setTenTaiKhoan(String tenTaiKhoan) {
        this.tenTaiKhoan = tenTaiKhoan;
    }

    public ChucVu getChucVuNhanVien() {
        return chucVuNhanVien;
    }

    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public NhanVien(String maNhanVien, String tenNhanVien, String sdt, String cccd, String diaChi, boolean gioiTinh, LocalDate ngaySinh, String hinhAnh, TrangThaiNhanVien trangThaiNhanVien, ChucVu chucVuNhanVien) {
        this.maNhanVien = maNhanVien;
        this.tenNhanVien = tenNhanVien;
        this.sdt = sdt;
        this.cccd = cccd;
        this.diaChi = diaChi;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.hinhAnh = hinhAnh;
        this.trangThaiNhanVien = trangThaiNhanVien;
        this.chucVuNhanVien = chucVuNhanVien;
    }

    public void setChucVuNhanVien(ChucVu chucVuNhanVien) {
        this.chucVuNhanVien = chucVuNhanVien;
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

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
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
