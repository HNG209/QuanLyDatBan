package org.login.quanlydatban.entity;

import org.hibernate.Session;
import org.login.quanlydatban.dao.ChiTietHoaDonDAO;
import org.login.quanlydatban.dao.KhachHangDAO;
import org.login.quanlydatban.entity.enums.TrangThaiHoaDon;
import org.login.quanlydatban.entity.keygenerator.DailyCounter;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

@Entity
@Table
public class HoaDon implements Serializable {
    @Id
    @Column(name = "ma_hoa_don")
    private String maHoaDon;

    @Column(nullable = false, name = "ngay_lap")
    private LocalDate ngayLap;

    @ManyToOne
    @JoinColumn(name = "ma_ban")
    private Ban ban;

    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ChiTietHoaDon> chiTietHoaDon;

    @ManyToOne
    @JoinColumn(name = "ma_khach_hang")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "ma_nhan_vien")
    private NhanVien nhanVien;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai_hoa_don")
    private TrangThaiHoaDon trangThaiHoaDon = TrangThaiHoaDon.CHUA_THANH_TOAN;

    @Column(name = "phu_thu")
    private double phuThu;

    @Column(name = "tong_tien")
    private double tongTien;

    @Column(name = "chiet_khau")
    private double chietKhau;

    @Transient
    private ChiTietHoaDonDAO chiTietHoaDonDAO;

    @PrePersist
    @PreUpdate
    public void generateId() {
        if (this.maHoaDon == null) {
            this.maHoaDon = generateCustomId();
        }

        if(trangThaiHoaDon == TrangThaiHoaDon.DA_THANH_TOAN){
            tongTien = tinhTongTien() + phuThu - chietKhau;
            if(khachHang != null){
                khachHang.setDiemTichLuy(khachHang.getDiemTichLuy() + (int)(tongTien * 1 / 1000.0));

                KhachHangDAO khachHangDAO = new KhachHangDAO();
                khachHangDAO.suaKhachHang(khachHang);
            }
        }
    }

    private String generateCustomId() {//generate HoaDon id when create(auto)
        String prefix = "HD";
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String datePart = today.format(dateFormatter);

        int counterValue = getAndUpdateDailyCounter(today);

        // Combine prefix, date part, and zero-padded counter (e.g., HD01102024001)
        return prefix + datePart + String.format("%03d", counterValue);
    }

    public String generateCustomIdFuture(LocalDate date) {//generate HoaDon id when needed(manual), use for future booking
        String prefix = "HD";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String datePart = date.format(dateFormatter);

        int counterValue = getAndUpdateDailyCounter(date);

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

    public void setMaHoaDon(LocalDate date) {
        this.maHoaDon = generateCustomIdFuture(date);
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

    public List<ChiTietHoaDon> getChiTietHoaDon() {
        return chiTietHoaDon;
    }

    public double tinhTongTien() {
        chiTietHoaDonDAO = new ChiTietHoaDonDAO();
        tongTien = chiTietHoaDonDAO.fetchChiTietHoaDonNative(maHoaDon).stream().mapToDouble(ChiTietHoaDon::tinhTongCTHD).sum();
        return tongTien;
    }

    public double getTongTien() {
        return tongTien;
    }

    public double getChietKhau() {
        return chietKhau;
    }

    public void setChietKhau(double chietKhau) {
        this.chietKhau = chietKhau;
    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHoaDon='" + maHoaDon + '\'' +
                ", ngayLap=" + ngayLap +
                ", ban=" + ban +
//                ", chiTietHoaDon=" + chiTietHoaDon +
                ", khachHang=" + khachHang +
                ", nhanVien=" + nhanVien +
                ", trangThaiHoaDon=" + trangThaiHoaDon +
                ", phuThu=" + phuThu +
                '}';
    }
}
