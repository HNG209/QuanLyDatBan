package org.login.quanlydatban.entity;

import org.hibernate.Session;
import org.login.quanlydatban.entity.keygenerator.DailyCustomerCounter;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table
public class KhachHang implements Serializable {

    @Id
    private String maKhachHang;

    @Column(nullable = false)
    private String tenKhachHang;

    @Column(nullable = false)
    private String sdt;

    @Column(nullable = false)
    private String cccd;

    @Column(nullable = false)
    private String diaChi;

    @Column(nullable = false)
    private String email;

    private int diemTichLuy = 0;

    public KhachHang() {

    }
    @PrePersist
    @PreUpdate
    public void generateId() {
        if (this.maKhachHang == null) {
            this.maKhachHang = generateCustomId();
        }
    }

    private String generateCustomId() {
        String prefix = "KH";
       int currentYear = LocalDate.now().getYear();
        int counterValue = getAndUpdateDailyCounter(currentYear);
        return prefix + currentYear + String.format("%04d", counterValue);
    }

    private int getAndUpdateDailyCounter(int currentYear) {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();
        DailyCustomerCounter dailyCounter = session.find(DailyCustomerCounter.class, currentYear);
        int counterValue;

        if (dailyCounter != null) {
            counterValue = dailyCounter.getCounterValue() + 1;
            dailyCounter.setCounterValue(counterValue);
        } else {
            counterValue = 1;
            dailyCounter = new DailyCustomerCounter();
            dailyCounter.setCounterDate(currentYear);
            dailyCounter.setCounterValue(counterValue);
            session.persist(dailyCounter);
        }

        session.getTransaction().commit();
        session.close();
        return counterValue;
    }

    public KhachHang(String maKhachHang, String tenKhachHang, String sdt, String cccd, String diaChi, String email, int diemTichLuy) {
        this.maKhachHang = maKhachHang;
        this.tenKhachHang = tenKhachHang;
        this.sdt = sdt;
        this.cccd = cccd;
        this.diaChi = diaChi;
        this.email = email;
        this.diemTichLuy = diemTichLuy;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

}
