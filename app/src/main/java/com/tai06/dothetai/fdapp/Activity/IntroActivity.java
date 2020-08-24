package com.tai06.dothetai.fdapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.tai06.dothetai.fdapp.Adapter.SlideIntroAdapter;
import com.tai06.dothetai.fdapp.LoginSignup.LoginActivity;
import com.tai06.dothetai.fdapp.OOP.SlideIntro;
import com.tai06.dothetai.fdapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class IntroActivity extends AppCompatActivity {

    private ViewPager view_pager;
    private TabLayout tab_layout;
    private Button start_intro;
    private Animation btn_animation;
    private List<SlideIntro> arrSildeIntro;
    private SlideIntroAdapter slideIntroAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        init();
        setSlideIntro();
        Click_event();
    }

    private void init(){
        view_pager = findViewById(R.id.view_pager);
        tab_layout = findViewById(R.id.tab_layout);
        start_intro = findViewById(R.id.start_intro);
        btn_animation = AnimationUtils.loadAnimation(this,R.anim.button_animation);
    }

    private void Click_event(){
        start_intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setSlideIntro(){
        arrSildeIntro = new ArrayList<>();
        arrSildeIntro.add(new SlideIntro(R.drawable.img1,"Fresh Food","Món ăn ngon đảm bảo thơm ngon nóng giòn"));
        arrSildeIntro.add(new SlideIntro(R.drawable.img2,"Fast Deliverty","Đồ uống tươi mát dễ uống"));
        arrSildeIntro.add(new SlideIntro(R.drawable.img3,"Easy Payment","Thanh toán dễ dàng, giao hàng nhanh chóng"));

        slideIntroAdapter = new SlideIntroAdapter(arrSildeIntro,this);
        view_pager.setAdapter(slideIntroAdapter);
        tab_layout.setupWithViewPager(view_pager,true);
//        tabIndicatorHeight="0dp" bỏ gạch chân dots
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new IntroActivity.RuntimeIntro(),1500,3000);
    }

    public class RuntimeIntro extends TimerTask {
        @Override
        public void run() {
            if(IntroActivity.this != null){
                IntroActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (view_pager.getCurrentItem()<arrSildeIntro.size()-1){
                            view_pager.setCurrentItem(view_pager.getCurrentItem() + 1);
                        }else if (view_pager.getCurrentItem() == arrSildeIntro.size()-1){
                            start_intro.setVisibility(View.VISIBLE);
                            start_intro.setAnimation(btn_animation);
                        }
                    }
                });
            }
        }
    }
}