package com.hellom.wheelview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ycuwq.datepicker.date.DatePickerDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WheelView picker = findViewById(R.id.wheel);
        List<String> one = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            one.add(String.valueOf(i));
        }
        picker.setLevelOneData(one);

        findViewById(R.id.open).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
        datePickerDialogFragment.show(getFragmentManager(), "DatePickerDialogFragment");
    }
}
