package org.login.quanlydatban.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import org.login.quanlydatban.dao.BanDAO;
import org.login.quanlydatban.entity.Ban;
import org.login.quanlydatban.entity.enums.TrangThaiBan;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChonBanController implements Initializable {
    @FXML
    private FlowPane flowPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label selectedLoaiBan;

    private BanDAO banDAO;

    private static ChonBanController instance;
    private static Parent root;
    public static ChonBanController getInstance() throws IOException {
        if (instance == null){
            instance = loadTrangChonBan();
        }
        return instance;
    }

    private static ChonBanController loadTrangChonBan() throws IOException {
        FXMLLoader loader = new FXMLLoader(ChonBanController.class.getResource("/org/login/quanlydatban/views/TrangChonBan.fxml"));
        root = loader.load();
        return loader.getController();
    }

    public Parent getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        flowPane.prefHeightProperty().bind(scrollPane.heightProperty());
        flowPane.prefWidthProperty().bind(scrollPane.widthProperty());

        banDAO = new BanDAO();

        for (Ban i : banDAO.readAll()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardBan_TrangChonBan.fxml"));
            try {
                AnchorPane pane = loader.load();
                CardBanController controller = loader.getController();
                controller.setBan(i);

                flowPane.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void refresh() {
        flowPane.getChildren().clear();
        for (Ban i : banDAO.readAll()){
            FXMLLoader loader = new FXMLLoader(ChonBanController.class.getResource("/org/login/quanlydatban/uicomponents/CardBan_TrangChonBan.fxml"));
            try {
                AnchorPane pane = loader.load();
                CardBanController controller = loader.getController();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardBan_TrangChonBan.fxml"));
            try {
                AnchorPane pane = loader.load();
                CardBanController controller = loader.getController();

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardBan_TrangChonBan.fxml"));
            try {
                AnchorPane pane = loader.load();
                CardBanController controller = loader.getController();

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardBan_TrangChonBan.fxml"));
            try {
                AnchorPane pane = loader.load();
                CardBanController controller = loader.getController();

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardBan_TrangChonBan.fxml"));
            try {
                AnchorPane pane = loader.load();
                CardBanController controller = loader.getController();

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardBan_TrangChonBan.fxml"));
            try {
                AnchorPane pane = loader.load();
                CardBanController controller = loader.getController();

                controller.setBan(i);

                flowPane.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
