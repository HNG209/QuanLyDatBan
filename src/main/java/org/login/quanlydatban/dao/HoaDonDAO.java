package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.login.quanlydatban.entity.*;
import org.login.quanlydatban.entity.enums.TrangThaiHoaDon;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

    private List<Object[]> dsHoaDon;

    public Object[] layDoanhThuVaSoHoaDonTheoMaNV(String maNhanVien, LocalDate ngay) {
        Session session = HibernateUtils.getFactory().openSession();
        Object[] result = null;

        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);

            // Define root for HoaDon table
            Root<HoaDon> rootHD = query.from(HoaDon.class);

            // Perform joins with other tables
            Join<HoaDon, NhanVien> joinNV = rootHD.join("nhanVien"); // "nhanVien" is the field in HoaDon that references NhanVien
            Join<HoaDon, ChiTietHoaDon> joinCTHD = rootHD.join("chiTietHoaDon"); // "chiTietHoaDons" is the collection in HoaDon
            Join<ChiTietHoaDon, MonAn> joinMA = joinCTHD.join("monAn"); // "monAn" is the field in ChiTietHoaDon that references MonAn

            // Add where conditions
            if (ngay != null) {
                query.where(
                        builder.equal(joinNV.get("maNhanVien"), maNhanVien),
                        builder.equal(rootHD.get("ngayLap"), ngay) // Assuming "ngayLap" is the date field in HoaDon
                );
            } else {
                query.where(
                        builder.equal(joinNV.get("maNhanVien"), maNhanVien)
                );
            }

            // Select fields for total revenue and invoice count
            query.multiselect(
                    builder.sum(builder.prod(joinMA.get("donGia"), joinCTHD.get("soLuong"))), // Total revenue
                    builder.countDistinct(rootHD.get("maHoaDon")) // Invoice count
            );

            // Group by NhanVien
            query.groupBy(joinNV.get("maNhanVien"));

            // Execute query and get result
            List<Object[]> results = session.createQuery(query).getResultList();
            if (!results.isEmpty()) {
                result = results.get(0); // Get the first result
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return result; // Returns total revenue and invoice count, or null if no result
    }

    // Method to get total revenue and invoice count by month for a specific employee
    public List<Object[]> layDoanhThuVaSoHoaDonTheoThang(String maNhanVien, Integer nam) {
        Session session = HibernateUtils.getFactory().openSession();
        List<Object[]> results = new ArrayList<>();

        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
            Root<HoaDon> rootHD = query.from(HoaDon.class);

            Join<HoaDon, NhanVien> joinNV = rootHD.join("nhanVien");
            Join<HoaDon, ChiTietHoaDon> joinCTHD = rootHD.join("chiTietHoaDon");
            Join<ChiTietHoaDon, MonAn> joinMA = joinCTHD.join("monAn");

            query.where(builder.equal(joinNV.get("maNhanVien"), maNhanVien));

            if (nam != null) {
                query.where(builder.equal(builder.function("YEAR", Integer.class, rootHD.get("ngayLap")), nam));
            }

            query.multiselect(
                    builder.function("MONTH", Integer.class, rootHD.get("ngayLap")),
                    builder.sum(builder.prod(joinMA.get("donGia"), joinCTHD.get("soLuong"))),
                    builder.countDistinct(rootHD.get("maHoaDon"))
            );
            query.groupBy(builder.function("MONTH", Integer.class, rootHD.get("ngayLap")));

            results = session.createQuery(query).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return results;
    }

    public List<Object[]> layDoanhThuVaSoHoaDonTheoQuy(String maNhanVien, int nam) {
        Session session = HibernateUtils.getFactory().openSession();
        List<Object[]> results = new ArrayList<>();

        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
            Root<HoaDon> rootHD = query.from(HoaDon.class);

            Join<HoaDon, NhanVien> joinNV = rootHD.join("nhanVien");
            Join<HoaDon, ChiTietHoaDon> joinCTHD = rootHD.join("chiTietHoaDon");
            Join<ChiTietHoaDon, MonAn> joinMA = joinCTHD.join("monAn");

            query.where(builder.equal(joinNV.get("maNhanVien"), maNhanVien));

            if (nam != 0) {
                query.where(builder.equal(builder.function("YEAR", Integer.class, rootHD.get("ngayLap")), nam));
            }

            query.multiselect(
                    builder.function("QUARTER", Integer.class, rootHD.get("ngayLap")),
                    builder.sum(builder.prod(joinMA.get("donGia"), joinCTHD.get("soLuong"))),
                    builder.countDistinct(rootHD.get("maHoaDon"))
            );
            query.groupBy(builder.function("QUARTER", Integer.class, rootHD.get("ngayLap")));

            results = session.createQuery(query).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return results;
    }

    // Method to get total revenue and invoice count by year for a specific employee
    public List<Object[]> layDoanhThuVaSoHoaDonTheoNam(String maNhanVien) {
        Session session = HibernateUtils.getFactory().openSession();
        List<Object[]> results = new ArrayList<>();

        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
            Root<HoaDon> rootHD = query.from(HoaDon.class);

            Join<HoaDon, NhanVien> joinNV = rootHD.join("nhanVien");
            Join<HoaDon, ChiTietHoaDon> joinCTHD = rootHD.join("chiTietHoaDon");
            Join<ChiTietHoaDon, MonAn> joinMA = joinCTHD.join("monAn");

            query.where(builder.equal(joinNV.get("maNhanVien"), maNhanVien));

            query.multiselect(
                    builder.function("YEAR", Integer.class, rootHD.get("ngayLap")),
                    builder.sum(builder.prod(joinMA.get("donGia"), joinCTHD.get("soLuong"))),
                    builder.countDistinct(rootHD.get("maHoaDon"))
            );
            query.groupBy(builder.function("YEAR", Integer.class, rootHD.get("ngayLap")));

            results = session.createQuery(query).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return results;

    }

    public List<Object[]> layDoanhThuTheoLoaiMonAn(int nam, int quy, int thang) {
        List<Object[]> result = new ArrayList<>();

        try (Session session = HibernateUtils.getFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
            Root<ChiTietHoaDon> rootCTHD = query.from(ChiTietHoaDon.class);

            // Thực hiện các join cần thiết
            Join<ChiTietHoaDon, MonAn> joinMA = rootCTHD.join("monAn"); // Join với bảng MonAn
            Join<MonAn, LoaiMonAn> joinLoaiMA = joinMA.join("loaiMonAn"); // Join với bảng LoaiMonAn
            Join<ChiTietHoaDon, HoaDon> joinHD = rootCTHD.join("hoaDon"); // Join với bảng HoaDon

            // Tính tổng doanh thu theo loại món ăn
            query.multiselect(
                    joinLoaiMA.get("tenLoaiMonAn"), // Tên loại món ăn
                    builder.sum(builder.prod(joinMA.get("donGia"), rootCTHD.get("soLuong"))) // Tính tổng doanh thu
            );


            List<Predicate> predicates = new ArrayList<>();


            if (nam != 0) {
                predicates.add(builder.equal(builder.function("year", Integer.class, joinHD.get("ngayLap")), nam));


                if (quy != 0 && thang == 0) {
                    predicates.add(builder.equal(
                            builder.function("quarter", Integer.class, joinHD.get("ngayLap")), quy
                    ));
                }


                if (thang != 0) {
                    predicates.add(builder.equal(builder.function("month", Integer.class, joinHD.get("ngayLap")), thang));
                }
            }


            if (!predicates.isEmpty()) {
                query.where(predicates.toArray(new Predicate[0]));
            }


            query.groupBy(joinLoaiMA.get("tenLoaiMonAn"));

            query.orderBy(builder.desc(builder.sum(builder.prod(joinMA.get("donGia"), rootCTHD.get("soLuong")))));


            result = session.createQuery(query)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<Integer> layCacNamLapHoaDonTheoMaNV(String maNV) {
        Session session = HibernateUtils.getFactory().openSession();
        List<Integer> years = new ArrayList<>();

        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Integer> query = builder.createQuery(Integer.class);
            Root<HoaDon> rootHD = query.from(HoaDon.class);

            // Join với bảng NhanVien
            Join<HoaDon, NhanVien> joinNV = rootHD.join("nhanVien");

            // Thêm điều kiện lọc theo mã nhân viên
            query.where(builder.equal(joinNV.get("maNhanVien"), maNV));

            // Chọn năm lập hóa đơn
            query.select(builder.function("YEAR", Integer.class, rootHD.get("ngayLap")));

            // Thêm nhóm theo năm
            query.groupBy(builder.function("YEAR", Integer.class, rootHD.get("ngayLap")));

            // Thực hiện truy vấn
            years = session.createQuery(query).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return years; // Trả về danh sách các năm đã lập hóa đơn
    }

    public List<Object[]> layBangXepHangMonAnTheoSoLuongBanVaDoanhThu(int nam, int thang, int quy) {
        Session session = HibernateUtils.getFactory().openSession();
        List<Object[]> result = new ArrayList<>();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
            Root<HoaDon> rootHD = query.from(HoaDon.class);

            Join<ChiTietHoaDon, HoaDon> joinCTHD = rootHD.join("chiTietHoaDon");
            Join<ChiTietHoaDon, MonAn> joinMA = joinCTHD.join("monAn");

            query.multiselect(
                    joinMA.get("tenMonAn"),
                    builder.sum(builder.prod(joinMA.get("donGia"), joinCTHD.get("soLuong"))),
                    builder.sum(joinCTHD.get("soLuong"))
            );
            List<Predicate> predicates = new ArrayList<>();
            if (nam != 0) {
                predicates.add(builder.equal(builder.function("year", Integer.class, rootHD.get("ngayLap")), nam));
                if (quy != 0 && thang == 0) {
                    predicates.add(builder.equal(
                            builder.function("quarter", Integer.class, rootHD.get("ngayLap")), quy
                    ));
                }
                if (thang != 0) {
                    predicates.add(builder.equal(builder.function("month", Integer.class, rootHD.get("ngayLap")), thang));
                }
            }
            if (!predicates.isEmpty()) {
                query.where(predicates.toArray(new Predicate[0]));
            }
            query.groupBy(joinMA.get("tenMonAn"));
            query.orderBy(builder.desc(builder.sum(joinCTHD.get("soLuong"))),builder.desc(builder.sum(builder.prod(joinMA.get("donGia"), joinCTHD.get("soLuong")))));
            result = session.createQuery(query)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        session.close();
        return result;

    }

    public HoaDon lapHoaDon(HoaDon hoaDon) {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();

        session.save(hoaDon);

        session.getTransaction().commit();
        session.close();

        return hoaDon;
    }

    public HoaDon updateHoaDon(HoaDon hoaDon) {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();

        session.update(hoaDon);

        session.getTransaction().commit();
        session.close();

        return hoaDon;
    }

    public List<HoaDon> getHoaDonFromBan(Ban ban) {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<HoaDon> query = builder.createQuery(HoaDon.class);
        Root<HoaDon> hoaDonRoot = query.from(HoaDon.class);

        Join<HoaDon, Ban> joinBan = hoaDonRoot.join("ban");

        Predicate predicate = builder.equal(joinBan.get("maBan"), ban.getMaBan());
        Predicate predicate1 = builder.equal(hoaDonRoot.get("trangThaiHoaDon"), TrangThaiHoaDon.CHUA_THANH_TOAN);

        query.select(hoaDonRoot).where(builder.and(predicate, predicate1));

        List<HoaDon> list = session.createQuery(query).getResultList();

        session.getTransaction().commit();
        session.close();

        return list;
    }

}
