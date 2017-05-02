package example.zxing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends AppCompatActivity {

    private EditText editData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editData = (EditText) findViewById(R.id.editText);
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
                editData.setText(contents);
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void scanBarCode(View view) {
        new IntentIntegrator(this).setOrientationLocked(false)
                .setBarcodeImageEnabled(true)
                .setCaptureActivity(CustomScannerActivity.class).initiateScan();
    }


    public void doAccept(View view) {
        Intent result = getIntent();
        result.putExtra(Intent.EXTRA_RETURN_RESULT, editData.getText().toString());
        setResult(RESULT_OK, result);
        finish();
    }
}
