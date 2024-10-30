package org.login.quanlydatban.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TrangQuanLyNhanVienController implements Initializable {
    @FXML
    private Button btnTaiAnh;
    @FXML
    private ImageView image1;
    private String duongdananh;

    @FXML
    public  void taiAnh(){
        btnTaiAnh = new Button();
        btnTaiAnh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Nhan nut tai anh");
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialDirectory(new File("E:\\QuanLyDatBanNhom2\\QuanLyDatBan\\src\\main\\resources\\org\\login\\quanlydatban\\Image"));
                fileChooser.setTitle("Mở file");

                // Thiết lập bộ lọc file nếu cần
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif");
                fileChooser.getExtensionFilters().add(extFilter);

                File file = fileChooser.showOpenDialog(null);
                System.out.println("Nhấn nút tải ảnh");

                if (file != null) {
                    duongdananh = file.getAbsolutePath(); // Cập nhật đường dẫn
                    // Cập nhật ImageView với ảnh mới
                    Image image = new Image(file.toURI().toString());
                    image1.setImage(image);
                }

            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

