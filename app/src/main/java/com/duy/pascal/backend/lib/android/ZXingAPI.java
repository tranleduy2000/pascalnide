package com.duy.pascal.backend.lib.android;

import android.content.Intent;

import com.duy.pascal.backend.lib.PascalLibrary;
import com.duy.pascal.backend.lib.android.temp.AndroidUtilsLib;
import com.duy.pascal.backend.lib.android.utils.AndroidLibraryManager;
import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.util.Map;

/**
 * Created by Duy on 01-May-17.
 */

public class ZXingAPI implements PascalLibrary {
    public static final String NAME = "barCode".toLowerCase();
    private static final String PACKAGE = "com.duy.pascal.plugin.zxing";
    private Intent barcodeIntent;
    private AndroidUtilsLib mAndroidFacade;

    public ZXingAPI(AndroidLibraryManager manager) throws RuntimePascalException {
        mAndroidFacade = new AndroidUtilsLib(manager);
        //invoke method will be
        if (manager.getContext() != null) {
            barcodeIntent = manager.getContext().getPackageManager().getLaunchIntentForPackage(PACKAGE);
            if (barcodeIntent == null) {
                throw new RuntimePascalException("Can not find plugin BarCode, please install plugin from GooglePlayStore");
            }
        }
    }

    @PascalMethod(description = "scan bar code")
    public StringBuilder scanBarCode() {
        // Run the activity an retrieve the result.
        final Intent data = mAndroidFacade.startActivityForResult(barcodeIntent);
        if (data.hasExtra(Intent.EXTRA_RETURN_RESULT)) {
            return new StringBuilder(data.getStringExtra(Intent.EXTRA_RETURN_RESULT));
        }
        return new StringBuilder("");
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    @PascalMethod(description = "stop")

    public void shutdown() {
    }
}
