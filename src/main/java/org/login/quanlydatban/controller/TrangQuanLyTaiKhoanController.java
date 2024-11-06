package org.login.quanlydatban.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.login.quanlydatban.dao.NhanVienDAO;
import org.login.quanlydatban.dao.TaiKhoanDAO;
import org.login.quanlydatban.entity.NhanVien;
import java.net.URL;
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
    private  ImageView btnHinhAnh;


    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            nhanVienDAO = new NhanVienDAO();
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


       // chuyen doi hinh anh
        btnHinhAnh.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
               String currentImageUrl = btnHinhAnh.getImage().getUrl();
                // an ma muon hien
                if (currentImageUrl.equals(hideImageUrl)) {

                    btnHinhAnh.setImage(new Image(showImageUrl));
                    btnHinhAnh.toFront();
                } else {
                    StringBuilder BuiString = new StringBuilder();

                    btnHinhAnh.setImage(new Image(hideImageUrl));
                    btnHinhAnh.toFront();
                }
                // dang hien nhung muon an
            }
        });

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
                            tenNhanVienn.setText(nvtim.getTenNhanVien());
                            maNhanVien.setText(nvtim.getMaNhanVien());
                            tenTaiKhoan.setText(taiKhoanDAO.getTaiKhoanNhanVien(cellValue).getUserName().toString());

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
