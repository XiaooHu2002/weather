package com.app.weather.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.weather.Mapp;
import com.app.weather.adapter.WeatherFutureRecyclerAdapter;
import com.app.weather.data.WeatherDataSource;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.view.QWeather;
import com.app.weather.R;
import com.app.weather.utils.SizeUtils;
import com.app.weather.utils.ToastUtils;

import java.util.List;

public class WeatherFutureActivity extends BaseActivity {

    private static final String LOCATION_ID = "locationId";

    private Toolbar toolbar;//标题栏控件
    private RecyclerView recyclerView;//列表控件

    private WeatherFutureRecyclerAdapter recyclerAdapter;//未来天气的适配器

    private WeatherDataSource weatherDataSource;//天气数据源
    private String locationId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_future);
        initData();
        initView();
        setView();
        getWeatherFuture();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        locationId = getIntent().getStringExtra(LOCATION_ID);

        weatherDataSource = Mapp.getInstance().getWeatherDataSource();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
    }

    /**
     * 设置控件
     */
    private void setView() {
        //将标题栏关联到页面
        setSupportActionBar(toolbar);
        //显示返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //设置列表的布局样式
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //设置列表的间隔距离
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int count = parent.getAdapter().getItemCount();
                int index = parent.getChildAdapterPosition(view);
                if (index < count - 1) {
                    outRect.set(0, 0, 0, SizeUtils.dp2px(20));
                }
            }

        });
        //设置列表的适配器
        recyclerAdapter = new WeatherFutureRecyclerAdapter();
        recyclerView.setAdapter(recyclerAdapter);
    }

    /**
     * 获取未来天气预报数据
     */
    private void getWeatherFuture() {
        showLoadingDialog();
        weatherDataSource.getWeatherFuture(this, locationId, new QWeather.OnResultWeatherDailyListener() {

            @Override
            public void onSuccess(WeatherDailyBean weatherDailyBean) {
                dismissLoadingDialog();
                if (weatherDailyBean.getCode() == Code.OK) {
                    List<WeatherDailyBean.DailyBean> dailyBeans = weatherDailyBean.getDaily();
                    recyclerAdapter.setNewData(dailyBeans);

                } else {
                    ToastUtils.showShort(weatherDailyBean.getCode().getTxt());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dismissLoadingDialog();
                ToastUtils.showShort(throwable.getMessage());
            }

        });
    }

    /**
     * 构建意图
     */
    public static Intent buildIntent(@NonNull Context context, @NonNull String locationId) {
        Intent intent = new Intent(context, WeatherFutureActivity.class);
        intent.putExtra(LOCATION_ID, locationId);
        return intent;
    }

}