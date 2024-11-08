package org.login.quanlydatban.entity.keygenerator;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class CTHDCompositeKey implements Serializable {

    private String maHoaDon;
    private String maMonAn;

    // Default constructor
    public CTHDCompositeKey() {}

    public CTHDCompositeKey(String maHoaDon, String maMonAn) {
        this.maHoaDon = maHoaDon;
        this.maMonAn = maMonAn;
    }

    // Getters and Setters
    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public String getMaMonAn() {
        return maMonAn;
    }

    public void setMaMonAn(String maMonAn) {
        this.maMonAn = maMonAn;
    }

    // Override equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CTHDCompositeKey that = (CTHDCompositeKey) o;
        return Objects.equals(maHoaDon, that.maHoaDon) && Objects.equals(maMonAn, that.maMonAn);
    }

    @Override
    public String toString() {
        return "CTHDCompositeKey{" +
                "maHoaDon='" + maHoaDon + '\'' +
                ", maMonAn='" + maMonAn + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHoaDon, maMonAn);
    }
}
