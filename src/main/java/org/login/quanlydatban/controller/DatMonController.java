package org.login.quanlydatban.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DatMonController implements Initializable {
    @FXML
    private FlowPane flowPane;

    @FXML
    private ScrollPane scrollPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        flowPane.prefHeightProperty().bind(scrollPane.heightProperty());
        flowPane.prefWidthProperty().bind(scrollPane.widthProperty());

        for (int i = 0; i < 30; i++){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/TestPane.fxml"));
            try {
                AnchorPane pane = loader.load();
                flowPane.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        scrollPane.vvalueProperty().addListener((obs, oldValue, newValue) -> {
            if(newValue.doubleValue() == 1.0){
                for (int i = 0; i < 30; i++){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/TestPane.fxml"));
                    try {
                        AnchorPane pane = loader.load();
                        flowPane.getChildren().add(pane);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });
    }
}
