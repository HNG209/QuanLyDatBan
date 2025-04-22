package org.login.quanlydatban.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.login.quanlydatban.entity.*;
import org.login.quanlydatban.entity.enums.TrangThaiHoaDon;
import org.login.quanlydatban.entity.keygenerator.DailyCounter;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

    private List<Object[]> dsHoaDon;

    public Object[] layDoanhThuVaSoHoaDon(String maNhanVien, LocalDate ngay) {
        Session session = HibernateUtils.getFactory().openSession();
        Object[] result = new Object[0];

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
            predicates.add(builder.equal(rootHD.get("trangThaiHoaDon"), TrangThaiHoaDon.DA_THANH_TOAN)); // Thêm điều kiện trạng thái

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
            predicates.add(builder.equal(rootHD.get("trangThaiHoaDon"), TrangThaiHoaDon.DA_THANH_TOAN)); // Thêm điều kiện trạng thái

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
            predicates.add(builder.equal(rootHD.get("trangThaiHoaDon"), TrangThaiHoaDon.DA_THANH_TOAN)); // Thêm điều kiện trạng thái

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

            // Thêm điều kiện lọc trạng thái "Đã thanh toán"
            Predicate statusPredicate = builder.equal(rootHD.get("trangThaiHoaDon"), TrangThaiHoaDon.DA_THANH_TOAN);

            if (maNhanVien != null) {
                query.where(builder.and(
                        builder.equal(joinNV.get("maNhanVien"), maNhanVien),
                        statusPredicate
                ));
            } else {
                query.where(statusPredicate);
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
            predicates.add(builder.equal(joinHD.get("trangThaiHoaDon"), TrangThaiHoaDon.DA_THANH_TOAN)); // Thêm điều kiện trạng thái

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

    public List<Object[]> layDoanhThuLoaiMonAnTheoNgay(String maNV, LocalDate date) {
        List<Object[]> result = new ArrayList<>();

        try (Session session = HibernateUtils.getFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
            Root<ChiTietHoaDon> rootCTHD = query.from(ChiTietHoaDon.class);

            Join<ChiTietHoaDon, MonAn> joinMA = rootCTHD.join("monAn");
            Join<MonAn, LoaiMonAn> joinLoaiMA = joinMA.join("loaiMonAn");
            Join<ChiTietHoaDon, HoaDon> joinHD = rootCTHD.join("hoaDon");
            Join<HoaDon, NhanVien> joinNV = joinHD.join("nhanVien");

            query.multiselect(
                    joinLoaiMA.get("tenLoaiMonAn"), // Tên loại món ăn
                    builder.sum(builder.prod(joinMA.get("donGia"), rootCTHD.get("soLuong"))) // Tính tổng doanh thu
            );

            List<Predicate> predicates = new ArrayList<>();

            if (maNV != null && !maNV.isEmpty()) {
                predicates.add(builder.equal(joinNV.get("maNhanVien"), maNV));
            }

            if (date != null) {
                predicates.add(builder.equal(joinHD.get("ngayLap"), date));
            }
            predicates.add(builder.equal(joinHD.get("trangThaiHoaDon"), TrangThaiHoaDon.DA_THANH_TOAN)); // Thêm điều kiện trạng thái

            if (!predicates.isEmpty()) {
                query.where(predicates.toArray(new Predicate[0]));
            }

            query.groupBy(joinLoaiMA.get("tenLoaiMonAn"));
            query.orderBy(builder.desc(builder.sum(builder.prod(joinMA.get("donGia"), rootCTHD.get("soLuong")))));

            result = session.createQuery(query).getResultList();
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

    public List<Object[]> layTop10MonAnTheoDoanhThuVaSoLuongBan() {
        List<Object[]> result = new ArrayList<>();
        Session session = HibernateUtils.getFactory().openSession();

        try {
            // Tạo CriteriaBuilder và CriteriaQuery
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);

            // Tạo Root và Join
            Root<HoaDon> rootHD = query.from(HoaDon.class);
            Join<HoaDon, ChiTietHoaDon> joinCTHD = rootHD.join("chiTietHoaDon");
            Join<ChiTietHoaDon, MonAn> joinMA = joinCTHD.join("monAn");

            // Lựa chọn các giá trị cần thiết
            query.multiselect(
                    joinMA.get("tenMonAn"), // Tên món ăn
                    builder.sum(builder.prod(joinMA.get("donGia"), joinCTHD.get("soLuong"))), // Tổng doanh thu
                    builder.sum(joinCTHD.get("soLuong")) // Tổng số lượng bán ra
            );

            query.groupBy(joinMA.get("tenMonAn"));

            // Thêm điều kiện trạng thái "DA_THANH_TOAN"
            query.where(builder.equal(rootHD.get("trangThaiHoaDon"), TrangThaiHoaDon.DA_THANH_TOAN));

            // Sắp xếp theo số lượng bán giảm dần, sau đó doanh thu giảm dần
            query.orderBy(
                    builder.desc(builder.sum(joinCTHD.get("soLuong"))), // Số lượng bán giảm dần
                    builder.desc(builder.sum(builder.prod(joinMA.get("donGia"), joinCTHD.get("soLuong")))) // Doanh thu giảm dần
            );

            // Thực thi truy vấn và lấy kết quả
            result = session.createQuery(query).setMaxResults(10).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

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
    public List<Object[]> laySoHoaDonTheoTrangThaiVaNgay(String maNV, LocalDate ngay) {
        List<Object[]> result = new ArrayList<>();

        try (Session session = HibernateUtils.getFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
            Root<HoaDon> rootHD = query.from(HoaDon.class);

            Join<HoaDon, NhanVien> joinNV = rootHD.join("nhanVien");

            query.multiselect(
                    rootHD.get("trangThaiHoaDon"),
                    builder.countDistinct(rootHD.get("maHoaDon"))
            );

            List<Predicate> predicates = new ArrayList<>();

            if (ngay != null) {
                predicates.add(builder.equal(rootHD.get("ngayLap"), ngay));
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
    public void xoaHoaDon(HoaDon hoaDon) {
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();

        String hql = "DELETE FROM HoaDon h WHERE h.maHoaDon = :maHoaDon";
        session.createQuery(hql).
                setParameter("maHoaDon", hoaDon.getMaHoaDon()).
                executeUpdate();

        session.getTransaction().commit();
        session.close();
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
}
