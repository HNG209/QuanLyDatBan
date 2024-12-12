package org.login.quanlydatban.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.login.quanlydatban.dao.NhanVienDAO;
import org.login.quanlydatban.dao.TaiKhoanDAO;
import org.login.quanlydatban.encryptionUtils.EncryptionUtils;
import org.login.quanlydatban.entity.NhanVien;
import org.login.quanlydatban.notification.Notification;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;

public class TrangQuanLyTaiKhoanController implements Initializable {
    // lay ra ten nhan vien
    private String tenNhanVien;
    @FXML
    private ImageView hinhAnh;
    @FXML
    private TableView<NhanVien> tableTaiKhoan;
    @FXML
    private TextField maNhanVien;
    private TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
    @FXML
    private TextField tenNhanVienn;
    @FXML
    private TextField tenTaiKhoan;
    @FXML
    private PasswordField password;
    @FXML
    private TextField tim;
    private String hienthi;
    @FXML
    private TableColumn<NhanVien, String> tableMaNhanVien; // 0 Cột ID
    @FXML
    private TableColumn<NhanVien, String> tableTenNhanVien; // 1 Cột Họ Tên
    @FXML
    private TableColumn<NhanVien, String> tableTenTaiKhoan; // 2
    private NhanVienDAO nhanVienDAO;
    private String matKhauHien;
    public TrangQuanLyTaiKhoanController() throws Exception {
    }


    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nhanVienDAO = new NhanVienDAO();
        try {

            List<NhanVien> listNhanVien = nhanVienDAO.getNhanVienWithTaiKhoan();
            tableMaNhanVien.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
            tableTenNhanVien.setCellValueFactory(new PropertyValueFactory<>("tenNhanVien"));
            tableTenTaiKhoan.setCellValueFactory(new PropertyValueFactory<>("tenTaiKhoan"));

            // Chuyển danh sách nhân viên thành ObservableList và thêm vào TableView
            ObservableList<NhanVien> observableList = FXCollections.observableArrayList(listNhanVien);
            tableTaiKhoan.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace(); // In ra thông tin lỗi
        }

        List<NhanVien> listNhanVien = nhanVienDAO.getNhanVienWithTaiKhoan();
        FilteredList<NhanVien> filteredList = new FilteredList<>(FXCollections.observableArrayList(listNhanVien), b -> true);

        tim.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(employee -> {
                // Nếu không có từ khóa tìm kiếm, hiển thị tất cả
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }
                return employee.getMaNhanVien().contains(newValue) || employee.getTenNhanVien().contains(newValue);
            });
        });
        tableTaiKhoan.setItems(filteredList);
        tableTaiKhoan.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 1) { // Kiểm tra nhấp chuột đơn,nv.getNgaySinh()
                    try {
                        int rowIndex = tableTaiKhoan.getSelectionModel().getSelectedIndex();
                        String cellValue = tableTaiKhoan.getItems().get(rowIndex).getMaNhanVien();
                        NhanVienDAO nvdao = new  NhanVienDAO();
                        NhanVien nvtim = nvdao.getNhanVien(cellValue);
                        matKhauHien = taiKhoanDAO.getTaiKhoanNhanVien(cellValue).getPassword().toString();
                        if(nvtim != null){
                            //String mk = themNhanVienController.decrypt(matKhauHien,themNhanVienController.generateKey());
                            tenNhanVienn.setText(nvtim.getTenNhanVien());
                            maNhanVien.setText(nvtim.getMaNhanVien());
                            tenTaiKhoan.setText(taiKhoanDAO.getTaiKhoanNhanVien(cellValue).getUserName().toString());
                            if(taiKhoanDAO.getTaiKhoanNhanVien(cellValue).getPassword().toString().equals("1111")) {
                                hienthi="1111";
                                password.setText("1111");
                            } else{
                                password.setText(EncryptionUtils.decrypt(taiKhoanDAO.getTaiKhoanNhanVien(cellValue).getPassword().toString(), System.getenv("ENCRYPTION_KEY")));
                                hienthi = EncryptionUtils.decrypt(taiKhoanDAO.getTaiKhoanNhanVien(cellValue).getPassword().toString(), System.getenv("ENCRYPTION_KEY"));
                            }

                            //hienthi = EncryptionUtils.decrypt(nvd.getTaiKhoanNhanVien(cellValue).getPassword().toString(), System.getenv("ENCRYPTION_KEY"));
                            Tooltip tooltip = new Tooltip(hienthi);
                            Tooltip.install(password, tooltip); // Cài đặt Tooltip cho PasswordField
                            Image image = new Image(getClass().getResource(nvtim.getHinhAnh()).toString());
                            hinhAnh.setImage(image);
                        }
                    } catch (Exception e) {
                        Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
                    }
                }
            }
        });
    }
}
