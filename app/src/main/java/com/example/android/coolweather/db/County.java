package com.example.android.coolweather.db;

import android.provider.ContactsContract;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2018/1/20.
 */

public class County extends DataSupport {
    private int id;

    private String countyName;

    private String weacherId;

    private int cityId;

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return this.id;
    }

    public void setCountyName(String countyName)
    {
        this.countyName = countyName;
    }

    public String getCountyName()
    {
        return this.countyName;
    }

    public void setWeacherId(String weacherId)
    {
        this.weacherId = weacherId;
    }

    public String getWeacherId()
    {
        return this.weacherId;
    }

    public void setCityId(int cityId)
    {
        this.cityId = cityId;
    }

    public int getCityId()
    {
        return this.cityId;
    }
}
