package com.tai06.dothetai.fdapp.OOP;

import java.io.Serializable;

public class HoaDon implements Serializable {
    protected int ma_hd;
    protected int ma_sp;
    protected int ma_kh;
    protected String ten_kh;
    protected String diachi;
    protected String sdt;
    protected String ngaydat_hd;
    protected String ngaygiao_hd;

    public HoaDon() {
    }

    public HoaDon(int ma_hd, int ma_sp, int ma_kh, String ten_kh, String diachi, String sdt, String ngaydat_hd, String ngaygiao_hd) {
        this.ma_hd = ma_hd;
        this.ma_sp = ma_sp;
        this.ma_kh = ma_kh;
        this.ten_kh = ten_kh;
        this.diachi = diachi;
        this.sdt = sdt;
        this.ngaydat_hd = ngaydat_hd;
        this.ngaygiao_hd = ngaygiao_hd;
    }

    public HoaDon(int ma_hd, String ten_kh, String diachi, String sdt, String ngaydat_hd, String ngaygiao_hd) {
        this.ma_hd = ma_hd;
        this.ten_kh = ten_kh;
        this.diachi = diachi;
        this.sdt = sdt;
        this.ngaydat_hd = ngaydat_hd;
        this.ngaygiao_hd = ngaygiao_hd;
    }

    public int getMa_hd() {
        return ma_hd;
    }

    public void setMa_hd(int ma_hd) {
        this.ma_hd = ma_hd;
    }

    public int getMa_sp() {
        return ma_sp;
    }

    public void setMa_sp(int ma_sp) {
        this.ma_sp = ma_sp;
    }

    public int getMa_kh() {
        return ma_kh;
    }

    public void setMa_kh(int ma_kh) {
        this.ma_kh = ma_kh;
    }

    public String getTen_kh() {
        return ten_kh;
    }

    public void setTen_kh(String ten_kh) {
        this.ten_kh = ten_kh;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getNgaydat_hd() {
        return ngaydat_hd;
    }

    public void setNgaydat_hd(String ngaydat_hd) {
        this.ngaydat_hd = ngaydat_hd;
    }

    public String getNgaygiao_hd() {
        return ngaygiao_hd;
    }

    public void setNgaygiao_hd(String ngaygiao_hd) {
        this.ngaygiao_hd = ngaygiao_hd;
    }
}
