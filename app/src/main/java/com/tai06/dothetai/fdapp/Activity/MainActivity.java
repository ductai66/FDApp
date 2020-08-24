package com.tai06.dothetai.fdapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.tai06.dothetai.fdapp.AdminActivity.InsertSanphamActivity;
import com.tai06.dothetai.fdapp.AdminActivity.UpdateSanPhamActivity;
import com.tai06.dothetai.fdapp.Fragment.Trangchu.TrangchuFragment;
import com.tai06.dothetai.fdapp.LoginSignup.ChangepswActivity;
import com.tai06.dothetai.fdapp.LoginSignup.LoginActivity;
import com.tai06.dothetai.fdapp.OOP.KhachHang;
import com.tai06.dothetai.fdapp.R;
import com.tai06.dothetai.fdapp.URL.Link;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int MSG_HEADER_VIEW = 1;

    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    private boolean back_home;
    private Handler handler;
    private KhachHang khachHang;
    private TextView ten_kh, email_kh;
    private ImageView img_kh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initHandler();
        getInfoKH();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.draw_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MSG_HEADER_VIEW:
                        khachHang = (KhachHang) msg.obj;
                        headView(khachHang);
                        setInfo(khachHang);
                        break;
                }
            }
        };
    }

    private void setInfo(KhachHang khachHang) {
        Fragment fragment = new TrangchuFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("khachhang", khachHang);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
        navigationView.setCheckedItem(R.id.menu_home);

        //set visiable quản lí của admin
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.manager);
        if (khachHang.getEmail().equals("a")) {
            menuItem.setVisible(true);
        } else {
            menuItem.setVisible(false);
        }
    }

    //Phần setup Navigation

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displayView(item.getItemId());
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayView(int id) {
        switch (id) {
            case R.id.menu_home:
                showFragment(new TrangchuFragment());
                back_home = true;
                break;
            case R.id.menu_donhang:
//                showFragment(new DonhangFragment());
                Intent intent = new Intent(this,DonHangActivity.class);
                intent.putExtra("khachhang",khachHang);
                startActivity(intent);
                back_home = false;
                break;
            case R.id.menu_doimatkhau:
                Intent intent1 = new Intent(MainActivity.this, ChangepswActivity.class);
                intent1.putExtra("ma_kh", String.valueOf(khachHang.getMa_kh()));
                intent1.putExtra("email", khachHang.getEmail());
                intent1.putExtra("password", khachHang.getPassword());
                startActivity(intent1);
                break;
            case R.id.menu_dangxuat:
//                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                i.putExtra("logged",true);
                startActivity(i);
                break;
            case R.id.insert_sanpham:
                startActivity(new Intent(MainActivity.this, InsertSanphamActivity.class));
                break;
            case R.id.update_sanpham:
                startActivity(new Intent(MainActivity.this, UpdateSanPhamActivity.class));
                break;
        }
    }

    private void showFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("khachhang",khachHang);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment).commit();
    }

    //phần setup menu search

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_bar:
                Intent intent = new Intent(MainActivity.this, SearchViewActivity.class);
                intent.putExtra("khachhang", khachHang);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //phần set thông tin headview

    private void headView(KhachHang khachHang) {
        View view = navigationView.getHeaderView(0);
        img_kh = view.findViewById(R.id.img_kh);
        email_kh = view.findViewById(R.id.email_kh);
        ten_kh = view.findViewById(R.id.ten_kh);
        Picasso.get().load(khachHang.getImage()).into(img_kh);
        email_kh.setText(khachHang.getEmail());
        ten_kh.setText(khachHang.getTen_kh());

        img_kh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,InfoKhachhangActivity.class);
                intent.putExtra("khachhang",khachHang);
                startActivity(intent);
            }
        });
    }

    //Phần get thông tin khách hàng từ email đăng nhập

    private void getInfoKH() {
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Link.URL_getInfoKH, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            KhachHang kh = new KhachHang();
                            JSONObject jsonObject = new JSONObject(response);
                            kh.setMa_kh(jsonObject.getInt("ma_kh"));
                            kh.setEmail(jsonObject.getString("email"));
                            kh.setPassword(jsonObject.getString("password"));
                            kh.setTen_kh(jsonObject.getString("ten_kh"));
                            kh.setSdt(jsonObject.getString("sdt"));
                            kh.setImage(jsonObject.getString("image"));
                            Message msg = new Message();
                            msg.what = MSG_HEADER_VIEW;
                            msg.obj = kh;
                            handler.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                        Log.d("AAA", "Error" + error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("email", email);
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }

    @Override
    protected void onResume() {
        getInfoKH();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            if (!back_home) { // nếu view hiện tại không phải là homefragment
                displayView(R.id.menu_home); // sẽ hiển thị view home fragment
            } else {
                moveTaskToBack(true); // nếu là view home fragment ,sẽ thoát khỏi app
//            super.onBackPressed();
            }
        }
    }
}