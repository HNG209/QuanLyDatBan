package org.login.quanlydatban.entity;

import org.hibernate.Session;
import org.login.quanlydatban.entity.enums.TrangThaiHoaDon;
import org.login.quanlydatban.entity.keygenerator.DailyCounter;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Entity
@Table
public class HoaDon implements Serializable {
    @Id
    private String maHoaDon;

    @Column(nullable = false)
    private LocalDate ngayLap;

    @ManyToOne
    @JoinColumn(name = "maBan")
    private Ban ban;

    @ManyToOne
    @JoinColumn(name = "maKhachHang")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "maNhanVien")
    private NhanVien nhanVien;

    @Enumerated(EnumType.STRING)
    private TrangThaiHoaDon trangThaiHoaDon = TrangThaiHoaDon.CHUA_THANH_TOAN;

    @Column
    private double phuThu;

    @PrePersist
    public void generateId() {
        if (this.maHoaDon == null) {
            this.maHoaDon = generateCustomId();
        }
    }

    private String generateCustomId() {
        String prefix = "HD";
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String datePart = today.format(dateFormatter);

        int counterValue = getAndUpdateDailyCounter(today);

        // Combine prefix, date part, and zero-padded counter (e.g., HD01102024001)
        return prefix + datePart + String.format("%03d", counterValue);
    }


    private int getAndUpdateDailyCounter(LocalDate today) {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();
//        DailyCounter dailyCounter = entityManager.find(DailyCounter.class, today);
        DailyCounter dailyCounter = session.find(DailyCounter.class, today);

        int counterValue;

        if (dailyCounter != null) {
            // Entry exists; increment the counter
            counterValue = dailyCounter.getCounterValue() + 1;
            dailyCounter.setCounterValue(counterValue);
        } else {
            // Entry doesn't exist; create a new one for today
            counterValue = 1;
            dailyCounter = new DailyCounter();
            dailyCounter.setCounterDate(today);
            dailyCounter.setCounterValue(counterValue);
//            entityManager.persist(dailyCounter);
            session.persist(dailyCounter);
        }

        session.getTransaction().commit();
        session.close();
        return counterValue;
    }

    public HoaDon() {}

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public void setNgayLap(LocalDate ngayLap) {
        this.ngayLap = ngayLap;
    }

    public void setBan(Ban ban) {
        this.ban = ban;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public void setTrangThaiHoaDon(TrangThaiHoaDon trangThaiHoaDon) {
        this.trangThaiHoaDon = trangThaiHoaDon;
    }

    public void setPhuThu(double phuThu) {
        this.phuThu = phuThu;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public LocalDate getNgayLap() {
        return ngayLap;
    }

    public Ban getBan() {
        return ban;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public TrangThaiHoaDon getTrangThaiHoaDon() {
        return trangThaiHoaDon;
    }

    public double getPhuThu() {
        return phuThu;
    }
}
