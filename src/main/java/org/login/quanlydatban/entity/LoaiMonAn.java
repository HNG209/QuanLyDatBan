package org.login.quanlydatban.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table


public class LoaiMonAn {
    @Id
    private String maLoaiMonAn;

    @Column(nullable = false)
    private String tenLoaiMonAn;


    @Column(nullable = true)
    private String moTaLoaiMonAn;


    public LoaiMonAn() {
    }

    public LoaiMonAn(String maLoaiMonAn, String tenLoaiMonAn, String moTaLoaiMonAn) {
        this.maLoaiMonAn = maLoaiMonAn;
        this.tenLoaiMonAn = tenLoaiMonAn;
        this.moTaLoaiMonAn = moTaLoaiMonAn;
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
}
