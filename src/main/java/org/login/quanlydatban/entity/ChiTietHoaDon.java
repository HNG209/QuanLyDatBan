package org.login.quanlydatban.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
public class ChiTietHoaDon implements Serializable {
    @Id
    private int Id;

    @Column(nullable = false)
    private int soLuong = 0;

    @ManyToOne
    @JoinColumn(name = "maMonAn")
    private MonAn monAn;

    @ManyToOne
    @JoinColumn(name = "maHoaDon")
    private HoaDon hoaDon;

}
