package org.login.quanlydatban.entity;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;

@Entity
@Table
public class LoaiMonAn implements Serializable {
    @Id
    private String maLoaiMonAn;

    @Column
    private String tenLoaiMonAn;

//    @Column
//    private String moTaLoaiMonAn;


    public LoaiMonAn() {
    }

    public LoaiMonAn(String tenLoai) {
        this.tenLoaiMonAn = tenLoai;
       // this.moTaLoaiMonAn = moTa;
    }

    @PrePersist
    public void onPrePersist() {
        if (tenLoaiMonAn != null) {
            this.maLoaiMonAn = generateLoaiMonAn(tenLoaiMonAn);
            System.out.println("1");
        } else {
            throw new IllegalArgumentException("tenLoaiMonAn is null");
        }

    }

    public String getMaLoaiMonAn() {
        return maLoaiMonAn;
    }

    public String getTenLoaiMonAn() {
        return tenLoaiMonAn;
    }

//    public String getMoTaLoaiMonAn() {
//        return moTaLoaiMonAn;
//    }

    public void setMaLoaiMonAn(String maLoaiMonAn) {
        this.maLoaiMonAn = maLoaiMonAn;
    }

    public void setTenLoaiMonAn(String tenLoaiMonAn) {
        this.tenLoaiMonAn = tenLoaiMonAn;
    }

//    public void setMoTaLoaiMonAn(String moTaLoaiMonAn) {
//        this.moTaLoaiMonAn = moTaLoaiMonAn;
//    }

    private String generateLoaiMonAn(String itemName) {
        String prefix = generatePrefixFromName(itemName); // Generate the "XX" part from the item name
        Long maxId = getMaLoaiFromDatabase(prefix); // Get the maximum "YY" part for the given prefix
        Long newIdNumber = (maxId == null) ? 1 : maxId + 1; // Increment the ID number
        return prefix + String.format("%02d", newIdNumber); // Combine "XX" and "YY" into the final format
    }

    public Long getMaLoaiFromDatabase(String prefix) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;
        Long maLoaiMon = null;

        try {
            transaction = session.beginTransaction();

            // Query to fetch IDs starting with the given prefix
            String query = "SELECT maLoaiMonAn FROM LoaiMonAn WHERE maLoaiMonAn LIKE :prefix";
            List<String> maMonAns = session.createQuery(query, String.class)
                    .setParameter("prefix", prefix + "%") // Match IDs with the given prefix
                    .getResultList();

            // Extract the "YY" part, convert to a number, and find the maximum
            maLoaiMon = maMonAns.stream()
                    .map(id -> id.substring(prefix.length())) // Extract "YY" part
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
        return maLoaiMon;
    }

    // Helper method to generate the "XX" part from the item name
    private String generatePrefixFromName(String name) {
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);
        String withoutDiacritics = normalized.replaceAll("\\p{M}", "");

        // Split the name into words, take the first character of each word, and convert to uppercase
        return Arrays.stream(withoutDiacritics.split("\\s+"))
                .filter(word -> !word.isEmpty())       // Ensure non-empty words
                .map(word -> word.substring(0, 1))    // Take the first letter of each word
                .map(String::toUpperCase)             // Convert to uppercase
                .limit(2)                             // Take only the first 2 letters
                .reduce("", String::concat);         // Combine into a single string
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
