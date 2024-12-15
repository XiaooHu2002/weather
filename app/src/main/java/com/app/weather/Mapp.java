package com.app.weather;

import android.app.Application;

import com.app.weather.data.WeatherDataSource;
import com.app.weather.data.impl.WeatherDataSourceImpl;
import com.qweather.sdk.view.HeConfig;
import com.app.weather.data.CityDataSource;
import com.app.weather.data.impl.CityDataSourceImpl;

public class Mapp extends Application {

    private static Mapp instance;//单例

    private CityDataSource cityDataSource;//城市数据源
    private WeatherDataSource weatherDataSource;//天气数据源

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initHeWeather();
    }

    /**
     * 获取实例
     */
    public static final Mapp getInstance() {
        return instance;
    }

    /**
     * 初始化和风天气
     */
    private void initHeWeather() {
        //配置appkey
        HeConfig.init("KK58VVT38T", "fe0c6f522b574c6985fca71d76bb3924");
        //切换至开发版服务
        HeConfig.switchToDevService();
    }

    /**
     * 懒加载获取城市数据源
     */
    public CityDataSource getCityDataSource() {
        if (cityDataSource == null) {
            cityDataSource = new CityDataSourceImpl();
        }
        return cityDataSource;
    }

    /**
     * 懒加载获取天气数据源
     */
    public WeatherDataSource getWeatherDataSource() {
        if (weatherDataSource == null) {
            weatherDataSource = new WeatherDataSourceImpl();
        }
        return weatherDataSource;
    }

}