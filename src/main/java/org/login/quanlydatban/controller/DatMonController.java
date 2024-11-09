package org.login.quanlydatban.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.Session;
import org.login.quanlydatban.dao.BanDAO;
import org.login.quanlydatban.dao.ChiTietHoaDonDAO;
import org.login.quanlydatban.dao.HoaDonDAO;
import org.login.quanlydatban.dao.MonAnDAO;
import org.login.quanlydatban.entity.*;
import org.login.quanlydatban.entity.enums.TrangThaiBan;
import org.login.quanlydatban.entity.enums.TrangThaiHoaDon;
import org.login.quanlydatban.entity.keygenerator.CTHDCompositeKey;
import org.login.quanlydatban.hibernate.HibernateUtils;
import org.login.quanlydatban.notification.Notification;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class DatMonController implements Initializable {
    @FXML
    private FlowPane flowPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField banID;

    @FXML
    private TextField trangThaiBanText;

    @FXML
    private TextField tongTienTxt;

    @FXML
    private Button btnGiuBan;

    @FXML
    private Button btnHuy;
    private MonAnDAO monAnDAO;
    private BanDAO banDAO;

    @FXML
    private TextField tienKhachDua;

    @FXML
    private TextField tienTraLai;

    @FXML
    private TextField phuThu;

    @FXML
    private TableView<Object[]> orderTable;

    @FXML
    private TableColumn<Object[], Double> donGia;

    @FXML
    private TableColumn<Object[], String> dvt;

    @FXML
    private TableColumn<Object[], Void> ghiChu;

    @FXML
    private TableColumn<Object[], String> maBan;

    @FXML
    private TableColumn<Object[], Integer> soLuong;

    @FXML
    private TableColumn<Object[], String> tenMon;

    @FXML
    private TableColumn<Object[], Void> huyMon;

    @FXML
    private TableColumn<Object[], Void> giam;

    @FXML
    private TableColumn<Object[], Void> tang;

    private ObservableList<Object[]> objectsObservableList;

    private Ban ban;

    private NhanVien nhanVien;

    private HoaDonDAO hoaDonDAO;

    private ChiTietHoaDonDAO chiTietHoaDonDAO;
    private HoaDon hoaDon;

    private Stage chuyenBanStage;
    private double tkd = 0.0;
    private double pt = 0.0;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        monAnDAO = new MonAnDAO();
        banDAO = new BanDAO();
        hoaDonDAO = new HoaDonDAO();
        chiTietHoaDonDAO = new ChiTietHoaDonDAO();

        objectsObservableList = FXCollections.observableArrayList();

        donGia.setCellValueFactory(data -> new SimpleDoubleProperty((Double)data.getValue()[2]).asObject());
        soLuong.setCellValueFactory(data -> new SimpleIntegerProperty((Integer) data.getValue()[3]).asObject());
        soLuong.setCellFactory(column -> new TableCell<Object[], Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.toString());
                    setTextAlignment(TextAlignment.CENTER); // Center the text horizontally
                    setAlignment(Pos.CENTER);               // Center the text vertically
                }
            }
        });

        maBan.setCellValueFactory(data -> new SimpleStringProperty((String)data.getValue()[0]));
        tenMon.setCellValueFactory(data -> new SimpleStringProperty((String)data.getValue()[1]));
        dvt.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue()[4]));

        dvt.setCellFactory(column -> new TableCell<Object[], String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setTextAlignment(TextAlignment.CENTER); // Center the text horizontally
                    setAlignment(Pos.CENTER);               // Center the text vertically
                }
            }
        });

        ghiChu.setCellFactory(col -> new TableCell<>() {
            private final Button button = new Button("...");
            private final StackPane pane = new StackPane();

            {
                button.setMaxWidth(Double.MAX_VALUE);  // Allow the button to expand horizontally
                button.setMaxHeight(Double.MAX_VALUE); // Allow the button to expand vertically

                Popup popup = new Popup();
                Pane widgetPane = new VBox(10); // Using VBox to organize the layout
                widgetPane.setStyle("-fx-background-color: lightgrey; -fx-padding: 10px; -fx-border-color: grey; -fx-border-width: 1px;");

                // Add a TextArea to the widget pane
                TextArea textArea = new TextArea();
                textArea.setPromptText("Enter your text here...");
                textArea.setPrefSize(200, 100); // Set preferred size for the TextArea

                // Add other components to the widget pane if needed
                Button widgetButton = new Button("Cập nhật ghi chú");

                // Add TextArea and Button to the widget-like Pane
                widgetPane.getChildren().addAll(textArea, widgetButton);

                popup.getContent().add(widgetPane);
                popup.setAutoHide(true); // Automatically hide when clicking outside

                widgetButton.setOnAction(event -> {
                    if (!textArea.getText().equals("")) {
                        String note = textArea.getText();
                        Object[] objects = getTableView().getItems().get(getIndex());
                        ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
                        CTHDCompositeKey key = new CTHDCompositeKey(hoaDon.getMaHoaDon(), String.valueOf(objects[0]));
                        chiTietHoaDon.setMaChiTietHoaDon(key);

                        chiTietHoaDon.setGhiChu(note);

                        chiTietHoaDonDAO.capNhatCTHD(chiTietHoaDon);

                        popup.hide();
                    }
                });

                button.setOnAction(event -> {
                    if (!popup.isShowing()) {
                        // Position the popup near the button
                        if (getTableView() != null) {// if there are not a row in table
                            Object[] objects = getTableView().getItems().get(getIndex());
                            CTHDCompositeKey key = new CTHDCompositeKey(hoaDon.getMaHoaDon(), String.valueOf(objects[0]));

                            ChiTietHoaDon chiTietHoaDon = chiTietHoaDonDAO.getCTHD(key);

                            textArea.setText(chiTietHoaDon.getGhiChu());
                            textArea.positionCaret(textArea.getLength());
                        }
                        popup.show(button, button.getScene().getWindow().getX() + button.getLayoutX(),
                                button.getScene().getWindow().getY() + button.getLayoutY() + button.getHeight());
                    } else {
                        popup.hide();
                    }
                });

                pane.getChildren().add(button);
                pane.setStyle("-fx-alignment: center;"); // Center the button within the StackPane
            }


            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                    button.prefWidthProperty().bind(pane.widthProperty());  // Bind button width to pane
                    button.prefHeightProperty().bind(pane.heightProperty()); // Bind button height to pane
                }
            }
        });
        huyMon.setCellFactory(col -> new TableCell<>() {
            private final Button button = new Button("x");
            private final StackPane pane = new StackPane();

            {
                button.setMaxWidth(Double.MAX_VALUE);  // Allow the button to expand horizontally
                button.setMaxHeight(Double.MAX_VALUE); // Allow the button to expand vertically

                button.setOnAction(event -> {
                    Object[] objects = getTableView().getItems().get(getIndex());
                    chiTietHoaDonDAO.deleteChiTietHoaDon(hoaDon.getMaHoaDon(), String.valueOf(objects[0]));
                    getTableView().getItems().remove(getIndex());
                    capNhatTongTien();

                });

                button.setStyle("-fx-background-color: #F3B664");
                pane.getChildren().add(button);
                pane.setStyle("-fx-alignment: center;"); // Center the button within the StackPane
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                    button.prefWidthProperty().bind(pane.widthProperty());  // Bind button width to pane
                    button.prefHeightProperty().bind(pane.heightProperty()); // Bind button height to pane
                }
            }
        });
        tang.setCellFactory(col -> new TableCell<>() {
            private final Button button = new Button("+");
            private final StackPane pane = new StackPane();

            {
                button.setMaxWidth(Double.MAX_VALUE);  // Allow the button to expand horizontally
                button.setMaxHeight(Double.MAX_VALUE); // Allow the button to expand vertically

                button.setOnAction(event -> {
                    Object[] objects = getTableView().getItems().get(getIndex());
                    int sl = Integer.parseInt(String.valueOf(objects[3])) + 1;

                    CTHDCompositeKey key = new CTHDCompositeKey(hoaDon.getMaHoaDon(), String.valueOf(objects[0]));

                    chiTietHoaDonDAO.capNhatSoLuong(key, sl);

                    objects[3] = sl;
                    orderTable.refresh();

                    capNhatTongTien();

                });

                button.setStyle("-fx-background-color: #9FBB73");
                pane.getChildren().add(button);
                pane.setStyle("-fx-alignment: center;"); // Center the button within the StackPane
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                    button.prefWidthProperty().bind(pane.widthProperty());  // Bind button width to pane
                    button.prefHeightProperty().bind(pane.heightProperty()); // Bind button height to pane
                }
            }
        });
        giam.setCellFactory(col -> new TableCell<>() {
            private final Button button = new Button("-");
            private final StackPane pane = new StackPane();

            {
                button.setMaxWidth(Double.MAX_VALUE);  // Allow the button to expand horizontally
                button.setMaxHeight(Double.MAX_VALUE); // Allow the button to expand vertically

                button.setOnAction(event -> {
                    Object[] objects = getTableView().getItems().get(getIndex());
                    int sl = Integer.parseInt(String.valueOf(objects[3]));
                    if(Integer.parseInt(String.valueOf(objects[3])) - 1 != 0)
                        sl = Integer.parseInt(String.valueOf(objects[3])) - 1;

                    CTHDCompositeKey key = new CTHDCompositeKey(hoaDon.getMaHoaDon(), String.valueOf(objects[0]));

                    chiTietHoaDonDAO.capNhatSoLuong(key, sl);

                    objects[3] = sl;
                    orderTable.refresh();

                    capNhatTongTien();
                });

                button.setStyle("-fx-background-color: #F3B664");
                pane.getChildren().add(button);
                pane.setStyle("-fx-alignment: center;"); // Center the button within the StackPane
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                    button.prefWidthProperty().bind(pane.widthProperty());  // Bind button width to pane
                    button.prefHeightProperty().bind(pane.heightProperty()); // Bind button height to pane
                }
            }
        });

        orderTable.setItems(objectsObservableList);
        monAnDAO.readAll();
        flowPane.prefHeightProperty().bind(scrollPane.heightProperty());
        flowPane.prefWidthProperty().bind(scrollPane.widthProperty());

        for (MonAn i : monAnDAO.getListMonAn()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardMonAn_TrangDatMon.fxml"));
            try {
                AnchorPane pane = loader.load();

                CardMonAnController controller = loader.getController();
                controller.setMonAn(i, this);
                controller.setController(this);
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

    public void capNhatTongTien() {
        tongTienTxt.setText(String.valueOf(hoaDon.tinhTongTien()));
    }

    public void setBan(Ban ban) {
        this.ban = ban;
        this.banID.setText(ban.getMaBan());
        this.trangThaiBanText.setText(ban.getTrangThaiBan().toString());

        if(ban.getTrangThaiBan().equals(TrangThaiBan.BAN_TRONG)) {
            this.btnHuy.setVisible(false);
            this.btnGiuBan.setVisible(true);
        }
        else {
            this.btnHuy.setVisible(true);
            this.btnGiuBan.setVisible(false);
        }
    }

    public boolean daLapHoaDon() {
        return hoaDon != null;
    }
    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public Ban getBan() {
        return this.ban;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
        chiTietHoaDonDAO.getCTHDfromHD(hoaDon).forEach(
                i -> loadBang(new Object[] {i.getMonAn().getMaMonAn(),
                        i.getMonAn().getTenMonAn(),
                        i.getMonAn().getDonGia(),
                        i.getSoLuong(),
                        i.getMonAn().getDonViTinh(),
                        " "}
                ));
    }

    @FXML
    void giuBan(ActionEvent event) {
        if(Notification.xacNhan("Xác nhận giữ bàn, bạn có thể huỷ ngay nếu muốn")){
            this.ban.setTrangThaiBan(TrangThaiBan.DANG_PHUC_VU);
            this.ban = banDAO.updateBan(ban);
            this.trangThaiBanText.setText(ban.getTrangThaiBan().toString());
            this.btnHuy.setVisible(true);
            this.btnGiuBan.setVisible(false);

            //lap hoa don voi trang thai chua thanh toan khi giu ban
            HoaDon hoaDon = new HoaDon();
            hoaDon.setNhanVien(nhanVien);
            hoaDon.setBan(ban);
            hoaDon.setTrangThaiHoaDon(TrangThaiHoaDon.CHUA_THANH_TOAN);
            hoaDon.setNgayLap(LocalDate.now());
            hoaDon.setKhachHang(null);

            this.hoaDon = hoaDonDAO.lapHoaDon(hoaDon);

            Notification.thongBao("Giữ bàn thành công", Alert.AlertType.INFORMATION);
        }
    }
    @FXML
    void huyBan(ActionEvent event) {
        if(orderTable.getItems().size() == 0){
            ban.setTrangThaiBan(TrangThaiBan.BAN_TRONG);
            this.ban = banDAO.updateBan(ban);
            this.trangThaiBanText.setText(ban.getTrangThaiBan().toString());
            this.btnHuy.setVisible(false);
            this.btnGiuBan.setVisible(true);

            if(hoaDon != null) {
                hoaDonDAO.xoaHoaDon(hoaDon);
                hoaDon = null;
            }
        }
        else Notification.thongBao("Không thể huỷ, hãy tiến hành thanh toán", Alert.AlertType.INFORMATION);
    }

    @FXML
    void chuyenBan(ActionEvent event) throws IOException {
        if(hoaDon == null){
            Notification.thongBao("Bàn hiện tại đang trống, không thể tiếp tục", Alert.AlertType.INFORMATION);
            return;
        }

        if(chuyenBanStage != null){
            if(chuyenBanStage.isShowing()) {
                chuyenBanStage.toFront();
                return;
            }
        }

        if(chuyenBanStage == null || chuyenBanStage.isShowing()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangChuyenBan.fxml"));
            Scene scene = new Scene(loader.load());
            ChuyenBanController controller = loader.getController();
            controller.setBanHienTai(ban);
            controller.setCurrentHD(hoaDon);
            controller.setDatMonController(this);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/org/login/quanlydatban/stylesheets/style.css")).toExternalForm());

            chuyenBanStage = new Stage();
            chuyenBanStage.setScene(scene);
            chuyenBanStage.initStyle(StageStyle.UNDECORATED);
            chuyenBanStage.show();
        }
        else {
            chuyenBanStage.show();
            chuyenBanStage.toFront();
        }
    }


    @FXML
    void thanhToan(ActionEvent event) {
        if(hoaDon != null){
            if (tkd != 0.0){
                if(pt != 0.0){
                    if(Notification.xacNhan("Xác nhận thanh toán?")){
                        orderTable.getItems().clear();
                        this.hoaDon.setTrangThaiHoaDon(TrangThaiHoaDon.DA_THANH_TOAN);

                        ban.setTrangThaiBan(TrangThaiBan.BAN_TRONG);
                        trangThaiBanText.setText(TrangThaiBan.BAN_TRONG.toString());
                        tongTienTxt.setText("0.0");
                        tienKhachDua.setText("0.0");
                        phuThu.setText("0.0");
                        tienTraLai.setText("");

                        hoaDonDAO.updateHoaDon(hoaDon);
                        banDAO.updateBan(ban);

                        hoaDon = null;

                        this.btnHuy.setVisible(false);
                        this.btnGiuBan.setVisible(true);
                    }
                }
                else Notification.thongBao("Vui lòng nhập phụ thu trước khi thanh toán", Alert.AlertType.INFORMATION);
            }
            else Notification.thongBao("Vui lòng nhập tiền khách đưa trước khi thanh toán", Alert.AlertType.INFORMATION);
        }
        else {
            Notification.thongBao("Vui lòng giữ bàn", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void back(MouseEvent event) throws IOException {
        if(anchorPane.getParent() instanceof BorderPane){
            BorderPane pane = (BorderPane) anchorPane.getParent();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangChonBan.fxml"));
            AnchorPane anchorPane = loader.load();
            ChonBanController controller = loader.getController();
            controller.setNhanVien(nhanVien);
            pane.setCenter(anchorPane);
        }
    }

    public void themChiTietHoaDon(MonAn monAn){//tao 1 lan
        ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();

        chiTietHoaDon.setHoaDon(hoaDon);
        chiTietHoaDon.setMonAn(monAn);
        chiTietHoaDon.setSoLuong(1);

        chiTietHoaDonDAO.luuCTHD(chiTietHoaDon);
    }

    public void themDuLieuVaoBangMonAn(Object[] objects, MonAn monAn){
        boolean trungMaMonAn = false;
        String maMonAnMoi = objects[0].toString();
        int soLuongMoi = Integer.parseInt(objects[3].toString());
        for (Object[] row: orderTable.getItems()){
            String maMonAnHienTai = row[0].toString();
            if(maMonAnHienTai.equalsIgnoreCase(maMonAnMoi)){
                int soLuongHienTai = (Integer) row[3];
                row[3] = soLuongMoi + soLuongHienTai; // Increment quantity
                orderTable.refresh(); // Refresh table to show updated quantity
                trungMaMonAn = true;

                CTHDCompositeKey key = new CTHDCompositeKey(hoaDon.getMaHoaDon(), maMonAnHienTai);

                chiTietHoaDonDAO.capNhatSoLuong(key, soLuongMoi + soLuongHienTai);
                break;
            }
        }
        if(!trungMaMonAn) {
            orderTable.getItems().add(objects);
            themChiTietHoaDon(monAn);
        }
    }

    public void loadBang(Object[] objects) {
        orderTable.getItems().add(objects);
    }

    @FXML
    void tinhTienTraLai(KeyEvent event) {
        if(hoaDon != null){
            if(event.getSource().equals(tienKhachDua)){
                System.out.println(0);
                if (tienKhachDua.getText().matches("\\d+"))
                    tkd = Double.parseDouble(tienKhachDua.getText());
                else {
                    Notification.thongBao("Chỉ được nhập số", Alert.AlertType.INFORMATION);
                    tienKhachDua.setText(tienKhachDua.getText().substring(0, tienKhachDua.getLength() - 1));
                    tienKhachDua.positionCaret(tienKhachDua.getText().length());
                }
            }
            else {
                if (phuThu.getText().matches("\\d+"))
                    pt = Double.parseDouble(phuThu.getText());
                else {
                    System.out.println(phuThu.getText());
                    Notification.thongBao("Chỉ được nhập số", Alert.AlertType.INFORMATION);
                    phuThu.setText(phuThu.getText().substring(0, phuThu.getLength() - 1));
                    phuThu.positionCaret(phuThu.getText().length());
                }
            }
            System.out.println(tkd);
            System.out.println(hoaDon.getTongTien() + pt);

            if (tkd >= (hoaDon.getTongTien() + pt)){
                tienTraLai.setText(String.valueOf(tkd - (hoaDon.getTongTien() + pt)));
            }
            else tienTraLai.setText("Tiền khách đưa phải lớn hơn hoặc bằng tổng tiền");
        }
        else Notification.thongBao("Hãy lập hoá đơn trước khi nhập", Alert.AlertType.INFORMATION);
    }

    public HoaDon getHoaDon() {
        return this.hoaDon;
    }
}
