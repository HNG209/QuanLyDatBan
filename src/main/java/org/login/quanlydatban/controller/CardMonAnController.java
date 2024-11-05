package org.login.quanlydatban.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.login.quanlydatban.dao.ChiTietHoaDonDAO;
import org.login.quanlydatban.entity.ChiTietHoaDon;
import org.login.quanlydatban.entity.MonAn;

import java.net.URL;
import java.util.ResourceBundle;

public class CardMonAnController implements Initializable {

    @FXML
    private Label giaTien;

    @FXML
    private Label tenMon;
    private MonAn monAn;

    private DatMonController controller;

    private ChiTietHoaDonDAO chiTietHoaDonDAO;

    public DatMonController getController() {
        return controller;
    }

    public void setController(DatMonController controller) {
        this.controller = controller;
    }

    @FXML
    public void them(){
        if(controller != null) {
            System.out.println(controller.getHoaDon());
            Object[] row = new Object[]{monAn.getMaMonAn(), monAn.getTenMonAn(), monAn.getDonGia(), 1, monAn.getDonViTinh(),""};
            controller.themDuLieuVaoBangMonAn(row, monAn);
            controller.capNhatTongTien();
        }
    }

    public void setMonAn(MonAn monAn, DatMonController controller) {
        this.monAn = monAn;
        this.controller = controller;
        tenMon.setText(monAn.getTenMonAn());
        giaTien.setText(String.valueOf(monAn.getDonGia()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chiTietHoaDonDAO = new ChiTietHoaDonDAO();
    }
}
