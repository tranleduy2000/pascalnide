package com.duy.pascal.frontend.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.adapters.InfoAdapter;
import com.duy.pascal.frontend.adapters.ItemInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InfoActivity extends AppCompatActivity {
    private static final String TAG = InfoActivity.class.getClass().getSimpleName();
    @BindView(R.id.appCompatImageView)
    ImageView img1;
    @BindView(R.id.appCompatImageView2)
    ImageView img2;
    @BindView(R.id.list_translate)
    RecyclerView mListTranslate;
    @BindView(R.id.list_license)
    RecyclerView mListLicense;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);
        setupActionBar();
        initContent();
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        setTitle(R.string.information);
    }

    private void initContent() {
        Log.d(TAG, "initContent: ");
        ArrayList<ItemInfo> dataTranslate = createListDataTranslate();
        InfoAdapter adapterTranslate = new InfoAdapter(this, dataTranslate);
        mListTranslate.setLayoutManager(new LinearLayoutManager(this));
        mListTranslate.setHasFixedSize(true);
        mListTranslate.setAdapter(adapterTranslate);


        InfoAdapter adapterLicense = new InfoAdapter(this, createListDataLicense());
        mListLicense.setLayoutManager(new LinearLayoutManager(this));
        mListLicense.setHasFixedSize(true);
        mListLicense.setAdapter(adapterLicense);
    }

    private ArrayList<ItemInfo> createListDataLicense() {
        ArrayList<ItemInfo> result = new ArrayList<>();
        result.add(new ItemInfo("duy", "adsdas", ""));
        result.add(new ItemInfo("duy", "adsdas", ""));
        result.add(new ItemInfo("ád", "adsdas", ""));
        result.add(new ItemInfo("dáduy", "adsdas", ""));
        result.add(new ItemInfo("dáduy", "adsdas", ""));
        result.add(new ItemInfo("dadsasduy", "adsdas", ""));
        return result;
    }

    private ArrayList<ItemInfo> createListDataTranslate() {
        Log.d(TAG, "createListDataTranslate: ");
        ArrayList<ItemInfo> result = new ArrayList<>();
        result.add(new ItemInfo("duy", "adsdas", ""));
        result.add(new ItemInfo("duy", "adsdas", ""));
        result.add(new ItemInfo("ád", "adsdas", ""));
        result.add(new ItemInfo("dáduy", "adsdas", ""));
        result.add(new ItemInfo("dáduy", "adsdas", ""));
        result.add(new ItemInfo("dadsasduy", "adsdas", ""));
        return result;
    }

}
