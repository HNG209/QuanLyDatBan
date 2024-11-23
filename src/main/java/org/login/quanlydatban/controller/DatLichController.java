package org.login.quanlydatban.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.StringConverter;
import org.login.quanlydatban.dao.BanDAO;
import org.login.quanlydatban.dao.HoaDonDAO;
import org.login.quanlydatban.dao.KhachHangDAO;
import org.login.quanlydatban.dao.LichDatDAO;
import org.login.quanlydatban.entity.Ban;
import org.login.quanlydatban.entity.HoaDon;
import org.login.quanlydatban.entity.KhachHang;
import org.login.quanlydatban.entity.LichDat;
import org.login.quanlydatban.entity.enums.*;
import org.login.quanlydatban.notification.Notification;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DatLichController implements Initializable {

    @FXML
    private Tab tabDatLich;

    @FXML
    private ComboBox<LoaiBan> cbLoaiBan;

    @FXML
    private ComboBox<Integer> cbGio;

    @FXML
    private ComboBox<Integer> cbPhut;

    @FXML
    private DatePicker tgNhanBan;

    private HoaDonDAO hoaDonDAO;

    private LichDatDAO lichDatDAO;

    private BanDAO banDAO;

    private Ban ban;

    @FXML
    private ComboBox<KhuVuc> khuVuc;

    @FXML
    private ComboBox<LoaiBan> loaiBan1;

    @FXML
    private TableView<Object[]> bookingTable;

    @FXML
    private TableColumn<Object[], String> loaiBanCol;

    @FXML
    private TableColumn<Object[], String> maLichDatCol;

    @FXML
    private TableColumn<Object[], String> ngayDatCol;

    @FXML
    private TableColumn<Object[], String> ngayNhanBanCol;

    @FXML
    private TableColumn<Object[], Integer> soLuongNguoiCol;

    @FXML
    private TableColumn<Object[], String> trangThaiCol;

    @FXML
    private TextField tenKhachHang;

    @FXML
    private TextField sdt;

    @FXML
    private TextField coc;

    @FXML
    private TextField soLuongNguoi;

    @FXML
    private FlowPane listViewBan;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField banTextField;

    private String prevSdt;

    private KhachHangDAO khachHangDAO;

    private HoaDon hoaDon;

    private static DatLichController instance;

    private static Parent root;

    public static DatLichController getInstance() throws IOException {
        if(instance == null)
            instance = loadTrangDatLich();
        return instance;
    }

    private static DatLichController loadTrangDatLich() throws IOException {
        FXMLLoader loader = new FXMLLoader(ChonBanController.class.getResource("/org/login/quanlydatban/views/TrangDatLich.fxml"));
        root = loader.load();
        return loader.getController();
    }

    public Parent getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbLoaiBan.getItems().addAll(LoaiBan.values());
        khuVuc.getItems().addAll(KhuVuc.values());
        loaiBan1.getItems().addAll(LoaiBan.values());

        hoaDonDAO = new HoaDonDAO();
        lichDatDAO = new LichDatDAO();
        khachHangDAO = new KhachHangDAO();
        banDAO = new BanDAO();

        maLichDatCol.setCellValueFactory(data -> new SimpleStringProperty((String)data.getValue()[0]));
        ngayDatCol.setCellValueFactory(data -> new SimpleStringProperty((String)data.getValue()[1]));
        ngayNhanBanCol.setCellValueFactory(data -> new SimpleStringProperty((String)data.getValue()[2]));
        loaiBanCol.setCellValueFactory(data -> new SimpleStringProperty((String)data.getValue()[3]));
        soLuongNguoiCol.setCellValueFactory(data -> new SimpleIntegerProperty((Integer) data.getValue()[4]).asObject());
        trangThaiCol.setCellValueFactory(data -> new SimpleStringProperty((String)data.getValue()[5]));

        for(int i = 1; i <= 24; i++)
            cbGio.getItems().add(i);

        for (int i = 0; i <= 55; i += 5)
            cbPhut.getItems().add(i);

        tenKhachHang.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // If newValue is false, the TextField has lost focus
                if(Notification.xacNhan("Lưu khách hàng này?")){
                    KhachHang khachHang = new KhachHang();
                    khachHang.setSdt(sdt.getText());
                    khachHang.setTenKhachHang(tenKhachHang.getText());

                    khachHangDAO.themKhachHang(khachHang);

//                    hoaDonDAO.updateHoaDon(hoaDon);
                    tenKhachHang.setEditable(false);
                }
            }
        });

        sdt.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // If newValue is false, the TextField has lost focus
                if(sdt.getText().length() < 10)
                    sdt.setText(prevSdt);
            }
        });

        tgNhanBan.setConverter(new StringConverter<>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            @Override
            public String toString(LocalDate date) {
                // Format the LocalDate to the desired string format
                return date != null ? date.format(dateFormatter) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                // Parse the string back to a LocalDate
                return string != null && !string.isEmpty() ? LocalDate.parse(string, dateFormatter) : null;
            }
        });

        listViewBan.prefWidthProperty().bind(scrollPane.widthProperty());
        listViewBan.prefHeightProperty().bind(scrollPane.heightProperty());

        refreshBang();
        refreshViewBan();
    }

    public void loadBang(Object[] objects) {
        bookingTable.getItems().add(objects);
    }

    public void setBan(Ban ban) {
        this.ban = ban;
        banTextField.setText(ban.getMaBan());
    }

    public void refreshViewBan() {
        listViewBan.getChildren().clear();
        for (Ban i : banDAO.readAll()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/uicomponents/CardBan_TrangDatLich.fxml"));
            try {
                AnchorPane pane = loader.load();
                CardBanDatLichController controller = loader.getController();

                controller.setBan(i);

                listViewBan.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void refreshBang() {
        bookingTable.getItems().clear();
        lichDatDAO.getDSLichDat().forEach(i -> loadBang(new Object[] {i.getMaLichDat(),
                i.getThoiGianDat().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")),
                i.getThoiGianNhanBan().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")),
                i.getHoaDon().getBan().getLoaiBan().toString(),
                i.getSoLuongNguoi(),
                i.getHoaDon().getTrangThaiHoaDon().toString()}));
    }

    public void loadDatLich() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangDatLich.fxml"));
        TabPane pane = loader.load();

        TrangChuController.getBorderPaneStatic().setCenter(pane);
    }

    @FXML
    void nhanBan(ActionEvent event) {
        try {
            if(bookingTable.getSelectionModel().getSelectedIndex() != -1){
                Ban b = hoaDon.getBan();

                //check if there're any served table at the time
                for(Ban i : banDAO.readByStatus(TrangThaiBan.DANG_PHUC_VU)){
                    if(i.getMaBan().equals(b.getMaBan()))
                        throw new IllegalArgumentException("Không thể nhận bàn, vui lòng thanh toán bàn hiện tại trước khi nhận bàn");
                }

                b.setTrangThaiBan(TrangThaiBan.DANG_PHUC_VU);
                banDAO.updateBan(b);

                hoaDon.setTrangThaiHoaDon(TrangThaiHoaDon.CHUA_THANH_TOAN);
                hoaDonDAO.updateHoaDon(hoaDon);
                System.out.println(hoaDon);
            }
            else throw new IllegalArgumentException("Vui lòng chọn 1 dòng");
        }
        catch (Exception e) {
            Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    @FXML
    void chonMon(ActionEvent event) throws IOException {
        try {
            if (ban != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangDatMon.fxml"));
                AnchorPane pane = loader.load();
                DatMonController controller = loader.getController();
                controller.setPageSelected(1);
                controller.setBan(ban);

                if (hoaDon != null)
                    controller.setHoaDon(hoaDon);
                else throw new IllegalArgumentException("Vui lòng chọn dòng tương ứng");

                TrangChuController.getBorderPaneStatic().setCenter(pane);
            } else throw new IllegalArgumentException("Vui lòng chọn bàn");
        }
        catch (Exception e){
            Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
        }
    }


    @FXML
    void chonDong(MouseEvent event) {
        LichDat lichDat = lichDatDAO.getLichDat((String) bookingTable.getSelectionModel().getSelectedItem()[0]);

        tgNhanBan.setValue(lichDat.getThoiGianNhanBan().toLocalDate());
        cbGio.getSelectionModel().select((Integer) lichDat.getThoiGianNhanBan().getHour());
        cbPhut.getSelectionModel().select((Integer) lichDat.getThoiGianNhanBan().getMinute());
        sdt.setText(lichDat.getKhachHang().getSdt());
        prevSdt = sdt.getText();
        tenKhachHang.setText(lichDat.getKhachHang().getTenKhachHang());
        soLuongNguoi.setText(String.valueOf(lichDat.getSoLuongNguoi()));
        ban = lichDat.getHoaDon().getBan();
        banTextField.setText(ban.getMaBan());
        hoaDon = lichDat.getHoaDon();
    }

    @FXML
    void datLich(ActionEvent event) {
        try {
            LichDat lichDat = new LichDat();

            lichDat.setThoiGianDat(LocalDateTime.now());

            lichDat.setNhanVien(TrangChuController.getTaiKhoan().getNhanVien());

            if (!prevSdt.isEmpty())
                lichDat.setKhachHang(khachHangDAO.getKHBySDT(prevSdt));
            else throw new IllegalArgumentException("Vui lòng nhập số điện thoại của khách hàng");

            if (!soLuongNguoi.getText().isEmpty())
                if (Integer.parseInt(soLuongNguoi.getText()) > 0)
                    lichDat.setSoLuongNguoi(Integer.parseInt(soLuongNguoi.getText()));
                else throw new IllegalArgumentException("Số lượng người > 0");
            else throw new IllegalArgumentException("Vui lòng nhập số lượng người");

            LocalDate date = tgNhanBan.getValue();
            HoaDon hoaDon = new HoaDon();
            hoaDon.setNgayLap(LocalDate.now());
            hoaDon.setTrangThaiHoaDon(TrangThaiHoaDon.DA_DAT);
            if (ban != null)
                hoaDon.setBan(ban);
            else throw new IllegalArgumentException("Vui lòng chọn bàn");
            hoaDon.setMaHoaDon(date);

            lichDat.setHoaDon(hoaDon);

            if (tgNhanBan.getValue() != null) {
                if (cbGio.getValue() != null && cbPhut.getValue() != null)
                    lichDat.setThoiGianNhanBan(LocalDateTime.of(tgNhanBan.getValue(), LocalTime.of(cbGio.getValue(), cbPhut.getValue())));
                else throw new IllegalArgumentException("Vui lòng chọn giờ");
            } else throw new IllegalArgumentException("Vui lòng chọn thời gian nhận bàn");


            hoaDonDAO.lapHoaDon(hoaDon);
            lichDatDAO.taoLichDat(lichDat);

            refreshBang();
        } catch (Exception e) {
            Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    @FXML
    void resetFilterBan(MouseEvent event) {

    }

    @FXML
    void resetFilterLich(MouseEvent event) {

    }

    @FXML
    void sdtEnter(KeyEvent event) {
        if(sdt.getText().length() == 10) {
            try {
                KhachHang khachHang = khachHangDAO.getKHBySDT(sdt.getText());
                tenKhachHang.setText(khachHang.getTenKhachHang());
                prevSdt = sdt.getText();
            }
            catch (NoResultException e) {
                if(Notification.xacNhan("Số điện thoại mới, bạn có muốn tạo khách hàng này?")){
                    tenKhachHang.requestFocus();
                    tenKhachHang.setEditable(true);
                }
                else sdt.setText(prevSdt);
            }
        }
        else tenKhachHang.setEditable(false);
    }
}
