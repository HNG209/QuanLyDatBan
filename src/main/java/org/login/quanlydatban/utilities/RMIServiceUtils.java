package org.login.quanlydatban.utilities;

import org.login.service.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIServiceUtils {
    private static final String hostName = "DESKTOP-AU6OVKD";

    public static BanService useBanService() throws MalformedURLException, NotBoundException, RemoteException {
        return (BanService) Naming.lookup("rmi://" + hostName + ":2909/banService");
    }

    public static CTHDService useCTHDService() throws MalformedURLException, NotBoundException, RemoteException {
        return (CTHDService) Naming.lookup("rmi://" + hostName + ":2909/cthdService");
    }

    public static HoaDonService useHoaDonService() throws MalformedURLException, NotBoundException, RemoteException {
        return (HoaDonService) Naming.lookup("rmi://" + hostName + ":2909/hoaDonService");
    }

    public static KhachHangService useKhachHangService() throws MalformedURLException, NotBoundException, RemoteException {
        return (KhachHangService) Naming.lookup("rmi://" + hostName + ":2909/khachHangService");
    }

    public static LichDatService useLichDatService() throws MalformedURLException, NotBoundException, RemoteException {
        return (LichDatService) Naming.lookup("rmi://" + hostName + ":2909/lichDatService");
    }

    public static MonAnService useMonAnService() throws MalformedURLException, NotBoundException, RemoteException {
        return (MonAnService) Naming.lookup("rmi://" + hostName + ":2909/monAnService");
    }

    public static LoaiMonService useLoaiMonService() throws MalformedURLException, NotBoundException, RemoteException {
        return (LoaiMonService) Naming.lookup("rmi://" + hostName + ":2909/loaiMonService");
    }

    public static NhanVienService useNhanVienService() throws MalformedURLException, NotBoundException, RemoteException {
        return (NhanVienService) Naming.lookup("rmi://" + hostName + ":2909/nhanVienService");
    }

    public static TaiKhoanService useTaiKhoanService() throws MalformedURLException, NotBoundException, RemoteException {
        return (TaiKhoanService) Naming.lookup("rmi://" + hostName + ":2909/taiKhoanService");
    }
}
