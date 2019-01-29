package com.hellom.picker1;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;


import com.hellom.picker.addresspicker.AddressPickerBuilder;
import com.hellom.picker.addresspicker.AddressPickerParams;
import com.hellom.picker.datepicker.DatePickerBuilder;
import com.hellom.picker.datepicker.DatePickerParams;
import com.hellom.picker.base.PickerConstant;
import com.hellom.picker.addresspicker.AddressPicker;
import com.hellom.picker.datepicker.DatePicker;
import com.hellom.picker.R;
import com.hellom.picker.base.WheelView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WheelView picker = findViewById(R.id.wheel);
        List<String> one = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            one.add(String.valueOf(i));
        }
        picker.setData(one);
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                showDatePicker();
                break;
            case R.id.btn_2:
                showAddressPicker();
                break;
            default:
                break;
        }
    }

    private void showDatePicker() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            DatePicker datePicker = new DatePickerBuilder(new DatePickerParams())
                    .setAlignMode(PickerConstant.CENTER_ALIGN_MODE)
                    .setBtnStyle(PickerConstant.TOP_BTN_STYLE)
                    .setCircle(false)
                    .setLeftText("取消")
                    .setRightText("确定")
                    .setLeftTextColor(0xff000000)
                    .setRightTextColor(0xff000000)
                    .setLeftTextSize(16)
                    .setRightTextSize(16)
                    .setLineColor(0xffadadad)
                    .setOffsetX(-16)
                    .setShowMode(PickerConstant.BOTTOM_STYLE)
                    .setShowSize(5)
                    .setTitle("选择日期")
                    .setTitleTextColor(0xff666666)
                    .setTitleTextSize(20)
                    .setVelocityRate(0.7f)
                    .setCurrentDate(1111, 11, 11)
                    .setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
                        @Override
                        public void onDateSelected(String year, String month, String day) {
                            Toast.makeText(MainActivity.this, year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
                        }
                    }).build();
            datePicker.show(fragmentManager, "datePicker");
        }
    }

    private void showAddressPicker() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            AddressPicker addressPicker = new AddressPickerBuilder(this, new AddressPickerParams())
                    .setAlignMode(PickerConstant.LEFT_ALIGN_MODE)
                    .setBtnStyle(PickerConstant.BOTTOM_BTN_STYLE)
                    .setCircle(true)
                    .setLeftText("取消")
                    .setRightText("确定")
                    .setLeftTextColor(0xff000000)
                    .setRightTextColor(0xff000000)
                    .setLeftTextSize(16)
                    .setRightTextSize(16)
                    .setLineColor(0xffadadad)
                    .setOffsetX(-16)
                    .setShowMode(PickerConstant.BOTTOM_STYLE)
                    .setShowSize(5)
                    .setVelocityRate(0.7f)
                    .setLeftBtnBackgroundColor(Color.GREEN)
                    .setRightBtnBackgroundColor(Color.BLUE)
                    .setCurrentAddress("湖北", "襄阳市", "枣阳市")
                    .setOnAddressSelectedListener(new AddressPicker.OnAddressSelectedListener() {
                        @Override
                        public void onAddressSelected(String year, String month, String day) {
                            Toast.makeText(MainActivity.this, year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
                        }
                    }).build();
            addressPicker.show(fragmentManager, "addressPicker");
        }
    }
}
