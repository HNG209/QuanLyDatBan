package org.login.quanlydatban.entity;

import org.login.quanlydatban.entity.enums.LoaiMonEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table
public class LoaiMonAn implements Serializable {
    @Id
    private String maLoaiMonAn;

    @Enumerated(EnumType.STRING)
    private LoaiMonEnum tenLoaiMonAn;

    @Column
    private String moTaLoaiMonAn;


    public LoaiMonAn() {
    }

    public LoaiMonAn(String maLoaiMonAn) {
        this.maLoaiMonAn = maLoaiMonAn;
    }

    public LoaiMonAn(String maLoaiMonAn, LoaiMonEnum tenLoaiMonAn, String moTaLoaiMonAn) {
        this.maLoaiMonAn = maLoaiMonAn;
        this.tenLoaiMonAn = tenLoaiMonAn;
        this.moTaLoaiMonAn = moTaLoaiMonAn;
    }

    public String getMaLoaiMonAn() {
        return maLoaiMonAn;
    }

    public LoaiMonEnum getTenLoaiMonAn() {
        return tenLoaiMonAn;
    }

    public String getMoTaLoaiMonAn() {
        return moTaLoaiMonAn;
    }

    public void setMaLoaiMonAn(String maLoaiMonAn) {
        this.maLoaiMonAn = maLoaiMonAn;
    }

    public void setTenLoaiMonAn(LoaiMonEnum tenLoaiMonAn) {
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
