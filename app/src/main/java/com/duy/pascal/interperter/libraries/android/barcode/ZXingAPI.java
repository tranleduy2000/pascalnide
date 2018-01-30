package com.duy.pascal.interperter.libraries.android.barcode;

import android.content.Context;
import android.content.Intent;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.libraries.PascalLibrary;
import com.duy.pascal.interperter.libraries.android.AndroidLibraryManager;
import com.duy.pascal.interperter.libraries.android.temp.AndroidUtilsLib;
import com.duy.pascal.interperter.libraries.annotations.PascalMethod;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;

/**
 * Created by Duy on 01-May-17.
 */
@SuppressWarnings("unused")
public class ZXingAPI extends PascalLibrary {
    public static final String NAME = "barCode".toLowerCase();
    private AndroidUtilsLib mAndroidFacade;
    private Context context;

    public ZXingAPI() {

    }

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
    @PascalMethod(description = "stop")
    public void onFinalize() {
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void declareConstants(ExpressionContextMixin context) {

    }

    @Override
    public void declareTypes(ExpressionContextMixin context) {

    }

    @Override
    public void declareVariables(ExpressionContextMixin context) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin context) {

    }
}
