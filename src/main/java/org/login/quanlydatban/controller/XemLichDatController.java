package org.login.quanlydatban.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
//import org.login.quanlydatban.dao.BanDAO;
//import org.login.quanlydatban.dao.HoaDonDAO;
//import org.login.quanlydatban.dao.LichDatDAO;
import org.login.quanlydatban.utilities.RMIServiceUtils;
import org.login.service.*;

import org.login.entity.Ban;
import org.login.entity.HoaDon;
import org.login.entity.LichDat;
import org.login.entity.enums.TrangThaiBan;
import org.login.entity.enums.TrangThaiHoaDon;
import org.login.quanlydatban.notification.Notification;
import org.login.quanlydatban.utilities.NumberFormatter;

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
import java.util.ResourceBundle;

public class XemLichDatController implements Initializable {

    @FXML
    private VBox boxChieu;

    @FXML
    private VBox boxChieuCN;

    @FXML
    private VBox boxChieuT2;

    @FXML
    private VBox boxChieuT3;

    @FXML
    private VBox boxChieuT4;

    @FXML
    private VBox boxChieuT5;

    @FXML
    private VBox boxChieuT6;

    @FXML
    private VBox boxChieuT7;

    @FXML
    private VBox boxSang;

    @FXML
    private VBox boxSangCN;

    @FXML
    private VBox boxSangT2;

    @FXML
    private VBox boxSangT3;

    @FXML
    private VBox boxSangT4;

    @FXML
    private VBox boxSangT5;

    @FXML
    private VBox boxSangT6;

    @FXML
    private VBox boxSangT7;

    @FXML
    private VBox boxToi;

    @FXML
    private VBox boxToiCN;

    @FXML
    private VBox boxToiT2;

    @FXML
    private VBox boxToiT3;

    @FXML
    private VBox boxToiT4;

    @FXML
    private VBox boxToiT5;

    @FXML
    private VBox boxToiT6;

    @FXML
    private VBox boxToiT7;

    @FXML
    private Label labelChieu;

    @FXML
    private Label labelSang;

    @FXML
    private Label labelToi;


    @FXML
    private Label t2CurrentDate;

    @FXML
    private Label t3CurrentDate;

    @FXML
    private Label t4CurrentDate;

    @FXML
    private Label t5CurrentDate;

    @FXML
    private Label t6CurrentDate;

    @FXML
    private Label t7CurrentDate;

    @FXML
    private Label cnCurrentDate;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField tfBan;

    @FXML
    private TextField tfMaLichDat;

    @FXML
    private TextField tfCCCD;

    @FXML
    private TextField tfTenKhachHang;

    @FXML
    private TextField tfThoiGianNhanBan;

    @FXML
    private TextField tfTienCoc;

    @FXML
    private TextField tfTrangThai;

    @FXML
    private Button btnHuyLich;

    @FXML
    private Button btnNhanBan;

    private LocalDate currentDate;

    private LichDatService lichDatService;

    private BanService banService;

    private HoaDonService hoaDonService;

    private LichDat selectedLichDat;

    private static XemLichDatController instance;

    private static Parent root;

    public static XemLichDatController getInstance() throws IOException {
        if(instance == null)
            instance = loadLich();
        return instance;
    }

    private static XemLichDatController loadLich() throws IOException {
        FXMLLoader loader = new FXMLLoader(ChonBanController.class.getResource("/org/login/quanlydatban/views/TrangXemLichDat.fxml"));
        root = loader.load();
        return loader.getController();
    }

    public Parent getRoot() {
        return root;
    }

