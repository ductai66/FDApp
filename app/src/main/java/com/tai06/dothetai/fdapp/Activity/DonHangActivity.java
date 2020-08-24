package com.tai06.dothetai.fdapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;
import com.tai06.dothetai.fdapp.Adapter.ViewFragmentAdapter;
import com.tai06.dothetai.fdapp.Fragment.DonHang.OderStoreFragment;
import com.tai06.dothetai.fdapp.Fragment.DonHang.ShipClientFragment;
import com.tai06.dothetai.fdapp.Fragment.HoaDon.OderFragment;
import com.tai06.dothetai.fdapp.Fragment.HoaDon.PayHoadonFragment;
import com.tai06.dothetai.fdapp.OOP.KhachHang;
import com.tai06.dothetai.fdapp.OOP.Sanpham;
import com.tai06.dothetai.fdapp.R;

public class DonHangActivity extends AppCompatActivity {

    public static final int MSG_KhachHang = 1;

    private Toolbar toolbar_hoadon;
    private Handler handler;
    private KhachHang khachHang;
    private TabLayout mTabs;
    private View mIndicator;
    private ViewPager mViewPager;
    private ViewFragmentAdapter viewFragmentAdapter;
    private int indicatorWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_hang);
        init();
        initHandler();
        getKhachHang();
        setViewFrag();
        Click_event();
    }

    private void init() {
        //toolbar
        toolbar_hoadon = findViewById(R.id.toolbar_hoadon);
        setSupportActionBar(toolbar_hoadon);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTabs = findViewById(R.id.tab);
        mIndicator = findViewById(R.id.indicator);
        mViewPager = findViewById(R.id.viewPager);
    }

    private void setViewFrag(){
        viewFragmentAdapter = new ViewFragmentAdapter(getSupportFragmentManager());
        Bundle bundle = new Bundle();
        bundle.putSerializable("khachhang",khachHang);

        Fragment frag_pay = new ShipClientFragment();
        frag_pay.setArguments(bundle);
        viewFragmentAdapter.AddFragment(frag_pay,"Đơn hàng vận chuyển");
        Fragment frag_oder = new OderStoreFragment();
        frag_oder.setArguments(bundle);
        viewFragmentAdapter.AddFragment(frag_oder,"Nhận tại cửa hàng");

        mViewPager.setAdapter(viewFragmentAdapter);
        mTabs.setupWithViewPager(mViewPager);
    }

    private void Click_event() {
        mTabs.post(new Runnable() {
            @Override
            public void run() {
                indicatorWidth = mTabs.getWidth()/mTabs.getTabCount();
                FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();
                indicatorParams.width = indicatorWidth;
                mIndicator.setLayoutParams(indicatorParams);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mIndicator.getLayoutParams();

                //Multiply positionOffset with indicatorWidth to get translation
                float translationOffset =  (positionOffset+position) * indicatorWidth ;
                params.leftMargin = (int) translationOffset;
                mIndicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        setViewFrag();
        super.onResume();
    }
}