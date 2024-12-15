package com.app.weather.utils;

import android.text.TextUtils;
import android.widget.Toast;

import com.app.weather.Mapp;

/**
 * 吐司工具
 */
public class ToastUtils {

    /**
     * 短时间吐司
     */
    public static void showShort(String text) {
        if (!TextUtils.isEmpty(text)) {
            Toast.makeText(Mapp.getInstance(), text, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 短时间吐司
     */
    public static void showShort(int resId) {
        Toast.makeText(Mapp.getInstance(), resId, Toast.LENGTH_SHORT).show();
    }

}