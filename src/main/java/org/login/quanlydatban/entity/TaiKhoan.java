package org.login.quanlydatban.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
public class TaiKhoan implements Serializable {
    // bien luu ten nhan vien

    @Id
    @Column(nullable = false, name = "user_name")
    private String userName;

    @Column(nullable = false)
    private String password;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_nhan_vien", referencedColumnName = "ma_nhan_vien") // Khóa ngoại liên kết đến NhanVien
    private NhanVien nhanVien;

    public TaiKhoan() {
    }

    public TaiKhoan(String userName, String password, NhanVien nhanVien) {
        this.userName = userName;
        this.password = password;
        this.nhanVien = nhanVien;
    }

    public TaiKhoan(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }


}
