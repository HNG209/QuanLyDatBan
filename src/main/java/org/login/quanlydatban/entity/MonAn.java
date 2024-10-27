package org.login.quanlydatban.entity;

import org.login.quanlydatban.entity.enums.TrangThaiMonAn;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
public class MonAn implements Serializable {

    @Id
    private String maMonAn;

    @OneToOne
    private LoaiMonAn loaiMonAn;

    @Column
    private String tenMonAn;

    @Column
    private double donGia;

    @Column
    private String donViTinh;

    @Column
    private String hinhAnh;

    @Column
    @Enumerated(EnumType.STRING)
    private TrangThaiMonAn trangThaiMonAn;

    public MonAn() {}
}
