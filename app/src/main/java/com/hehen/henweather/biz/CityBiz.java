package com.hehen.henweather.biz;

import android.util.Log;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.hehen.henweather.bean.City;
import com.hehen.henweather.dao.CityDao;
import com.hehen.henweather.utils.data.DataUtils;
import com.hehen.henweather.utils.http.CommonRequest;
import com.hehen.henweather.utils.http.HttpUtils;
import com.hehen.henweather.utils.http.StringCallback;
import com.hehen.henweather.utils.other.GsonUtils;
import com.hehen.henweather.utils.other.SPUtils;
import com.hehen.henweather.utils.other.T;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * @author chenping
 * @date 2019/2/27 12:56 AM
 * @Description:
 */
public class CityBiz {
    private static CityDao dao = new CityDao();
    private static  String url = "http://cdn.sojson.com/_city.json";
    public static boolean initFlag = false;  //保存城市数据加载完成
    public static  void initLoad() {
        boolean flag = (boolean) SPUtils.getInstance().get("initCity",true);
        if(!flag){
            HttpUtils.sendRequest(CommonRequest.createRequestGet(url, null), new StringCallback() {
                @Override
                public void onError(Exception e) {
                    T.showToast(e.getMessage());
                    return;
                }
                @Override
                public void onSuccess(String body) {
                    List<City> cityList = GsonUtils.getGson().fromJson(body, new TypeToken<List<City>>() {
                    }.getType());
                    if (cityList != null && !cityList.isEmpty()) {
                        DataUtils.setCitys(cityList);  //添加缓存
                        dao.addAll(cityList);
                        Log.i(TAG, "onSuccess: "+cityList);
                        SPUtils.getInstance().put("initCity",true);
                    }
                }
            });
    }
}
    public List<City> getProvince(){
        List<City> list;
        if(DataUtils.mProvince ==null||DataUtils.mProvince.isEmpty()){
            list = dao.findByPid(0);
            if(list ==null){
                initLoad();
            }
            DataUtils.setProvince(list);
            return list ;
        }else{
            return DataUtils.mProvince;
        }
    }

    public List<City> getcities(City cities){
      return dao.findByPid(cities.getId());
    }
    public List<City> getCount(City city){
        return dao.findByPid(city.getId());
    }
}