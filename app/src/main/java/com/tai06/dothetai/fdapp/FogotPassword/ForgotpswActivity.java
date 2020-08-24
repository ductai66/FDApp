package com.tai06.dothetai.fdapp.FogotPassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.tai06.dothetai.fdapp.LoginSignup.LoginActivity;
import com.tai06.dothetai.fdapp.R;
import com.tai06.dothetai.fdapp.SendCode.SendGmail;
import com.tai06.dothetai.fdapp.URL.Check;
import com.tai06.dothetai.fdapp.URL.Link;
import com.tai06.dothetai.fdapp.URL.RandomCode;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class ForgotpswActivity extends AppCompatActivity {

    public static final int MAX = 999999;
    public static final int MIN = 100000;

    private TextInputLayout inputlayout_forgetpsw;
    private TextInputEditText email_forgetpsw;
    private Button btn_forgetpsw;
    private ImageButton close_forgetpsw;
    private Toolbar toolbar_forgetpsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpsw);
        init();
        Click_event();
    }

    private void init() {
        inputlayout_forgetpsw = findViewById(R.id.inputlayout_forgetpsw);
        email_forgetpsw = findViewById(R.id.email_forgetpsw);
        btn_forgetpsw = findViewById(R.id.btn_forgetpsw);
        close_forgetpsw = findViewById(R.id.close_forgetpsw);
        toolbar_forgetpsw = findViewById(R.id.toolbar_forgetpsw);
        setSupportActionBar(toolbar_forgetpsw);
        getSupportActionBar().setTitle("Gửi mã xác nhận");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void Click_event() {
        email_forgetpsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    close_forgetpsw.setVisibility(View.VISIBLE);
                } else {
                    close_forgetpsw.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        close_forgetpsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_forgetpsw.getText().clear();
            }
        });
        btn_forgetpsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                button();
            }
        });
    }

    private boolean check_email(){
        String email = "Kiểm tra lại email";
        return Check.checkin(inputlayout_forgetpsw,email_forgetpsw,Link.PATTERN_EMAIL,email);
    }

    private void button() {
        String email = email_forgetpsw.getText().toString().trim();
        if (email.equals("")) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
        } else {
            if (check_email()) {
                checkgmail();
            } else {
                Toast.makeText(this, "Email không đúng", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void checkgmail() {
        String email = email_forgetpsw.getText().toString().trim();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(ForgotpswActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Link.URL_check_email, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")) {
                            int codeAccept = new RandomCode().randomCode(MIN, MAX);
                            new SendGmail().send("Verification Account: " + String.valueOf(codeAccept), email);
                            Intent intent = new Intent(ForgotpswActivity.this, CheckCodeActivity.class);
                            intent.putExtra("codeAccept", codeAccept);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ForgotpswActivity.this, "Email này không tồn tại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ForgotpswActivity.this, "Xảy ra lỗi!", Toast.LENGTH_SHORT).show();
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ForgotpswActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}