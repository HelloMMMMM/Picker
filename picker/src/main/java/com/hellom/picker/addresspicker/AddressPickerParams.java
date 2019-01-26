package com.hellom.picker.addresspicker;

import com.hellom.picker.base.PickerParams;

/**
 * author:helloM
 * email:1694327880@qq.com
 */
public class AddressPickerParams extends PickerParams {
    /**
     * 地址选择监听
     */
    private AddressPicker.OnAddressSelectedListener mOnAddressSelectedListener;
    /**
     * 初始地址
     */
    private String currentProvince, currentCity, currentCounty;

    AddressPicker.OnAddressSelectedListener getOnAddressSelectedListener() {
        return mOnAddressSelectedListener;
    }

    void setOnAddressSelectedListener(AddressPicker.OnAddressSelectedListener mOnAddressSelectedListener) {
        this.mOnAddressSelectedListener = mOnAddressSelectedListener;
    }

    String getCurrentProvince() {
        return currentProvince;
    }

    void setCurrentProvince(String currentProvince) {
        this.currentProvince = currentProvince;
    }

    String getCurrentCity() {
        return currentCity;
    }

    void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    String getCurrentCounty() {
        return currentCounty;
    }

    void setCurrentCounty(String currentCounty) {
        this.currentCounty = currentCounty;
    }
}
