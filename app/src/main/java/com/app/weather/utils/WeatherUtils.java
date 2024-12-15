package com.app.weather.utils;

import androidx.annotation.NonNull;

import com.app.weather.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 天气工具
 */
public class WeatherUtils {

    private static Map<String, Integer> iconMap;//天气图标字典

    /**
     * 懒加载获取天气图标字典
     */
    public static Map<String, Integer> getIconMap() {
        if (iconMap == null) {
            iconMap = new HashMap<>();
            //晴
            iconMap.put("100", R.drawable.ic_sunny);
            iconMap.put("150", R.drawable.ic_sunny);
            //多云
            iconMap.put("101", R.drawable.ic_cloudy);
            iconMap.put("151", R.drawable.ic_cloudy);
            //阴
            iconMap.put("104", R.drawable.ic_overcast);
            //严寒
            iconMap.put("901", R.drawable.ic_frost);
            iconMap.put("1016", R.drawable.ic_frost);
            //冰雹
            iconMap.put("1015", R.drawable.ic_hail);
            //阵雨
            iconMap.put("300", R.drawable.ic_the_rain_turned_fine);
            iconMap.put("350", R.drawable.ic_the_rain_turned_fine);
            //小雨
            iconMap.put("305", R.drawable.ic_light_rain);
            iconMap.put("308", R.drawable.ic_light_rain);
            iconMap.put("309", R.drawable.ic_light_rain);
            iconMap.put("399", R.drawable.ic_light_rain);
            //中雨
            iconMap.put("306", R.drawable.ic_moderate_rain);
            //大雨
            iconMap.put("307", R.drawable.ic_heavy_rain);
            //暴雨
            iconMap.put("310", R.drawable.ic_rainstorm);
            iconMap.put("311", R.drawable.ic_rainstorm);
            //大暴雨
            iconMap.put("312", R.drawable.ic_thunderstorm);
            iconMap.put("1003", R.drawable.ic_thunderstorm);
            iconMap.put("1043", R.drawable.ic_thunderstorm);
            //小雪
            iconMap.put("400", R.drawable.ic_light_snow);
            //中雪
            iconMap.put("401", R.drawable.ic_moderate_snow);
            //大雪
            iconMap.put("402", R.drawable.ic_heavy_snow);
            iconMap.put("403", R.drawable.ic_heavy_snow);
            //雨夹雪
            iconMap.put("404", R.drawable.ic_sleet);
            iconMap.put("405", R.drawable.ic_sleet);
            //阵雪
            iconMap.put("407", R.drawable.ic_light_snow_to_clear_up);
            //雾霾
            iconMap.put("500", R.drawable.ic_smog);
            iconMap.put("501", R.drawable.ic_smog);
            iconMap.put("502", R.drawable.ic_smog);
            //扬沙
            iconMap.put("503", R.drawable.ic_sand_dust);
            iconMap.put("504", R.drawable.ic_sand_dust);
            iconMap.put("507", R.drawable.ic_sand_dust);
            iconMap.put("1051", R.drawable.ic_sand_dust);
        }
        return iconMap;
    }

    /**
     * 根据天气代码获取天气图标
     */
    public static int getIcon(@NonNull String code) {
        Integer icon = getIconMap().get(code);
        return icon == null ? R.drawable.ic_weather_unkown : icon.intValue();
    }

}