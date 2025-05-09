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
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.login.quanlydatban.dao.NhanVienDAO;
//import org.login.quanlydatban.dao.TaiKhoanDAO;

import org.login.quanlydatban.utilities.RMIServiceUtils;
import org.login.service.NhanVienService;

import org.login.entity.NhanVien;
import org.login.entity.enums.ChucVu;
import org.login.entity.enums.TrangThaiNhanVien;
import org.login.quanlydatban.notification.Notification;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
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
    private NhanVienService nhanVienService;

    private String duongdananh;
    @FXML
    private Button btnthem;
    @FXML
    private TextField searchID;
    private String image11;

    // bien ten nhan vien
    private String nhanvien;

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

    private String maNhanVien;
    private String cellValue;

    public void XuatFile() {
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
    public void themNhanVien() throws MalformedURLException, NotBoundException, RemoteException {
        String host = System.getenv("HOST_NAME");
        nhanVienService = (NhanVienService) Naming.lookup("rmi://" + host + ":2909/nhanVienService");
        List<NhanVien> employeeList = nhanVienService.getAllTaiKhoan();
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
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace(); // Xử lý ngoại lệ nếu có lỗi khi tải FXML
                }
            }
        });
    }

    // xet lai du lieu cho bang nhan vien
    public void xetLaiduLieuChoBang() {
        try {
            List<NhanVien> listNhanVien = nhanVienService.getAllTaiKhoan();
            nhanVienID.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
            tenNhanVien.setCellValueFactory(new PropertyValueFactory<>("tenNhanVien"));
            diaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
            gioiTinh.setCellValueFactory(cellData -> {
                boolean isNam = cellData.getValue().getGioiTinh();
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
    public void taiAnh() {
        btnTaiAnh1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Nhấn nút tải ảnh");

                // Khởi tạo FileChooser để người dùng chọn hình ảnh
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Mở file");

                // Thiết lập bộ lọc file hình ảnh
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif");
                fileChooser.getExtensionFilters().add(extFilter);

                // Mở cửa sổ chọn file và lấy file được chọn
                File file = fileChooser.showOpenDialog(null);
                if (file != null) {
                    try {
                        // Lấy đường dẫn tuyệt đối của file
                        String absolutePath = file.getAbsolutePath();
                        System.out.println(absolutePath);
                        System.out.println(file.getParent());
                        // Thư mục đích để lưu ảnh
                        File destinationDirectory = new File(file.getParent());
                        if (!destinationDirectory.exists()) {
                            destinationDirectory.mkdirs(); // Tạo thư mục nếu chưa tồn tại
                        }

                        // Tạo file đích trong thư mục
                        File destinationFile = new File(destinationDirectory, file.getName());

                        // Copy file vào thư mục đích
                        Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);


                        // Gán đường dẫn ảnh tuyệt đối vào biến duongdananh
                        duongdananh = destinationFile.getAbsolutePath();

                        // Hiển thị ảnh trong ImageView
                        Image image = new Image(destinationFile.toURI().toString());
                        image1.setImage(image);

                    } catch (IOException e) {
                        Notification.thongBao(e.getMessage(), Alert.AlertType.WARNING);
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
    public void xuatFileExcel(Stage primaryStage) {
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

    public void loaddulieulenform(NhanVien nhanVien) {

        image11 = nhanVien.getHinhAnh();
        duongdananh = image11;
        Image image = new Image("file:" + image11);
        image1.setImage(image);
        maNhanVien1.setEditable(false);
        maNhanVien1.setText(nhanVien.getMaNhanVien());
        hoTen.setText(nhanVien.getTenNhanVien());
        diaChi1.setText(nhanVien.getDiaChi());
        cccd.setText(nhanVien.getCccd());
        dienThoai.setText(nhanVien.getSdt());
        ngaySinh.setValue(nhanVien.getNgaySinh());
        if (nhanVien.getChucVuNhanVien().equals(ChucVu.NHAN_VIEN)) {
            chucVu.setValue("Nhân viên");
        } else {
            chucVu.setValue("Quản Lý");
        }
        // gioi tinh
        if (!nhanVien.getGioiTinh()) {
            gioiTinh1.setValue("NAM");
        } else {
            gioiTinh1.setValue("NỮ");
        }
        //Trang thai nhan vien
        if (nhanVien.getTrangThaiNhanVien().equals(TrangThaiNhanVien.DANG_LAM)) {
            trangThaiLamViec.setValue("ĐANG LÀM");

        } else if (nhanVien.getTrangThaiNhanVien().equals(TrangThaiNhanVien.NGHI_PHEP)) {
            trangThaiLamViec.setValue("NGHỈ PHÉP");
        } else
            trangThaiLamViec.setValue("NGHI_VIEC");

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
        try {
            nhanVienService = RMIServiceUtils.useNhanVienService();
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            throw new RuntimeException(e);
        }
        List<NhanVien> listNhanVien = null;
        try {
            listNhanVien = nhanVienService.getAllTaiKhoan();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        tableNhanVien.setPrefHeight(60);
        // Thiết lập các cột cho TableView
        try {
            nhanVienID.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
            tenNhanVien.setCellValueFactory(new PropertyValueFactory<>("tenNhanVien"));
            diaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
            gioiTinh.setCellValueFactory(cellData -> {
                boolean isNam = cellData.getValue().getGioiTinh();
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
                List<NhanVien> nvs = null;
                try {
                    nvs = nhanVienService.getAllTaiKhoan();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }

                long countNhanViensdt = nvs.stream()
                        .filter(x -> x.getSdt().equals(dienThoai.getText()))
                        .count();

                long countNhanViencccd = nvs.stream()
                        .filter(x -> x.getSdt().equals(dienThoai.getText()))
                        .count();

                if (countNhanViencccd >= 2) {
                    showWarn("Căn cước công dân này đã được sử dụng, vui lòng sử dụng số căn cước công dân khác");
                    return;
                }
                if (countNhanViensdt >= 2) {
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
                Boolean gt = !gioiTinh1.getValue().equals("NAM");
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
                    nhanVienService.updateNhanVien(cellValue, nv);
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
                            employee.getDiaChi() != null && employee.getDiaChi().contains(lowerCaseFilter) ||
                            employee.getTrangThaiNhanVien().toString().contains(lowerCaseFilter) || employee.getMaNhanVien().contains(lowerCaseFilter);
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

        tableNhanVien.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 1) { // Kiểm tra nhấp chuột đơn,nv.getNgaySinh()
                    int rowIndex = tableNhanVien.getSelectionModel().getSelectedIndex();
                    cellValue = tableNhanVien.getItems().get(rowIndex).getMaNhanVien();
                    NhanVien nvtim = null;
                    try {
                        nvtim = nhanVienService.getNhanVien(cellValue);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    loaddulieulenform(nvtim);
                }
            }
        });
    }
}
