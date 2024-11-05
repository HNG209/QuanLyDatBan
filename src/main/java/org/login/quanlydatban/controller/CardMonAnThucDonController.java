package org.login.quanlydatban.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.login.quanlydatban.entity.MonAn;

public class CardMonAnThucDonController {

    @FXML
    private Label giaTien;

    @FXML
    private Label tenMon;
    private MonAn monAn;

    private DatMonController controller;
    private ThucDonController controllerThucDon;

    @FXML
    public void them(){

    }

    public void setMonAnThucDon(MonAn monAn, ThucDonController controller) {
        this.monAn = monAn;
        this.controllerThucDon = controller;
        tenMon.setText(monAn.getTenMonAn());
        giaTien.setText(String.valueOf(monAn.getDonGia()));
    }
}
