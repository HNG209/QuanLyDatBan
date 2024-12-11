package org.login.quanlydatban.controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.login.quanlydatban.dao.NhanVienDAO;
import org.login.quanlydatban.dao.TaiKhoanDAO;
import org.login.quanlydatban.encryptionUtils.EncryptionUtils;
import org.login.quanlydatban.entity.NhanVien;
import org.login.quanlydatban.entity.TaiKhoan;
import org.login.quanlydatban.entity.enums.ChucVu;
import org.login.quanlydatban.entity.enums.TrangThaiNhanVien;
import org.login.quanlydatban.notification.Notification;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.ResourceBundle;

public class TrangQuanLyNhanVienController implements Initializable {
    @FXML
    private Button btnTaiAnh1;
    @FXML
    private ImageView image1;
    @FXML
    private Button btnHuyBo; // btn huy bo
    @FXML
    private Button btnLuu; // btn luu
    @FXML
    private TextField diaChi1; // dia chi
    @FXML
    private TextField cccd;// can cuoc cong dan
    @FXML
    private TextField dienThoai;
    @FXML
    private TextField hoTen; // ho ten
    @FXML
    private TextField maNhanVien1; // ma nhan vien
    @FXML
    private ComboBox<String> gioiTinh1; // cbx gioi tinh
    @FXML
    private ComboBox<String> trangThaiLamViec; // cbx trang thai lam viec
    @FXML
    private ComboBox<String> chucVu; // cbx chuc vu
    @FXML
    private DatePicker ngaySinh;
    private NhanVienDAO nhanVienDAO;

    private String duongdananh;
    @FXML
    private Button btnthem;
    @FXML
    private TextField searchID;
    private String image11;

    // bien ten nhan vien
    private String nhanvien;
    private NhanVienDAO nvdao;
    @FXML
    private TableView<NhanVien> tableNhanVien;
    @FXML
    private Button btnxuatFile;
    private String duongdan;

    @FXML
    private TableColumn<NhanVien, String> nhanVienID; // 0 Cột ID
    @FXML
    private TableColumn<NhanVien, String> tenNhanVien; // 1 Cột Họ Tên
    @FXML
    private TableColumn<NhanVien, String> diaChi; // 7 cột địa chỉ
    @FXML
    private TableColumn<NhanVien, String> gioiTinh; // 5 Cột giới tính
    @FXML
    private TableColumn<NhanVien, String> trangThai; // 6 Cột Ngày Sinh
    @FXML
    private TableColumn<NhanVien, String> soDienThoai; // 4 Cột sdt

    private NhanVienDAO employeeList1;
    private String maNhanVien;
    private String cellValue;


