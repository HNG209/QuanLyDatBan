package org.login.quanlydatban.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.login.service.CTHDService;
import org.login.service.HoaDonService;
import org.login.entity.*;
import org.login.entity.enums.TrangThaiHoaDon;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HoaDonController implements Initializable {

    @FXML
    private TextField textMaKH;
    @FXML
    private TextField textTenKH;
    @FXML
    private TextField textcccdKH;
    @FXML
    private TextField textMaNV;
    @FXML
    private TextField textTenNV;
    @FXML
    private TextField textcccdNV;
    @FXML
    private TextField textTrangThaiNV;

    @FXML
    private TextField tfMaHoaDon;
    @FXML
    private TextField tfTenKhachHang;
    @FXML
    private TextField tfMaban;
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
    private TableColumn<HoaDon, String> colChietkhau;
    @FXML
    private TableColumn<HoaDon, Double> colPhuThu;

    @FXML
    private TableView<HoaDon> tabTatCa;

    @FXML
    private Button btnXuatHD;

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

    private HoaDonService hoaDonService;
    private CTHDService cthdService;
    private MonAn monAn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String host = System.getenv("HOST_NAME");

        try {
            hoaDonService = (HoaDonService) Naming.lookup("rmi://"+ host + ":2909/hoaDonService");
            cthdService = (CTHDService) Naming.lookup("rmi://"+ host + ":2909/cthdService");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            throw new RuntimeException(e);
        }
        List<HoaDon> hoaDonList = null;
        try {
            hoaDonList = hoaDonService.getAllHoaDon();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        System.out.println(hoaDonList);
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
                String tongTien = String.valueOf(hoaDon.getTongTien());
                return new SimpleStringProperty(tongTien);
            });
            colPhuThu.setCellValueFactory(new PropertyValueFactory<>("phuThu"));
            colChietkhau.setCellValueFactory(new PropertyValueFactory<>("chietKhau"));
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

                    List<ChiTietHoaDon> chiTietList = null;
                    try {
                        chiTietList = cthdService.getChiTietHoaDonByMaHoaDon(maHoaDon);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }


                    ObservableList<ChiTietHoaDon> observableList1 = FXCollections.observableArrayList(chiTietList);
                    tableCTHD.setItems(observableList1);

                    HoaDon hoaDon = tabTatCa.getSelectionModel().getSelectedItem();
                    if (hoaDon != null) {
                        btnXuatHD.setDisable(false);
                        if (hoaDon.getTrangThaiHoaDon() != TrangThaiHoaDon.DA_THANH_TOAN)
                            btnXuatHD.setDisable(true);
                        NhanVien nhanVien = hoaDon.getNhanVien();
                        KhachHang khachHang = hoaDon.getKhachHang();

                        if (nhanVien != null) {
                            textMaNV.setText(nhanVien.getMaNhanVien());

                            textTenNV.setText(nhanVien.getTenNhanVien());
                            textcccdNV.setText(nhanVien.getCccd());

                            textTrangThaiNV.setText(nhanVien.getTrangThaiNhanVien().toString());
                        }

                        if (khachHang != null) {
                            textMaKH.setText(khachHang.getMaKhachHang());
                            textTenKH.setText(khachHang.getTenKhachHang());
                            textcccdKH.setText(khachHang.getCccd());
                        } else {
                            textMaKH.setText("");
                            textTenKH.setText("");
                            textcccdKH.setText("");
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
                "Chưa thanh toán"
        );

        // Đặt danh sách vào ComboBox
        cbTrangThai.getSelectionModel().select("Trạng thái");
        cbTrangThai.setItems(trangThaiList);


    }

    public void onTimKiemClicked() throws RemoteException {
        // Lấy thông tin từ các trường đầu vào
        String maHoaDon = tfMaHoaDon.getText().trim();
        String tenKhachHang = tfTenKhachHang.getText().trim();
        String maBan = tfMaban.getText().trim();
        String trangThai = cbTrangThai.getSelectionModel().getSelectedItem();

        LocalDate ngayLap = dpNgayLap.getValue();

        List<HoaDon> danhSachGoc = hoaDonService.getAllHoaDon();


        // Lọc danh sách dựa trên các tiêu chí
        List<HoaDon> ketQuaLoc = danhSachGoc.stream()
                .filter(hoaDon -> maHoaDon.isEmpty() || hoaDon.getMaHoaDon().contains(maHoaDon))
                .filter(hoaDon -> tenKhachHang.isEmpty() || (hoaDon.getKhachHang() != null && hoaDon.getKhachHang().getMaKhachHang().contains(tenKhachHang)))
                .filter(hoaDon -> maBan.isEmpty() || (hoaDon.getBan() != null && hoaDon.getBan().getMaBan().contains(maBan)))
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

    public void onResetClicked() throws RemoteException {

        tfMaHoaDon.clear();
        tfTenKhachHang.clear();
        cbTrangThai.getSelectionModel().select("Trạng thái");
        dpNgayLap.setValue(null);

        textTenNV.clear();
        textcccdKH.clear();
        textTenKH.clear();
        textTenKH.clear();
        textMaKH.clear();
        textMaNV.clear();
        textcccdNV.clear();
        textTrangThaiNV.clear();

        tableCTHD.getItems().clear();
        //Cập nhật lại bảng
        List<HoaDon> danhSachGoc = hoaDonService.getAllHoaDon();

        ObservableList<HoaDon> observableList = FXCollections.observableArrayList(danhSachGoc);
        tabTatCa.setItems(observableList);
    }

    private TrangThaiHoaDon convertStringToEnum(String trangThai) {
        if ("Đã thanh toán".equalsIgnoreCase(trangThai)) {
            return TrangThaiHoaDon.DA_THANH_TOAN;
        } else if ("Chưa thanh toán".equalsIgnoreCase(trangThai)) {
            return TrangThaiHoaDon.CHUA_THANH_TOAN;
        }
        return null;
    }

    public double tinhTongTien() throws RemoteException {

        // Lấy danh sách các chi tiết hóa đơn cho hóa đơn hiện tại
        List<ChiTietHoaDon> chiTietList = cthdService.getChiTietHoaDonByMaHoaDon(String.valueOf(this.colMaHoaDon));

        if (chiTietList == null || chiTietList.isEmpty()) {
            return 0.0;
        }
        return chiTietList.stream()
                .mapToDouble(ChiTietHoaDon::tinhTongCTHD)
                .sum();
    }

    public void XuatHD() throws IOException {
        HoaDon selectedHoaDon = tabTatCa.getSelectionModel().getSelectedItem();
        if (selectedHoaDon == null) {
            showAlert("Vui lòng chọn 1 hoá đơn để xuất!");
            return;
        }
        if (selectedHoaDon.getTrangThaiHoaDon() == TrangThaiHoaDon.DA_THANH_TOAN) {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/login/quanlydatban/views/TrangXuatHoaDon.fxml")));
            AnchorPane pane = loader.load();
            XuatHoaDonController controller = loader.getController();
            controller.setHoaDon(selectedHoaDon);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane));
            stage.showAndWait();
        } else {
            showAlert("Hóa đơn chưa được thanh toán!");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
