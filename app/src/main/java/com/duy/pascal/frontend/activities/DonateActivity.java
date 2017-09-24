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

package com.duy.pascal.frontend.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.duy.pascal.frontend.BuildConfig;
import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import aidl.util.IabBroadcastReceiver;
import aidl.util.IabHelper;
import aidl.util.IabResult;
import aidl.util.Inventory;
import aidl.util.Purchase;

/**
 * Created by Duy on 11-Aug-17.
 */

public class DonateActivity extends BaseActivity implements IabBroadcastReceiver.IabBroadcastListener, View.OnClickListener {
    public static final int REQUEST_DONATE = 10001;
    private static final String SKU_DONATE_ONE = "java_nide_donate_one_dollar";
    private static final String SKU_DONATE_TWO = "java_nide_donate_two_dollar";
    private static final String SKU_DONATE_THREE = "java_nide_donate_three_dollar";
    private static final String SKU_DONATE_FOUR = "java_nide_donate_four_dollar";
    private static final String SKU_DONATE_FIVE = "java_nide_donate_five_dollar";
    private static final String TAG = "DonateActivity";
    private Handler mHandler = new Handler();
    private IabHelper mIabHelper;
    private IabBroadcastReceiver mBroadcastReceiver;
    private IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inv) {
            if (mIabHelper == null) return;
            if (result.isFailure()) {
                iabError(new RuntimeException("Failed to query inventory: " + result));
            }
            DLog.d(TAG, "Query inventory was successful. ");
            if (inv != null) {
                Purchase premiumPurchase = inv.getPurchase(SKU_DONATE_ONE);
                DLog.d(TAG, "onQueryInventoryFinished: " + premiumPurchase);
                boolean success = premiumPurchase != null && verifyDeveloperPayload(premiumPurchase);
                if (success) {
                    Toast.makeText(DonateActivity.this, "Thank for donate me", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    private aidl.util.IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase info) {
            if (mIabHelper == null) return;

            if (result.isFailure()) {
                iabError(new RuntimeException("Error purchasing: " + result));
                return;
            }

            if (!verifyDeveloperPayload(info)) {
                iabError(new RuntimeException("Error purchasing. Authenticity verification failed."));
                return;
            }
            DLog.d(TAG, "Purchase successful.");
            if (info.getSku().equals(SKU_DONATE_ONE)) {
                Toast.makeText(DonateActivity.this, "Thank for donate me", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private boolean verifyDeveloperPayload(Purchase premiumPurchase) {
        String developerPayload = premiumPurchase.getDeveloperPayload();
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        setupToolbar();
        setTitle(R.string.donate_for_project);
        findViewById(R.id.btn_donate_1).setOnClickListener(this);
        findViewById(R.id.btn_donate_2).setOnClickListener(this);
        findViewById(R.id.btn_donate_3).setOnClickListener(this);
        findViewById(R.id.btn_donate_4).setOnClickListener(this);
        findViewById(R.id.btn_donate_5).setOnClickListener(this);

        initIab();
    }

    private void initIab() {
        String base64Key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh+uvYlgvcAOOi9fTL8AUN7KDGq" +
                "j3diAZ+5V6d5Ovl7QJNMuDJJpUEsMHzj/fpd/NO7k2gevNLGwX1JKM5BhWLDq/FHB8xTHT1yalZ69fhPPN" +
                "tqCENtdtOTFj4owfzgCSW2TL2gBWzh/JpyBXH9rYyKsgcm5SNg5zA2uCp7Sb5yQDSLFCIiEkrriDPQS0GR" +
                "cIp60qi62NtmHIEht39hcBiTeFAkyJ1pDdweaiNkcQowR04YOQt/NqF4X6peBs+5a4jzZ6UQWvl/YZ+yxz" +
                "8Zf6T2dvskgrY6xCuwflWhPkMOqRAZXL4lVgywenm1NIydgf/ROThnJ7Dj1Li+8R07JarQIDAQAB";
        mIabHelper = new IabHelper(this, base64Key);
        mIabHelper.enableDebugLogging(BuildConfig.DEBUG);
        mIabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    iabError(null);
                    return;
                }
                if (mIabHelper == null) return;

                mBroadcastReceiver = new IabBroadcastReceiver(DonateActivity.this);
                IntentFilter intentFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, intentFilter);
                DLog.d(TAG, "onIabSetupFinished: setup success");

                try {
                    mIabHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (Exception e) {
                    iabError(e);
                }
            }
        });

    }

    private void iabError(Exception e) {
        if (e != null) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mIabHelper.handleActivityResult(requestCode, resultCode, data)) {

        } else {
            DLog.d(TAG, "onActivityResult handled by IABUtil.");
        }

    }

    private void purchase(String sku) {
        FirebaseAnalytics.getInstance(this).logEvent("purchase" + sku, new Bundle());
        String payload = "";
        try {
            mIabHelper.launchPurchaseFlow(this, sku, REQUEST_DONATE, mPurchaseFinishedListener, payload);
        } catch (Exception e) {
            iabError(e);
        }
    }

    @Override
    public void receivedBroadcast() {
        try {
            mIabHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            iabError(e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DLog.d(TAG, "onDestroy() called");

        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
        try {
            if (mIabHelper != null) {
                mIabHelper.disposeWhenFinished();
                mIabHelper = null;
            }
        } catch (Exception e) {
            //iab unregister
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_donate_1:
                purchase(SKU_DONATE_ONE);
                break;
            case R.id.btn_donate_2:
                purchase(SKU_DONATE_TWO);
                break;
            case R.id.btn_donate_3:
                purchase(SKU_DONATE_THREE);
                break;
            case R.id.btn_donate_4:
                purchase(SKU_DONATE_FOUR);
                break;
            case R.id.btn_donate_5:
                purchase(SKU_DONATE_FIVE);
                break;
        }
    }
}
