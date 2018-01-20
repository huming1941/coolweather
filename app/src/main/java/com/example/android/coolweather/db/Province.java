package com.example.android.coolweather.db;

import org.litepal.crud.DataSupport;


/**
 * Created by Administrator on 2018/1/20.
 */

public class Province extends DataSupport {
    private int id;

    private String provinceName;

    private int provinceCode;

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return this.id;
    }

    public void setProvinceName(String provinceName)
    {
        this.provinceName = provinceName;
    }

    public String getProvinceName()
    {
        return this.provinceName;
    }

    public void setProvinceCode(int provinceCode)
    {
        this.provinceCode = provinceCode;
    }

    public int getProvinceCode()
    {
        return this.provinceCode;
    }

}
