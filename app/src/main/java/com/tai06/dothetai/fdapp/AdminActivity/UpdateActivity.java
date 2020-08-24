package com.tai06.dothetai.fdapp.AdminActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.tai06.dothetai.fdapp.OOP.Sanpham;
import com.tai06.dothetai.fdapp.R;
import com.tai06.dothetai.fdapp.URL.Link;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_IMAGE = 1;
    private Bitmap bitmap;
    private Button btn_updatesp;
    private ImageView img_product;
    private Toolbar toolbar_updatesp;
    private TextInputEditText name_product,price,mota_sp;
    private Sanpham sanpham;
    private String ma_lh,ma_sp,image;
    private boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        init();
        Click_event();
    }

    private void init(){
        img_product = findViewById(R.id.img_product);
        name_product = findViewById(R.id.name_product);
        price = findViewById(R.id.price);
        mota_sp = findViewById(R.id.mota_sp);
        btn_updatesp = findViewById(R.id.btn_updatesp);

        toolbar_updatesp = findViewById(R.id.toolbar_updatesp);
        setSupportActionBar(toolbar_updatesp);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sanpham = (Sanpham) getIntent().getSerializableExtra("sanpham");
        ma_lh = String.valueOf(sanpham.getMa_lh());
        ma_sp = String.valueOf(sanpham.getMa_sp());
        image = sanpham.getImage();
        Picasso.get().load(sanpham.getImage()).into(img_product);
        name_product.setText(sanpham.getTen_sp());
        price.setText(String.valueOf(sanpham.getGia_sp()));
        mota_sp.setText(sanpham.getMota_sp());
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
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                img_product.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
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

    private void Click_event(){
        img_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = true;
                eventImage();
            }
        });

        btn_updatesp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text_tensp = name_product.getText().toString().trim();
                String text_giasp = price.getText().toString().trim();
                String text_motasp = mota_sp.getText().toString().trim();
                if (text_tensp.equals("") || text_giasp.equals("") || text_motasp.equals("")){
                    Toast.makeText(UpdateActivity.this, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
                }else{
                    int gia = Integer.parseInt(text_giasp);
                    if (gia<1000){
                        Toast.makeText(UpdateActivity.this, "Kiểm tra lại giá sản phẩm", Toast.LENGTH_SHORT).show();
                    }else {
                        if (check == true){
                            postUpdateSP(Link.URL_postUpdateSP,imageToString(bitmap));
                        }else{
                            postUpdateSP(Link.URL_postUpdateSP1,image);
                        }
                    }
                }
            }
        });
    }

    private void postUpdateSP(String url,String img){
        RequestQueue requestQueue = Volley.newRequestQueue(UpdateActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")){
                    Toast.makeText(UpdateActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(UpdateActivity.this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("ma_sp",ma_sp);
                param.put("ma_lh",ma_lh);
                param.put("ten_sp",name_product.getText().toString().trim());
                param.put("gia_sp",price.getText().toString().trim());
                param.put("image", img);
                param.put("mota_sp",mota_sp.getText().toString().trim());
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}