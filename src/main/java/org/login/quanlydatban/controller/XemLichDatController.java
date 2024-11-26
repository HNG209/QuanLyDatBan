package org.login.quanlydatban.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class XemLichDatController implements Initializable {

    @FXML
    private VBox boxChieu;

    @FXML
    private VBox boxChieuCN;

    @FXML
    private VBox boxChieuT2;

    @FXML
    private VBox boxChieuT3;

    @FXML
    private VBox boxChieuT4;

    @FXML
    private VBox boxChieuT5;

    @FXML
    private VBox boxChieuT6;

    @FXML
    private VBox boxChieuT7;

    @FXML
    private VBox boxSang;

    @FXML
    private VBox boxSangCN;

    @FXML
    private VBox boxSangT2;

    @FXML
    private VBox boxSangT3;

    @FXML
    private VBox boxSangT4;

    @FXML
    private VBox boxSangT5;

    @FXML
    private VBox boxSangT6;

    @FXML
    private VBox boxSangT7;

    @FXML
    private VBox boxToi;

    @FXML
    private VBox boxToiCN;

    @FXML
    private VBox boxToiT2;

    @FXML
    private VBox boxToiT3;

    @FXML
    private VBox boxToiT4;

    @FXML
    private VBox boxToiT5;

    @FXML
    private VBox boxToiT6;

    @FXML
    private VBox boxToiT7;

    @FXML
    private Label labelChieu;

    @FXML
    private Label labelSang;

    @FXML
    private Label labelToi;


    @FXML
    private Label t2CurrentDate;

    @FXML
    private Label t3CurrentDate;

    @FXML
    private Label t4CurrentDate;

    @FXML
    private Label t5CurrentDate;

    @FXML
    private Label t6CurrentDate;

    @FXML
    private Label t7CurrentDate;

    @FXML
    private Label cnCurrentDate;


    private static XemLichDatController instance;

    private static Parent root;

    public static XemLichDatController getInstance() throws IOException {
        if(instance == null)
            instance = loadLich();
        return instance;
    }

    private static XemLichDatController loadLich() throws IOException {
        FXMLLoader loader = new FXMLLoader(ChonBanController.class.getResource("/org/login/quanlydatban/views/TrangXemLichDat.fxml"));
        root = loader.load();
        return loader.getController();
    }

    public Parent getRoot() {
        return root;
    }

    public void assignDateToColumn(LocalDate date) {
        switch (date.getDayOfWeek()) {
            case MONDAY -> t2CurrentDate.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            case TUESDAY -> t3CurrentDate.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            case WEDNESDAY -> t4CurrentDate.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            case THURSDAY -> t5CurrentDate.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            case FRIDAY -> t6CurrentDate.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            case SATURDAY -> t7CurrentDate.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            default -> cnCurrentDate.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
    }

    public void refreshCurrentWeek() {
        LocalDate today = LocalDate.now();
        assignDateToColumn(today);

        for(int i = today.getDayOfWeek().getValue() - 1; i > 0; i--) {
            assignDateToColumn(today.minusDays(i));
        }

        for(int i = today.getDayOfWeek().getValue() + 1; i < 8; i++) {
            assignDateToColumn(today.plusDays(i - today.getDayOfWeek().getValue()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshCurrentWeek();

        VBox.setVgrow(labelSang, Priority.ALWAYS);
        labelSang.prefHeightProperty().bind(boxSang.heightProperty());

        VBox.setVgrow(labelChieu, Priority.ALWAYS);
        labelChieu.prefHeightProperty().bind(boxChieu.heightProperty());

        VBox.setVgrow(labelToi, Priority.ALWAYS);
        labelToi.prefHeightProperty().bind(boxToi.heightProperty());

        for (int i = 0; i < 10; i++) {
            FXMLLoader loader = new FXMLLoader(ChonBanController.class.getResource("/org/login/quanlydatban/uicomponents/CardLich_TrangXemLich.fxml"));
            try {
                AnchorPane pane = loader.load();
                boxSangT2.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
