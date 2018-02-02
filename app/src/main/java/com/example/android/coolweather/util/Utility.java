package com.example.android.coolweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.coolweather.db.City;
import com.example.android.coolweather.db.County;
import com.example.android.coolweather.db.Province;
import com.example.android.coolweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.text.TextUtils.isEmpty;

/**
 * Created by Administrator on 2018/1/21.
 */

public class Utility {
    private static final String TAG = "Utility";

    public static Weather handleWeatherResponse(String response)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }
        catch (Exception ex)
        {
            Log.e(TAG, "handleWeatherResponse: ",ex);
        }
        return null;
    }

    /**
     * 解析省份
     * @param response 相应体
     * @return 是否解析成功
     */
    public static boolean handleProvinceReponse(String response)
    {
        if (!isEmpty(response))
        {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0;i < allProvinces.length();i++)
                {
                    JSONObject jsonObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(jsonObject.getString("name"));
                    province.setProvinceCode(jsonObject.getInt("id"));
                    province.save();
                }
                return true;
            }
            catch (JSONException ex)
            {
                Log.e(TAG, "handleProvinceReponse: ", ex);
            }

        }
        return false;
    }

    /**
     * 解析城市数据
     * @param response 响应
     * @param provinceId 省份id
     * @return 是否解析成功
     */
    public static boolean handleCityResponse(String response,int provinceId)
    {
        if (!isEmpty(response))
        {
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0;i < allCities.length();i++)
                {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                Log.e(TAG, "handleCityResponse: ",e );
            }
        }
        return false;
    }

    /**
     * 解析县级
     * @param response 响应体
     * @param cityId 县级id
     * @return 解析结果
     */
    public static boolean handleCountyResponse(String response,int cityId)
    {
        if (!isEmpty(response)) {
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i=0;i < allCounties.length();i++)
                {
                   JSONObject countyObject =  allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCityId(cityId);
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeacherId(countyObject.getString("weather_id"));
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                Log.e(TAG, "handleCountyResponse: ",e);
            }
        }
        return false;
    }
}
