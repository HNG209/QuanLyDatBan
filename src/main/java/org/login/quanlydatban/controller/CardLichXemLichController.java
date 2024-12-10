package org.login.quanlydatban.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.login.quanlydatban.entity.LichDat;
import org.login.quanlydatban.entity.enums.TrangThaiHoaDon;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.ResourceBundle;

public class CardLichXemLichController implements Initializable {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label labelBan;

    @FXML
    private Label labelThoiGian;

    @FXML
    private Label labeltrangThai;

    @FXML
    private GridPane statusNhanBan;

    @FXML
    private ImageView icon;

    private LichDat lichDat;

    public LichDat getLichDat() {
        return lichDat;
    }

    public void setLichDat(LichDat lichDat) {
        this.lichDat = lichDat;
        labeltrangThai.setText(lichDat.getHoaDon().getTrangThaiHoaDon().toString());
        labelThoiGian.setText(lichDat.getThoiGianNhanBan().toLocalTime().toString());
        labelBan.setText(lichDat.getHoaDon().getBan().getMaBan());

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/login/quanlydatban/icons/cancel.png")));

        switch (lichDat.getHoaDon().getTrangThaiHoaDon()){
            case DA_DAT:
                anchorPane.getStyleClass().add("sage-green");
                image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/login/quanlydatban/icons/online-booking.png")));
                break;
            case CHUA_THANH_TOAN:
                anchorPane.getStyleClass().add("sunset-orange");
                image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/login/quanlydatban/icons/no-money.png")));
                break;
            case DA_THANH_TOAN:
                anchorPane.getStyleClass().add("pale-lemon");
                image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/login/quanlydatban/icons/check.png")));
                break;
            default:
                anchorPane.getStyleClass().add("light-coral");
                break;
        }

        icon.setImage(image);

        statusNhanBan.setVisible(false);
        if(LocalDateTime.now().isAfter(lichDat.getThoiGianNhanBan()) && lichDat.getHoaDon().getTrangThaiHoaDon() == TrangThaiHoaDon.DA_DAT)
            statusNhanBan.setVisible(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void selectLich(MouseEvent event) throws IOException {
        XemLichDatController.getInstance().setSelectedLichDat(lichDat);
    }
}
