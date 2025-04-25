package org.login.quanlydatban.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
//import org.login.quanlydatban.dao.BaoCaoDAO;
//import org.login.quanlydatban.dao.HoaDonDAO;
//import org.login.quanlydatban.dao.NhanVienDAO;

import org.login.service.HoaDonService;
import org.login.entity.BaoCao;
import org.login.entity.TaiKhoan;
import org.login.quanlydatban.utilities.Clock;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class KetCaController {

    @FXML
    private Label chenhLech;

    @FXML
    private TextField menhGia100K;

    @FXML
    private TextField menhGia10K;

    @FXML
    private TextField menhGia1K;

    @FXML
    private TextField menhGia200K;

    @FXML
    private TextField menhGia20K;

    @FXML
    private TextField menhGia2K;

    @FXML
    private TextField menhGia500K;

    @FXML
    private TextField menhGia50K;

    @FXML
    private TextField menhGia5K;

    @FXML
    private Label tenNhanVien;

    @FXML
    private Label thoiGianHienTai;

    @FXML
    private Label tienCuoiCa;

    @FXML
    private TextField tienVaoCa;

    @FXML
    private Label tongDoanhThu;

    @FXML
    private Label tongMenhGia;

    @FXML
    private Label tongSoHoaDon;
    private TaiKhoan taiKhoan;
    private HoaDonService hoaDonService;
    private DecimalFormat df = new DecimalFormat("#,### VND");
    ;
    public static boolean isKetCa;

    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public static void setIsKetCa(boolean isKetCa) {
        KetCaController.isKetCa = isKetCa;
    }

    @FXML
    public void initialize() throws RemoteException, MalformedURLException, NotBoundException {
        String host = System.getenv("HOST_NAME");
        Clock clock = new Clock();
        clock.startClock(thoiGianHienTai);
        hoaDonService = (HoaDonService) Naming.lookup("rmi://"+ host + ":2909/hoaDonService");
        loadDuLieu();
        loadTienCuoiCaVaChenhLech();
        menhGia1K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia2K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia5K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia10K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia20K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia50K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia100K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia200K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        menhGia500K.textProperty().addListener((observable, oldValue, newValue) -> tinhTongMenhGia());
        tienVaoCa.textProperty().addListener((observable, oldValue, newValue) -> loadTienCuoiCaVaChenhLech());

    }

    public void loadDuLieu() throws RemoteException {
        String maNV = TrangChuController.taiKhoan.getNhanVien().getMaNhanVien();
        tenNhanVien.setText(TrangChuController.taiKhoan.getNhanVien().getTenNhanVien());
        Object[] doanhThuVaSoHD;
        doanhThuVaSoHD = hoaDonService.layDoanhThuVaSoHoaDon(maNV, LocalDate.now());
        tienVaoCa.setText(VaoCaController.tongTienVaoCa.isEmpty() ? df.format(0) : VaoCaController.tongTienVaoCa);
        if (doanhThuVaSoHD.length == 0) {
            tongSoHoaDon.setText("0");
            tongDoanhThu.setText("0");
        } else {
            tongDoanhThu.setText(df.format(Double.parseDouble(doanhThuVaSoHD[0].toString())));
            tongSoHoaDon.setText(doanhThuVaSoHD[1].toString());
        }
    }

    @FXML
    private void tinhTongMenhGia() {
        int tien1K = parseTextField(menhGia1K);
        int tien2K = parseTextField(menhGia2K);
        int tien5K = parseTextField(menhGia5K);
        int tien10K = parseTextField(menhGia10K);
        int tien20K = parseTextField(menhGia20K);
        int tien50K = parseTextField(menhGia50K);
        int tien100K = parseTextField(menhGia100K);
        int tien200K = parseTextField(menhGia200K);
        int tien500K = parseTextField(menhGia500K);

        double tongMenhGiaTien = tien1K * 1000 + tien2K * 2000 + tien5K * 5000
                + tien10K * 10000 + tien20K * 20000 + tien50K * 50000
                + tien100K * 100000 + tien200K * 200000 + tien500K * 500000;

        tongMenhGia.setText(df.format(tongMenhGiaTien));
        double tienVao = tienVaoCa.getText().isEmpty() ? 0.0 : Double.parseDouble(tienVaoCa.getText().replace(" VND", "").replace(",", ""));

        double tienChenhLech = tongMenhGiaTien - tienVao;
        if (tienChenhLech < 0) {
            chenhLech.setTextFill(Color.RED);
        } else chenhLech.setTextFill(Color.BLACK);
        chenhLech.setText(df.format(Math.abs(tienChenhLech)));
    }

    private int parseTextField(TextField textField) {
        try {
            return textField.getText().isEmpty() ? 0 : Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
            Platform.runLater(() -> textField.setText("0"));
            return 0;
        }
    }

    @FXML
    private void loadTienCuoiCaVaChenhLech() {
        double tienVao = tienVaoCa.getText().isEmpty() ? 0.0 : Double.parseDouble(tienVaoCa.getText().replace(" VND", "").replace(",", ""));

        double doanhThu = tongDoanhThu.getText().isEmpty() ? 0.0 : Double.parseDouble(tongDoanhThu.getText().replace(" VND", "").replace(",", ""));
        double tienCuoiCaValue = tienVao + doanhThu;
        double tongMenhGiaTien = tongMenhGia.getText().isEmpty() ? 0.0 : Double.parseDouble(tongMenhGia.getText().replace(" VND", "").replace(",", ""));
        double tienChenhLech = tongMenhGiaTien - tienCuoiCaValue;
        if (tienChenhLech < 0.0) {
            chenhLech.setTextFill(Color.RED);
        } else {
            chenhLech.setTextFill(Color.BLACK);
        }
        chenhLech.setText(df.format(Math.abs(tienChenhLech)));
        tienCuoiCa.setText(df.format(tienCuoiCaValue));
    }

    @FXML
    private void thoat() {
        int option = JOptionPane.showConfirmDialog(
                null,
                "Bạn có chắc chắn muốn hủy kết ca không?",
                "Thông báo",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            Stage stage = (Stage) Window.getWindows().stream()
                    .filter(Window::isFocused)
                    .findFirst()
                    .orElse(null);
            if (stage != null) {
                stage.close();
            }
        }
    }


    @FXML
    private void lamMoi() {
        menhGia1K.setText("");
        menhGia2K.setText("");
        menhGia5K.setText("");
        menhGia10K.setText("");
        menhGia20K.setText("");
        menhGia50K.setText("");
        menhGia100K.setText("");
        menhGia200K.setText("");
        menhGia500K.setText("");
        tongMenhGia.setText("");
        chenhLech.setText("");

    }

    @FXML
    private void xuatBaoCaoKetCa() {
        String maNhanVien = taiKhoan.getNhanVien().getMaNhanVien();
        String tenNhanVien = taiKhoan.getNhanVien().getTenNhanVien();
        String ngayLap = thoiGianHienTai.getText();
        String tienVaoCaText = null;
        String tongSoHoaDonText = tongSoHoaDon.getText();
        String tongDoanhThuText = tongDoanhThu.getText();
        String tongMenhGiaText = tongMenhGia.getText();
        String chenhLechText = chenhLech.getText();

        if (maNhanVien.isEmpty() || tenNhanVien.isEmpty() || ngayLap.isEmpty() || tienVaoCa.getText().isEmpty() ||
                tongSoHoaDonText.isEmpty() || tongDoanhThuText.isEmpty() || tongMenhGiaText.isEmpty() || chenhLechText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Dữ liệu không đầy đủ. Vui lòng kiểm tra lại.");
            return;
        } else {
            tienVaoCaText = df.format(Double.parseDouble(tienVaoCa.getText().replace(" VND", "").replace(",", "")));
        }

        if (chenhLech.getTextFill() == Color.RED) {
            int option = JOptionPane.showConfirmDialog(null, "Có chênh lệch giữa số tiền thực tế và bàn giao. Bạn có muốn tiếp tục?", "Thông báo", JOptionPane.YES_NO_OPTION);
            if (option != JOptionPane.YES_OPTION) {
                return;
            }
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn nơi lưu báo cáo");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        // Đặt tên mặc định cho tệp
        String fileName = "BaoCaoKetCa_" + maNhanVien + "_" + ngayLap.replace("/", "_").replace(":", "_").replace(" ", "") + ".txt";
        fileChooser.setInitialFileName(fileName);

        File selectedFile = fileChooser.showSaveDialog(chenhLech.getScene().getWindow());

        if (selectedFile != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {
                String title = "BÁO CÁO KẾT CA";
                int width = 78;
                int padding = (width - title.length()) / 2;
                String centeredTitle = " ".repeat(padding) + title + " ".repeat(padding);
                writer.write(centeredTitle);
                writer.newLine();
                writer.write("-----------------------------------------------------------------------");
                writer.newLine();
                writer.newLine();

                writer.write("Mã nhân viên: " + maNhanVien);
                writer.newLine();
                writer.write("Tên nhân viên: " + tenNhanVien);
                writer.newLine();
                writer.write("Thời gian vào ca: " + VaoCaController.thoiGianVaoCa);
                writer.newLine();
                writer.write("Thời gian kết ca: " + ngayLap);
                writer.newLine();
                writer.write("-----------------------------------------------------------------------");
                writer.newLine();
                writer.write("Tiền vào ca: " + tienVaoCaText);
                writer.newLine();
                writer.write("Tổng số hóa đơn: " + tongSoHoaDonText);
                writer.newLine();
                writer.write("Tổng doanh thu: " + tongDoanhThuText);
                writer.newLine();
                writer.write("-----------------------------------------------------------------------");
                writer.newLine();
                writer.write("Tiền bàn giao thực tế: " + tongMenhGiaText);
                writer.newLine();
                writer.write("Chênh lệch: " + chenhLechText);
                writer.newLine();

//                BaoCao bc = new BaoCao(null, VaoCaController.thoiGianVaoCa, thoiGianHienTai.getText(), Double.parseDouble(tienVaoCaText.replace(" VND", "").replace(",", "")), Double.parseDouble(tongDoanhThuText.replace(" VND", "").replace(",", "")), Double.parseDouble(tongMenhGiaText.replace(" VND", "").replace(",", "")), taiKhoan.getNhanVien());
//                BaoCaoDAO baoCaoDAO = new BaoCaoDAO();
//                baoCaoDAO.themBaoCao(bc);

                JOptionPane.showMessageDialog(null, "Xuất báo cáo kết ca thành công");
                isKetCa = true;
                for (Window window : Window.getWindows()) {
                    if (window instanceof Stage) {
                        ((Stage) window).close();
                    }
                }
                Stage currentStage = (Stage) chenhLech.getScene().getWindow();
                currentStage.close();
                TrangChuController.dangXuat();

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Xuất báo cáo kết ca không thành công");
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Không có tệp nào được chọn. Báo cáo không được lưu.");
        }
    }
}
