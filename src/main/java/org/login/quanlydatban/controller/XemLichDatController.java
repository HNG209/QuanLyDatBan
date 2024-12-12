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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import net.bytebuddy.asm.Advice;
import org.login.quanlydatban.dao.BanDAO;
import org.login.quanlydatban.dao.HoaDonDAO;
import org.login.quanlydatban.dao.LichDatDAO;
import org.login.quanlydatban.entity.Ban;
import org.login.quanlydatban.entity.HoaDon;
import org.login.quanlydatban.entity.LichDat;
import org.login.quanlydatban.entity.enums.TrangThaiBan;
import org.login.quanlydatban.entity.enums.TrangThaiHoaDon;
import org.login.quanlydatban.notification.Notification;
import org.login.quanlydatban.utilities.NumberFormatter;

import java.io.IOException;
import java.net.URL;
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

    private LichDatDAO lichDatDAO;

    private BanDAO banDAO;

    private HoaDonDAO hoaDonDAO;

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
        boxSangT2.setMinWidth(Region.USE_COMPUTED_SIZE);
        boxSangT2.setMaxWidth(Region.USE_COMPUTED_SIZE);
        boxSangT2.setPrefHeight(Region.USE_COMPUTED_SIZE);

        boxSangT3.getChildren().clear();
        boxSangT3.setMinWidth(Region.USE_COMPUTED_SIZE);
        boxSangT3.setMaxWidth(Region.USE_COMPUTED_SIZE);
        boxSangT3.setPrefHeight(Region.USE_COMPUTED_SIZE);

        boxSangT4.getChildren().clear();
        boxSangT4.setMinWidth(Region.USE_COMPUTED_SIZE);
        boxSangT4.setMaxWidth(Region.USE_COMPUTED_SIZE);
        boxSangT4.setPrefHeight(Region.USE_COMPUTED_SIZE);

        boxSangT5.getChildren().clear();
        boxSangT5.setMinWidth(Region.USE_COMPUTED_SIZE);
        boxSangT5.setMaxWidth(Region.USE_COMPUTED_SIZE);
        boxSangT5.setPrefHeight(Region.USE_COMPUTED_SIZE);

        boxSangT6.getChildren().clear();
        boxSangT6.setMinWidth(Region.USE_COMPUTED_SIZE);
        boxSangT6.setMaxWidth(Region.USE_COMPUTED_SIZE);
        boxSangT6.setPrefHeight(Region.USE_COMPUTED_SIZE);

        boxSangT7.getChildren().clear();
        boxSangT7.setMinWidth(Region.USE_COMPUTED_SIZE);
        boxSangT7.setMaxWidth(Region.USE_COMPUTED_SIZE);
        boxSangT7.setPrefHeight(Region.USE_COMPUTED_SIZE);

        boxSangCN.getChildren().clear();
        boxSangCN.setMinWidth(Region.USE_COMPUTED_SIZE);
        boxSangCN.setMaxWidth(Region.USE_COMPUTED_SIZE);
        boxSangCN.setPrefHeight(Region.USE_COMPUTED_SIZE);

        boxChieuT2.getChildren().clear();
        boxChieuT2.setMinWidth(Region.USE_COMPUTED_SIZE);
        boxChieuT2.setMaxWidth(Region.USE_COMPUTED_SIZE);
        boxChieuT2.setPrefHeight(Region.USE_COMPUTED_SIZE);

        boxChieuT3.getChildren().clear();
        boxChieuT3.setMinWidth(Region.USE_COMPUTED_SIZE);
        boxChieuT3.setMaxWidth(Region.USE_COMPUTED_SIZE);
        boxChieuT3.setPrefHeight(Region.USE_COMPUTED_SIZE);

        boxChieuT4.getChildren().clear();
        boxChieuT4.setMinWidth(Region.USE_COMPUTED_SIZE);
        boxChieuT4.setMaxWidth(Region.USE_COMPUTED_SIZE);
        boxChieuT4.setPrefHeight(Region.USE_COMPUTED_SIZE);

        boxChieuT5.getChildren().clear();
        boxChieuT5.setMinWidth(Region.USE_COMPUTED_SIZE);
        boxChieuT5.setMaxWidth(Region.USE_COMPUTED_SIZE);
        boxChieuT5.setPrefHeight(Region.USE_COMPUTED_SIZE);

        boxChieuT6.getChildren().clear();
        boxChieuT6.setMinWidth(Region.USE_COMPUTED_SIZE);
        boxChieuT6.setMaxWidth(Region.USE_COMPUTED_SIZE);
        boxChieuT6.setPrefHeight(Region.USE_COMPUTED_SIZE);

        boxChieuT7.getChildren().clear();
        boxChieuT7.setMinWidth(Region.USE_COMPUTED_SIZE);
        boxChieuT7.setMaxWidth(Region.USE_COMPUTED_SIZE);
        boxChieuT7.setPrefHeight(Region.USE_COMPUTED_SIZE);

        boxChieuCN.getChildren().clear();
        boxChieuCN.setMinWidth(Region.USE_COMPUTED_SIZE);
        boxChieuCN.setMaxWidth(Region.USE_COMPUTED_SIZE);
        boxChieuCN.setPrefHeight(Region.USE_COMPUTED_SIZE);

        boxToiT2.getChildren().clear();
        boxToiT2.setMinWidth(Region.USE_COMPUTED_SIZE);
        boxToiT2.setMaxWidth(Region.USE_COMPUTED_SIZE);
        boxToiT2.setPrefHeight(Region.USE_COMPUTED_SIZE);

        boxToiT3.getChildren().clear();
        boxToiT3.setMinWidth(Region.USE_COMPUTED_SIZE);
        boxToiT3.setMaxWidth(Region.USE_COMPUTED_SIZE);
        boxToiT3.setPrefHeight(Region.USE_COMPUTED_SIZE);

        boxToiT4.getChildren().clear();
        boxToiT4.setMinWidth(Region.USE_COMPUTED_SIZE);
        boxToiT4.setMaxWidth(Region.USE_COMPUTED_SIZE);
        boxToiT4.setPrefHeight(Region.USE_COMPUTED_SIZE);

        boxToiT5.getChildren().clear();
        boxToiT5.setMinWidth(Region.USE_COMPUTED_SIZE);
        boxToiT5.setMaxWidth(Region.USE_COMPUTED_SIZE);
        boxToiT5.setPrefHeight(Region.USE_COMPUTED_SIZE);

        boxToiT6.getChildren().clear();
        boxToiT6.setMinWidth(Region.USE_COMPUTED_SIZE);
        boxToiT6.setMaxWidth(Region.USE_COMPUTED_SIZE);
        boxToiT6.setPrefHeight(Region.USE_COMPUTED_SIZE);

        boxToiT7.getChildren().clear();
        boxToiT7.setMinWidth(Region.USE_COMPUTED_SIZE);
        boxToiT7.setMaxWidth(Region.USE_COMPUTED_SIZE);
        boxToiT7.setPrefHeight(Region.USE_COMPUTED_SIZE);

        boxToiCN.getChildren().clear();
        boxToiCN.setMinWidth(Region.USE_COMPUTED_SIZE);
        boxToiCN.setMaxWidth(Region.USE_COMPUTED_SIZE);
        boxToiCN.setPrefHeight(Region.USE_COMPUTED_SIZE);
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
        List<LichDat> list = lichDatDAO.getDSLichDatFrom(getFirstDayOfWeek(), getLastDayOfWeek(), trangThaiHoaDon);
        for(LichDat i : list) {
            LocalDateTime time = i.getThoiGianNhanBan();

            FXMLLoader loader = new FXMLLoader(ChonBanController.class.getResource("/org/login/quanlydatban/uicomponents/CardLich_TrangXemLich.fxml"));
            AnchorPane pane = loader.load();

            CardLichXemLichController controller = loader.getController();
            controller.setLichDat(i);

            switch (time.getDayOfWeek()) {
                case MONDAY:
                    if (time.toLocalTime().isAfter(LocalTime.of(6, 0)) && time.toLocalTime().isBefore(LocalTime.of(12, 0)))
                        boxSangT2.getChildren().add(pane);
                    else if (time.toLocalTime().isAfter(LocalTime.of(12, 0)) && time.toLocalTime().isBefore(LocalTime.of(18, 0)))
                        boxChieuT2.getChildren().add(pane);
                    else if (time.toLocalTime().isAfter(LocalTime.of(18, 0)) && time.toLocalTime().isBefore(LocalTime.of(22, 0)))
                        boxToiT2.getChildren().add(pane);
                    break;
                case TUESDAY:
                    if (time.toLocalTime().isAfter(LocalTime.of(6, 0)) && time.toLocalTime().isBefore(LocalTime.of(12, 0)))
                        boxSangT3.getChildren().add(pane);
                    else if (time.toLocalTime().isAfter(LocalTime.of(12, 0)) && time.toLocalTime().isBefore(LocalTime.of(18, 0)))
                        boxChieuT3.getChildren().add(pane);
                    else if (time.toLocalTime().isAfter(LocalTime.of(18, 0)) && time.toLocalTime().isBefore(LocalTime.of(22, 0)))
                        boxToiT3.getChildren().add(pane);
                    break;
                case WEDNESDAY:
                    if (time.toLocalTime().isAfter(LocalTime.of(6, 0)) && time.toLocalTime().isBefore(LocalTime.of(12, 0)))
                        boxSangT4.getChildren().add(pane);
                    else if (time.toLocalTime().isAfter(LocalTime.of(12, 0)) && time.toLocalTime().isBefore(LocalTime.of(18, 0)))
                        boxChieuT4.getChildren().add(pane);
                    else if (time.toLocalTime().isAfter(LocalTime.of(18, 0)) && time.toLocalTime().isBefore(LocalTime.of(22, 0)))
                        boxToiT4.getChildren().add(pane);
                    break;
                case THURSDAY:
                    if (time.toLocalTime().isAfter(LocalTime.of(6, 0)) && time.toLocalTime().isBefore(LocalTime.of(12, 0)))
                        boxSangT5.getChildren().add(pane);
                    else if (time.toLocalTime().isAfter(LocalTime.of(12, 0)) && time.toLocalTime().isBefore(LocalTime.of(18, 0)))
                        boxChieuT5.getChildren().add(pane);
                    else if (time.toLocalTime().isAfter(LocalTime.of(18, 0)) && time.toLocalTime().isBefore(LocalTime.of(22, 0)))
                        boxToiT5.getChildren().add(pane);
                    break;
                case FRIDAY:
                    if (time.toLocalTime().isAfter(LocalTime.of(6, 0)) && time.toLocalTime().isBefore(LocalTime.of(12, 0)))
                        boxSangT6.getChildren().add(pane);
                    else if (time.toLocalTime().isAfter(LocalTime.of(12, 0)) && time.toLocalTime().isBefore(LocalTime.of(18, 0)))
                        boxChieuT6.getChildren().add(pane);
                    else if (time.toLocalTime().isAfter(LocalTime.of(18, 0)) && time.toLocalTime().isBefore(LocalTime.of(22, 0)))
                        boxToiT6.getChildren().add(pane);
                    break;
                case SATURDAY:
                    if (time.toLocalTime().isAfter(LocalTime.of(6, 0)) && time.toLocalTime().isBefore(LocalTime.of(12, 0)))
                        boxSangT7.getChildren().add(pane);
                    else if (time.toLocalTime().isAfter(LocalTime.of(12, 0)) && time.toLocalTime().isBefore(LocalTime.of(18, 0)))
                        boxChieuT7.getChildren().add(pane);
                    else if (time.toLocalTime().isAfter(LocalTime.of(18, 0)) && time.toLocalTime().isBefore(LocalTime.of(22, 0)))
                        boxToiT7.getChildren().add(pane);
                    break;
                case SUNDAY:
                    if (time.toLocalTime().isAfter(LocalTime.of(6, 0)) && time.toLocalTime().isBefore(LocalTime.of(12, 0)))
                        boxSangCN.getChildren().add(pane);
                    else if (time.toLocalTime().isAfter(LocalTime.of(12, 0)) && time.toLocalTime().isBefore(LocalTime.of(18, 0)))
                        boxChieuCN.getChildren().add(pane);
                    else if (time.toLocalTime().isAfter(LocalTime.of(18, 0)) && time.toLocalTime().isBefore(LocalTime.of(22, 0)))
                        boxToiCN.getChildren().add(pane);
                    break;
                default:
                    break;
            }
        }
        boxSangT2.requestLayout();
        boxSangT3.requestLayout();
        boxSangT4.requestLayout();
        boxSangT5.requestLayout();
        boxSangT6.requestLayout();
        boxSangT7.requestLayout();
        boxSangCN.requestLayout();

        boxChieuT2.requestLayout();
        boxChieuT3.requestLayout();
        boxChieuT4.requestLayout();
        boxChieuT5.requestLayout();
        boxChieuT6.requestLayout();
        boxChieuT7.requestLayout();
        boxChieuCN.requestLayout();

        boxToiT2.requestLayout();
        boxToiT3.requestLayout();
        boxToiT4.requestLayout();
        boxToiT5.requestLayout();
        boxToiT6.requestLayout();
        boxToiT7.requestLayout();
        boxToiCN.requestLayout();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lichDatDAO = new LichDatDAO();
        banDAO = new BanDAO();
        hoaDonDAO = new HoaDonDAO();

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
                    hoaDonDAO.updateHoaDon(hoaDon);

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
            for(Ban i : banDAO.readByStatus(TrangThaiBan.DANG_PHUC_VU)){
                if(i.getMaBan().equals(b.getMaBan()))
                    throw new IllegalArgumentException("Không thể nhận bàn, vui lòng thanh toán bàn " + i.getMaBan() + " trước khi nhận bàn");
            }

            if(Notification.xacNhan("Nhận bàn?")){
                b.setTrangThaiBan(TrangThaiBan.DANG_PHUC_VU);
                banDAO.updateBan(b);

                hoaDon.setTrangThaiHoaDon(TrangThaiHoaDon.CHUA_THANH_TOAN);
                hoaDonDAO.updateHoaDon(hoaDon);

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
