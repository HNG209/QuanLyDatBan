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
import org.login.service.CTHDService;
import org.login.entity.ChiTietHoaDon;
import org.login.entity.LichDat;
import org.login.quanlydatban.utilities.NumberFormatter;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

public class PhieuDatLichController implements Initializable {
    @FXML
    private TextFlow phieuDatArea;

    private LichDat lichDat;

    private CTHDService cthdService;

    public LichDat getLichDat() {
        return lichDat;
    }

    public void setLichDat(LichDat lichDat) throws RemoteException {
        this.lichDat = lichDat;
        ImageView img = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/login/quanlydatban/icons/logoHD.png"))));
        ColorAdjust grayscale = new ColorAdjust();
        grayscale.setSaturation(-1);
        img.setEffect(grayscale);
        img.setFitWidth(245);
        img.setFitHeight(180);
        img.setBlendMode(BlendMode.MULTIPLY);
        phieuDatArea.getChildren().add(img);

        String maLD = String.format("\n   Mã lịch đặt: %s", lichDat.getMaLichDat());
        Text maLDline = new Text(maLD);
        maLDline.setFont(Font.font("Courier New", FontWeight.NORMAL, 12));
        phieuDatArea.getChildren().add(maLDline);

        String ngayLap = String.format("\n   Ngày đặt: %s", lichDat.getThoiGianDat().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " (" + lichDat.getThoiGianDat().toLocalTime().getHour() + ":" + lichDat.getThoiGianDat().toLocalTime().getMinute() + ")");
        Text nlLine = new Text(ngayLap);
        nlLine.setFont(Font.font("Courier New", FontWeight.NORMAL, 12));
        phieuDatArea.getChildren().add(nlLine);

        String ngayNhanban = String.format("\n   Ngày nhận bàn: %s", lichDat.getThoiGianNhanBan().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " (" + lichDat.getThoiGianNhanBan().toLocalTime().getHour() + ":" + lichDat.getThoiGianNhanBan().toLocalTime().getMinute() + ")");
        Text nnbLine = new Text(ngayNhanban);
        nnbLine.setFont(Font.font("Courier New", FontWeight.NORMAL, 12));
        phieuDatArea.getChildren().add(nnbLine);

        String nv = String.format("\n\n   Nhân viên: %s(%s)\n   Khách hàng: %s(%s) CCCD: %s\n",
                lichDat.getNhanVien().getMaNhanVien(),
                lichDat.getNhanVien().getTenNhanVien(),
                lichDat.getKhachHang().getMaKhachHang(),
                lichDat.getKhachHang().getTenKhachHang(),
                lichDat.getKhachHang().getCccd()
        );
        Text nvLine = new Text(nv);
        nvLine.setFont(Font.font("Courier New", FontWeight.NORMAL, 12));
        phieuDatArea.getChildren().add(nvLine);

        String headerCol = String.format("\n   Chi tiết đặt trước\n\n   %-5s| %-20s| %15s| %10s| %15s\n", "STT", "Tên món", "Đơn giá", "Số lượng", "Thành tiền");
        Text headerText = new Text(headerCol);
        headerText.setFont(Font.font("Courier New", FontWeight.BOLD, 12));
        phieuDatArea.getChildren().add(headerText);

        int index = 1;
        for (ChiTietHoaDon item : cthdService.fetchChiTietHoaDonNative(lichDat.getHoaDon().getMaHoaDon())) {
            String line = String.format("   %-5d| %-20s| %15s| %10d| %15s\n",
                    index++,
                    item.getMonAn().getTenMonAn(),
                    NumberFormatter.formatPrice(String.valueOf((int) item.getMonAn().getDonGia())),
                    item.getSoLuong(),
                    NumberFormatter.formatPrice(String.valueOf((int) item.getMonAn().getDonGia() * item.getSoLuong())));

            Text lineText = new Text(line);
            lineText.setFont(Font.font("Courier New", FontWeight.NORMAL, 12));
            phieuDatArea.getChildren().add(lineText);
        }

        String total = String.format("\n   %73s\n   %73s",
                "Tổng tiền: " + NumberFormatter.formatPrice(String.valueOf((int) lichDat.getHoaDon().tinhTongTien())),
                "Tiền cọc: " + NumberFormatter.formatPrice(String.valueOf((int) lichDat.getTienCoc())));
        Text totalLine = new Text(total);
        totalLine.setFont(Font.font("Courier New", FontWeight.NORMAL, 12));
        phieuDatArea.getChildren().add(totalLine);

        String footer = String.format("\n\n\n\n     %10s\n  %5s", "Nhà hàng TOBO hân hạnh được phục vụ quý khách!", "Mọi thông tin liên lạc, vui lòng liên hệ: 0355227249");
        Text footerLine = new Text(footer);
        footerLine.setFont(Font.font("Courier New", FontWeight.NORMAL, 17));
        phieuDatArea.getChildren().add(footerLine);
    }

    @FXML
    void huy(ActionEvent event) {
        Stage stage = (Stage) phieuDatArea.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String host = System.getenv("HOST_NAME");
        try {
            cthdService = (CTHDService) Naming.lookup("rmi://"+ host + ":2909/cthdService");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
