package org.login.quanlydatban.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import org.login.quanlydatban.entity.MonAn;
import org.login.quanlydatban.utilities.NumberFormatter;


public class RankingCardController {
    @FXML
    private Label doanhSo;

    @FXML
    private Label doanhThu;

    @FXML
    private Label rank;

    @FXML
    private Label tenMonAn;


    public void setMonAn(int i, String tenMonAn, double doanhThu, int doanhSo) {
        rank.setText(String.valueOf(i));
        this.tenMonAn.setText(tenMonAn);
        this.doanhSo.setText(String.valueOf(doanhSo));
        this.doanhThu.setText(NumberFormatter.formatPrice(String.valueOf(doanhThu)));

    }


}
