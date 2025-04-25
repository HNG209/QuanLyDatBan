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
    }
}
