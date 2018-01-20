package com.example.android.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2018/1/20.
 */

public class City extends DataSupport {
    private int id;

    private String cityName;

    private int cityCode;

    private int provinceId;

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return this.id;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public String getCityName()
    {
        return this.cityName;
    }

    public void setCityCode(int cityCode)
    {
        this.cityCode = cityCode;
    }

    public int getCityCode()
    {
        return this.cityCode;
    }

    public void setProvinceId(int provinceId)
    {
        this.provinceId = provinceId;
    }

    public int getProvinceId()
    {
        return this.provinceId;
    }
}
