package com.app.weather.data.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.weather.Mapp;
import com.app.weather.bean.City;
import com.app.weather.data.CityDataSource;
import com.app.weather.utils.GsonUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 城市数据源的实现类
 */
public class CityDataSourceImpl implements CityDataSource {

    private static final String LOCATION_ID_COLLECTIONS = "location_id_collections";
    private static final String LOCATION_ID_LAST = "location_id_last";

    private List<City> cities;//城市列表
    private Map<String, City> cityCache;//城市列表的字典，作为缓存

    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public City selectOne(@NonNull String locationId) {
        Map<String, City> map = getCache();
        return map.get(locationId);
    }

    @Nullable
    @Override
    public City selectOneByNameZH(@NonNull String adm1NameZH, @NonNull String adm2NameZH, @Nullable String locationNameZH) {
        List<City> list = selectList(null);
        for (City city : list) {
            //匹配省市
            if (adm1NameZH.equals(city.getAdm1NameZH()) && adm2NameZH.equals(city.getAdm2NameZH())) {
                //如果没有区，则用市去匹配
                if (TextUtils.isEmpty(locationNameZH) ? adm2NameZH.startsWith(city.getLocationNameZH()) : locationNameZH.startsWith(city.getLocationNameZH())) {
                    return city;
                }
            }
        }
        return null;
    }

    @NonNull
    @Override
    public List<City> selectList(@Nullable String keywords) {
        if (cities == null) {
            String value = getAssetString("China-City-List.txt");
            if (value != null) {
                cities = GsonUtils.listFromJson(value, City.class);
            }
        }
        if (cities == null) {
            return new ArrayList<>();
        }
        if (TextUtils.isEmpty(keywords)) {
            return cities;
        }
        List<City> result = new ArrayList<>();
        for (City item : cities) {
            if (item.getAdm1NameZH().contains(keywords) || item.getAdm2NameZH().contains(keywords) || item.getLocationNameZH().contains(keywords) || keywords.contains(item.getLocationNameZH())) {
                result.add(item);
            }
        }
        return result;
    }

    @Nullable
    @Override
    public String getLastCity() {
        return getSharedPreferences().getString(LOCATION_ID_LAST, null);
    }

    @Override
    public void saveLastCity(@NonNull String locationId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOCATION_ID_LAST, locationId);
        editor.commit();
    }

    @NonNull
    @Override
    public List<String> selectCollections() {
        String value = getSharedPreferences().getString(LOCATION_ID_COLLECTIONS, null);
        return TextUtils.isEmpty(value) ? new ArrayList<>() : GsonUtils.listFromJson(value, String.class);
    }

    @Override
    public void saveCollection(@NonNull String locationId) {
        List<String> list = selectCollections();
        if (!list.contains(locationId)) {
            list.add(locationId);
        }
        saveCollections(list);
    }

    @Override
    public void saveCollections(@Nullable List<String> locationIds) {
        String value = GsonUtils.toJson(locationIds == null ? new ArrayList<>() : locationIds);
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(LOCATION_ID_COLLECTIONS, value);
        editor.commit();
    }

    @Override
    public void deleteCollection(@NonNull String locationId) {
        List<String> list = selectCollections();
        if (list.contains(locationId)) {
            list.remove(locationId);
        }
        saveCollections(list);
    }

    @Override
    public List<City> locationIds2Cities(@Nullable List<String> locationIds) {
        if (locationIds == null) {
            return null;
        }

        List<City> list = new ArrayList<>();
        for (String locationId : locationIds) {
            City city = selectOne(locationId);
            list.add(city);
        }
        return list;
    }

    @Override
    public List<String> cities2LocationIds(@Nullable List<City> cities) {
        if (cities == null) {
            return null;
        }

        List<String> list = new ArrayList<>();
        for (City city : cities) {
            String locationId = city.getLocationID();
            list.add(locationId);
        }
        return list;
    }

    private SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null) {
            sharedPreferences = Mapp.getInstance().getSharedPreferences("Weather", Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    /**
     * 懒加载获取缓存
     */
    @NonNull
    private Map<String, City> getCache() {
        if (cityCache == null) {
            cityCache = new HashMap<>();

            List<City> list = selectList(null);
            for (City city : list) {
                cityCache.put(city.getLocationID(), city);
            }
        }
        return cityCache;
    }

    /**
     * 获取asset文件数据
     */
    @Nullable
    private String getAssetString(@NonNull String assetName) {
        try {
            AssetManager assetManager = Mapp.getInstance().getResources().getAssets();
            InputStream inputStream = assetManager.open(assetName);
            return readStreamToString(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 读取文件
     */
    @Nullable
    private String readStreamToString(@NonNull InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        String result = null;

        try {
            byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) { //当等于-1说明没有数据可以读取了
                byteArrayOutputStream.write(buffer, 0, len);
            }

            //把读取到的字节数组转换为字符串
            result = byteArrayOutputStream.toString();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
