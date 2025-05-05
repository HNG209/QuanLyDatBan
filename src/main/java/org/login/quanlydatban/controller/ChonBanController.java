package org.login.quanlydatban.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import org.login.quanlydatban.utilities.RMIServiceUtils;
import org.login.service.BanService;
import org.login.entity.Ban;
import org.login.entity.enums.KhuVuc;
import org.login.entity.enums.LoaiBan;
import org.login.entity.enums.TrangThaiBan;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

public class ChonBanController implements Initializable {
    @FXML
    private FlowPane flowPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField tfMaBan;

    @FXML
    private ComboBox<KhuVuc> cbKhuVuc;

    @FXML
    private ComboBox<LoaiBan> cbLoaiBan;

    @FXML
    private ComboBox<TrangThaiBan> cbTrangThaiBan;

    private BanService banService;

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

        try {
            banService = RMIServiceUtils.useBanService();
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            throw new RuntimeException(e);
        }
        cbKhuVuc.getItems().add(null);
        cbKhuVuc.getItems().addAll(KhuVuc.values());

        cbTrangThaiBan.getItems().add(null);
        cbTrangThaiBan.getItems().addAll(TrangThaiBan.values());

        cbLoaiBan.getItems().add(null);
        cbLoaiBan.getItems().addAll(LoaiBan.values());

        try {
            for (Ban i : banService.readAll()){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardBan_TrangChonBan.fxml"));
                try {
                    AnchorPane pane = loader.load();
                    CardBanChonBanController controller = loader.getController();
                    controller.setBan(i);

                    flowPane.getChildren().add(pane);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void refresh() throws RemoteException {
        flowPane.getChildren().clear();
        for (Ban i : banService.readAll()){
            FXMLLoader loader = new FXMLLoader(ChonBanController.class.getResource("/org/login/quanlydatban/uicomponents/CardBan_TrangChonBan.fxml"));
            try {
                AnchorPane pane = loader.load();
                CardBanChonBanController controller = loader.getController();
                controller.setBan(i);

                flowPane.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void refresh(List<Ban> list){
        flowPane.getChildren().clear();
        for (Ban i : list){
            FXMLLoader loader = new FXMLLoader(ChonBanController.class.getResource("/org/login/quanlydatban/uicomponents/CardBan_TrangChonBan.fxml"));
            try {
                AnchorPane pane = loader.load();
                CardBanChonBanController controller = loader.getController();
                controller.setBan(i);

                flowPane.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void timKiem(MouseEvent event) throws RemoteException {
        refresh(banService.getListBanBy(tfMaBan.getText(),
                cbTrangThaiBan.getSelectionModel().getSelectedItem(),
                cbLoaiBan.getSelectionModel().getSelectedItem(),
                cbKhuVuc.getSelectionModel().getSelectedItem()));
    }

    @FXML
    void refresh(MouseEvent event) throws RemoteException {
        tfMaBan.clear();

        cbLoaiBan.setValue(null);
        cbKhuVuc.setValue(null);
        cbTrangThaiBan.setValue(null);

        refresh();
    }
}