    public void assignDateToColumn(LocalDate date) {
        switch (date.getDayOfWeek()) {
            case MONDAY -> t2CurrentDate.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            case TUESDAY -> t3CurrentDate.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            case WEDNESDAY -> t4CurrentDate.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            case THURSDAY -> t5CurrentDate.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            case FRIDAY -> t6CurrentDate.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            case SATURDAY -> t7CurrentDate.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            default -> cnCurrentDate.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
    }

    public void refreshCurrentWeek() {
        assignDateToColumn(currentDate);

        for(int i = currentDate.getDayOfWeek().getValue() - 1; i > 0; i--) {
            assignDateToColumn(currentDate.minusDays(i));
        }

        for(int i = currentDate.getDayOfWeek().getValue() + 1; i < 8; i++) {
            assignDateToColumn(currentDate.plusDays(i - currentDate.getDayOfWeek().getValue()));
        }
    }

    public void refreshBox() {
        boxSangT2.getChildren().clear();
        boxSangT3.getChildren().clear();
        boxSangT4.getChildren().clear();
        boxSangT5.getChildren().clear();
        boxSangT6.getChildren().clear();
        boxSangT7.getChildren().clear();
        boxSangCN.getChildren().clear();

        boxChieuT2.getChildren().clear();
        boxChieuT3.getChildren().clear();
        boxChieuT4.getChildren().clear();
        boxChieuT5.getChildren().clear();
        boxChieuT6.getChildren().clear();
        boxChieuT7.getChildren().clear();
        boxChieuCN.getChildren().clear();

        boxToiT2.getChildren().clear();
        boxToiT3.getChildren().clear();
        boxToiT4.getChildren().clear();
        boxToiT5.getChildren().clear();
        boxToiT6.getChildren().clear();
        boxToiT7.getChildren().clear();
        boxToiCN.getChildren().clear();
    }

    public void refreshTextFields() {
        tfMaLichDat.clear();
        tfThoiGianNhanBan.clear();
        tfBan.clear();
        tfTrangThai.clear();
        tfCCCD.clear();
        tfTenKhachHang.clear();
        tfTienCoc.clear();

        btnNhanBan.setDisable(true);
        btnHuyLich.setDisable(true);

        selectedLichDat = null;
    }

    public LocalDate getFirstDayOfWeek() {
        return currentDate.minusDays(currentDate.getDayOfWeek().getValue() - 1);
    }

    public LocalDate getLastDayOfWeek() {
        return getFirstDayOfWeek().plusDays(6);
    }

    public LichDat getSelectedLichDat() {
        return selectedLichDat;
    }

    public void setSelectedLichDat(LichDat selectedLichDat) {
        this.selectedLichDat = selectedLichDat;
        tfMaLichDat.setText(selectedLichDat.getMaLichDat());
        tfThoiGianNhanBan.setText(selectedLichDat.getThoiGianNhanBan().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")));
        tfBan.setText(selectedLichDat.getHoaDon().getBan().getMaBan());
        tfTrangThai.setText(selectedLichDat.getHoaDon().getTrangThaiHoaDon().toString());
        tfCCCD.setText(selectedLichDat.getKhachHang().getCccd());
        tfTenKhachHang.setText(selectedLichDat.getKhachHang().getTenKhachHang());
        tfTienCoc.setText(NumberFormatter.formatPrice(String.valueOf((int) selectedLichDat.getTienCoc())));


        if(selectedLichDat.getHoaDon().getTrangThaiHoaDon() != TrangThaiHoaDon.DA_DAT){
            btnNhanBan.setDisable(true);
            btnHuyLich.setDisable(true);
        }
        else {
            btnNhanBan.setDisable(false);
            btnHuyLich.setDisable(false);
        }
    }

    public void loadLichDatFromCurrentWeek(TrangThaiHoaDon trangThaiHoaDon) throws IOException {
        refreshBox();
        List<LichDat> list = lichDatService.getDSLichDatFrom(getFirstDayOfWeek(), getLastDayOfWeek(), trangThaiHoaDon);
        for(LichDat i : list) {
            LocalDateTime time = i.getThoiGianNhanBan();

            FXMLLoader loader = new FXMLLoader(ChonBanController.class.getResource("/org/login/quanlydatban/uicomponents/CardLich_TrangXemLich.fxml"));
            AnchorPane pane = loader.load();

            CardLichXemLichController controller = loader.getController();
            controller.setLichDat(i);

            switch (time.getDayOfWeek()) {
                case MONDAY:
                    if (time.toLocalTime().isAfter(LocalTime.of(6, 0)) && (time.toLocalTime().isBefore(LocalTime.of(12, 1))))
                        boxSangT2.getChildren().add(pane);
                    else if (time.toLocalTime().isAfter(LocalTime.of(12, 0)) && time.toLocalTime().isBefore(LocalTime.of(18, 1)))
                        boxChieuT2.getChildren().add(pane);
                    else
                        boxToiT2.getChildren().add(pane);
                    break;
                case TUESDAY:
                    if (time.toLocalTime().isAfter(LocalTime.of(6, 0)) && time.toLocalTime().isBefore(LocalTime.of(12, 1)))
                        boxSangT3.getChildren().add(pane);
                    else if (time.toLocalTime().isAfter(LocalTime.of(12, 0)) && time.toLocalTime().isBefore(LocalTime.of(18, 1)))
                        boxChieuT3.getChildren().add(pane);
                    else
                        boxToiT3.getChildren().add(pane);
                    break;
                case WEDNESDAY:
                    if (time.toLocalTime().isAfter(LocalTime.of(6, 0)) && time.toLocalTime().isBefore(LocalTime.of(12, 1)))
                        boxSangT4.getChildren().add(pane);
                    else if (time.toLocalTime().isAfter(LocalTime.of(12, 0)) && time.toLocalTime().isBefore(LocalTime.of(18, 1)))
                        boxChieuT4.getChildren().add(pane);
                    else
                        boxToiT4.getChildren().add(pane);
                    break;
                case THURSDAY:
                    if (time.toLocalTime().isAfter(LocalTime.of(6, 0)) && time.toLocalTime().isBefore(LocalTime.of(12, 1)))
                        boxSangT5.getChildren().add(pane);
                    else if (time.toLocalTime().isAfter(LocalTime.of(12, 0)) && time.toLocalTime().isBefore(LocalTime.of(18, 1)))
                        boxChieuT5.getChildren().add(pane);
                    else
                        boxToiT5.getChildren().add(pane);
                    break;
                case FRIDAY:
                    if (time.toLocalTime().isAfter(LocalTime.of(6, 0)) && time.toLocalTime().isBefore(LocalTime.of(12, 1)))
                        boxSangT6.getChildren().add(pane);
                    else if (time.toLocalTime().isAfter(LocalTime.of(12, 0)) && time.toLocalTime().isBefore(LocalTime.of(18, 1)))
                        boxChieuT6.getChildren().add(pane);
                    else
                        boxToiT6.getChildren().add(pane);
                    break;
                case SATURDAY:
                    if (time.toLocalTime().isAfter(LocalTime.of(6, 0)) && time.toLocalTime().isBefore(LocalTime.of(12, 1)))
                        boxSangT7.getChildren().add(pane);
                    else if (time.toLocalTime().isAfter(LocalTime.of(12, 0)) && time.toLocalTime().isBefore(LocalTime.of(18, 1)))
                        boxChieuT7.getChildren().add(pane);
                    else
                        boxToiT7.getChildren().add(pane);
                    break;
                case SUNDAY:
                    if (time.toLocalTime().isAfter(LocalTime.of(6, 0)) && time.toLocalTime().isBefore(LocalTime.of(12, 1)))
                        boxSangCN.getChildren().add(pane);
                    else if (time.toLocalTime().isAfter(LocalTime.of(12, 0)) && time.toLocalTime().isBefore(LocalTime.of(18, 1)))
                        boxChieuCN.getChildren().add(pane);
                    else
                        boxToiCN.getChildren().add(pane);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            lichDatService = RMIServiceUtils.useLichDatService();
            banService = RMIServiceUtils.useBanService();
            hoaDonService = RMIServiceUtils.useHoaDonService();

        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            throw new RuntimeException(e);
        }

        currentDate = LocalDate.now();
        refreshCurrentWeek();

        datePicker.setValue(LocalDate.now());

        VBox.setVgrow(labelSang, Priority.ALWAYS);
        labelSang.prefHeightProperty().bind(boxSang.heightProperty());

        VBox.setVgrow(labelChieu, Priority.ALWAYS);
        labelChieu.prefHeightProperty().bind(boxChieu.heightProperty());

        VBox.setVgrow(labelToi, Priority.ALWAYS);
        labelToi.prefHeightProperty().bind(boxToi.heightProperty());

        datePicker.setConverter(new StringConverter<>() {
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

        try {
            loadLichDatFromCurrentWeek(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void dateBack(ActionEvent event) throws IOException {
        currentDate = currentDate.minusDays(7);
        refreshCurrentWeek();
        loadLichDatFromCurrentWeek(null);
    }

    @FXML
    void dateForward(ActionEvent event) throws IOException {
        currentDate = currentDate.plusDays(7);
        refreshCurrentWeek();
        loadLichDatFromCurrentWeek(null);
    }

    @FXML
    void dateChange(ActionEvent event) throws IOException {
        currentDate = datePicker.getValue();
        if(currentDate == null){
            currentDate = LocalDate.now();
            datePicker.setValue(LocalDate.now());
        }
        refreshCurrentWeek();
        loadLichDatFromCurrentWeek(null);
    }


    @FXML
    void huyLich(ActionEvent event) {
        try {
            if (selectedLichDat != null) {
                if (Notification.xacNhan("Xác nhận huỷ lịch này?, không thể hoàn tác")) {
                    HoaDon hoaDon = selectedLichDat.getHoaDon();
                    hoaDon.setTrangThaiHoaDon(TrangThaiHoaDon.DA_HUY);
                    hoaDonService.updateHoaDon(hoaDon);

                    loadLichDatFromCurrentWeek(null);

                    Notification.thongBao("Đã huỷ thành công lịch đặt " + selectedLichDat.getMaLichDat(), Alert.AlertType.INFORMATION);
                    refreshTextFields();
                }
            } else throw new IllegalArgumentException("Vui lòng chọn lịch để huỷ");
        }
        catch (Exception e){
            Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    @FXML
    void nhanBan(ActionEvent event) {
        try{
            if(selectedLichDat == null)
                throw new IllegalArgumentException("Vui lòng chọn lịch để tiếp tục");

            Ban b = selectedLichDat.getHoaDon().getBan();
            HoaDon hoaDon = selectedLichDat.getHoaDon();

            if(selectedLichDat.getThoiGianNhanBan().isAfter(LocalDateTime.now()))
                throw new IllegalArgumentException("Chưa đến thời gian để nhận bàn");

            //check if there're any served table at the time
            for(Ban i : banService.readByStatus(TrangThaiBan.DANG_PHUC_VU)){
                if(i.getMaBan().equals(b.getMaBan()))
                    throw new IllegalArgumentException("Không thể nhận bàn, vui lòng thanh toán bàn " + i.getMaBan() + " trước khi nhận bàn");
            }

            if(Notification.xacNhan("Nhận bàn?")){
                b.setTrangThaiBan(TrangThaiBan.DANG_PHUC_VU);
                banService.updateBan(b);

                hoaDon.setTrangThaiHoaDon(TrangThaiHoaDon.CHUA_THANH_TOAN);
                hoaDonService.updateHoaDon(hoaDon);

                loadLichDatFromCurrentWeek(null);

                Notification.thongBao("Nhận bàn thành công, mã bàn: " + b.getMaBan(), Alert.AlertType.INFORMATION);
            }
        }
        catch (Exception e){
            Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    @FXML
    void resetFilter(MouseEvent event) throws IOException {
        loadLichDatFromCurrentWeek(null);
    }

    @FXML
    void loadDaDat(ActionEvent event) throws IOException {
        loadLichDatFromCurrentWeek(TrangThaiHoaDon.DA_DAT);
    }

    @FXML
    void loadDaHuy(ActionEvent event) throws IOException {
        loadLichDatFromCurrentWeek(TrangThaiHoaDon.DA_HUY);
    }

    @FXML
    void loadDaTT(ActionEvent event) throws IOException {
        loadLichDatFromCurrentWeek(TrangThaiHoaDon.DA_THANH_TOAN);
    }

    @FXML
    void loadChuaTT(ActionEvent event) throws IOException {
        loadLichDatFromCurrentWeek(TrangThaiHoaDon.CHUA_THANH_TOAN);
    }
}
