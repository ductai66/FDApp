package com.tai06.dothetai.fdapp.FogotPassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.tai06.dothetai.fdapp.Activity.MainActivity;
import com.tai06.dothetai.fdapp.LoginSignup.LoginActivity;
import com.tai06.dothetai.fdapp.R;
import com.tai06.dothetai.fdapp.URL.Check;
import com.tai06.dothetai.fdapp.URL.Link;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class NewPasswordActivity extends AppCompatActivity {
    private TextInputLayout layout1_newpsw,layout2_newpsw;
    private TextInputEditText email_newpsw,password_newpsw,confirm_newpsw;
    private Button btn_newpsw;
    private String email;
    private Toolbar toolbar_inputnewpsw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        init();
        Click_event();
    }

    private void init(){
        layout1_newpsw = findViewById(R.id.layout1_newpsw);
        layout2_newpsw = findViewById(R.id.layout2_newpsw);
        email_newpsw = findViewById(R.id.email_newpsw);
        password_newpsw = findViewById(R.id.password_newpsw);
        confirm_newpsw = findViewById(R.id.confirm_newpsw);
        btn_newpsw = findViewById(R.id.btn_newpsw);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        email_newpsw.setText(email);
        toolbar_inputnewpsw = findViewById(R.id.toolbar_inputnewpsw);
        setSupportActionBar(toolbar_inputnewpsw);
        getSupportActionBar().setTitle("Tạo mật khẩu mới");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void Click_event(){
        btn_newpsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                check_edittext();
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

    private void check_edittext(){
        String psw = password_newpsw.getText().toString().trim();
        String confirm = confirm_newpsw.getText().toString().trim();
        if (psw.equals("") || confirm.equals("")){
            Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
        }else{
            if (check_password()){
                if (psw.trim().equals(confirm)){
                    UpdatePassword();
                }else{
                    Toast.makeText(this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Kiểm tra lại", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean check_password(){
        String psw = "Mật khẩu dài hơn 8 kí tự,gồm a-zA-Z0-9,không kí tự đặc biệt";
        return Check.checkin(layout2_newpsw,password_newpsw,Link.PATTERN_PASSWORD,psw);
    }

    private void UpdatePassword(){
        String pass = password_newpsw.getText().toString().trim();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(NewPasswordActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Link.URL_postUpdateKH, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")){
                            Toast.makeText(NewPasswordActivity.this, "Tạo mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(NewPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(NewPasswordActivity.this, "Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NewPasswordActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                        Log.d("AAA","Error" + error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("email",email);
                        param.put("password",pass);
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
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
    public void onBackPressed() {
        Intent intent = new Intent(NewPasswordActivity.this,CheckCodeActivity.class);
        startActivity(intent);
        finish();
    }
}