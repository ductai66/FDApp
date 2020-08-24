package com.tai06.dothetai.fdapp.LoginSignup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tai06.dothetai.fdapp.R;

public class BottomSheetSignup extends BottomSheetDialogFragment {

    private View view;

    public BottomSheetSignup(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bottom_sheet_signup, container, false);
        return view;
    }
}
