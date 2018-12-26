package com.hellom.wheelview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WheelView picker = findViewById(R.id.wheel);
        for (int i = 0; i < 10; i++) {
            picker.addData("轰趴馆");
            picker.addData("台球");
            picker.addData("密室逃脱");
            picker.addData("卡丁车");
            picker.addData("桌游");
            picker.addData("真人CS");
            picker.addData("DIY");
        }
        //picker.setCenterItem(4);
    }
}
