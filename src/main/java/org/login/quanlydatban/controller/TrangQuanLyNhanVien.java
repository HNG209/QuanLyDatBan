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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.login.quanlydatban.dao.NhanVienDAO;
import org.login.quanlydatban.entity.NhanVien;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TrangQuanLyNhanVien implements Initializable {
    @FXML
    private Button btnthem;
    @FXML
    private TextField searchID;

    // bien ten nhan vien
    private String nhanvien;
    private NhanVienDAO nhanVienDAO;
    @FXML
    private TableView<NhanVien> tableNhanVien;
    @FXML
    private Button btnxuatFile;

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
                    TrangThemNhanVien nv = loader.getController();
                    nv.setTenNhanVien(getNhanvien().toString());
                    nv.SetTrangQuanLyNhanVien(TrangQuanLyNhanVien.this);
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


    public String getNhanvien() {
        return nhanvien;
    }
    // su dung de lay ten nhan vien
    public void setNhanvien(String nhanvien) {
        this.nhanvien = nhanvien;
    }

    // xuat ra file excel

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
                } else if (lowerCaseFilter.matches("^(QL|NV)\\d{4}$")) {
                    return employee.getMaNhanVien() != null && employee.getMaNhanVien().contains(lowerCaseFilter);
                    // Duyệt theo tên
                } else
                    return employee.getTenNhanVien() != null && employee.getTenNhanVien().contains(lowerCaseFilter) ||  employee.getDiaChi() != null && employee.getDiaChi().contains(lowerCaseFilter);
                    // Duyệt theo địa chỉ

            });
        });
        // Cập nhật TableView với danh sách đã lọc
        tableNhanVien.setItems(filteredList);

        // khi click mo dong tren table, thi hien len thong tin day du

        tableNhanVien.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if (mouseEvent.getClickCount() == 1) { // Kiểm tra nhấp chuột đơn,nv.getNgaySinh()
                    try {

                        int rowIndex = tableNhanVien.getSelectionModel().getSelectedIndex();
                        String cellValue = tableNhanVien.getItems().get(rowIndex).getMaNhanVien();
                        NhanVienDAO nvdao = new  NhanVienDAO();
                        NhanVien nvtim = nvdao.getNhanVien(cellValue);
                        if(nvtim != null){
                            System.out.println(nvtim.getTenNhanVien());
                            System.out.println("ma nahn vien"+ cellValue);
                            System.out.println("nhan vien da lick co ten la "+ nvtim.getMaNhanVien());
                        }

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/HienvaSuaNhanVien.fxml"));
                        Parent newWindow = loader.load();
                        TrangHienNhanVien nvfml = loader.getController();
                        nvfml.SetTrangQuanLyNhanVien(TrangQuanLyNhanVien.this);
                        nvfml.setGetMaNhanVien(cellValue);
                        nvfml.loaddulieulenform(nvtim);
                        System.out.println(nvfml.getGetMaNhanVien()+" ma nhan vien sau khi truyen ");
                        Stage stage = new Stage();
                        stage.setScene(new Scene(newWindow));
                        stage.setTitle("Quản Lý Nhân Viên");
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace(); // Xử lý ngoại lệ nếu có lỗi khi tải FXML
                    }

                }

           }

   });

//         tableNhanVien.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
//                     int selectedIndex = 0;
//                     if(newValue != null)
//                        selectedIndex = tableNhanVien.getSelectionModel().getSelectedIndex(); // Lấy chỉ số của dòng được chọn
//                        NhanVien rowData = tableNhanVien.getItems().get(selectedIndex); // Lấy mảng chuỗi từ chỉ số hàng
//                        String cellValueID = rowData.getMaNhanVien(); // Lấy giá trị từ cột
//                        NhanVien nv = listNhanVien.stream().filter(e -> e.getMaNhanVien().equals(cellValueID)).findFirst().orElse(null);
//                        // Tải giao diện từ file FXML
//                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/login/quanlydatban/views/HienvaSuaNhanVien.fxml"));
//                        Parent newWindow = null;
//                        try {
//                         newWindow = loader.load();
//                        } catch (IOException e) {
//                         throw new RuntimeException(e);
//                        }
//                        TrangHienNhanVien nvfml = loader.getController();
//                        System.out.println(cellValueID);
//
//                        nvfml.SetTrangQuanLyNhanVien(TrangQuanLyNhanVien.this, nv);
//
//                        Stage stage = new Stage();
//                        stage.setScene(new Scene(newWindow));
//                        stage.setTitle("Quản Lý Nhân Viên");
//                        stage.show();
//
//
//        });
    }
}
