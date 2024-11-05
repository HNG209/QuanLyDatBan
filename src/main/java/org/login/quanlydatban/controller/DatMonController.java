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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
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

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
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
    private TableView<Object[]> orderTable;

    @FXML
    private TableColumn<Object[], Double> donGia;

    @FXML
    private TableColumn<Object[], String> dvt;

    @FXML
    private TableColumn<Object[], String> ghiChu;

    @FXML
    private TableColumn<Object[], String> maBan;

    @FXML
    private TableColumn<Object[], Integer> soLuong;

    @FXML
    private TableColumn<Object[], String> tenMon;

    private ObservableList<Object[]> objectsObservableList;

    private Ban ban;

    private NhanVien nhanVien;

    private HoaDonDAO hoaDonDAO;

    private ChiTietHoaDonDAO chiTietHoaDonDAO;
    private HoaDon hoaDon;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        monAnDAO = new MonAnDAO();
        banDAO = new BanDAO();
        hoaDonDAO = new HoaDonDAO();
        chiTietHoaDonDAO = new ChiTietHoaDonDAO();

        objectsObservableList = FXCollections.observableArrayList();

        donGia.setCellValueFactory(data -> new SimpleDoubleProperty((Double)data.getValue()[2]).asObject());
        soLuong.setCellValueFactory(data -> new SimpleIntegerProperty((Integer) data.getValue()[3]).asObject());
        maBan.setCellValueFactory(data -> new SimpleStringProperty((String)data.getValue()[0]));
        tenMon.setCellValueFactory(data -> new SimpleStringProperty((String)data.getValue()[1]));
        dvt.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue()[4]));
        ghiChu.setCellValueFactory( data -> new SimpleStringProperty((String) data.getValue()[5]));
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
        if(xacNhan("Xác nhận giữ bàn, bạn có thể huỷ ngay nếu muốn")){
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

            thongBao("Giữ bàn thành công", Alert.AlertType.INFORMATION);
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
        }
        else thongBao("Không thể huỷ, hãy tiến hành thanh toán", Alert.AlertType.INFORMATION);
    }

    @FXML
    void thanhToan(ActionEvent event) {
        if(xacNhan("Xác nhận thanh toán?")){
            orderTable.getItems().clear();
            this.hoaDon.setTrangThaiHoaDon(TrangThaiHoaDon.DA_THANH_TOAN);

            ban.setTrangThaiBan(TrangThaiBan.BAN_TRONG);
            trangThaiBanText.setText(TrangThaiBan.BAN_TRONG.toString());

            hoaDonDAO.updateHoaDon(hoaDon);
            banDAO.updateBan(ban);

            this.btnHuy.setVisible(false);
            this.btnGiuBan.setVisible(true);
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

    public boolean xacNhan(String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText(text);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK)
            return true;
        return false;
    }

    public void thongBao(String text, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Thông báo");
        alert.setHeaderText(text);

        alert.showAndWait();
    }

    public HoaDon getHoaDon() {
        return this.hoaDon;
    }
}
