package com.tai06.dothetai.fdapp.Fragment.HoaDon;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
import com.tai06.dothetai.fdapp.Activity.HoaDonActivity;
import com.tai06.dothetai.fdapp.Activity.MainActivity;
import com.tai06.dothetai.fdapp.OOP.KhachHang;
import com.tai06.dothetai.fdapp.OOP.Phuong;
import com.tai06.dothetai.fdapp.OOP.Quan;
import com.tai06.dothetai.fdapp.OOP.Sanpham;
import com.tai06.dothetai.fdapp.R;
import com.tai06.dothetai.fdapp.URL.Check;
import com.tai06.dothetai.fdapp.URL.Link;
import com.tai06.dothetai.fdapp.URL.RandomCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayHoadonFragment extends Fragment {

    private View view;
    public static final int MAX = 999999;
    public static final int MIN = 1;
    public static final int MSG_QUAN = 1;
    public static final int MSG_PHUONG = 2;
    public static final int MSG_SanPham = 3;
    public static final int MSG_KhachHang = 4;

    private TextView tongtien_hd, soluong_sp, ghichu_hd, gia_sp, name_product, text_address;
    private TextView datetime, setdatetime;
    private ImageView img_product;
    private TextInputEditText name_kh, diachi, sdt_kh;
    private TextInputLayout inputlayout_sdt;
    private Button thanhtoan;
    private int tongtien = 0;
    private Toolbar toolbar_hoadon;
    private Handler handler;
    private Spinner sp_parent, sp_child;
    private List<Quan> arrQuan;
    private List<Phuong> arrPhuong;
    private String address, txt_quan, txt_phuong;
    private String ma_kh, email, ma_sp,datemy,img;
    private boolean check;
    private Sanpham sanpham;
    private KhachHang khachHang;

    public PayHoadonFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pay_hoadon, container, false);
        init();
        initHandler();
        getSanPham();
        getKhachHang();
        getDataIntent();
        getQuan();
        SetDateTime();
        Click_event();
        return view;
    }
    private void init() {
        img_product = view.findViewById(R.id.img_product);
        soluong_sp = view.findViewById(R.id.soluong_sp);
        gia_sp = view.findViewById(R.id.gia_sp);
        tongtien_hd = view.findViewById(R.id.tongtien_hd);
        ghichu_hd = view.findViewById(R.id.ghichu_hd);
        name_product = view.findViewById(R.id.name_product);
        name_kh = view.findViewById(R.id.name_kh);
        diachi = view.findViewById(R.id.diachi);
        sdt_kh = view.findViewById(R.id.sdt_kh);
        text_address = view.findViewById(R.id.text_address);
        datetime = view.findViewById(R.id.datetime);
        setdatetime = view.findViewById(R.id.setdatetime);
        inputlayout_sdt = view.findViewById(R.id.inputlayout_sdt);
        sp_parent = view.findViewById(R.id.sp_parent);
        sp_child = view.findViewById(R.id.sp_child);
        thanhtoan = view.findViewById(R.id.thanhtoan);
    }



    private void getDataIntent() {
        Bundle bundle = getArguments();
        String note = bundle.getString("ghichu");
        if (!note.trim().isEmpty()) {
            ghichu_hd.setText(bundle.getString("ghichu"));
        } else {
            ghichu_hd.setTextColor(getResources().getColor(R.color.bac));
            ghichu_hd.setText("(trống)");
        }

        soluong_sp.setText(bundle.getString("soluong"));
        tongtien_hd.setText(bundle.getString("tongtien"));
    }

    private void Click_event() {
        thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                String txt_btn = thanhtoan.getText().toString().trim();
                if (txt_btn.trim().equals("Thanh toán")){
                    check_text();
                }
                else{
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("email",email);
                    startActivity(intent);
                    thanhtoan.setEnabled(false);
                    thanhtoan.setText("Thanh toán");
                }
            }
        });
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //check text empty in edittext ?
    private void check_text() {
        String ten = name_kh.getText().toString().trim();
        String sdt = sdt_kh.getText().toString().trim();
        String dc = diachi.getText().toString().trim();
        if (ten.equals("") || sdt.equals("") || dc.equals("")) {
            Toast.makeText(getActivity(), "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
        } else {
            if (check_phone()) {
                check_hoadon();
            }else{
                Toast.makeText(getActivity(), "Kiểm tra lại thông tin", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //check sdt_kh
    private boolean check_phone(){
        String sdt = "Kiểm tra lại số điện thoại";
        return Check.checkin(inputlayout_sdt,sdt_kh, Link.PATTERN_PHONE,sdt);
    }

    // random mã hóa đơn check trên db nếu tồn tại thì random lại và ngược lại thêm vào hóa đơn và cthd
    private void check_hoadon() {
        do {
            int codeHD = new RandomCode().randomCode(MIN, MAX);
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Link.URL_check_hoadon, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.trim().equals("success")) {
                        check = true;
                    } else {
                        postInsertHD(codeHD);
                        check = false;
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Xảy ra lỗi!", Toast.LENGTH_SHORT).show();
                    Log.d("AAA", "error" + error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("ma_hd", String.valueOf(codeHD));
                    return param;
                }
            };
            requestQueue.add(stringRequest);
        } while (check);
    }

    //thêm hóa đơn vs mã hóa đơn đã đc check
    private void postInsertHD(int ma_hd) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String date_time = simpleDateFormat.format(calendar.getTime());

        //fomat date cho text datetime sang dạng yyyy/MM/dd HH:mm insert phpmyadmin
        String select_time = datetime.getText().toString().trim();
        datemy = new RandomCode().getStringForDate(select_time);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Link.URL_postInsertHD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    postInsertCTHD(ma_hd);
                } else {
                    Toast.makeText(getActivity(), "Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Mất kết nối", Toast.LENGTH_SHORT).show();
                Log.d("AAA", "error" + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("ma_hd", String.valueOf(ma_hd));
                param.put("ma_sp", ma_sp);
                param.put("ma_kh", ma_kh);
                param.put("ten_kh", name_kh.getText().toString().trim());
                param.put("diachi", diachi.getText().toString().trim() + address);
                param.put("sdt", sdt_kh.getText().toString().trim());
                param.put("ngaydat_hd", date_time);
                param.put("ngaygiao_hd", datemy);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    //thêm cthd theo mã hóa đơn
    private void postInsertCTHD(int codeHD) {
        String tong = tongtien_hd.getText().toString().trim();
        tong = tong.substring(0, tong.length() - 3);
        tongtien = Integer.parseInt(tong);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Link.URL_postInsertCTHD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    showSuccessDialog();
                    thanhtoan.setText("Quay lại trang chủ");
                } else {
                    Toast.makeText(getActivity(), "Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Mất kết nối", Toast.LENGTH_SHORT).show();
                Log.d("AAA", "error" + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("ma_hd", String.valueOf(codeHD));
                param.put("sl_sp", soluong_sp.getText().toString().trim());
                param.put("thanhtien", String.valueOf(tongtien));
                param.put("ghichu", ghichu_hd.getText().toString().trim());
                param.put("trangthai", "Đang đóng gói");
                param.put("vanchuyen","ship");
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    //thiết thập ngày giờ giao và nhận đơn hàng
    private void SetDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String getcurrent = simpleDateFormat.format(calendar.getTime());
        datetime.setText(getcurrent);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        setdatetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                String mytime = simpleDateFormat.format(calendar.getTime());

                                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
                                String timeselect = simpleDateFormat1.format(calendar.getTime());
                                String timestart = "08:00";
                                String timeout = "23:30";
                                Date now = new Date();
                                Date select = new Date();
                                Date TimeSelect = new Date();
                                Date TimeStart = new Date();
                                Date TimeOut = new Date();
                                try {
                                    now = simpleDateFormat.parse(getcurrent);
                                    select = simpleDateFormat.parse(mytime);
                                    TimeSelect = simpleDateFormat1.parse(timeselect);
                                    TimeStart = simpleDateFormat1.parse(timestart);
                                    TimeOut = simpleDateFormat1.parse(timeout);
                                    if (now.before(select) && TimeSelect.after(TimeStart) && TimeSelect.before(TimeOut)) {
                                        datetime.setText(mytime);
                                    } else {
                                        Toast.makeText(getActivity(), "Thời gian không hợp lệ", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, hour, minute, true);
                        timePickerDialog.show();
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()+(1000*60*60*24*2));//sau 2 ngày từ ngày hiện tại
//                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
    }

    //set text Address
    private void initHandler() {
        handler = new Handler() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MSG_QUAN:
                        arrQuan = new ArrayList<>();
                        arrQuan.addAll((Collection<? extends Quan>) msg.obj);
                        List<String> listQuan = new ArrayList<>();
                        arrQuan.stream().forEach(quan -> {
                            listQuan.add(quan.getTen_quan());
                        });
                        SpinnerQuan(listQuan);
                        break;
                    case MSG_PHUONG:
                        arrPhuong = new ArrayList<>();
                        arrPhuong.addAll((Collection<? extends Phuong>) msg.obj);
                        List<String> listPhuong = new ArrayList<>();
                        arrPhuong.stream().forEach(phuong -> {
                            listPhuong.add(phuong.getTen_phuong());
                        });
                        SpinnerPhuong(listPhuong);
                        break;
                    case MSG_SanPham:
                        sanpham = new Sanpham();
                        sanpham = (Sanpham) msg.obj;
                        setSanpham(sanpham);
                        break;
                    case MSG_KhachHang:
                        khachHang = new KhachHang();
                        khachHang = (KhachHang) msg.obj;
                        setKhachHang(khachHang);
                        break;
                }
            }
        };
    }

    private void setSanpham(Sanpham sanpham){
        ma_sp = String.valueOf(sanpham.getMa_sp());
        Picasso.get().load(sanpham.getImage()).into(img_product);
        img = sanpham.getImage();
        name_product.setText(sanpham.getTen_sp());
        gia_sp.setText(String.valueOf(sanpham.getGia_sp())+"VNĐ");
    }

    private void setKhachHang(KhachHang khachHang){
        ma_kh = String.valueOf(khachHang.getMa_kh());
        email = khachHang.getEmail();
        name_kh.setText(khachHang.getTen_kh());
        sdt_kh.setText(khachHang.getSdt());
    }

    private void getSanPham(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                sanpham = (Sanpham) getArguments().getSerializable("sanpham");
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
                khachHang = (KhachHang) getArguments().getSerializable("khachhang");
                Message msg = new Message();
                msg.what = MSG_KhachHang;
                msg.obj = khachHang;
                handler.sendMessage(msg);
            }
        });
        thread.start();
    }

    private void getQuan() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Link.URL_getQuan, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Quan> list = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                list.add(new Quan(
                                        jsonObject.getInt("ma_quan"),
                                        jsonObject.getString("ten_quan")
                                ));
                                Message msg = new Message();
                                msg.what = MSG_QUAN;
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
                        Toast.makeText(getActivity(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("AAA", "Error" + error.toString());
                    }
                });
                requestQueue.add(jsonArrayRequest);
            }
        });
        thread.start();
    }

    private void SpinnerQuan(List<String> list) {
        ArrayAdapter arrSP_Quan = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
        arrSP_Quan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_parent.setAdapter(arrSP_Quan);
        sp_parent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txt_quan = sp_parent.getSelectedItem().toString();
                switch (position) {
                    case 0:
                        getPhuong(Link.id_hoankiem);
                        break;
                    case 1:
                        getPhuong(Link.id_dongda);
                        break;
                    case 2:
                        getPhuong(Link.id_badinh);
                        break;
                    case 3:
                        getPhuong(Link.id_haibatrung);
                        break;
                    case 4:
                        getPhuong(Link.id_hoangmai);
                        break;
                    case 5:
                        getPhuong(Link.id_thanhxuan);
                        break;
                    case 6:
                        getPhuong(Link.id_longbien);
                        break;
                    case 7:
                        getPhuong(Link.id_namtuliem);
                        break;
                    case 8:
                        getPhuong(Link.id_bactuliem);
                        break;
                    case 9:
                        getPhuong(Link.id_tayho);
                        break;
                    case 10:
                        getPhuong(Link.id_caugiay);
                        break;
                    case 11:
                        getPhuong(Link.id_hadong);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getPhuong(int ma_quan) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Link.URL_getPhuong, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Phuong> list = new ArrayList<>();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                list.add(new Phuong(
                                        jsonObject.getInt("ma_phuong"),
                                        jsonObject.getInt("ma_quan"),
                                        jsonObject.getString("ten_phuong")
                                ));
                                Message msg = new Message();
                                msg.what = MSG_PHUONG;
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
                        Toast.makeText(getActivity(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("AAA", "Error" + error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("ma_quan", String.valueOf(ma_quan));
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }

    private void SpinnerPhuong(List<String> list) {
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_child.setAdapter(arrayAdapter);
        sp_child.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txt_phuong = sp_child.getSelectedItem().toString();
                text_address.setText("Địa chỉ: " + txt_phuong + ", " + txt_quan);
                address = ", " + txt_phuong + ", " + txt_quan;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showSuccessDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogTheme);
        builder.setCancelable(false);
        View view_alert= LayoutInflater.from(getActivity()).inflate(R.layout.layout_success_dialog,(ConstraintLayout) view.findViewById(R.id.layoutDialogContainer));
        builder.setView(view_alert);
        ((TextView) view_alert.findViewById(R.id.textTitle)).setText(getResources().getString(R.string.success_title));
        ((TextView) view_alert.findViewById(R.id.textMessage)).setText(getResources().getString(R.string.success_Message));
        ((ImageView) view_alert.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_success);
        ((Button) view_alert.findViewById(R.id.buttonNo)).setText(getResources().getString(R.string.btnNo));
        ((Button) view_alert.findViewById(R.id.buttonYes)).setText(getResources().getString(R.string.btnYes));

        AlertDialog alertDialog = builder.create();
        view_alert.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        String ten = name_kh.getText().toString().trim();
        String dc = diachi.getText().toString().trim() + address;
        String number = sdt_kh.getText().toString().trim();
        String sl = soluong_sp.getText().toString().trim();
        String gia = gia_sp.getText().toString().trim();
        Bundle bundle = getArguments();
        String note = bundle.getString("ghichu");

        view_alert.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                String date_time = simpleDateFormat.format(calendar.getTime());

                //fomat date cho text datetime sang dạng yyyy/MM/dd HH:mm insert phpmyadmin
                String select_time = datetime.getText().toString().trim();
                datemy = new RandomCode().getStringForDate(select_time);

                alertDialog.dismiss();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity(),R.style.AlertDialogTheme);
                builder1.setCancelable(false);
                View view1_alert= LayoutInflater.from(getActivity()).inflate(R.layout.layout_view_cthd,(ConstraintLayout) view.findViewById(R.id.layoutDialogCthd));
                builder1.setView(view1_alert);
                ((TextView) view1_alert.findViewById(R.id.textTitle)).setText(getResources().getString(R.string.success_title));
                ((ImageView) view1_alert.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_success);
                ((TextView) view1_alert.findViewById(R.id.ten_kh)).setText(ten);
                ((TextView) view1_alert.findViewById(R.id.diachii)).setText(dc);
                ((TextView) view1_alert.findViewById(R.id.sdt)).setText(number);
                ImageView anh = ((ImageView) view1_alert.findViewById(R.id.image));
                Picasso.get().load(img).into(anh);
                ((TextView) view1_alert.findViewById(R.id.sl_sp)).setText(sl);
                ((TextView) view1_alert.findViewById(R.id.gia_sp)).setText(gia);
                ((TextView) view1_alert.findViewById(R.id.ngaydat_hd)).setText(date_time);
                ((TextView) view1_alert.findViewById(R.id.ngaygiao_hd)).setText(datemy);
                ((TextView) view1_alert.findViewById(R.id.ghichu_hd)).setText(note);
                ((TextView) view1_alert.findViewById(R.id.trangthai)).setText("Đang đóng gói");
                ((TextView) view1_alert.findViewById(R.id.thanhtien)).setText(String.valueOf(tongtien)+"VNĐ");
                ((Button) view1_alert.findViewById(R.id.buttonYes)).setText(getResources().getString(R.string.btnNo));
                AlertDialog alertDialog1 = builder1.create();
                if (alertDialog1.getWindow() != null){
                    alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                view1_alert.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog1.dismiss();
                    }
                });
                alertDialog1.show();
            }
        });

        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }
}