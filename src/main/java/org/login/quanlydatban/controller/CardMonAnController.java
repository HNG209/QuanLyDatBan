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

    public DatMonController getController() {
        return controller;
    }

    public void setController(DatMonController controller) {
        this.controller = controller;
    }

    @FXML
    public void them(){
        if(controller != null) {

            Object[] row = new Object[]{monAn.getMaMonAn(), monAn.getTenMonAn(), monAn.getDonGia(), 1, monAn.getDonViTinh(),""};
            controller.themDuLieuVaoBangMonAn(row);
        }
    }

    public void setMonAn(MonAn monAn, DatMonController controller) {
        this.monAn = monAn;
        this.controller = controller;
        tenMon.setText(monAn.getTenMonAn());
        giaTien.setText(String.valueOf(monAn.getDonGia()));
    }
}
