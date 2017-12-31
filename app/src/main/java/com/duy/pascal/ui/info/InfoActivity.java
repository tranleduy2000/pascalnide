/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.ui.info;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.duy.pascal.ui.R;
import com.duy.pascal.ui.purchase.activities.InAppPurchaseActivity;

import java.util.ArrayList;

public class InfoActivity extends InAppPurchaseActivity implements View.OnClickListener {
    private static final String TAG = InfoActivity.class.getClass().getSimpleName();
    private RecyclerView mListTranslate;
    private RecyclerView mListLicense;
    private Toolbar mToolbar;

    @Override
    public void updateUiPremium() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        mListTranslate = findViewById(R.id.list_translate);
        mListLicense = findViewById(R.id.list_license);

        setupToolbar();
        initContent();

        findViewById(R.id.btn_upgrade).setOnClickListener(this);
    }

    protected void setupToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(R.string.information);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initContent() {
        new TaskLoadData().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_upgrade:
                showDialogUpgrade();
                break;
        }
    }


    private class TaskLoadData extends AsyncTask<Void, Void, Void> {
        private ArrayList<ItemInfo> dataTranslate;
        private ArrayList<ItemInfo> dataLicense;


        @Override
        protected Void doInBackground(Void... params) {
            dataTranslate = InfoAppUtil.readListTranslate(getResources().openRawResource(R.raw.help_translate));
            dataLicense = InfoAppUtil.readListLicense(getResources().openRawResource(R.raw.license));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            HelpTranslateAdapter adapterTranslate = new HelpTranslateAdapter(InfoActivity.this, dataTranslate);
            mListTranslate.setLayoutManager(new LinearLayoutManager(InfoActivity.this));
            mListTranslate.setHasFixedSize(false);
            mListTranslate.setAdapter(adapterTranslate);
            mListTranslate.setNestedScrollingEnabled(false);

            LicenseAdapter adapterLicense = new LicenseAdapter(InfoActivity.this, dataLicense);
            mListLicense.setLayoutManager(new LinearLayoutManager(InfoActivity.this));
            mListLicense.setHasFixedSize(false);
            mListLicense.setAdapter(adapterLicense);
            mListLicense.addItemDecoration(new DividerItemDecoration(InfoActivity.this, DividerItemDecoration.VERTICAL));
            mListLicense.setNestedScrollingEnabled(false);
        }
    }

}
