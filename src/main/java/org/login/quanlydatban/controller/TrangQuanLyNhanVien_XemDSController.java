package org.login.quanlydatban.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.login.quanlydatban.dao.NhanVienDAO;
import org.login.quanlydatban.dao.TaiKhoanDAO;
import org.login.quanlydatban.entity.NhanVien;
import org.login.quanlydatban.entity.TaiKhoan;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TrangQuanLyNhanVien_XemDSController implements Initializable {
    @FXML
    private Button btnthem;
    @FXML
    private ComboBox<String> cbxTimKiem;
    @FXML
    private Label tenNhanVien23;
    // bien ten nhan vien
    private String nhanvien;
    private NhanVienDAO nhanVienDAO;
    @FXML
    private TableView<NhanVien> tableNhanVien;

    @FXML
    private TableColumn<NhanVien, String> nhanVienID; // 0 Cột ID
    @FXML
    private TableColumn<NhanVien, String> tenNhanVien; // 1 Cột Họ Tên
    @FXML
    private TableColumn<NhanVien, String> diaChi; // 7 cột địa chỉ
    @FXML
    private TableColumn<NhanVien, String> gioiTinh; // 5 Cột giới tính
    @FXML
    private TableColumn<NhanVien, String> trangThai; // 6 Cột Ngày Sinh
    @FXML
    private TableColumn<NhanVien, String> soDienThoai; // 4 Cột sdt

    // them nhan vien
    public void themNhanVien(){
        btnthem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    System.out.println("Nhan nut them");
                    // Tải giao diện từ file FXML
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/QuanLyNhanVien.fxml"));
                    Parent newWindow = loader.load();
                    TrangQuanLyNhanVienController nv = loader.getController();
                    System.out.println(getNhanvien().toString());
                    nv.setTenNhanVien(getNhanvien().toString());
                    // Tạo một cửa sổ mới
                    Stage stage = new Stage();
                    stage.setScene(new Scene(newWindow));
                    stage.setTitle("Quản Lý Nhân Viên");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace(); // Xử lý ngoại lệ nếu có lỗi khi tải FXML
                }
            }
        });
    }

    public String getNhanvien() {
        return nhanvien;
    }
    // su dung de lay ten nhan vien
    public void setNhanvien(String nhanvien) {
        this.nhanvien = nhanvien;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nhanVienDAO = new NhanVienDAO();
        List<NhanVien>  listNhanVien = nhanVienDAO.getAllTaiKhoan();
        tableNhanVien.setPrefHeight(60);
        // Thiết lập các cột cho TableView
        try {
            nhanVienID.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
            tenNhanVien.setCellValueFactory(new PropertyValueFactory<>("tenNhanVien"));
            diaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
            gioiTinh.setCellValueFactory(cellData -> {
                boolean isNam = cellData.getValue().isGioiTinh();
                return new SimpleStringProperty(!isNam ? "Nam" : "Nữ");
            });

            trangThai.setCellValueFactory(new PropertyValueFactory<>("trangThaiNhanVien"));
            soDienThoai.setCellValueFactory(new PropertyValueFactory<>("sdt"));

            // Chuyển danh sách nhân viên thành ObservableList và thêm vào TableView
            ObservableList<NhanVien> observableList = FXCollections.observableArrayList(listNhanVien);
            tableNhanVien.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace(); // In ra thông tin lỗi
        }
    }
}
