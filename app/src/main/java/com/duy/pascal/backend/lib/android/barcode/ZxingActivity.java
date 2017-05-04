package com.duy.pascal.backend.lib.android.barcode;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.activities.AbstractAppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ZxingActivity extends AbstractAppCompatActivity {

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
                Log.d("MainActivity", "Cancelled scan");
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
        new IntentIntegrator(this).setOrientationLocked(false)
                .setBarcodeImageEnabled(true)
                .setCameraId(Camera.CameraInfo.CAMERA_FACING_BACK)
                .setCaptureActivity(ZxingScannerActivity.class).initiateScan();
    }

}
