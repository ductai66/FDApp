package com.tai06.dothetai.fdapp.OOP;

import java.io.Serializable;

public class Sanpham implements Serializable {
    private int ma_sp;
    private int ma_lh;
    private String ten_sp;
    private int gia_sp;
    private String image;
    private String mota_sp;

    public Sanpham() {
    }

    public Sanpham(int ma_sp, int ma_lh, String ten_sp, int gia_sp, String image, String mota_sp) {
        this.ma_sp = ma_sp;
        this.ma_lh = ma_lh;
        this.ten_sp = ten_sp;
        this.gia_sp = gia_sp;
        this.image = image;
        this.mota_sp = mota_sp;
    }

    public int getMa_sp() {
        return ma_sp;
    }

    public void setMa_sp(int ma_sp) {
        this.ma_sp = ma_sp;
    }

    public int getMa_lh() {
        return ma_lh;
    }

    public void setMa_lh(int ma_lh) {
        this.ma_lh = ma_lh;
    }

    public String getTen_sp() {
        return ten_sp;
    }

    public void setTen_sp(String ten_sp) {
        this.ten_sp = ten_sp;
    }

    public int getGia_sp() {
        return gia_sp;
    }

    public void setGia_sp(int gia_sp) {
        this.gia_sp = gia_sp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMota_sp() {
        return mota_sp;
    }

    public void setMota_sp(String mota_sp) {
        this.mota_sp = mota_sp;
    }
}
