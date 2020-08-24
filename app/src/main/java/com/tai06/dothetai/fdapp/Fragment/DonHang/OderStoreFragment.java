package com.tai06.dothetai.fdapp.Fragment.DonHang;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tai06.dothetai.fdapp.Adapter.DonHangAdapter;
import com.tai06.dothetai.fdapp.Adapter.OderAdapter;
import com.tai06.dothetai.fdapp.OOP.CTHD;
import com.tai06.dothetai.fdapp.OOP.KhachHang;
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

public class OderStoreFragment extends Fragment {

    public static final int MSG_DONHANG = 1;
    public static final int MSG_KhachHang = 2;

    private RecyclerView recycle_donhang;
    private Handler handler;
    private List<CTHD> arrCTHD;
    private OderAdapter oderAdapter;
    private KhachHang khachHang;
    private ProgressDialog progressDialog;
    private TextView empty_donhang;

    private View view;

    public OderStoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_oder_store, container, false);
        init();
        progress_dialog();
        initHandler();
        getKhachHang();
        check_donhang();
        return view;
    }

    private void init() {
        recycle_donhang = view.findViewById(R.id.recycle_donhang);
        empty_donhang = view.findViewById(R.id.empty_donhang);
    }

    public void progress_dialog(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MSG_DONHANG:
                        arrCTHD = new ArrayList<>();
                        arrCTHD.addAll((Collection<? extends CTHD>) msg.obj);
                        setDonHangAdapter(arrCTHD);
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
                khachHang = (KhachHang) getArguments().getSerializable("khachhang");
                Message msg = new Message();
                msg.what = MSG_KhachHang;
                msg.obj = khachHang;
                handler.sendMessage(msg);
            }
        });
        thread.start();
    }

    public void setDonHangAdapter(List<CTHD> list) {
        oderAdapter = new OderAdapter(list, OderStoreFragment.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        recycle_donhang.setLayoutManager(gridLayoutManager);
        recycle_donhang.setAdapter(oderAdapter);
        recycle_donhang.setHasFixedSize(true);
        oderAdapter.notifyDataSetChanged();
    }

    private void getDonHang() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Link.URL_getDonHang, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<CTHD> list = new ArrayList<>();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                list.add(new CTHD(
                                        jsonObject.getInt("ma_hd"),
                                        jsonObject.getString("ten_kh"),
                                        jsonObject.getString("diachi"),
                                        jsonObject.getString("sdt"),
                                        jsonObject.getString("ngaydat_hd"),
                                        jsonObject.getString("ngaygiao_hd"),
                                        jsonObject.getString("ten_sp"),
                                        jsonObject.getString("image"),
                                        jsonObject.getInt("sl_sp"),
                                        jsonObject.getInt("gia_sp"),
                                        jsonObject.getInt("thanhtien"),
                                        jsonObject.getString("ghichu"),
                                        jsonObject.getString("trangthai"),
                                        jsonObject.getString("vanchuyen")
                                ));
                                Message msg = new Message();
                                msg.what = MSG_DONHANG;
                                msg.obj = list;
                                handler.sendMessage(msg);
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("AAA", "Error" + error.toString());
                        progressDialog.dismiss();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("ma_kh", String.valueOf(khachHang.getMa_kh()));
                        param.put("vanchuyen","oder");
                        return param;
                    }
                };
//                int socketTimeout = 30000;//set timeout 30s
//                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//                stringRequest.setRetryPolicy(policy);
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }

    private void check_donhang() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Link.URL_check_donhang, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")) {
                            empty_donhang.setVisibility(View.VISIBLE);
                            recycle_donhang.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Bạn chưa có đơn hàng nào!", Toast.LENGTH_SHORT).show();
                        } else {
                            empty_donhang.setVisibility(View.GONE);
                            recycle_donhang.setVisibility(View.VISIBLE);
                            getDonHang();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("AAA", "Error" + error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("ma_kh", String.valueOf(khachHang.getMa_kh()));
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }

    public void ShowHide(ImageView imageView, TableLayout tableLayout) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageView.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.ic_expand_more).getConstantState())) {
                    tableLayout.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.ic_expand_less);
                } else {
                    tableLayout.setVisibility(View.GONE);
                    imageView.setImageResource(R.drawable.ic_expand_more);
                }
            }
        });
    }

    //cap nhat cthd sau khi huy don hang
    public void postUpdateCTHD(TextView textView,int ma_hd){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Link.URL_postUpdateCTHD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")){
                    Toast.makeText(getActivity(), "Hệ thống đã xử lí", Toast.LENGTH_SHORT).show();
                    textView.setText("Đã hủy");
                }else{
                    Toast.makeText(getActivity(),"Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                Log.d("AAA", "Lỗi!\n " + error.toString());
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("ma_hd",String.valueOf(ma_hd));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    //Xoa CTHD
    public void postDelectCTHD(CTHD cthd,int i){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Link.URL_postDeleteCTHD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")){
                    Toast.makeText(getActivity(), "Đã xóa", Toast.LENGTH_SHORT).show();
                    postDelectHD(cthd,i);
                }else{
                    Toast.makeText(getActivity(),"Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                Log.d("AAA", "Lỗi!\n " + error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("ma_hd",String.valueOf(cthd.getMa_hd()));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    //Xoa HD
    public void postDelectHD(CTHD cthd,int i){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Link.URL_postDeleteHD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")){
                    Toast.makeText(getActivity(), "Đã xóa", Toast.LENGTH_SHORT).show();
                    arrCTHD.remove(i);
                    setDonHangAdapter(arrCTHD);
                }else{
                    Toast.makeText(getActivity(),"Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                Log.d("AAA", "Lỗi!\n " + error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("ma_hd",String.valueOf(cthd.getMa_hd()));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }
}