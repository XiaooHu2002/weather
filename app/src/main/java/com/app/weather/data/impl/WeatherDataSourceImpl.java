package com.app.weather.data.impl;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.weather.data.WeatherDataSource;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.QWeather;

/**
 * 天气数据源的实现类
 */
public class WeatherDataSourceImpl implements WeatherDataSource {

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void getWeatherNow(@NonNull Context context, @NonNull String locationId, @Nullable QWeather.OnResultWeatherNowListener listener) {
        QWeather.getWeatherNow(context, locationId, Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherNowListener() {

            @Override
            public void onError(Throwable throwable) {
                if (listener != null) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            listener.onError(throwable);
                        }

                    });
                }
            }

            @Override
            public void onSuccess(WeatherNowBean weatherNowBean) {
                if (listener != null) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            listener.onSuccess(weatherNowBean);
                        }

                    });
                }
            }

        });
    }

    @Override
    public void getWeatherFuture(@NonNull Context context, @NonNull String locationId, @Nullable QWeather.OnResultWeatherDailyListener listener) {
        QWeather.getWeather7D(context, locationId, Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherDailyListener() {

            @Override
            public void onSuccess(WeatherDailyBean weatherDailyBean) {
                if (listener != null) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            listener.onSuccess(weatherDailyBean);
                        }

                    });
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (listener != null) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            listener.onError(throwable);
                        }

                    });
                }
            }

        });
    }

}
