package com.tai06.dothetai.fdapp.AdminActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.tai06.dothetai.fdapp.Activity.MainActivity;
import com.tai06.dothetai.fdapp.R;
import com.tai06.dothetai.fdapp.URL.Link;
import com.tai06.dothetai.fdapp.URL.RandomCode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsertSanphamActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_IMAGE = 1;

    public static final int MAX = 999999;
    public static final int MIN = 1;
    private boolean check;
    private boolean select = false;
    private Bitmap bitmap;
    private Spinner sp_category;
    private Button btn_insertsp;
    private ImageView img_product;
    private Toolbar toolbar_insertsp;
    private TextInputEditText name_product, price, mota_sp;
    private String ma_lh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_sanpham);
        init();
        SpinnerCategory();
        Click_event();
    }

    private void init() {
        img_product = findViewById(R.id.img_product);
        sp_category = findViewById(R.id.sp_category);
        name_product = findViewById(R.id.name_product);
        price = findViewById(R.id.price);
        mota_sp = findViewById(R.id.mota_sp);
        btn_insertsp = findViewById(R.id.btn_insertsp);

        toolbar_insertsp = findViewById(R.id.toolbar_insertsp);
        setSupportActionBar(toolbar_insertsp);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void Click_event() {
        img_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                select = true;
                eventImage();
            }
        });

        btn_insertsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text_btn = btn_insertsp.getText().toString().trim();
                String text_tensp = name_product.getText().toString().trim();
                String text_giasp = price.getText().toString().trim();
                String text_motasp = mota_sp.getText().toString().trim();
                if (text_tensp.equals("") || text_giasp.equals("") || text_motasp.equals("")) {
                    Toast.makeText(InsertSanphamActivity.this, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    int gia = Integer.parseInt(text_giasp);
                    if (gia < 1000) {
                        Toast.makeText(InsertSanphamActivity.this, "Kiểm tra lại giá sản phẩm", Toast.LENGTH_SHORT).show();
                    } else{
                        if (text_btn.equals("Thêm sản phẩm")){
                            if (select == true){
                                check_sanpham();
                            }else{
                                Toast.makeText(InsertSanphamActivity.this, "Vui lòng chọn hình ảnh", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            name_product.getText().clear();
                            price.getText().clear();
                            mota_sp.getText().clear();
                            btn_insertsp.setText("Thêm sản phẩm");
                        }
                    }
                }
            }
        });
    }

    private void eventImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image from gallery"), REQUEST_CODE_IMAGE);
    }

    private void check_sanpham() {
        do {
            int codeSP = new RandomCode().randomCode(MIN, MAX);
            RequestQueue requestQueue = Volley.newRequestQueue(InsertSanphamActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Link.URL_check_sanpham, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.trim().equals("success")) {
                        check = true;
                    } else {
                        postInsertSP(codeSP);
                        check = false;
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(InsertSanphamActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                    Log.d("AAA", "Error" + error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("ma_sp", String.valueOf(codeSP));
                    return param;
                }
            };
            requestQueue.add(stringRequest);
        } while (check);
    }

    private void postInsertSP(int ma_sp) {
        RequestQueue requestQueue = Volley.newRequestQueue(InsertSanphamActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Link.URL_postInsertSP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    Toast.makeText(InsertSanphamActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    btn_insertsp.setText("Tiếp tục");
                } else {
                    Toast.makeText(InsertSanphamActivity.this, "Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InsertSanphamActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                Log.d("AAA", "Error" + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("ma_sp", String.valueOf(ma_sp));
                param.put("ma_lh", ma_lh);
                param.put("ten_sp", name_product.getText().toString());
                param.put("gia_sp", price.getText().toString().trim());
                String img = imageToString(bitmap);
                param.put("image", img);
                param.put("mota_sp", mota_sp.getText().toString().trim());
                return param;
            }
        };
        int socketTimeout = 30000;//set timeout 30s
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void SpinnerCategory() {
        List<String> list = new ArrayList<>();
        list.add(0, "Food");
        list.add(1, "Drink");
        list.add(2, "Combo");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_category.setAdapter(arrayAdapter);
        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        ma_lh = "1";
                        break;
                    case 1:
                        ma_lh = "2";
                        break;
                    case 2:
                        ma_lh = "3";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            if (uri != null){
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    img_product.setImageBitmap(bitmap);
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