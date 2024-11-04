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
import org.login.quanlydatban.entity.NhanVien;
import org.login.quanlydatban.entity.enums.ChucVu;
import org.login.quanlydatban.entity.enums.TrangThaiNhanVien;
import org.login.quanlydatban.hibernate.HibernateUtils;
import java.io.File;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.ResourceBundle;

public class TrangHienNhanVien implements Initializable {
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
    private URL resourceUrl = getClass().getResource("/org/login/quanlydatban/Image/");
    private String duongdan;

    private TrangQuanLyNhanVien trangQuanLyNhanVien;

    private String getMaNhanVien;
    private  NhanVien nv;
    private   NhanVienDAO nvdao;

    public void SetTrangQuanLyNhanVien(TrangQuanLyNhanVien trangQuanLyNhanVien) {
        this.trangQuanLyNhanVien = trangQuanLyNhanVien;
    }

    public String getGetMaNhanVien() {
        return getMaNhanVien;
    }

    public void setGetMaNhanVien(String getMaNhanVien) {
        this.getMaNhanVien = getMaNhanVien;
    }

    @FXML
    public  void taiAnh(){
        btnTaiAnh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Nhan nut tai anh");
                FileChooser fileChooser = new FileChooser();


                File initialDirectory = null;
                try {
                    initialDirectory = new File(resourceUrl.toURI());
                } catch (URISyntaxException e) {
                    System.out.println("Thư mục 'Image' không tìm thấy.");
                }
                fileChooser.setInitialDirectory(initialDirectory);




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


    // bat regex cho ten
    public boolean tencheck(TextField hoTen){
        if(!hoTen.getText().matches("^([A-Z][a-zà-ÿ]*)( [A-Z][a-zà-ÿ]*)*$")){
            showWarn("Ten khong hop le");
        }
        return true;
    }

    // cccd, 11 so
    public boolean cancuoccongdancheck(TextField cccd){
        if(!cccd.getText().matches("^[0-9]{11}$")){
            showWarn("Can cuoc cong dan khong hop le");
            return false;
        }
        return true;
    }

    // so dien thoai
    public boolean sdtcheck(TextField dienThoai){
        if(!dienThoai.getText().matches("^0[0-9]{9}$")){
            showWarn("So dien thoai khong hop le");
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
    // Load du lieu len form
    public void loaddulieulenform(NhanVien nhanVien){
        String imageUrl = nhanVien.getHinhAnh();
        // Tạo đối tượng Image từ URL
        Image image = new Image(resourceUrl + imageUrl);
        // Tạo ImageView và đặt hình ảnh vào
        image1.setImage(image);
        maNhanVien.setText(nhanVien.getMaNhanVien());
        hoTen.setText(nhanVien.getTenNhanVien());
        diaChi.setText(nhanVien.getDiaChi());
        cccd.setText(nhanVien.getCccd());
        dienThoai.setText(nhanVien.getSdt());
        ngaySinh.setValue(nhanVien.getNgaySinh());
        if(nhanVien.getChucVuNhanVien().equals(ChucVu.NHAN_VIEN)){
             chucVu.setValue("Nhân viên");
        }else{
            chucVu.setValue("Quản Lý");
        }

        // gioi tinh
        if(nhanVien.isGioiTinh() == false){
            gioiTinh.setValue("NAM");
        }else{
            gioiTinh.setValue("NỮ");
        }
        //Trang thai nhan vien
        if(nhanVien.getTrangThaiNhanVien().equals(TrangThaiNhanVien.DANG_LAM)){
            trangThaiLamViec.setValue("DANG_LAM");

        }else if(nhanVien.getTrangThaiNhanVien().equals(TrangThaiNhanVien.NGHI_PHEP)){
            trangThaiLamViec.setValue("NGHI_PHEP");
        }else
            trangThaiLamViec.setValue("NGHI_VIEC");


    }


    public void suaNhanVien(){

        Boolean gt = gioiTinh.getValue().equals("NAM") ? false : true;
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

        NhanVien nv = new NhanVien(maNhanVien.getText().toString(),hoTen.getText().toString(),dienThoai.getText().toString(),cccd.getText().toString(),diaChi.getText().toString(),gt,ngaySinh.getValue(),duongdan,tt,cv);
        NhanVienDAO nvd = new NhanVienDAO();
        nvd.updateNhanVien(nv);

        System.out.println(maNhanVien.getText().toString()+ hoTen.getText().toString()+dienThoai.getText().toString()+ cccd.getText().toString()+ diaChi.getText().toString()+ ngaySinh.getValue()+ duongdan + tt + cv);
        System.out.println(gt+"");

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        System.out.println(getGetMaNhanVien() +" Ban trang sau khi thuc hien");

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
        diaChi.focusedProperty().addListener((obs,oldVal,newVal)->{
            if(!newVal){
                diaChicheck(diaChi);
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

        btnLuu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(tencheck(hoTen) && cancuoccongdancheck(cccd) && sdtcheck(dienThoai) && diaChicheck(diaChi)){
                    if(chucvuCheck(chucVu) && trangThaiCheck(trangThaiLamViec) && gioiTinhCheck(gioiTinh)){
                        chinhSuaNhanVien();
                       // nvdao.addNhanVien(nv);
                        trangQuanLyNhanVien.xetLaiduLieuChoBang();
                    }
                }else{
                    showWarn("Ban can nhap day du thong tin");
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

    public void chinhSuaNhanVien(){
        nvdao = new NhanVienDAO();
        NhanVien nv1 = nvdao.getNhanVien(getMaNhanVien);
        duongdan = nv1.getHinhAnh();
        Boolean gt = gioiTinh.getValue().equals("NAM") ? false : true;
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

        nv = new NhanVien(maNhanVien.getText().toString(),hoTen.getText().toString(),dienThoai.getText().toString(),cccd.getText().toString(),diaChi.getText().toString(),gt,ngaySinh.getValue(),duongdan.toString(),tt,cv);
        System.out.println(nv.toString());
        NhanVienDAO nvd = new NhanVienDAO();
        nvd.updateNhanVien(maNhanVien.getText(),nv);

        System.out.println(maNhanVien.getText().toString()+ hoTen.getText().toString()+dienThoai.getText().toString()+ cccd.getText().toString()+ diaChi.getText().toString()+ ngaySinh.getValue()+ duongdan + tt + cv);
        System.out.println(gt+"");
    }

}
