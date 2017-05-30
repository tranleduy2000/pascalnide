package com.duy.pascal.backend.builtin_libraries.android.barcode;

import android.content.Context;
import android.content.Intent;

import com.duy.pascal.backend.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.backend.builtin_libraries.PascalLibrary;
import com.duy.pascal.backend.builtin_libraries.android.AndroidLibraryManager;
import com.duy.pascal.backend.builtin_libraries.android.temp.AndroidUtilsLib;
import com.duy.pascal.backend.builtin_libraries.annotations.PascalMethod;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

import java.util.Map;

/**
 * Created by Duy on 01-May-17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class ZXingAPI implements PascalLibrary {
    public static final String NAME = "barCode".toLowerCase();
    private AndroidUtilsLib mAndroidFacade;
    private Context context;

    public ZXingAPI(AndroidLibraryManager manager) throws RuntimePascalException {
        mAndroidFacade = new AndroidUtilsLib(manager);
        if (manager.getContext() != null) {
            this.context = manager.getContext();
        }
    }

    @PascalMethod(description = "Scan bar code")
    public StringBuilder scanBarCode() {
        final Intent intent = new Intent(context, ZxingActivity.class);
        intent.putExtra(ZxingActivity.TYPE, ZxingActivity.TYPE_BAR);
        // Run the activity an retrieve the result.
        final Intent data = mAndroidFacade.startActivityForResult(intent);
        if (data != null) {
            return new StringBuilder(data.getStringExtra(Intent.EXTRA_RETURN_RESULT));
        }
        return new StringBuilder("");
    }

    @PascalMethod(description = "Scan QR code")
    public StringBuilder scanQRCode() {
        final Intent intent = new Intent(context, ZxingActivity.class);
        intent.putExtra(ZxingActivity.TYPE, ZxingActivity.TYPE_QR);
        // Run the activity an retrieve the result.
        final Intent data = mAndroidFacade.startActivityForResult(intent);
        if (data != null) {
            return new StringBuilder(data.getStringExtra(Intent.EXTRA_RETURN_RESULT));
        }
        return new StringBuilder("");
    }

    @PascalMethod(description = "Scan matrix code")
    public StringBuilder scanMatrixCode() {
        final Intent intent = new Intent(context, ZxingActivity.class);
        intent.putExtra(ZxingActivity.TYPE, ZxingActivity.TYPE_MATRIX);
        // Run the activity an retrieve the result.
        final Intent data = mAndroidFacade.startActivityForResult(intent);
        if (data != null) {
            return new StringBuilder(data.getStringExtra(Intent.EXTRA_RETURN_RESULT));
        }
        return new StringBuilder("");
    }

    @PascalMethod(description = "Scan product code")
    public StringBuilder scanProductCode() {
        final Intent intent = new Intent(context, ZxingActivity.class);
        intent.putExtra(ZxingActivity.TYPE, ZxingActivity.TYPE_PRODUCT);
        // Run the activity an retrieve the result.
        final Intent data = mAndroidFacade.startActivityForResult(intent);
        if (data != null) {
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

    @Override
    public void declareConstants(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareTypes(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareVariables(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin parentContext) {

    }
}
