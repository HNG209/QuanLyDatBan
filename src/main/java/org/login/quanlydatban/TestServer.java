package org.login.quanlydatban;

import org.login.entity.Ban;
import org.login.service.BanService;
import org.login.service.HoaDonService;
import org.login.service.TaiKhoanService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class TestServer {
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        String host = System.getenv("HOST_NAME");
        TaiKhoanService taiKhoanService = (TaiKhoanService) Naming.lookup("rmi://" + host + ":2909/taiKhoanService");

        HoaDonService hoaDonService = (HoaDonService) Naming.lookup("rmi://" + host + ":2909/hoaDonService");
        BanService banService = (BanService) Naming.lookup("rmi://" + host + ":2909/banService");

        Ban ban = banService.readAll().getFirst();
        System.out.println(ban);

        hoaDonService.getAllHoaDon();
        hoaDonService.getHoaDonFromBan(ban).forEach(
                i -> System.out.println(i.getMaHoaDon())
        );
//        System.out.println(taiKhoanService.getTaiKhoan("LeNgocDung"));
//        String host = System.getenv("HOST_NAME");
//
//        context.bind("rmi://"+ host + ":2909/banService", banService);
//        context.bind("rmi://"+ host + ":2909/cthdService", cthdService);
//        context.bind("rmi://"+ host + ":2909/hoaDonService", hoaDonService);
//        context.bind("rmi://"+ host + ":2909/khachHangService", khachHangService);
//        context.bind("rmi://"+ host + ":2909/lichDatService", lichDatService);
//        context.bind("rmi://"+ host + ":2909/monAnService", monAnService);
//        context.bind("rmi://"+ host + ":2909/loaiMonService", loaiMonService);
//        context.bind("rmi://"+ host + ":2909/nhanVienService", nhanVienService);
//        context.bind("rmi://"+ host + ":2909/taiKhoanService", taiKhoanService);
    }
}
