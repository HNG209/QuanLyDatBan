package org.login.quanlydatban.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.login.quanlydatban.dao.ChiTietHoaDonDAO;
import org.login.quanlydatban.dao.HoaDonDAO;
import org.login.quanlydatban.dao.NhanVienDAO;
import org.login.quanlydatban.entity.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HoaDonController implements Initializable {

    @FXML
    private TextField textMaKH;
    @FXML
    private TextField textTenKH;
    @FXML
    private TextField textsdtKH;
    @FXML
    private TextField textMaNV;
    @FXML
    private TextField textTenNV;
    @FXML
    private TextField textsdtNV;
    @FXML
    private ComboBox<String> cbTrangThaiNV;

    @FXML
    private TextField tfMaHoaDon;
    @FXML
    private TextField tfTenKhachHang;
    @FXML
    private ComboBox<String> cbTrangThai;
    @FXML
    private DatePicker dpNgayLap;
    @FXML
    private TableView<HoaDon> tableView;


    @FXML
    private TableColumn<HoaDon, String> colMaHoaDon;
    @FXML
    private TableColumn<HoaDon, LocalDate> colNgayLap;
    @FXML
    private TableColumn<HoaDon, String> colMaKhachHang;
    @FXML
    private TableColumn<HoaDon, String> colTrangThai;
    @FXML
    private TableColumn<HoaDon, String> colMaBan;
    @FXML
    private TableColumn<HoaDon, String> colMaNhanVien;
    @FXML
    private TableColumn<HoaDon, Double> colTongTien;
    @FXML
    private TableColumn<HoaDon, Double> colPhuThu;

    @FXML
    private TableView<HoaDon> tabTatCa;


    @FXML
    private TableView<ChiTietHoaDon> tableChiTietHoaDon;
    @FXML
    private TableColumn<ChiTietHoaDon, String> colTenMonAn;
    @FXML
    private TableColumn<ChiTietHoaDon, Integer> colSoLuong;
    @FXML
    private TableColumn<ChiTietHoaDon, String> colGhiChu;
    @FXML
    private TableView<ChiTietHoaDon> tableCTHD;
    private HoaDonDAO HoaDonDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HoaDonDAO = new HoaDonDAO();
        ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO();
        List<HoaDon> hoaDonList = HoaDonDAO.getAllHoaDon();

        ObservableList<HoaDon> observableList = FXCollections.observableArrayList(hoaDonList);


        try {
            colMaHoaDon.setCellValueFactory(new PropertyValueFactory<>("maHoaDon"));
            colNgayLap.setCellValueFactory(new PropertyValueFactory<>("ngayLap"));
            colMaKhachHang.setCellValueFactory(cellData -> {
                KhachHang khachHang = cellData.getValue().getKhachHang();
                return new SimpleStringProperty(khachHang != null ? khachHang.getMaKhachHang() : "");
            });
            colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThaiHoaDon"));
           colMaBan.setCellValueFactory(cellData -> {
               Ban ban = cellData.getValue().getBan();
               return new SimpleStringProperty(ban != null ? ban.getMaBan() : "");
           });
            colMaNhanVien.setCellValueFactory(cellData -> {
                NhanVien nhanVien = cellData.getValue().getNhanVien();
                return new SimpleStringProperty(nhanVien != null ? nhanVien.getMaNhanVien() : "");
            });

            colTongTien.setCellValueFactory(new PropertyValueFactory<>("tongTien"));
            colPhuThu.setCellValueFactory(new PropertyValueFactory<>("phuThu"));

            tabTatCa.setItems(observableList);

            colTenMonAn.setCellValueFactory(cellData -> {
                MonAn monAn = cellData.getValue().getMonAn();
                return new SimpleStringProperty(monAn != null ? monAn.getTenMonAn() : "");
            });
            colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
            //colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
            tabTatCa.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {
                    String maHoaDon = tabTatCa.getSelectionModel().getSelectedItem().getMaHoaDon();

                    List<ChiTietHoaDon> chiTietList = chiTietHoaDonDAO.getChiTietHoaDonByMaHoaDon(maHoaDon);

                    // Chuyển danh sách chi tiết hóa đơn thành ObservableList và đặt vào TableView
                    ObservableList<ChiTietHoaDon> observableList1 = FXCollections.observableArrayList(chiTietList);
                    tableCTHD.setItems(observableList1);

                    HoaDon hoaDon = tabTatCa.getSelectionModel().getSelectedItem();
                    if (hoaDon != null) {
                        // Lấy thông tin nhân viên và khách hàng
                        NhanVien nhanVien = hoaDon.getNhanVien();
                        KhachHang khachHang = hoaDon.getKhachHang();

                        // Hiển thị thông tin vào các TextField hoặc Label
                        if (nhanVien != null) {
                            textMaNV.setText(nhanVien.getMaNhanVien());
                            textTenNV.setText(nhanVien.getTenNhanVien());
                            textsdtNV.setText(nhanVien.getSdt());
                        }

                        if (khachHang != null) {
                            textMaKH.setText(khachHang.getMaKhachHang());
                            textTenKH.setText(khachHang.getTenKhachHang());
                            textsdtKH.setText(khachHang.getSdt());
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
       }
        ObservableList<String> trangThaiList = FXCollections.observableArrayList(
                "Đã thanh toán",
                "Chưa thanh toán"
        );

        // Đặt danh sách vào ComboBox
        cbTrangThai.setItems(trangThaiList);



    }
    public void onTimKiemClicked() {
        // Lấy thông tin từ các trường đầu vào
        String maHoaDon = tfMaHoaDon.getText().trim();
        String tenKhachHang = tfTenKhachHang.getText().trim();
        String trangThai = cbTrangThai.getSelectionModel().getSelectedItem();

        LocalDate ngayLap = dpNgayLap.getValue();

        // Danh sách hóa đơn gốc (từ cơ sở dữ liệu hoặc danh sách ban đầu)
        HoaDonDAO = new HoaDonDAO();
        List<HoaDon> danhSachGoc = HoaDonDAO.getAllHoaDon();


        // Lọc danh sách dựa trên các tiêu chí
        List<HoaDon> ketQuaLoc = danhSachGoc.stream()
                .filter(hoaDon -> maHoaDon.isEmpty() || hoaDon.getMaHoaDon().contains(maHoaDon))
                .filter(hoaDon -> tenKhachHang.isEmpty() || hoaDon.getKhachHang().getTenKhachHang().contains(tenKhachHang))
                .filter(hoaDon -> trangThai == null || hoaDon.getTrangThaiHoaDon().equals(trangThai))
                .filter(hoaDon -> ngayLap == null || hoaDon.getNgayLap().equals(ngayLap))
                .collect(Collectors.toList());

        // Cập nhật bảng với danh sách hóa đơn đã lọc
        tabTatCa.getItems().setAll(ketQuaLoc);
    }
    public void onResetClicked() {
        // 1. Xóa hết thông tin trong các TextField
        tfMaHoaDon.clear();
        tfTenKhachHang.clear();
        cbTrangThai.getSelectionModel().clearSelection();  // Nếu bạn muốn reset ComboBox trạng thái
        dpNgayLap.setValue(null);  // Nếu bạn có DatePicker cho ngày

        textTenNV.clear();
        textsdtKH.clear();
        textTenKH.clear();
        textTenKH.clear();
        textMaKH.clear();
        textMaNV.clear();
        textsdtNV.clear();

        tableChiTietHoaDon.setItems(null);
        // 2. Cập nhật lại bảng với tất cả các hóa đơn (không lọc)
        HoaDonDAO hoaDonDAO = new HoaDonDAO();
        List<HoaDon> danhSachGoc = hoaDonDAO.getAllHoaDon();

        // Chuyển danh sách hóa đơn thành ObservableList và gán vào bảng
        ObservableList<HoaDon> observableList = FXCollections.observableArrayList(danhSachGoc);
        tabTatCa.setItems(observableList);
    }



}
