package com.tai06.dothetai.fdapp.Fragment.HoaDon;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
import com.tai06.dothetai.fdapp.Activity.MainActivity;
import com.tai06.dothetai.fdapp.OOP.KhachHang;
import com.tai06.dothetai.fdapp.OOP.Sanpham;
import com.tai06.dothetai.fdapp.R;
import com.tai06.dothetai.fdapp.URL.Check;
import com.tai06.dothetai.fdapp.URL.Link;
import com.tai06.dothetai.fdapp.URL.RandomCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OderFragment extends Fragment {

    public static final int MAX = 999999;
    public static final int MIN = 1;

    public static final int MSG_Sanpham = 1;
    public static final int MSG_Khachhang = 2;

    private View view;
    private TextInputEditText name_kh,sdt_kh;
    private TextInputLayout layout_edit2;
    private ImageView img_product;
    private TextView name_product,gia_sp,soluong_sp,ghichu_hd,tongtien_hd;
    private TextView datetime,setdatetime;
    private Button thanhtoan;
    private Sanpham sanpham;
    private KhachHang khachHang;
    private Handler handler;
    private boolean check;
    private int tongtien = 0;
    private String ma_kh, email, ma_sp,datemy,img;

    public OderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_oder, container, false);
        init();
        initHandler();
        getDataIntent();
        getKhachHang();
        getSanPham();
        SetDateTime();
        Click_event();
        return view;
    }

    private void init(){
        name_kh = view.findViewById(R.id.name_kh);
        sdt_kh = view.findViewById(R.id.sdt_kh);
        datetime = view.findViewById(R.id.datetime);
        setdatetime = view.findViewById(R.id.setdatetime);
        img_product = view.findViewById(R.id.img_product);
        name_product = view.findViewById(R.id.name_product);
        gia_sp = view.findViewById(R.id.gia_sp);
        soluong_sp = view.findViewById(R.id.soluong_sp);
        ghichu_hd = view.findViewById(R.id.ghichu_hd);
        tongtien_hd = view.findViewById(R.id.tongtien_hd);
        thanhtoan = view.findViewById(R.id.thanhtoan);
        layout_edit2 = view.findViewById(R.id.layout_edit2);
    }

    private void initHandler(){
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case MSG_Sanpham:
                        sanpham = new Sanpham();
                        sanpham = (Sanpham) msg.obj;
                        setSanpham(sanpham);
                        break;
                    case MSG_Khachhang:
                        khachHang = new KhachHang();
                        khachHang = (KhachHang) msg.obj;
                        setKhachHang(khachHang);
                        break;
                }
            }
        };
    }

    private void Click_event(){
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

    private void check_text() {
        String ten = name_kh.getText().toString().trim();
        String sdt = sdt_kh.getText().toString().trim();
        if (ten.equals("") || sdt.equals("")) {
            Toast.makeText(getActivity(), "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
        } else {
            if (check_phone()) {
                check_hoadon();
            }else{
                Toast.makeText(getActivity(), "Kiểm tra lại thông tin", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean check_phone(){
        String sdt = "Kiểm tra lại số điện thoại";
        return Check.checkin(layout_edit2,sdt_kh, Link.PATTERN_PHONE,sdt);
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
                msg.what = MSG_Sanpham;
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
                msg.what = MSG_Khachhang;
                msg.obj = khachHang;
                handler.sendMessage(msg);
            }
        });
        thread.start();
    }

    private void check_hoadon(){
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

    private void postInsertHD(int ma_hd){
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
                param.put("diachi", "Nhận tại cửa hàng");
                param.put("sdt", sdt_kh.getText().toString().trim());
                param.put("ngaydat_hd", date_time);
                param.put("ngaygiao_hd", datemy);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

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
                param.put("vanchuyen","oder");
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

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

    private void showSuccessDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogTheme);
        builder.setCancelable(false);
        View view_alert = LayoutInflater.from(getContext()).inflate(R.layout.layout_success_dialog, (ConstraintLayout) view.findViewById(R.id.layoutDialogContainer));
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
                android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(getActivity(),R.style.AlertDialogTheme);
                builder1.setCancelable(false);
                View view1_alert= LayoutInflater.from(getActivity()).inflate(R.layout.layout_view_cthd,(ConstraintLayout) view.findViewById(R.id.layoutDialogCthd));
                builder1.setView(view1_alert);

                ((TextView) view1_alert.findViewById(R.id.textTitle)).setText(getResources().getString(R.string.success_title));
                ((ImageView) view1_alert.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_success);
                ((TextView) view1_alert.findViewById(R.id.ten_kh)).setText(ten);
                ((TextView) view1_alert.findViewById(R.id.diachii)).setText("Nhận tại cửa hàng");
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
                android.app.AlertDialog alertDialog1 = builder1.create();
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