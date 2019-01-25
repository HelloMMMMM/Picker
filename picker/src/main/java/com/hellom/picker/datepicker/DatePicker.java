package com.hellom.picker.datepicker;

import android.support.v4.app.FragmentManager;

import com.hellom.picker.BasePicker;
import com.hellom.picker.DatePickerParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * author:helloM
 * email:1694327880@qq.com
 */
public class DatePicker {

    private int currentYear, currentMonth, currentDay;
    private DatePickerParams params;
    private BasePicker basePicker;

    /**
     * 年份半数范围
     */
    private static final int YEAR_HALF_RANGE = 150;

    public DatePicker(DatePickerParams params) {
        this.params = params;
        initView();
        initListener();
        initData();
    }

    private void initView() {
        basePicker = BasePicker.newInstance(params);
    }

    private void initListener() {
        basePicker.setOnClickListener(new BasePicker.OnClickListener() {
            @Override
            public void cancel() {
                basePicker.dismiss();
            }

            @Override
            public void sure() {
                if (params.getOnDateSelectedListener() != null) {
                    params.getOnDateSelectedListener().onDateSelected(basePicker.getPickerOneSelectedItem(),
                            basePicker.getPickerTwoSelectedItem(), basePicker.getPickerThreeSelectedItem());
                }
                basePicker.dismiss();
            }
        });
        basePicker.setOnPickerSelectedChangedListener(new BasePicker.OnPickerSelectedChangedListener() {
            @Override
            public void onPickerOneSelectedChanged(String data, int position) {
                try {
                    currentYear = Integer.parseInt(data);
                    basePicker.setPickerThreeData(initDayData(currentYear, currentMonth));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPickerTwoSelectedChanged(String data, int position) {
                try {
                    currentMonth = Integer.parseInt(data);
                    basePicker.setPickerThreeData(initDayData(currentYear, currentMonth));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPickerThreeSelectedChanged(String data, int position) {
                try {
                    currentDay = Integer.parseInt(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initData() {
        currentYear = params.getCurrentYear() == 0 ? getCurrentYear() : params.getCurrentYear();
        currentMonth = params.getCurrentMonth() == 0 ? getCurrentMonth() : params.getCurrentMonth();
        currentDay = params.getCurrentDay() == 0 ? getCurrentDayOfMonth() : params.getCurrentDay();
        basePicker.setPickerOneData(initYearData(currentYear));
        basePicker.setPickerOneSelectedItem(String.valueOf(currentYear));
        basePicker.setPickerTwoData(initMonthData());
        basePicker.setPickerTwoSelectedItem(String.valueOf(currentMonth));
        basePicker.setPickerThreeData(initDayData(currentYear, currentMonth));
        basePicker.setPickerThreeSelectedItem(String.valueOf(currentDay));
    }

    private int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    private int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    private int getCurrentDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    private int getDayOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 0);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    private List<String> initDayData(int year, int month) {
        int dayOfMonth = getDayOfMonth(year, month);
        List<String> dayData = new ArrayList<>(dayOfMonth);
        for (int i = 1; i <= dayOfMonth; i++) {
            dayData.add(String.valueOf(i));
        }
        return dayData;
    }

    private List<String> initMonthData() {
        int monthCount = 12;
        List<String> monthData = new ArrayList<>(monthCount);
        for (int i = 1; i <= monthCount; i++) {
            monthData.add(String.valueOf(i));
        }
        return monthData;
    }

    private List<String> initYearData(int currentYear) {
        int yearCount = YEAR_HALF_RANGE * 2 + 1;
        List<String> yearData = new ArrayList<>(yearCount);
        for (int i = currentYear - YEAR_HALF_RANGE; i <= currentYear + YEAR_HALF_RANGE; i++) {
            yearData.add(String.valueOf(i));
        }
        return yearData;
    }

    public void show(FragmentManager fragmentManager, String tag) {
        basePicker.show(fragmentManager, tag);
    }

    public interface OnDateSelectedListener {
        /**
         * 日期选择回调
         *
         * @param year  年
         * @param month 月
         * @param day   日
         */
        void onDateSelected(String year, String month, String day);
    }
}
