package com.hellom.picker;

import com.hellom.picker.datepicker.DatePicker;

/**
 * author:helloM
 * email:1694327880@qq.com
 */
public class DatePickerBuilder extends BasePickerBuilder<DatePickerBuilder, DatePicker> implements IDatePickerBuilder {
    private DatePickerParams params;

    public DatePickerBuilder(PickerParams params) {
        super(params);
        boolean isDatePickerParams = params instanceof DatePickerParams;
        if (isDatePickerParams) {
            this.params = (DatePickerParams) params;
        } else {
            throw new IllegalArgumentException("must be DatePickerParams");
        }
    }

    @Override
    protected DatePickerBuilder castBuilder() {
        return this;
    }

    @Override
    public DatePicker build() {
        return DatePicker.newInstance(this.params);
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
}
