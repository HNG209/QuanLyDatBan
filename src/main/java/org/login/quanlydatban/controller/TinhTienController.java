package org.login.quanlydatban.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TinhTienController implements Initializable {


    @FXML
    private Button num0;

    @FXML
    private Button num1;

    @FXML
    private Button num2;

    @FXML
    private Button num3;

    @FXML
    private Button num4;

    @FXML
    private Button num5;

    @FXML
    private Button num6;

    @FXML
    private Button num7;

    @FXML
    private Button num8;

    @FXML
    private Button num9;

    @FXML
    private TextField tfTienGoc;

    @FXML
    private TextField tfTienKhachDua;

    @FXML
    private TextField tfTienTraLai;

    private TextField focusedField;

    @FXML
    private Button troVe;

    @FXML
    private Button xoa;

//    private static TinhTienController instance;
//
//    private static Parent root;
//
//    public static TinhTienController getInstance() throws IOException {
//        if(instance == null)
//            instance = loadTienIch();
//        return instance;
//    }
//
//    private static TinhTienController loadTienIch() throws IOException {
//        FXMLLoader loader = new FXMLLoader(ChonBanController.class.getResource("/org/login/quanlydatban/views/TrangTinhTien.fxml"));
//        root = loader.load();
//        return loader.getController();
//    }
//
//    public Parent getRoot() {
//        return root;
//    }


    private TextField getFocusedTextField(TextField... textFields) {
        for (TextField textField : textFields) {
            if (textField.isFocused()) {
                return textField;
            }
        }
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfTienGoc.setOnMouseClicked(e -> focusedField = tfTienGoc);
        tfTienKhachDua.setOnMouseClicked(e -> focusedField = tfTienKhachDua);
    }

    @FXML
    void enter(ActionEvent event) {
//        TextField focusedField = getFocusedTextField(tfTienGoc, tfTienKhachDua);
        if(focusedField != null){
            if(event.getSource().equals(num0))
                focusedField.appendText("0");
            else if (event.getSource().equals(num1))
                focusedField.appendText("1");
            else if (event.getSource().equals(num2))
                focusedField.appendText("2");
            else if (event.getSource().equals(num3))
                focusedField.appendText("3");
            else if (event.getSource().equals(num4))
                focusedField.appendText("4");
            else if (event.getSource().equals(num5))
                focusedField.appendText("5");
            else if (event.getSource().equals(num6))
                focusedField.appendText("6");
            else if (event.getSource().equals(num7))
                focusedField.appendText("7");
            else if (event.getSource().equals(num8))
                focusedField.appendText("8");
            else if (event.getSource().equals(num9))
                focusedField.appendText("9");
            else if (event.getSource().equals(xoa))
                focusedField.clear();
            else
                focusedField.setText(focusedField.getText().substring(0, focusedField.getLength() - 1));
        }
    }

    @FXML
    void xong(ActionEvent event) {
        Stage current = (Stage) tfTienGoc.getScene().getWindow();
        current.close();
    }

    @FXML
    void refresh(ActionEvent event) {
        focusedField = null;
        tfTienKhachDua.clear();
        tfTienGoc.clear();
        tfTienTraLai.clear();
    }
}
