package com.hellom.picker.addresspicker;

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
import com.hellom.picker.addresspicker.bean.Province;
import com.hellom.picker.addresspicker.db.manager.AddressDictManager;
import com.hellom.picker.baseview.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * author:helloM
 * email:1694327880@qq.com
 */
public class AddressPicker extends DialogFragment implements View.OnClickListener {

    private WheelView provinceList, cityList, areaList;
    private int currentProvince, currentCity, currentArea;
    private AddressDictManager mAddressDictManager;

    private AddressPicker.OnAddressSelectedListener mOnAddressSelectedListener;

    public void setOnAddressSelectedListener(OnAddressSelectedListener mOnAddressSelectedListener) {
        this.mOnAddressSelectedListener = mOnAddressSelectedListener;
    }

    public static AddressPicker newInstance() {
        Bundle args = new Bundle();
        AddressPicker fragment = new AddressPicker();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initStyle();

        View view = inflater.inflate(R.layout.dialog_fragment_address_picker, container, false);
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
        mAddressDictManager = new AddressDictManager(getActivity());
        provinceList = view.findViewById(R.id.province_picker);
        cityList = view.findViewById(R.id.city_picker);
        areaList = view.findViewById(R.id.area_picker);
    }

    private void initListener(View view) {
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
        view.findViewById(R.id.tv_sure).setOnClickListener(this);
        provinceList.setOnSelectedChangedListener(new WheelView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged() {
                try {
                    //currentYear = Integer.parseInt(yearList.getSelectedItemData());
                    //dayList.setData(initDayData(currentYear, currentMonth));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        cityList.setOnSelectedChangedListener(new WheelView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged() {
                try {
                    //currentMonth = Integer.parseInt(monthList.getSelectedItemData());
                    //dayList.setData(initDayData(currentYear, currentMonth));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        areaList.setOnSelectedChangedListener(new WheelView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged() {
                try {
                    //currentDay = Integer.parseInt(dayList.getSelectedItemData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initData() {
        List<String> data = new ArrayList<>();
        List<Province> provinces = mAddressDictManager.getProvinceList();
        for (int i = 0; i < provinces.size(); i++) {
            data.add(provinces.get(i).name);
        }
        provinceList.setData(data);
        //cityList.setData(initMonthData());
        //areaList.setData(initDayData(currentYear, currentMonth));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_cancel) {
            dismiss();
        } else if (id == R.id.tv_sure) {
            if (mOnAddressSelectedListener != null) {
                mOnAddressSelectedListener.onAddressSelected(provinceList.getSelectedItemData(), cityList.getSelectedItemData(), areaList.getSelectedItemData());
            }
            dismiss();
        }
    }

    public interface OnAddressSelectedListener {
        /**
         * 日期选择回调
         *
         * @param province 省
         * @param city     市
         * @param area     区
         */
        void onAddressSelected(String province, String city, String area);
    }
}
