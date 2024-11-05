package org.login.quanlydatban.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.login.quanlydatban.dao.HoaDonDAO;
import org.login.quanlydatban.entity.HoaDon;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class HoaDonController implements Initializable {

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
    private TableView<HoaDon> tableHoaDon;

    private HoaDonDAO xemHoaDonDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        xemHoaDonDAO = new HoaDonDAO();
        List<HoaDon> hoaDonList = xemHoaDonDAO.getAllHoaDon();


       try {
            colMaHoaDon.setCellValueFactory(new PropertyValueFactory<>("maHoaDon"));
            colNgayLap.setCellValueFactory(new PropertyValueFactory<>("ngayLap"));
            colMaKhachHang.setCellValueFactory(new PropertyValueFactory<>("maKhachHang"));
            colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThaiHoaDon"));
            colMaBan.setCellValueFactory(new PropertyValueFactory<>("maBan"));
            colMaNhanVien.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
            colTongTien.setCellValueFactory(new PropertyValueFactory<>("tongTien"));
            colPhuThu.setCellValueFactory(new PropertyValueFactory<>("phuThu"));

            ObservableList<HoaDon> observableList = FXCollections.observableArrayList(hoaDonList);
            tableHoaDon.setItems(observableList);

        } catch (Exception e) {
            e.printStackTrace();
       }


    }
}
