package org.login.quanlydatban;

import net.datafaker.Faker;
import org.hibernate.Session;
import org.login.quanlydatban.entity.*;
import org.login.quanlydatban.entity.enums.*;
import org.login.quanlydatban.entity.keygenerator.CTHDCompositeKey;
import org.login.quanlydatban.hibernate.HibernateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GenerateDB {
    public static void main(String[] args) {

        Faker faker = new Faker();

        List<KhachHang> khachHangList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Session session = HibernateUtils.getFactory().openSession();
            session.getTransaction().begin();
            KhachHang kh = new KhachHang();
            kh.setTenKhachHang(faker.name().fullName());
            kh.setEmail(faker.internet().emailAddress());
            kh.setSdt(faker.options().option("0334023155", "0323264266"));
            kh.setCccd(faker.options().option("012345678999", "082304017616", "013624578999"));
            kh.setDiaChi(faker.address().fullAddress());
            session.persist(kh);
            khachHangList.add(kh);

            session.getTransaction().commit();
            session.close();
        }

        // Tạo dữ liệu giả cho bảng NhanVien ngay sinh, dia chi, gioi tinh, sdt, cccd, ttnv, chuc vu
        List<NhanVien> nhanVienList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Session session = HibernateUtils.getFactory().openSession();
            session.getTransaction().begin();
            NhanVien nv = new NhanVien();
            nv.setTenNhanVien(faker.name().femaleFirstName() + " " + faker.name().lastName() + " " + faker.name().fullName());
            nv.setDiaChi(faker.address().fullAddress());
            nv.setSdt(faker.options().option("0334023155", "0323264266"));
            nv.setCccd(faker.options().option("012345678999", "082304017616", "013624578999"));

            int year = LocalDate.now().getYear() - ThreadLocalRandom.current().nextInt(18, 66);
            int month = ThreadLocalRandom.current().nextInt(1, 13);
            int day = ThreadLocalRandom.current().nextInt(1, 29);
            LocalDate randomBirthDate = LocalDate.of(year, month, day);
            nv.setNgaySinh(randomBirthDate);
            nv.setGioiTinh(faker.random().nextBoolean());
            ChucVu randomChucVu = ChucVu.values()[faker.random().nextInt(0, ChucVu.values().length - 1)];
            TrangThaiNhanVien randomTrangThaiNhanVien = TrangThaiNhanVien.values()[faker.random().nextInt(0, TrangThaiNhanVien.values().length - 1)];
            nv.setChucVuNhanVien(randomChucVu);
            nv.setTrangThaiNhanVien(randomTrangThaiNhanVien);// Vị trí

            TaiKhoan tk = new TaiKhoan(nv.getTenNhanVien(), "1111", nv);
            nv.setTaiKhoan(tk);
            nhanVienList.add(nv);
            session.persist(nv);
            session.persist(tk);
            System.out.println(nv.getTenNhanVien());
            session.getTransaction().commit();
            session.close();
        }

        List<LoaiMonAn> loaiMonAnList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Session session = HibernateUtils.getFactory().openSession();
            session.getTransaction().begin();

            String tem = faker.food().dish();
            LoaiMonAn loaiMonAn = new LoaiMonAn();
            loaiMonAn.setTenLoaiMonAn(tem);
            loaiMonAnList.add(loaiMonAn);
            session.persist(loaiMonAn);

            session.getTransaction().commit();
            session.close();
        }

        List<MonAn> monAnList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Session session = HibernateUtils.getFactory().openSession();
            session.getTransaction().begin();
            MonAn sp = new MonAn();
            sp.setTenMonAn(faker.commerce().productName());
            sp.setDonGia(Double.parseDouble(faker.commerce().price(10.0, 1000.0)));
            sp.setLoaiMonAn(loaiMonAnList.get(faker.random().nextInt(0, loaiMonAnList.size() - 1)));


            monAnList.add(sp);
            session.persist(sp);

            session.getTransaction().commit();
            session.close();
        }

//      // Table ban loai ban , trang thai ban, khu vuc
        List<Ban> banList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Session session = HibernateUtils.getFactory().openSession();
            session.getTransaction().begin();

            Ban sp = new Ban();

            LoaiBan randomLoaiBan = LoaiBan.values()[faker.number().numberBetween(0, LoaiBan.values().length - 1)];
            sp.setLoaiBan(randomLoaiBan);
            KhuVuc randomKhuVuc = KhuVuc.values()[faker.number().numberBetween(0, KhuVuc.values().length - 1)];
            sp.setKhuVuc(randomKhuVuc);
            TrangThaiBan randomTrangThaiBan = TrangThaiBan.values()[faker.number().numberBetween(0, TrangThaiBan.values().length - 1)];
            sp.setTrangThaiBan(randomTrangThaiBan);

            banList.add(sp);
            session.persist(sp);

            session.getTransaction().commit();
            session.close();
        }
        //ChucVu.values()[faker.random().nextInt(0, ChucVu.values().length - 1)];
        // ngay lap, phu thu, trang thai hoa don, ma ban, ma khach hang, ma nhan vien, tong tien, chiet khau
