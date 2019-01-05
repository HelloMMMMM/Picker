package com.hellom.picker.addresspicker;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
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
    private int currentProvince, currentCity, currentCounty;
    private List<Province> provinceData;
    private List<City> cityData;
    private List<County> countyData;
    private AddressDictManager mAddressDictManager;
    private static Params params;
    /**
     * 显示位置
     */
    public static final int BOTTOM_STYLE = 1;
    public static final int CENTER_STYLE = 2;

    public static Builder builder() {
        return new Builder();
    }

    private static AddressPicker newInstance(Params p) {
        params = p;
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
        if (params.showMode == BOTTOM_STYLE) {
            setStyle(DialogFragment.STYLE_NO_TITLE, R.style.bottom_dialog_fragment);
        }
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
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            if (params.showMode == BOTTOM_STYLE) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                layoutParams.gravity = Gravity.BOTTOM;
            } else if (params.showMode == CENTER_STYLE) {
                layoutParams.gravity = Gravity.CENTER;
            }
        }
    }

    private void initSize() {
        Window window = getDialog().getWindow();
        if (window != null) {
            if (params.showMode == BOTTOM_STYLE) {
                window.setLayout(-1, -2);
            } else if (params.showMode == CENTER_STYLE) {
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                window.setLayout((int) (displayMetrics.widthPixels * 0.9f), -2);
            }
        }
    }

    private void initView(View view) {
        provinceList = view.findViewById(R.id.province_picker);
        cityList = view.findViewById(R.id.city_picker);
        areaList = view.findViewById(R.id.area_picker);
        provinceList.setTextColor(params.textColor);
        cityList.setTextColor(params.textColor);
        areaList.setTextColor(params.textColor);
        provinceList.setTextSize(params.textSize);
        cityList.setTextSize(params.textSize);
        areaList.setTextSize(params.textSize);
        provinceList.setLineColor(params.lineColor);
        cityList.setLineColor(params.lineColor);
        areaList.setLineColor(params.lineColor);
        provinceList.setOffsetX(params.mOffsetX);
        cityList.setOffsetX(params.mOffsetX);
        areaList.setOffsetX(params.mOffsetX);
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
                    County county = countyData.get(areaList.getSelectedItemPosition());
                    currentCounty = county.id;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initData() {
        mAddressDictManager = new AddressDictManager(getActivity());
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
            if (params.mOnAddressSelectedListener != null) {
                params.mOnAddressSelectedListener.onAddressSelected(provinceList.getSelectedItemData(), cityList.getSelectedItemData(), areaList.getSelectedItemData());
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

    /**
     * 建造者
     */
    public static class Builder {
        private Params p;

        private Builder() {
            p = new Params();
        }

        public Builder setShowMode(int mode) {
            if (mode == CENTER_STYLE || mode == BOTTOM_STYLE) {
                p.showMode = mode;
            }
            return this;
        }

        public Builder setOffsetX(int offsetX) {
            p.mOffsetX = offsetX;
            return this;
        }

        public Builder setTextSize(int size) {
            p.textSize = size;
            return this;
        }

        public Builder setTextColor(int color) {
            p.textColor = color;
            return this;
        }

        public Builder setLineColor(int color) {
            p.lineColor = color;
            return this;
        }

        public Builder setOnAddressSelectedListener(OnAddressSelectedListener onAddressSelectedListener) {
            p.mOnAddressSelectedListener = onAddressSelectedListener;
            return this;
        }

        public Builder setAddress(String province, String city, String county) {
            p.currentProvince = province;
            p.currentCity = city;
            p.currentCounty = county;
            return this;
        }

        public AddressPicker build() {
            return AddressPicker.newInstance(p);
        }
    }

    /**
     * 可定义参数
     */
    private static class Params {
        /**
         * 显示位置
         */
        private int showMode = BOTTOM_STYLE;
        /**
         * 偏移(负左偏,正右偏)
         */
        private int mOffsetX = 0;
        /**
         * 文字相关
         */
        private int textSize = 16;
        private int textColor = 0xFF333333;
        /**
         * 分割线颜色
         */
        private int lineColor = 0xFFffffff;
        /**
         * 日期选择监听
         */
        private OnAddressSelectedListener mOnAddressSelectedListener;
        /**
         * 初始位置
         */
        private String currentProvince, currentCity, currentCounty;
    }
}
