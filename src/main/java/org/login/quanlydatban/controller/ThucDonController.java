package org.login.quanlydatban.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.login.quanlydatban.dao.BanDAO;
import org.login.quanlydatban.dao.MonAnDAO;
import org.login.quanlydatban.entity.MonAn;
import org.login.quanlydatban.hibernate.HibernateUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ThucDonController implements Initializable {
    @FXML
    private FlowPane flowPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button taiAnh;

    @FXML
    private TableView<?> orderTable;

    private MonAnDAO monAnDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        monAnDAO = new MonAnDAO();

         taiAnh.setOnAction(new EventHandler<ActionEvent>() {
             @Override
             public void handle(ActionEvent actionEvent) {
                 System.out.println("Nhan nut tai anh");
                 FileChooser fileChooser = new FileChooser();

                 fileChooser.setInitialDirectory(new File("..\\org\\login\\quanlydatban\\Image"));
                 fileChooser.setTitle("Mở file");

                 // Thiết lập bộ lọc file nếu cần
                 FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif");
                 fileChooser.getExtensionFilters().add(extFilter);

                 File file = fileChooser.showOpenDialog(null);
                 System.out.println("Nhấn nút tải ảnh");
             }
         });

        monAnDAO.readAll();
        flowPane.prefHeightProperty().bind(scrollPane.heightProperty());
        flowPane.prefWidthProperty().bind(scrollPane.widthProperty());

        for (MonAn i : monAnDAO.getListMonAn()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardMonAn_TrangThucDon.fxml"));
            try {
                AnchorPane pane = loader.load();

                CardMonAnController controller = loader.getController();
                controller.setMonAnThucDon(i, this);

                flowPane.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        monAnDAO.getListMonAn();
//        scrollPane.vvalueProperty().addListener((obs, oldValue, newValue) -> {
//            if(newValue.doubleValue() == 1.0){
//                for (int i = 0; i < 20; i++){
//                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardMonAn_TrangDatMon.fxml"));
//                    try {
//                        AnchorPane pane = loader.load();
//                        flowPane.getChildren().add(pane);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//
//            }
//        });
    }

    // nut tai anh


    public void themMonAn() {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

      //      session.save(monAnDAO.getListMonAn());
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


}
