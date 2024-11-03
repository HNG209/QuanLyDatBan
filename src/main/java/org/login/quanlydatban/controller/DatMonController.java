package org.login.quanlydatban.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
import java.util.Objects;
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
    private TableView<Objects> orderTable;

    @FXML
    private TableColumn<Objects, Double> donGia;

    @FXML
    private TableColumn<Objects, String> dvt;

    @FXML
    private TableColumn<Objects, String> ghiChu;

    @FXML
    private TableColumn<Objects, String> maBan;

    @FXML
    private TableColumn<Objects, Integer> soLuong;

    @FXML
    private TableColumn<Objects, String> tenMon;

    private ObservableList<Objects> objectsObservableList;

    private Ban ban;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        monAnDAO = new MonAnDAO();
        banDAO = new BanDAO();

        objectsObservableList = FXCollections.observableArrayList();

//        donGia.cellValueFactoryProperty(data -> new SimpleDoubleProperty())

        monAnDAO.readAll();
        flowPane.prefHeightProperty().bind(scrollPane.heightProperty());
        flowPane.prefWidthProperty().bind(scrollPane.widthProperty());

        for (MonAn i : monAnDAO.getListMonAn()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardMonAn_TrangDatMon.fxml"));
            try {
                AnchorPane pane = loader.load();

                CardMonAnController controller = loader.getController();
                controller.setMonAn(i, this);

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
}
