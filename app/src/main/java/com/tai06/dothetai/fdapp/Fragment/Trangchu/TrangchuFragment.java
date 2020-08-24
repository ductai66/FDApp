package com.tai06.dothetai.fdapp.Fragment.Trangchu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.tai06.dothetai.fdapp.Activity.InfoDrinkActivity;
import com.tai06.dothetai.fdapp.Activity.InfoFoodActivity;
import com.tai06.dothetai.fdapp.Activity.ViewMoreActivity;
import com.tai06.dothetai.fdapp.Adapter.ComboAdapter;
import com.tai06.dothetai.fdapp.Adapter.DrinkAdapter;
import com.tai06.dothetai.fdapp.Adapter.FoodAdapter;
import com.tai06.dothetai.fdapp.Adapter.SlideImageAdapter;
import com.tai06.dothetai.fdapp.OOP.KhachHang;
import com.tai06.dothetai.fdapp.OOP.Sanpham;
import com.tai06.dothetai.fdapp.OOP.SlideImage;
import com.tai06.dothetai.fdapp.R;
import com.tai06.dothetai.fdapp.URL.Link;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TrangchuFragment extends Fragment {

    public static final int MSG_RECYL_Food = 1;
    public static final int MSG_RECYL_Drink = 2;
    public static final int MSG_RECYL_Combo = 3;
    public static final int MSG_KhachHang = 4;

    private View view;
    private ViewPager slide_page;
    private TabLayout tab_slide;
    private Handler handler;
    private RecyclerView recycle_food,recycle_drink,recycle_combo;
    private TextView more_food,more_drink,more_combo;
    private ProgressDialog progressDialog;
    private List<Sanpham> arraySanpham;
    private FoodAdapter foodAdapter;
    private DrinkAdapter drinkAdapter;
    private ComboAdapter comboAdapter;
    private KhachHang khachHang;
    private List<SlideImage> arrSlide;
    private SlideImageAdapter slideImageAdapter;


    public TrangchuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trangchu, container, false);
        init();
        progress_dialog();
        initHandler();
        getKhachHang();
        getSanpham(MSG_RECYL_Food, Link.URL_getFood);
        getSanpham(MSG_RECYL_Drink, Link.URL_getDrink);
        getSanpham(MSG_RECYL_Combo, Link.URL_getCombo);
        SlideImage();
        Click_textview(more_food,1,"Danh sách Food");
        Click_textview(more_drink,2,"Danh sách Drink");
        Click_textview(more_combo,3,"Danh sách Combo");
        return view;
    }

    private void init(){
        //autoslide
        slide_page = view.findViewById(R.id.slide_page);
        tab_slide = view.findViewById(R.id.tab_slide);

        //recyclerview
        recycle_food = view.findViewById(R.id.recycle_food);
        recycle_drink = view.findViewById(R.id.recycle_drink);
        recycle_combo = view.findViewById(R.id.recycle_combo);

        //textview
        more_food = view.findViewById(R.id.more_food);
        more_drink = view.findViewById(R.id.more_drink);
        more_combo = view.findViewById(R.id.more_combo);
    }

    //set danh sach product

    private void initHandler(){
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case MSG_RECYL_Food:
                        arraySanpham = new ArrayList<>();
                        arraySanpham.addAll((Collection<? extends Sanpham>)msg.obj);//arraySanpham sẽ nhận tất cả thông tin của Sanpham
                        FoodAdapter(arraySanpham);
                        break;
                    case MSG_RECYL_Drink:
                        arraySanpham = new ArrayList<>();
                        arraySanpham.addAll((Collection<? extends Sanpham>)msg.obj);
                        DrinkAdapter(arraySanpham);
                        break;
                    case MSG_RECYL_Combo:
                        arraySanpham = new ArrayList<>();
                        arraySanpham.addAll((Collection<? extends Sanpham>)msg.obj);
                        ComboAdapter(arraySanpham);
                        break;
                    case MSG_KhachHang:
                        khachHang = new KhachHang();
                        khachHang = (KhachHang) msg.obj;
                        break;
                }
            }
        };
    }

    private void FoodAdapter(List<Sanpham> list){
        foodAdapter = new FoodAdapter(list,TrangchuFragment.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1,GridLayoutManager.HORIZONTAL,false);
        recycle_food.setLayoutManager(gridLayoutManager);
        recycle_food.setAdapter(foodAdapter);
        recycle_food.setNestedScrollingEnabled(true);
        recycle_food.setHasFixedSize(true);
        foodAdapter.notifyDataSetChanged();
    }

    private void DrinkAdapter(List<Sanpham> list){
        drinkAdapter = new DrinkAdapter(list,TrangchuFragment.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2,GridLayoutManager.HORIZONTAL,false);
        recycle_drink.setLayoutManager(gridLayoutManager);
        recycle_drink.setAdapter(drinkAdapter);
        recycle_food.setNestedScrollingEnabled(true);
        recycle_drink.setHasFixedSize(true);
        drinkAdapter.notifyDataSetChanged();
    }

    private void ComboAdapter(List<Sanpham> list){
        comboAdapter = new ComboAdapter(list,TrangchuFragment.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1,GridLayoutManager.VERTICAL,false);
        recycle_combo.setLayoutManager(gridLayoutManager);
        recycle_combo.setAdapter(comboAdapter);
        recycle_combo.setHasFixedSize(true);
        comboAdapter.notifyDataSetChanged();
    }

    private void getSanpham(int MSG,String url){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
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
                                msg.what = MSG;
                                msg.obj = list;
                                handler.sendMessage(msg);
                                progressDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
                int socketTimeout = 30000;//set timeout 30s
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                jsonArrayRequest.setRetryPolicy(policy);
                requestQueue.add(jsonArrayRequest);
            }
        });
        thread.start();
    }

    private void getKhachHang(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                khachHang = (KhachHang) getArguments().getSerializable("khachhang");
                Message msg = new Message();
                msg.what = MSG_KhachHang;
                msg.obj = khachHang;
                handler.sendMessage(msg);
            }
        });
        thread.start();
    }

    //Auto slide image
    private void SlideImage(){
        arrSlide = new ArrayList<>();
        arrSlide.add(new SlideImage(Link.URL_Slide1));
        arrSlide.add(new SlideImage(Link.URL_Slide2));
        arrSlide.add(new SlideImage(Link.URL_Slide3));

        slideImageAdapter = new SlideImageAdapter(arrSlide,TrangchuFragment.this);
        slide_page.setAdapter(slideImageAdapter);
//        slide_page.setPadding(0,0,150,0);
//        slide_page.setPageMargin(20);
//        slideImageAdapter.notifyDataSetChanged();
        tab_slide.setupWithViewPager(slide_page,true);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TrangchuFragment.RuntimeSlide(),3000,6000);
    }

    public class RuntimeSlide extends TimerTask {
        @Override
        public void run() {
            if(getActivity()!=null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (slide_page.getCurrentItem()<arrSlide.size()-1){//nếu slide ở vị trí hiện tại nhỏ hơn so với độ dài list slide - 1
                            slide_page.setCurrentItem(slide_page.getCurrentItem()+1);//set slide hiện tại = slide hiện tại + 1
                        }else{
                            slide_page.setCurrentItem(0);//ngược lại nếu slide htai = độ dài list slide thì quay về slide ban đầu
                        }
                    }
                });
            }
        }
    }

    private void progress_dialog(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void Click_textview(TextView textview,int ma_lh,String title){
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewMoreActivity.class);
                intent.putExtra("khachhang",khachHang);
                intent.putExtra("ma_lh",ma_lh);
                intent.putExtra("title",title);
                startActivity(intent);
            }
        });
    }

    public void Click_ItemFood(Sanpham sanpham){
        Intent intent = new Intent(getActivity(), InfoFoodActivity.class);
        intent.putExtra("khachhang",khachHang);
        intent.putExtra("food",sanpham);
        startActivity(intent);
    }

    public void Click_ItemDrink(Sanpham sanpham){
        Intent intent = new Intent(getActivity(), InfoDrinkActivity.class);
        intent.putExtra("khachhang",khachHang);
        intent.putExtra("drink",sanpham);
        startActivity(intent);
    }

}