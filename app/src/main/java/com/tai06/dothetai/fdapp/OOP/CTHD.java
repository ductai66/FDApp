package com.tai06.dothetai.fdapp.OOP;

import java.io.Serializable;

public class CTHD extends HoaDon implements Serializable {
    private int ma_cthd;
    private int ma_hd;
    private String ten_sp;
    private String image;
    private int sl_sp;
    private int gia_sp;
    private int thanhtien;
    private String ghichu;
    private String trangthai;
    private String vanchuyen;

    public CTHD(int ma_cthd, int ma_hd, int sl_sp, int thanhtien, String ghichu, String trangthai) {
        this.ma_cthd = ma_cthd;
        this.ma_hd = ma_hd;
        this.sl_sp = sl_sp;
        this.thanhtien = thanhtien;
        this.ghichu = ghichu;
        this.trangthai = trangthai;
    }

    public CTHD(int ma_hd, String ten_kh, String diachi, String sdt, String ngaydat_hd, String ngaygiao_hd,String ten_sp ,String image , int sl_sp,int gia_sp , int thanhtien, String ghichu, String trangthai,String vanchuyen) {
        super(ma_hd,ten_kh,diachi,sdt,ngaydat_hd,ngaygiao_hd);
        this.ten_sp = ten_sp;
        this.image = image;
        this.sl_sp = sl_sp;
        this.gia_sp = gia_sp;
        this.thanhtien = thanhtien;
        this.ghichu = ghichu;
        this.trangthai = trangthai;
        this.vanchuyen = vanchuyen;
    }

    public int getMa_cthd() {
        return ma_cthd;
    }

    public void setMa_cthd(int ma_cthd) {
        this.ma_cthd = ma_cthd;
    }

    public String getTen_sp() {
        return ten_sp;
    }

    public void setTen_sp(String ten_sp) {
        this.ten_sp = ten_sp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getSl_sp() {
        return sl_sp;
    }

    public void setSl_sp(int sl_sp) {
        this.sl_sp = sl_sp;
    }

    public int getGia_sp() {
        return gia_sp;
    }

    public void setGia_sp(int gia_sp) {
        this.gia_sp = gia_sp;
    }

    public int getThanhtien() {
        return thanhtien;
    }

    public void setThanhtien(int thanhtien) {
        this.thanhtien = thanhtien;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

    public String getVanchuyen() {
        return vanchuyen;
    }

    public void setVanchuyen(String vanchuyen) {
        this.vanchuyen = vanchuyen;
    }
}
