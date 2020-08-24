package com.tai06.dothetai.fdapp.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.tai06.dothetai.fdapp.R;
import com.tai06.dothetai.fdapp.URL.Check;
import com.tai06.dothetai.fdapp.URL.Link;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class ChangepswActivity extends AppCompatActivity {

    private TextInputLayout layout2_changepsw;
    private TextInputEditText pswold_changepsw,password_changepsw,confirm_changepsw;
    private Toolbar toolbar_changepsw;
    private Button btn_changepsw;
    private String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepsw);
        init();
        Click_btn();
    }

    private void init(){
        toolbar_changepsw = findViewById(R.id.toolbar_changepsw);
        setSupportActionBar(toolbar_changepsw);
        getSupportActionBar().setTitle("Đổi mật khẩu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layout2_changepsw = findViewById(R.id.layout2_changepsw);
        pswold_changepsw = findViewById(R.id.pswold_changepsw);
        password_changepsw = findViewById(R.id.password_changepsw);
        confirm_changepsw = findViewById(R.id.confirm_changepsw);
        btn_changepsw = findViewById(R.id.btn_changepsw);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
    }

    private void Click_btn(){
        btn_changepsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                edit_text();
            }
        });
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view!=null){
            InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    private void edit_text(){
        String mail = pswold_changepsw.getText().toString().trim();
        String psw = password_changepsw.getText().toString().trim();
        String confirm = confirm_changepsw.getText().toString().trim();
        if (mail.equals("")||psw.equals("")||confirm.equals("")){
            Toast.makeText(this, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
        }else{
            check_passold();
        }
    }
    private boolean check_pswnewold(){
        String oldpsw = pswold_changepsw.getText().toString().trim();
        String newpsw = password_changepsw.getText().toString().trim();
        if(newpsw.trim().equals(oldpsw)){
            layout2_changepsw.setErrorEnabled(true);
            layout2_changepsw.setError("Mật khẩu mới trùng mật khẩu cũ");
            return false;
        }else{
            layout2_changepsw.setErrorEnabled(false);
            return true;
        }
    }

    private boolean check_passconfirm(){
        String psw = password_changepsw.getText().toString().trim();
        String cfm = confirm_changepsw.getText().toString().trim();
        if (psw.trim().equals(cfm)){
            return true;
        }else{
            Toast.makeText(this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean check_pass(){
        String psw = "Mật khẩu dài hơn 8 kí tự,gồm a-zA-Z0-9,không kí tự đặc biệt";
        return Check.checkin(layout2_changepsw,password_changepsw,Link.PATTERN_PASSWORD,psw);
    }

    private void check_passold(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Link.URL_check_login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")){
                    if(check_pass() && check_pswnewold() && check_passconfirm()){
                        update_pass();
                    }else{
                        Toast.makeText(ChangepswActivity.this, "Kiểm tra lại thông tin", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ChangepswActivity.this, "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("email",email);
                param.put("password",pswold_changepsw.getText().toString().trim());
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void update_pass(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Link.URL_postUpdateKH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")){
                    Toast.makeText(ChangepswActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChangepswActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(ChangepswActivity.this, "Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("email",email);
                param.put("password",password_changepsw.getText().toString());
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}