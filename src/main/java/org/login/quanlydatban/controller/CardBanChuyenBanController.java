package org.login.quanlydatban.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.login.entity.Ban;

public class CardBanChuyenBanController {
    private Ban ban;

    @FXML
    private Label khuVuc;

    @FXML
    private Label loaiBan;

    @FXML
    private Label maBan;

    private ChuyenBanController chuyenBanController;

    public Ban getBan() {
        return ban;
    }

    public void setBan(Ban ban) {
        this.ban = ban;
        maBan.setText(ban.getMaBan());
        khuVuc.setText(ban.getKhuVuc().toString());
        loaiBan.setText(ban.getLoaiBan().toString());
    }

    public void setChuyenBanController(ChuyenBanController chuyenBanController) {
        this.chuyenBanController = chuyenBanController;
    }

    @FXML
    void chonBan(ActionEvent event) {
        chuyenBanController.setBanChuyen(ban);
    }
}
