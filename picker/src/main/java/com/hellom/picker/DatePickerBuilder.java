package com.hellom.picker;

import com.hellom.picker.datepicker.DatePicker;

import static com.hellom.picker.PickerConstant.*;

/**
 * author:helloM
 * email:1694327880@qq.com
 */
public class DatePickerBuilder implements IPickerBuilder<DatePickerBuilder, DatePicker>, IDatePickerBuilder {
    private DatePickerParams params;

    public DatePickerBuilder(PickerParams params) {
        boolean isDatePickerParams = params instanceof DatePickerParams;
        if (isDatePickerParams) {
            this.params = (DatePickerParams) params;
        } else {
            throw new IllegalArgumentException("must be DatePickerParams");
        }
    }

    @Override
    public DatePickerBuilder setOnDateSelectedListener(DatePicker.OnDateSelectedListener onDateSelectedListener) {
        params.setOnDateSelectedListener(onDateSelectedListener);
        return this;
    }

    @Override
    public DatePickerBuilder setCurrentDate(int year, int month, int day) {
        params.setCurrentYear(year);
        params.setCurrentMonth(month);
        params.setCurrentDay(day);
        return this;
    }

    @Override
    public DatePickerBuilder setShowMode(int mode) {
        if (mode == CENTER_STYLE || mode == BOTTOM_STYLE) {
            params.setShowMode(mode);
        }
        return this;
    }

    @Override
    public DatePickerBuilder setOffsetX(int offsetX) {
        params.setOffsetX(offsetX);
        return this;
    }

    @Override
    public DatePickerBuilder setTextSize(int size) {
        params.setTextSize(size);
        return this;
    }

    @Override
    public DatePickerBuilder setTextColor(int color) {
        params.setTextColor(color);
        return this;
    }

    @Override
    public DatePickerBuilder setLineColor(int color) {
        params.setLineColor(color);
        return this;
    }

    @Override
    public DatePicker build() {
        return DatePicker.newInstance(this.params);
    }
}
