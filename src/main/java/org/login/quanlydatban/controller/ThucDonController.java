package org.login.quanlydatban.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.login.quanlydatban.dao.MonAnDAO;
import org.login.quanlydatban.entity.LoaiMonAn;
import org.login.quanlydatban.entity.MonAn;
import org.login.quanlydatban.entity.enums.LoaiMonEnum;
import org.login.quanlydatban.entity.enums.TrangThaiMonAn;
import org.login.quanlydatban.hibernate.HibernateUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
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

    private String maMonAn;

    private String duongDanAnh;

    @FXML
    private TextField tenMonAn;

    @FXML
    private TextField giatxt;

    @FXML
    private TextField moTa;

    @FXML
    private TextField donViTinh;

    @FXML
    private ComboBox<String> loaiMonAn;
    @FXML
    private ComboBox<String> trangThaiMon;

    @FXML
    private ImageView anhMon;

    private String generateMaMonAn() {
        Long maxId = getMaxIdFromDatabase();
        Long newIdNumber = (maxId == null) ? 1 : maxId + 1; // Tăng mã lên 1
        return String.format("%04d", newIdNumber); // Định dạng mã
    }

    public Long getMaxIdFromDatabase() {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;
        Long maxId = null;

        try {
            transaction = session.beginTransaction();

            String query = "SELECT maMonAn FROM MonAn";
            List<String> maMonAns = session.createQuery(query, String.class)
                    .getResultList();

            maxId = maMonAns.stream()
                    .filter(ma -> ma.matches("\\d{4}")) // Ensure only 4-digit numbers are processed
                    .map(Long::parseLong)
                    .max(Long::compare)
                    .orElse(0L);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace(); // Consider using a logger for better error handling
        } finally {
            if (session != null) {
                session.close(); // Ensure the session is closed properly
            }
        }
        return maxId;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        monAnDAO = new MonAnDAO();

         taiAnh.setOnAction(new EventHandler<ActionEvent>() {
             @Override
             public void handle(ActionEvent actionEvent) {
                 System.out.println("Nhan nut tai anh");
                 FileChooser fileChooser = new FileChooser();

                 fileChooser.setInitialDirectory(new File("../QuanLyDatBan/src/main/resources/org/login/quanlydatban/Image"));
                 fileChooser.setTitle("Mở file");

                 // Thiết lập bộ lọc file nếu cần
                 FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif");
                 fileChooser.getExtensionFilters().add(extFilter);

                 File file = fileChooser.showOpenDialog(null);
                 System.out.println("Nhấn nút tải ảnh");

                 if (file != null) {
                     duongDanAnh = file.getAbsolutePath();
                     duongDanAnh = file.getAbsolutePath(); // Cập nhật đường dẫn
                     // Cập nhật ImageView với ảnh mới
                     Image image = new Image(file.toURI().toString());
                     anhMon.setImage(image);
                 }
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

    // Nut lay du lieu

    public void layDuLieu(){
        LoaiMonAn loaiMon1 = null;
        TrangThaiMonAn trangThaiMonAn = null;
        double gia = Double.parseDouble(giatxt.getText());
        if(loaiMonAn.getValue().equals("MÓN CHIÊN GIÒN")){
            loaiMon1.setTenLoaiMonAn(LoaiMonEnum.MON_CHIEN_GION);
        }
        else if(loaiMonAn.getValue().equals("MÓN TRÁNG MIỆNG"))
        {
            loaiMon1.setTenLoaiMonAn(LoaiMonEnum.TRANG_MIENG);
        }
        else if(loaiMonAn.equals("KHAI VỊ"))
        {
            loaiMon1.setTenLoaiMonAn(LoaiMonEnum.KHAI_VI);
        }
        else if(loaiMonAn.equals("NƯỚC GIẢI KHÁT"))
        {
            loaiMon1.setTenLoaiMonAn(LoaiMonEnum.NUOC_GIAI_KHAT);
        }
        else if(loaiMonAn.equals("MÓN XÀO"))
        {
            loaiMon1.setTenLoaiMonAn(LoaiMonEnum.MON_XAO);
        }
        else if(loaiMonAn.equals("MÓN HẢI SẢN"))
        {
            loaiMon1.setTenLoaiMonAn(LoaiMonEnum.MON_HAI_SAN);
        }
        else{
            loaiMon1.setTenLoaiMonAn(LoaiMonEnum.MON_TRUYEN_THONG);
        }

        if(trangThaiMon.equals("CO_SAN")){
            trangThaiMonAn = TrangThaiMonAn.CO_SAN;
        }
        else if(trangThaiMon.equals("TAM_HET"))
        {
            trangThaiMonAn = TrangThaiMonAn.TAM_HET;
        }
        else {
            trangThaiMonAn = TrangThaiMonAn.NGUNG_BAN;
        }

        MonAn monAn = new MonAn(maMonAn, loaiMon1, tenMonAn.getText().toString(), gia,  donViTinh.getText().toString(), duongDanAnh,
                trangThaiMonAn);
        MonAnDAO mad = new MonAnDAO();
        mad.themMonAn(monAn);
    }



}