//       // Tạo dữ liệu giả cho bảng hoadon
        List<HoaDon> hoaDonList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Session session = HibernateUtils.getFactory().openSession();
            session.getTransaction().begin();
            HoaDon hd = new HoaDon();
            hd.setNgayLap(LocalDate.now());
            hd.setPhuThu(Double.parseDouble(faker.commerce().price(10.0, 1000.0)));
            hd.setTrangThaiHoaDon(TrangThaiHoaDon.DA_DAT);
            // Trạng thái hóa đơn
            int randomIndexBan = faker.random().nextInt(banList.size() - 1);
            Ban randomBan = banList.get(randomIndexBan);
            hd.setBan(randomBan); // Ngày lập hóa đơn
            hd.setChietKhau(Double.parseDouble(faker.commerce().price(0.0, 100.0))); // Chiết khấu
            // Mã bàn
            int randomIndexKH = faker.random().nextInt(khachHangList.size() - 1);
            KhachHang randomKH = khachHangList.get(randomIndexKH);
            hd.setKhachHang(randomKH);

            int randomIndex = faker.random().nextInt(nhanVienList.size() - 1);
            NhanVien randomNhanVien = nhanVienList.get(randomIndex);
            hd.setNhanVien(randomNhanVien); // Mã nhân viên
            hoaDonList.add(hd);
            session.persist(hd);

            session.getTransaction().commit();
            session.close();


        }
        // thoi gian dat, thoi gian nhan, khach hang, nhan vien, so luong nguoi, hoa don, tien coc, ghi chu
        List<LichDat> lichiDatList = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            Session session = HibernateUtils.getFactory().openSession();
            session.getTransaction().begin();
            LichDat ld = new LichDat();
            LocalDate today = LocalDate.now();

            // Tạo số ngày ngẫu nhiên từ 0 đến 365
            int daysToAdd = ThreadLocalRandom.current().nextInt(0, 366);
            // Tính toán ngày ngẫu nhiên
            LocalDate randomFutureDate = today.plusDays(daysToAdd);
            ld.setThoiGianDat(LocalDateTime.of(randomFutureDate, LocalTime.MIN));

            int nam = ThreadLocalRandom.current().nextInt(0, LocalDateTime.now().getYear());
            int thang = ThreadLocalRandom.current().nextInt(1, 13);
            int ngay = ThreadLocalRandom.current().nextInt(1, 29);
            LocalDate randomNgay = LocalDate.of(nam, thang, ngay);
            int gio = faker.random().nextInt(0, 20);
            int phut = faker.random().nextInt(0, 11)*5;
            ld.setThoiGianNhanBan(LocalDateTime.of(randomNgay, LocalTime.of(gio,phut)));

            // nhan vien
            int randomNhanVien = faker.random().nextInt(0, nhanVienList.size()-1);
            NhanVien nv = session.get(NhanVien.class, nhanVienList.get(randomNhanVien).getMaNhanVien());
            ld.setNhanVien(nv);

            // khach hnag
            int randomKH = faker.random().nextInt(0, khachHangList.size()-1);
            KhachHang kh = session.get(KhachHang.class, khachHangList.get(randomKH).getMaKhachHang());
            ld.setKhachHang(kh);

            ld.setSoLuongNguoi(faker.random().nextInt(10));

            // tao hoa don
            HoaDon hd = new HoaDon();
            hd.setNgayLap(randomNgay);
            hd.setPhuThu(Double.parseDouble(faker.commerce().price(10.0, 1000.0)));
            hd.setTrangThaiHoaDon(TrangThaiHoaDon.DA_DAT);
            // Trạng thái hóa đơn
            int randomIndexBan = faker.random().nextInt(banList.size() - 1);
            Ban randomBan = session.get(Ban.class, banList.get(randomIndexBan).getMaBan());
            hd.setBan(randomBan); // set ban

            hd.setChietKhau(Double.parseDouble(faker.commerce().price(0.0, 100.0))); // Chiết khấu

            // khach hang
            hd.setKhachHang(kh);
            // nhan vien
            hd.setNhanVien(nv);
            hoaDonList.add(hd);
            session.persist(hd);

            ld.setHoaDon(hd);
            lichiDatList.add(ld);
            session.persist(ld);

            session.getTransaction().commit();
            session.close();


        }

        // Mở một phiên làm việc duy nhất
        List<ChiTietHoaDon> chiTietHoaDonList = new ArrayList<>();
        Session session = HibernateUtils.getFactory().openSession();
        session.getTransaction().begin();

        for (int i = 0; i < hoaDonList.size(); i++) {
            for (int j = 0; j < monAnList.size(); j++) {
                ChiTietHoaDon pn = new ChiTietHoaDon();

                // Lấy đối tượng MonAn từ phiên thay vì tạo mới
                MonAn monAn = session.get(MonAn.class, monAnList.get(j).getMaMonAn());
                pn.setMonAn(monAn);
                HoaDon hoaDon = session.get(HoaDon.class, hoaDonList.get(i).getMaHoaDon());
                pn.setHoaDon(hoaDon);

                // Tạo khóa composite
                CTHDCompositeKey compositeKey = new CTHDCompositeKey(
                        hoaDonList.get(i).getMaHoaDon(),
                        monAn.getMaMonAn() // Sử dụng đối tượng đã lấy
                );
                pn.setMaChiTietHoaDon(compositeKey);

                // Thiết lập số lượng và ghi chú
                pn.setSoLuong(faker.random().nextInt(1, 4));
                pn.setGhiChu(faker.options().option("It rau", "It cay", "Nhieu ot"));

                // Kiểm tra sự tồn tại của bản ghi
                ChiTietHoaDon existingRecord = session.get(ChiTietHoaDon.class, compositeKey);
                if (existingRecord == null) {
                    // Thêm chi tiết hóa đơn vào danh sách
                    chiTietHoaDonList.add(pn);
                    // Lưu vào cơ sở dữ liệu
                    session.persist(pn);
                }
            }
        }






//        } catch (Exception e) {
//            e.printStackTrace();
//            SessionImplementor sessionImplementor = (SessionImplementor) session;
//            PersistenceContext persistenceContext = sessionImplementor.getPersistenceContext();
//
//            persistenceContext.getEntitiesByKey().forEach((key, entity) -> {
//                System.out.println("Entity Key: " + key + ", Entity: " + entity);
//            });
//        }

    }

}

