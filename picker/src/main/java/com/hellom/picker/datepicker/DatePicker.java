package com.hellom.picker.datepicker;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.hellom.picker.R;
import com.hellom.picker.baseview.WheelView;

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
     * 显示位置
     */
    private int showMode = BOTTOM_STYLE;
    public static final int BOTTOM_STYLE = 1;
    public static final int CENTER_STYLE = 2;
    /**
     * 偏移(负左偏,正右偏)
     */
    private int mOffsetX = -1;
    /**
     * 文字相关
     */
    private int textSize = -1;
    private int textColor = -1;
    /**
     * 分割线颜色
     */
    private int lineColor = -1;
    /**
     * 日期范围
     */
    private int minYear, minMonth, minDay;
    private int maxYear, maxMonth, maxDay;

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    public void setCurrentMonth(int currentMonth) {
        this.currentMonth = currentMonth;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }

    public void setShowMode(int showMode) {
        this.showMode = showMode;
    }

    public void setOffsetX(int mOffsetX) {
        this.mOffsetX = mOffsetX;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    /**
     * 年份半数范围
     */
    private static final int YEAR_HALF_RANGE = 150;

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
        if (showMode == BOTTOM_STYLE) {
            setStyle(DialogFragment.STYLE_NO_TITLE, R.style.bottom_dialog_fragment);
        }
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
        if (window != null && showMode == BOTTOM_STYLE) {
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
        if (textColor != -1) {
            yearList.setTextColor(textColor);
            monthList.setTextColor(textColor);
            dayList.setTextColor(textColor);
        }
        if (textSize != -1) {
            yearList.setTextSize(textSize);
            monthList.setTextSize(textSize);
            dayList.setTextSize(textSize);
        }
        if (lineColor != -1) {
            yearList.setLineColor(lineColor);
            monthList.setLineColor(lineColor);
            dayList.setLineColor(lineColor);
        }
        if (mOffsetX != -1) {
            yearList.setOffsetX(mOffsetX);
            monthList.setOffsetX(mOffsetX);
            dayList.setOffsetX(mOffsetX);
        }
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
        currentYear = currentYear == 0 ? getCurrentYear() : currentYear;
        currentMonth = currentMonth == 0 ? getCurrentMonth() : currentMonth;
        currentDay = currentDay == 0 ? getCurrentDayOfMonth() : currentDay;
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

    private static int getDayOfMonth(int year, int month) {
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

    /**
     * dialog构造
     */
    public static class Builder {
        private int showMode = BOTTOM_STYLE;
        private int mOffsetX;
        private int textSize;
        private int textColor;
        private int lineColor;
        private int currentYear;
        private int currentMonth;
        private int currentDay;

        public Builder setShowMode(int mode) {
            if (mode == CENTER_STYLE || mode == BOTTOM_STYLE) {
                showMode = mode;
            }
            return this;
        }

        public Builder setOffsetX(int offsetX) {
            mOffsetX = offsetX;
            return this;
        }

        public Builder setTextSize(int size) {
            textSize = size;
            return this;
        }

        public Builder setTextColor(int color) {
            textColor = color;
            return this;
        }

        public Builder setLineColor(int color) {
            lineColor = color;
            return this;
        }

        /*public Builder setMinRange(int year, int month, int day) {
            // TODO: 2019/1/2/002 最小时间 
            minYear = year > 0 ? year : 0;
            minMonth = month > 0 && month <= 12 ? month : 0;
            minDay = day > 0 && day <= getDayOfMonth(year, month) ? day : 0;
            return this;
        }

        public Builder setMaxRange(int year, int month, int day) {
            // TODO: 2019/1/2/002 最大时间
            maxYear = year > 0 ? year : 0;
            maxMonth = month > 0 && month <= 12 ? month : 0;
            maxDay = day > 0 && day <= getDayOfMonth(year, month) ? day : 0;
            return this;
        }*/

        public Builder setCurrentDate(int year, int month, int day) {
            currentYear = year > 0 ? year : 0;
            currentMonth = month > 0 && month <= 12 ? month : 0;
            currentDay = day > 0 && day <= getDayOfMonth(year, month) ? day : 0;
            return this;
        }

        public DatePicker build() {
            DatePicker datePicker = DatePicker.newInstance();
            datePicker.setShowMode(showMode);
            datePicker.setTextColor(textColor);
            datePicker.setLineColor(lineColor);
            datePicker.setTextSize(textSize);
            datePicker.setOffsetX(mOffsetX);
            datePicker.setCurrentYear(currentYear);
            datePicker.setCurrentMonth(currentMonth);
            datePicker.setCurrentDay(currentDay);
            return datePicker;
        }
    }
}
