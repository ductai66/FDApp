package com.tai06.dothetai.fdapp.OOP;

import java.io.Serializable;

public class KhachHang implements Serializable {
    private int ma_kh;
    private String email;
    private String password;
    private String ten_kh;
    private String sdt;
    private String image;

    public KhachHang() {
    }

    public KhachHang(int ma_kh, String email, String password, String ten_kh, String sdt, String image) {
        this.ma_kh = ma_kh;
        this.email = email;
        this.password = password;
        this.ten_kh = ten_kh;
        this.sdt = sdt;
        this.image = image;
    }

    public int getMa_kh() {
        return ma_kh;
    }

    public void setMa_kh(int ma_kh) {
        this.ma_kh = ma_kh;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTen_kh() {
        return ten_kh;
    }

    public void setTen_kh(String ten_kh) {
        this.ten_kh = ten_kh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
