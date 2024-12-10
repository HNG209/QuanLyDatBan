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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import org.login.quanlydatban.dao.LoaiMonDAO;
import org.login.quanlydatban.dao.MonAnDAO;
import org.login.quanlydatban.entity.LoaiMonAn;
import org.login.quanlydatban.entity.MonAn;
import org.login.quanlydatban.entity.enums.TrangThaiMonAn;
import org.login.quanlydatban.notification.Notification;
import org.login.quanlydatban.utilities.NumberFormatter;

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
    private TextField txtTimKiem;

    @FXML
    private ComboBox<String> cbDonViTinh;

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
    private double donGia = 0.0;

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
        loadDonViTinhComboBox();

        List<MonAn> monAnList = monAnDAO.getAllMonAn();
        if (monAnList == null) {
            Notification.thongBao("Danh sach mon an bi trong", Alert.AlertType.WARNING);
        }

        List<LoaiMonAn> loaiMonAnList = loaiMonDAO.getListLoai();
        if (loaiMonAnList == null) {
            Notification.thongBao("Danh sach loai mon an bi trong", Alert.AlertType.WARNING);
        }

        taiAnh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //System.out.println("Nhấn nút tải ảnh");
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Mở file");

                // Thiết lập thư mục khởi tạo
                File initialDir = new File("src/main/resources/org/login/quanlydatban/Image");
                if (initialDir.exists() && initialDir.isDirectory()) {
                    fileChooser.setInitialDirectory(initialDir);
                } else {
                    Notification.thongBao(
                            "Thư mục khởi tạo không tồn tại hoặc không phải là thư mục: " + initialDir.getAbsolutePath(),
                            Alert.AlertType.ERROR
                    );
                    return; // Kết thúc nếu thư mục khởi tạo không hợp lệ
                }

                // Thiết lập bộ lọc file
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif");
                fileChooser.getExtensionFilters().add(extFilter);

                // Hiển thị hộp thoại chọn file
                File file = fileChooser.showOpenDialog(null);

                if (file != null) {
                    // Lấy đường dẫn tương đối từ thư mục gốc dự án
                    File projectRoot = new File("src/main/resources/org/login/quanlydatban/Image");
                    duongDanAnh = projectRoot.toURI().relativize(file.toURI()).getPath();

                    // Cập nhật ImageView với ảnh mới
                    Image image = new Image(file.toURI().toString());
                    anhMon.setImage(image);

                    // Hiển thị thông báo thành công
                    Notification.thongBao("Tải ảnh thành công!", Alert.AlertType.INFORMATION);
                } else {
                    Notification.thongBao("Không có tệp nào được chọn.", Alert.AlertType.WARNING);
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

        cbTimLoaiMon.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            cbTimLoaiMon.setValue(newValue);
        });

        cbDonViTinh.setOnAction(event -> {
            String newValue = cbDonViTinh.getEditor().getText().trim();

            if (!newValue.isEmpty() && !cbDonViTinh.getItems().contains(newValue)) {
                // Add the new value to the combo box
                cbDonViTinh.getItems().add(newValue);
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
                if (txtTenMonAn.getText() == null || cbDonViTinh.getValue() == null || txtGia.getText() == null ||
                        cbloaiMonAn.getValue() == null ||
                        Objects.equals(txtTenMonAn.getText(), "") ||
                        Objects.equals(cbDonViTinh.getValue(), "") ||
                        Objects.equals(txtGia.getText(), "") ||
                        Objects.equals(cbloaiMonAn.getValue(), "")) {
                    Notification.thongBao("Bạn cần nhập đầy đủ thông tin!", Alert.AlertType.WARNING);
                }
//                else if (!regexGia()) {
//                    showWarn("Bạn cần nhập đúng thông tin!");}
                else {
                    themMon();
                    refreshControl(event);
                    loadLoaiMonAnComboBox();
                    Notification.thongBao("Đã thêm thành công!", Alert.AlertType.INFORMATION);
                }

            } catch (Exception e) {
                Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
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
                    Notification.thongBao("Bạn cần chọn một món để cập nhật!", Alert.AlertType.WARNING);
                } else {
                    String tenMonMoi = txtTenMonAn.getText();
                    String donViMoi = cbDonViTinh.getValue();
                    double giaMoi = donGia;
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

                    String moTa = txfMoTa.getText();

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("XÁC NHẬN CẬP NHẬT");
                    alert.setHeaderText("Bạn có chắc chắn muốn cập nhật món này?");

                    ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

                    if (result == ButtonType.OK) {
                        monMoi = new MonAn(monAn.getMaMonAn(), loaiMonMoi, tenMonMoi, giaMoi, donViMoi, anhMoi, trangThaiMoi, moTa);
                        monAnDAO.capNhatMonAn(monAn, monMoi);
                        Notification.thongBao("Đã cập nhật thành công!", Alert.AlertType.INFORMATION);
                        refreshControl(event);

                    } else {
                        Notification.thongBao("Đã hủy cập nhật!", Alert.AlertType.INFORMATION);
                    }

                }
            } catch (Exception e) {
                System.out.println(e);
            }

        }
    }

    @FXML
    void xoaRongControl(ActionEvent event) {
        Image imageXoaRong = new Image(getClass().getResource("/org/login/quanlydatban/icons/empty.png").toExternalForm());

        Object source = event.getSource();
        if (source == btnXoaRong) {
            setMonAn(null);
            txtTenMonAn.requestFocus();
            txtTenMonAn.setText("");
            cbloaiMonAn.getSelectionModel().clearSelection();
            cbloaiMonAn.setValue("");
            cbtrangThaiMon.getSelectionModel().selectFirst();
            cbDonViTinh.getSelectionModel().clearSelection();
            txtGia.setText("");
            anhMon.setImage(imageXoaRong);
            txfMoTa.setText("");
        }
    }

    @FXML
    void btnTimKiem(MouseEvent event) {
        String keyword = txtTimKiem.getText().trim();
        String selectedType = cbTimLoaiMon.getValue();
        int sortOption = cbSapXep.getSelectionModel().getSelectedIndex();

        if (cbTimLoaiMon.getValue() == null && keyword.isEmpty() && sortOption == -1) {
            Notification.thongBao("Bạn cần nhập/chọn một trong các cách tìm trước khi tiến hành tìm kiếm", Alert.AlertType.WARNING);
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
                Notification.thongBao("Không tìm thấy món ăn phù hợp với tiêu chí tìm kiếm!", Alert.AlertType.WARNING);
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
            throw new IllegalArgumentException("Danh sách Loai Mon An rỗng.");
        }
    }

    private void loadDonViTinhComboBox() {
        cbDonViTinh.getItems().clear();  // Clear current items to avoid duplicates
        List<String> donViList = monAnDAO.getListDon();

        if (donViList != null) {
            for (String monan : donViList) {
                cbDonViTinh.getItems().add(monan);
            }
        } else {
            throw new IllegalArgumentException("Danh sách Don Vi Tinh rỗng.");
        }
    }

