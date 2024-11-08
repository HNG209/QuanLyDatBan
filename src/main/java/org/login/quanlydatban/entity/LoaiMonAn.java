package org.login.quanlydatban.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table
public class LoaiMonAn implements Serializable {
    @Id
    private String maLoaiMonAn;

    @Column
    private String tenLoaiMonAn;

    @Column
    private String moTaLoaiMonAn;


    public LoaiMonAn() {
    }

    public LoaiMonAn(String maLoai, String tenLoai, String moTa) {
        this.maLoaiMonAn = maLoai;
        this.tenLoaiMonAn = tenLoai;
        this.moTaLoaiMonAn = moTa;
    }

    public String getMaLoaiMonAn() {
        return maLoaiMonAn;
    }

    public String getTenLoaiMonAn() {
        return tenLoaiMonAn;
    }

    public String getMoTaLoaiMonAn() {
        return moTaLoaiMonAn;
    }

    public void setMaLoaiMonAn(String maLoaiMonAn) {
        this.maLoaiMonAn = maLoaiMonAn;
    }

    public void setTenLoaiMonAn(String tenLoaiMonAn) {
        this.tenLoaiMonAn = tenLoaiMonAn;
    }

    public void setMoTaLoaiMonAn(String moTaLoaiMonAn) {
        this.moTaLoaiMonAn = moTaLoaiMonAn;
    }

    @Override
    public String toString() {
        return "LoaiMonAn{" +
                "maLoaiMonAn='" + maLoaiMonAn + '\'' +
                ", tenLoaiMonAn='" + tenLoaiMonAn + '\'' +
                ", moTaLoaiMonAn='" + moTaLoaiMonAn + '\'' +
                '}';
    }
}
