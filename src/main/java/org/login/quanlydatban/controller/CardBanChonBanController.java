package org.login.quanlydatban.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.login.service.HoaDonService;

import org.login.entity.Ban;
import org.login.entity.enums.TrangThaiBan;
import org.login.entity.HoaDon;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CardBanChonBanController implements Initializable {
    @FXML
    private Label khuVuc;

    @FXML
    private Label loaiBan;

    @FXML
    private Button chuyenBan;

    @FXML
    private Label maBan;

    @FXML
    private ImageView image;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label trangThai;
    private Ban ban;
    private HoaDonService hoaDonService;


    private Stage currentStage;
    @FXML
    void chonBan(ActionEvent event) throws IOException {
        if(ban.getTrangThaiBan() != TrangThaiBan.TAM_NGUNG_PHUC_VU) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangDatMon.fxml"));
            AnchorPane anchorPane = loader.load();
            DatMonController controller = loader.getController();

            controller.setBan(ban);
            controller.setPageSelected(0);

            List<HoaDon> list = hoaDonService.getHoaDonFromBan(ban);

            if (!list.isEmpty()) {
                HoaDon hoaDon = list.get(list.size() - 1);
                controller.setHoaDon(hoaDon);
                controller.capNhatTongTien();
            }

            TrangChuController.getBorderPaneStatic().setCenter(anchorPane);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thao tác không thể được thực hiện");
            alert.setHeaderText("Bàn đang trong trạng thái bảo trì, không thể phu vụ ngay bây giờ");
            alert.showAndWait();
        }
    }

    public void setBan(Ban ban){
        this.ban = ban;
        khuVuc.setText(ban.getKhuVuc().toString());
        loaiBan.setText(ban.getLoaiBan().toString());
        maBan.setText(ban.getMaBan());
        trangThai.setText(ban.getTrangThaiBan().toString());
        Image img;
        switch (ban.getTrangThaiBan()) {
            case BAN_TRONG:
                img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/login/quanlydatban/icons/bantrong.png")));
                this.image.setImage(img);
                this.chuyenBan.setDisable(true);
                this.anchorPane.getStyleClass().add("sage-green");
                break;
            case DANG_PHUC_VU:
                img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/login/quanlydatban/icons/bandangphucvu.png")));
                this.image.setImage(img);
                this.chuyenBan.setDisable(false);
                this.anchorPane.getStyleClass().add("sunset-orange");
                break;
            default:
                img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/login/quanlydatban/icons/banhong.png")));
                this.image.setImage(img);
                this.anchorPane.getStyleClass().add("pale-lemon");
                break;
        }
    }
    @FXML
    void chuyenBan(ActionEvent event) throws IOException {
        if(ban.getTrangThaiBan() == TrangThaiBan.DANG_PHUC_VU){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangChuyenBan.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/org/login/quanlydatban/stylesheets/style.css")).toExternalForm());

            ChuyenBanController chuyenBanController = loader.getController();
            chuyenBanController.setBanHienTai(ban);
            chuyenBanController.setCurrentHD(hoaDonService.getHoaDonFromBan(ban).get(0));

            if(currentStage == null){
                currentStage = new Stage();
                currentStage.setScene(scene);
                currentStage.initStyle(StageStyle.UNDECORATED);
                currentStage.show();
            }
            else {
                currentStage.show();
                currentStage.toFront();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String host = System.getenv("HOST_NAME");
        try {
            hoaDonService = (HoaDonService) Naming.lookup("rmi://"+ host + ":2909/hoaDonService");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
