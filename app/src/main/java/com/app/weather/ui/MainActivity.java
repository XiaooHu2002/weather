package com.app.weather.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.weather.data.WeatherDataSource;
import com.app.weather.dialog.LocatingDialog;
import com.app.weather.utils.LocationHelper;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.QWeather;
import com.app.weather.Mapp;
import com.app.weather.R;
import com.app.weather.bean.City;
import com.app.weather.data.CityDataSource;
import com.app.weather.utils.SizeUtils;
import com.app.weather.utils.ToastUtils;
import com.app.weather.utils.WeatherUtils;

import java.util.List;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_CHOOSE_CITY = 1;

    private LinearLayout positionLinearLayout;//位置布局控件
    private TextView positionTextView;//位置文本控件
    private TextView futureTextView;//未来天气按钮控件
    private LinearLayout contentLinearLayout;//内容布局控件
    private ImageView weatherImageView;//天气图片控件
    private TextView weatherTextView;//天气文本控件
    private TextView temperatureTextView;//温度文本控件
    private TextView windDirTextView;//风向文本控件
    private TextView windSpeedTextView;//风速文本控件
    private TextView humidityTextView;//湿度文本控件
    private TextView precipTextView;//降水量文本控件

    private LocatingDialog locatingDialog;//定位加载对话框

    private CityDataSource cityDataSource;//城市数据源
    private WeatherDataSource weatherDataSource;//天气数据源

    private LocationHelper locationHelper;//定位助手

    private City locationCity;//定位的城市
    private City lastCity;//最近一次操作的城市

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        setView();
        getWeatherNow();
    }

    /**
     * 后一个页面回传的处理
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case REQUEST_CHOOSE_CITY: {
                if (resultCode == RESULT_OK) {
                    String locationId = data.getStringExtra("city");

                    cityDataSource.saveLastCity(locationId);
                    City city = cityDataSource.selectOne(locationId);

                    //刷新数据
                    notifyCityWeather(city);

                } else if (resultCode == RESULT_CANCELED) {
                    getWeatherNow();
                }
            }
            break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLocatingDialog();
        if (locationHelper != null) {
            locationHelper.destory();
            locationHelper = null;
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        cityDataSource = Mapp.getInstance().getCityDataSource();
        weatherDataSource = Mapp.getInstance().getWeatherDataSource();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        positionLinearLayout = findViewById(R.id.position_linearLayout);
        positionTextView = findViewById(R.id.position_textView);
        futureTextView = findViewById(R.id.future_textView);
        contentLinearLayout = findViewById(R.id.content_linearLayout);
        weatherImageView = findViewById(R.id.weather_imageView);
        weatherTextView = findViewById(R.id.weather_textView);
        temperatureTextView = findViewById(R.id.temperature_textView);
        windDirTextView = findViewById(R.id.windDir_textView);
        windSpeedTextView = findViewById(R.id.windSpeed_textView);
        humidityTextView = findViewById(R.id.humidity_textView);
        precipTextView = findViewById(R.id.precip_textView);
    }

    /**
     * 设置控件
     */
    private void setView() {
        positionLinearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = CityCollectionActivity.buildIntent(MainActivity.this);
                startActivityForResult(intent, REQUEST_CHOOSE_CITY);
            }

        });
    }

    /**
     * 根据城市，刷新控件和天气数据
     */
    private void notifyCityWeather(@NonNull City city) {
        //如果最近操作的城市和当前传入的城市市同一个，则不做操作，减少天气数据的请求次数
        if (city.equals(lastCity)) {
            return;
        }

        lastCity = city;
        setCityView(city);
        getWeatherNow(city);
    }

    /**
     * 设置城市相关控件
     */
    private void setCityView(@NonNull City city) {
        positionTextView.setText(city.getLocationNameZH());

        futureTextView.setVisibility(View.VISIBLE);
        futureTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = WeatherFutureActivity.buildIntent(MainActivity.this, city.getLocationID());
                startActivity(intent);
            }

        });
    }

    /**
     * 检查定位权限是否授权，已授权则开启定位，未授权则使用默认城市
     */
    private void checkPermissionToLocation() {
        XXPermissions.with(this).permission(Permission.ACCESS_COARSE_LOCATION).permission(Permission.ACCESS_FINE_LOCATION).request(new OnPermissionCallback() {

            @Override
            public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                //如果用户选中模糊定位，allGranted是false，但是也应该去定位，所以不需要判断allGranted
                startLocation();
            }

            @Override
            public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
                OnPermissionCallback.super.onDenied(permissions, doNotAskAgain);
                getDefaultCityWeatherNow();
            }

        });
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        showLocatingDialog();
        if (locationHelper == null) {
            locationHelper = new LocationHelper();
        }
        locationHelper.startLocation(new LocationHelper.OnResultListener() {

            @Override
            public void onSuccess(@NonNull Location location) {
                dismissLocatingDialog();
                Address address = LocationHelper.getAddress(location);
                if (address == null) {
                    ToastUtils.showShort("未找到定位地址信息");
                    getDefaultCityWeatherNow();
                } else {
                    City city = cityDataSource.selectOneByNameZH(address.getAdminArea(), address.getLocality(), address.getSubLocality());
                    if (city == null) {
                        ToastUtils.showShort("未找到有效的定位地址信息");
                        getDefaultCityWeatherNow();
                    } else {
                        locationCity = city;
                        notifyCityWeather(city);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull String msg) {
                dismissLocatingDialog();
                ToastUtils.showShort(msg);
                getDefaultCityWeatherNow();
            }

        });
    }

    /**
     * 获取默认城市并获取天气数据，将该城市作为定位城市，防止未找到收藏城市时重新定位
     */
    private void getDefaultCityWeatherNow() {
        City city = cityDataSource.selectList(null).get(0);
        locationCity = city;
        notifyCityWeather(city);
    }

    /**
     * 获取实时天气
     */
    private void getWeatherNow() {
        //获取最近操作的城市
        String locationId = cityDataSource.getLastCity();
        //检查该城市是否在收藏夹中,没有就获取收藏列表的第一个数据
        if (!TextUtils.isEmpty(locationId)) {
            List<String> collections = cityDataSource.selectCollections();
            if (!collections.contains(locationId)) {
                locationId = collections.size() > 0 ? collections.get(0) : null;
            }
        }
        //根据id获取城市对象
        City city = TextUtils.isEmpty(locationId) ? null : cityDataSource.selectOne(locationId);
        //如果收藏的城市不存在，则使用定位的城市，如果没有定位的城市，则开启定位
        if (city == null && locationCity != null) {
            city = locationCity;
        }

        if (city == null) {
            checkPermissionToLocation();
        } else {
            notifyCityWeather(city);
        }
    }

    /**
     * 获取实时天气
     */
    private void getWeatherNow(@NonNull City city) {
        showLoadingDialog();
        weatherDataSource.getWeatherNow(this, city.getLocationID(), new QWeather.OnResultWeatherNowListener() {

            @Override
            public void onSuccess(WeatherNowBean weatherBean) {
                contentLinearLayout.setVisibility(View.VISIBLE);
                dismissLoadingDialog();
                if (weatherBean.getCode() == Code.OK) {
                    WeatherNowBean.NowBaseBean nowBaseBean = weatherBean.getNow();

                    //设置天气动画图标
                    int icon = WeatherUtils.getIcon(nowBaseBean.getIcon());
                    weatherImageView.setImageResource(icon);

                    //设置天气名称
                    weatherTextView.setText(nowBaseBean.getText());

                    //设置温度
                    SpannableStringBuilder temperatureBuilder = new SpannableStringBuilder(nowBaseBean.getTemp() + "°C");
                    temperatureBuilder.setSpan(new AbsoluteSizeSpan(SizeUtils.sp2px(30)), nowBaseBean.getTemp().length(), temperatureBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    temperatureTextView.setText(temperatureBuilder);

                    //设置风向
                    windDirTextView.setText(nowBaseBean.getWindDir());

                    //设置风速
                    windSpeedTextView.setText(nowBaseBean.getWindSpeed() + "km/h");

                    //设置相对湿度
                    humidityTextView.setText(nowBaseBean.getHumidity() + "%");

                    //设置降水量
                    precipTextView.setText(nowBaseBean.getPrecip() + "mm");

                } else {
                    ToastUtils.showShort(weatherBean.getCode().getTxt());
                }
            }

            @Override
            public void onError(Throwable e) {
                contentLinearLayout.setVisibility(View.INVISIBLE);
                dismissLoadingDialog();
                ToastUtils.showShort(e.getMessage());
            }

        });
    }

    /**
     * 显示定位加载对话框
     */
    private void showLocatingDialog() {
        if (locatingDialog == null) {
            locatingDialog = new LocatingDialog(this);
            locatingDialog.setCancelable(false);
            locatingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    if (locationHelper != null) {
                        locationHelper.stopLocation();
                    }
                }

            });
        }
        if (!locatingDialog.isShowing()) {
            locatingDialog.show();
        }
    }

    /**
     * 关闭定位加载对话框
     */
    private void dismissLocatingDialog() {
        if (locatingDialog != null && locatingDialog.isShowing()) {
            locatingDialog.dismiss();
        }
    }

}