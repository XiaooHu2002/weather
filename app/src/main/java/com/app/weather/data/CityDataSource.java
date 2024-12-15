package com.app.weather.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.weather.bean.City;

import java.util.List;

/**
 * 城市数据源
 */
public interface CityDataSource {

    /**
     * 根据地区主键查询城市
     */
    @Nullable
    City selectOne(@NonNull String locationId);

    /**
     * 根据地区中文名查询城市
     */
    @Nullable
    City selectOneByNameZH(@NonNull String adm1NameZH, @NonNull String adm2NameZH, @Nullable String locationNameZH);

    /**
     * 查找城市列表
     */
    @NonNull
    List<City> selectList(@Nullable String keywords);

    /**
     * 获取最近一次操作的城市主键
     */
    @Nullable
    String getLastCity();

    /**
     * 保存最近一次操作的城市主键
     */
    void saveLastCity(@NonNull String locationId);

    /**
     * 获取收藏的城市主键列表
     */
    @NonNull
    List<String> selectCollections();

    /**
     * 保存收藏的城市主键
     */
    void saveCollection(@NonNull String locationId);

    /**
     * 保存收藏的城市主键列表
     */
    void saveCollections(@Nullable List<String> locationIds);

    /**
     * 删除收藏的城市主键
     */
    void deleteCollection(@NonNull String locationId);

    /**
     * 将城市主键列表转成城市列表
     */
    List<City> locationIds2Cities(@Nullable List<String> locationIds);

    /**
     * 将城市列表转成城市主键列表
     */
    List<String> cities2LocationIds(@Nullable List<City> cities);

}