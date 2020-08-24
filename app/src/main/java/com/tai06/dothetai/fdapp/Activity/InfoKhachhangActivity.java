package com.tai06.dothetai.fdapp.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.tai06.dothetai.fdapp.AdminActivity.InsertSanphamActivity;
import com.tai06.dothetai.fdapp.OOP.KhachHang;
import com.tai06.dothetai.fdapp.R;
import com.tai06.dothetai.fdapp.URL.Link;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoKhachhangActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_IMAGE = 1;

    private CircleImageView img_kh;
    private ImageView camera;
    private TextInputEditText name_kh,sdt_kh;
    private Button btn_save;
    private KhachHang khachHang;
    private boolean select = false;
    private Bitmap bitmap;
    private Toolbar toolbar_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_khachhang);
        init();
        setInfo();
        Click_event();
    }

    private void init(){
        img_kh = findViewById(R.id.img_kh);
        camera = findViewById(R.id.camera);
        name_kh = findViewById(R.id.name_kh);
        sdt_kh = findViewById(R.id.sdt_kh);
        btn_save = findViewById(R.id.btn_save);
        toolbar_info = findViewById(R.id.toolbar_info);
        setSupportActionBar(toolbar_info);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        khachHang = (KhachHang) getIntent().getSerializableExtra("khachhang");
    }

    private void setInfo(){
        name_kh.setText(khachHang.getTen_kh());
        sdt_kh.setText(khachHang.getSdt());
        Picasso.get().load(khachHang.getImage()).into(img_kh);
    }

    private void Click_event(){
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                select = true;
                eventImage();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name_kh.getText().toString().trim();
                String sdt = sdt_kh.getText().toString().trim();
                if (name.equals("") || sdt.equals("")){
                    Toast.makeText(InfoKhachhangActivity.this, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
                }else{
                    if (select == true){
                        UpdateKhachhang(imageToString(bitmap));
                    }else{
                        UpdateKhachhang(khachHang.getImage());
                    }
                }
            }
        });
    }

    private void UpdateKhachhang(String img){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Link.URL_postUpdateInfoKH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")){
                    Toast.makeText(InfoKhachhangActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(InfoKhachhangActivity.this, "Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InfoKhachhangActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                Log.d("AAA", "Error" + error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("ma_kh",String.valueOf(khachHang.getMa_kh()));
                param.put("email",khachHang.getEmail());
                param.put("ten_kh",name_kh.getText().toString().trim());
                param.put("sdt",sdt_kh.getText().toString().trim());
                param.put("image",img);
                return param;
            }
        };
        int socketTimeout = 30000;//set timeout 30s
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void eventImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image from gallery"), REQUEST_CODE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            if (uri != null){
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    img_kh.setImageBitmap(bitmap);
                    select = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                select = false;
            }
        }
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        byte[] arrByte = out.toByteArray();
        String encodedImage = Base64.encodeToString(arrByte, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}