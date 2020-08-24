package com.tai06.dothetai.fdapp.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tai06.dothetai.fdapp.Activity.HoaDonActivity;
import com.tai06.dothetai.fdapp.Activity.MainActivity;
import com.tai06.dothetai.fdapp.FogotPassword.CheckCodeActivity;
import com.tai06.dothetai.fdapp.FogotPassword.ForgotpswActivity;
import com.tai06.dothetai.fdapp.R;
import com.tai06.dothetai.fdapp.SendCode.SendGmail;
import com.tai06.dothetai.fdapp.URL.Check;
import com.tai06.dothetai.fdapp.URL.Link;
import com.tai06.dothetai.fdapp.URL.RandomCode;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class LoginActivity extends AppCompatActivity {
    public static final int MAX = 999999;
    public static final int MIN = 100000;

    private TextInputEditText email_login, password;
    private TextView forget_pass, add_user;
    private Button login, signup, btn_login, btn_signup;
    private String email, pass;
    private LinearLayout bottom_sheet_login, bottom_sheet_signup;
    private BottomSheetBehavior bottomSheetBehavior, bottomSheetBehavior1;

    private TextInputLayout inputlayout1, inputlayout2, inputlayout5;
    private TextInputEditText email_signup, password_signup, confirm_signup, name_signup, sdt_signup;
    private TextView login_signup;
    private String emaill, passwordd, confirm, name, sdt;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Boolean keep = true;
    private Boolean log;
    private Boolean logg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        setInfoLogin();
        Click_event_login();
        Click_event_signup();
    }

    private void init() {
        bottom_sheet_login = findViewById(R.id.bottom_sheet_login);
        bottom_sheet_signup = findViewById(R.id.bottom_sheet_signup);

        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_login);
        bottomSheetBehavior1 = BottomSheetBehavior.from(bottom_sheet_signup);

        email_login = findViewById(R.id.email_login);
        password = findViewById(R.id.password);
        forget_pass = findViewById(R.id.forget_pass);
        add_user = findViewById(R.id.add_user);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);

        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);

        email_signup = findViewById(R.id.email_signup);
        password_signup = findViewById(R.id.password_signup);
        confirm_signup = findViewById(R.id.confirm_signup);
        name_signup = findViewById(R.id.name_signup);
        sdt_signup = findViewById(R.id.sdt_signup);
        login_signup = findViewById(R.id.login_signup);
        inputlayout1 = findViewById(R.id.inputlayout1);
        inputlayout2 = findViewById(R.id.inputlayout2);
        inputlayout5 = findViewById(R.id.inputlayout5);

        sp = getSharedPreferences("login", MODE_PRIVATE);
        editor = sp.edit();

        if (sp.getBoolean("log",false) == false){
            Intent i = getIntent();
            log = i.getBooleanExtra("logged", false);
            editor.putBoolean("log",log).apply();
            logg = sp.getBoolean("log",false);
        }else if (sp.getBoolean("log",false) == true){
            logg = sp.getBoolean("log",false);
        }

        if (logg == false){
            if (sp.getBoolean("logged", false)) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                String e = sp.getString("email", "");
                intent.putExtra("email", e);
                startActivity(intent);
            }
        }
    }

    private void setInfoLogin() {
        email_login.setText(sp.getString("email", ""));
        sp.getBoolean("logged",false);
    }

    private void getText() {
        emaill = email_signup.getText().toString().trim();
        passwordd = password_signup.getText().toString().trim();
        confirm = confirm_signup.getText().toString().trim();
        name = name_signup.getText().toString().trim();
        sdt = sdt_signup.getText().toString().trim();
    }

    private void Click_event_login() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                checkLogin();
            }
        });

        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotpswActivity.class);
                startActivity(intent);
            }
        });
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void checkLogin() {
        email = email_login.getText().toString().trim();
        pass = password.getText().toString().trim();
        if (email.trim().equals("") || pass.trim().equals("")) {
            Toast.makeText(this, "Vui lòng nhập thông tin!", Toast.LENGTH_SHORT).show();
        } else {
            postLogin();
        }
    }

    private void postLogin() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Link.URL_check_login, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")) {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("email", email_login.getText().toString().trim());
                            startActivity(intent);
                            editor.putString("email", email_login.getText().toString().trim()).apply();
                            editor.putBoolean("logged", true).apply();
                            editor.putBoolean("log",false).apply();
                        } else {
                            Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu sai!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                        Log.d("AAA", "Error" + error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("email", email_login.getText().toString().trim());
                        param.put("password", password.getText().toString().trim());
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }

    //regester account
    private boolean check_email() {
        String email = "Kiểm tra lại email";
        return Check.checkin(inputlayout1, email_signup, Link.PATTERN_EMAIL, email);
    }

    private boolean check_psw() {
        String psw = "Mật khẩu dài hơn 8 kí tự,gồm a-zA-Z0-9,không kí tự đặc biệt";
        return Check.checkin(inputlayout2, password_signup, Link.PATTERN_PASSWORD, psw);
    }

    private boolean check_phone() {
        String sdt = "Kiểm tra lại số điện thoại";
        return Check.checkin(inputlayout5, sdt_signup, Link.PATTERN_PHONE, sdt);
    }

    private void Click_event_signup() {
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                getText();
                if (emaill.equals("") || passwordd.equals("") || confirm.equals("") || name.equals("") || sdt.equals("")) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    if (check_email() && check_psw() && check_phone()) {
                        if (passwordd.equals(confirm)) {
                            check_Email();
                        } else {
                            Toast.makeText(LoginActivity.this, "Mật khẩu không trùng khớp!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Kiểm tra lại thông tin!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }


    private void check_Email() {
        getText();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Link.URL_check_email, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")) {
                            Toast.makeText(LoginActivity.this, "Email đã được sử dụng", Toast.LENGTH_SHORT).show();
                        } else {
                            int codeAccept = new RandomCode().randomCode(MIN, MAX);
                            new SendGmail().send("Verification Signup: " + String.valueOf(codeAccept), emaill);
                            show_dialog(codeAccept);
//                            post_SignUp();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Xảy ra lỗi!", Toast.LENGTH_SHORT).show();
                        Log.d("AAA", "Error" + error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("email", emaill);
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }

    private void show_dialog(int code) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setCancelable(false);
        View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.layout_checkcode_dialog, (ConstraintLayout) findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText(getResources().getString(R.string.codeAcept));
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_success);
        ((Button) view.findViewById(R.id.buttonNo)).setText("Hủy");
        ((Button) view.findViewById(R.id.buttonYes)).setText("Tiếp tục");
        EditText editMessage = view.findViewById(R.id.editMessage);

        AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editMessage.getText().toString();
                if (input.equals("")) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập mã xác nhận", Toast.LENGTH_SHORT).show();
                } else {
                    int checkCode = Integer.parseInt(editMessage.getText().toString());
                    if (code == checkCode) {
                        alertDialog.dismiss();
                        post_SignUp();
                    } else {
                        Toast.makeText(LoginActivity.this, "Kiểm tra lại mã xác nhận", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }

    private void post_SignUp() {
        getText();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Link.URL_postInsertKH, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")) {
                            Toast.makeText(LoginActivity.this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Xảy ra lỗi!", Toast.LENGTH_SHORT).show();
                        Log.d("AAA", "Lỗi " + error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("email", emaill);
                        param.put("password", passwordd);
                        param.put("ten_kh", name);
                        param.put("sdt", sdt);
                        param.put("image", "");
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED && bottomSheetBehavior1.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            super.onBackPressed();
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startActivity(startMain);
            finish();
        } else {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED || bottomSheetBehavior1.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }

    }

}