package com.duy.testapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SampleView sampleView = new SampleView(this);
        sampleView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(sampleView);
        testStack(1);
    }


    private void testStack(long depth) {
        Log.d(TAG, "testStack: " + depth);
        long[] array = new long[30000];
        for (int i = 0; i < array.length; i++) {
            array[i] = 1;
        }
        testStack(depth + 1);
    }
    //30856
    //2572  int[] array = new int[10000];
    //1543  long[] array = new long[10000];
}
