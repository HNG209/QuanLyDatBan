package org.login.quanlydatban.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import org.login.quanlydatban.dao.BanDAO;
import org.login.quanlydatban.dao.MonAnDAO;
import org.login.quanlydatban.entity.Ban;
import org.login.quanlydatban.entity.MonAn;
import org.login.quanlydatban.entity.enums.TrangThaiBan;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DatMonController implements Initializable {
    @FXML
    private FlowPane flowPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField banID;

    @FXML
    private TextField trangThaiBanText;

    @FXML
    private Button btnGiuBan;

    @FXML
    private Button btnHuy;
    private MonAnDAO monAnDAO;
    private BanDAO banDAO;

    @FXML
    private TableView<Object[]> orderTable;

    @FXML
    private TableColumn<Object[], Double> donGia;

    @FXML
    private TableColumn<Object[], String> dvt;

    @FXML
    private TableColumn<Object[], String> ghiChu;

    @FXML
    private TableColumn<Object[], String> maBan;

    @FXML
    private TableColumn<Object[], Integer> soLuong;

    @FXML
    private TableColumn<Object[], String> tenMon;

    private ObservableList<Object[]> objectsObservableList;

    private Ban ban;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        monAnDAO = new MonAnDAO();
        banDAO = new BanDAO();

        objectsObservableList = FXCollections.observableArrayList();

        donGia.setCellValueFactory(data -> new SimpleDoubleProperty((Double)data.getValue()[2]).asObject());
        soLuong.setCellValueFactory(data -> new SimpleIntegerProperty((Integer) data.getValue()[3]).asObject());
        maBan.setCellValueFactory(data -> new SimpleStringProperty((String)data.getValue()[0]));
        tenMon.setCellValueFactory(data -> new SimpleStringProperty((String)data.getValue()[1]));
        dvt.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue()[4]));
        ghiChu.setCellValueFactory( data -> new SimpleStringProperty((String) data.getValue()[5]));
        orderTable.setItems(objectsObservableList);
        monAnDAO.readAll();
        flowPane.prefHeightProperty().bind(scrollPane.heightProperty());
        flowPane.prefWidthProperty().bind(scrollPane.widthProperty());

        for (MonAn i : monAnDAO.getListMonAn()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardMonAn_TrangDatMon.fxml"));
            try {
                AnchorPane pane = loader.load();

                CardMonAnController controller = loader.getController();
                controller.setMonAn(i, this);
                controller.setController(this);
                flowPane.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        monAnDAO.getListMonAn();
//        scrollPane.vvalueProperty().addListener((obs, oldValue, newValue) -> {
//            if(newValue.doubleValue() == 1.0){
//                for (int i = 0; i < 20; i++){
//                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardMonAn_TrangDatMon.fxml"));
//                    try {
//                        AnchorPane pane = loader.load();
//                        flowPane.getChildren().add(pane);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//
//            }
//        });
    }

    public void setBan(Ban ban) {
        this.ban = ban;
        this.banID.setText(ban.getMaBan());
        this.trangThaiBanText.setText(ban.getTrangThaiBan().toString());

        if(ban.getTrangThaiBan().equals(TrangThaiBan.BAN_TRONG)) {
            this.btnHuy.setVisible(false);
            this.btnGiuBan.setVisible(true);
        }
        else {
            this.btnHuy.setVisible(true);
            this.btnGiuBan.setVisible(false);
        }
    }

    public Ban getBan() {
        return this.ban;
    }

    @FXML
    void giuBan(ActionEvent event) {
        this.ban = banDAO.updateBan(ban, TrangThaiBan.DANG_PHUC_VU);
        this.trangThaiBanText.setText(ban.getTrangThaiBan().toString());
        this.btnHuy.setVisible(true);
        this.btnGiuBan.setVisible(false);
    }
    @FXML
    void huyBan(ActionEvent event) {
        this.ban = banDAO.updateBan(ban, TrangThaiBan.BAN_TRONG);
        this.trangThaiBanText.setText(ban.getTrangThaiBan().toString());
        this.btnHuy.setVisible(false);
        this.btnGiuBan.setVisible(true);
    }
    @FXML
    void back(MouseEvent event) throws IOException {
        if(anchorPane.getParent() instanceof BorderPane){
            BorderPane pane = (BorderPane) anchorPane.getParent();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangChonBan.fxml"));
            AnchorPane anchorPane = loader.load();

            pane.setCenter(anchorPane);
        }
    }
    public void themDuLieuVaoBangMonAn(Object[] objects){
        boolean trungMaMonAn = false;
        String maMonAnMoi = objects[0].toString();
        int soLuongMoi = Integer.parseInt(objects[3].toString());
        for (Object[] row: orderTable.getItems()){
            String maMonAnHienTai = row[0].toString();
            if(maMonAnHienTai.equalsIgnoreCase(maMonAnMoi)){
                int soLuongHienTai = (Integer) row[3];
                row[3] = soLuongMoi + soLuongHienTai; // Increment quantity
                orderTable.refresh(); // Refresh table to show updated quantity
                trungMaMonAn = true;
                break;
            }
        }if(!trungMaMonAn) {
            orderTable.getItems().add(objects);
        }
    }
}
