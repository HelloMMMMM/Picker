package com.hellom.picker.addresspicker;


import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.hellom.picker.addresspicker.bean.City;
import com.hellom.picker.addresspicker.bean.County;
import com.hellom.picker.addresspicker.bean.Province;
import com.hellom.picker.addresspicker.db.manager.AddressDictManager;
import com.hellom.picker.base.BasePicker;
import com.hellom.picker.base.IBasePicker;


import java.util.ArrayList;
import java.util.List;

/**
 * author:helloM
 * email:1694327880@qq.com
 */
public class AddressPicker implements IBasePicker {

    private int currentProvince = -1, currentCity = -1, currentCounty = -1;
    private List<Province> provinceData;
    private List<City> cityData;
    private List<County> countyData;
    private AddressDictManager mAddressDictManager;
    private BasePicker basePicker;
    private AddressPickerParams params;
    private Context context;

    AddressPicker(Context context, AddressPickerParams params) {
        this.context = context;
        this.params = params;
        initView();
        initListener();
        initData();
    }

    private void initView() {
        basePicker = BasePicker.newInstance(params);
    }

    private void initListener() {
        basePicker.setOnClickListener(new BasePicker.OnClickListener() {
            @Override
            public void cancel() {
                basePicker.dismiss();
            }

            @Override
            public void sure() {
                if (params.getOnAddressSelectedListener() != null) {
                    params.getOnAddressSelectedListener().onAddressSelected(basePicker.getPickerOneSelectedItem(),
                            basePicker.getPickerTwoSelectedItem(), basePicker.getPickerThreeSelectedItem());
                }
                basePicker.dismiss();
            }
        });
        basePicker.setOnPickerSelectedChangedListener(new BasePicker.OnPickerSelectedChangedListener() {
            @Override
            public void onPickerOneSelectedChanged(String data, int position) {
                try {
                    Province province = provinceData.get(position);
                    currentProvince = province.id;
                    basePicker.setPickerTwoData(initCityData(province.id));
                    basePicker.setPickerThreeData(initCountryData(currentCity));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPickerTwoSelectedChanged(String data, int position) {
                try {
                    City city = cityData.get(position);
                    currentCity = city.id;
                    basePicker.setPickerThreeData(initCountryData(city.id));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPickerThreeSelectedChanged(String data, int position) {
                try {
                    County county = countyData.get(position);
                    currentCounty = county.id;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initData() {
        mAddressDictManager = new AddressDictManager(context);
        basePicker.setPickerOneData(initProvinceData());
        setCurrentProvince(params.getCurrentProvince());
        basePicker.setPickerTwoData(initCityData(currentProvince));
        setCurrentCity(params.getCurrentCity());
        basePicker.setPickerThreeData(initCountryData(currentCity));
        setCurrentCounty(params.getCurrentCounty());
    }

    private List<String> initProvinceData() {
        List<String> data = new ArrayList<>();
        provinceData = mAddressDictManager.getProvinceList();
        if (provinceData != null) {
            if (provinceData.size() > 0) {
                currentProvince = provinceData.get(0).id;
            }
            for (int i = 0; i < provinceData.size(); i++) {
                data.add(provinceData.get(i).name);
            }
        }
        return data;
    }

    private List<String> initCityData(int provinceId) {
        List<String> data = new ArrayList<>();
        cityData = mAddressDictManager.getCityList(provinceId);
        if (cityData != null) {
            if (cityData.size() > 0) {
                currentCity = cityData.get(0).id;
            }
            for (int i = 0; i < cityData.size(); i++) {
                data.add(cityData.get(i).name);
            }
        }
        return data;
    }

    private List<String> initCountryData(int cityId) {
        List<String> data = new ArrayList<>();
        countyData = mAddressDictManager.getCountyList(cityId);
        if (countyData != null) {
            if (countyData.size() > 0) {
                currentCounty = countyData.get(0).id;
            }
            for (int i = 0; i < countyData.size(); i++) {
                data.add(countyData.get(i).name);
            }
        }
        return data;
    }

    private void setCurrentProvince(String province) {
        Province provinceBean = mAddressDictManager.getProvinceBean(province);
        if (provinceBean != null) {
            currentProvince = provinceBean.id;
            basePicker.setPickerOneSelectedItem(province);
        }
    }

    private void setCurrentCity(String city) {
        City cityBean = mAddressDictManager.getCityBean(city);
        if (cityBean != null) {
            currentCity = cityBean.id;
            basePicker.setPickerTwoSelectedItem(city);
        }
    }

    private void setCurrentCounty(String county) {
        County countyBean = mAddressDictManager.getCountyBean(county);
        if (countyBean != null) {
            currentCounty = countyBean.id;
            basePicker.setPickerThreeSelectedItem(county);
        }
    }

    @Override
    public void show(FragmentManager fragmentManager, String tag) {
        basePicker.show(fragmentManager, tag);
    }

    public interface OnAddressSelectedListener {
        /**
         * 日期选择回调
         *
         * @param province 省
         * @param city     市
         * @param area     区
         */
        void onAddressSelected(String province, String city, String area);
    }
}
