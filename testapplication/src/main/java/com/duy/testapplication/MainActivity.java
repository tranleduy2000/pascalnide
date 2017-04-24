package com.duy.testapplication;

import android.os.Bundle;
import android.view.View;

import com.googlecode.android_scripting.activity.Main;

public class MainActivity extends Main implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {

    }
}
