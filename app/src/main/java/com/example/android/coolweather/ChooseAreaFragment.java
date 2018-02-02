package com.example.android.coolweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.coolweather.db.City;
import com.example.android.coolweather.db.County;
import com.example.android.coolweather.db.Province;
import com.example.android.coolweather.util.HttpUtil;
import com.example.android.coolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/1/21.
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;

    public static final int LEVEL_CITY = 1;

    public static final int LEVEL_COUNTY = 2;

    public static final String ADDRESS = "http://guolin.tech/api/china";

    private ProgressDialog progressDialog;

    private TextView titleText;

    private Button backButton;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private List<String> dataList = new ArrayList<String>();

    /**
     * 省列表
     */
    private List<Province> provinceList;

    /**
     * 市列表
     */
    private List<City> cityList;

    /**
     * 县列表
     */
    private List<County> countyList;

    /**
     * 选中的省份
     */
    private Province selectedProvince;

    /**
     * 选中的城市
     */
    private City selectedCity;

    /**
     * 当前选中的级别
     */
    private int currentLevel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.choose_area,container,false);
        titleText = (TextView)view.findViewById(R.id.title_text);
        backButton = (Button)view.findViewById(R.id.back_button);
        listView = (ListView)view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentLevel == LEVEL_PROVINCE)
                {
                    selectedProvince = provinceList.get(i);
                    queryCities();
                }
                else if (currentLevel == LEVEL_CITY)
                {
                    selectedCity = cityList.get(i);
                    queryCounties();
                }
                else if (currentLevel == LEVEL_COUNTY)
                {
                    String weatherId = countyList.get(i).getWeacherId();
                    Intent intent = new Intent(getActivity(),WeatherActivity.class);
                    intent.putExtra("weather_id",weatherId);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLevel == LEVEL_COUNTY)
                {
                    queryCities();
                }
                else if (currentLevel == LEVEL_CITY)
                {
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }


    private void queryProvinces()
    {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if (!provinceList.isEmpty())
        {
            dataList.clear();
            for (Province province:provinceList)
            {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }
        else
        {
            String address = ADDRESS;
            queryFromService(address,"province");
        }
    }

    private void queryCities()
    {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?",String.valueOf(selectedProvince.getId())).find(City.class);

        if (!cityList.isEmpty())
        {
            dataList.clear();
            for (City city : cityList)
            {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }
        else
        {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = ADDRESS + "/" + provinceCode;
            queryFromService(address,"city");
        }
    }

    private void queryCounties()
    {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?",String.valueOf(selectedCity.getId())).find(County.class);

        if (!countyList.isEmpty())
        {
            dataList.clear();
            for (County county : countyList)
            {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }
        else
        {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = ADDRESS + "/" + provinceCode + "/" + cityCode;
            queryFromService(address,"county");
        }
    }

    private void queryFromService(String address,final String type)
    {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type))
                {
                    result = Utility.handleProvinceReponse(responseText);
                }
                else if ("city".equals(type))
                {
                    result = Utility.handleCityResponse(responseText,selectedProvince.getId());
                }
                else if ("county".equals(type))
                {
                    result = Utility.handleCountyResponse(responseText,selectedCity.getId());
                }
                if (result)
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type))
                            {
                                queryProvinces();
                            }
                            else if ("city".equals(type))
                            {
                                queryCities();
                            }
                            else if ("county".equals(type))
                            {
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog()
    {
        if (progressDialog == null)
        {
            progressDialog = new ProgressDialog(this.getActivity());
            progressDialog.setMessage("正在加载......");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog()
    {
        if (progressDialog != null)
        {
            progressDialog.dismiss();
        }
    }


}