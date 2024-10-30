package org.login.quanlydatban.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class CardBanController {
    @FXML
    private Label khuVuc;

    @FXML
    private Label loaiBan;

    @FXML
    private Label maBan;

    @FXML
    private AnchorPane anchorPane;

    private int index;
    @FXML
    void chonBan(ActionEvent event) throws IOException {
        if(anchorPane.getParent().getParent().getParent().getParent().getParent().getParent() instanceof BorderPane){
            BorderPane pane = (BorderPane) anchorPane.getParent().getParent().getParent().getParent().getParent().getParent();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangDatMon.fxml"));
            AnchorPane anchorPane = loader.load();
            DatMonController controller = loader.getController();

            controller.setData(index);

            pane.setCenter(anchorPane);
        }
    }

    public void setIndex(int index){
        this.index = index;
    }
    @FXML
    void chuyenBan(ActionEvent event) {
        System.out.println("chuyen");
    }
}
