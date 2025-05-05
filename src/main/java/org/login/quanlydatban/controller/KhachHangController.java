package org.login.quanlydatban.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.login.quanlydatban.utilities.RMIServiceUtils;
import org.login.service.KhachHangService;
import org.login.entity.KhachHang;
import org.login.quanlydatban.notification.Notification;

import java.rmi.Naming;
import java.rmi.RemoteException;
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
    private final DecimalFormat df = new DecimalFormat("0000");
    private KhachHangService khachHangService;

    @FXML
    public void initialize() {
        try {
            khachHangService = RMIServiceUtils.useKhachHangService();

            tableKhachHang.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            List<Object[]> dsKhachHang = khachHangService.layDSKhachHang();

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
        } catch (Exception e) {
            Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
        }
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
        if (maKH.isEmpty() && sdt.isEmpty()) {
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
                    if (khachHangService.timKhachHangTheoSDT(sdt) == null) {
                        khachHang.setSdt(sdt);
                    } else {
                        throw new IllegalArgumentException("Số điện thoại đã đăng kí thành viên");
                    }
                }

                khachHang.setEmail(txtEmail.getText() == null ? "" : txtEmail.getText().trim());
                khachHang.setDiaChi(txtDiaChi.getText() == null ? "" : txtDiaChi.getText().trim());
                khachHang.setCccd(txtCCCD.getText() == null ? "" : txtCCCD.getText().trim());


                boolean kq = khachHangService.suaKhachHang(khachHang);

                if (kq) {

                    themDuLieuVaoBangKhachHang();
                    lamMoi();
                    Notification.thongBao("Sửa thông tin khách hàng thành công", Alert.AlertType.INFORMATION);
                } else {
                    throw new IllegalArgumentException("Sửa thông tin khách hàng thất bại");
                }
            } else {
                throw new IllegalArgumentException("Vui lòng chọn khách hàng để sửa");
            }
        } catch (Exception e) {
            Notification.thongBao(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void themKhachHang() {
        if (!tableKhachHang.getSelectionModel().isEmpty()) {
            Notification.thongBao("Vui lòng nhấn 'Làm mới' và nhập thông tin khách hàng để thêm mới", Alert.AlertType.INFORMATION);
            return;
        }

        try {
            if (!validateInput()) {
                return;
            }
            KhachHang khachHang = new KhachHang(
                    null,
                    txtTenKH.getText().trim(),
                    txtSDT.getText().trim(),
                    txtCCCD.getText().trim(),
                    txtDiaChi.getText().trim(),
                    txtEmail.getText().trim(),
                    0
            );
            KhachHang kh = khachHangService.themKhachHang(khachHang);

            if (kh != null) {
                themDuLieuVaoBangKhachHang();
                lamMoi();
                Notification.thongBao("Thêm khách hàng thành công", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            Notification.thongBao(e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private boolean validateInput() throws RemoteException {
        String sdtRegex = "^(09|03|02|04)\\d{8}$";
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        String cccdRegex = "^\\d{3}[0-9][0-9]\\d{7}$";
        String tenRegex = "^[A-Z][a-zA-Z]*( [A-Z][a-zA-Z]*)*$";
        String diaChiRegex = "^[A-Z0-9][a-zA-Z0-9/]*( [A-Z0-9][a-zA-Z0-9/]*)*$";
        if (txtTenKH.getText().isEmpty() && txtSDT.getText().isEmpty() && txtCCCD.getText().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập thông tin khách hàng");
        }

        if (!txtTenKH.getText().matches(tenRegex)) {
            throw new IllegalArgumentException("Tên khách hàng không hợp lệ");
        }
        if (!txtSDT.getText().matches(sdtRegex)) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        }
        if (khachHangService.timKhachHangTheoSDT(txtSDT.getText()) != null) {
            throw new IllegalArgumentException("Số điện thoại đã đăng kí thành viên");
        }
        if (!txtEmail.getText().equalsIgnoreCase("") && !txtEmail.getText().matches(emailRegex)) {
            throw new IllegalArgumentException("Email không hợp lệ");
        }

        if (!txtCCCD.getText().equalsIgnoreCase("") && !txtCCCD.getText().matches(cccdRegex)) {
            throw new IllegalArgumentException("CCCD không hợp lệ");
        }
        if (!txtDiaChi.getText().equalsIgnoreCase("") && !txtDiaChi.getText().matches(diaChiRegex)) {
            throw new IllegalArgumentException("Địa chỉ nhập không hợp lệ");
        }
        return true;
    }


    public void themDuLieuVaoBangKhachHang() throws RemoteException {
        tableKhachHang.getItems().clear();
        List<Object[]> dsKhachHang = khachHangService.layDSKhachHang();


        for (Object[] row : dsKhachHang) {
            KhachHang khachHang = new KhachHang(
                    row[0] == null ? null : row[0].toString(),
                    row[1] == null ? null : row[1].toString(),
                    row[2] == null ? null : row[2].toString(),
                    row[3] == null ? null : row[3].toString(),
                    row[4] == null ? null : row[4].toString(),
                    row[5] == null ? null : row[5].toString(),
                    Integer.parseInt(row[6].toString())
            );

            tableKhachHang.getItems().add(khachHang);
        }
    }

    @FXML
    public void lamMoi() throws RemoteException {
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
    public void lamMoiTimKiem() throws RemoteException {
        txtTimMaKH.clear();
        txtTimSDT.clear();
        lamMoi();
    }
}
