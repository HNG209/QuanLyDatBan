package org.login.quanlydatban.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.login.quanlydatban.dao.ChiTietHoaDonDAO;
import org.login.quanlydatban.dao.HoaDonDAO;
import org.login.quanlydatban.entity.*;
import org.login.quanlydatban.entity.enums.TrangThaiHoaDon;

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
    private TextField textTrangThaiNV;

    @FXML
    private TextField tfMaHoaDon;
    @FXML
    private TextField tfTenKhachHang;
    @FXML
    private ComboBox<String> cbTrangThai;
    @FXML
    private DatePicker dpNgayLap;



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
    private TableColumn<HoaDon, String> colTongTien;
    @FXML
    private TableColumn<HoaDon, Double> colPhuThu;

    @FXML
    private TableView<HoaDon> tabTatCa;



    @FXML
    private TableColumn<ChiTietHoaDon, String> colTenMonAn;
    @FXML
    private TableColumn<ChiTietHoaDon, Integer> colSoLuong;
    @FXML
    private TableColumn<ChiTietHoaDon, String> colDonGia;
    @FXML
    private TableColumn<ChiTietHoaDon, String> colGhiChu;
    @FXML
    private TableColumn<ChiTietHoaDon, String> colCTHDTongTien;
    @FXML
    private TableView<ChiTietHoaDon> tableCTHD;
    private HoaDonDAO HoaDonDAO;
    private MonAn monAn;

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

            colTongTien.setCellValueFactory(cellData -> {
                HoaDon hoaDon = cellData.getValue();
                String tongTien = String.valueOf(hoaDon.tinhTongTien()+ hoaDon.getPhuThu());
                return new SimpleStringProperty(tongTien);
            });
            colPhuThu.setCellValueFactory(new PropertyValueFactory<>("phuThu"));

            tabTatCa.setItems(observableList);

            colTenMonAn.setCellValueFactory(cellData -> {
                MonAn monAn = cellData.getValue().getMonAn();
                return new SimpleStringProperty(monAn != null ? monAn.getTenMonAn() : "");
            });
            colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
            colDonGia.setCellValueFactory(cellData -> {
                ChiTietHoaDon chiTiet = cellData.getValue();
                MonAn monAn = chiTiet.getMonAn();

                String donGia = (monAn != null) ? String.format("%.0f", monAn.getDonGia()) : "0";

                return new SimpleStringProperty(donGia);
            });

            colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
            colCTHDTongTien.setCellValueFactory(cellData -> {
                ChiTietHoaDon chiTiet = cellData.getValue();
                double tongTien = chiTiet.tinhTongCTHD();
                return new SimpleStringProperty(String.format("%.2f", tongTien));
            });

            tabTatCa.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {
                    String maHoaDon = tabTatCa.getSelectionModel().getSelectedItem().getMaHoaDon();

                    List<ChiTietHoaDon> chiTietList = chiTietHoaDonDAO.getChiTietHoaDonByMaHoaDon(maHoaDon);


                    ObservableList<ChiTietHoaDon> observableList1 = FXCollections.observableArrayList(chiTietList);
                    tableCTHD.setItems(observableList1);

                    HoaDon hoaDon = tabTatCa.getSelectionModel().getSelectedItem();
                    if (hoaDon != null) {

                        NhanVien nhanVien = hoaDon.getNhanVien();
                        KhachHang khachHang = hoaDon.getKhachHang();

                        if (nhanVien != null) {
                            textMaNV.setText(nhanVien.getMaNhanVien());

                            textTenNV.setText(nhanVien.getTenNhanVien());
                            textsdtNV.setText(nhanVien.getSdt());

                            textTrangThaiNV.setText(nhanVien.getTrangThaiNhanVien().toString());
                        }

                        if (khachHang != null) {
                            textMaKH.setText(khachHang.getMaKhachHang());
                            textTenKH.setText(khachHang.getTenKhachHang());
                            textsdtKH.setText(khachHang.getSdt());
                        }else {
                            textMaKH.setText("");
                            textTenKH.setText("");
                            textsdtKH.setText("");
                        }

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
       }
        ObservableList<String> trangThaiList = FXCollections.observableArrayList(
                "Trạng thái",
                "Đã thanh toán",
                "Chưa thanh toán",
                "Đã đặt",
                "Đã huỷ"
        );

        // Đặt danh sách vào ComboBox
        cbTrangThai.getSelectionModel().select("Trạng thái");
        cbTrangThai.setItems(trangThaiList);



    }
    public void onTimKiemClicked() {
        // Lấy thông tin từ các trường đầu vào
        String maHoaDon = tfMaHoaDon.getText().trim();
        String tenKhachHang = tfTenKhachHang.getText().trim();
        String trangThai = cbTrangThai.getSelectionModel().getSelectedItem();

        LocalDate ngayLap = dpNgayLap.getValue();

        HoaDonDAO = new HoaDonDAO();
        List<HoaDon> danhSachGoc = HoaDonDAO.getAllHoaDon();


        // Lọc danh sách dựa trên các tiêu chí
        List<HoaDon> ketQuaLoc = danhSachGoc.stream()
                .filter(hoaDon -> maHoaDon.isEmpty() || hoaDon.getMaHoaDon().contains(maHoaDon))
                .filter(hoaDon -> tenKhachHang.isEmpty() ||  (hoaDon.getKhachHang() != null && hoaDon.getKhachHang().getMaKhachHang().contains(tenKhachHang)))
                .filter(hoaDon -> {
                    TrangThaiHoaDon trangThaiEnum = convertStringToEnum(trangThai);
                    TrangThaiHoaDon trangThaiHoaDonEnum = hoaDon.getTrangThaiHoaDon();
                    return trangThaiEnum == null
                            || trangThaiEnum == trangThaiHoaDonEnum;
                })
                .filter(hoaDon -> ngayLap == null || hoaDon.getNgayLap().equals(ngayLap))
                .collect(Collectors.toList());

        // Cập nhật bảng với danh sách hóa đơn đã lọc
        tabTatCa.getItems().setAll(ketQuaLoc);
    }
    public void onResetClicked() {

        tfMaHoaDon.clear();
        tfTenKhachHang.clear();
        cbTrangThai.getSelectionModel().select("Trạng thái");
        dpNgayLap.setValue(null);

        textTenNV.clear();
        textsdtKH.clear();
        textTenKH.clear();
        textTenKH.clear();
        textMaKH.clear();
        textMaNV.clear();
        textsdtNV.clear();
        textTrangThaiNV.clear();

        tableCTHD.getItems().clear();
        //Cập nhật lại bảng
        HoaDonDAO hoaDonDAO = new HoaDonDAO();
        List<HoaDon> danhSachGoc = hoaDonDAO.getAllHoaDon();

        ObservableList<HoaDon> observableList = FXCollections.observableArrayList(danhSachGoc);
        tabTatCa.setItems(observableList);
    }
    private TrangThaiHoaDon convertStringToEnum(String trangThai) {
        if ("Đã thanh toán".equalsIgnoreCase(trangThai)) {
            return TrangThaiHoaDon.DA_THANH_TOAN;
        } else if ("Chưa thanh toán".equalsIgnoreCase(trangThai)) {
            return TrangThaiHoaDon.CHUA_THANH_TOAN;
        } else if ("Đã đặt".equalsIgnoreCase(trangThai)) {
            return TrangThaiHoaDon.DA_DAT;
        } else if ("Đã huỷ".equalsIgnoreCase(trangThai)) {
            return TrangThaiHoaDon.DA_HUY;
        }
        return null;
    }
    public double tinhTongTien() {
        ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO();

        // Lấy danh sách các chi tiết hóa đơn cho hóa đơn hiện tại
        List<ChiTietHoaDon> chiTietList = chiTietHoaDonDAO.getChiTietHoaDonByMaHoaDon(String.valueOf(this.colMaHoaDon));

        if (chiTietList == null || chiTietList.isEmpty()) {
            return 0.0;
        }
        return chiTietList.stream()
                .mapToDouble(chiTiet -> chiTiet.tinhTongCTHD())
                .sum();
    }





}