    public void XuatFile(){
        btnxuatFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    // them nhan vien
    public void themNhanVien(){
        employeeList1 = new NhanVienDAO();
        List<NhanVien>  employeeList = employeeList1.getAllTaiKhoan();
        btnthem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    System.out.println("Nhan nut them");
                    // Tải giao diện từ file FXML
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/QuanLyNhanVien.fxml"));
                    Parent newWindow = loader.load();
                    TrangThemNhanVienController nv = loader.getController();
//                    nv.setTenNhanVien(getNhanvien().toString());
                    nv.SetTrangQuanLyNhanVien(TrangQuanLyNhanVienController.this);
                    // Tạo một cửa sổ mới
                    Stage stage = new Stage();
                    stage.setScene(new Scene(newWindow));
                    stage.setTitle("Quản Lý Nhân Viên");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace(); // Xử lý ngoại lệ nếu có lỗi khi tải FXML
                }
            }
        });
    }

    // xet lai du lieu cho bang nhan vien
    public void xetLaiduLieuChoBang(){
        try {
            nhanVienDAO = new NhanVienDAO();
            List<NhanVien>  listNhanVien = nhanVienDAO.getAllTaiKhoan();
            nhanVienID.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
            tenNhanVien.setCellValueFactory(new PropertyValueFactory<>("tenNhanVien"));
            diaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
            gioiTinh.setCellValueFactory(cellData -> {
                boolean isNam = cellData.getValue().isGioiTinh();
                return new SimpleStringProperty(!isNam ? "Nam" : "Nữ");
            });

            trangThai.setCellValueFactory(new PropertyValueFactory<>("trangThaiNhanVien"));
            soDienThoai.setCellValueFactory(new PropertyValueFactory<>("sdt"));

            // Chuyển danh sách nhân viên thành ObservableList và thêm vào TableView
            ObservableList<NhanVien> observableList = FXCollections.observableArrayList(listNhanVien);
            tableNhanVien.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace(); // In ra thông tin lỗi
        }
    }

    @FXML
    public  void taiAnh(){
        btnTaiAnh1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Nhấn nút tải ảnh");

                // Khởi tạo FileChooser để người dùng chọn hình ảnh
                FileChooser fileChooser = new FileChooser();
                URL resourceUrl = getClass().getResource("/org/login/quanlydatban/Image/giabao.png");
                File initialDirectory = null;
                try {
                    initialDirectory = new File(resourceUrl.toURI());
                } catch (URISyntaxException e) {
                    System.out.println("Không tìm thấy thư mục");
                }
                fileChooser.setInitialDirectory(initialDirectory);
                fileChooser.setTitle("Mở file");

                // Thiết lập bộ lọc file hình ảnh
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif");
                fileChooser.getExtensionFilters().add(extFilter);

                // Mở cửa sổ chọn file và lấy file được chọn
                File file = fileChooser.showOpenDialog(null);
                if (file != null) {
                    // Lấy tên file từ tệp được chọn
                    String fileName = file.getName(); // Ví dụ "image.jpg"

                    // Định nghĩa đường dẫn lưu file trong thư mục Image của dự án
                    File destinationDirectory = new File(initialDirectory, fileName);
                    try {
                        // Copy file vào thư mục Image trong dự án
                        Files.copy(file.toPath(), destinationDirectory.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Ảnh đã được lưu thành công.");

                        // Cập nhật đường dẫn hình ảnh vào cơ sở dữ liệu (hoặc biến)
                        duongdan = "/org/login/quanlydatban/Image/" + fileName;  // Đường dẫn tương đối
                        duongdananh = duongdan;  // Cập nhật đường dẫn ảnh

                        // Cập nhật ImageView với ảnh mới
                        Image image = new Image(destinationDirectory.toURI().toString());
                        image1.setImage(image);

                    } catch (IOException e) {
                        System.out.println("Lỗi khi lưu ảnh: " + e.getMessage());
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
    public String getNhanvien() {
        return nhanvien;
    }
    // su dung de lay ten nhan vien
    public void setNhanvien(String nhanvien) {
        this.nhanvien = nhanvien;
    }

    // xuat ra file excel
    public void xuatFileExcel(Stage primaryStage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu File Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Data");

                // Tạo tiêu đề
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Mã Nhân Viên");
                headerRow.createCell(1).setCellValue("Tên Nhân Viên");
                headerRow.createCell(2).setCellValue("Số điện thoại");
                headerRow.createCell(3).setCellValue("Địa chỉ");

                // Thêm dữ liệu từ TableView
                ObservableList<NhanVien> items = tableNhanVien.getItems();
                int rowCount = 1;
                for (NhanVien nv : items) {
                    Row row = sheet.createRow(rowCount++);
                    row.createCell(0).setCellValue(nv.getMaNhanVien());
                    row.createCell(1).setCellValue(nv.getTenNhanVien());
                    row.createCell(2).setCellValue(nv.getSdt());
                    row.createCell(3).setCellValue(nv.getDiaChi());
                }

                // Ghi ra file
                try (FileOutputStream outputStream = new FileOutputStream(file)) {
                    workbook.write(outputStream);
                }

                System.out.println("Xuất file Excel thành công tại: " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void loaddulieulenform(NhanVien nhanVien){

        image11 = nhanVien.getHinhAnh();
//        if(image11 != null || image11.isEmpty()){
//            Image image = new Image("file:" + image11);
//            // Tạo ImageView và đặt hình ảnh vào
//            System.out.println("lll"+image11);
//
//        }
        duongdananh = image11;
        Image image = new Image(getClass().getResource(image11).toString());
        image1.setImage(image);
        maNhanVien1.setEditable(false);
        maNhanVien1.setText(nhanVien.getMaNhanVien());
        hoTen.setText(nhanVien.getTenNhanVien());
        diaChi1.setText(nhanVien.getDiaChi());
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
            gioiTinh1.setValue("NAM");
        }else{
            gioiTinh1.setValue("NỮ");
        }
        //Trang thai nhan vien
        if(nhanVien.getTrangThaiNhanVien().equals(TrangThaiNhanVien.DANG_LAM)){
            trangThaiLamViec.setValue("ĐANG LÀM");

        }else if(nhanVien.getTrangThaiNhanVien().equals(TrangThaiNhanVien.NGHI_PHEP)){
            trangThaiLamViec.setValue("NGHỈ PHÉP");
        }else
            trangThaiLamViec.setValue("NGHI_VIEC");

    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Kết Quả");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void chinhSuaNhanVien(String getMaNhanVien){
        nvdao = new NhanVienDAO();
        NhanVien nv1 = nvdao.getNhanVien(getMaNhanVien);
        Boolean gt = gioiTinh1.getValue().equals("NAM") ? false : true;
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

       // NhanVien nv = new NhanVien(getMaNhanVien,hoTen.getText().toString(),dienThoai.getText().toString(),cccd.getText().toString(),diaChi1.getText().toString(),gt,ngaySinh.getValue(),duongdananh,tt,cv);
        NhanVien nv = new NhanVien();
        //nv.setMaNhanVien(maNhanVientt);
        nv.setTenNhanVien(hoTen.getText());
        nv.setGioiTinh(gt);
        nv.setCccd(cccd.getText());
        nv.setSdt(dienThoai.getText());
        nv.setChucVuNhanVien(cv);
        nv.setTrangThaiNhanVien(tt);
        nv.setDiaChi(diaChi1.getText());
        nv.setHinhAnh(duongdananh);
        nv.setNgaySinh(ngaySinh.getValue());
        NhanVienDAO nvd = new NhanVienDAO();
        nvd.updateNhanVien(nv1.getMaNhanVien().toString(),nv);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nhanVienDAO = new NhanVienDAO();
        List<NhanVien>  listNhanVien = nhanVienDAO.getAllTaiKhoan();
        tableNhanVien.setPrefHeight(60);
        // Thiết lập các cột cho TableView
        try {
            nhanVienID.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
            tenNhanVien.setCellValueFactory(new PropertyValueFactory<>("tenNhanVien"));
            diaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
            gioiTinh.setCellValueFactory(cellData -> {
                boolean isNam = cellData.getValue().isGioiTinh();
                return new SimpleStringProperty(!isNam ? "Nam" : "Nữ");
            });

            trangThai.setCellValueFactory(new PropertyValueFactory<>("trangThaiNhanVien"));
            soDienThoai.setCellValueFactory(new PropertyValueFactory<>("sdt"));

            // Chuyển danh sách nhân viên thành ObservableList và thêm vào TableView
            ObservableList<NhanVien> observableList = FXCollections.observableArrayList(listNhanVien);
            tableNhanVien.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace(); // In ra thông tin lỗi
        }
        FilteredList<NhanVien> filteredList = new FilteredList<>(FXCollections.observableArrayList(listNhanVien), b -> true);


        btnHuyBo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                maNhanVien1.setEditable(false);
                maNhanVien1.setText("");
                hoTen.setText("");
                diaChi1.setText("");
                cccd.setText("");
                dienThoai.setText("");
                ngaySinh.setValue(null);
                trangThaiLamViec.setValue(null);
                gioiTinh1.setValue(null);
                chucVu.setValue(null);
                image1.setImage(null);
            }
        });
        btnLuu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                NhanVienDAO nvd = new NhanVienDAO();
                List<NhanVien> nvs = nvd.getAllTaiKhoan();

                long countNhanViensdt = nvs.stream()
                        .filter(x -> x.getSdt().equals(dienThoai.getText()))
                        .count();

                long countNhanViencccd = nvs.stream()
                        .filter(x -> x.getSdt().equals(dienThoai.getText()))
                        .count();

                if(countNhanViencccd >= 2){
                    showWarn("Căn cước công dân này đã được sử dụng, vui lòng sử dụng số căn cước công dân khác");
                    return;
                }
                if(countNhanViensdt >=2){
                    showWarn("Số điện thoại này đã được sử dụng, vui lòng sử dụng số điện thoại khác");
                    return;
                }
                if (gioiTinh1.getValue() == null) {
                    showWarn("Phải chọn giới tính của nhân viên.");
                    return;
                }
                if (chucVu.getValue() == null) {
                    showWarn("Phải chọn chức vụ của nhân viên.");
                    return;
                }
                ChucVu cv = null;
                if (chucVu.getValue().equals("Nhân viên")) {
                    cv = ChucVu.NHAN_VIEN;
                } else if (chucVu.getValue().equals("Quản Lý")) {
                    cv = ChucVu.QUAN_LY;
                }
                Boolean gt = gioiTinh1.getValue().equals("NAM") ? false : true;
                NhanVien nv = new NhanVien();
                try {
                    nv.setGioiTinh(gt);
                    nv.setTenNhanVien(hoTen.getText());
                    nv.setDiaChi(diaChi1.getText());
                    nv.setCccd(cccd.getText());
                    nv.setSdt(dienThoai.getText());
                    nv.setChucVuNhanVien(cv);
                    nv.setTrangThaiNhanVien(TrangThaiNhanVien.DANG_LAM);
                    nv.setHinhAnh(duongdananh);
                    nv.setNgaySinh(ngaySinh.getValue());
                    nvd.updateNhanVien(cellValue,nv);
                    showAlert("Cập nhật nhân viên thành công");
                    xetLaiduLieuChoBang();
                } catch (Exception e) {
                    Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
                }
            }
        });
        searchID.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(employee -> {
                // Nếu không có từ khóa tìm kiếm, hiển thị tất cả
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                // Chuyển đổi từ khóa tìm kiếm thành chữ thường
                String lowerCaseFilter = newValue.toString();

                // Duyệt CCCD và SDT
                if (lowerCaseFilter.matches("^\\d+$")) {
                    if (lowerCaseFilter.length() == 10) {
                        return employee.getSdt() != null && employee.getSdt().contains(lowerCaseFilter);
                    } else {
                        return employee.getCccd() != null && employee.getCccd().contains(lowerCaseFilter);
                    }
                    // Duyệt theo mã nhân viên
                } else
                    return employee.getTenNhanVien() != null && employee.getTenNhanVien().contains(lowerCaseFilter) ||
                            employee.getDiaChi() != null && employee.getDiaChi().contains(lowerCaseFilter)||
                            employee.getTrangThaiNhanVien().toString().contains(lowerCaseFilter)||employee.getMaNhanVien().contains(lowerCaseFilter);
                    // Duyệt theo địa chỉ

            });
        });
        // Cập nhật TableView với danh sách đã lọc
        tableNhanVien.setItems(filteredList);
        btnxuatFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage currentStage = (Stage) btnxuatFile.getScene().getWindow();
                xuatFileExcel(currentStage);
            }
        });

        // khi click mo dong tren table, thi hien len thong tin day du

        tableNhanVien.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 1) { // Kiểm tra nhấp chuột đơn,nv.getNgaySinh()
                        int rowIndex = tableNhanVien.getSelectionModel().getSelectedIndex();
                        cellValue = tableNhanVien.getItems().get(rowIndex).getMaNhanVien();
                        NhanVienDAO nvdao = new  NhanVienDAO();
                        NhanVien nvtim = nvdao.getNhanVien(cellValue);
                        loaddulieulenform(nvtim);
                }
           }
        });
    }
}
