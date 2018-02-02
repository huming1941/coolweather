package com.example.android.coolweather.gson;

/**
 * Created by Administrator on 2018/1/24.
 */

public class AQI {
    public AQICity city;

    public class AQICity
    {
        public String aqi;

        public String pm25;
    }
}
