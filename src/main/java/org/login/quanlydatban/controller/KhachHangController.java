package org.login.quanlydatban.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.login.quanlydatban.dao.KhachHangDAO;
import org.login.quanlydatban.entity.KhachHang;

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
    private KhachHangDAO khachHangDAO = new KhachHangDAO();
    @FXML
    public void initialize() {
        tableKhachHang.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        List<Object[]> dsKhachHang = khachHangDAO.layDSKhachHang();
        themDuLieuVaoBangKhachHang();
        ObservableList<KhachHang> data = FXCollections.observableArrayList();

        colmaKH.setCellValueFactory(new PropertyValueFactory<>("maKhachHang"));
        colTenKH.setCellValueFactory(new PropertyValueFactory<>("tenKhachHang"));
        colSDT.setCellValueFactory(new PropertyValueFactory<>("sdt"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        colCCCD.setCellValueFactory(new PropertyValueFactory<>("cccd"));
        colDTL.setCellValueFactory(new PropertyValueFactory<>("diemTichLuy"));

        tableKhachHang.setItems(data);

    }
    @FXML
    public void timKiemKhachHang() {
        String maKH = txtMaKH.getText().trim();
        String sdt = txtSDT.getText().trim();
        ObservableList<KhachHang> khachHangList = tableKhachHang.getItems();
        for (KhachHang kh : khachHangList) {
            boolean found = (maKH.isEmpty() || kh.getMaKhachHang().equalsIgnoreCase(maKH)) &&
                    (sdt.isEmpty() || kh.getSdt().equalsIgnoreCase(sdt));
            if (found) {
                tableKhachHang.getSelectionModel().select(kh);
                tableKhachHang.scrollTo(kh);  // Cuộn đến dòng được chọn
                return;
            }
        }

        tableKhachHang.getSelectionModel().clearSelection();
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
                khachHang.setDiemTichLuy(Integer.parseInt(txtDTL.getText().trim()));

                if (khachHangDAO.suaKhachHang(khachHang)) {
                    themDuLieuVaoBangKhachHang();
                    lamMoi();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void themKhachHang() {
        try {
            KhachHang khachHang = new KhachHang(
                    txtMaKH.getText().trim(),
                    txtTenKH.getText().trim(),
                    txtSDT.getText().trim(),
                    txtEmail.getText().trim(),
                    txtDiaChi.getText().trim(),
                    txtCCCD.getText().trim(),
                    Integer.parseInt(txtDTL.getText().trim())
            );

            if (khachHangDAO.themKhachHang(khachHang)) {
                themDuLieuVaoBangKhachHang();
                lamMoi();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void themDuLieuVaoBangKhachHang(){
        List<Object[]> dsKhachHang = khachHangDAO.layDSKhachHang();


        for (Object[] row : dsKhachHang) {

            KhachHang khachHang = new KhachHang(
                    row[0].toString(), // MaKhachHang
                    row[1].toString(), // TenKhachHang
                    row[2].toString(), // Sdt
                    row[3].toString(), // Email
                    row[4].toString(), // DiaChi
                    row[5].toString(), // CCCD
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




}
