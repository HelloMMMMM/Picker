package com.hellom.picker;

import static com.hellom.picker.PickerConstant.*;

/**
 * author:helloM
 * email:1694327880@qq.com
 */
public abstract class BasePickerBuilder<T> {

    private PickerParams params;

    public BasePickerBuilder(PickerParams params) {
        this.params = params;
    }

    public BasePickerBuilder setShowMode(int mode) {
        if (mode == CENTER_STYLE || mode == BOTTOM_STYLE) {
            params.setShowMode(mode);
        }
        return this;
    }

    public BasePickerBuilder setOffsetX(int offsetX) {
        params.setOffsetX(offsetX);
        return this;
    }

    public BasePickerBuilder setTextSize(int size) {
        params.setTextSize(size);
        return this;
    }

    public BasePickerBuilder setTextColor(int color) {
        params.setTextColor(color);
        return this;
    }

    public BasePickerBuilder setLineColor(int color) {
        params.setLineColor(color);
        return this;
    }

    public abstract T build();
}
