package com.hellom.wheelview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WheelView picker = findViewById(R.id.wheel);
        picker.setLevel(WheelView.LEVEL_THREE);
        List<String> one = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            one.add("轰趴馆");
            one.add("台球");
            one.add("密室逃脱");
            one.add("卡丁车");
            one.add("桌游");
            one.add("真人CS");
            one.add("DIY");
        }
        List<String> two = new ArrayList<>(one);
        List<String> three = new ArrayList<>(one);
        picker.setLevelOneData(one);
        picker.setLevelTwoData(two);
        picker.setLevelThreeData(three);
    }
}
