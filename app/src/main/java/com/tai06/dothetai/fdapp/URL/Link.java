package com.tai06.dothetai.fdapp.URL;

import java.util.regex.Pattern;

public class Link {
    // recycle trangchu
    public static final String URL_getFood = "https://tai06dothe.000webhostapp.com/FDApp/PHP/getFood.php";
    public static final String URL_getDrink = "https://tai06dothe.000webhostapp.com/FDApp/PHP/getDrink.php";
    public static final String URL_getCombo = "https://tai06dothe.000webhostapp.com/FDApp/PHP/getCombo.php";

    //searchview
    public static final String URL_getSearchview = "https://tai06dothe.000webhostapp.com/FDApp/PHP/getSearchview.php";
    public static final String URL_check_searchview = "https://tai06dothe.000webhostapp.com/FDApp/PHP/check_searchview.php";

    //Viewmore
    public static final String URL_getViewmore = "https://tai06dothe.000webhostapp.com/FDApp/PHP/getViewmore.php";

    //check loginsignup
    public static final String URL_check_email = "https://tai06dothe.000webhostapp.com/FDApp/PHP/check_email.php";
    public static final String URL_check_login = "https://tai06dothe.000webhostapp.com/FDApp/PHP/check_login.php";
    public static final String URL_check_signup = "https://tai06dothe.000webhostapp.com/FDApp/PHP/check_signup.php";

    //khach hang
    public static final String URL_postInsertKH = "https://tai06dothe.000webhostapp.com/FDApp/PHP/postInsertKH.php";
    public static final String URL_postUpdateKH = "https://tai06dothe.000webhostapp.com/FDApp/PHP/postUpdateKH.php";
    public static final String URL_getInfoKH = "https://tai06dothe.000webhostapp.com/FDApp/PHP/getInfoKH.php";
    public static final String URL_postUpdateInfoKH = "https://tai06dothe.000webhostapp.com/FDApp/UpdateInfoKH/postUpdateInfoKH.php";
    public static final String URL_Image_User = "https://tai06dothe.000webhostapp.com/FDApp/UpdateInfoKH/ic_user.png";

    //sanpham
    public static final String URL_check_sanpham = "https://tai06dothe.000webhostapp.com/FDApp/PHP/check_sanpham.php";
    public static final String URL_postInsertSP = "https://tai06dothe.000webhostapp.com/FDApp/InsertSanPham/postInsertSP.php";
    public static final String URL_postUpdateSP = "https://tai06dothe.000webhostapp.com/FDApp/InsertSanPham/postUpdateSP.php";
    public static final String URL_postUpdateSP1 = "https://tai06dothe.000webhostapp.com/FDApp/InsertSanPham/postUpdateSP1.php";

    //donhang
    public static final String URL_getDonHang = "https://tai06dothe.000webhostapp.com/FDApp/PHP/getDonHang.php";
    public static final String URL_check_donhang = "https://tai06dothe.000webhostapp.com/FDApp/PHP/check_donhang.php";
    public static final String URL_check_hoadon = "https://tai06dothe.000webhostapp.com/FDApp/PHP/check_hoadon.php";
    public static final String URL_postDeleteCTHD = "https://tai06dothe.000webhostapp.com/FDApp/PHP/postDeleteCTHD.php";
    public static final String URL_postDeleteHD = "https://tai06dothe.000webhostapp.com/FDApp/PHP/postDeleteHD.php";
    public static final String URL_postUpdateCTHD = "https://tai06dothe.000webhostapp.com/FDApp/PHP/postUpdateCTHD.php";

    //hoa don
    public static final String URL_postInsertHD = "https://tai06dothe.000webhostapp.com/FDApp/PHP/postInsertHD.php";
    public static final String URL_postInsertCTHD = "https://tai06dothe.000webhostapp.com/FDApp/PHP/postInsertCTHD.php";

    //Slide image
    public static final String URL_Slide1 = "https://dothetaind00.000webhostapp.com/FoodDrink/Slide/banner4.jpg";
    public static final String URL_Slide2 = "https://dothetaind00.000webhostapp.com/FoodDrink/Slide/banner7.jpg";
    public static final String URL_Slide3 = "https://dothetaind00.000webhostapp.com/FoodDrink/Slide/banner6.jpg";

    //Pattern
    public static final Pattern PATTERN_EMAIL = Pattern.compile("^[a-zA-Z0-9_[^!@#%^&$*()\\s-+=]]+@[ge]mail.com$");
    public static final Pattern PATTERN_PASSWORD = Pattern.compile("^[a-zA-Z0-9[^_!@#%^&$*()\\s-+=]]{8,}$");
    public static final Pattern PATTERN_PHONE = Pattern.compile("^(03|05|07|08|09)+[0-9]{8,}$");
    public static final String  SUBJECT_SEND = "FoodD & Drink";

    //Address
    public static final String URL_getQuan = "https://tai06dothe.000webhostapp.com/FDApp/PHP/getQuan.php";
    public static final String URL_getPhuong = "https://tai06dothe.000webhostapp.com/FDApp/PHP/getPhuong.php";

    //id_quận
    public static final int id_hoankiem = 1;
    public static final int id_dongda = 2;
    public static final int id_badinh = 3;
    public static final int id_haibatrung = 4;
    public static final int id_hoangmai = 5;
    public static final int id_thanhxuan = 6;
    public static final int id_longbien = 7;
    public static final int id_namtuliem = 8;
    public static final int id_bactuliem = 9;
    public static final int id_tayho = 10;
    public static final int id_caugiay = 11;
    public static final int id_hadong = 12;

}
