package org.login.quanlydatban.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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
    private ImageView btnRefresh;

    @FXML
    private TextField txtTenMonAn;

    @FXML
    private TextField txtGia;

    @FXML
    private TextArea txfMoTa;

    @FXML
    private TextField txtDonViTinh;

    @FXML
    private TextField txtTimKiem;


    @FXML
    private ComboBox<String> cbTimLoaiMon;

    @FXML
    private ComboBox<String> cbSapXep;

    @FXML
    private ComboBox<String> cbloaiMonAn;
    @FXML
    private ComboBox<String> cbtrangThaiMon;

    @FXML
    private ImageView anhMon;

    @FXML
    private Button btnThemMon;

//    @FXML
//    private Button btnXoaMon;

    @FXML
    private Button btnCapNhat;


    @FXML
    private Button btnXoaRong;

//    @FXML
//    private Button btnTimKiem;
//
//    @FXML
//    private Button btnRefresh;


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
        ObservableList<String> sharedList = FXCollections.observableArrayList();
        cbloaiMonAn.setItems(sharedList);
        cbTimLoaiMon.setItems(sharedList);
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

                fileChooser.setInitialDirectory(new File("../QuanLyDatBan/src/main/resources/org/login/quanlydatban/ImageFood"));
                fileChooser.setTitle("Mở file");

                // Thiết lập bộ lọc file nếu cần
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif");
                fileChooser.getExtensionFilters().add(extFilter);

                File file = fileChooser.showOpenDialog(null);
                System.out.println("Nhấn nút tải ảnh");

                if (file != null) {
                    //duongDanAnh = file.getAbsolutePath();
                    duongDanAnh = file.getAbsolutePath(); // Cập nhật đường dẫn
                    // Cập nhật ImageView với ảnh mới
                    Image image = new Image(file.toURI().toString());
                    anhMon.setImage(image);
                }
            }
        });

//        btnTimKiem.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                String s = txtTimKiem.getText().trim();
//                if (cbChiMuc.getSelectionModel().isEmpty()) {
//                    showWarn("Bạn cần chọn một chỉ mục trước khi tìm kiếm");
//                }
//                else if (cbChiMuc.getSelectionModel().getSelectedIndex() == 0) {
//                    if (txtTimKiem.getText().trim().isEmpty()) {
//                        showWarn("Vui lòng điền tên muốn tìm");
//                    } else {
//                        timKiemTheoTen(s);
//                    }
//                }
//                else if (cbChiMuc.getSelectionModel().getSelectedIndex() == 1) {
//                    if (txtTimKiem.getText().trim().isEmpty()) {
//                        showWarn("Vui lòng điền tên muốn tìm");
//                    } else {
//                        timKiemTheoLoai(s);
//                    }
//                }
//                else if (cbChiMuc.getSelectionModel().getSelectedIndex() == 2) {
//                    if (cbSapXep.getSelectionModel().isEmpty()){
//                        showWarn("Vui lòng chọn cách sắp xếp");
//                    }
//                    else {
//                        if (cbSapXep.getSelectionModel().getSelectedIndex() == 0) {
//                            ascendingSorting();
//                        }
//                        else {
//                            descendingSorting();
//                        }
//                    }
//                }
//
//            }
//        });

        cbloaiMonAn.getEditor().setOnAction(actionEvent -> {
            String newValue = cbloaiMonAn.getEditor().getText();
            if (!newValue.isEmpty() && !sharedList.contains(newValue)) {
                sharedList.add(newValue);
                cbloaiMonAn.setValue(newValue); // Set the new value as selected
            }
        });

        cbTimLoaiMon.getEditor().setOnAction(actionEvent -> {
            String newValue = cbTimLoaiMon.getEditor().getText();
            if (!newValue.isEmpty() && !sharedList.contains(newValue)) {
                sharedList.add(newValue);
                cbTimLoaiMon.setValue(newValue); // Set the new value as selected
            }
        });

        monAnDAO.getAllMonAn();
        flowPane.prefHeightProperty().bind(scrollPane.heightProperty());
        flowPane.prefWidthProperty().bind(scrollPane.widthProperty());

        for (MonAn i : monAnDAO.getAllMonAn()) {
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
                if (txtTenMonAn.getText() == null || txtDonViTinh.getText() == null || txtGia.getText() == null ||
                        cbloaiMonAn.getValue() == null ||
                        Objects.equals(txtTenMonAn.getText(), "") ||
                        Objects.equals(txtDonViTinh.getText(), "") ||
                        Objects.equals(txtGia.getText(), "") ||
                        Objects.equals(cbloaiMonAn.getValue(), "")) {
                    showWarn("Bạn cần nhập đầy đủ thông tin!");
                } else if (!regexGia()) {
                    showWarn("Bạn cần nhập đúng thông tin!");
                } else {

                    themMon();
                    refreshControl(event);
                    loadLoaiMonAnComboBox();
                    System.out.println("Thêm dc rồi, yay :D");
                }

            } catch (Exception e) {
                System.out.println(e);
            }

        }
    }

