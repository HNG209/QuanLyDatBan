package org.login.quanlydatban.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
//import org.login.quanlydatban.dao.ChiTietHoaDonDAO;
//import org.login.quanlydatban.dao.HoaDonDAO;

import org.login.quanlydatban.utilities.RMIServiceUtils;
import org.login.service.*;
import org.login.entity.ChiTietHoaDon;
import org.login.entity.HoaDon;
import org.login.quanlydatban.utilities.NumberFormatter;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

public class XuatHoaDonController implements Initializable {
    private HoaDon hoaDon;

    private HoaDonService hoaDonService;

    private CTHDService cthdService;

    @FXML
    private TextFlow hdArea;

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) throws RemoteException {
        this.hoaDon = hoaDon;
        ImageView img = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/login/quanlydatban/icons/logoHD.png"))));
        ColorAdjust grayscale = new ColorAdjust();
        grayscale.setSaturation(-1);
        img.setEffect(grayscale);
        img.setFitWidth(245);
        img.setFitHeight(180);
        img.setBlendMode(BlendMode.MULTIPLY);
        hdArea.getChildren().add(img);

        String ngayLap = String.format("\n   Ngày lập: %-50s", hoaDon.getNgayLap().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        Text nlLine = new Text(ngayLap);
        nlLine.setFont(Font.font("Courier New", FontWeight.NORMAL, 12));
        hdArea.getChildren().add(nlLine);

        String nv = String.format("\n   Nhân viên: %-50s\n   Mã hoá đơn: %-50s", hoaDon.getNhanVien().getTenNhanVien(), hoaDon.getMaHoaDon());
        Text nvLine = new Text(nv);
        nvLine.setFont(Font.font("Courier New", FontWeight.NORMAL, 12));
        hdArea.getChildren().add(nvLine);

        String kh = String.format("\n\n   Khách hàng: %s(%s)\n   Điểm tích luỹ còn lại: %d",
                hoaDon.getKhachHang() == null ? "" : hoaDon.getKhachHang().getMaKhachHang(),
                hoaDon.getKhachHang() == null ? "" : hoaDon.getKhachHang().getTenKhachHang(),
                hoaDon.getKhachHang() == null ? 0 : hoaDon.getKhachHang().getDiemTichLuy());
        Text khLine = new Text(kh);
        khLine.setFont(Font.font("Courier New", FontWeight.NORMAL, 12));
        hdArea.getChildren().add(khLine);

        String headerCol = String.format("\n\n   %-5s| %-20s| %15s| %10s| %15s\n", "STT", "Tên món", "Đơn giá", "Số lượng", "Thành tiền");
        Text headerText = new Text(headerCol);
        headerText.setFont(Font.font("Courier New", FontWeight.BOLD, 12));
        hdArea.getChildren().add(headerText);
        int index = 1;
        for (ChiTietHoaDon item : cthdService.fetchChiTietHoaDonNative(hoaDon.getMaHoaDon())) {
            String line = String.format("   %-5d| %-20s| %15s| %10d| %15s\n",
                    index++,
                    item.getMonAn().getTenMonAn(),
                    NumberFormatter.formatPrice(String.valueOf((int) item.getMonAn().getDonGia())),
                    item.getSoLuong(),
                    NumberFormatter.formatPrice(String.valueOf((int) item.getMonAn().getDonGia() * item.getSoLuong())));

            Text lineText = new Text(line);
            lineText.setFont(Font.font("Courier New", FontWeight.NORMAL, 12));
            hdArea.getChildren().add(lineText);
        }
        String phuThu = String.format("\n   %73s\n\n   %73s", "----------", "Phụ thu: " + NumberFormatter.formatPrice(String.valueOf((int) hoaDon.getPhuThu())));
        Text ptLine = new Text(phuThu);
        ptLine.setFont(Font.font("Courier New", FontWeight.NORMAL, 12));
        hdArea.getChildren().add(ptLine);

        String chietKhau = String.format("\n   %73s", "Chiết khấu: " + NumberFormatter.formatPrice(String.valueOf((int) hoaDon.getChietKhau())));
        Text ckLine = new Text(chietKhau);
        ckLine.setFont(Font.font("Courier New", FontWeight.NORMAL, 12));
        hdArea.getChildren().add(ckLine);

        String total = String.format("\n   %73s", "Tổng tiền: " + NumberFormatter.formatPrice(String.valueOf((int) hoaDonService.tinhTongTien(hoaDon))));
        Text totalLine = new Text(total);
        totalLine.setFont(Font.font("Courier New", FontWeight.NORMAL, 12));
        hdArea.getChildren().add(totalLine);

        String footer = String.format("\n\n\n\n     %10s\n  %5s", "Nhà hàng TOBO hân hạnh được phục vụ quý khách!", "Mọi thông tin liên lạc, vui lòng liên hệ: 0355227249");
        Text footerLine = new Text(footer);
        footerLine.setFont(Font.font("Courier New", FontWeight.NORMAL, 17));
        hdArea.getChildren().add(footerLine);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            hoaDonService = RMIServiceUtils.useHoaDonService();
            cthdService = RMIServiceUtils.useCTHDService();
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void huy(ActionEvent event) {
        Stage stage = (Stage) hdArea.getScene().getWindow();
        stage.close();
    }
}