//    private void showWarn(String message) {
//        Alert alert = new Alert(Alert.AlertType.WARNING);
//        alert.setTitle("Kết Quả");
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }

    //CRUD
    public void themMon() {
        MonAnDAO monAnDAO = new MonAnDAO(); // DAO for MonAn
        MonAn monAn = new MonAn();
        String selectedLoaiMon = cbloaiMonAn.getValue(); // Get the selected or entered value
        LoaiMonAn loaiMon = loaiMonDAO.getLoaiMonByName(selectedLoaiMon);
        //double gia;
         //If it doesn't exist, add it
        if (loaiMon == null) {
            themLoaiMon(); // Adds the new LoaiMonAn
            loaiMon = loaiMonDAO.getLoaiMonByName(selectedLoaiMon); // Retrieve the newly added LoaiMonAn
        }

        TrangThaiMonAn ttMonAn = comboTTValue();

        // Get other input values from UI components
        String tenMonAn = txtTenMonAn.getText().trim();
        String donViTinh = cbDonViTinh.getValue();
        String moTaMonAn = txfMoTa.getText();

        // Generate ID for the new MonAn
//        String maMonAn = generateMaMonAn(cbloaiMonAn.getValue());

        String duongDanAnh = null;
        String imageUrl = anhMon.getImage().getUrl(); // Get the URL of the image
        if (imageUrl != null && imageUrl.startsWith("file:")) {
//            if (imageUrl.endsWith("restaurant.png")) {
//                File defaultImageFile = new File(getClass().getResource("/org/login/quanlydatban/icons/empty.png").getPath());
//                duongDanAnh = defaultImageFile.getAbsolutePath();
//            } else {
                duongDanAnh = imageUrl.substring(5); // Remove "file:" prefix to get the file path
//            }

        }

        // Create the new MonAn object
//        monAn.setMaMonAn(maMonAn);
        monAn.setLoaiMonAn(loaiMon);
        monAn.setTenMonAn(tenMonAn);
        monAn.setDonGia(donGia);
        monAn.setDonViTinh(donViTinh);
        monAn.setHinhAnh(duongDanAnh);
        monAn.setTrangThaiMonAn(ttMonAn);
        monAn.setMoTaMonAn(moTaMonAn);
//        MonAn monAn = new MonAn(maMonAn, loaiMon, tenMonAn, gia, donViTinh, duongDanAnh, ttMonAn);

        // Save MonAn to the database
        monAnDAO.themMonAn(monAn);
    }

    public void themLoaiMon () {
        LoaiMonDAO loaiMonDAO = new LoaiMonDAO();
//        String maLoai = generateLoaiMonAn(cbloaiMonAn.getValue());
        String tenLoai = cbloaiMonAn.getValue();
        //String moTa = txfMoTa.getText();

        LoaiMonAn loaiMonAn = new LoaiMonAn(tenLoai);
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

    @FXML
    void formatGia(KeyEvent event) {
        if(event.getSource().equals(txtGia)){
            if(txtGia.getText().isEmpty()) {
                return;
            }
            txtGia.setText(NumberFormatter.formatPrice(txtGia.getText()));
            txtGia.positionCaret(txtGia.getText().length());

            if (!txtGia.getText().replace(".", "").matches("\\d+"))
            {
                Notification.thongBao("Chỉ được nhập số", Alert.AlertType.INFORMATION);
                txtGia.setText(txtGia.getText().substring(0, txtGia.getLength() - 1));
                txtGia.setText(NumberFormatter.formatPrice(txtGia.getText()));
                txtGia.positionCaret(txtGia.getText().length());
            }
            else {
                donGia = Double.parseDouble(txtGia.getText().replace(".", "").trim());
            }
        }
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
            cbDonViTinh.setValue(String.valueOf(monAn.getDonViTinh()));

            txtGia.setText(NumberFormatter.formatPrice(String.format("%.0f", monAn.getDonGia())));
            donGia = monAn.getDonGia();

            String imagePath = monAn.getHinhAnh();
            String imageDefaultPath = "/org/login/quanlydatban/icons/restaurant.png";
            if (imagePath != null && !imagePath.isEmpty()) {
                anhMon.setImage(new Image(new File(imagePath).toURI().toString()));
            } else {
                anhMon.setImage(new Image(getClass().getResource(imageDefaultPath).toString()));
            }

            txfMoTa.setText(monAn.getMoTaMonAn());
        }

    }

}

