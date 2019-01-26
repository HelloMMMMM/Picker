package com.hellom.picker.datepicker;

import com.hellom.picker.base.PickerParams;
import com.hellom.picker.datepicker.DatePicker;

/**
 * author:helloM
 * email:1694327880@qq.com
 */
public class DatePickerParams extends PickerParams {
    /**
     * 日期选择监听
     */
    private DatePicker.OnDateSelectedListener mOnDateSelectedListener;
    /**
     * 初始时间
     */
    private int currentYear, currentMonth, currentDay;

    DatePicker.OnDateSelectedListener getOnDateSelectedListener() {
        return mOnDateSelectedListener;
    }

    void setOnDateSelectedListener(DatePicker.OnDateSelectedListener mOnDateSelectedListener) {
        this.mOnDateSelectedListener = mOnDateSelectedListener;
    }

    int getCurrentYear() {
        return currentYear;
    }

    void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    int getCurrentMonth() {
        return currentMonth;
    }

    void setCurrentMonth(int currentMonth) {
        this.currentMonth = currentMonth;
    }

    int getCurrentDay() {
        return currentDay;
    }

    void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }
}
