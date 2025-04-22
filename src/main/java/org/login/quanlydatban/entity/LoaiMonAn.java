package org.login.quanlydatban.entity;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table
public class LoaiMonAn implements Serializable {
    @Id
    @Column(name = "ma_loai_mon_an")
    private String maLoaiMonAn;

    @Column(name = "ten_loai_mon_an")
    private String tenLoaiMonAn;

    public LoaiMonAn() {
    }

    public LoaiMonAn(String tenLoai) {
        this.tenLoaiMonAn = tenLoai;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LoaiMonAn loaiMonAn = (LoaiMonAn) o;
        return Objects.equals(maLoaiMonAn, loaiMonAn.maLoaiMonAn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maLoaiMonAn);
    }

    public String getMaLoaiMonAn() {
        return maLoaiMonAn;
    }

    public String getTenLoaiMonAn() {
        return tenLoaiMonAn;
    }

    public void setMaLoaiMonAn(String maLoaiMonAn) {
        this.maLoaiMonAn = maLoaiMonAn;
    }

    public void setTenLoaiMonAn(String tenLoaiMonAn) {
        this.tenLoaiMonAn = tenLoaiMonAn;
    }

    @Override
    public String toString() {
        return "LoaiMonAn{" +
                "maLoaiMonAn='" + maLoaiMonAn + '\'' +
                ", tenLoaiMonAn='" + tenLoaiMonAn + '\'' +
                //", moTaLoaiMonAn='" + moTaLoaiMonAn + '\'' +
                '}';
    }
}
