package org.login.quanlydatban.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class TrangQuanLyNhanVienController {
    @FXML
    private Button btnTaiAnh;

    @FXML
    public  void taiAnh(){
        btnTaiAnh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Chọn ảnh");
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg"));


                // Mở hộp thoại và chờ người dùng chọn file
                int userSelection = fileChooser.showOpenDialog(null);

                // Kiểm tra xem người dùng đã chọn file chưa
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToOpen = fileChooser.getSelectedFile();
                    String filePath = fileToOpen.getAbsolutePath(); // Lấy đường dẫn file

                    // Thêm đường dẫn vào JComboBox

                }
            }
        });
    }
}

