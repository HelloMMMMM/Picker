package com.hellom.picker.addresspicker;

import android.content.Context;

import com.hellom.picker.base.BasePickerBuilder;
import com.hellom.picker.base.PickerParams;

/**
 * author:helloM
 * email:1694327880@qq.com
 */
public class AddressPickerBuilder extends BasePickerBuilder<AddressPickerBuilder, AddressPicker> implements IAddressPickerBuilder {
    private AddressPickerParams params;
    private Context context;

    public AddressPickerBuilder(Context context, PickerParams params) {
        super(params);
        this.context = context;
        boolean isDatePickerParams = params instanceof AddressPickerParams;
        if (isDatePickerParams) {
            this.params = (AddressPickerParams) params;
        } else {
            throw new IllegalArgumentException("must be AddressPickerParams");
        }
    }

    @Override
    protected AddressPickerBuilder castBuilder() {
        return this;
    }

    @Override
    public AddressPicker build() {
        return new AddressPicker(context, params);
    }

    @Override
    public AddressPickerBuilder setOnAddressSelectedListener(AddressPicker.OnAddressSelectedListener listener) {
        params.setOnAddressSelectedListener(listener);
        return this;
    }

    @Override
    public AddressPickerBuilder setCurrentAddress(String province, String city, String county) {
        params.setCurrentProvince(province);
        params.setCurrentCity(city);
        params.setCurrentCounty(county);
        return this;
    }
}
