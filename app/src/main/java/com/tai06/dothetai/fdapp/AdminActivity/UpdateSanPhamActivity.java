package com.tai06.dothetai.fdapp.AdminActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tai06.dothetai.fdapp.Activity.InfoDrinkActivity;
import com.tai06.dothetai.fdapp.Activity.InfoFoodActivity;
import com.tai06.dothetai.fdapp.Activity.ViewMoreActivity;
import com.tai06.dothetai.fdapp.Adapter.UpdateAdapter;
import com.tai06.dothetai.fdapp.Adapter.ViewMoreAdapter;
import com.tai06.dothetai.fdapp.OOP.Sanpham;
import com.tai06.dothetai.fdapp.R;
import com.tai06.dothetai.fdapp.URL.Link;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UpdateSanPhamActivity extends AppCompatActivity {
    public static final int MSG_Food = 1;
    public static final int MSG_Drink = 2;
    public static final int MSG_Combo = 3;

    private List<Sanpham> arrSanpham;
    private Handler handler;
    private ImageButton expan_food,expan_drink,expan_combo;
    private Toolbar toolbar_insertsp;
    private UpdateAdapter updateAdapter;
    private RecyclerView recycle_food,recycle_drink,recycle_combo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_san_pham);
        init();
        initHandler();
        getSanpham(Link.URL_getFood,MSG_Food);
        getSanpham(Link.URL_getDrink,MSG_Drink);
        getSanpham(Link.URL_getCombo,MSG_Combo);
        Click_expanable(expan_food,recycle_food);
        Click_expanable(expan_drink,recycle_drink);
        Click_expanable(expan_combo,recycle_combo);
    }

    private void init(){
        recycle_food = findViewById(R.id.recycle_food);
        recycle_drink = findViewById(R.id.recycle_drink);
        recycle_combo = findViewById(R.id.recycle_combo);

        expan_food = findViewById(R.id.expand_food);
        expan_drink = findViewById(R.id.expand_drink);
        expan_combo = findViewById(R.id.expand_combo);

        toolbar_insertsp = findViewById(R.id.toolbar_insertsp);
        setSupportActionBar(toolbar_insertsp);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void Click_expanable(ImageButton imageButton, RecyclerView recyclerView) {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageButton.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.ic_expand_less).getConstantState())) {
                    recyclerView.setVisibility(View.GONE);
                    imageButton.setImageResource(R.drawable.ic_expand_more);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    imageButton.setImageResource(R.drawable.ic_expand_less);
                }
            }
        });
    }

    private void initHandler(){
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch(msg.what){
                    case MSG_Food:
                        arrSanpham = new ArrayList<>();
                        arrSanpham.addAll((Collection<? extends Sanpham>) msg.obj);
                        setAdapter(arrSanpham,recycle_food);
                        break;
                    case MSG_Drink:
                        arrSanpham = new ArrayList<>();
                        arrSanpham.addAll((Collection<? extends Sanpham>) msg.obj);
                        setAdapter(arrSanpham,recycle_drink);
                        break;
                    case MSG_Combo:
                        arrSanpham = new ArrayList<>();
                        arrSanpham.addAll((Collection<? extends Sanpham>) msg.obj);
                        setAdapter(arrSanpham,recycle_combo);
                        break;
                }
            }
        };
    }

    private void setAdapter(List<Sanpham> list,RecyclerView recyclerView){
        updateAdapter = new UpdateAdapter(list,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(updateAdapter);
        recyclerView.setHasFixedSize(true);
        updateAdapter.notifyDataSetChanged();
    }

    private void getSanpham(String link,int a){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(UpdateSanPhamActivity.this);
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, link, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Sanpham> list = new ArrayList<>();
                        for (int i=0;i<response.length();i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                list.add(new Sanpham(
                                        jsonObject.getInt("ma_sp"),
                                        jsonObject.getInt("ma_lh"),
                                        jsonObject.getString("ten_sp"),
                                        jsonObject.getInt("gia_sp"),
                                        jsonObject.getString("image"),
                                        jsonObject.getString("mota_sp")
                                ));
                                Message msg = new Message();
                                msg.what = a;
                                msg.obj = list;
                                handler.sendMessage(msg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UpdateSanPhamActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(jsonArrayRequest);
            }
        });
        thread.start();
    }

    public void showSanpham(Sanpham sanpham){
        Intent intent = new Intent(UpdateSanPhamActivity.this, UpdateActivity.class);
        intent.putExtra("sanpham",sanpham);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        getSanpham(Link.URL_getFood,MSG_Food);
        getSanpham(Link.URL_getDrink,MSG_Drink);
        getSanpham(Link.URL_getCombo,MSG_Combo);
        super.onResume();
    }
}