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
import org.login.quanlydatban.dao.LoaiMonDAO;
import org.login.quanlydatban.dao.MonAnDAO;
import org.login.quanlydatban.entity.LoaiMonAn;
import org.login.quanlydatban.entity.MonAn;
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

    @FXML
    private TextField txtTenMonAn;

    @FXML
    private TextField txtGia;

    @FXML
    private TextArea txfMoTa;

    @FXML
    private TextField txtDonViTinh;

    @FXML
    private ComboBox<String> cbloaiMonAn;
    @FXML
    private ComboBox<String> cbtrangThaiMon;

    @FXML
    private ImageView anhMon;

    @FXML
    private Button btnThemMon;

    @FXML
    private Button btnXoaRong;

    private MonAnDAO monAnDAO;

    private String maMonAn;

    private String maLoai;

    private String duongDanAnh;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println();
        monAnDAO = new MonAnDAO();
        System.out.println(monAnDAO.getAllMonAn());

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


//        btnXoaRong.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                txtTenMonAn.requestFocus();
//                txtTenMonAn.setText("");
//                cbloaiMonAn.getSelectionModel().selectFirst();
//                cbtrangThaiMon.getSelectionModel().selectFirst();
//                txtDonViTinh.setText("");
//                txtGia.setText("");
//                anhMon.setImage(null);
//                txfMoTa.setText("");
//            }
//        });

        monAnDAO.getAllMonAn();
        flowPane.prefHeightProperty().bind(scrollPane.heightProperty());
        flowPane.prefWidthProperty().bind(scrollPane.widthProperty());

        for (MonAn i : monAnDAO.getAllMonAn()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardMonAn_TrangThucDon.fxml"));
            try {
                AnchorPane pane = loader.load();

                CardMonAnThucDonController controller = loader.getController();
                controller.setMonAnThucDon(i, this);

                flowPane.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //monAnDAO.getListMonAn();
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

    @FXML
    void themControl(ActionEvent event) {
        Object source = event.getSource();
        if (source == btnThemMon) {
            try {
                themMon();
                System.out.println("Thêm dc rồi, yay :D");
            }
            catch (Exception e) {
                System.out.println(e);
            }

        }
    }

    @FXML
    void xoaRongControl(ActionEvent event) {
        Object source = event.getSource();
        if (source == btnXoaRong) {
            txtTenMonAn.requestFocus();
            txtTenMonAn.setText("");
            cbloaiMonAn.getSelectionModel().selectFirst();
            cbtrangThaiMon.getSelectionModel().selectFirst();
            txtDonViTinh.setText("");
            txtGia.setText("");
            anhMon.setImage(null);
            txfMoTa.setText("");
        }
    }
//    private String generateLoaiMonAn(String prefix) {
//        Long maxId = getMaLoaiFromDatabase(prefix);
//        Long newIdNumber = (maxId == null) ? 1 : maxId + 1; // Increment ID by 1
//        return prefix + String.format("%02d", newIdNumber); // Combine prefix with formatted number
//    }
//
//    public Long getMaLoaiFromDatabase(String prefix) {
//        Session session = HibernateUtils.getFactory().openSession();
//        Long maLoai = null;
//
//        try {
//            String query = "SELECT loaiMonAn FROM MonAn WHERE loaiMonAn.maLoaiMonAn LIKE :prefix";
//            List<String> loaiMonAns = session.createQuery(query, String.class)
//                    .setParameter("prefix", prefix + "%")
//                    .getResultList();
//
//            maLoai = loaiMonAns.stream()
//                    .filter(ma -> ma.matches(prefix + "\\d{2}")) // Ensure it matches the format with the prefix
//                    .map(ma -> Long.parseLong(ma.substring(prefix.length()))) // Extract and parse the numeric part
//                    .max(Long::compare)
//                    .orElse(0L);
//
//        } catch (Exception e) {
//            e.printStackTrace(); // Replace with logger if needed
//        } finally {
//            if (session != null) {
//                session.close(); // Ensure the session is closed properly
//            }
//        }
//        return maLoai;
//    }

    private String generateMaMonAn() {
        Long maxId = getMaMonFromDatabase();
        Long newIdNumber = (maxId == null) ? 1 : maxId + 1; // Tăng mã lên 1
        return String.format("%04d", newIdNumber); // Định dạng mã
    }

//    private void generateMaMonAn1() {
//        int countRecord = 10;
//        for(int i = 0; i < 10; i++) {
//            String ma = "MA" +  String.format("%04d", i);
//            System.out.println();
//        }
//    }

    public Long getMaMonFromDatabase() {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;
        Long maMon = null;

        try {
            transaction = session.beginTransaction();

            String query = "SELECT maMonAn FROM MonAn";
            List<String> maMonAns = session.createQuery(query, String.class)
                    .getResultList();

            maMon = maMonAns.stream()
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
        return maMon;
    }


    public void themMon() {
        MonAnDAO monAnDAO = new MonAnDAO(); // DAO for MonAn
        LoaiMonDAO loaiMonDAO = new LoaiMonDAO(); // DAO for LoaiMonAn

        // Determine LoaiMonAn based on selected ComboBox value
        String loaiMonAnValue = cbloaiMonAn.getValue(); // Value from ComboBox
        LoaiMonAn loaiMon1 = null;

        // Fetch LoaiMonAn by id based on the selection
        switch (loaiMonAnValue) {
            case "TRANG_MIENG":
                loaiMon1 = loaiMonDAO.getMaLoaiMon("7");
                break;
            case "MON_HAI_SAN":
                loaiMon1 = loaiMonDAO.getMaLoaiMon("6");
                break;
            case "MON_TRUYEN_THONG":
                loaiMon1 = loaiMonDAO.getMaLoaiMon("5");
                break;
            case "NUOC_GIAI_KHAT":
                loaiMon1 = loaiMonDAO.getMaLoaiMon("4");
                break;
            case "KHAI_VI":
                loaiMon1 = loaiMonDAO.getMaLoaiMon("3");
                break;
            case "MON_XAO":
                loaiMon1 = loaiMonDAO.getMaLoaiMon("2");
                break;
            default:
                loaiMon1 = loaiMonDAO.getMaLoaiMon("1");
                break;
        }

        // Determine TrangThaiMonAn based on ComboBox value
        TrangThaiMonAn trangThaiMonAn = null;
        String trangThaiValue = cbtrangThaiMon.getValue(); // Value from ComboBox

        // Set the appropriate TrangThaiMonAn enum value
        switch (trangThaiValue) {
            case "CO SAN":
                trangThaiMonAn = TrangThaiMonAn.CO_SAN;
                break;
            case "TAM HET":
                trangThaiMonAn = TrangThaiMonAn.TAM_HET;
                break;
            default:
                trangThaiMonAn = TrangThaiMonAn.NGUNG_BAN;
                break;
        }

        // Get other input values from UI components
        String moTa = txfMoTa.getText();
        double gia = Double.parseDouble(txtGia.getText());
        String tenMonAn = txtTenMonAn.getText();
        String donViTinh = txtDonViTinh.getText();

        // Generate ID for the new MonAn
        String maMonAn = generateMaMonAn();

        // Assuming duongDanAnh is a field that holds the path of the selected image
        String duongDanAnh = "path_to_image"; // replace with actual image path

        // Create the new MonAn object
        MonAn monAn = new MonAn(maMonAn, loaiMon1, tenMonAn, gia, donViTinh, duongDanAnh, trangThaiMonAn);

        // Save MonAn to the database
        monAnDAO.themMonAn(monAn);
    }


}