//    @FXML
//    void xoaControl(ActionEvent event) {
//        Object source = event.getSource();
//        if (source == btnXoaMon) {
//            try{
//                if (monAn == null) {
//                    showWarn("Bạn cần chọn một món để xóa!");
//                }
//                else {
//                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//                    alert.setTitle("XÁC NHẬN XÓA");
//                    alert.setHeaderText("Bạn có chắc chắn muốn xóa món này?");
//
//                    ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
//                    if (result == ButtonType.OK) {
//                        monAnDAO.xoaMonAn(monAn.getMaMonAn());
//                        showWarn("Đã xóa thành công!");
//                        refreshControl(event);
//                        xoaRongControl(event);
//                    }
//                    else {
//                        System.out.println("Delete cancel");
//                    }
//
//                }
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//
//        }
//    }

    @FXML
    void capNhatControl(ActionEvent event) {
        MonAn monMoi = new MonAn();
        LoaiMonDAO loaiMonDAO = new LoaiMonDAO();
        Object source = event.getSource();
        if (source == btnCapNhat) {
            try {
                if (monAn == null) {
                    showWarn("Bạn cần chọn một món để cập nhật!");
                } else {
                    String tenMonMoi = txtTenMonAn.getText();
                    String donViMoi = txtDonViTinh.getText();
                    double giaMoi = Double.parseDouble(txtGia.getText());
                    TrangThaiMonAn trangThaiMoi = comboTTValue();

                    String anhMoi = anhMon.getImage().getUrl();
                    if (anhMoi.startsWith("file:")) {
                        Path path = Paths.get(URI.create(anhMoi)); // Convert the URI to a Path
                        anhMoi = path.toString(); // Get the standard file path
                    }

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
                        monMoi = new MonAn(monAn.getMaMonAn(), loaiMonMoi, tenMonMoi, giaMoi, donViMoi, anhMoi, trangThaiMoi);
                        monAnDAO.capNhatMonAn(monAn, monMoi);
                        showWarn("Đã cập nhật thành công!");
                        refreshControl(event);

                    } else {
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
        if (source == btnXoaRong) {
            setMonAn(null);
            txtTenMonAn.requestFocus();
            txtTenMonAn.setText("");
            cbloaiMonAn.getSelectionModel().clearSelection();
            cbloaiMonAn.setValue("");
            cbtrangThaiMon.getSelectionModel().selectFirst();
            txtDonViTinh.setText("");
            txtGia.setText("");
            anhMon.setImage(imageXoaRong);
            txfMoTa.setText("");
        }
    }

    @FXML
    void btnTimKiem(MouseEvent event) {
        String keyword = txtTimKiem.getText().trim();
        String selectedType = cbTimLoaiMon.isEditable() ? cbTimLoaiMon.getEditor().getText().trim() : cbTimLoaiMon.getValue();
        Integer sortOption = cbSapXep.getSelectionModel().getSelectedIndex();

        if (cbTimLoaiMon.getValue() == null && keyword.isEmpty() && cbSapXep.getSelectionModel().isEmpty()) {
            showWarn("Bạn cần nhập/chọn một trong các cách tìm trước khi tiến hành tìm kiếm");
        }
        else {
            // Clear the previous list
            flowPane.getChildren().clear();
            flowPane.prefHeightProperty().bind(scrollPane.heightProperty());
            flowPane.prefWidthProperty().bind(scrollPane.widthProperty());

            // Fetch all items
            List<MonAn> allItems = monAnDAO.getAllMonAn();

            // Filter items based on criteria
            List<MonAn> filteredItems = allItems.stream()
                    .filter(item ->
                            (keyword.isEmpty() || item.getTenMonAn().toLowerCase().contains(keyword.toLowerCase())) &&
                                    (selectedType == null || item.getLoaiMonAn().getTenLoaiMonAn().toLowerCase().contains(selectedType.toLowerCase()))
                    )
                    .collect(Collectors.toList());

            // Sort items if a sort option is selected
            if (sortOption == 0) {
                filteredItems.sort(Comparator.comparing(MonAn::getDonGia).thenComparing(MonAn::getTenMonAn));
            } else {
                filteredItems.sort(Comparator.comparing(MonAn::getDonGia).reversed().thenComparing(MonAn::getTenMonAn));
            }

            // Display items
            filteredItems.forEach(this::addItemToFlowPane);

            // Show warning if no items are found
            if (filteredItems.isEmpty()) {
                showWarn("Không tìm thấy món ăn phù hợp với tiêu chí tìm kiếm!");
            }
      }


//        else if (cbTimLoaiMon.getSelectionModel().getSelectedIndex() == 0) {
//            if (txtTimKiem.getText().trim().isEmpty()) {
//                showWarn("Vui lòng điền tên muốn tìm");
//            } else {
//                timKiemTheoTen(s);
//            }
//        }
//        else if (cbTimLoaiMon.getSelectionModel().getSelectedIndex() == 1) {
//            if (txtTimKiem.getText().trim().isEmpty()) {
//                showWarn("Vui lòng điền tên muốn tìm");
//            } else {
//                timKiemTheoLoai(s);
//            }
//        }
//        else if (cbTimLoaiMon.getSelectionModel().getSelectedIndex() == 2) {
//            if (cbSapXep.getSelectionModel().isEmpty()){
//                showWarn("Vui lòng chọn cách sắp xếp");
//            }
//            else {
//                if (cbSapXep.getSelectionModel().getSelectedIndex() == 0) {
//                    ascendingSorting();
//                }
//                else {
//                    descendingSorting();
//                }
//            }
//        }

}

//    //FINDING
//    void timKiemTheoTen (String tenMonAn) {
//        monAnDAO.getAllMonAn();
//            for (MonAn i : monAnDAO.getAllMonAn()){
//                if (i.getTenMonAn().toLowerCase().contains(tenMonAn.toLowerCase())) {
//                    addItemToFlowPane(i);
//                }
//            }
//
//    }
//
//    void timKiemTheoLoai(String loaiMonAn) {
//        List<MonAn> allItems = monAnDAO.getAllMonAn(); // Fetch all items once
//        for (MonAn item : allItems) {
//            if (item.getLoaiMonAn() != null
//                    && item.getLoaiMonAn().getTenLoaiMonAn().toLowerCase().contains(loaiMonAn.toLowerCase())) {
//                addItemToFlowPane(item);
//            }
//        }
//    }
//
//
//    void ascendingSorting () {
//        List<MonAn> sapXepMon = monAnDAO.getAllMonAn();
////        flowPane.getChildren().clear();
////        flowPane.prefHeightProperty().bind(scrollPane.heightProperty());
////        flowPane.prefWidthProperty().bind(scrollPane.widthProperty());
//
//        sapXepMon.sort(Comparator.comparing(MonAn::getDonGia).thenComparing(MonAn::getTenMonAn))  ;
//
//        for (MonAn i : sapXepMon){
//            addItemToFlowPane(i);
//        }
////        if (flowPane.getChildren().isEmpty()) {
////            showWarn("Không tìm thấy danh sách!");
////        }
//    }
//
//    void descendingSorting () {
//        List<MonAn> sapXepMon = monAnDAO.getAllMonAn();
////        flowPane.getChildren().clear();
////        flowPane.prefHeightProperty().bind(scrollPane.heightProperty());
////        flowPane.prefWidthProperty().bind(scrollPane.widthProperty());
//
//        sapXepMon.sort(Comparator.comparing(MonAn::getDonGia).reversed().thenComparing(MonAn::getTenMonAn))  ;
//
//        for (MonAn i : sapXepMon){
//            addItemToFlowPane(i);
//        }
////        if (flowPane.getChildren().isEmpty()) {
////            showWarn("Không tìm thấy danh sách!");
////        }
//    }

    private void addItemToFlowPane(MonAn monAn) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardMonAn_TrangThucDon.fxml"));
        try {
            AnchorPane pane = loader.load();
            CardMonAnThucDonController controller = loader.getController();
            controller.setMonAnThucDon(monAn, this);
            controller.setController(this);
            flowPane.getChildren().add(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
//

    @FXML
    void refreshControl(Event event) {
        Object source = event.getSource();
        if (source == btnRefresh || source == btnThemMon || source == btnCapNhat) {
            txtTimKiem.clear();
            cbTimLoaiMon.getEditor().clear();
            cbTimLoaiMon.setValue("");
            cbSapXep.getSelectionModel().clearSelection();
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

    private String generateLoaiMonAn(String itemName) {
        String prefix = generatePrefixFromName(itemName); // Generate the "XX" part from the item name
        Long maxId = getMaLoaiFromDatabase(prefix); // Get the maximum "YY" part for the given prefix
        Long newIdNumber = (maxId == null) ? 1 : maxId + 1; // Increment the ID number
        return prefix + String.format("%02d", newIdNumber); // Combine "XX" and "YY" into the final format
    }

    // Method to retrieve the maximum "YY" part for a specific prefix from the database
    public Long getMaLoaiFromDatabase(String prefix) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;
        Long maLoaiMon = null;

        try {
            transaction = session.beginTransaction();

            // Query to fetch IDs starting with the given prefix
            String query = "SELECT maLoaiMonAn FROM LoaiMonAn WHERE maLoaiMonAn LIKE :prefix";
            List<String> maMonAns = session.createQuery(query, String.class)
                    .setParameter("prefix", prefix + "%") // Match IDs with the given prefix
                    .getResultList();

            // Extract the "YY" part, convert to a number, and find the maximum
            maLoaiMon = maMonAns.stream()
                    .map(id -> id.substring(prefix.length())) // Extract "YY" part
                    .filter(yy -> yy.matches("\\d+"))         // Ensure it is numeric
                    .map(Long::parseLong)                    // Convert to Long
                    .max(Long::compare)                      // Find the maximum
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

    // Helper method to generate the "XX" part from the item name
    private String generatePrefixFromName(String name) {
        // Split the name into words, take the first character of each word, and convert to uppercase
        return Arrays.stream(name.split("\\s+"))
                .filter(word -> !word.isEmpty())       // Ensure non-empty words
                .map(word -> word.substring(0, 1))    // Take the first letter of each word
                .map(String::toUpperCase)             // Convert to uppercase
                .limit(2)                             // Take only the first 2 letters
                .reduce("", String::concat);         // Combine into a single string
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

    private String generateMaMonAn(String itemName) {
        // Generate the "XXXX" part using the logic for XXYY
        String prefix = generateLoaiMonAn(itemName); // Assume this generates the XXYY format

        // Fetch the maximum "YYYY" part for the given "XXXX" prefix and increment it
        Long maxSuffix = getMaMonFromDatabase(prefix);
        Long newSuffix = (maxSuffix == null) ? 1 : maxSuffix + 1;

        // Combine "XXXX" (from XXYY) and "YYYY" into the final format
        return prefix + String.format("%04d", newSuffix); // Ensure "YYYY" is 4 digits
    }

    // Method to retrieve the maximum "YYYY" value for a specific "XXXX" prefix
    public Long getMaMonFromDatabase(String prefix) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;
        Long maxSuffix = null;

        try {
            transaction = session.beginTransaction();

            // Query to fetch IDs starting with the given "XXXX" prefix
            String query = "SELECT maMonAn FROM MonAn WHERE maMonAn LIKE :prefix";
            List<String> maMonAns = session.createQuery(query, String.class)
                    .setParameter("prefix", prefix + "%") // Match IDs with the given prefix
                    .getResultList();

            // Extract the "YYYY" part, convert to a number, and find the maximum
            maxSuffix = maMonAns.stream()
                    .map(id -> id.substring(prefix.length())) // Extract "YYYY" part
                    .filter(yy -> yy.matches("\\d+"))         // Ensure it is numeric
                    .map(Long::parseLong)                    // Convert to Long
                    .max(Long::compare)                      // Find the maximum
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
        return maxSuffix;
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

        //System.out.println(loaiMon.getMaLoaiMonAn());

        TrangThaiMonAn ttMonAn = comboTTValue();

        // Get other input values from UI components

        double gia = Double.parseDouble(txtGia.getText());
        String tenMonAn = txtTenMonAn.getText();
        String donViTinh = txtDonViTinh.getText();

        // Generate ID for the new MonAn
        String maMonAn = generateMaMonAn(cbloaiMonAn.getValue());

        // Assuming duongDanAnh is a field that holds the path of the selected image
        String duongDanAnh = this.duongDanAnh; // replace with actual image path

        // Create the new MonAn object
        MonAn monAn = new MonAn(maMonAn, loaiMon, tenMonAn, gia, donViTinh, duongDanAnh, ttMonAn);

        // Save MonAn to the database
        monAnDAO.themMonAn(monAn);
    }

    public void themLoaiMon () {
        LoaiMonDAO loaiMonDAO = new LoaiMonDAO();
        String maLoai = generateLoaiMonAn(cbloaiMonAn.getValue());
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
            case "Có sẵn":
                trangThaiMonAn = TrangThaiMonAn.CO_SAN;
                break;
            case "Tạm hết":
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

        if (monAn != null) {
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

            String imagePath = monAn.getHinhAnh();
            String imageDefaultPath = "/org/login/quanlydatban/icons/empty.png";
            if (imagePath != null && !imagePath.isEmpty()) {
                anhMon.setImage(new Image(new File(imagePath).toURI().toString()));
            } else {
                anhMon.setImage(new Image(getClass().getResource(imageDefaultPath).toString()));
            }

            txfMoTa.setText(loaiMon.getMoTaLoaiMonAn());
        }

    }

}

