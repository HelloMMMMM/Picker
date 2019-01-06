package com.hellom.picker.datepicker;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
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
    private static Params params;

    /**
     * 显示位置
     */
    public static final int BOTTOM_STYLE = 1;
    public static final int CENTER_STYLE = 2;
    /**
     * 年份半数范围
     */
    private static final int YEAR_HALF_RANGE = 150;

    public static Builder builder() {
        return new Builder();
    }

    private static DatePicker newInstance(Params p) {
        params = p;
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
        if (params.showMode == BOTTOM_STYLE) {
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
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            if (params.showMode == BOTTOM_STYLE) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                layoutParams.gravity = Gravity.BOTTOM;
            } else if (params.showMode == CENTER_STYLE) {
                layoutParams.gravity = Gravity.CENTER;
            }
        }
    }

    private void initSize() {
        Window window = getDialog().getWindow();
        if (window != null) {
            if (params.showMode == BOTTOM_STYLE) {
                window.setLayout(-1, -2);
            } else if (params.showMode == CENTER_STYLE) {
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                window.setLayout((int) (displayMetrics.widthPixels * 0.9f), -2);
            }
        }
    }

    private void initView(View view) {
        yearList = view.findViewById(R.id.year_picker);
        monthList = view.findViewById(R.id.month_picker);
        dayList = view.findViewById(R.id.day_picker);
        yearList.setTextColor(params.textColor);
        monthList.setTextColor(params.textColor);
        dayList.setTextColor(params.textColor);
        yearList.setTextSize(params.textSize);
        monthList.setTextSize(params.textSize);
        dayList.setTextSize(params.textSize);
        yearList.setLineColor(params.lineColor);
        monthList.setLineColor(params.lineColor);
        dayList.setLineColor(params.lineColor);
        yearList.setOffsetX(params.mOffsetX);
        monthList.setOffsetX(params.mOffsetX);
        dayList.setOffsetX(params.mOffsetX);
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
        currentYear = params.currentYear == 0 ? getCurrentYear() : params.currentYear;
        currentMonth = params.currentMonth == 0 ? getCurrentMonth() : params.currentMonth;
        currentDay = params.currentDay == 0 ? getCurrentDayOfMonth() : params.currentDay;
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
            if (params.mOnDateSelectedListener != null) {
                params.mOnDateSelectedListener.onDateSelected(yearList.getSelectedItemData(), monthList.getSelectedItemData(), dayList.getSelectedItemData());
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
     * 建造者
     */
    public static class Builder {
        private Params p;

        private Builder() {
            p = new Params();
        }

        public Builder setShowMode(int mode) {
            if (mode == CENTER_STYLE || mode == BOTTOM_STYLE) {
                p.showMode = mode;
            }
            return this;
        }

        public Builder setOffsetX(int offsetX) {
            p.mOffsetX = offsetX;
            return this;
        }

        public Builder setTextSize(int size) {
            p.textSize = size;
            return this;
        }

        public Builder setTextColor(int color) {
            p.textColor = color;
            return this;
        }

        public Builder setLineColor(int color) {
            p.lineColor = color;
            return this;
        }

        public Builder setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
            p.mOnDateSelectedListener = onDateSelectedListener;
            return this;
        }

        public Builder setCurrentDate(int year, int month, int day) {
            p.currentYear = year;
            p.currentMonth = month;
            p.currentDay = day;
            return this;
        }

        public DatePicker build() {
            return DatePicker.newInstance(p);
        }
    }

    /**
     * 可定义参数
     */
    private static class Params {
        /**
         * 显示位置
         */
        private int showMode = BOTTOM_STYLE;
        /**
         * 偏移(负左偏,正右偏)
         */
        private int mOffsetX = 0;
        /**
         * 文字相关
         */
        private int textSize = 16;
        private int textColor = 0xFF333333;
        /**
         * 分割线颜色
         */
        private int lineColor = 0xFFffffff;
        /**
         * 日期选择监听
         */
        private OnDateSelectedListener mOnDateSelectedListener;
        /**
         * 初始时间
         */
        private int currentYear, currentMonth, currentDay;

        private int btnTextSize;

        private int leftBtnTextColor;

        private int rightBtnTextColor;
    }
}
