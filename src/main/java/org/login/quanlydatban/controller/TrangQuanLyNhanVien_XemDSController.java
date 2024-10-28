package org.login.quanlydatban.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class TrangQuanLyNhanVien_XemDSController {
    @FXML
    private Button btnthem;

    public void themNhanVien(){
        btnthem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    System.out.println("Nhan nut them");
                    // Tải giao diện từ file FXML
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/QuanLyNhanVien.fxml"));
                    Parent newWindow = loader.load();

                    // Tạo một cửa sổ mới
                    Stage stage = new Stage();
                    stage.setScene(new Scene(newWindow));
                    stage.setTitle("Quản Lý Nhân Viên");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace(); // Xử lý ngoại lệ nếu có lỗi khi tải FXML
                }
            }
        });
    }
}
