package org.login.quanlydatban.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
//import org.login.quanlydatban.dao.*;
import org.login.quanlydatban.utilities.RMIServiceUtils;
import org.login.service.*;
import org.login.entity.Ban;
import org.login.entity.HoaDon;
import org.login.entity.KhachHang;
import org.login.entity.LichDat;
import org.login.entity.enums.*;
import org.login.quanlydatban.notification.Notification;
import org.login.quanlydatban.utilities.NumberFormatter;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class DatLichController implements Initializable {

    @FXML
    private ComboBox<Integer> cbGio;

    @FXML
    private ComboBox<Integer> cbPhut;

    @FXML
    private ComboBox<LoaiBan> cbBanLoaiBan;

    @FXML
    private ComboBox<KhuVuc> cbKhuVuc;

    @FXML
    private ComboBox<TrangThaiHoaDon> cbTrangThai;

    @FXML
    private DatePicker tgNhanBan;

    @FXML
    private DatePicker tKngayNhanBan;

    @FXML
    private TableView<Object[]> tableLichDat;

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
    private TextField tfTenKhachHang;

    @FXML
    private TextField tfCCCD;

    @FXML
    private TextField tfCoc;

    @FXML
    private TextField tfTKmaBan;

    @FXML
    private TextField tfTKCCCD;

    @FXML
    private TextField tfSoLuongNguoi;

    @FXML
    private TextField tfBan;

    @FXML
    private TextField tfTKmaLichDat;

    @FXML
    private FlowPane listViewBan;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button btnNhanBan;

    @FXML
    private Button btnHuyLich;

    @FXML
    private Button btnChonMon;

    @FXML
    private Button btnDatLich;

    @FXML
    private Button btnXuat;

    @FXML
    private TextArea txtGhiChu;

    double c = 0.0;

    double ckd = 0.0;

    private KhachHangService khachHangService;

    private CTHDService cthdService;

    private HoaDonService hoaDonService;

    private LichDatService lichDatService;

    private BanService banService;

    private Ban ban;

    private HoaDon hoaDon;

    private String prevCCCD = "";

    private LichDat selectedLD;

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
        cbKhuVuc.getItems().add(null);
        cbKhuVuc.getItems().addAll(KhuVuc.values());

        cbBanLoaiBan.getItems().add(null);
        cbBanLoaiBan.getItems().addAll(LoaiBan.values());

        cbTrangThai.getItems().add(null);
        cbTrangThai.getItems().addAll(TrangThaiHoaDon.values());

        try {
            hoaDonService = RMIServiceUtils.useHoaDonService();
            lichDatService = RMIServiceUtils.useLichDatService();
            khachHangService = RMIServiceUtils.useKhachHangService();
            cthdService = RMIServiceUtils.useCTHDService();
            banService = RMIServiceUtils.useBanService();
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            throw new RuntimeException(e);
        }


        maLichDatCol.setCellValueFactory(data -> new SimpleStringProperty((String)data.getValue()[0]));
        ngayDatCol.setCellValueFactory(data -> new SimpleStringProperty((String)data.getValue()[1]));
        ngayNhanBanCol.setCellValueFactory(data -> new SimpleStringProperty((String)data.getValue()[2]));
        loaiBanCol.setCellValueFactory(data -> new SimpleStringProperty((String)data.getValue()[3]));
        soLuongNguoiCol.setCellValueFactory(data -> new SimpleIntegerProperty((Integer) data.getValue()[4]).asObject());
        trangThaiCol.setCellValueFactory(data -> new SimpleStringProperty((String)data.getValue()[5]));

        for(int i = 7; i <= 20; i++)
            cbGio.getItems().add(i);

        for (int i = 0; i <= 55; i += 5)
            cbPhut.getItems().add(i);

        tfTenKhachHang.focusedProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!newValue && tfTenKhachHang.isEditable()) { // If newValue is false, the TextField has lost focus
                    if (Notification.xacNhan("Lưu khách hàng này?")) {
                        KhachHang khachHang = new KhachHang();
                        khachHang.setCccd(tfCCCD.getText());
                        khachHang.setTenKhachHang(tfTenKhachHang.getText());

                        prevCCCD = tfCCCD.getText();

                        khachHangService.themKhachHang(khachHang);
                        tfTenKhachHang.setEditable(false);
                    }
                }
            }
            catch (Exception e) {
                Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
            }
        });

        tfCCCD.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // If newValue is false, the TextField has lost focus
                if(tfCCCD != null && tfCCCD.getText().length() < 12)
                    tfCCCD.setText(prevCCCD);
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

        try {
            refreshBang();
            refreshViewBan();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        refeshTextFieldsAndButtons();
    }

    public void loadBang(Object[] objects) {
        tableLichDat.getItems().add(objects);
    }

    public void setBan(Ban ban) {
        try{
            if(selectedLD != null){
                if(hoaDon.getTrangThaiHoaDon() != TrangThaiHoaDon.DA_DAT) return;

                if(selectedLD.getThoiGianNhanBan().isBefore(LocalDateTime.now())){
                    if(ban.getTrangThaiBan() == TrangThaiBan.DANG_PHUC_VU)
                        throw new IllegalArgumentException("Bàn hiện đang phục vụ, vui lòng chọn bàn khác");
                }

                int sl = 0;
                switch (ban.getLoaiBan()){
                    case LoaiBan.BAN_2_NGUOI -> sl = 2;
                    case LoaiBan.BAN_5_NGUOI -> sl = 5;
                    case LoaiBan.BAN_10_NGUOI -> sl = 10;
                }

                if(!lichDatService.getDSLichDatBy(selectedLD.getThoiGianNhanBan(), ban).isEmpty())
                    throw new IllegalArgumentException("Bàn này đã tồn tại lịch đặt có cùng thời gian");

                if(selectedLD.getSoLuongNguoi() > sl){
                    if(Notification.xacNhan("Số lượng khách trong lịch đặt vượt quá sức chứa của bàn đã chọn. Bạn có chắc chắn muốn đổi bàn không?")) {
                        hoaDon.setBan(ban);

                        hoaDon = hoaDonService.updateHoaDon(hoaDon);
                        refreshBang();
                        this.ban = ban;
                        tfBan.setText(ban.getMaBan());
                    }
                }
                else {
                    if(Notification.xacNhan("Xác nhận đổi bàn cho lịch đặt")) {
                        hoaDon.setBan(ban);

                        hoaDon = hoaDonService.updateHoaDon(hoaDon);
                        refreshBang();
                        this.ban = ban;
                        tfBan.setText(ban.getMaBan());
                    }
                }
            }
            else {
                this.ban = ban;
                tfBan.setText(ban.getMaBan());
            }
        }
        catch (Exception e){
            Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    public void refreshViewBan() throws RemoteException {
        listViewBan.getChildren().clear();
        for (Ban i : banService.readAll()){
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

    public void refreshViewBan(List<Ban> list) {
        listViewBan.getChildren().clear();
        for (Ban i : list){
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

    public void refreshBang() throws RemoteException {
        tableLichDat.getItems().clear();
        List<LichDat> lichDatList = lichDatService.getDSLichDat();
        lichDatList.forEach(i -> loadBang(new Object[]{i.getMaLichDat(),
                i.getThoiGianDat().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")),
                i.getThoiGianNhanBan().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")),
                i.getHoaDon().getBan().getLoaiBan().toString(),
                i.getSoLuongNguoi(),
                i.getHoaDon().getTrangThaiHoaDon() != TrangThaiHoaDon.DA_DAT ? i.getHoaDon().getTrangThaiHoaDon().toString()
                        : i.getThoiGianNhanBan().isBefore(LocalDateTime.now()) ? TrangThaiHoaDon.DA_DAT + " (có thể nhận bàn)" : TrangThaiHoaDon.DA_DAT.toString()}));
    }

    public void refreshBang(List<LichDat> list) {
        tableLichDat.getItems().clear();
        list.forEach(i -> loadBang(new Object[]{i.getMaLichDat(),
                i.getThoiGianDat().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")),
                i.getThoiGianNhanBan().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")),
                i.getHoaDon().getBan().getLoaiBan().toString(),
                i.getSoLuongNguoi(),
                i.getHoaDon().getTrangThaiHoaDon() != TrangThaiHoaDon.DA_DAT ? i.getHoaDon().getTrangThaiHoaDon().toString()
                        : i.getThoiGianNhanBan().isBefore(LocalDateTime.now()) ? TrangThaiHoaDon.DA_DAT + " (có thể nhận bàn)" : TrangThaiHoaDon.DA_DAT.toString()}));
    }

    public void capNhatCoc() throws RemoteException {
        double tongTien = hoaDonService.tinhTongTien(hoaDon);
        if(tongTien < 200_000.0)
            selectedLD.setTienCoc(0.0);
        else selectedLD.setTienCoc(tongTien / 2);

        c = selectedLD.getTienCoc();

        tfCoc.setText(NumberFormatter.formatPrice(String.valueOf((int) selectedLD.getTienCoc())));
        lichDatService.capNhatLichDat(selectedLD);
    }

    public void refeshTextFieldsAndButtons() {
        btnChonMon.setDisable(true);
        btnHuyLich.setDisable(true);
        btnNhanBan.setDisable(true);
        btnDatLich.setDisable(false);
        btnXuat.setDisable(true);
        tfTenKhachHang.setEditable(false);
        tfCCCD.setEditable(true);
        tfSoLuongNguoi.setEditable(true);

        tgNhanBan.setValue(null);
        cbGio.setValue(null);
        cbPhut.setValue(null);
        tfCCCD.clear();
        tfBan.clear();
        tfTenKhachHang.clear();
        tfSoLuongNguoi.clear();
        txtGhiChu.clear();
        tfCoc.clear();

        prevCCCD = null;
        hoaDon = null;
        ban = null;
        selectedLD = null;
    }

    @FXML
    void nhanBan(ActionEvent event) {
        try {
            if(tableLichDat.getSelectionModel().getSelectedIndex() != -1){
                Ban b = hoaDon.getBan();
                LichDat lichDat = lichDatService.getLichDat((String) tableLichDat.getSelectionModel().getSelectedItem()[0]);

                if(lichDat.getThoiGianNhanBan().isAfter(LocalDateTime.now()))
                    throw new IllegalArgumentException("Chưa đến thời gian để nhận bàn");

                //check if there're any served table at the time
                for(Ban i : banService.readByStatus(TrangThaiBan.DANG_PHUC_VU)) {
                    if (i.getMaBan().equals(b.getMaBan()))
                        throw new IllegalArgumentException("Không thể nhận bàn, vui lòng thanh toán bàn " + i.getMaBan() + " trước khi nhận bàn");
                }

                if(Notification.xacNhan("Nhận bàn?")){
                    b.setTrangThaiBan(TrangThaiBan.DANG_PHUC_VU);
                    banService.updateBan(b);

                    hoaDon.setTrangThaiHoaDon(TrangThaiHoaDon.CHUA_THANH_TOAN);
                    hoaDonService.updateHoaDon(hoaDon);
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
            if (ban == null)
                throw new IllegalArgumentException("Vui lòng chọn bàn");

            if(selectedLD.getThoiGianNhanBan().isBefore(LocalDateTime.now()))
                throw new IllegalArgumentException("Không thể chọn món do đã tới thời gian nhận bàn");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/TrangDatMon.fxml"));
            AnchorPane pane = loader.load();
            DatMonController controller = loader.getController();
            controller.setPageSelected(1);
            controller.setBan(ban);

            if (hoaDon != null)
                controller.setHoaDon(hoaDon);
            else throw new IllegalArgumentException("Vui lòng chọn dòng tương ứng");

            TrangChuController.getBorderPaneStatic().setCenter(pane);
        }
        catch (Exception e){
            Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    @FXML
    void xuatPhieuDat(ActionEvent event) throws IOException {
        if(selectedLD != null && selectedLD.getHoaDon().getTrangThaiHoaDon() == TrangThaiHoaDon.DA_DAT){
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/login/quanlydatban/views/TrangPhieuDatLich.fxml")));
            AnchorPane pane = loader.load();

            PhieuDatLichController controller = loader.getController();
            controller.setLichDat(selectedLD);
            Stage stage = new Stage();

            stage.setScene(new Scene(pane));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
    }

    @FXML
    void chonDong(MouseEvent event) throws RemoteException {
        selectedLD = lichDatService.getLichDat((String) tableLichDat.getSelectionModel().getSelectedItem()[0]);

        tgNhanBan.setValue(selectedLD.getThoiGianNhanBan().toLocalDate());
        cbGio.getSelectionModel().select((Integer) selectedLD.getThoiGianNhanBan().getHour());
        cbPhut.getSelectionModel().select((Integer) selectedLD.getThoiGianNhanBan().getMinute());
        tfCCCD.setText(selectedLD.getKhachHang().getCccd());
        prevCCCD = tfCCCD.getText();
        tfTenKhachHang.setText(selectedLD.getKhachHang().getTenKhachHang());
        tfSoLuongNguoi.setText(String.valueOf(selectedLD.getSoLuongNguoi()));
        ban = selectedLD.getHoaDon().getBan();
        tfBan.setText(ban.getMaBan());
        hoaDon = selectedLD.getHoaDon();
        txtGhiChu.setText(selectedLD.getGhiChu());
        tfTenKhachHang.setEditable(false);
        tfCoc.setText(NumberFormatter.formatPrice(String.valueOf((int) selectedLD.getTienCoc())));
        c = selectedLD.getTienCoc();

        if(hoaDon.getTrangThaiHoaDon() != TrangThaiHoaDon.DA_DAT){
            tfCCCD.setEditable(false);
            tfSoLuongNguoi.setEditable(false);
            tgNhanBan.setEditable(false);

            btnDatLich.setDisable(false);
            btnChonMon.setDisable(true);
            btnHuyLich.setDisable(true);
            btnNhanBan.setDisable(true);
            btnXuat.setDisable(true);
        }
        else{
            tfCCCD.setEditable(true);
            tfSoLuongNguoi.setEditable(true);
            tgNhanBan.setEditable(true);

            btnDatLich.setDisable(true);
            btnNhanBan.setDisable(false);
            btnChonMon.setDisable(false);
            btnHuyLich.setDisable(false);
            btnXuat.setDisable(false);
        }
    }

    @FXML
    void nhapSLNguoi(KeyEvent event) {
        try {
            if(tfSoLuongNguoi.getText().isEmpty()){
                refreshViewBan();
                return;
            }
            if(tfSoLuongNguoi.getText().matches("\\d+")){
                int sl = Integer.parseInt(tfSoLuongNguoi.getText());
                if(sl <= 2 && sl > 0) {
                    cbBanLoaiBan.setValue(LoaiBan.BAN_2_NGUOI);
                    refreshViewBan(banService.getListBanBy("", null, LoaiBan.BAN_2_NGUOI, null));
                } else if (sl > 2 && sl <= 5) {
                    cbBanLoaiBan.setValue(LoaiBan.BAN_5_NGUOI);
                    refreshViewBan(banService.getListBanBy("", null, LoaiBan.BAN_5_NGUOI, null));
                }
                else if (sl > 5 && sl <= 10) {
                    cbBanLoaiBan.setValue(LoaiBan.BAN_10_NGUOI);
                    refreshViewBan(banService.getListBanBy("", null, LoaiBan.BAN_10_NGUOI, null));
                }
            }
            else throw new IllegalArgumentException("Vui lòng nhập số");
        }
        catch (Exception e) {
            Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    @FXML
    void datLich(ActionEvent event) {
        try {
            LichDat lichDat = new LichDat();

            lichDat.setThoiGianDat(LocalDateTime.now());

            lichDat.setNhanVien(TrangChuController.getTaiKhoan().getNhanVien());

            lichDat.setGhiChu(txtGhiChu.getText());

            if (prevCCCD != null && !prevCCCD.isEmpty())
                lichDat.setKhachHang(khachHangService.getKHByCCCD(prevCCCD));
            else throw new IllegalArgumentException("Vui lòng nhập CCCD của khách hàng");

            if (!tfSoLuongNguoi.getText().isEmpty())
                if (Integer.parseInt(tfSoLuongNguoi.getText()) > 0)
                    lichDat.setSoLuongNguoi(Integer.parseInt(tfSoLuongNguoi.getText()));
                else throw new IllegalArgumentException("Số lượng người > 0");
            else throw new IllegalArgumentException("Vui lòng nhập số lượng người");

            LocalDate date = tgNhanBan.getValue();
            HoaDon hoaDon = new HoaDon();
            hoaDon.setNgayLap(LocalDate.now());
//            hoaDon.setNhanVien(TrangChuController.getTaiKhoan().getNhanVien());
            hoaDon.setTrangThaiHoaDon(TrangThaiHoaDon.DA_DAT);
            hoaDon.setKhachHang(khachHangService.getKHByCCCD(prevCCCD));
            if (ban != null)
                hoaDon.setBan(ban);
            else throw new IllegalArgumentException("Vui lòng chọn bàn");

            if (tgNhanBan.getValue() != null) {
                if (cbGio.getValue() != null && cbPhut.getValue() != null)
                    lichDat.setThoiGianNhanBan(LocalDateTime.of(tgNhanBan.getValue(), LocalTime.of(cbGio.getValue(), cbPhut.getValue())));
                else throw new IllegalArgumentException("Vui lòng chọn giờ");
            } else throw new IllegalArgumentException("Vui lòng chọn thời gian nhận bàn");

            lichDat.setTienCoc(0);
            hoaDon.setNgayLap(date);

            hoaDon = hoaDonService.lapHoaDon(hoaDon);
            lichDat.setHoaDon(hoaDon);
            lichDat = lichDatService.taoLichDat(lichDat);


            Notification.thongBao("Đặt lịch thành công, mã lịch đặt: " + lichDat.getMaLichDat(), Alert.AlertType.INFORMATION);
            refreshBang();

            tableLichDat.getSelectionModel().select(tableLichDat.getItems().size() - 1);
            chonDong(null);
        } catch (Exception e) {
           Notification.thongBao(e.getMessage(), e.getStackTrace().clone()[0].toString(), Alert.AlertType.WARNING);
        }
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
                    hoaDonService.updateHoaDon(hoaDon);

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
    void resetFilterBan(MouseEvent event) throws RemoteException {
        cbBanLoaiBan.setValue(null);
        cbKhuVuc.setValue(null);
        tfTKmaBan.clear();

        refreshViewBan();
    }

    @FXML
    void resetFilterLich(MouseEvent event) throws RemoteException {
        tfTKmaLichDat.clear();
        tKngayNhanBan.setValue(null);
        cbTrangThai.setValue(null);
        tfTKCCCD.clear();

        refreshBang();
    }

    @FXML
    void searchLich(MouseEvent event) throws RemoteException {
        String maLD = tfTKmaLichDat.getText();
        LocalDate ngayNhanBan = tKngayNhanBan.getValue();
        TrangThaiHoaDon trangThaiHoaDon = cbTrangThai.getSelectionModel().getSelectedItem();
        String cccd = tfTKCCCD.getText();

        refreshBang(lichDatService.getDSLichDatBy(maLD, ngayNhanBan, trangThaiHoaDon, cccd));
    }

    @FXML
    void searchBan(MouseEvent event) throws RemoteException {
        String maBan = tfTKmaBan.getText();
        LoaiBan loaiBan = cbBanLoaiBan.getSelectionModel().getSelectedItem();
        KhuVuc khuVuc = cbKhuVuc.getSelectionModel().getSelectedItem();

        refreshViewBan(banService.getListBanBy(maBan, null, loaiBan, khuVuc));
    }

    @FXML
    void nhapCCCD(KeyEvent event) {
        try {
            if (tfCCCD.getText().length() == 12) {
                KhachHang khachHang = new KhachHang();
                khachHang.setCccd(tfCCCD.getText());
                khachHang = khachHangService.getKHByCCCD(tfCCCD.getText());
                tfTenKhachHang.setText(khachHang.getTenKhachHang());
                prevCCCD = tfCCCD.getText();
            }
        } catch (NoResultException e) {
            if (Notification.xacNhan("CCCD mới, bạn có muốn tạo khách hàng này?")) {
                tfTenKhachHang.requestFocus();
                tfTenKhachHang.setEditable(true);
            } else tfCCCD.setText(prevCCCD);
        } catch (Exception e) {
            Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
            tfCCCD.clear();
            tfTenKhachHang.clear();
        }
    }
}
