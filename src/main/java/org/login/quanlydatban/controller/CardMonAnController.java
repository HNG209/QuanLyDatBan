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

    private DatMonController controller;
    @FXML
    public void them(){

    }

    public void setMonAn(MonAn monAn, DatMonController controller) {
        this.monAn = monAn;
        this.controller = controller;
        tenMon.setText(monAn.getTenMonAn());
        giaTien.setText(String.valueOf(monAn.getDonGia()));
    }
}
