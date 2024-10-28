package org.login.quanlydatban.dao;

import java.sql.*;
import java.time.LocalDate;

public class HoaDonDAO {

    private Connection connection;

    public HoaDonDAO(Connection connection) {
        this.connection = connection;
    }

    // Phương thức tính tổng doanh thu theo ngày hiện tại
    public double getTongDoanhThuTheoNgay(LocalDate ngay, String maNhanVien) throws SQLException {
        String sql = "SELECT SUM(ma.donGia * ct.soLuong) AS tong_doanh_thu " +
                "FROM HoaDon hd " +
                "JOIN ChiTietHoaDon ct ON hd.maHoaDon = ct.maHoaDon " +
                "JOIN MonAn ma ON ct.maMonAn = ma.maMonAn " +
                "WHERE DATE(hd.ngayLap) = ? AND hd.maNhanVien = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(ngay));
            stmt.setString(2, maNhanVien);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("tong_doanh_thu");
                }
            }
        }
        return 0;
    }

    // Phương thức tính tổng số hóa đơn theo ngày hiện tại
    public int getTongSoHoaDonTheoNgay(LocalDate ngay, String maNhanVien) throws SQLException {
        String sql = "SELECT COUNT(*) AS Tong_So_Hoa_Don " +
                "FROM HoaDon hd " +
                "WHERE DATE(ngayLap) = ? AND hd.maNhanVien = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(ngay));
            stmt.setString(2, maNhanVien);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Tong_So_Hoa_Don");
                }
            }
        }
        return 0;
    }

    // Phương thức tính tổng doanh thu theo tháng
    public double getTongDoanhThuTheoThang(int thang, int nam, String maNhanVien) throws SQLException {
        String sql = "SELECT SUM(ma.donGia * ct.soLuong) AS tong_doanh_thu " +
                "FROM HoaDon hd " +
                "JOIN ChiTietHoaDon ct ON hd.maHoaDon = ct.maHoaDon " +
                "JOIN MonAn ma ON ct.maMonAn = ma.maMonAn " +
                "WHERE MONTH(hd.ngayLap) = ? AND YEAR(hd.ngayLap) = ? AND hd.maNhanVien = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, thang);
            stmt.setInt(2, nam);
            stmt.setString(3, maNhanVien);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("tong_doanh_thu");
                }
            }
        }
        return 0;
    }

    // Phương thức tính tổng số hóa đơn theo tháng
    public int getTongSoHoaDonTheoThang(int thang, int nam, String maNhanVien) throws SQLException {
        String sql = "SELECT COUNT(*) AS Tong_So_Hoa_Don " +
                "FROM HoaDon hd " +
                "WHERE MONTH(hd.ngayLap) = ? AND YEAR(hd.ngayLap) = ? AND hd.maNhanVien = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, thang);
            stmt.setInt(2, nam);
            stmt.setString(3, maNhanVien);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Tong_So_Hoa_Don");
                }
            }
        }
        return 0;
    }

    // Phương thức tính tổng doanh thu theo năm
    public double getTongDoanhThuTheoNam(int nam, String maNhanVien) throws SQLException {
        String sql = "SELECT SUM(ma.donGia * ct.soLuong) AS tong_doanh_thu " +
                "FROM HoaDon hd " +
                "JOIN ChiTietHoaDon ct ON hd.maHoaDon = ct.maHoaDon " +
                "JOIN MonAn ma ON ct.maMonAn = ma.maMonAn " +
                "WHERE YEAR(hd.ngayLap) = ? AND hd.maNhanVien = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, nam);
            stmt.setString(2, maNhanVien);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("tong_doanh_thu");
                }
            }
        }
        return 0;
    }

    // Phương thức tính tổng số hóa đơn theo năm
    public int getTongSoHoaDonTheoNam(int nam, String maNhanVien) throws SQLException {
        String sql = "SELECT COUNT(*) AS tong_so_HoaDon " +
                "FROM HoaDon hd " +
                "WHERE YEAR(hd.ngayLap) = ? AND hd.maNhanVien = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, nam);
            stmt.setString(2, maNhanVien);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("tong_so_HoaDon");
                }
            }
        }
        return 0;
    }

    // Phương thức tính tổng doanh thu theo quý
    public double getTongDoanhThuTheoQuy(int quy, int nam, String maNhanVien) throws SQLException {
        String sql = "SELECT SUM(ma.donGia * ct.soLuong) AS tong_doanh_thu " +
                "FROM HoaDon hd " +
                "JOIN ChiTietHoaDon ct ON hd.maHoaDon = ct.maHoaDon " +
                "JOIN MonAn ma ON ct.maMonAn = ma.maMonAn " +
                "WHERE QUARTER(hd.ngayLap) = ? AND YEAR(hd.ngayLap) = ? AND hd.maNhanVien = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, quy);
            stmt.setInt(2, nam);
            stmt.setString(3, maNhanVien);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("tong_doanh_thu");
                }
            }
        }
        return 0;
    }

    // Phương thức tính tổng số hóa đơn theo quý
    public int getTongSoHoaDonTheoQuy(int quy, int nam, String maNhanVien) throws SQLException {
        String sql = "SELECT COUNT(*) AS Tong_So_Hoa_Don " +
                "FROM HoaDon hd " +
                "WHERE QUARTER(hd.ngayLap) = ? AND YEAR(hd.ngayLap) = ? AND hd.maNhanVien = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, quy);
            stmt.setInt(2, nam);
            stmt.setString(3, maNhanVien);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Tong_So_Hoa_Don");
                }
            }
        }
        return 0;
    }
}
