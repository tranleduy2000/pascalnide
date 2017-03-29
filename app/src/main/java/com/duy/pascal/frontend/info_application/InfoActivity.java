package com.duy.pascal.frontend.info_application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.duy.pascal.frontend.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InfoActivity extends AppCompatActivity {
    private static final String TAG = InfoActivity.class.getClass().getSimpleName();
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initContent() {
        Log.d(TAG, "initContent: ");
        ArrayList<ItemInfo> dataTranslate = InfoAppUtil.readListTranslate(getResources().openRawResource(R.raw.help_translate));

        HelpTranslateAdapter adapterTranslate = new HelpTranslateAdapter(this, dataTranslate);
        mListTranslate.setLayoutManager(new LinearLayoutManager(this));
        mListTranslate.setHasFixedSize(true);
        mListTranslate.setAdapter(adapterTranslate);
//        mListTranslate.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        HelpTranslateAdapter adapterLicense = new HelpTranslateAdapter(this, createListDataLicense());
        mListLicense.setLayoutManager(new LinearLayoutManager(this));
        mListLicense.setHasFixedSize(true);
//        mListLicense.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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


}
