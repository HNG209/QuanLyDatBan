package org.login.quanlydatban.entity;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.login.quanlydatban.dao.LoaiMonDAO;
import org.login.quanlydatban.entity.enums.TrangThaiMonAn;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table
public class MonAn implements Serializable {

    @Id
    private String maMonAn;

    @ManyToOne
    @JoinColumn(name = "maLoaiMonAn")
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

    @Column
    private String moTaMonAn;

    public MonAn() {}



    @Override
    public String toString() {
        return "MonAn{" +
                "maMonAn='" + maMonAn + '\'' +
                ", loaiMonAn=" + loaiMonAn +
                ", tenMonAn='" + tenMonAn + '\'' +
                ", donGia=" + donGia +
                ", donViTinh='" + donViTinh + '\'' +
                ", hinhAnh='" + hinhAnh + '\'' +
                ", trangThaiMonAn=" + trangThaiMonAn +
                ", moTa='" + moTaMonAn + '\'' +
                '}';
    }

    public MonAn(String maMonAn, LoaiMonAn loaiMonAn, String tenMonAn, double donGia, String donViTinh, String hinhAnh, TrangThaiMonAn trangThaiMonAn, String moTaMonAn) {
        this.maMonAn = maMonAn;
        this.loaiMonAn = loaiMonAn;
        this.tenMonAn = tenMonAn;
        this.donGia = donGia;
        this.donViTinh = donViTinh;
        this.hinhAnh = hinhAnh;
        this.trangThaiMonAn = trangThaiMonAn;
        this.moTaMonAn = moTaMonAn;
    }

    @PrePersist
    public void onPrePersist() {
        if (loaiMonAn.getMaLoaiMonAn() != null) {
            this.maMonAn = generateMaMonAn(loaiMonAn.getMaLoaiMonAn());
        } else {
            throw new IllegalArgumentException("loaiMonAn is null");
        }
    }

    public String getMaMonAn() {
        return maMonAn;
    }

    public LoaiMonAn getLoaiMonAn() {
        return loaiMonAn;
    }

    public String getTenMonAn() {
        return tenMonAn;
    }

    public double getDonGia() {
        return donGia;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public TrangThaiMonAn getTrangThaiMonAn() {
        return trangThaiMonAn;
    }

    public String getMoTaMonAn() {
        return moTaMonAn;
    }

    public void setMaMonAn(String maMonAn) {
        this.maMonAn = maMonAn;
    }

    public void setLoaiMonAn(LoaiMonAn loaiMonAn) {
        this.loaiMonAn = loaiMonAn;
    }

    public void setTenMonAn(String tenMonAn) {
        this.tenMonAn = tenMonAn;
    }

    public void setDonGia(double donGia) {
        if (donGia > 0) {
            this.donGia = donGia;
        } else {
            throw new IllegalArgumentException("Giá phải lớn hơn 0");
        }
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public void setTrangThaiMonAn(TrangThaiMonAn trangThaiMonAn) {
        this.trangThaiMonAn = trangThaiMonAn;
    }

    public void setMoTaMonAn(String moTaMonAn) {
        this.moTaMonAn = moTaMonAn;
    }

    private String generateMaMonAn(String itemName) {
        //LoaiMonDAO loaiMonDAO = new LoaiMonDAO();
        // Generate the "XXXX" part using the logic for XXYY
        //loaiMonDAO.themLoaiMonAn(loaiMonAn); // Assume this generates the XXYY format
        String prefix = loaiMonAn.getMaLoaiMonAn();
        // Fetch the maximum "YYYY" part for the given "XXXX" prefix and increment it
        Long maxSuffix = getMaMonFromDatabase(prefix);
        Long newSuffix = (maxSuffix == null) ? 1 : maxSuffix + 1;

        // Combine "XXXX" (from XXYY) and "YYYY" into the final format
        return prefix + String.format("%04d", newSuffix); // Ensure "YYYY" is 4 digits
    }

    // Method to retrieve the maximum "YYYY" value for a specific "XXXX" prefix
    public Long getMaMonFromDatabase(String prefix) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;
        Long maxSuffix = null;

        try {
            transaction = session.beginTransaction();

            // Query to fetch IDs starting with the given "XXXX" prefix
            String query = "SELECT maMonAn FROM MonAn WHERE maMonAn LIKE :prefix";
            List<String> maMonAns = session.createQuery(query, String.class)
                    .setParameter("prefix", prefix + "%") // Match IDs with the given prefix
                    .getResultList();

            // Extract the "YYYY" part, convert to a number, and find the maximum
            maxSuffix = maMonAns.stream()
                    .map(id -> id.substring(prefix.length())) // Extract "YYYY" part
                    .filter(yy -> yy.matches("\\d+"))         // Ensure it is numeric
                    .map(Long::parseLong)                    // Convert to Long
                    .max(Long::compare)                      // Find the maximum
                    .orElse(0L);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace(); // Consider using a logger for better error handling
        } finally {
            if (session != null) {
                session.close(); // Ensure the session is closed properly
            }
        }
        return maxSuffix;
    }
}


