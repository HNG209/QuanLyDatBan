package org.login.quanlydatban.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
//import org.login.dao.BanDAO;
//import org.login.dao.HoaDonDAO;
import org.login.service.BanService;
import org.login.entity.Ban;
import org.login.entity.HoaDon;
import org.login.entity.enums.KhuVuc;
import org.login.entity.enums.LoaiBan;
import org.login.entity.enums.TrangThaiBan;
import org.login.quanlydatban.notification.Notification;
import org.login.service.HoaDonService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

public class ChuyenBanController implements Initializable {
    @FXML
    private Button btnHuy;

    @FXML
    private FlowPane flowPane;

    @FXML
    private TextField khuVucBanChuyen;

    @FXML
    private TextField khuVucBanHT;

    @FXML
    private TextField loaiBanChuyen;

    @FXML
    private TextField loaiBanHT;

    @FXML
    private TextField maBanChuyen;

    @FXML
    private ComboBox<KhuVuc> cbKhuVuc;

    @FXML
    private ComboBox<LoaiBan> cbLoaiBan;

    @FXML
    private TextField tfMaBan;

    @FXML
    private TextField maBanHT;
    private Ban banHienTai;
    private Ban banChuyen;

    private BanService banService;
    private HoaDonService hoaDonService;

    private HoaDon currentHD;

    private DatMonController datMonController;
    @FXML
    void huy(ActionEvent event) {
        Stage currentStage = (Stage) btnHuy.getScene().getWindow();
        currentStage.close();
    }

    public Ban getBanHienTai() {
        return banHienTai;
    }

    public void setBanHienTai(Ban banHienTai) {
        this.banHienTai = banHienTai;

        if (banHienTai != null){
            maBanHT.setText(banHienTai.getMaBan());
            khuVucBanHT.setText(banHienTai.getKhuVuc().toString());
            loaiBanHT.setText(banHienTai.getLoaiBan().toString());
        }
        else {
            maBanHT.clear();
            khuVucBanHT.clear();
            loaiBanHT.clear();
        }
    }

    public Ban getBanChuyen() {
        return banChuyen;
    }

    public void setBanChuyen(Ban banChuyen) {
        this.banChuyen = banChuyen;

        if(banChuyen != null){
            maBanChuyen.setText(banChuyen.getMaBan());
            khuVucBanChuyen.setText(banChuyen.getKhuVuc().toString());
            loaiBanChuyen.setText(banChuyen.getLoaiBan().toString());
        }
        else {
            maBanChuyen.clear();
            khuVucBanChuyen.clear();
            loaiBanChuyen.clear();
        }
    }

    public DatMonController getDatMonController() {
        return datMonController;
    }

    public void setDatMonController(DatMonController datMonController) {
        this.datMonController = datMonController;
    }

    public HoaDon getCurrentHD() {
        return currentHD;
    }

    public void setCurrentHD(HoaDon currentHD) {
        this.currentHD = currentHD;
    }

    @FXML
    void chuyenBan(ActionEvent event) throws IOException {
        if (banChuyen != null) {
            if (Notification.xacNhan("Xác nhận chuyển bàn?")) {
                currentHD.setBan(banChuyen);
                hoaDonService.updateHoaDon(currentHD);

                banHienTai.setTrangThaiBan(TrangThaiBan.BAN_TRONG);
                banChuyen.setTrangThaiBan(TrangThaiBan.DANG_PHUC_VU);

                banService.updateBan(banHienTai);
                banService.updateBan(banChuyen);

                //(1) called from CardBanController, datMonController = null
                //(2) called from DatMonController, datMonController != null, point: to update the current table in datMonController
                if(datMonController != null)
                    datMonController.setBan(banChuyen);
                else
                    ChonBanController.getInstance().refresh();

                Notification.thongBao("Chuyển thành công từ bàn " + banHienTai.getMaBan() + " sang bàn " + banChuyen.getMaBan(), Alert.AlertType.INFORMATION);

                this.setBanHienTai(banChuyen);
                this.setBanChuyen(null);

                Stage stage = (Stage) btnHuy.getScene().getWindow();
                stage.close();
            }
        } else Notification.thongBao("Vui lòng chọn bàn để chuyển", Alert.AlertType.INFORMATION);
    }

    @FXML
    void timKiem(MouseEvent event) throws RemoteException {
        loadBan(banService.getListBanBy(tfMaBan.getText(),
                TrangThaiBan.BAN_TRONG,
                cbLoaiBan.getSelectionModel().getSelectedItem(),
                cbKhuVuc.getSelectionModel().getSelectedItem()));
    }

    public void loadBan() throws RemoteException {
        flowPane.getChildren().clear();
        for (Ban i : banService.getListBanTrong()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardBan_TrangChuyenBan.fxml"));
            try {
                AnchorPane pane = loader.load();
                CardBanChuyenBanController controller = loader.getController();
                controller.setBan(i);
                controller.setChuyenBanController(this);

                flowPane.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void loadBan(List<Ban> list){
        flowPane.getChildren().clear();
        for (Ban i : list){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardBan_TrangChuyenBan.fxml"));
            try {
                AnchorPane pane = loader.load();
                CardBanChuyenBanController controller = loader.getController();
                controller.setBan(i);
                controller.setChuyenBanController(this);

                flowPane.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void resetBan(MouseEvent event) throws RemoteException {
        tfMaBan.clear();
        cbLoaiBan.setValue(null);
        cbKhuVuc.setValue(null);
        loadBan();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String host = System.getenv("HOST_NAME");
        try {
            banService = (BanService) Naming.lookup("rmi://"+ host + ":2909/banService");
            hoaDonService = (HoaDonService) Naming.lookup("rmi://"+ host + ":2909/hoaDonService");
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        cbKhuVuc.getItems().add(null);
        cbKhuVuc.getItems().addAll(KhuVuc.values());

        cbLoaiBan.getItems().add(null);
        cbLoaiBan.getItems().addAll(LoaiBan.values());

        try {
            loadBan();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
