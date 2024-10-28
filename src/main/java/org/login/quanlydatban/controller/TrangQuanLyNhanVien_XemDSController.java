package org.login.quanlydatban.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TrangQuanLyNhanVien_XemDSController implements Initializable {
    @FXML
    private Button btnthem;
    @FXML
    private ComboBox<String> cbxTimKiem;


    // them nhan vien
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

    // load du lieu len combobox tim kiem, loc theo tieu chi@
    ObservableList<String> list = FXCollections.observableArrayList("Theo ID","Họ tên","Ngày sinh","Địa chỉ");
    ObservableList<String> list1 = FXCollections.observableArrayList("Theo ID","Họ tên","Ngày sinh","Địa chỉ");


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void onselected1(ActionEvent event){

    }
}
