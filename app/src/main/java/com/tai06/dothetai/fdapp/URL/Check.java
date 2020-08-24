package com.tai06.dothetai.fdapp.URL;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Check {
    public static boolean checkin(TextInputLayout layout, TextInputEditText editText, Pattern pattern, String error){
        String str = editText.getText().toString().trim();
        Matcher matcher = pattern.matcher(str); // PATTERN_EMAIL là biểu thức chính quy
        if (!matcher.matches()){ // nếu không đúng biểu thức chính quy
            layout.setErrorEnabled(true);
            layout.setError(error);
            return false;
        }else{
            layout.setErrorEnabled(false);
            return true;
        }
    }
}
