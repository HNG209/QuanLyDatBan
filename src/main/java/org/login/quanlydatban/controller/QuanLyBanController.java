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

import java.util.*;


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
    private ComboBox<Object> locKhuVuc;
    @FXML
    private ComboBox<Object> locLoaiBan;
    @FXML
    private ComboBox<Object> locTrangThai;

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

        ObservableList<Object> khuVucList = FXCollections.observableArrayList("Tất cả");
        khuVucList.addAll(KhuVuc.values());
        locKhuVuc.getItems().setAll(khuVucList);

        ObservableList<Object> loaiBanList = FXCollections.observableArrayList("Tất cả");
        loaiBanList.addAll(LoaiBan.values());
        locLoaiBan.getItems().setAll(loaiBanList);

        ObservableList<Object> trangThaiList = FXCollections.observableArrayList("Tất cả");
        trangThaiList.addAll(TrangThaiBan.values());
        locTrangThai.getItems().setAll(trangThaiList);

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
        for (Ban ban : tableBan.getItems()) {
            if (ban.getMaBan().equals(maBan)) {
                String LB;
                if (loaiBan == LoaiBan.BAN_2_NGUOI) {
                    LB="02";
                } else if (loaiBan == LoaiBan.BAN_5_NGUOI) {
                    LB="05";
                } else {
                    LB="10";
                }
                maBan = generateMaBan(khuVuc,LB);
                showAlert("Mã bàn đã tồn tại! Hệ thống đã tự động tạo mã mới!");
            }
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận thêm bàn");
        alert.setHeaderText("Bạn có chắc chắn muốn thêm bàn mới không?");
        alert.setContentText("Mã bàn: " + maBan + "\nKhu vực: " + khuVuc + "\nLoại bàn: " + loaiBan);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            Ban newBan = new Ban(maBan, loaiBan, trangThai, khuVuc);
            banDAO.themBan(newBan);
            tableBan.getItems().add(newBan);
            clearForm();
            checkTamNgungPV.setDisable(true);
            checkTamNgungPV.setSelected(false);
            onClickReset();
            isEditing = false;
        }
        else {
            rollbackMaBanCounter(khuVuc);
        }

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
    private void handleSuaBan() {
        Ban selectedBan = tableBan.getSelectionModel().getSelectedItem();
        if (selectedBan == null) {
            showAlert("Vui lòng chọn bàn cần chỉnh sửa!");
            return;
        }

        selectedBan.setKhuVuc(textKhuVuc.getSelectionModel().getSelectedItem());
        selectedBan.setLoaiBan(textLoaiBan.getSelectionModel().getSelectedItem());
        selectedBan.setTrangThaiBan(checkTamNgungPV.isSelected() ? TrangThaiBan.TAM_NGUNG_PHUC_VU : TrangThaiBan.BAN_TRONG);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận sửa bàn");
        alert.setHeaderText("Bạn có chắc chắn muốn sửa bàn không?");


        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            banDAO.capnhatBan(selectedBan);
            tableBan.refresh();
            List<Ban> banList = banDAO.readAll();
            ObservableList<Ban> banObservableList = FXCollections.observableArrayList(banList);
            tableBan.setItems(banObservableList);
            clearForm();
            checkTamNgungPV.setDisable(false);
            onClickReset();

        }

    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void locBan() {

        Object selectedKhuVuc = locKhuVuc.getSelectionModel().getSelectedItem();
        Object selectedLoaiBan = locLoaiBan.getSelectionModel().getSelectedItem();
        Object selectedTrangThai = locTrangThai.getSelectionModel().getSelectedItem();

        ObservableList<Ban> filteredList = FXCollections.observableArrayList();
        List<Ban> danhSachBan = banDAO.readAll();

        for (Ban ban : danhSachBan) {

            boolean matchKhuVuc = (selectedKhuVuc == null || selectedKhuVuc.equals("Tất cả") || ban.getKhuVuc().equals(selectedKhuVuc));
            boolean matchLoaiBan = (selectedLoaiBan == null || selectedLoaiBan.equals("Tất cả") || ban.getLoaiBan().equals(selectedLoaiBan));
            boolean matchTrangThai = (selectedTrangThai == null || selectedTrangThai.equals("Tất cả") || ban.getTrangThaiBan().equals(selectedTrangThai));


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

        locKhuVuc.getSelectionModel().clearSelection();
        locLoaiBan.getSelectionModel().clearSelection();
        locTrangThai.getSelectionModel().clearSelection();

          locKhuVuc.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Chọn khu vực"); // PromptText của locKhuVuc
                } else {
                    setText(item.toString());
                }
            }
        });
        locLoaiBan.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Chọn loại bàn");
                } else {
                    setText(item.toString());
                }
            }
        });
        locTrangThai.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Chọn trạng thái");
                } else {
                    setText(item.toString());
                }
            }
        });
    }
    @FXML
    private void hienThiThongTinBan() {
        Ban selectedBan = tableBan.getSelectionModel().getSelectedItem();

        if (selectedBan != null) {

            isEditing = true;

            textMaBan.setText(selectedBan.getMaBan());
            textKhuVuc.getSelectionModel().select(selectedBan.getKhuVuc());
            textLoaiBan.getSelectionModel().select(selectedBan.getLoaiBan());
            if (selectedBan.getTrangThaiBan() == TrangThaiBan.TAM_NGUNG_PHUC_VU) {
                checkTamNgungPV.setSelected(true);
            } else {
                checkTamNgungPV.setSelected(false);
            }
            if(selectedBan.getTrangThaiBan() == TrangThaiBan.DANG_PHUC_VU ) {
                checkTamNgungPV.setDisable(true);
                textLoaiBan.setDisable(true);
                textKhuVuc.setDisable(true);
            } else {
                checkTamNgungPV.setDisable(false);
                textLoaiBan.setDisable(false);
                textKhuVuc.setDisable(false);
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
    private void rollbackMaBanCounter(KhuVuc khuVuc) {
        if (khuVucCounter.containsKey(khuVuc)) {
            khuVucCounter.put(khuVuc, khuVucCounter.get(khuVuc) - 1);
        }
    }






}
