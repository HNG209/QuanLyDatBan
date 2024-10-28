package org.login.quanlydatban.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.login.quanlydatban.entity.MonAn;

public class CardMonAnController {

    @FXML
    private Label giaTien;

    @FXML
    private Label tenMon;
    private MonAn monAn;
    @FXML
    public void them(){
        System.out.println(monAn);
    }

    public void setMonAn(MonAn monAn) {
        this.monAn = monAn;
        tenMon.setText(monAn.getTenMonAn());
        giaTien.setText(String.valueOf(monAn.getDonGia()));
    }
}
