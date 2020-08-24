package com.tai06.dothetai.fdapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.tai06.dothetai.fdapp.OOP.KhachHang;
import com.tai06.dothetai.fdapp.OOP.Sanpham;
import com.tai06.dothetai.fdapp.R;
import com.tai06.dothetai.fdapp.URL.Link;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InfoFoodActivity extends AppCompatActivity {
    public static final int MSG_SanPham = 1;
    public static final int MSG_KhachHang = 2;

    private Toolbar toolbar;
    private ImageView image_product,ic_remove,ic_add;
    private int sL_sp = 1, sum_money = 0;
    private int tong = 0;
    private TextView name_product,detail_product,soluong;
    private Button tongtien;
    private Sanpham sanpham;
    private KhachHang khachHang;
    private String text1 ="",text2="",text3="";
    public  String ghichu = "";
    private Handler handler;
    private CheckBox checkBox1,checkBox2,checkBox3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_food);
        init();
        initHandler();
        getSanPham();
        getKhachHang();
        Click_event();
    }

    private void init(){
        toolbar = findViewById(R.id.toolbar_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        image_product = findViewById(R.id.image_product);
        name_product = findViewById(R.id.name_product);
        detail_product = findViewById(R.id.detail_product);
        soluong = findViewById(R.id.soluong);
        tongtien = findViewById(R.id.tongtien);
        checkBox1 = findViewById(R.id.checkbox1);
        checkBox2 = findViewById(R.id.checkbox2);
        checkBox3 = findViewById(R.id.checkbox3);
        ic_add = findViewById(R.id.ic_add);
        ic_remove = findViewById(R.id.ic_remove);
    }

    private void initHandler(){
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case MSG_SanPham:
                        sanpham = new Sanpham();
                        sanpham = (Sanpham) msg.obj;
                        setSanpham(sanpham);
                        Click_checkbox(sanpham.getGia_sp());
                        break;
                    case MSG_KhachHang:
                        khachHang = new KhachHang();
                        khachHang = (KhachHang) msg.obj;
                        break;
                }
            }
        };
    }

    private void setSanpham(Sanpham sanpham){
        Picasso.get().load(sanpham.getImage()).into(image_product);
        name_product.setText(sanpham.getTen_sp());
        detail_product.setText(sanpham.getMota_sp());
        tongtien.setText(String.valueOf(sanpham.getGia_sp())+"VNĐ");
    }

    private void getSanPham(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                sanpham = (Sanpham) getIntent().getSerializableExtra("food");
                Message msg = new Message();
                msg.what = MSG_SanPham;
                msg.obj = sanpham;
                handler.sendMessage(msg);
            }
        });
        thread.start();
    }

    private void getKhachHang(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                khachHang = (KhachHang) getIntent().getSerializableExtra("khachhang");
                Message msg = new Message();
                msg.what = MSG_KhachHang;
                msg.obj = khachHang;
                handler.sendMessage(msg);
            }
        });
        thread.start();
    }

    private void Click_event(){
        tongtien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), HoaDonActivity.class);
                intent.putExtra("khachhang",khachHang);
                intent.putExtra("sanpham",sanpham);

                intent.putExtra("ghichu",ghichu);
                intent.putExtra("soluong",soluong.getText().toString().trim());
                intent.putExtra("tongtien",tongtien.getText().toString().trim());
                startActivity(intent);
            }
        });
    }

    private void Click_checkbox(int gia_mh) {
        soluong.setText(String.valueOf(sL_sp));
        ic_remove.setEnabled(false);
        ic_remove.setImageResource(R.drawable.ic_remove_silver);
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){ //mặc định ischecked = true
                    tong = tong + 23000;
                    sum_money = (sL_sp * gia_mh) + tong;
                    tongtien.setText(String.valueOf(sum_money) + "VNĐ");
                    text1 = text1 + "Miếng bò whopper nhỏ +23000VNĐ \n";
                    ghichu = text1+text2+text3;
                }else{
                    tong = tong - 23000;
                    sum_money = (sL_sp * gia_mh) + tong;
                    tongtien.setText(String.valueOf(sum_money) + "VNĐ");
                    text1 = "";
                    ghichu = text1+text2+text3;
                }
            }
        });
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){ //mặc định ischecked = true
                    tong = tong + 15000;
                    sum_money = (sL_sp * gia_mh) + tong;
                    tongtien.setText(String.valueOf(sum_money) + "VNĐ");
                    text2 = text2 + "Thịt xông khói +15000VNĐ";
                    ghichu = text1+text2+text3;
                }else{
                    tong = tong - 15000;
                    sum_money = (sL_sp * gia_mh) + tong;
                    tongtien.setText(String.valueOf(sum_money) + "VNĐ");
                    text2 = "";
                    ghichu = text1+text2+text3;
                }
            }
        });
        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){ //mặc định ischecked = true
                    tong = tong + 12000;
                    sum_money = (sL_sp * gia_mh) + tong;
                    tongtien.setText(String.valueOf(sum_money) + "VNĐ");
                    text3 = text3 + "\n Phô mai mỹ +12000VNĐ";
                    ghichu = text1+text2+text3;
                }else{
                    tong = tong - 12000;
                    sum_money = (sL_sp * gia_mh) + tong;
                    tongtien.setText(String.valueOf(sum_money) + "VNĐ");
                    text3 = "";
                    ghichu = text1+text2+text3;
                }
            }
        });

        ic_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sL_sp--;
                if (sL_sp == 1) {
                    sL_sp = 1;
                    ic_remove.setImageResource(R.drawable.ic_remove_silver);
                    ic_remove.setEnabled(false);
                }
                soluong.setText(String.valueOf(sL_sp));
                sum_money = sL_sp * gia_mh;
                tongtien.setText(String.valueOf(sum_money) + "VNĐ");
            }
        });
        ic_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sL_sp++;
                if (sL_sp > 10){
                    sL_sp = 10;
                    Toast.makeText(InfoFoodActivity.this, "Số lượng đặt tối đa", Toast.LENGTH_SHORT).show();
                }
                sum_money = sL_sp * gia_mh;
                ic_remove.setEnabled(true);
                ic_remove.setImageResource(R.drawable.ic_remove);
                soluong.setText(String.valueOf(sL_sp));
                tongtien.setText(String.valueOf(sum_money) + "VNĐ");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}