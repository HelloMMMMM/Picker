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
 * @author mx
 */
public class DatePicker extends DialogFragment implements View.OnClickListener {

    private WheelView yearList, monthList, dayList;
    private int currentYear, currentMonth, currentDay;
    private List<String> yearData, monthData, dayData;

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
                Log.e("mx", "data:" + yearList.getSelectedItemData());
            }
        });
        monthList.setOnSelectedChangedListener(new WheelView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged() {
                Log.e("mx", "data:" + monthList.getSelectedItemData());
                dayData.add(String.valueOf(dayData.size() + 1));
                dayList.setData(dayData);
            }
        });
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH) + 1;
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        yearData = new ArrayList<>();
        for (int i = currentYear - 100; i < currentYear + 100; i++) {
            yearData.add(String.valueOf(i));
        }
        monthData = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            monthData.add(String.valueOf(i));
        }
        dayData = new ArrayList<>();
        calendar.set(currentYear, currentMonth, 0);
        for (int i = 1; i <= calendar.get(Calendar.DAY_OF_MONTH); i++) {
            dayData.add(String.valueOf(i));
        }
        yearList.setData(yearData);
        monthList.setData(monthData);
        dayList.setData(dayData);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_cancel) {
            dismiss();
        } else if (id == R.id.tv_sure) {
            dismiss();
        }
    }
}
