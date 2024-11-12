package org.login.quanlydatban.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.login.quanlydatban.dao.NhanVienDAO;
import org.login.quanlydatban.dao.TaiKhoanDAO;
import org.login.quanlydatban.entity.NhanVien;

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

    private   String showImageUrl = getClass().getResource("/org/login/quanlydatban/icons/show.png").toString();
    private   String hideImageUrl = getClass().getResource("/org/login/quanlydatban/icons/hide.png").toString();
    @FXML
    private TableColumn<NhanVien, String> tableMaNhanVien; // 0 Cột ID
    @FXML
    private TableColumn<NhanVien, String> tableTenNhanVien; // 1 Cột Họ Tên
    @FXML
    private TableColumn<NhanVien, String> tableTenTaiKhoan; // 2
    @FXML
    private Button btnLuu;
    @FXML
    private Button btnHuy;
    private NhanVienDAO nhanVienDAO;
    private String matKhauHien;
    @FXML
    private TextField nhapLaiMatKhau;
    private TrangThemNhanVienController themNhanVienController = new TrangThemNhanVienController();
    private static final String ALGORITHM = "AES";

    public TrangQuanLyTaiKhoanController() throws Exception {
    }


    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public  String encrypt(String data, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(128); // You can choose 192 or 256 bits
        return keyGen.generateKey();
    }

    public String decrypt(String encryptedData, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        byte[] originalData = cipher.doFinal(decodedData);
        return new String(originalData);
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
                            password.setText(taiKhoanDAO.getTaiKhoanNhanVien(cellValue).getPassword().toString());
                            System.out.println(decrypt(matKhauHien,generateKey()));
                            //nhapLaiMatKhau.setText(themNhanVienController.decrypt(password.getText(), themNhanVienController.getKey()));
                            Image image = new Image("file:"+ nvtim.getHinhAnh());
                            hinhAnh.setImage(image);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        });
    }
}
