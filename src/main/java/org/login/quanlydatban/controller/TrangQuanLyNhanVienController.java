package org.login.quanlydatban.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.login.quanlydatban.dao.NhanVienDAO;
import org.login.quanlydatban.entity.NhanVien;
import org.login.quanlydatban.entity.enums.ChucVu;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.ResourceBundle;

public class TrangQuanLyNhanVienController implements Initializable {
    @FXML
    private Button btnTaiAnh;
    @FXML
    private ImageView image1;
    private String duongdananh;
    // bien de lay ten nhan vien
    private String tenNhanVien;
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
    private Session session;


    @FXML
    public  void taiAnh(){
        btnTaiAnh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Nhan nut tai anh");
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialDirectory(new File("E:\\QuanLyDatBanNhom2\\QuanLyDatBan\\src\\main\\resources\\org\\login\\quanlydatban\\Image"));
                fileChooser.setTitle("Mở file");

                // Thiết lập bộ lọc file nếu cần
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif");
                fileChooser.getExtensionFilters().add(extFilter);

                File file = fileChooser.showOpenDialog(null);
                System.out.println("Nhấn nút tải ảnh");

                if (file != null) {
                    duongdananh = file.getAbsolutePath(); // Cập nhật đường dẫn
                    // Cập nhật ImageView với ảnh mới
                    Image image = new Image(file.toURI().toString());
                    image1.setImage(image);
                }

            }
        });
    }
    // them nhan vien
    public void ThemNhanVien(String tenNhanVien){
        NhanVienDAO nvdao = new NhanVienDAO();
        NhanVien nv = nvdao.getNhanVien(tenNhanVien);

    }


    // bat regex cho ten
    public void tencheck(TextField hoTen){
        if(!hoTen.getText().matches("^[A-Z][a-z]*( [A-Z][a-z]*)*$")){
            showWarn("Ten khong hop le");
        }
    }

    // cccd, 11 so
    public void cancuoccongdancheck(TextField cccd){
        if(!cccd.getText().matches("^[0-9]{11}$")){
            showWarn("Can cuoc cong dan khong hop le");
        }
    }

    // so dien thoai
    public void sdtcheck(TextField dienThoai){
        if(!dienThoai.getText().matches("^0[0-9]{9}$")){
           showWarn("So dien thoai khong hop le");
        }
    }

    // chuc vu
    public void chucvuCheck(ComboBox<String> chucVu){
        if (chucVu.getValue() == null || chucVu.getValue().isEmpty()) {
            // Tạo một hộp thoại thông báo
            showWarn("Ban phai chon chuc vu");
        }
    }

    public void trangThaiCheck(ComboBox<String> trangthai){
        if (trangthai.getValue() == null || trangthai.getValue().isEmpty()) {
            // Tạo một hộp thoại thông báo
            showWarn("Phai chon trang thai");
        }
    }


   // rang buoc cho tuoi
    private int calculateAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }


    public void gioiTinhCheck(ComboBox<String> gioiTinh){
        if (gioiTinh.getValue() == null || gioiTinh.getValue().isEmpty()) {
            // Tạo một hộp thoại thông báo
            showWarn("Ban can phai chon gioi tinh");
        }
    }


    private void showWarn(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Kết Quả");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    // ma nhan vien tang dan
    private String generateMaNhanVien(String chucVu) {
        // Xác định tiền tố dựa trên chức vụ đã chọn
        String prefix = chucVu.equals("Nhân viên") ? "NV" : "QL";

        Long maxId = getMaxIdFromDatabase(prefix);
        Long newIdNumber = (maxId == null) ? 1 : maxId + 1; // Tăng mã lên 1
        return prefix + String.format("%04d", newIdNumber); // Định dạng mã
    }



    public Long getMaxIdFromDatabase(String prefix) {
        Session session = HibernateUtils.getFactory().openSession();
        Transaction transaction = null;
        Long maxId = null;

        try {
            transaction = session.beginTransaction();

            String query = "SELECT maNhanVien FROM NhanVien WHERE maNhanVien LIKE :prefix";
            List<String> maNhanViens = session.createQuery(query)
                    .setParameter("prefix", prefix + "%")
                    .getResultList();

             maxId = maNhanViens.stream()
                    .map(ma -> Long.parseLong(ma.substring(prefix.length())))
                    .max(Long::compare)
                    .orElse(0L);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            // Không cần phải đóng session ở đây, sẽ tự động quản lý
        }
        return maxId;
    }


    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public String getDuongdananh() {
        return duongdananh;
    }

    public void setDuongdananh(String duongdananh) {
        this.duongdananh = duongdananh;
    }

    public ImageView getImage1() {
        return image1;
    }

    public void setImage1(ImageView image1) {
        this.image1 = image1;
    }

    public Button getBtnTaiAnh() {
        return btnTaiAnh;
    }

    public void setBtnTaiAnh(Button btnTaiAnh) {
        this.btnTaiAnh = btnTaiAnh;
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
            hoTen.focusedProperty().addListener((obs, oldVal, newVal) ->{
                if(!newVal){
                    tencheck(hoTen);
                }
            });

            ngaySinh.focusedProperty().addListener((obs, oldVal, newVal) ->{
                if(!newVal){
                   LocalDate l = ngaySinh.getValue();
                   if(l != null){
                      int tuoi = calculateAge(l);
                      if(tuoi <= 15){
                          showAlert("Tuoi khong hop le");
                      }
                   }
                }
            });
            cccd.focusedProperty().addListener((obs, oldVal, newVal) ->{
                if(!newVal){
                    cancuoccongdancheck(cccd);
                }
            });

           dienThoai.focusedProperty().addListener((obs, oldVal, newVal) ->{
                if(!newVal){
                    sdtcheck(dienThoai);
                }
           });

           chucVu.setOnAction(new EventHandler<ActionEvent>() {
               @Override
               public void handle(ActionEvent event) {
                   String selectedChucVu = chucVu.getValue();
                   String maNhanVien1 = generateMaNhanVien(selectedChucVu);
                   maNhanVien.setText(maNhanVien1);
               }
           });


           // them nhan vien
           // xuat file






    }
}

