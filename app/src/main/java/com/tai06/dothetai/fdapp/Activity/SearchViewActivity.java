package com.tai06.dothetai.fdapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.tai06.dothetai.fdapp.Adapter.SearchviewAdapter;
import com.tai06.dothetai.fdapp.OOP.KhachHang;
import com.tai06.dothetai.fdapp.OOP.Sanpham;
import com.tai06.dothetai.fdapp.R;
import com.tai06.dothetai.fdapp.URL.Link;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchViewActivity extends AppCompatActivity {

    public static final int MSG_SEARCH=1;
    public static final int MSG_KhachHang=2;

    private Toolbar toolbar;
    private SearchviewAdapter searchviewAdapter;
    private List<Sanpham> arrSanpham;
    private RecyclerView recycle_search;
    private Handler handler;
    private TextInputEditText text_search;
    private ImageButton clear_text;
    private TextView empty_search;
    private ProgressDialog progressDialog;
    private KhachHang khachHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        init();
        initHandler();
        getKhachHang();
//        Back_Arrow();
        Click_event();
    }

    private void init(){
        // toolbar
        toolbar = findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        text_search = findViewById(R.id.text_search);
        clear_text = findViewById(R.id.clear_text);
        recycle_search = findViewById(R.id.recycle_search);
        empty_search = findViewById(R.id.empty_search);
    }

    private void Click_event(){
        clear_text.setVisibility(View.GONE);
        text_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    clear_text.setVisibility(View.VISIBLE);
                }else {
                    clear_text.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        clear_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_search.getText().clear();
            }
        });
    }

    //icon seacrch on toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.search_bar:
                String values = text_search.getText().toString().trim();
                if (values.equals("")){
                    Toast.makeText(this, "Bạn chưa nhập từ khóa", Toast.LENGTH_SHORT).show();
                }else{
                    check_searchview();
                    closeKeyboard();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //initHandler
    private void initHandler(){
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case MSG_SEARCH:
                        arrSanpham = new ArrayList<>();
                        arrSanpham.addAll((Collection<? extends Sanpham>) msg.obj);
                        setSearchAdapter(arrSanpham);
                        break;
                    case MSG_KhachHang:
                        khachHang = new KhachHang();
                        khachHang = (KhachHang) msg.obj;
                        break;
                }
            }
        };
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

    private void setSearchAdapter(List<Sanpham> list){
        searchviewAdapter = new SearchviewAdapter(list,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1, GridLayoutManager.VERTICAL,false);
        recycle_search.setLayoutManager(gridLayoutManager);
        recycle_search.setAdapter(searchviewAdapter);
        recycle_search.setHasFixedSize(true);
        searchviewAdapter.notifyDataSetChanged();
    }

    private void check_searchview(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(SearchViewActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Link.URL_check_searchview, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")){
                            empty_search.setVisibility(View.VISIBLE);
                            recycle_search.setVisibility(View.GONE);
                            Toast.makeText(SearchViewActivity.this, "Không tìm thấy kết quả!", Toast.LENGTH_SHORT).show();
                        }else{
                            empty_search.setVisibility(View.GONE);
                            recycle_search.setVisibility(View.VISIBLE);
                            getSearchview();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SearchViewActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("AAA","Error"+error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("ten_sp",text_search.getText().toString().trim());
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }

    private void getSearchview(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(SearchViewActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST,Link.URL_getSearchview,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                List<Sanpham> list = new ArrayList<>();
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    for(int i = 0; i < jsonArray.length(); i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        list.add(new Sanpham(
                                                jsonObject.getInt("ma_sp"),
                                                jsonObject.getInt("ma_lh"),
                                                jsonObject.getString("ten_sp"),
                                                jsonObject.getInt("gia_sp"),
                                                jsonObject.getString("image"),
                                                jsonObject.getString("mota_sp")
                                        ));
                                        Message msg = new Message();
                                        msg.what = MSG_SEARCH;
                                        msg.obj = list;
                                        handler.sendMessage(msg);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SearchViewActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("AAA","Error"+error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> param = new HashMap<>();
                        param.put("ten_sp",text_search.getText().toString().trim());
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view!=null){
            InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    private void Back_Arrow(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchViewActivity.this,MainActivity.class);
                intent.putExtra("email",khachHang.getEmail());
                startActivity(intent);
            }
        });
    }


    public void showProduct(Sanpham sanpham){
        if (sanpham.getMa_lh()== 1 || sanpham.getMa_lh() == 3){
            Intent intent = new Intent(SearchViewActivity.this, InfoFoodActivity.class);
            intent.putExtra("khachhang",khachHang);
            intent.putExtra("food",sanpham);
            startActivity(intent);
        }else if (sanpham.getMa_lh()==2){
            Intent intent = new Intent(SearchViewActivity.this, InfoDrinkActivity.class);
            intent.putExtra("khachhang",khachHang);
            intent.putExtra("drink",sanpham);
            startActivity(intent);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}