package com.app.weather.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.weather.R;

public class LocatingDialog extends Dialog {

    private TextView cancelTextView;


    public LocatingDialog(@NonNull Context context) {
        super(context, R.style.MaskDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_locating);
        initView();
        setView();
    }

    private void initView() {
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);

        cancelTextView = findViewById(R.id.cancel_textView);
    }

    private void setView() {
        cancelTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
            }

        });
    }

}