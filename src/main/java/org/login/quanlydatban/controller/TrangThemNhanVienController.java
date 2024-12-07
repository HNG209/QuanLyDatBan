package org.login.quanlydatban.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.login.quanlydatban.dao.NhanVienDAO;
import org.login.quanlydatban.dao.TaiKhoanDAO;
import org.login.quanlydatban.encryptionUtils.EncryptionUtils;
import org.login.quanlydatban.entity.NhanVien;
import org.login.quanlydatban.entity.TaiKhoan;
import org.login.quanlydatban.entity.enums.ChucVu;
import org.login.quanlydatban.entity.enums.TrangThaiNhanVien;
import org.login.quanlydatban.hibernate.HibernateUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.persistence.PrePersist;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLOutput;
import java.time.LocalDate;
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
    private TaiKhoanDAO taiKhoanDAO;
    private String duongdan;// duong dan cua anh
    private TrangQuanLyNhanVienController trangQuanLyNhanVien;
    private String  maNhanVientt;
    public TrangThemNhanVienController() throws Exception {

    }


    public void SetTrangQuanLyNhanVien(TrangQuanLyNhanVienController trangQuanLyNhanVien) {
        this.trangQuanLyNhanVien = trangQuanLyNhanVien;
    }

    @FXML
    public  void taiAnh(){
        btnTaiAnh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Nhan nut tai anh");
                FileChooser fileChooser = new FileChooser();
                URL resourceUrl = getClass().getResource("/org/login/quanlydatban/Image/");
                File initialDirectory = null;


                try {
                    initialDirectory = new File(resourceUrl.toURI());
                } catch (URISyntaxException e) {
                    System.out.println("Không tìm thấy thư mục");
                }


                fileChooser.setInitialDirectory(initialDirectory);

                fileChooser.setInitialDirectory(initialDirectory);
                fileChooser.setTitle("Mở file");

                // Thiết lập bộ lọc file nếu cần
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif");
                fileChooser.getExtensionFilters().add(extFilter);

                File file = fileChooser.showOpenDialog(null);
                System.out.println("Nhấn nút tải ảnh");

                if (file != null) {
                    duongdan = file.getAbsolutePath();
                    duongdananh = file.getAbsolutePath(); // Cập nhật đường dẫn
                    // Cập nhật ImageView với ảnh mới
                    Image image = new Image(file.toURI().toString());
                    image1.setImage(image);
                }

            }
        });
    }
    // bat regex cho ten
    public boolean tencheck(TextField hoTen){
        if(!hoTen.getText().matches("^([A-Z][a-z]*)( [A-Z][a-z]*)*$")){
            showWarn("Ten bạn nhập không hợp lệ, có thể do tên chưa kí tự số, kí tự đặc biệt");
            return false;
        }else if(hoTen.equals("") || hoTen == null){
            showWarn("Bạn vui lòng nhập tên của nhân viên không!!");
            return false;
        }
        return true;
    }

    // cccd, 12 so
    public boolean cancuoccongdancheck(TextField cccd){
        if(!cccd.getText().matches("^[0-9]{12}$")){
            showWarn("Can cuoc cong dan phải là kí tự số và có 12 kí tự");
            return false;
        }else if(cccd.equals("") || cccd == null){
            showWarn("Bạn vui lòng nhập căn cước công dân của nhân viên!!");
            return false;
        }
        return true;
    }

    // so dien thoai
    public boolean sdtcheck(TextField dienThoai){
        if(!dienThoai.getText().matches("^0[0-9]{9}$")){
           showWarn("So dien thoai bạn nhập không hợp lệ và phải có đủ 10 kí tự");
           return false;
        }else if(dienThoai.equals("") || dienThoai == null){
            showWarn("Bạn vui loòng nhập số điện thoại của nhân viên!!");
            return false;
        }
        return true;
    }
    public boolean diaChicheck(TextField diaChi){
        if(diaChi.getText().equals("")){
            showWarn("Ban phai nhap dia chi");
            return false;
        }
        return true;
    }

    // chuc vu
    public boolean chucvuCheck(ComboBox<String> chucVu){
        if (chucVu.getValue() == null || chucVu.getValue().isEmpty()) {
            // Tạo một hộp thoại thông báo
            showWarn("Ban phai chon chuc vu");
            return false;
        }
        return true;
    }

    public boolean trangThaiCheck(ComboBox<String> trangthai){
        if (trangthai.getValue() == null || trangthai.getValue().isEmpty()) {
            // Tạo một hộp thoại thông báo
            showWarn("Phai chon trang thai");
            return  false;
        }
        return true;
    }
   // rang buoc cho tuoi
    private int calculateAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

    public boolean gioiTinhCheck(ComboBox<String> gioiTinh){
        if (gioiTinh.getValue() == null || gioiTinh.getValue().isEmpty()) {
            // Tạo một hộp thoại thông báo
            showWarn("Ban can phai chon gioi tinh");
            return  false;
        }
        return true;
    }
    private void showWarn(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Kết Quả");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
           chucVu.setOnAction(new EventHandler<ActionEvent>() {
               @Override
               public void handle(ActionEvent event) {
                   //maNhanVientt = generateMaNhanVien(chucVu.getValue());
               }
           });
           maNhanVien.setEditable(false);
           btnLuu.setOnAction(new EventHandler<ActionEvent>() {
               @Override
               public void handle(ActionEvent event) {
                   if(gioiTinhCheck(gioiTinh)){
                       if(tencheck(hoTen)){
                           if(diaChicheck(diaChi)){
                               if(cancuoccongdancheck(cccd)){
                                   if(sdtcheck(dienThoai)){
                                       if(trangThaiCheck(trangThaiLamViec)){
                                           if(duongdan!= null){
                                               int tuoi = calculateAge(ngaySinh.getValue());
                                               if(tuoi < 15){
                                                   showWarn("Tuổi nhân viên phải lớn hơn 15");
                                               }else {
                                                   try {
                                                      ThemNhanVien();
                                                      showAlert("Thêm nhân viên thành công");
                                                      Stage stage = (Stage) btnLuu.getScene().getWindow();
                                                      stage.close();
                                                   } catch (Exception e) {
                                                      throw new RuntimeException(e);
                                                   }
                                                trangQuanLyNhanVien.xetLaiduLieuChoBang();
                                               }
                                           }else{
                                               showAlert("Bạn phải chọn ảnh của nhân viên");
                                           }
                                       }
                                   }
                               }
                           }
                       }
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

    public void ThemNhanVien() throws Exception {
         Boolean gt = gioiTinh.getValue().equals("NAM") ? false : true;
         taiKhoanDAO = new TaiKhoanDAO();
         TrangThaiNhanVien tt = null;
         if(trangThaiLamViec.getValue().equals("ĐANG LÀM")){
             tt = TrangThaiNhanVien.DANG_LAM;
         }else if(trangThaiLamViec.getValue().equals("NGHỈ PHÉP")){
             tt = TrangThaiNhanVien.NGHI_PHEP;
         }else if(trangThaiLamViec.getValue().equals("NGHỈ VIỆC")){
            tt = TrangThaiNhanVien.NGHI_VIEC;
         }

         ChucVu cv = null;
        if(chucVu.getValue().equals("Nhân viên")){
            cv = ChucVu.NHAN_VIEN;
        }else if(chucVu.getValue().equals("Quản Lý")){
            cv = ChucVu.QUAN_LY;
        }


        //NhanVien nv = new NhanVien(maNhanVien.getText().toString(),hoTen.getText().toString(),dienThoai.getText().toString(),cccd.getText().toString(),diaChi.getText().toString(),gt,ngaySinh.getValue(),duongdan,tt,cv);
        NhanVien nv = new NhanVien();
        //nv.setMaNhanVien(maNhanVientt);
        nv.setTenNhanVien(hoTen.getText());
        nv.setCccd(cccd.getText());
        nv.setSdt(dienThoai.getText());
        nv.setChucVuNhanVien(cv);
        nv.setTrangThaiNhanVien(tt);
        nv.setDiaChi(diaChi.getText());
        nv.setHinhAnh(duongdan);
        nv.setNgaySinh(ngaySinh.getValue());
        NhanVienDAO nvd = new NhanVienDAO();
        nvd.addNhanVien(nv);
        String tenTaiKhoan = hoTen.getText().toString().replaceAll("\\s+","");
        String mk= EncryptionUtils.encrypt("1111", System.getenv("ENCRYPTION_KEY"));
        TaiKhoan takKhoan = new TaiKhoan(tenTaiKhoan,mk, nvd.getNhanVien(nv.getMaNhanVien().toString()));
        taiKhoanDAO.addNhanVien(takKhoan);
    }
}

