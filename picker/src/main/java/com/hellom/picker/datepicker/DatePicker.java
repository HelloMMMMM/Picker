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
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;

import com.hellom.picker.DatePickerParams;
import com.hellom.picker.R;
import com.hellom.picker.baseview.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.hellom.picker.PickerConstant.*;


/**
 * author:helloM
 * email:1694327880@qq.com
 */
public class DatePicker extends DialogFragment implements View.OnClickListener {

    private WheelView yearList, monthList, dayList;
    private int currentYear, currentMonth, currentDay;
    private static DatePickerParams params;

    private ViewStub bottomBtn;

    /**
     * 年份半数范围
     */
    private static final int YEAR_HALF_RANGE = 150;

    public static DatePicker newInstance(DatePickerParams p) {
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
        if (params.getShowMode() == BOTTOM_STYLE) {
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
            if (params.getShowMode() == BOTTOM_STYLE) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                layoutParams.gravity = Gravity.BOTTOM;
            } else if (params.getShowMode() == CENTER_STYLE) {
                layoutParams.gravity = Gravity.CENTER;
            }
        }
    }

    private void initSize() {
        Window window = getDialog().getWindow();
        if (window != null) {
            if (params.getShowMode() == BOTTOM_STYLE) {
                window.setLayout(-1, -2);
            } else if (params.getShowMode() == CENTER_STYLE) {
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                window.setLayout((int) (displayMetrics.widthPixels * 0.9f), -2);
            }
        }
    }

    private void initView(View view) {
        ViewStub viewStub = view.findViewById(R.id.top_btn);
        viewStub.inflate();
        yearList = view.findViewById(R.id.year_picker);
        monthList = view.findViewById(R.id.month_picker);
        dayList = view.findViewById(R.id.day_picker);
        yearList.setTextColor(params.getTextColor());
        monthList.setTextColor(params.getTextColor());
        dayList.setTextColor(params.getTextColor());
        yearList.setTextSize(params.getTextSize());
        monthList.setTextSize(params.getTextSize());
        dayList.setTextSize(params.getTextSize());
        yearList.setLineColor(params.getLineColor());
        monthList.setLineColor(params.getLineColor());
        dayList.setLineColor(params.getLineColor());
        yearList.setOffsetX(params.getOffsetX());
        monthList.setOffsetX(params.getOffsetX());
        dayList.setOffsetX(params.getOffsetX());
        bottomBtn = view.findViewById(R.id.bottom_btn);
        bottomBtn.inflate();
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
        currentYear = params.getCurrentYear() == 0 ? getCurrentYear() : params.getCurrentYear();
        currentMonth = params.getCurrentMonth() == 0 ? getCurrentMonth() : params.getCurrentMonth();
        currentDay = params.getCurrentDay() == 0 ? getCurrentDayOfMonth() : params.getCurrentDay();
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
            if (params.getmOnDateSelectedListener() != null) {
                params.getmOnDateSelectedListener().onDateSelected(yearList.getSelectedItemData(), monthList.getSelectedItemData(), dayList.getSelectedItemData());
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
}
