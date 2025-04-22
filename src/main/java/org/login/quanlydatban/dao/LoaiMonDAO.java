package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.login.quanlydatban.entity.LoaiMonAn;
import org.login.quanlydatban.entity.MonAn;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;

public class LoaiMonDAO {
    private LoaiMonAn loaiMonAn;
    private List<LoaiMonAn> listLoai;

    public void themLoaiMonAn(LoaiMonAn loaiMonAn) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            loaiMonAn.setMaLoaiMonAn(this.generateLoaiMonAn(loaiMonAn));
            session.save(loaiMonAn);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public List<LoaiMonAn> getListLoai() {
        List<LoaiMonAn> listLoai = null;
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;
        try  {
            transaction = session.beginTransaction();
            // Sử dụng HQL để lấy tất cả mon an
            org.hibernate.query.Query<LoaiMonAn> query = session.createQuery("FROM LoaiMonAn", LoaiMonAn.class);
            listLoai  = query.list(); // Nhớ lưu kết quả vào danh sách
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception if needed
            listLoai = null;
        }
        return listLoai;
    }

    public LoaiMonAn getMaLoaiMon(String id) {
        LoaiMonAn loaiMonAn = null;
        try (Session session = HibernateUtils.getFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            loaiMonAn = session.get(LoaiMonAn.class, id); // Retrieve LoaiMonAn by primary key (id)

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception if needed
        }
        return loaiMonAn;
    }

    public LoaiMonAn getLoaiMonByName(String tenLoai) {
        Session session = HibernateUtils.getFactory().openSession();
        try {
            return session.createQuery("from LoaiMonAn where tenLoaiMonAn = :tenLoai", LoaiMonAn.class)
                    .setParameter("tenLoai", tenLoai)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }

    private String generateLoaiMonAn(LoaiMonAn loaiMonAn) {
        String prefix = generatePrefixFromName(loaiMonAn.getTenLoaiMonAn()); // Generate the "XX" part from the item name
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
        if(name.isEmpty())
            throw new IllegalArgumentException("tenLoaiMonAn must be specified");
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
}
