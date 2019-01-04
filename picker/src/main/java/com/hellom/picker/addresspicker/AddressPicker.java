package com.hellom.picker.addresspicker;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.hellom.picker.R;
import com.hellom.picker.addresspicker.bean.City;
import com.hellom.picker.addresspicker.bean.County;
import com.hellom.picker.addresspicker.bean.Province;
import com.hellom.picker.addresspicker.db.manager.AddressDictManager;
import com.hellom.picker.baseview.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * author:helloM
 * email:1694327880@qq.com
 */
public class AddressPicker extends DialogFragment implements View.OnClickListener {

    private WheelView provinceList, cityList, areaList;
    private int currentProvince, currentCity, currentArea;
    private List<Province> provinceData;
    private List<City> cityData;
    private List<County> countryData;
    private AddressDictManager mAddressDictManager;

    private AddressPicker.OnAddressSelectedListener mOnAddressSelectedListener;

    public void setOnAddressSelectedListener(OnAddressSelectedListener mOnAddressSelectedListener) {
        this.mOnAddressSelectedListener = mOnAddressSelectedListener;
    }

    public static AddressPicker newInstance() {
        Bundle args = new Bundle();
        AddressPicker fragment = new AddressPicker();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initStyle();

        View view = inflater.inflate(R.layout.dialog_fragment_address_picker, container, false);
        initView(view);
        initListener(view);
        initData();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.bottom_dialog_fragment);
    }

    @Override
    public void onStart() {
        super.onStart();
        initSize();
    }

    private void initStyle() {
        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(true);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.BOTTOM;

        }
    }

    private void initSize() {
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setLayout(-1, -2);
        }
    }

    private void initView(View view) {
        mAddressDictManager = new AddressDictManager(getActivity());
        provinceList = view.findViewById(R.id.province_picker);
        cityList = view.findViewById(R.id.city_picker);
        areaList = view.findViewById(R.id.area_picker);
    }

    private void initListener(View view) {
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
        view.findViewById(R.id.tv_sure).setOnClickListener(this);
        provinceList.setOnSelectedChangedListener(new WheelView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged() {
                try {
                    Province province = provinceData.get(provinceList.getSelectedItemPosition());
                    currentProvince = province.id;
                    cityList.setData(initCityData(province.id));
                    areaList.setData(initCountryData(currentCity));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        cityList.setOnSelectedChangedListener(new WheelView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged() {
                try {
                    City city = cityData.get(cityList.getSelectedItemPosition());
                    currentCity = city.id;
                    areaList.setData(initCountryData(city.id));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        areaList.setOnSelectedChangedListener(new WheelView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged() {
                try {
                    County county = countryData.get(areaList.getSelectedItemPosition());
                    currentArea = county.id;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initData() {
        provinceList.setData(initProvinceData());
        cityList.setData(initCityData(currentProvince));
        areaList.setData(initCountryData(currentCity));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_cancel) {
            dismiss();
        } else if (id == R.id.tv_sure) {
            if (mOnAddressSelectedListener != null) {
                mOnAddressSelectedListener.onAddressSelected(provinceList.getSelectedItemData(), cityList.getSelectedItemData(), areaList.getSelectedItemData());
            }
            dismiss();
        }
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
        countryData = mAddressDictManager.getCountyList(cityId);
        if (countryData != null) {
            if (countryData.size() > 0) {
                currentArea = countryData.get(0).id;
            }
            for (int i = 0; i < countryData.size(); i++) {
                data.add(countryData.get(i).name);
            }
        }
        return data;
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
