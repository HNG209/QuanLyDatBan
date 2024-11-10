package org.login.quanlydatban.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.login.quanlydatban.dao.BanDAO;

import org.login.quanlydatban.entity.*;
import org.login.quanlydatban.entity.enums.KhuVuc;
import org.login.quanlydatban.entity.enums.LoaiBan;
import org.login.quanlydatban.entity.enums.TrangThaiBan;

import java.net.URL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


public class QuanLyBanController implements Initializable {

    @FXML
    private TextField textALL;
    @FXML
    private Label textSLBanTrong;
    @FXML
    private Label textSLDangPhucVu;
    @FXML
    private Label textSLDaDat;
    @FXML
    private Label textSLTamNgung;
    @FXML
    private Label textTatCa1;
    @FXML
    private Label textTatCa2;
    @FXML
    private Label textTatCa3;
    @FXML
    private Label textTatCa4;

    @FXML
    private TextField textMaBan;
    @FXML
    private ComboBox<KhuVuc> textKhuVuc;
    @FXML
    private ComboBox<LoaiBan> textLoaiBan;
    @FXML
    private CheckBox checkTamNgungPV;

    @FXML
    private ComboBox<KhuVuc> locKhuVuc;
    @FXML
    private ComboBox<LoaiBan> locLoaiBan;
    @FXML
    private ComboBox<TrangThaiBan> locTrangThai;

    @FXML
    private TableColumn<Ban, String> colMaBan;
    @FXML
    private TableColumn<Ban, String> colKhuVuc;
    @FXML
    private TableColumn<Ban, String> colLoaiBan;
    @FXML
    private TableColumn<Ban, String> colTrangThai;
    @FXML
    private TableColumn<Ban, String> colSTT;


    @FXML
    private TableView<Ban> tableBan;




