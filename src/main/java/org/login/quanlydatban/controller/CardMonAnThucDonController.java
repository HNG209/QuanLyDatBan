package org.login.quanlydatban.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.login.quanlydatban.entity.MonAn;

public class CardMonAnThucDonController {

    @FXML
    private Label giaTien;

    @FXML
    private Label tenMon;
    private MonAn monAn;

    private ThucDonController controller;

    @FXML
    public void them(){

    }

    @FXML
    void chonControl(MouseEvent event) {
        controller.setMonAn(monAn);
    }



    public void setController(ThucDonController controller){
        this.controller = controller;
    }

    public void setMonAnThucDon(MonAn monAn, ThucDonController controller) {
        this.monAn = monAn;
        this.controller = controller;
        tenMon.setText(monAn.getTenMonAn());
        giaTien.setText(String.valueOf(monAn.getDonGia()));
    }
}
