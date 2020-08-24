package com.tai06.dothetai.fdapp.OOP;

public class Phuong {
    private int ma_phuong;
    private int ma_quan;
    private String ten_phuong;

    public Phuong() {
    }

    public Phuong(int ma_phuong, int ma_quan, String ten_phuong) {
        this.ma_phuong = ma_phuong;
        this.ma_quan = ma_quan;
        this.ten_phuong = ten_phuong;
    }

    public int getMa_phuong() {
        return ma_phuong;
    }

    public void setMa_phuong(int ma_phuong) {
        this.ma_phuong = ma_phuong;
    }

    public int getMa_quan() {
        return ma_quan;
    }

    public void setMa_quan(int ma_quan) {
        this.ma_quan = ma_quan;
    }

    public String getTen_phuong() {
        return ten_phuong;
    }

    public void setTen_phuong(String ten_phuong) {
        this.ten_phuong = ten_phuong;
    }
}
