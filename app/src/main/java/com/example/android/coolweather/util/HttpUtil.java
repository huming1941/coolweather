package com.example.android.coolweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2018/1/21.
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback)
    {
        OkHttpClient httpClient = new OkHttpClient();
        Request request = (new Request.Builder()).url(address).build();
        httpClient.newCall(request).enqueue(callback);
    }
}
