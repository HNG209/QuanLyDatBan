package org.login.quanlydatban.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
public class TaiKhoan implements Serializable {

    @Id
    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @OneToOne(fetch = FetchType.EAGER)
    private NhanVien nhanVien;

    public TaiKhoan() {
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

    @Override
    public String toString() {
        return "TaiKhoan{" +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", nhanVien=" + nhanVien +
                '}';
    }
}
