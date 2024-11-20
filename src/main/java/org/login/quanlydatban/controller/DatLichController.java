package org.login.quanlydatban.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import org.login.quanlydatban.entity.enums.LoaiBan;
import org.login.quanlydatban.entity.enums.LoaiTiec;

import java.io.IOException;
import java.net.URL;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbLoaiBan.getItems().addAll(LoaiBan.values());
        cbLoaiTiec.getItems().addAll(LoaiTiec.values());
        cbChonLoaiTiec.getItems().addAll(LoaiTiec.values());

        for(int i = 1; i <= 24; i++)
            cbGio.getItems().add(i);

        for (int i = 5; i <= 55; i += 5)
            cbPhut.getItems().add(i);
    }

    public void loadDatLich() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangDatLich.fxml"));
        TabPane pane = loader.load();

        TrangChuController.getBorderPaneStatic().setCenter(pane);
    }

    @FXML
    void nhanBan(ActionEvent event) throws IOException {

    }

    @FXML
    void chonMon(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangDatMon.fxml"));
        AnchorPane pane = loader.load();
        DatMonController controller = loader.getController();
        controller.setDatLichController(this);

        tabDatLich.setContent(pane);
    }
}
