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
                    .setCurrentDate(1111, 11, 11)
                    .setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
                        @Override
                        public void onDateSelected(String year, String month, String day) {
                            Toast.makeText(MainActivity.this, year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setLineColor(Color.BLUE).setTextSize(18).setTextColor(Color.BLUE).setOffsetX(-16).setShowMode(PickerConstant.BOTTOM_STYLE)
                    .setBtnStyle(PickerConstant.TOP_BTN_STYLE).setTitle("选择日期")
                    .build();
            datePicker.show(fragmentManager, "datePicker");
        }
    }

    private void showAddressPicker() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            AddressPicker addressPicker = new AddressPickerBuilder(this, new AddressPickerParams())
                    .setOffsetX(-16).setLineColor(Color.GREEN)
                    .setTextColor(Color.GREEN).setShowMode(PickerConstant.BOTTOM_STYLE).setTextSize(18)
                    .setCurrentAddress("湖北", "襄阳市", "枣阳市")
                    .setOnAddressSelectedListener(new AddressPicker.OnAddressSelectedListener() {
                        @Override
                        public void onAddressSelected(String year, String month, String day) {
                            Toast.makeText(MainActivity.this, year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
                        }
                    }).setCircle(true).build();
            addressPicker.show(fragmentManager, "addressPicker");
        }
    }
}
