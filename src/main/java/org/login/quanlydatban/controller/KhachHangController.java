package org.login.quanlydatban.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.login.quanlydatban.dao.KhachHangDAO;
import org.login.quanlydatban.entity.KhachHang;
import org.login.quanlydatban.notification.Notification;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.List;

public class KhachHangController {

    @FXML
    private TableColumn<?, ?> colCCCD;

    @FXML
    private TableColumn<?, ?> colDTL;

    @FXML
    private TableColumn<?, ?> colDiaChi;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colSDT;

    @FXML
    private TableColumn<?, ?> colTenKH;

    @FXML
    private TableColumn<?, ?> colmaKH;

    @FXML
    private TableView<KhachHang> tableKhachHang;

    @FXML
    private TextField txtCCCD;

    @FXML
    private TextField txtDTL;

    @FXML
    private TextField txtDiaChi;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtMaKH;

    @FXML
    private TextField txtSDT;
    @FXML
    private TextField txtTimMaKH;

    @FXML
    private TextField txtTimSDT;

    @FXML
    private TextField txtTenKH;
    private DecimalFormat df = new DecimalFormat("0000");
    private KhachHangDAO khachHangDAO = new KhachHangDAO();
    @FXML
    public void initialize() {
        tableKhachHang.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        List<Object[]> dsKhachHang = khachHangDAO.layDSKhachHang();

        ObservableList<KhachHang> data = FXCollections.observableArrayList();

        colmaKH.setCellValueFactory(new PropertyValueFactory<>("maKhachHang"));
        colTenKH.setCellValueFactory(new PropertyValueFactory<>("tenKhachHang"));
        colSDT.setCellValueFactory(new PropertyValueFactory<>("sdt"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        colCCCD.setCellValueFactory(new PropertyValueFactory<>("cccd"));
        colDTL.setCellValueFactory(new PropertyValueFactory<>("diemTichLuy"));
        tableKhachHang.setItems(data);
        themDuLieuVaoBangKhachHang();
    }
    @FXML
    public void onCustomerSelected() {

        KhachHang selectedCustomer = tableKhachHang.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            txtMaKH.setText(selectedCustomer.getMaKhachHang());
            txtTenKH.setText(selectedCustomer.getTenKhachHang());
            txtSDT.setText(selectedCustomer.getSdt());
            txtEmail.setText(selectedCustomer.getEmail());
            txtDiaChi.setText(selectedCustomer.getDiaChi());
            txtCCCD.setText(selectedCustomer.getCccd());
            txtDTL.setText(String.valueOf(selectedCustomer.getDiemTichLuy()));
        }
    }

    @FXML
    public void timKiemKhachHang() {
        String maKH = txtTimMaKH.getText().trim();
        String sdt = txtTimSDT.getText().trim();
        ObservableList<KhachHang> khachHangList = tableKhachHang.getItems();
        boolean timThay = false;
        if(maKH.isEmpty() && sdt.isEmpty()) {
            Notification.thongBao("Vui lòng nhập thông tin khách hàng cần tìm", Alert.AlertType.INFORMATION);
            return;
        }
        for (KhachHang kh : khachHangList) {
            boolean trungKhop = (maKH.isEmpty() || kh.getMaKhachHang().equalsIgnoreCase(maKH)) &&
                    (sdt.isEmpty() || kh.getSdt().equalsIgnoreCase(sdt));
            if (trungKhop) {
                tableKhachHang.getSelectionModel().select(kh);
                tableKhachHang.scrollTo(kh);
                onCustomerSelected();
                timThay = true;
                break;
            }

        }

        if (!timThay) {
            Notification.thongBao("Không tìm thấy khách hàng với thông tin đã nhập", Alert.AlertType.INFORMATION);
            tableKhachHang.getSelectionModel().clearSelection();
        }
    }

    @FXML
    public void suaKhachHang() {
        try {

            KhachHang khachHang = tableKhachHang.getSelectionModel().getSelectedItem();

            if (khachHang != null) {
                String sdt = txtSDT.getText().trim();
                String currentSDT = khachHang.getSdt();
                khachHang.setTenKhachHang(txtTenKH.getText().trim());
                if (!sdt.equals(currentSDT)) {
                    if (khachHangDAO.timKhachHangTheoSDT(sdt) == null) {
                        khachHang.setSdt(sdt);
                    } else {
                        Notification.thongBao("Số điện thoại đã đăng kí thành viên", Alert.AlertType.INFORMATION);
                        return;
                    }
                }

                khachHang.setEmail(txtEmail.getText() == null ? "" : txtEmail.getText().trim());
                khachHang.setDiaChi(txtDiaChi.getText() == null ? "" : txtDiaChi.getText().trim());
                khachHang.setCccd(txtCCCD.getText() == null ? "" : txtCCCD.getText().trim());



                boolean kq = khachHangDAO.suaKhachHang(khachHang);

                if (kq) {

                    themDuLieuVaoBangKhachHang();
                    lamMoi();
                    Notification.thongBao("Sửa thông tin khách hàng thành công", Alert.AlertType.INFORMATION);
                } else {
                    Notification.thongBao("Sửa thông tin khách hàng thất bại", Alert.AlertType.INFORMATION);
                }
            } else {
                Notification.thongBao("Vui lòng chọn khách hàng để sửa", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            Notification.thongBao("Có lỗi xảy ra khi sửa thông tin khách hàng", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void themKhachHang() {
        if (!tableKhachHang.getSelectionModel().isEmpty()) {
            Notification.thongBao("Vui lòng nhấn 'Làm mới' và nhập thông tin khách hàng để thêm mới", Alert.AlertType.INFORMATION);
            return;
        }

        if (!validateInput()) {
            return;
        }
        try {
            KhachHang khachHang = new KhachHang(
                    null,
                    txtTenKH.getText().trim(),
                    txtSDT.getText().trim(),
                    txtCCCD.getText().trim(),
                    txtDiaChi.getText().trim(),
                    txtEmail.getText().trim(),
                    0
            );
            KhachHang kh = khachHangDAO.themKhachHang(khachHang);

            if (kh != null) {
                themDuLieuVaoBangKhachHang();
                lamMoi();
                Notification.thongBao("Thêm khách hàng thành công", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            Notification.thongBao("Có lỗi xảy ra khi thêm khách hàng", Alert.AlertType.ERROR);
        }
    }


    private boolean validateInput() {
        String sdtRegex = "^(09|03|02|04)\\d{8}$";
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        String cccdRegex = "^\\d{3}[0-9][0-9]\\d{7}$";
        String tenRegex = "^[A-Z][a-zA-Z]*( [A-Z][a-zA-Z]*)*$";
        String diaChiRegex = "^[A-Z0-9][a-zA-Z0-9/]*( [A-Z0-9][a-zA-Z0-9/]*)*$";
        if(txtTenKH.getText().isEmpty() && txtSDT.getText().isEmpty() && txtCCCD.getText().isEmpty()) {
            Notification.thongBao("Vui lòng nhập thông tin khách hàng", Alert.AlertType.INFORMATION);
            return false;
        }

        if (!txtTenKH.getText().matches(tenRegex)) {
            Notification.thongBao("Tên khách hàng không hợp lệ", Alert.AlertType.ERROR);
            return false;
        }
        if (!txtSDT.getText().matches(sdtRegex)) {
            Notification.thongBao("Số điện thoại không hợp lệ", Alert.AlertType.ERROR);
            return false;
        }
        if(khachHangDAO.timKhachHangTheoSDT(txtSDT.getText()) != null) {
            Notification.thongBao("Số điện thoại đã đăng kí thành viên", Alert.AlertType.INFORMATION);
            return false;
        }
        if (!txtEmail.getText().equalsIgnoreCase("")&&!txtEmail.getText().matches(emailRegex)) {
            Notification.thongBao("Email không hợp lệ", Alert.AlertType.ERROR);
            return false;
        }

        if (!txtCCCD.getText().equalsIgnoreCase("")&&!txtCCCD.getText().matches(cccdRegex)) {
            Notification.thongBao("CCCD không hợp lệ", Alert.AlertType.ERROR);
            return false;
        }
        if (!txtDiaChi.getText().equalsIgnoreCase("")&&!txtDiaChi.getText().matches(diaChiRegex)) {
            Notification.thongBao("Địa chỉ nhập không hợp lệ", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }


    public void themDuLieuVaoBangKhachHang(){
        tableKhachHang.getItems().clear();
        List<Object[]> dsKhachHang = khachHangDAO.layDSKhachHang();


        for (Object[] row : dsKhachHang) {

            KhachHang khachHang = new KhachHang(
                    row[0].toString(),
                    row[1].toString(),
                    row[2].toString(),
                    row[3] == null ? null :row[3].toString(),
                    row[4] == null ? null :row[4].toString(),
                    row[5] == null ? null :row[5].toString(),
                    Integer.parseInt(row[6].toString())
            );

            tableKhachHang.getItems().add(khachHang);
        }
    }
    @FXML
    public void lamMoi() {
        txtMaKH.clear();
        txtTenKH.clear();
        txtSDT.clear();
        txtEmail.clear();
        txtDiaChi.clear();
        txtCCCD.clear();
        txtDTL.clear();
        tableKhachHang.getSelectionModel().clearSelection();

        themDuLieuVaoBangKhachHang();
    }
    @FXML
    public void lamMoiTimKiem() {
        txtTimMaKH.clear();
        txtTimSDT.clear();
        lamMoi();
    }





}
