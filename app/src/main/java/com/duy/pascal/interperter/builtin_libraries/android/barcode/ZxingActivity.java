package com.duy.pascal.interperter.builtin_libraries.android.barcode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

public class ZxingActivity extends CaptureActivity {

    public static final String TYPE = "type";
    public static final int TYPE_BAR = 1;
    public static final int TYPE_QR = 2;
    public static final int TYPE_MATRIX = 3;
    public static final int TYPE_PRODUCT = 4;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing_api);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scanBarCode();
            }
        }, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                DLog.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String contents = result.getContents();
                Toast.makeText(this, contents, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_RETURN_RESULT, contents);
                setResult(RESULT_OK, intent);
                finish();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void scanBarCode() {
        Intent intent = getIntent();
        int type = 0;
        if (intent != null) {
            type = intent.getIntExtra(TYPE, TYPE_BAR);
        }
        IntentIntegrator integrator = new IntentIntegrator(this);
        switch (type) {
            case TYPE_BAR:
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                break;
            case TYPE_MATRIX:
                integrator.setDesiredBarcodeFormats(IntentIntegrator.DATA_MATRIX_TYPES);
                break;
            case TYPE_PRODUCT:
                integrator.setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES);
                break;
            case TYPE_QR:
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                break;
        }
        integrator.setOrientationLocked(true);
        integrator.setPrompt("Scan a barcode");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

}
