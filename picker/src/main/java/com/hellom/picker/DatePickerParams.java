package com.hellom.picker;

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

    public DatePicker.OnDateSelectedListener getmOnDateSelectedListener() {
        return mOnDateSelectedListener;
    }

    public void setmOnDateSelectedListener(DatePicker.OnDateSelectedListener mOnDateSelectedListener) {
        this.mOnDateSelectedListener = mOnDateSelectedListener;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    public int getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(int currentMonth) {
        this.currentMonth = currentMonth;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }
}
