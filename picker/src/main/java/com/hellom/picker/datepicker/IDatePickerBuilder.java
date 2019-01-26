package com.hellom.picker.datepicker;

import com.hellom.picker.datepicker.DatePicker;
import com.hellom.picker.datepicker.DatePickerBuilder;

/**
 * author:helloM
 * email:1694327880@qq.com
 */
public interface IDatePickerBuilder {
    /**
     * 设置日期选择监听
     *
     * @param listener 监听器
     * @return this
     */
    DatePickerBuilder setOnDateSelectedListener(DatePicker.OnDateSelectedListener listener);

    /**
     * 设置初始日期
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return this
     */
    DatePickerBuilder setCurrentDate(int year, int month, int day);

}
