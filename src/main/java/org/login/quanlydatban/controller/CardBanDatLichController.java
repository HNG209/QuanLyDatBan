package org.login.quanlydatban.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.login.quanlydatban.entity.Ban;

import java.io.IOException;
import java.util.Objects;

public class CardBanDatLichController {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ImageView image;

    @FXML
    private Label khuVuc;

    @FXML
    private Label loaiBan;

    @FXML
    private Label maBan;

    @FXML
    private Label trangThai;

    private Ban ban;

    @FXML
    void chonBan(ActionEvent event) throws IOException {
        DatLichController.getInstance().setBan(ban);
    }

    public Ban getBan() {
        return ban;
    }

    public void setBan(Ban ban) {
        this.ban = ban;
        loaiBan.setText(ban.getLoaiBan().toString());
        maBan.setText(ban.getMaBan());
        khuVuc.setText(ban.getKhuVuc().toString());
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
                this.anchorPane.getStyleClass().add("sunset-orange");
                break;
            default:
                img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/login/quanlydatban/icons/banhong.png")));
                this.image.setImage(img);
                this.anchorPane.getStyleClass().add("pale-lemon");
                break;
        }
    }
}
