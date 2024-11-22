package org.login.quanlydatban.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.login.quanlydatban.dao.HoaDonDAO;
import org.login.quanlydatban.dao.KhachHangDAO;
import org.login.quanlydatban.dao.LichDatDAO;
import org.login.quanlydatban.entity.Ban;
import org.login.quanlydatban.entity.HoaDon;
import org.login.quanlydatban.entity.KhachHang;
import org.login.quanlydatban.entity.LichDat;
import org.login.quanlydatban.entity.enums.KhuVuc;
import org.login.quanlydatban.entity.enums.LoaiBan;
import org.login.quanlydatban.entity.enums.LoaiTiec;
import org.login.quanlydatban.entity.enums.TrangThaiHoaDon;
import org.login.quanlydatban.notification.Notification;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class DatLichController implements Initializable {

    @FXML
    private Tab tabDatLich;

    @FXML
    private ComboBox<LoaiBan> cbLoaiBan;

    @FXML
    private ComboBox<LoaiTiec> cbLoaiTiec;

    @FXML
    private ComboBox<LoaiTiec> cbChonLoaiTiec;

    @FXML
    private ComboBox<Integer> cbGio;

    @FXML
    private ComboBox<Integer> cbPhut;

    @FXML
    private DatePicker tgNhanBan;

    private HoaDonDAO hoaDonDAO;

    private LichDatDAO lichDatDAO;

    private Ban ban;

    @FXML
    private ComboBox<KhuVuc> khuVuc;

    @FXML
    private ComboBox<LoaiBan> loaiBan1;

    @FXML
    private TableView<Object[]> bookingTable;

    @FXML
    private TableColumn<Object[], String> loaiBanCol;

    @FXML
    private TableColumn<Object[], String> maLichDatCol;

    @FXML
    private TableColumn<Object[], String> ngayDatCol;

    @FXML
    private TableColumn<Object[], String> ngayNhanBanCol;

    @FXML
    private TableColumn<Object[], Integer> soLuongNguoiCol;

    @FXML
    private TextField tenKhachHang;

    @FXML
    private TextField sdt;

    @FXML
    private TextField coc;

    @FXML
    private TextField soLuongNguoi;

    private String prevSdt;

    private KhachHangDAO khachHangDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbLoaiBan.getItems().addAll(LoaiBan.values());
        cbLoaiTiec.getItems().addAll(LoaiTiec.values());
        cbChonLoaiTiec.getItems().addAll(LoaiTiec.values());
        khuVuc.getItems().addAll(KhuVuc.values());
        loaiBan1.getItems().addAll(LoaiBan.values());

        hoaDonDAO = new HoaDonDAO();
        lichDatDAO = new LichDatDAO();
        khachHangDAO = new KhachHangDAO();

        maLichDatCol.setCellValueFactory(data -> new SimpleStringProperty((String)data.getValue()[0]));
        ngayDatCol.setCellValueFactory(data -> new SimpleStringProperty((String)data.getValue()[1]));
        ngayNhanBanCol.setCellValueFactory(data -> new SimpleStringProperty((String)data.getValue()[2]));
        loaiBanCol.setCellValueFactory(data -> new SimpleStringProperty((String)data.getValue()[3]));
        soLuongNguoiCol.setCellValueFactory(data -> new SimpleIntegerProperty((Integer) data.getValue()[4]).asObject());

        for(int i = 1; i <= 24; i++)
            cbGio.getItems().add(i);

        for (int i = 5; i <= 55; i += 5)
            cbPhut.getItems().add(i);

        tenKhachHang.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // If newValue is false, the TextField has lost focus
                if(Notification.xacNhan("Lưu khách hàng này?")){
                    KhachHang khachHang = new KhachHang();
                    khachHang.setSdt(sdt.getText());
                    khachHang.setTenKhachHang(tenKhachHang.getText());

                    khachHangDAO.themKhachHang(khachHang);

//                    hoaDonDAO.updateHoaDon(hoaDon);
                    tenKhachHang.setEditable(false);
                }
            }
        });

        sdt.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // If newValue is false, the TextField has lost focus
                if(sdt.getText().length() < 10)
                    sdt.setText(prevSdt);
            }
        });
    }

    public void loadBang(Object[] objects) {
        bookingTable.getItems().add(objects);
    }

    public void loadDatLich() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangDatLich.fxml"));
        TabPane pane = loader.load();

        TrangChuController.getBorderPaneStatic().setCenter(pane);
    }

    @FXML
    void nhanBan(ActionEvent event) {
        try {
            LocalDate date = tgNhanBan.getValue();
            HoaDon hoaDon = new HoaDon();

            hoaDon.setNgayLap(LocalDate.now());
            hoaDon.setTrangThaiHoaDon(TrangThaiHoaDon.DA_DAT);
            hoaDon.setMaHoaDon(date);
            hoaDonDAO.lapHoaDon(hoaDon);
        }
        catch (Exception e) {
            Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    @FXML
    void chonMon(ActionEvent event) throws IOException {
        if(ban != null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangDatMon.fxml"));
            AnchorPane pane = loader.load();
            DatMonController controller = loader.getController();
            controller.setDatLichController(this);

            tabDatLich.setContent(pane);
        }
        else Notification.thongBao("Vui lòng chọn bàn", Alert.AlertType.WARNING);
    }

    @FXML
    void datLich(ActionEvent event) {
        try {
            LichDat lichDat = new LichDat();

            lichDat.setThoiGianDat(LocalDateTime.now());

            if(tgNhanBan.getValue() != null){
                if(cbGio.getValue() != null && cbPhut.getValue() != null)
                    lichDat.setThoiGianNhanBan(LocalDateTime.of(tgNhanBan.getValue(), LocalTime.of(cbGio.getValue(), cbPhut.getValue())));
                else throw new IllegalArgumentException("Vui lòng chọn giờ");
            }
            else throw new IllegalArgumentException("Vui lòng chọn thời gian nhận bàn");

            LocalDate date = tgNhanBan.getValue();
            HoaDon hoaDon = new HoaDon();
            hoaDon.setNgayLap(LocalDate.now());
            hoaDon.setTrangThaiHoaDon(TrangThaiHoaDon.DA_DAT);
            hoaDon.setMaHoaDon(date);
            hoaDonDAO.lapHoaDon(hoaDon);
            lichDat.setHoaDon(hoaDon);

            lichDat.setNhanVien(TrangChuController.getTaiKhoan().getNhanVien());

            if(cbChonLoaiTiec.getValue() != null)
                lichDat.setLoaiTiec(cbChonLoaiTiec.getValue());
            else throw new IllegalArgumentException("Vui lòng chọn loại tiệc");

            if(!prevSdt.isEmpty())
                lichDat.setKhachHang(khachHangDAO.getKHBySDT(prevSdt));
            else throw new IllegalArgumentException("Vui lòng nhập số điện thoại của khách hàng");

            if(!soLuongNguoi.getText().isEmpty())
                if(Integer.parseInt(soLuongNguoi.getText()) > 0)
                    lichDat.setSoLuongNguoi(Integer.parseInt(soLuongNguoi.getText()));
                else throw new IllegalArgumentException("Số lượng người > 0");
            else throw new IllegalArgumentException("Vui lòng nhập số lượng người");

            lichDatDAO.taoLichDat(lichDat);
        }
        catch (Exception e){
            Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
        }
    }
    @FXML
    void resetFilterBan(MouseEvent event) {

    }

    @FXML
    void resetFilterLich(MouseEvent event) {

    }

    @FXML
    void sdtEnter(KeyEvent event) {
        if(sdt.getText().length() == 10) {
            try {
                KhachHang khachHang = khachHangDAO.getKHBySDT(sdt.getText());
                tenKhachHang.setText(khachHang.getTenKhachHang());
                prevSdt = sdt.getText();
            }
            catch (NoResultException e) {
                if(Notification.xacNhan("Số điện thoại mới, bạn có muốn tạo khách hàng này?")){
                    tenKhachHang.requestFocus();
                    tenKhachHang.setEditable(true);
                }
                else sdt.setText(prevSdt);
            }
        }
        else tenKhachHang.setEditable(false);
    }
}
