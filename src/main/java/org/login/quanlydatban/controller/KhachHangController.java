package org.login.quanlydatban.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.poi.hpsf.Decimal;
import org.login.quanlydatban.dao.KhachHangDAO;
import org.login.quanlydatban.entity.KhachHang;

import javax.swing.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

public class KhachHangController {

    @FXML
    private Button BtnReset;

    @FXML
    private Button btnLamMoi;

    @FXML
    private Button btnSuaTT;

    @FXML
    private Button btnThem;

    @FXML
    private Button btnTimKiem;

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
        tableKhachHang.setOnMouseClicked(event -> {
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
        });

    }
    @FXML
    public void timKiemKhachHang() {
        String maKH = txtTimMaKH.getText().trim();
        String sdt = txtTimSDT.getText().trim();
        ObservableList<KhachHang> khachHangList = tableKhachHang.getItems();
        boolean timTay = false;

        for (KhachHang kh : khachHangList) {
            boolean trungKhop = (maKH.isEmpty() || kh.getMaKhachHang().equalsIgnoreCase(maKH)) &&
                    (sdt.isEmpty() || kh.getSdt().equalsIgnoreCase(sdt));
            if (trungKhop) {
                tableKhachHang.getSelectionModel().select(kh);
                tableKhachHang.scrollTo(kh);
                timTay = true;
                break;
            }
        }

        if (!timTay) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy khách hàng với thông tin đã nhập.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            tableKhachHang.getSelectionModel().clearSelection();
        }
    }

    @FXML
    public void suaKhachHang() {
        try {

            KhachHang khachHang = tableKhachHang.getSelectionModel().getSelectedItem();

            if (khachHang != null) {
                khachHang.setTenKhachHang(txtTenKH.getText().trim());
                khachHang.setSdt(txtSDT.getText().trim());
                khachHang.setEmail(txtEmail.getText().trim());
                khachHang.setDiaChi(txtDiaChi.getText().trim());
                khachHang.setCccd(txtCCCD.getText().trim());


                boolean kq = khachHangDAO.suaKhachHang(khachHang);

                if (kq) {

                    themDuLieuVaoBangKhachHang();
                    lamMoi();
                    JOptionPane.showMessageDialog(null, "Sửa thông tin khách hàng thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Sửa thông tin khách hàng thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn khách hàng để sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi sửa thông tin khách hàng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    public void themKhachHang() {
        if (!txtMaKH.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Vui lòng nhấn 'Làm mới' và nhập thông tin khách hàng để thêm mới.",
                    "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateInput()) {
            return;
        }

        try {
            KhachHang kh = khachHangDAO.layKhachHangDauTienTheoNam();
            String ma = null;
            int maKH = 0;
            if(kh == null) {
                ma = "KH" + LocalDate.now().getYear() + "0001";

            }
            else{
                maKH = Integer.parseInt(kh.getMaKhachHang().substring(kh.getMaKhachHang().length() - 4)) + 1;
                ma = kh.getMaKhachHang().substring(0,6) + df.format(maKH);
            }


            KhachHang khachHang = new KhachHang(
                    ma,
                    txtTenKH.getText().trim(),
                    txtSDT.getText().trim(),
                    txtCCCD.getText().trim(),
                    txtDiaChi.getText().trim(),
                    txtEmail.getText().trim(),
                    0
            );

            if (khachHangDAO.themKhachHang(khachHang)) {
                themDuLieuVaoBangKhachHang();
                lamMoi();
                JOptionPane.showMessageDialog(null, "Thêm khách hàng thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateInput() {
        String sdtRegex = "^(09|03|02|04)\\d{8}$";
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        String cccdRegex = "^\\d{3}[0-9][0-9]\\d{6}$";
        String tenRegex = "^[\\p{L} ]+$";
        String diaChiRegex = "^([A-Z0-9].*)?$\n";


        if (!txtSDT.getText().matches(sdtRegex)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!txtEmail.getText().equalsIgnoreCase("")&&!txtEmail.getText().matches(emailRegex)) {
            JOptionPane.showMessageDialog(null, "Email không hợp lệ.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!txtCCCD.getText().matches(cccdRegex)) {
            JOptionPane.showMessageDialog(null, "CCCD không hợp lệ.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!txtTenKH.getText().matches(tenRegex)) {
            JOptionPane.showMessageDialog(null, "Tên khách hàng không hợp lệ.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!txtDiaChi.getText().equalsIgnoreCase("")&&!txtDiaChi.getText().matches(diaChiRegex)) {
            JOptionPane.showMessageDialog(null, "Đianh chỉ nhập không hợp lệ.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }


    public void themDuLieuVaoBangKhachHang(){
        tableKhachHang.getItems().clear();
        List<Object[]> dsKhachHang = khachHangDAO.layDSKhachHang();


        for (Object[] row : dsKhachHang) {

            KhachHang khachHang = new KhachHang(
                    row[0].toString(), // MaKhachHang
                    row[1].toString(), // TenKhachHang
                    row[2].toString(), // Sdt
                    row[5].toString(), // Email
                    row[4].toString(), // DiaChi
                    row[3].toString(), // CCCD
                    Integer.parseInt(row[6].toString()) // DiemTichLuy
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

        themDuLieuVaoBangKhachHang();
    }
    @FXML
    public void lamMoiTimKiem() {
        txtTimMaKH.clear();
        txtTimSDT.clear();
    }





}
