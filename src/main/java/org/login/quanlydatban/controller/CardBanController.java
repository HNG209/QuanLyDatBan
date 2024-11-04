package org.login.quanlydatban.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.login.quanlydatban.dao.HoaDonDAO;
import org.login.quanlydatban.entity.Ban;
import org.login.quanlydatban.entity.HoaDon;
import org.login.quanlydatban.entity.NhanVien;
import org.login.quanlydatban.entity.enums.TrangThaiBan;
import org.login.quanlydatban.entity.enums.TrangThaiHoaDon;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CardBanController implements Initializable {
    @FXML
    private Label khuVuc;

    @FXML
    private Label loaiBan;

    @FXML
    private Label maBan;

    @FXML
    private ImageView image;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label trangThai;
    private Ban ban;

    private HoaDonDAO hoaDonDAO;
    private NhanVien nhanVien;
    @FXML
    void chonBan(ActionEvent event) throws IOException {
        if(ban.getTrangThaiBan() != TrangThaiBan.TAM_NGUNG_PHUC_VU){
            if(anchorPane.getParent().getParent().getParent().getParent().getParent().getParent() instanceof BorderPane){
                BorderPane pane = (BorderPane) anchorPane.getParent().getParent().getParent().getParent().getParent().getParent();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangDatMon.fxml"));
                AnchorPane anchorPane = loader.load();
                DatMonController controller = loader.getController();

                controller.setBan(ban);
                controller.setNhanVien(nhanVien);

                List<HoaDon> list = hoaDonDAO.getHoaDonFromBan(ban);

                if(!list.isEmpty()){
                    HoaDon hoaDon = list.get(list.size() - 1);
                    controller.setHoaDon(hoaDon);
                }

                pane.setCenter(anchorPane);
            }
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
                this.anchorPane.getStyleClass().add("sage-green");
                break;
            case DANG_PHUC_VU:
                img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/login/quanlydatban/icons/bandangphucvu.png")));
                this.image.setImage(img);
                this.anchorPane.getStyleClass().add("pale-lemon");
                break;
            case TAM_NGUNG_PHUC_VU:
                img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/login/quanlydatban/icons/banhong.png")));
                this.image.setImage(img);
                this.anchorPane.getStyleClass().add("light-coral");
                break;
            default:
                img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/login/quanlydatban/icons/bandattruoc.png")));
                this.image.setImage(img);
                this.anchorPane.getStyleClass().add("sunset-orange");
                break;
        }
    }
    @FXML
    void chuyenBan(ActionEvent event) {
        System.out.println("chuyen");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hoaDonDAO = new HoaDonDAO();
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }
}
