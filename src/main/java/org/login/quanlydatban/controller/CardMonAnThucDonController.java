package org.login.quanlydatban.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.login.quanlydatban.entity.MonAn;
import org.login.quanlydatban.utilities.NumberFormatter;

import java.io.File;

public class CardMonAnThucDonController {
    @FXML
    private ImageView anhMonDisplay;

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
        if (monAn.getHinhAnh() != null && !monAn.getHinhAnh().isEmpty()) {
            anhMonDisplay.setImage(new Image(new File(monAn.getHinhAnh()).toURI().toString()));
        } else {
            String defaultImage = "/org/login/quanlydatban/icons/empty.png";
            anhMonDisplay.setImage(new Image(getClass().getResource(defaultImage).toString()));
        }

        tenMon.setText(monAn.getTenMonAn());
        double donGia = monAn.getDonGia(); // Get the raw double value
        String formattedPrice = NumberFormatter.formatPrice(String.format("%.0f", donGia)); // Format without decimals
        giaTien.setText(formattedPrice);
    }
}
