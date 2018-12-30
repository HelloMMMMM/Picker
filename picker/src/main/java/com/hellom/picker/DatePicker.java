package com.hellom.picker;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * author:helloM
 * email:1694327880@qq.com
 */
public class DatePicker extends DialogFragment implements View.OnClickListener {

    private WheelView yearList, monthList, dayList;
    private int currentYear, currentMonth, currentDay;

    /**
     * 年份半数范围
     */
    private int yearHalfRange = 150;

    private OnDateSelectedListener mOnDateSelectedListener;

    public void setOnDateSelectedListener(OnDateSelectedListener mOnDateSelectedListener) {
        this.mOnDateSelectedListener = mOnDateSelectedListener;
    }

    public static DatePicker newInstance() {
        Bundle args = new Bundle();
        DatePicker fragment = new DatePicker();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initStyle();

        View view = inflater.inflate(R.layout.dialog_fragment_date_picker, container, false);
        initView(view);
        initListener(view);
        initData();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.bottom_dialog_fragment);
    }

    @Override
    public void onStart() {
        super.onStart();
        initSize();
    }

    private void initStyle() {
        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(true);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.BOTTOM;

        }
    }

    private void initSize() {
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setLayout(-1, -2);
        }
    }

    private void initView(View view) {
        yearList = view.findViewById(R.id.year_picker);
        monthList = view.findViewById(R.id.month_picker);
        dayList = view.findViewById(R.id.day_picker);
    }

    private void initListener(View view) {
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
        view.findViewById(R.id.tv_sure).setOnClickListener(this);
        yearList.setOnSelectedChangedListener(new WheelView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged() {
                try {
                    currentYear = Integer.parseInt(yearList.getSelectedItemData());
                    dayList.setData(initDayData(currentYear, currentMonth));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        monthList.setOnSelectedChangedListener(new WheelView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged() {
                try {
                    currentMonth = Integer.parseInt(monthList.getSelectedItemData());
                    dayList.setData(initDayData(currentYear, currentMonth));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dayList.setOnSelectedChangedListener(new WheelView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged() {
                try {
                    currentDay = Integer.parseInt(dayList.getSelectedItemData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initData() {
        currentYear = getCurrentYear();
        currentMonth = getCurrentMonth();
        currentDay = getCurrentDayOfMonth();
        yearList.setData(initYearData(currentYear));
        yearList.setSelectedItem(String.valueOf(currentYear));
        monthList.setData(initMonthData());
        monthList.setSelectedItem(String.valueOf(currentMonth));
        dayList.setData(initDayData(currentYear, currentMonth));
        dayList.setSelectedItem(String.valueOf(currentDay));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_cancel) {
            dismiss();
        } else if (id == R.id.tv_sure) {
            if (mOnDateSelectedListener != null) {
                mOnDateSelectedListener.onDateSelected(yearList.getSelectedItemData(), monthList.getSelectedItemData(), dayList.getSelectedItemData());
            }
            dismiss();
        }
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
        int yearCount = yearHalfRange * 2 + 1;
        List<String> yearData = new ArrayList<>(yearCount);
        for (int i = currentYear - yearHalfRange; i <= currentYear + yearHalfRange; i++) {
            yearData.add(String.valueOf(i));
        }
        return yearData;
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
