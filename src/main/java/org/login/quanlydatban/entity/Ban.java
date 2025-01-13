package org.login.quanlydatban.entity;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.login.quanlydatban.entity.enums.KhuVuc;
import org.login.quanlydatban.entity.enums.LoaiBan;
import org.login.quanlydatban.entity.enums.TrangThaiBan;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Entity
@Table
public class Ban {
    @Id
    @Column(name = "ma_ban")
    private String maBan;

    @Column(nullable = false, name = "loai_ban")
    @Enumerated(EnumType.STRING)
    private LoaiBan loaiBan;

    @Column(nullable = false, name = "trang_thai_ban")
    @Enumerated(EnumType.STRING)
    private TrangThaiBan trangThaiBan;

    @Column(nullable = false, name = "khu_vuc")
    @Enumerated(EnumType.STRING)
    private KhuVuc khuVuc;

    public Ban() {
    }

    public Ban(String maBan, LoaiBan loaiBan, TrangThaiBan trangThaiBan, KhuVuc khuVuc) {
        this.maBan = maBan;
        this.loaiBan = loaiBan;
        this.trangThaiBan = trangThaiBan;
        this.khuVuc = khuVuc;
    }

    @PrePersist
    public void generateID() {
        if (this.getMaBan() == null) {
            // Giả sử bạn có phương thức để lấy khu vực và sức chứa
            this.maBan = generateMaBan(getKhuVuc(),getLoaiBan());
        }
    }


    public Long getMaxIdFromDatabaseForTable(String areaPrefix) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;
        Long maxId = null;

        try {
            transaction = session.beginTransaction();

            String query = "SELECT maBan FROM Ban WHERE maBan LIKE :prefix";
            List<String> maBans = session.createQuery(query)
                    .setParameter("prefix", areaPrefix + "%")
                    .getResultList();

            maxId = maBans.stream()
                    .map(ma -> Long.parseLong(ma.substring(areaPrefix.length())))
                    .max(Long::compare)
                    .orElse(0L);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            // Không cần phải đóng session ở đây, sẽ tự động quản lý
        }
        return maxId;
    }

    private String generateMaBan(KhuVuc khuVuc, LoaiBan loaiBan) {
        String areaPrefix = khuVuc.toString(); // Lấy khu vực
        String capacityCode;

        switch (loaiBan) {
            case BAN_2_NGUOI:
                capacityCode = "02";
                break;
            case BAN_5_NGUOI:
                capacityCode = "05";
                break;
            case BAN_10_NGUOI:
                capacityCode = "10";
                break;
            default:
                throw new IllegalArgumentException("Loại bàn không hợp lệ");
        }

        Long maxId = getMaxIdFromDatabaseForTable(areaPrefix + capacityCode);
        Long newIdNumber = (maxId == null) ? 1 : maxId + 1; // Tăng mã lên 1
        return areaPrefix + capacityCode + String.format("%03d", newIdNumber); // Định dạng mã
    }
    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
        this.maBan = maBan;
    }

    public LoaiBan getLoaiBan() {
        return loaiBan;
    }

    public void setLoaiBan(LoaiBan loaiBan) {
        this.loaiBan = loaiBan;
    }

    public TrangThaiBan getTrangThaiBan() {
        return trangThaiBan;
    }

    public void setTrangThaiBan(TrangThaiBan trangThaiBan) {
        this.trangThaiBan = trangThaiBan;
    }

    public KhuVuc getKhuVuc() {
        return khuVuc;
    }

    public void setKhuVuc(KhuVuc khuVuc) {
        this.khuVuc = khuVuc;
    }

    @Override
    public String toString() {
        return "Ban{" +
                "maBan='" + maBan + '\'' +
                ", loaiBan=" + loaiBan +
                ", trangThaiBan=" + trangThaiBan +
                ", khuVuc=" + khuVuc +
                '}';
    }

}
