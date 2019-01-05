package com.hellom.picker1;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;


import com.hellom.picker.addresspicker.AddressPicker;
import com.hellom.picker.datepicker.DatePicker;
import com.hellom.picker.R;
import com.hellom.picker.baseview.WheelView;

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
            DatePicker datePicker = (DatePicker) fragmentManager.findFragmentByTag("datePicker");
            if (datePicker == null) {
                datePicker = DatePicker.builder()
                        .setLineColor(Color.BLUE).setTextSize(18).setTextColor(Color.BLUE).setOffsetX(-16).setShowMode(DatePicker.BOTTOM_STYLE)
                        .setCurrentDate(1111, 11, 11)
                        .setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
                            @Override
                            public void onDateSelected(String year, String month, String day) {
                                Toast.makeText(MainActivity.this, year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
                            }
                        }).build();
            }
            datePicker.show(fragmentManager, "datePicker");
        }
    }

    private void showAddressPicker() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            AddressPicker addressPicker = (AddressPicker) fragmentManager.findFragmentByTag("addressPicker");
            if (addressPicker == null) {
                addressPicker = AddressPicker.builder().setOffsetX(-16).setLineColor(Color.GREEN)
                        .setTextColor(Color.GREEN).setShowMode(AddressPicker.BOTTOM_STYLE).setTextSize(18)
                        .setOnAddressSelectedListener(new AddressPicker.OnAddressSelectedListener() {
                            @Override
                            public void onAddressSelected(String year, String month, String day) {
                                Toast.makeText(MainActivity.this, year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
                            }
                        }).build();
            }
            addressPicker.show(fragmentManager, "addressPicker");
        }
    }
}
