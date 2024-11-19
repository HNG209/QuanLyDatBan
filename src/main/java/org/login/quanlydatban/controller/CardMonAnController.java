package org.login.quanlydatban.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import org.login.quanlydatban.dao.ChiTietHoaDonDAO;
import org.login.quanlydatban.entity.ChiTietHoaDon;
import org.login.quanlydatban.entity.MonAn;
import org.login.quanlydatban.notification.Notification;
import org.login.quanlydatban.utilities.NumberFormatter;

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
            if(controller.daLapHoaDon()){
                System.out.println(controller.getHoaDon());
                Object[] row = new Object[]{monAn.getMaMonAn(), monAn.getTenMonAn(), monAn.getDonGia(), 1, monAn.getDonViTinh(),""};
                controller.themDuLieuVaoBangMonAn(row, monAn);
                controller.capNhatTongTien();
                controller.capNhatTienTraLai();
            }
            else Notification.thongBao("Hãy xác nhận giữ bàn trước khi thêm món ăn", Alert.AlertType.INFORMATION);
        }
    }

    public void setMonAn(MonAn monAn, DatMonController controller) {
        this.monAn = monAn;
        this.controller = controller;
        tenMon.setText(monAn.getTenMonAn());
        giaTien.setText(NumberFormatter.formatPrice(String.valueOf((int) monAn.getDonGia())) + "đ/ " + monAn.getDonViTinh());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chiTietHoaDonDAO = new ChiTietHoaDonDAO();
    }

}
