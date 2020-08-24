package com.tai06.dothetai.fdapp.FogotPassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tai06.dothetai.fdapp.R;

public class CheckCodeActivity extends AppCompatActivity {
    private EditText input_codeAccept;
    private Button btn_checkcode;
    private int codeAccept;
    private String email;
    private Toolbar toolbar_checkcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_code);
        init();
        Click_event();
    }

    private void init() {
        input_codeAccept = findViewById(R.id.input_codeAccept);
        btn_checkcode = findViewById(R.id.btn_checkcode);
        Intent intent = getIntent();
        codeAccept = intent.getIntExtra("codeAccept", 0);
        email = intent.getStringExtra("email");
        toolbar_checkcode = findViewById(R.id.toolbar_checkcode);
        setSupportActionBar(toolbar_checkcode);
        getSupportActionBar().setTitle("Kiểm tra mã xác nhận");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void Click_event() {
        btn_checkcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                String input = input_codeAccept.getText().toString().trim();
                if (input.equals("")) {
                    Toast.makeText(CheckCodeActivity.this, "Vui lòng nhập mã xác nhận", Toast.LENGTH_SHORT).show();
                } else {
                    int checkCode = Integer.parseInt(input_codeAccept.getText().toString().trim());
                    if (codeAccept == checkCode) {
                        Intent intent = new Intent(getApplicationContext(), NewPasswordActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(CheckCodeActivity.this, "Kiểm tra lại mã xác nhận", Toast.LENGTH_SHORT).show();
                    }
                }
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CheckCodeActivity.this, ForgotpswActivity.class);
        startActivity(intent);
        finish();
    }
}