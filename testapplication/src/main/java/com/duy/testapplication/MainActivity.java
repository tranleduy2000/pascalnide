package com.duy.testapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.googlecode.sl4a.facade.FacadeConfiguration;
import com.googlecode.sl4a.facade.FacadeManager;
import com.googlecode.sl4a.rpc.MethodDescriptor;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacadeManager facadeManager = new FacadeManager(FacadeConfiguration.getSdkLevel(),
                getApplicationContext(), null, FacadeConfiguration.getFacadeClasses());
        MethodDescriptor makeToast = facadeManager.getMethodDescriptor("makeToast");
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("hello android");
        try {
            makeToast.invoke(facadeManager, jsonArray);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

    }
}
