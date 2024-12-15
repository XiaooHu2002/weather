package com.app.weather.ui;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.weather.dialog.LoadingDialog;

/**
 * 页面的基础类
 */
public class BaseActivity extends AppCompatActivity {

    private LoadingDialog loadingDialog;//加载对话框

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在页面关闭的时候，需要关闭对话框
        dismissLoadingDialog();
    }

    /**
     * 设置菜单点击
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //设置标题栏的返回键点击事件
        if (item.getItemId() == android.R.id.home) {
            //页面返回
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 显示加载对话框
     */
    protected void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
            loadingDialog.setCancelable(false);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    /**
     * 关闭加载对话框
     */
    protected void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

}
