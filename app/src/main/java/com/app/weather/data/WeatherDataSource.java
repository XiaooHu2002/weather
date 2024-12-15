package com.app.weather.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qweather.sdk.view.QWeather;

/**
 * 天气数据源
 */
public interface WeatherDataSource {

    /**
     * 获取当前天气
     */
    void getWeatherNow(@NonNull Context context, @NonNull String locationId, @Nullable QWeather.OnResultWeatherNowListener listener);

    /**
     * 获取未来天气
     */
    void getWeatherFuture(@NonNull Context context, @NonNull String locationId, @Nullable QWeather.OnResultWeatherDailyListener listener);

}