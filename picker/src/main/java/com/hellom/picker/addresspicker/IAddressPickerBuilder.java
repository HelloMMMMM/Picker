package com.hellom.picker.addresspicker;

/**
 * author:helloM
 * email:1694327880@qq.com
 */
public interface IAddressPickerBuilder {
    /**
     * 设置地址选择监听
     *
     * @param listener 监听器
     * @return this
     */
    AddressPickerBuilder setOnAddressSelectedListener(AddressPicker.OnAddressSelectedListener listener);

    /**
     * 设置初始地址
     *
     * @param province 省
     * @param city     市
     * @param county   区
     * @return this
     */
    AddressPickerBuilder setCurrentAddress(String province, String city, String county);
}
