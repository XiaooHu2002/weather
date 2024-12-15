package com.app.weather.bean;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class City extends BaseBean {

    @SerializedName("Location_ID")
    private String locationID;//地区主键

    @SerializedName("Location_Name_EN")
    private String locationNameEN;//地区英文名称

    @SerializedName("Location_Name_ZH")
    private String locationNameZH;//地区中文名称

    @SerializedName("Country_Code")
    private String countryCode;//国家编码

    @SerializedName("Country_Name_EN")
    private String countryNameEN;//国家英文名称

    @SerializedName("Country_Name_ZH")
    private String countryNameZH;//国家中文名称

    @SerializedName("Adm1_Name_EN")
    private String adm1NameEN;//省英文名称

    @SerializedName("Adm1_Name_ZH")
    private String adm1NameZH;//省中文名称

    @SerializedName("Adm2_Name_EN")
    private String adm2NameEN;//市英文名称

    @SerializedName("Adm2_Name_ZH")
    private String adm2NameZH;//市中文名称

    @SerializedName("Timezone")
    private String timezone;//时区

    @SerializedName("Latitude")
    private String latitude;//维度

    @SerializedName("Longitude")
    private String longitude;//经度

    @SerializedName("Adcode")
    private String adcode;//地区编码

    /**
     * 因为locationID是主键，具有唯一性，所以以此字段做是否相同的判断
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(locationID, city.locationID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationID);
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getLocationNameEN() {
        return locationNameEN;
    }

    public void setLocationNameEN(String locationNameEN) {
        this.locationNameEN = locationNameEN;
    }

    public String getLocationNameZH() {
        return locationNameZH;
    }

    public void setLocationNameZH(String locationNameZH) {
        this.locationNameZH = locationNameZH;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryNameEN() {
        return countryNameEN;
    }

    public void setCountryNameEN(String countryNameEN) {
        this.countryNameEN = countryNameEN;
    }

    public String getCountryNameZH() {
        return countryNameZH;
    }

    public void setCountryNameZH(String countryNameZH) {
        this.countryNameZH = countryNameZH;
    }

    public String getAdm1NameEN() {
        return adm1NameEN;
    }

    public void setAdm1NameEN(String adm1NameEN) {
        this.adm1NameEN = adm1NameEN;
    }

    public String getAdm1NameZH() {
        return adm1NameZH;
    }

    public void setAdm1NameZH(String adm1NameZH) {
        this.adm1NameZH = adm1NameZH;
    }

    public String getAdm2NameEN() {
        return adm2NameEN;
    }

    public void setAdm2NameEN(String adm2NameEN) {
        this.adm2NameEN = adm2NameEN;
    }

    public String getAdm2NameZH() {
        return adm2NameZH;
    }

    public void setAdm2NameZH(String adm2NameZH) {
        this.adm2NameZH = adm2NameZH;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

}