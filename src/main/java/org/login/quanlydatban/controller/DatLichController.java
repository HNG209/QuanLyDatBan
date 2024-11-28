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
import org.login.quanlydatban.utilities.NumberFormatter;

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
    private TextField cocKD;

    @FXML
    private TextField soLuongNguoi;

    @FXML
    private FlowPane listViewBan;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField banTextField;

    @FXML
    private Button btnChonMon;

    @FXML
    private Button btnNhanBan;

    @FXML
    private Button btnHuyLich;

    @FXML
    private TextArea txtGhiChu;

    @FXML
    private TextField tienTraLai;

    @FXML
    private Button btnDatLich;

    private String prevSdt;

    double c = 0.0;

    double ckd = 0.0;

    private KhachHangDAO khachHangDAO;

    private HoaDon hoaDon;

    private Ban ban;

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
            if (!newValue && tenKhachHang.isEditable()) { // If newValue is false, the TextField has lost focus
                if(Notification.xacNhan("Lưu khách hàng này?")){
                    KhachHang khachHang = new KhachHang();
                    khachHang.setSdt(sdt.getText());
                    khachHang.setTenKhachHang(tenKhachHang.getText());

                    prevSdt = sdt.getText();

                    khachHangDAO.themKhachHang(khachHang);
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

    public void refeshTextFieldsAndButtons() {
        btnChonMon.setDisable(true);
        btnHuyLich.setDisable(true);
        btnNhanBan.setDisable(true);
        btnDatLich.setDisable(false);
        tenKhachHang.setEditable(false);

        tgNhanBan.setValue(null);
        cbGio.getSelectionModel().clearSelection();
        cbPhut.getSelectionModel().clearSelection();
        sdt.clear();
        banTextField.clear();
        tenKhachHang.clear();
        soLuongNguoi.clear();
        txtGhiChu.clear();
        coc.clear();

        prevSdt = null;
        hoaDon = null;
        ban = null;
    }

    @FXML
    void nhanBan(ActionEvent event) {
        try {
            if(bookingTable.getSelectionModel().getSelectedIndex() != -1){
                Ban b = hoaDon.getBan();
                LichDat lichDat = lichDatDAO.getLichDat((String) bookingTable.getSelectionModel().getSelectedItem()[0]);

                if(lichDat.getThoiGianNhanBan().isAfter(LocalDateTime.now()))
                    throw new IllegalArgumentException("Chưa đến thời gian để nhận bàn");

                //check if there're any served table at the time
                for(Ban i : banDAO.readByStatus(TrangThaiBan.DANG_PHUC_VU)){
                    if(i.getMaBan().equals(b.getMaBan()))
                        throw new IllegalArgumentException("Không thể nhận bàn, vui lòng thanh toán bàn hiện tại trước khi nhận bàn");
                }

                if(Notification.xacNhan("Nhận bàn?")){
                    b.setTrangThaiBan(TrangThaiBan.DANG_PHUC_VU);
                    banDAO.updateBan(b);

                    hoaDon.setTrangThaiHoaDon(TrangThaiHoaDon.CHUA_THANH_TOAN);
                    hoaDonDAO.updateHoaDon(hoaDon);
                    refreshBang();
                    refreshViewBan();
                    refeshTextFieldsAndButtons();
                }
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
        txtGhiChu.setText(lichDat.getGhiChu());
        tenKhachHang.setEditable(false);

        if(hoaDon.getTrangThaiHoaDon() != TrangThaiHoaDon.DA_DAT){
            sdt.setEditable(false);
            soLuongNguoi.setEditable(false);
            cbGio.setEditable(false);
            cbPhut.setEditable(false);
            tgNhanBan.setEditable(false);

            btnDatLich.setDisable(false);
            btnChonMon.setDisable(true);
            btnHuyLich.setDisable(true);
            btnNhanBan.setDisable(true);
        }
        else{
            sdt.setEditable(true);
            soLuongNguoi.setEditable(true);
            cbGio.setEditable(true);
            cbPhut.setEditable(true);
            tgNhanBan.setEditable(true);

            btnDatLich.setDisable(true);
            btnNhanBan.setDisable(false);
            btnChonMon.setDisable(false);
            btnHuyLich.setDisable(false);
        }
    }

    @FXML
    void datLich(ActionEvent event) {
        try {
            LichDat lichDat = new LichDat();

            lichDat.setThoiGianDat(LocalDateTime.now());

            lichDat.setNhanVien(TrangChuController.getTaiKhoan().getNhanVien());

            lichDat.setGhiChu(txtGhiChu.getText());

            if (prevSdt != null && !prevSdt.isEmpty())
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
            hoaDon.setKhachHang(khachHangDAO.getKHBySDT(prevSdt));
            if (ban != null)
                hoaDon.setBan(ban);
            else throw new IllegalArgumentException("Vui lòng chọn bàn");

            if (tgNhanBan.getValue() != null) {
                if (cbGio.getValue() != null && cbPhut.getValue() != null)
                    lichDat.setThoiGianNhanBan(LocalDateTime.of(tgNhanBan.getValue(), LocalTime.of(cbGio.getValue(), cbPhut.getValue())), ban);
                else throw new IllegalArgumentException("Vui lòng chọn giờ");
            } else throw new IllegalArgumentException("Vui lòng chọn thời gian nhận bàn");

            lichDat.setTienCoc(c);

            hoaDon.setMaHoaDon(date);
            lichDat.setHoaDon(hoaDon);

            hoaDonDAO.lapHoaDon(hoaDon);
            lichDatDAO.taoLichDat(lichDat);

            Notification.thongBao("Đặt lịch thành công, mã lịch đặt: " + lichDat.getMaLichDat(), Alert.AlertType.INFORMATION);
            refreshBang();
        } catch (Exception e) {
            Notification.thongBao(e.getMessage(), e.getStackTrace().clone()[0].toString(), Alert.AlertType.WARNING);
        }
    }

    @FXML
    void cocEnter(KeyEvent event) {
        if(event.getSource().equals(coc)){
            if(coc.getText().equals(""))
                return;
            coc.setText(NumberFormatter.formatPrice(coc.getText()));
            coc.positionCaret(coc.getText().length());

            if (coc.getText().replace(".", "").matches("\\d+"))
                c = Double.parseDouble(coc.getText().replace(".", ""));
            else {
                Notification.thongBao("Chỉ được nhập số", Alert.AlertType.INFORMATION);
                coc.setText(coc.getText().substring(0, coc.getLength() - 1));
                coc.setText(NumberFormatter.formatPrice(coc.getText()));
                coc.positionCaret(coc.getText().length());
            }
            capNhatTienTraLai();
        }
        else {
            if (cocKD.getText().equals("")){
                ckd = 0.0;
                capNhatTienTraLai();
                return;
            }

            cocKD.setText(NumberFormatter.formatPrice(cocKD.getText()));
            cocKD.positionCaret(cocKD.getText().length());

            if (cocKD.getText().replace(".", "").matches("\\d+"))
                ckd = Double.parseDouble(cocKD.getText().replace(".", ""));
            else {
                Notification.thongBao("Chỉ được nhập số", Alert.AlertType.INFORMATION);
                cocKD.setText(cocKD.getText().substring(0, cocKD.getLength() - 1));
                cocKD.setText(NumberFormatter.formatPrice(cocKD.getText()));
                cocKD.positionCaret(cocKD.getText().length());
            }
            capNhatTienTraLai();
        }
    }

    public void capNhatTienTraLai() {
        tienTraLai.setText(NumberFormatter.formatPrice(String.valueOf((int) (c - ckd))));
    }

    @FXML
    void refreshInput(MouseEvent event) {
        refeshTextFieldsAndButtons();
    }

    @FXML
    void huyLich(ActionEvent event) {
        try{
            if(hoaDon != null) {
                if(Notification.xacNhan("Xác nhận huỷ lịch này?, không thể hoàn tác")){
                    hoaDon.setTrangThaiHoaDon(TrangThaiHoaDon.DA_HUY);
                    hoaDonDAO.updateHoaDon(hoaDon);

                    refreshBang();
                    refeshTextFieldsAndButtons();
                }
            }
        }
        catch (Exception e) {
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
