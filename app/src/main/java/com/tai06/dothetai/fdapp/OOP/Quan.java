package com.tai06.dothetai.fdapp.OOP;

public class Quan {
    private int ma_quan;
    private String ten_quan;

    public Quan() {
    }

    public Quan(int ma_quan, String ten_quan) {
        this.ma_quan = ma_quan;
        this.ten_quan = ten_quan;
    }

    public int getMa_quan() {
        return ma_quan;
    }

    public void setMa_quan(int ma_quan) {
        this.ma_quan = ma_quan;
    }

    public String getTen_quan() {
        return ten_quan;
    }

    public void setTen_quan(String ten_quan) {
        this.ten_quan = ten_quan;
    }
}
