package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.login.quanlydatban.entity.*;
import org.login.quanlydatban.entity.enums.TrangThaiHoaDon;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

    private List<Object[]> dsHoaDon;

    public Object[] layDoanhThuVaSoHoaDon(String maNhanVien, LocalDate ngay) {
        Session session = HibernateUtils.getFactory().openSession();
        Object[] result = new Object[0];  // Initialize result as an empty array

        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);

            Root<HoaDon> rootHD = query.from(HoaDon.class);
            Join<HoaDon, NhanVien> joinNV = rootHD.join("nhanVien");
            Join<HoaDon, ChiTietHoaDon> joinCTHD = rootHD.join("chiTietHoaDon");
            Join<ChiTietHoaDon, MonAn> joinMA = joinCTHD.join("monAn");

            List<Predicate> predicates = new ArrayList<>();

            if (maNhanVien != null && !maNhanVien.isEmpty()) {
                predicates.add(builder.equal(joinNV.get("maNhanVien"), maNhanVien));
            }
            if (ngay != null) {
                predicates.add(builder.equal(rootHD.get("ngayLap"), ngay));
            }

            if (!predicates.isEmpty()) {
                query.where(predicates.toArray(new Predicate[0]));
            }

            query.multiselect(
                    builder.sum(builder.prod(joinMA.get("donGia"), joinCTHD.get("soLuong"))),
                    builder.countDistinct(rootHD.get("maHoaDon"))
            );

            if (maNhanVien != null && !maNhanVien.isEmpty()) {
                query.groupBy(joinNV.get("maNhanVien"));
            }

            List<Object[]> results = session.createQuery(query).getResultList();
            if (!results.isEmpty()) {
                result = results.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return result;
    }



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

            List<Predicate> predicates = new ArrayList<>();

            if (maNhanVien != null && !maNhanVien.isEmpty()) {
                predicates.add(builder.equal(joinNV.get("maNhanVien"), maNhanVien));
            }

            if (nam != null) {
                predicates.add(builder.equal(builder.function("YEAR", Integer.class, rootHD.get("ngayLap")), nam));
            }

            if (!predicates.isEmpty()) {
                query.where(predicates.toArray(new Predicate[0]));
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

            List<Predicate> predicates = new ArrayList<>();

            if (maNhanVien != null && !maNhanVien.isEmpty()) {
                predicates.add(builder.equal(joinNV.get("maNhanVien"), maNhanVien));
            }

            if (nam != 0) {
                predicates.add(builder.equal(builder.function("YEAR", Integer.class, rootHD.get("ngayLap")), nam));
            }

            if (!predicates.isEmpty()) {
                query.where(predicates.toArray(new Predicate[0]));
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
            if (maNhanVien != null) {
                query.where(builder.equal(joinNV.get("maNhanVien"), maNhanVien));

            }
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

            Join<ChiTietHoaDon, MonAn> joinMA = rootCTHD.join("monAn");
            Join<MonAn, LoaiMonAn> joinLoaiMA = joinMA.join("loaiMonAn");
            Join<ChiTietHoaDon, HoaDon> joinHD = rootCTHD.join("hoaDon");

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

    public List<Integer> layCacNamLapHoaDon(String maNV) {
        Session session = HibernateUtils.getFactory().openSession();
        List<Integer> years = new ArrayList<>();

        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Integer> query = builder.createQuery(Integer.class);
            Root<HoaDon> rootHD = query.from(HoaDon.class);

            Join<HoaDon, NhanVien> joinNV = rootHD.join("nhanVien");

            if(maNV != null) {
                query.where(builder.equal(joinNV.get("maNhanVien"), maNV));
            }
            query.select(builder.function("YEAR", Integer.class, rootHD.get("ngayLap")));

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
    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> listHoaDon = new ArrayList<>(); // Khởi tạo

       // List<HoaDon> listHoaDon = null;
        Session session = HibernateUtils.getFactory().openSession();
        try (session) {
            session.getTransaction().begin();
            listHoaDon = session.createQuery("FROM HoaDon", HoaDon.class).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            session.close();
        }
        if(listHoaDon == null){
            System.out.println("Không có hóa đơn nào");
        }

        return listHoaDon;
    }

    public HoaDon lapHoaDon(HoaDon hoaDon) {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();

        session.persist(hoaDon);

        session.getTransaction().commit();
        session.close();

        return hoaDon;
    }

    public HoaDon getHoaDon(String maHD) {
        Session session = HibernateUtils.getFactory().openSession();

        HoaDon hoaDon = session.get(HoaDon.class, maHD);

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
    public List<Object[]> laySoHoaDonTheoTrangThaiHoaDon(String maNV, int nam, int quy, int thang) {
        List<Object[]> result = new ArrayList<>();

        try (Session session = HibernateUtils.getFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
            Root<HoaDon> rootHD = query.from(HoaDon.class);

            Join<HoaDon, NhanVien> joinNV = rootHD.join("nhanVien");

            query.multiselect(
                    rootHD.get("trangThaiHoaDon"), // Trạng thái hóa đơn
                    builder.countDistinct(rootHD.get("maHoaDon")) // Đếm số hóa đơn
            );

            List<Predicate> predicates = new ArrayList<>();

            // Thêm điều kiện cho năm
            if (nam != 0) {
                predicates.add(builder.equal(builder.function("year", Integer.class, rootHD.get("ngayLap")), nam));
            }

            if (quy != 0 && thang == 0) {
                predicates.add(builder.equal(builder.function("quarter", Integer.class, rootHD.get("ngayLap")), quy));
            }

            if (thang != 0) {
                predicates.add(builder.equal(builder.function("month", Integer.class, rootHD.get("ngayLap")), thang));
            }

            if (maNV != null && !maNV.isEmpty()) {
                predicates.add(builder.equal(joinNV.get("maNhanVien"), maNV));
            }

            if (!predicates.isEmpty()) {
                query.where(predicates.toArray(new Predicate[0]));
            }

            query.groupBy(rootHD.get("trangThaiHoaDon"));
            query.orderBy(builder.desc(builder.countDistinct(rootHD.get("maHoaDon"))));

            result = session.createQuery(query).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }









}
