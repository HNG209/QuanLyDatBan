package org.login.quanlydatban.entity;


import javax.persistence.*;

@Entity
@Table
public class ChiTietHoaDon {
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
