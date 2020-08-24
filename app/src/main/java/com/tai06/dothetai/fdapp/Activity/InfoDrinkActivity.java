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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class InfoDrinkActivity extends AppCompatActivity {
    public static final int MSG_SanPham = 1;
    public static final int MSG_KhachHang = 2;

    private Toolbar toolbar;
    private Button tongtien;
    private Sanpham sanpham;
    private KhachHang khachHang;
    private Handler handler;
    private int sL_sp = 1, sum_money = 0;
    private TextView name_product, detail_product, soluong;
    private ImageView ic_add, ic_remove, temp_row, size_row, sugar_row, cold_row, image_product;
    private RadioGroup temp_group, size_group, sugar_group, cold_group;
    private RadioButton radioButtontemp, radioButtonsize, radioButtonsugar, radioButtoncold, sizeL, sizeM;
    private int tien1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_drink);
        init();
        initHandler();
        getSanPham();
        getKhachHang();
        Click_radio();
        Click_expanable(temp_row, temp_group);
        Click_expanable(size_row, size_group);
        Click_expanable(sugar_row, sugar_group);
        Click_expanable(cold_row, cold_group);
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        temp_group = findViewById(R.id.temp_group);
        size_group = findViewById(R.id.size_group);
        sugar_group = findViewById(R.id.sugar_group);
        cold_group = findViewById(R.id.cold_group);

        sizeM = findViewById(R.id.sizeM);
        sizeL = findViewById(R.id.sizeL);

        tongtien = findViewById(R.id.tongtien);
        name_product = findViewById(R.id.name_product);
        detail_product = findViewById(R.id.detail_product);
        ic_add = findViewById(R.id.ic_add);
        ic_remove = findViewById(R.id.ic_remove);

        temp_row = findViewById(R.id.temp_row);
        size_row = findViewById(R.id.size_row);
        sugar_row = findViewById(R.id.sugar_row);
        cold_row = findViewById(R.id.cold_row);

        image_product = findViewById(R.id.image_product);
        soluong = findViewById(R.id.soluong);

        sanpham = (Sanpham) getIntent().getSerializableExtra("drink");
        Picasso.get().load(sanpham.getImage()).into(image_product);
        name_product.setText(sanpham.getTen_sp());
        detail_product.setText(sanpham.getMota_sp());
        tongtien.setText(String.valueOf(sanpham.getGia_sp())+"VNĐ");
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
                        Click_radio(sanpham.getGia_sp());
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
                sanpham = (Sanpham) getIntent().getSerializableExtra("drink");
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

    private void Click_radio(int gia_mh) {
        soluong.setText(String.valueOf(sL_sp));
        ic_remove.setEnabled(false);
        ic_remove.setImageResource(R.drawable.ic_remove_silver);
        size_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int sl = Integer.parseInt(soluong.getText().toString().trim());
                switch (checkedId) {
                    case R.id.sizeM:
                        tien1 = 0;
                        sum_money = sl * gia_mh;
                        tongtien.setText(String.valueOf(sum_money) + "VNĐ");
                        break;
                    case R.id.sizeL:
                        tien1 = 6000;
                        sum_money = sl * (gia_mh + tien1);
                        tongtien.setText(String.valueOf(sum_money) + "VNĐ");
                        break;
                }
            }
        });
        ic_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sL_sp--;
                if (sL_sp <= 1) {
                    sL_sp = 1;
                    ic_remove.setImageResource(R.drawable.ic_remove_silver);
                    ic_remove.setEnabled(false);
                }
                soluong.setText(String.valueOf(sL_sp));
                sum_money = sL_sp * (gia_mh + tien1);
                tongtien.setText(String.valueOf(sum_money) + "VNĐ");
            }
        });
        ic_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sL_sp++;
                if (sL_sp >= 10){
                    sL_sp = 10;
                    Toast.makeText(InfoDrinkActivity.this, "Số lượng đặt tối đa", Toast.LENGTH_SHORT).show();
                }
                sum_money = sL_sp * (gia_mh + tien1);
                ic_remove.setEnabled(true);
                ic_remove.setImageResource(R.drawable.ic_remove);
                soluong.setText(String.valueOf(sL_sp));
                tongtien.setText(String.valueOf(sum_money) + "VNĐ");
            }
        });
    }

    private void Click_expanable(ImageView imageView, RadioGroup radioGroup) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageView.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.ic_expand_less).getConstantState())) {
                    radioGroup.setVisibility(View.GONE);
                    imageView.setImageResource(R.drawable.ic_expand_more);
                } else {
                    radioGroup.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.ic_expand_less);
                }
            }
        });
    }


    private void Click_radio() {
        tongtien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButtontemp = (RadioButton) findViewById(temp_group.getCheckedRadioButtonId());
                String a = radioButtontemp.getText().toString();
                radioButtonsize = (RadioButton) findViewById(size_group.getCheckedRadioButtonId());
                String b = radioButtonsize.getText().toString();
                radioButtonsugar = (RadioButton) findViewById(sugar_group.getCheckedRadioButtonId());
                String c = radioButtonsugar.getText().toString();
                radioButtoncold = (RadioButton) findViewById(cold_group.getCheckedRadioButtonId());
                String d = radioButtoncold.getText().toString();
                String ghichu = "Nhiệt độ: " + a + "\nKích thước: " + b + "\nLượng đường: " + c + "\nLượng đá: " + d;

                Intent intent = new Intent(getApplication(), HoaDonActivity.class);
                intent.putExtra("khachhang",khachHang);
                intent.putExtra("sanpham",sanpham);

                intent.putExtra("ghichu", ghichu);
                intent.putExtra("soluong", soluong.getText().toString().trim());
                intent.putExtra("tongtien", tongtien.getText().toString().trim());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}