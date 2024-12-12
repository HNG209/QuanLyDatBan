package org.login.quanlydatban.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.login.quanlydatban.entity.MonAn;
import org.login.quanlydatban.utilities.NumberFormatter;

import java.io.File;


public class CardChiTietMonAnController {
    @FXML
    private TextField donGia;

    @FXML
    private TextField tenLoaiMonAn;

    @FXML
    private TextField tenMonAn;

    @FXML
    private TextField trangThai;

    @FXML
    private ImageView image;

    private MonAn monAn;

    private DatMonController controller;

    public DatMonController getController() {
        return controller;
    }

    public void setController(DatMonController controller) {
        this.controller = controller;
    }

    public void setMonAn(MonAn monAn) {
        this.monAn = monAn;
        tenMonAn.setText(monAn.getTenMonAn());
        image.setImage(new Image(new File(monAn.getHinhAnh()).toURI().toString()));
        donGia.setText(NumberFormatter.formatPrice(String.valueOf((int) monAn.getDonGia())) + "đ/ " + monAn.getDonViTinh());
        tenLoaiMonAn.setText(monAn.getLoaiMonAn().getTenLoaiMonAn());
        trangThai.setText(monAn.getTrangThaiMonAn().toString());
    }
    @FXML
    public void thoat() {
        Stage stage = (Stage) Window.getWindows().stream()
                .filter(Window::isFocused)
                .findFirst()
                .orElse(null);
        if (stage != null) {
            stage.close();
        }
    }
}