    private BanDAO banDAO;
    private Map<KhuVuc, Integer> khuVucCounter = new HashMap<>();
    private boolean isEditing = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        banDAO = new BanDAO();
        List<Ban> banList = banDAO.readAll();
        ObservableList<Ban> banObservableList = FXCollections.observableArrayList(banList);
        try {
            colSTT.setCellValueFactory(cellData ->
                    new SimpleStringProperty(String.valueOf(tableBan.getItems().indexOf(cellData.getValue()) + 1))
            );

            colMaBan.setCellValueFactory(new PropertyValueFactory<>("maBan"));
            colKhuVuc.setCellValueFactory(new PropertyValueFactory<>("khuVuc"));
            colLoaiBan.setCellValueFactory(new PropertyValueFactory<>("loaiBan"));
            colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThaiBan"));

            tableBan.setItems(banObservableList);
            demTongBan(banList);
            demSoLuongTheoTrangThai(banList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        textKhuVuc.getItems().setAll(KhuVuc.values());
        textLoaiBan.getItems().setAll(LoaiBan.values());

        locKhuVuc.getItems().setAll(KhuVuc.values());
        locLoaiBan.getItems().setAll(LoaiBan.values());
        locTrangThai.getItems().setAll(TrangThaiBan.values());
        checkTamNgungPV.setDisable(true);
        textMaBan.setDisable(true);

        tableBan.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            hienThiThongTinBan();

        });
        textKhuVuc.getSelectionModel().selectedItemProperty().addListener((obs, oldKhuVuc, newKhuVuc) -> {
            updateMaBan();
        });
        textLoaiBan.getSelectionModel().selectedItemProperty().addListener((obs, oldLoaiBan, newLoaiBan) -> {
            updateMaBan();
        });

    }
    private void demTongBan(List<Ban> banList) {
        int tongBan = banList.size();
        textALL.setText(String.valueOf(tongBan));
        textTatCa1.setText(String.valueOf(tongBan));
        textTatCa2.setText(String.valueOf(tongBan));
        textTatCa3.setText(String.valueOf(tongBan));
        textTatCa4.setText(String.valueOf(tongBan));
    }
    private void demSoLuongTheoTrangThai(List<Ban> banList) {

        long soBanTrong = banList.stream().filter(ban -> "BÀN TRỐNG".equals(ban.getTrangThaiBan().toString())).count();
        long soBanDangPhucVu = banList.stream().filter(ban -> "ĐANG PHỤC VỤ".equals(ban.getTrangThaiBan().toString())).count();
        long soBanDaDat = banList.stream().filter(ban -> "ĐÃ ĐẶT".equals(ban.getTrangThaiBan().toString())).count();
        long soBanTamNgung = banList.stream().filter(ban -> "TẠM NGƯNG PHỤC VỤ".equals(ban.getTrangThaiBan().toString())).count();

        textSLBanTrong.setText(String.valueOf(soBanTrong));
        textSLDangPhucVu.setText(String.valueOf(soBanDangPhucVu));
        textSLDaDat.setText(String.valueOf(soBanDaDat));
        textSLTamNgung.setText(String.valueOf(soBanTamNgung));


    }
    @FXML
    private void handleThemBan() {
        String maBan = textMaBan.getText();
        KhuVuc khuVuc = textKhuVuc.getSelectionModel().getSelectedItem();
        LoaiBan loaiBan = textLoaiBan.getSelectionModel().getSelectedItem();
        TrangThaiBan trangThai = TrangThaiBan.BAN_TRONG;

        if (maBan.isEmpty() || textKhuVuc.getSelectionModel().isEmpty() || textLoaiBan.getSelectionModel().isEmpty()) {
            showAlert("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        Ban newBan = new Ban(maBan,loaiBan, trangThai,khuVuc);
        List<Ban> banList = banDAO.readAll();
        banDAO.themBan(newBan);
        banList.add(newBan);
        clearForm();
        checkTamNgungPV.setDisable(true);
        checkTamNgungPV.setSelected(false);
        onClickReset();
        isEditing = false;
    }

    @FXML
    private void handleClear() {
        clearForm();
    }

    private void clearForm() {
        textMaBan.clear();
        textKhuVuc.getSelectionModel().clearSelection();
        textLoaiBan.getSelectionModel().clearSelection();
        checkTamNgungPV.setSelected(false);
        tableBan.getSelectionModel().clearSelection();
        checkTamNgungPV.setDisable(true);
        isEditing = false;
    }
    @FXML
    private void handleXoaBan() {
        Ban selectedBan = tableBan.getSelectionModel().getSelectedItem();
        List<Ban> banList = banDAO.readAll();
        if (selectedBan != null) {
            banDAO.deleteBan(selectedBan.getMaBan());
            banList.remove(selectedBan);
            clearForm();
        } else {
            showAlert("Vui lòng chọn bàn cần xóa!");
        }
        checkTamNgungPV.setDisable(true);
        onClickReset();
    }
    @FXML
    private void handleSuaBan() {
        Ban selectedBan = tableBan.getSelectionModel().getSelectedItem();
        if (selectedBan == null) {
            showAlert("Vui lòng chọn bàn cần chỉnh sửa!");
            return;
        }


        selectedBan.setKhuVuc(textKhuVuc.getSelectionModel().getSelectedItem());
        selectedBan.setLoaiBan(textLoaiBan.getSelectionModel().getSelectedItem());
        selectedBan.setTrangThaiBan(checkTamNgungPV.isSelected() ? TrangThaiBan.TAM_NGUNG_PHUC_VU : TrangThaiBan.BAN_TRONG);

        banDAO.capnhatBan(selectedBan);
        tableBan.refresh();
        List<Ban> banList = banDAO.readAll();
        ObservableList<Ban> banObservableList = FXCollections.observableArrayList(banList);
        tableBan.setItems(banObservableList);
        clearForm();
        checkTamNgungPV.setDisable(false);
        onClickReset();

    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void locBan() {
        KhuVuc selectedKhuVuc = locKhuVuc.getSelectionModel().getSelectedItem();
        LoaiBan selectedLoaiBan =locLoaiBan.getSelectionModel().getSelectedItem();
        TrangThaiBan selectedTrangThai = locTrangThai.getSelectionModel().getSelectedItem();

        ObservableList<Ban> filteredList = FXCollections.observableArrayList();
        List<Ban> danhSachBan = banDAO.readAll();
        for (Ban ban : danhSachBan) {
            boolean matchKhuVuc = selectedKhuVuc.equals("Tất cả") || ban.getKhuVuc().equals(selectedKhuVuc);
            boolean matchLoaiBan = selectedLoaiBan.equals("Tất cả") || ban.getLoaiBan().equals(selectedLoaiBan);
            boolean matchTrangThai = selectedTrangThai.equals("Tất cả") || ban.getTrangThaiBan().equals(selectedTrangThai);

            if (matchKhuVuc && matchLoaiBan && matchTrangThai) {
                filteredList.add(ban);
            }
        }

        tableBan.setItems(filteredList);
    }
    public void onClickReset() {
        banDAO = new BanDAO();
        List<Ban> banList = banDAO.readAll();
        ObservableList<Ban> banObservableList = FXCollections.observableArrayList(banList);
        tableBan.setItems(banObservableList);
        demTongBan(banList);
        demSoLuongTheoTrangThai(banList);
    }
    @FXML
    private void hienThiThongTinBan() {
        Ban selectedBan = tableBan.getSelectionModel().getSelectedItem();

        if (selectedBan != null) {
            // Hiển thị thông tin của bàn vào các trường tương ứng
            isEditing = true;

            textMaBan.setText(selectedBan.getMaBan());
            textKhuVuc.getSelectionModel().select(selectedBan.getKhuVuc());
            textLoaiBan.getSelectionModel().select(selectedBan.getLoaiBan());

            checkTamNgungPV.setDisable(false);

            if (selectedBan.getTrangThaiBan() == TrangThaiBan.TAM_NGUNG_PHUC_VU) {
                checkTamNgungPV.setSelected(true);
            } else {
                checkTamNgungPV.setSelected(false);
            }
        } else {
            isEditing = false;
            clearForm();
        }
    }
    private void updateMaBan() {
        if (isEditing) return;
        KhuVuc selectedKhuVuc = textKhuVuc.getSelectionModel().getSelectedItem();
        LoaiBan selectedLoaiBan = textLoaiBan.getSelectionModel().getSelectedItem();
        String sucChua = null;
        if(selectedLoaiBan == LoaiBan.BAN_2_NGUOI) sucChua = "02";
        if(selectedLoaiBan == LoaiBan.BAN_5_NGUOI) sucChua = "05";
        if(selectedLoaiBan == LoaiBan.BAN_10_NGUOI) sucChua = "10";
        // Kiểm tra nếu đã chọn đủ khu vực và loại bàn
        if (selectedKhuVuc != null && sucChua != null) {


            String maBan = generateMaBan(selectedKhuVuc, sucChua);
            textMaBan.setText(maBan);

        }
    }
    private String generateMaBan(KhuVuc khuVuc, String yy) {
        List<Ban> banList = banDAO.readAll();

        // Khởi tạo số thứ tự nếu khu vực chưa có mã bàn nào
        khuVucCounter.putIfAbsent(khuVuc, 1);

        // Lấy ký tự đầu của khu vực làm mã X
        String x = khuVuc.toString().substring(0, 1).toUpperCase();


        String zzz;
        while (true) {
            zzz = String.format("%03d", khuVucCounter.get(khuVuc));
            String newMaBan = x + yy + zzz;

            if (banList.stream().noneMatch(ban -> ban.getMaBan().equals(newMaBan))) {
                khuVucCounter.put(khuVuc, khuVucCounter.get(khuVuc) + 1);
                return newMaBan;
            }

            khuVucCounter.put(khuVuc, khuVucCounter.get(khuVuc) + 1);
        }


    }






}
