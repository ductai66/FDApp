package com.tai06.dothetai.fdapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tai06.dothetai.fdapp.Adapter.ViewMoreAdapter;
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

public class ViewMoreActivity extends AppCompatActivity {
    public static final int MSG_ViewMore = 1;
    public static final int MSG_KhachHang = 2;

    private Toolbar toolbar_viewmore;
    private RecyclerView recycle_viewmore;
    private Handler handler;
    private ViewMoreAdapter viewMoreAdapter;
    private List<Sanpham> arrSanpham;
    private TextView title_viewmore;
    private KhachHang khachHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_more);
        init();
        initHandler();
        getKhachHang();
        getViewMore();
    }

    private void init(){
        toolbar_viewmore = findViewById(R.id.toolbar_viewmore);
        setSupportActionBar(toolbar_viewmore);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recycle_viewmore = findViewById(R.id.recycle_viewmore);
        title_viewmore = findViewById(R.id.title_viewmore);

    }

    private void initHandler(){
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case MSG_ViewMore:
                        arrSanpham = new ArrayList<>();
                        arrSanpham.addAll((Collection<? extends Sanpham>) msg.obj);
                        setAdapterViewMore(arrSanpham);
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

    private void setAdapterViewMore(List<Sanpham> list){
        viewMoreAdapter = new ViewMoreAdapter(list,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false );
        recycle_viewmore.setLayoutManager(gridLayoutManager);
        recycle_viewmore.setAdapter(viewMoreAdapter);
        recycle_viewmore.setHasFixedSize(true);
        viewMoreAdapter.notifyDataSetChanged();
    }

    private void getViewMore(){
        Intent intent = getIntent();
        int ma_lh = intent.getIntExtra("ma_lh",0);
        String title = intent.getStringExtra("title");
        title_viewmore.setText(title);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(ViewMoreActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Link.URL_getViewmore, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Sanpham> list = new ArrayList<>();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i=0;i<jsonArray.length();i++){
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
                                msg.what = MSG_ViewMore;
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
                        Toast.makeText(ViewMoreActivity.this, "Xảy ra lỗi!", Toast.LENGTH_SHORT).show();
                        Log.d("AAA","Error"+error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("ma_lh",String.valueOf(ma_lh));
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }


    public void showSanpham(Sanpham sanpham){
        if (sanpham.getMa_lh()== 1 || sanpham.getMa_lh() == 3){
            Intent intent = new Intent(ViewMoreActivity.this, InfoFoodActivity.class);
            intent.putExtra("khachhang",khachHang);
            intent.putExtra("food",sanpham);
            startActivity(intent);
        }else if (sanpham.getMa_lh()==2){
            Intent intent = new Intent(ViewMoreActivity.this, InfoDrinkActivity.class);
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
