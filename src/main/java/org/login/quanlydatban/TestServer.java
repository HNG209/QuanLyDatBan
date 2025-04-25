package org.login.quanlydatban;

import org.login.service.TaiKhoanService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class TestServer {
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        String host = System.getenv("HOST_NAME");
        TaiKhoanService taiKhoanService = (TaiKhoanService) Naming.lookup("rmi://"+ host + ":2909/taiKhoanService");

        System.out.println(taiKhoanService.getTaiKhoan("LeNgocDung"));
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
