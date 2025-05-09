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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
//import org.login.quanlydatban.dao.*;
//import org.login.quanlydatban.entity.*;
//import org.login.quanlydatban.entity.enums.TrangThaiBan;
//import org.login.quanlydatban.entity.enums.TrangThaiHoaDon;
//import org.login.quanlydatban.entity.keygenerator.CTHDCompositeKey;

import org.login.entity.*;
import org.login.entity.enums.TrangThaiBan;
import org.login.entity.enums.TrangThaiHoaDon;
import org.login.entity.keygenerator.CTHDCompositeKey;
import org.login.quanlydatban.notification.Notification;
import org.login.quanlydatban.utilities.NumberFormatter;

//services
import org.login.quanlydatban.utilities.RMIServiceUtils;
import org.login.service.*;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class DatMonController implements Initializable {
    //buttons
    @FXML
    private Button btnChuyenBan;

    @FXML
    private Button btnGiuBan;

    @FXML
    private Button btnHuy;

    @FXML
    private Button btnXacNhan;

    //table column
    private ObservableList<Object[]> objectsObservableList;

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

    //textfields
    @FXML
    private TextField timTheoGiaTD;

    @FXML
    private TextField timTheoGiaTT;

    @FXML
    private TextField timTheoLoai;

    @FXML
    private TextField timTheoTen;

    @FXML
    private TextField tfDiemTichLuyDung;

//    @FXML
//    private TextField tienKhachDua;
//
//    @FXML
//    private TextField tienTraLai;

    @FXML
    private TextField tfCCCD;

    @FXML
    private TextField tenKhachHang;

    @FXML
    private TextField phuThu;

    @FXML
    private TextField banID;

    @FXML
    private TextField trangThaiBanText;

    @FXML
    private TextField tongTienTxt;

    @FXML
    private TextField tfDiemTichLuyht;

    @FXML
    private Pagination pagination;

    //DAO
    private HoaDonService hoaDonService;

    private KhachHangService khachHangService;

    private CTHDService cthdService;

    private LichDatService lichDatService;

    private MonAnService monAnService;

    private BanService banService;

    private HoaDon hoaDon;

    //temp data
    private KhachHang khachHang;

    private Ban ban;

//    private double pt = 0.0;

    private double tienTL = 0.0;

    private String prevCCCD = "";

    private int pageSelected;

    private Stage chuyenBanStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            monAnService = RMIServiceUtils.useMonAnService();
            banService = RMIServiceUtils.useBanService();
            hoaDonService = RMIServiceUtils.useHoaDonService();
            cthdService = RMIServiceUtils.useCTHDService();
            khachHangService = RMIServiceUtils.useKhachHangService();
            lichDatService = RMIServiceUtils.useLichDatService();

            pagination.setPageFactory(pageIndex -> {
                try {
                    return createPageContent(pageIndex);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });

            tenKhachHang.focusedProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    if (!newValue && !tfCCCD.getText().isEmpty() && tenKhachHang.isEditable()) { // If newValue is false, the TextField has lost focus
                        if (Notification.xacNhan("Lưu khách hàng này?")) {
                            KhachHang khachHang = new KhachHang();
                            khachHang.setCccd(tfCCCD.getText());
                            khachHang.setTenKhachHang(tenKhachHang.getText());

                            khachHangService.themKhachHang(khachHang);
                            this.khachHang = khachHang;

                            hoaDon.setKhachHang(khachHang);
                            hoaDonService.updateHoaDon(hoaDon);

                            tenKhachHang.setEditable(false);

                            prevCCCD = khachHang.getCccd();
                        }
                        else tfCCCD.setText(prevCCCD);
                    }
                }
                catch (Exception e){
                    Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
                    tfCCCD.clear();
                    tenKhachHang.clear();
                }
            });

            tfCCCD.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue && khachHang != null && tfCCCD.getText().length() == 12) { // If newValue is false, the TextField has lost focus
                    if (Notification.xacNhan("Khách hàng sẽ được thêm vào hoá đơn")) {
                        KhachHang khachHang = null;
                        try {
                            khachHang = khachHangService.getKHByCCCD(tfCCCD.getText());
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }

                        prevCCCD = khachHang.getCccd();
                        hoaDon.setKhachHang(khachHang);
                        try {
                            hoaDonService.updateHoaDon(hoaDon);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });

            tfDiemTichLuyDung.focusedProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    if (!newValue) { // If newValue is false, the TextField has lost focus
                        if (tfDiemTichLuyDung.getText().length() > 2) {//111, 1111
                            int dtl = Integer.parseInt(tfDiemTichLuyDung.getText());
                            if (dtl <= hoaDon.getKhachHang().getDiemTichLuy())
                                tienTL = dtl * 10.0;
                            else throw new IllegalArgumentException("Điểm tích luỹ vượt mức khả dụng");
                            capNhatTongTien();
//                            capNhatTienTraLai();
                        }
                    }
                } catch (Exception e) {
                    Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
                    tfDiemTichLuyDung.clear();
                    tienTL = 0.0;
                    try {
                        capNhatTongTien();
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });


            objectsObservableList = FXCollections.observableArrayList();

            donGia.setCellValueFactory(data -> new SimpleDoubleProperty((Double) data.getValue()[2]).asObject());
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

            maBan.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue()[0]));
            tenMon.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue()[1]));
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
                    widgetPane.setStyle("-fx-alignment: center;-fx-background-color: lightgrey; -fx-padding: 10px; -fx-border-color: grey; -fx-border-width: 1px;");

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
                            chiTietHoaDon.setSoLuong(Integer.parseInt(String.valueOf(objects[3])));

                            try {
                                cthdService.capNhatCTHD(chiTietHoaDon);
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }

                            popup.hide();
                        }
                    });

                    button.setOnAction(event -> {
                        if (!popup.isShowing()) {
                            // Position the popup near the button
                            if (getTableView() != null) {// if there are not a row in table
                                Object[] objects = getTableView().getItems().get(getIndex());
                                CTHDCompositeKey key = new CTHDCompositeKey(hoaDon.getMaHoaDon(), String.valueOf(objects[0]));

                                ChiTietHoaDon chiTietHoaDon = null;
                                try {
                                    chiTietHoaDon = cthdService.getCTHD(key);
                                } catch (RemoteException e) {
                                    throw new RuntimeException(e);
                                }

                                textArea.setText(chiTietHoaDon.getGhiChu());
                                textArea.positionCaret(textArea.getLength());
                            }
                            popup.show(button, button.getScene().getWindow().getX() + button.getLayoutX(),
                                    button.getScene().getWindow().getY() + button.getLayoutY() + button.getHeight());
                        } else {
                            popup.hide();
                        }
                    });

                    button.setStyle("-fx-background-color: #F1EB90;");
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
                        try {
                            cthdService.deleteChiTietHoaDon(hoaDon.getMaHoaDon(), String.valueOf(objects[0]));
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        getTableView().getItems().remove(getIndex());
                        try {
                            capNhatTongTien();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
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

                        try {
                            cthdService.capNhatSoLuong(key, sl);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }

                        objects[3] = sl;
                        orderTable.refresh();

                        try {
                            capNhatTongTien();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
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
                        if (Integer.parseInt(String.valueOf(objects[3])) - 1 != 0)
                            sl = Integer.parseInt(String.valueOf(objects[3])) - 1;

                        CTHDCompositeKey key = new CTHDCompositeKey(hoaDon.getMaHoaDon(), String.valueOf(objects[0]));

                        try {
                            cthdService.capNhatSoLuong(key, sl);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }

                        objects[3] = sl;
                        orderTable.refresh();

                        try {
                            capNhatTongTien();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
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
            monAnService.readAll();
        } catch (Exception e) {
            Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    private ScrollPane createPageContent(int pageIndex) throws RemoteException {
        FlowPane fp = new FlowPane();
        fp.setAlignment(Pos.CENTER);
        fp.setHgap(15);
        fp.setVgap(10);

        String tenMon = timTheoTen.getText();
        String loaiMon = timTheoLoai.getText();
        double giaTT = timTheoGiaTT.getText().isEmpty() ? 0.0 : Double.parseDouble(timTheoGiaTT.getText().replace(".", ""));
        double giaTD = timTheoGiaTD.getText().isEmpty() ? 0.0 : Double.parseDouble(timTheoGiaTD.getText().replace(".", ""));

        List<MonAn> monAnList = monAnService.getMonAnBy(tenMon, giaTT, giaTD, loaiMon, pageIndex, 5);
        int size = monAnService.countMonAnBy(tenMon, giaTT, giaTD, loaiMon);
        int maximumPageCount;

        if(size % 5 == 0)
            maximumPageCount = size / 5;
        else maximumPageCount = (size / 5) + 1;

        pagination.setPageCount(maximumPageCount);

        for (MonAn i : monAnList) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardMonAn_TrangDatMon.fxml"));
            try {
                AnchorPane pane = loader.load();

                CardMonAnController controller = loader.getController();
                controller.setMonAn(i, this);
                controller.setController(this);
                fp.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        ScrollPane sc = new ScrollPane(fp);
        fp.prefHeightProperty().bind(sc.heightProperty());
        fp.prefWidthProperty().bind(sc.widthProperty());

        return sc;
    }
    
    public void capNhatTongTien() throws RemoteException {
        if(tienTL > 0.0)
            tongTienTxt.setText(NumberFormatter.formatPrice(String.valueOf((int) (hoaDonService.tinhTongTien(hoaDon) + hoaDon.getPhuThu() - tienTL))) + " (-" + NumberFormatter.formatPrice(String.valueOf((int) tienTL)) + ")");
        else
            tongTienTxt.setText(NumberFormatter.formatPrice(String.valueOf((int) (hoaDonService.tinhTongTien(hoaDon) + hoaDon.getPhuThu()))));
    }

    public void setPageSelected(int i) {
        this.pageSelected = i;
        switch (pageSelected){
            case 1:
                btnGiuBan.setDisable(true);
                btnHuy.setDisable(true);
                btnChuyenBan.setDisable(true);
                btnXacNhan.setDisable(true);
                phuThu.setEditable(false);
                break;
            default:
                break;
        }
    }

    public void setBan(Ban ban) {
        this.ban = ban;
        this.banID.setText(ban.getMaBan());

        if(pageSelected == 0) {
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
        else {
            this.trangThaiBanText.setText(ban.getTrangThaiBan().toString() + " (ĐÃ ĐẶT TRƯỚC)");
        }
    }

    public boolean daLapHoaDon() {
        return hoaDon != null;
    }

    public Ban getBan() {
        return this.ban;
    }

    public void refresh() {
        orderTable.getItems().clear();
    }

    public void setHoaDon(HoaDon hoaDon) throws RemoteException {
        this.hoaDon = hoaDon;
        if(hoaDon != null){
            tfCCCD.setEditable(true);
            if(hoaDon.getKhachHang() != null){
                khachHang = hoaDon.getKhachHang();
                tfCCCD.setText(khachHang.getCccd());
                prevCCCD = khachHang.getCccd();
                tenKhachHang.setText(khachHang.getTenKhachHang());
                tfDiemTichLuyht.setText(String.valueOf(khachHang.getDiemTichLuy()));
            }
            else {
                tfDiemTichLuyDung.setDisable(true);
            }
            phuThu.setText(NumberFormatter.formatPrice(String.valueOf((int) hoaDon.getPhuThu())));
            capNhatTongTien();
        }
        cthdService.getCTHDfromHD(hoaDon).forEach(
                i -> loadBang(new Object[] {i.getMonAn().getMaMonAn(),
                        i.getMonAn().getTenMonAn(),
                        i.getMonAn().getDonGia(),
                        i.getSoLuong(),
                        i.getMonAn().getDonViTinh(),
                        " "}
                ));
    }

    @FXML
    void giuBan(ActionEvent event) throws IOException {
//        try {
            List<LichDat> lichDatBanHienTai = lichDatService.getLichDatIf(ban);

            if (!lichDatBanHienTai.isEmpty()) {
                if(Notification.xacNhan("Bàn hiện tại đang có lịch đặt, vui lòng kiểm tra lịch")){
                    DatLichController.getInstance().refreshBang(lichDatBanHienTai);
                    TrangChuController.getBorderPaneStatic().setCenter(DatLichController.getInstance().getRoot());
                    return;
                }
                return;
            }

            if (Notification.xacNhan("Xác nhận giữ bàn, bạn có thể huỷ ngay nếu muốn")) {
                this.ban.setTrangThaiBan(TrangThaiBan.DANG_PHUC_VU);
                this.ban = banService.updateBan(ban);
                this.trangThaiBanText.setText(ban.getTrangThaiBan().toString());
                this.btnHuy.setVisible(true);
                this.btnGiuBan.setVisible(false);

                //lap hoa don voi trang thai chua thanh toan khi giu ban
                HoaDon hoaDon = new HoaDon();
                hoaDon.setBan(ban);
                hoaDon.setTrangThaiHoaDon(TrangThaiHoaDon.CHUA_THANH_TOAN);
                hoaDon.setNgayLap(LocalDate.now());
                hoaDon.setKhachHang(null);

                this.hoaDon = hoaDonService.lapHoaDon(hoaDon);

                Notification.thongBao("Giữ bàn thành công", Alert.AlertType.INFORMATION);
            }
//        }
//        catch (Exception e){
//            Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
//        }
    }

    @FXML
    void huyBan(ActionEvent event) {
        try {
            if (orderTable.getItems().size() == 0) {
                ban.setTrangThaiBan(TrangThaiBan.BAN_TRONG);
                this.ban = banService.updateBan(ban);
                this.trangThaiBanText.setText(ban.getTrangThaiBan().toString());
                this.btnHuy.setVisible(false);
                this.btnGiuBan.setVisible(true);

                if (hoaDon != null) {
                    hoaDon.setTrangThaiHoaDon(TrangThaiHoaDon.DA_HUY);
                    hoaDonService.updateHoaDon(hoaDon);
                    hoaDon = null;
                }
            } else throw new IllegalArgumentException("Không thể huỷ, hãy tiến hành thanh toán");
        }
        catch (Exception e){
            Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
        }
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
        try {
            if (hoaDon == null)
                throw new IllegalArgumentException("Vui lòng giữ bàn");

            if (orderTable.getItems().isEmpty())
                throw new IllegalArgumentException("Hoá đơn rỗng, bạn chi có thể huỷ");

            if (Notification.xacNhan("Xác nhận thanh toán?")) {
                orderTable.getItems().clear();
                this.hoaDon.setTrangThaiHoaDon(TrangThaiHoaDon.DA_THANH_TOAN);
//                this.hoaDon.setPhuThu(pt);
                hoaDon.setNhanVien(TrangChuController.getTaiKhoan().getNhanVien());
                this.hoaDon.setChietKhau(tienTL);

                ban.setTrangThaiBan(TrangThaiBan.BAN_TRONG);
                trangThaiBanText.setText(TrangThaiBan.BAN_TRONG.toString());

                if(khachHang != null){
                    int amount = Integer.parseInt(tfDiemTichLuyDung.getText().isEmpty() ? "0" : tfDiemTichLuyDung.getText());
                    khachHang.setDiemTichLuy(khachHang.getDiemTichLuy() - amount);
                    khachHangService.suaKhachHang(khachHang);
                }

                tongTienTxt.clear();
                phuThu.clear();
                tfCCCD.clear();
                tenKhachHang.clear();
                tfDiemTichLuyht.clear();
                tfDiemTichLuyDung.clear();

                hoaDonService.updateHoaDon(hoaDon);
                banService.updateBan(ban);

                if (Notification.xacNhan("Thanh toán thành công, in hoá đơn?")) {
                    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/login/quanlydatban/views/TrangXuatHoaDon.fxml")));
                    AnchorPane pane = loader.load();
                    XuatHoaDonController controller = loader.getController();
                    controller.setHoaDon(hoaDon);

                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(new Scene(pane));
                    stage.showAndWait();
                }
                hoaDon = null;
                khachHang = null;

                this.btnHuy.setVisible(false);
                this.btnGiuBan.setVisible(true);
            }
        }
        catch (Exception e){
            Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    @FXML
    void back(MouseEvent event) throws IOException {
        if(pageSelected == 0){
            ChonBanController.getInstance().refresh();
            TrangChuController.getBorderPaneStatic().setCenter(ChonBanController.getInstance().getRoot());
        }
        else {
            TrangChuController.getBorderPaneStatic().setCenter(DatLichController.getInstance().getRoot());
            DatLichController.getInstance().capNhatCoc();
        }
    }

    public void themChiTietHoaDon(MonAn monAn) throws RemoteException {//tao 1 lan
        ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();

        chiTietHoaDon.setHoaDon(hoaDon);
        chiTietHoaDon.setMonAn(monAn);
        chiTietHoaDon.setSoLuong(1);

        cthdService.luuCTHD(chiTietHoaDon);
    }

    public void themDuLieuVaoBangMonAn(Object[] objects, MonAn monAn) throws RemoteException {
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

                cthdService.capNhatSoLuong(key, soLuongMoi + soLuongHienTai);
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
    void nhapPhuThu(KeyEvent event) {
        try {
            if (phuThu.getText().equals("")) {
                hoaDon.setPhuThu(0.0);
                capNhatTongTien();

                hoaDonService.updateHoaDon(hoaDon);
                return;
            }
            phuThu.setText(NumberFormatter.formatPrice(phuThu.getText()));
            phuThu.positionCaret(phuThu.getText().length());

            if (phuThu.getText().replace(".", "").matches("\\d+"))
                hoaDon.setPhuThu(Double.parseDouble(phuThu.getText().replace(".", "")));
            else throw new IllegalArgumentException("Chỉ được nhập số");

            capNhatTongTien();
            hoaDonService.updateHoaDon(hoaDon);
        }
        catch (Exception e) {
            phuThu.setText(phuThu.getText().substring(0, phuThu.getLength() - 1));
            phuThu.setText(NumberFormatter.formatPrice(phuThu.getText()));
            phuThu.positionCaret(phuThu.getText().length());
        }
    }
//    @FXML
//    void tinhTienTraLai(KeyEvent event) {
//        try {
//            if (hoaDon == null) {
//                tienKhachDua.clear();
//                phuThu.clear();
//                throw new IllegalArgumentException("Hãy lập hoá đơn trước khi nhập");
//            }
//
//            if (event.getSource().equals(tienKhachDua)) {
//                if (tienKhachDua.getText().equals(""))
//                    return;
//                tienKhachDua.setText(NumberFormatter.formatPrice(tienKhachDua.getText()));
//                tienKhachDua.positionCaret(tienKhachDua.getText().length());
//
//                if (tienKhachDua.getText().replace(".", "").matches("\\d+"))
//                    tkd = Double.parseDouble(tienKhachDua.getText().replace(".", ""));
//                else throw new IllegalArgumentException("Chỉ được nhập số");
//
//            } else {
//                if (phuThu.getText().equals("")) {
//                    pt = 0.0;
//                    capNhatTienTraLai();
//                    return;
//                }
//
//                phuThu.setText(NumberFormatter.formatPrice(phuThu.getText()));
//                phuThu.positionCaret(phuThu.getText().length());
//
//                if (phuThu.getText().replace(".", "").matches("\\d+"))
//                    pt = Double.parseDouble(phuThu.getText().replace(".", ""));
//                else throw new IllegalArgumentException("Chỉ được nhập số");
//            }
//
//            if ((tkd + tienTL) >= (hoaDon.getTongTien() + pt)) {
//                tienTraLai.setText(NumberFormatter.formatPrice(String.valueOf((int) ((tkd + tienTL) - (hoaDon.getTongTien() + pt)))));
//            } else tienTraLai.clear();
//        }
//        catch (Exception e){
//            Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
//
//            if(event.getSource().equals(tienKhachDua)){
//                tienKhachDua.setText(tienKhachDua.getText().substring(0, tienKhachDua.getLength() - 1));
//                tienKhachDua.setText(NumberFormatter.formatPrice(tienKhachDua.getText()));
//                tienKhachDua.positionCaret(tienKhachDua.getText().length());
//            }
//
//            if (event.getSource().equals(phuThu)) {
//                phuThu.setText(phuThu.getText().substring(0, phuThu.getLength() - 1));
//                phuThu.setText(NumberFormatter.formatPrice(phuThu.getText()));
//                phuThu.positionCaret(phuThu.getText().length());
//            }
//
//            capNhatTienTraLai();
//        }
//    }

//    public void capNhatTienTraLai() {
//        if (hoaDon == null)
//            throw new IllegalArgumentException("Hãy lập hoá đơn trước khi nhập");
//
//        if ((tienTL + tkd) >= (hoaDon.getTongTien() + pt))
//            tienTraLai.setText(NumberFormatter.formatPrice(String.valueOf((int) ((tienTL + tkd) - (hoaDon.getTongTien() + pt)))));
//    }

    @FXML
    void timKiem(MouseEvent event) {
        pagination.setPageFactory(pageIndex -> {
            try {
                return createPageContent(pageIndex);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    void refreshSearch(MouseEvent event) {
        timTheoTen.clear();
        timTheoLoai.clear();
        timTheoGiaTT.clear();
        timTheoGiaTD.clear();

        this.timKiem(event);
    }

    @FXML
    void formatGia(KeyEvent event) {
        try {
            if (event.getSource().equals(timTheoGiaTT)) {
                if (timTheoGiaTT.getText().equals(""))
                    return;
                timTheoGiaTT.setText(NumberFormatter.formatPrice(timTheoGiaTT.getText()));
                timTheoGiaTT.positionCaret(timTheoGiaTT.getText().length());

                if (!timTheoGiaTT.getText().replace(".", "").matches("\\d+"))
                    throw new IllegalArgumentException("Chỉ được nhập số");

            } else {
                if (timTheoGiaTD.getText().equals(""))
                    return;
                timTheoGiaTD.setText(NumberFormatter.formatPrice(timTheoGiaTD.getText()));
                timTheoGiaTD.positionCaret(timTheoGiaTD.getText().length());

                if (!timTheoGiaTD.getText().replace(".", "").matches("\\d+"))
                    throw new IllegalArgumentException("Chỉ được nhập số");
            }
        }
        catch (Exception e){
            Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
            if(event.getSource().equals(timTheoGiaTT)){
                timTheoGiaTT.setText(timTheoGiaTT.getText().substring(0, timTheoGiaTT.getLength() - 1));
                timTheoGiaTT.setText(NumberFormatter.formatPrice(timTheoGiaTT.getText()));
                timTheoGiaTT.positionCaret(timTheoGiaTT.getText().length());
            }
            else {
                timTheoGiaTD.setText(timTheoGiaTD.getText().substring(0, timTheoGiaTD.getLength() - 1));
                timTheoGiaTD.setText(NumberFormatter.formatPrice(timTheoGiaTD.getText()));
                timTheoGiaTD.positionCaret(timTheoGiaTD.getText().length());
            }
        }
    }

    @FXML
    void nhapCCCD(KeyEvent event) {
        try {
            if (hoaDon == null) throw new IllegalArgumentException("Hãy lập hoá đơn trước khi nhập");

            if (tfCCCD.getText().length() == 12) {
                KhachHang khachHang = new KhachHang();
                khachHang.setCccd(tfCCCD.getText());
                khachHang = khachHangService.getKHByCCCD(tfCCCD.getText());
                tenKhachHang.setText(khachHang.getTenKhachHang());
                tfDiemTichLuyht.setText(String.valueOf(khachHang.getDiemTichLuy()));
                this.khachHang = khachHang;
            } else tenKhachHang.setEditable(false);

        } catch (NoResultException e) {
            khachHang = null;
            if (Notification.xacNhan("CCCD mới, bạn có muốn tạo khách hàng này?")) {
                tenKhachHang.requestFocus();
                tenKhachHang.setEditable(true);
            }
            else tfCCCD.setText(prevCCCD);
        } catch (Exception e) {
            Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
            tfCCCD.clear();
            tenKhachHang.clear();
        }

    }

    @FXML
    void nhapDTL(KeyEvent event) throws RemoteException {
        try {
            if(hoaDon == null) throw new IllegalArgumentException("Hãy lập hoá đơn trước khi nhập");

            if (tfDiemTichLuyDung.getText().isEmpty())
                throw new IllegalArgumentException("Đã huỷ áp dụng điểm tích luỹ");

            if (!tfDiemTichLuyDung.getText().matches("\\d+"))
                throw new IllegalArgumentException("Vui lòng nhập số");
        }
        catch (Exception e){
            Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
            tfDiemTichLuyDung.clear();
            tienTL = 0.0;
            capNhatTongTien();
//            capNhatTienTraLai();
        }
    }


    public HoaDon getHoaDon() {
        return this.hoaDon;
    }
}
