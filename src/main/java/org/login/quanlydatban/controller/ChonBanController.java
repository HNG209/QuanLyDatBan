package org.login.quanlydatban.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import org.login.quanlydatban.dao.BanDAO;
import org.login.quanlydatban.entity.Ban;
import org.login.quanlydatban.entity.NhanVien;
import org.login.quanlydatban.entity.enums.LoaiBan;
import org.login.quanlydatban.entity.enums.TrangThaiBan;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ChonBanController implements Initializable {
    @FXML
    private FlowPane flowPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label selectedLoaiBan;

    private BanDAO banDAO;

    private NhanVien nhanVien;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        flowPane.prefHeightProperty().bind(scrollPane.heightProperty());
        flowPane.prefWidthProperty().bind(scrollPane.widthProperty());

        banDAO = new BanDAO();

        for (Ban i : banDAO.readAll()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardBan.fxml"));
            try {
                AnchorPane pane = loader.load();
                CardBanController controller = loader.getController();
                controller.setNhanVien(nhanVien);

                controller.setBan(i);

                flowPane.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void showBanDaDat(MouseEvent event) {
        selectedLoaiBan.setText(TrangThaiBan.DA_DAT.toString());
        flowPane.getChildren().clear();

        for (Ban i : banDAO.readByStatus(TrangThaiBan.DA_DAT)){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardBan.fxml"));
            try {
                AnchorPane pane = loader.load();
                CardBanController controller = loader.getController();
                controller.setNhanVien(nhanVien);

                controller.setBan(i);

                flowPane.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void showBanDangPhucVu(MouseEvent event) {
        selectedLoaiBan.setText(TrangThaiBan.DANG_PHUC_VU.toString());
        flowPane.getChildren().clear();

        for (Ban i : banDAO.readByStatus(TrangThaiBan.DANG_PHUC_VU)){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardBan.fxml"));
            try {
                AnchorPane pane = loader.load();
                CardBanController controller = loader.getController();
                controller.setNhanVien(nhanVien);

                controller.setBan(i);

                flowPane.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void showBanHong(MouseEvent event) {
        selectedLoaiBan.setText(TrangThaiBan.TAM_NGUNG_PHUC_VU.toString());
        flowPane.getChildren().clear();

        for (Ban i : banDAO.readByStatus(TrangThaiBan.TAM_NGUNG_PHUC_VU)){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardBan.fxml"));
            try {
                AnchorPane pane = loader.load();
                CardBanController controller = loader.getController();
                controller.setNhanVien(nhanVien);

                controller.setBan(i);

                flowPane.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @FXML
    void showBanTrong(MouseEvent event) {
        selectedLoaiBan.setText(TrangThaiBan.BAN_TRONG.toString());
        flowPane.getChildren().clear();

        for (Ban i : banDAO.readByStatus(TrangThaiBan.BAN_TRONG)){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardBan.fxml"));
            try {
                AnchorPane pane = loader.load();
                CardBanController controller = loader.getController();
                controller.setNhanVien(nhanVien);

                controller.setBan(i);

                flowPane.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void showAll(MouseEvent event) {
        selectedLoaiBan.setText("TẤT CẢ BÀN");
        flowPane.getChildren().clear();

        for (Ban i : banDAO.readAll()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardBan.fxml"));
            try {
                AnchorPane pane = loader.load();
                CardBanController controller = loader.getController();
                controller.setNhanVien(nhanVien);

                controller.setBan(i);

                flowPane.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }
}
