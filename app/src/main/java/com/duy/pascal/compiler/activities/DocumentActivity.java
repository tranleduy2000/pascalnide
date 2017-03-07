package com.duy.pascal.compiler.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.duy.pascal.compiler.R;

/**
 * Created by Duy on 27-Feb-17.
 */

public class DocumentActivity extends AbstractAppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document);
    }
}
