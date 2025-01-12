package org.login.quanlydatban.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.login.quanlydatban.dao.NhanVienDAO;
import org.login.quanlydatban.dao.TaiKhoanDAO;
import org.login.quanlydatban.encryptionUtils.EncryptionUtils;
import org.login.quanlydatban.entity.NhanVien;
import org.login.quanlydatban.entity.TaiKhoan;
import org.login.quanlydatban.entity.enums.ChucVu;
import org.login.quanlydatban.entity.enums.TrangThaiNhanVien;
import org.login.quanlydatban.hibernate.HibernateUtils;
import org.login.quanlydatban.notification.Notification;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;

public class TrangThemNhanVienController implements Initializable {
    @FXML
    private Button btnTaiAnh;
    @FXML
    private ImageView image1;
    private String duongdananh;
    @FXML
    private Button btnHuyBo; // btn huy bo
    @FXML
    private Button btnLuu; // btn luu
    @FXML
    private TextField diaChi; // dia chi
    @FXML
    private TextField cccd;// can cuoc cong dan
    @FXML
    private TextField dienThoai;
    @FXML
    private TextField hoTen; // ho ten
    @FXML
    private TextField maNhanVien; // ma nhan vien
    @FXML
    private ComboBox<String> gioiTinh; // cbx gioi tinh
    @FXML
    private ComboBox<String> trangThaiLamViec; // cbx trang thai lam viec
    @FXML
    private ComboBox<String> chucVu; // cbx chuc vu
    @FXML
    private DatePicker ngaySinh;
    private TaiKhoanDAO taiKhoanDAO;
    private TrangQuanLyNhanVienController trangQuanLyNhanVien;
    public void SetTrangQuanLyNhanVien(TrangQuanLyNhanVienController trangQuanLyNhanVien) {
        this.trangQuanLyNhanVien = trangQuanLyNhanVien;
    }
    @FXML
    public void taiAnh() {
        btnTaiAnh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Nhấn nút tải ảnh");

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Mở file");

                // Thiết lập bộ lọc file hình ảnh
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif");
                fileChooser.getExtensionFilters().add(extFilter);

                // Mở cửa sổ chọn file và lấy file được chọn
                File file = fileChooser.showOpenDialog(null);
                if (file != null) {
                    try {
                        // Đường dẫn tuyệt đối của file cha
                        File destinationDirectory = new File("src/main/resources/org/login/quanlydatban/Image/");
                        if (!destinationDirectory.exists()) {
                            destinationDirectory.mkdirs(); // Tạo thư mục nếu chưa tồn tại
                        }

                        // Copy file vào thư mục Image
                        File destinationFile = new File(destinationDirectory, file.getName());
                        Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                        // Cập nhật ImageView
                        Image image = new Image(destinationFile.toURI().toString());
                        image1.setImage(image);

                        // Gán đường dẫn ảnh vào biến
                        duongdananh = destinationFile.getAbsolutePath();

                    } catch (IOException e) {

                        showWarn("Lỗi khi lưu ảnh: " + e.getMessage());
                    }
                }
            }
        });
    }

    private void showWarn(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Kết Quả");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Kết Quả");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
           maNhanVien.setEditable(false);
           btnLuu.setOnAction(new EventHandler<ActionEvent>() {
               @Override
               public void handle(ActionEvent event) {
                       if (gioiTinh.getValue() == null) {
                           showWarn("Phải chọn giới tính của nhân viên.");
                           return;
                       }
                       if (chucVu.getValue() == null) {
                           showWarn("Phải chọn chức vụ của nhân viên.");
                           return;
                       }
                       NhanVienDAO nvd = new NhanVienDAO();
                       List<NhanVien> nvs = nvd.getAllTaiKhoan();
                       NhanVien nvsdt = nvs.stream().filter(x -> x.getSdt().equals(dienThoai.getText())).findFirst().orElse(null);
                       NhanVien nvscccd = nvs.stream().filter(x -> x.getCccd().equals(cccd.getText())).findFirst().orElse(null);
                       if (nvscccd != null) {
                           showWarn("Căn cước công dân này đã được sử dụng, vui lòng sử dụng số căn cước công dân khác");
                           return;
                       }
                       if (nvsdt != null) {
                           showWarn("Số điện thoại này đã được sử dụng, vui lòng sử dụng số điện thoại khác");
                           return;
                       }
                       taiKhoanDAO = new TaiKhoanDAO();
                       ChucVu cv = null;
                       if (chucVu.getValue().equals("Nhân viên")) {
                           cv = ChucVu.NHAN_VIEN;
                       } else if (chucVu.getValue().equals("Quản Lý")) {
                           cv = ChucVu.QUAN_LY;
                       }
                       Boolean gt = gioiTinh.getValue().equals("NAM") ? false : true;
                       NhanVien nv = new NhanVien();
                       try {
                           nv.setGioiTinh(gt);
                           nv.setTenNhanVien(hoTen.getText());
                           nv.setDiaChi(diaChi.getText());
                           nv.setCccd(cccd.getText());
                           nv.setSdt(dienThoai.getText());
                           nv.setChucVuNhanVien(cv);
                           nv.setTrangThaiNhanVien(TrangThaiNhanVien.DANG_LAM);
                           nv.setHinhAnh(duongdananh);
                           nv.setNgaySinh(ngaySinh.getValue());
                           nvd.addNhanVien(nv);
                           String tenTaiKhoan = hoTen.getText().toString().replaceAll("\\s+", "");
                           TaiKhoan takKhoan = new TaiKhoan(tenTaiKhoan, "1111", nvd.getNhanVien(nv.getMaNhanVien().toString()));
                           taiKhoanDAO.addNhanVien(takKhoan);
                           Stage stage = (Stage) btnLuu.getScene().getWindow();
                           showWarn("Thêm nhân viên thành công");
                           stage.close();
                           trangQuanLyNhanVien.xetLaiduLieuChoBang();
                       } catch (Exception e) {
                           Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
                       }
               }
           });
           btnHuyBo.setOnAction(new EventHandler<ActionEvent>() {
               @Override
               public void handle(ActionEvent event) {
                   // Giả sử bạn có một biến stage đại diện cho cửa sổ hiện tại
                   Stage currentStage = (Stage) btnHuyBo.getScene().getWindow();
                   currentStage.close(); // Đóng cửa sổ
               }
           });
    }
}

