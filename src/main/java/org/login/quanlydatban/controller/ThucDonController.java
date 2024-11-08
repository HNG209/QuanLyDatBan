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
    private Button btnXoaMon;

    @FXML
    private Button btnCapNhat;


    @FXML
    private Button btnXoaRong;

    @FXML
    private Button btnRefresh;


    private MonAn monAn;

    private MonAnDAO monAnDAO;

    private LoaiMonDAO loaiMonDAO;

    private String duongDanAnh;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbloaiMonAn.getSelectionModel().selectFirst();
        cbtrangThaiMon.getSelectionModel().selectFirst();
        monAnDAO = new MonAnDAO();
        loaiMonDAO = new LoaiMonDAO();
        System.out.println(monAnDAO.getAllMonAn());

        loadLoaiMonAnComboBox();

        List<MonAn> monAnList = monAnDAO.getAllMonAn();
        if (monAnList == null) {
            showWarn("Danh sach mon an bi trong");
        }

        List<LoaiMonAn> loaiMonAnList = loaiMonDAO.getListLoai();
        if (loaiMonAnList == null) {
            showWarn("Danh sach loai mon an bi trong");
        }

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

        cbloaiMonAn.getEditor().setOnAction(actionEvent -> {
            String newValue = cbloaiMonAn.getEditor().getText();
            if (!newValue.isEmpty() && !cbloaiMonAn.getItems().contains(newValue)) {
                cbloaiMonAn.getItems().add(newValue);
                cbloaiMonAn.setValue(newValue); // Set the new value as selected
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
                controller.setController(this);
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
                if (!regexGia()) {
                    showWarn("Bạn cần nhập đúng thông tin!");
                } else {

                    themMon();
                    refreshControl(event);
                    loadLoaiMonAnComboBox();
                    System.out.println("Thêm dc rồi, yay :D");
                }

            }
            catch (Exception e) {
                System.out.println(e);
            }

        }
    }

    @FXML
    void xoaControl(ActionEvent event) {
        Object source = event.getSource();
        if (source == btnXoaMon) {
            try{
                if (monAn == null) {
                    showWarn("Bạn cần chọn một món để xóa!");
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("XÁC NHẬN XÓA");
                    alert.setHeaderText("Bạn có chắc chắn muốn xóa món này?");

                    ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
                    if (result == ButtonType.OK) {
                        monAnDAO.xoaMonAn(monAn.getMaMonAn());
                        showWarn("Đã xóa thành công!");
                        refreshControl(event);
                        xoaRongControl(event);
                    }
                    else {
                        System.out.println("Delete cancel");
                    }

                }
            } catch (Exception e) {
                System.out.println(e);
            }

        }
    }

    @FXML
    void capNhatControl(ActionEvent event) {
        MonAn monMoi = new MonAn();
        LoaiMonDAO loaiMonDAO = new LoaiMonDAO();
        Object source = event.getSource();
        if (source == btnCapNhat) {
            try{
                if (monAn == null) {
                    showWarn("Bạn cần chọn một món để cập nhật!");
                }
                else {
                    String tenMonMoi = txtTenMonAn.getText();
                    String donViMoi = txtDonViTinh.getText();
                    double giaMoi = Double.parseDouble(txtGia.getText());
                    TrangThaiMonAn trangThaiMoi = comboTTValue();
                    String loaiMonMoiName = cbloaiMonAn.getValue();

                    LoaiMonAn loaiMonMoi = loaiMonDAO.getLoaiMonByName(loaiMonMoiName);

                    // If LoaiMonAn doesn't exist, add it
                    if (loaiMonMoi == null) {
                        themLoaiMon(); // This will add the new LoaiMonAn
                        loaiMonMoi = loaiMonDAO.getLoaiMonByName(loaiMonMoiName); // Retrieve the newly added LoaiMonAn
                    }

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("XÁC NHẬN CẬP NHẬT");
                    alert.setHeaderText("Bạn có chắc chắn muốn cập nhật món này?");

                    ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

                    if (result == ButtonType.OK) {
                        monMoi = new MonAn(monAn.getMaMonAn(), loaiMonMoi, tenMonMoi, giaMoi, donViMoi, duongDanAnh, trangThaiMoi);
                        monAnDAO.capNhatMonAn(monAn, monMoi);
                        showWarn("Đã cập nhật thành công!");
                        refreshControl(event);

                    }
                    else {
                        System.out.println("Update cancel");
                    }

                }
            } catch (Exception e) {
                System.out.println(e);
            }

        }
    }

    @FXML
    void xoaRongControl(ActionEvent event) {
        Image imageXoaRong = new Image(getClass().getResource("/org/login/quanlydatban/icons/restaurant.png").toExternalForm());

        Object source = event.getSource();
        if (source == btnXoaRong || source == btnXoaMon) {
            txtTenMonAn.requestFocus();
            txtTenMonAn.setText("");
            cbloaiMonAn.getSelectionModel().selectFirst();
            cbtrangThaiMon.getSelectionModel().selectFirst();
            txtDonViTinh.setText("");
            txtGia.setText("");
            anhMon.setImage(imageXoaRong);
            txfMoTa.setText("");
        }
    }

    @FXML
    void refreshControl(ActionEvent event) {
        Object source = event.getSource();
        if (source == btnRefresh || source == btnThemMon || source == btnXoaMon || source == btnCapNhat) {
            flowPane.getChildren().clear(); // Clear existing items

            List<MonAn> monAnList = monAnDAO.getAllMonAn(); // Retrieve the latest data from the database

            for (MonAn monAn : monAnList) {
                try {
                    // Load the card UI for each item
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardMonAn_TrangThucDon.fxml"));
                    AnchorPane pane = loader.load();

                    // Set up the card controller with the MonAn data
                    CardMonAnThucDonController controller = loader.getController();
                    controller.setMonAnThucDon(monAn, this);

                    // Add the card to the flowPane
                    flowPane.getChildren().add(pane);
                } catch (IOException e) {
                    e.printStackTrace(); // Log the error
                }
            }
        }
    }

    private void loadLoaiMonAnComboBox() {
        cbloaiMonAn.getItems().clear();  // Clear current items to avoid duplicates
        List<LoaiMonAn> loaiMonAnList = loaiMonDAO.getListLoai();

        if (loaiMonAnList != null) {
            for (LoaiMonAn loaiMonAn : loaiMonAnList) {
                cbloaiMonAn.getItems().add(loaiMonAn.getTenLoaiMonAn());
            }
        } else {
            showWarn("Danh sách LoaiMonAn rỗng.");
        }
    }

    private String generateLoaiMonAn() {
        Long maxId = getMaMonFromDatabase();
        Long newIdNumber = (maxId == null) ? 1 : maxId + 1; // Tăng mã lên 1
        return String.format("%02d", newIdNumber); // Định dạng mã
    }

    public Long getMaLoaiFromDatabase() {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;
        Long maLoaiMon = null;

        try {
            transaction = session.beginTransaction();

            String query = "SELECT maLoaiMonAn FROM LoaiMonAn ";
            List<String> maMonAns = session.createQuery(query, String.class)
                    .getResultList();

            maLoaiMon = maMonAns.stream()
                    .filter(ma -> ma.matches("\\d{2}")) // Ensure only 4-digit numbers are processed
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
        return maLoaiMon;
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

    //
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



    //REGEX
    public boolean regexGia(){
        String rgia = txtGia.getText();
        try {
            double gia = Double.parseDouble(rgia);
            if (gia > 0) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showWarn(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Kết Quả");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    //CRUD
    public void themMon() {
        MonAnDAO monAnDAO = new MonAnDAO(); // DAO for MonAn

        String selectedLoaiMon = cbloaiMonAn.getValue(); // Get the selected or entered value
        LoaiMonAn loaiMon = loaiMonDAO.getLoaiMonByName(selectedLoaiMon);

         //If it doesn't exist, add it
        if (loaiMon == null) {
            themLoaiMon(); // Adds the new LoaiMonAn
            loaiMon = loaiMonDAO.getLoaiMonByName(selectedLoaiMon); // Retrieve the newly added LoaiMonAn
        }

        System.out.println(loaiMon.getMaLoaiMonAn());

        TrangThaiMonAn ttMonAn = comboTTValue();

        // Get other input values from UI components

        double gia = Double.parseDouble(txtGia.getText());
        String tenMonAn = txtTenMonAn.getText();
        String donViTinh = txtDonViTinh.getText();

        // Generate ID for the new MonAn
        String maMonAn = generateMaMonAn();

        // Assuming duongDanAnh is a field that holds the path of the selected image
        String duongDanAnh = "path_to_image"; // replace with actual image path

        // Create the new MonAn object
        MonAn monAn = new MonAn(maMonAn, loaiMon, tenMonAn, gia, donViTinh, duongDanAnh, ttMonAn);

        // Save MonAn to the database
        monAnDAO.themMonAn(monAn);
    }

    public void themLoaiMon () {
        LoaiMonDAO loaiMonDAO = new LoaiMonDAO();
        String maLoai = generateLoaiMonAn();
        String tenLoai = cbloaiMonAn.getValue();
        String moTa = txfMoTa.getText();

        LoaiMonAn loaiMonAn = new LoaiMonAn(maLoai, tenLoai, moTa);
        loaiMonDAO.themLoaiMonAn(loaiMonAn);

    }

//    public LoaiMonAn comboLoaiMonValue () {
//        LoaiMonDAO loaiMonDAO = new LoaiMonDAO(); // DAO for LoaiMonAn
//        // Determine LoaiMonAn based on selected ComboBox value
//        String loaiMonAnValue = cbloaiMonAn.getValue(); // Value from ComboBox
//        LoaiMonAn loaiMon1 = null;
//
//        // Fetch LoaiMonAn by id based on the selection
//        return LoaiMon1;
//    }


    public TrangThaiMonAn comboTTValue() {
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
        return trangThaiMonAn;
    }

    public void setMonAn(MonAn monAn) {
        this.monAn = monAn;
        String ma = monAn.getMaMonAn();
        txtTenMonAn.setText(monAn.getTenMonAn());

        // Setting the corresponding LoaiMonAn name in ComboBox
        LoaiMonAn loaiMon = monAn.getLoaiMonAn();
        if (loaiMon != null) {
            cbloaiMonAn.setValue(loaiMon.getTenLoaiMonAn());
        } else {
            cbloaiMonAn.getSelectionModel().clearSelection();
        }

        cbtrangThaiMon.setValue(String.valueOf(monAn.getTrangThaiMonAn()));
        txtDonViTinh.setText(monAn.getDonViTinh());
        txtGia.setText(String.valueOf(monAn.getDonGia()));
    }

}

