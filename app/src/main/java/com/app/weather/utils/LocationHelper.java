package com.app.weather.utils;

import static android.content.Context.LOCATION_SERVICE;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.weather.Mapp;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * 定位服务
 */
public class LocationHelper {

    private LocationManager locationManager;
    private LocationListener locationListener;

    /**
     * 开始定位(只定位一次)
     */
    @SuppressLint("MissingPermission")
    public void startLocation(@Nullable OnResultListener onResultListener) {
        if (locationManager == null) {
            locationManager = (LocationManager) Mapp.getInstance().getSystemService(LOCATION_SERVICE);
        }
        stopLocation();
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            //不需要海拔信息
            criteria.setAltitudeRequired(false);
            //不需要方位信息
            criteria.setBearingRequired(false);
            //允许产生流量资费, 一般手机都是流量包月套餐
            criteria.setCostAllowed(true);
            //低功耗
            criteria.setPowerRequirement(Criteria.POWER_LOW);

            String provider = locationManager.getBestProvider(criteria, true);
            locationListener = new LocationListener() {

                @Override
                public void onLocationChanged(@NonNull Location location) {
                    stopLocation();

                    if (onResultListener != null) {
                        onResultListener.onSuccess(location);
                    }
                }

            };
            locationManager.requestLocationUpdates(provider, 0, 0, locationListener);

        } else {
            if (onResultListener != null) {
                onResultListener.onFailure("定位失败");
            }
        }
    }

    /**
     * 结束定位
     */
    public void stopLocation() {
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
            locationManager = null;
        }
    }

    /**
     * 释放资源
     */
    public void destory() {
        stopLocation();
        locationListener = null;
    }

    /**
     * 根据定位信息获取地址
     */
    @Nullable
    public static Address getAddress(@NonNull Location location) {
        Geocoder geocoder = new Geocoder(Mapp.getInstance(), Locale.CHINA);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0);
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    public interface OnResultListener {

        void onSuccess(@NonNull Location location);

        void onFailure(@NonNull String msg);

    }

}