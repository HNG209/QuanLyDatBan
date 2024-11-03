package org.login.quanlydatban.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import org.login.quanlydatban.dao.MonAnDAO;
import org.login.quanlydatban.entity.MonAn;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ThucDonController implements Initializable {
    @FXML
    private FlowPane flowPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TableView<?> orderTable;

    private MonAnDAO monAnDAO;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

//        monAnDAO = new MonAnDAO();
//        monAnDAO.readMonAn();
//        flowPane.prefHeightProperty().bind(scrollPane.heightProperty());
//        flowPane.prefWidthProperty().bind(scrollPane.widthProperty());
//
//        for (MonAn i : monAnDAO.getListMonAn()){
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardMonAn2.fxml"));
//            try {
//                AnchorPane pane = loader.load();
//
//                CardMonAnController controller = loader.getController();
//                controller.setMonAn(i);
//
//                flowPane.getChildren().add(pane);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
////        for (int i = 0; i < 20; i++){
////            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardMonAn.fxml"));
////            try {
////                AnchorPane pane = loader.load();
////
////                CardMonAnController controller = loader.getController();
////                controller.setIndex(i);
////
////                flowPane.getChildren().add(pane);
////            } catch (IOException e) {
////                throw new RuntimeException(e);
////            }
////        }
//
//        monAnDAO.getListMonAn();
////        scrollPane.vvalueProperty().addListener((obs, oldValue, newValue) -> {
////            if(newValue.doubleValue() == 1.0){
////                for (int i = 0; i < 20; i++){
////                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardMonAn.fxml"));
////                    try {
////                        AnchorPane pane = loader.load();
////                        flowPane.getChildren().add(pane);
////                    } catch (IOException e) {
////                        throw new RuntimeException(e);
////                    }
////                }
////
////            }
////        });
    }


}
