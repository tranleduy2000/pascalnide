/*
 * Copyright (C) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.googlecode.sl4a.facade;

import android.app.Activity;
import android.content.Intent;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.libraries.PascalLibrary;
import com.duy.pascal.interperter.libraries.android.AndroidLibraryManager;
import com.duy.pascal.interperter.libraries.annotations.PascalMethod;
import com.duy.pascal.interperter.libraries.annotations.PascalParameter;
import com.googlecode.sl4a.Constants;

import java.io.Serializable;
import java.util.Map;

/**
 * Allows you to return results to a startActivityForResult call.
 *
 * @author Alexey Reznichenko (alexey.reznichenko@gmail.com)
 */
@SuppressWarnings("unused")
public class ActivityResultFacade extends PascalLibrary {

    private static final String sRpcDescription =
            "Sets the result of a script execution. Whenever the script APK is called via "
                    + "startActivityForResult(), the resulting intent will contain " + Constants.EXTRA_RESULT
                    + " extra with the given value.";
    private static final String sCodeDescription =
            "The result code to propagate back to the originating activity, often RESULT_CANCELED (0) "
                    + "or RESULT_OK (-1)";

    private Activity mActivity = null;
    private Intent mResult = null;
    private int mResultCode;

    public ActivityResultFacade() {

    }

    public ActivityResultFacade(AndroidLibraryManager manager) {
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = sRpcDescription)
    public synchronized void setResultBoolean(
            @PascalParameter(name = "resultCode", description = sCodeDescription) Integer resultCode,
            @PascalParameter(name = "resultValue") Boolean resultValue) {
        mResult = new Intent();
        mResult.putExtra(Constants.EXTRA_RESULT, resultValue.booleanValue());
        mResultCode = resultCode;
        if (mActivity != null) {
            setResult();
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = sRpcDescription)
    public synchronized void setResultByte(
            @PascalParameter(name = "resultCode", description = sCodeDescription) Integer resultCode,
            @PascalParameter(name = "resultValue") Byte resultValue) {
        mResult = new Intent();
        mResult.putExtra(Constants.EXTRA_RESULT, resultValue.byteValue());
        mResultCode = resultCode;
        if (mActivity != null) {
            setResult();
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = sRpcDescription)
    public synchronized void setResultShort(
            @PascalParameter(name = "resultCode", description = sCodeDescription) Integer resultCode,
            @PascalParameter(name = "resultValue") Short resultValue) {
        mResult = new Intent();
        mResult.putExtra(Constants.EXTRA_RESULT, resultValue.shortValue());
        mResultCode = resultCode;
        if (mActivity != null) {
            setResult();
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = sRpcDescription)
    public synchronized void setResultChar(
            @PascalParameter(name = "resultCode", description = sCodeDescription) Integer resultCode,
            @PascalParameter(name = "resultValue") Character resultValue) {
        mResult = new Intent();
        mResult.putExtra(Constants.EXTRA_RESULT, resultValue.charValue());
        mResultCode = resultCode;
        if (mActivity != null) {
            setResult();
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = sRpcDescription)
    public synchronized void setResultInteger(
            @PascalParameter(name = "resultCode", description = sCodeDescription) Integer resultCode,
            @PascalParameter(name = "resultValue") Integer resultValue) {
        mResult = new Intent();
        mResult.putExtra(Constants.EXTRA_RESULT, resultValue.intValue());
        mResultCode = resultCode;
        if (mActivity != null) {
            setResult();
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = sRpcDescription)
    public synchronized void setResultLong(
            @PascalParameter(name = "resultCode", description = sCodeDescription) Integer resultCode,
            @PascalParameter(name = "resultValue") Long resultValue) {
        mResult = new Intent();
        mResult.putExtra(Constants.EXTRA_RESULT, resultValue.longValue());
        mResultCode = resultCode;
        if (mActivity != null) {
            setResult();
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = sRpcDescription)
    public synchronized void setResultFloat(
            @PascalParameter(name = "resultCode", description = sCodeDescription) Integer resultCode,
            @PascalParameter(name = "resultValue") Float resultValue) {
        mResult = new Intent();
        mResult.putExtra(Constants.EXTRA_RESULT, resultValue.floatValue());
        mResultCode = resultCode;
        if (mActivity != null) {
            setResult();
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = sRpcDescription)
    public synchronized void setResultDouble(
            @PascalParameter(name = "resultCode", description = sCodeDescription) Integer resultCode,
            @PascalParameter(name = "resultValue") Double resultValue) {
        mResult = new Intent();
        mResult.putExtra(Constants.EXTRA_RESULT, resultValue.doubleValue());
        mResultCode = resultCode;
        if (mActivity != null) {
            setResult();
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = sRpcDescription)
    public synchronized void setResultString(
            @PascalParameter(name = "resultCode", description = sCodeDescription) Integer resultCode,
            @PascalParameter(name = "resultValue") String resultValue) {
        mResult = new Intent();
        mResult.putExtra(Constants.EXTRA_RESULT, resultValue);
        mResultCode = resultCode;
        if (mActivity != null) {
            setResult();
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = sRpcDescription)
    public synchronized void setResultBooleanArray(
            @PascalParameter(name = "resultCode", description = sCodeDescription) Integer resultCode,
            @PascalParameter(name = "resultValue") Boolean[] resultValue) {
        mResult = new Intent();
        boolean[] array = new boolean[resultValue.length];
        for (int i = 0; i < resultValue.length; i++) {
            array[i] = resultValue[i];
        }
        mResult.putExtra(Constants.EXTRA_RESULT, array);
        mResultCode = resultCode;
        if (mActivity != null) {
            setResult();
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = sRpcDescription)
    public synchronized void setResultByteArray(
            @PascalParameter(name = "resultCode", description = sCodeDescription) Integer resultCode,
            @PascalParameter(name = "resultValue") Byte[] resultValue) {
        mResult = new Intent();
        byte[] array = new byte[resultValue.length];
        for (int i = 0; i < resultValue.length; i++) {
            array[i] = resultValue[i];
        }
        mResult.putExtra(Constants.EXTRA_RESULT, array);
        mResultCode = resultCode;
        if (mActivity != null) {
            setResult();
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = sRpcDescription)
    public synchronized void setResultShortArray(
            @PascalParameter(name = "resultCode", description = sCodeDescription) Integer resultCode,
            @PascalParameter(name = "resultValue") Short[] resultValue) {
        mResult = new Intent();
        short[] array = new short[resultValue.length];
        for (int i = 0; i < resultValue.length; i++) {
            array[i] = resultValue[i];
        }
        mResult.putExtra(Constants.EXTRA_RESULT, array);
        mResultCode = resultCode;
        if (mActivity != null) {
            setResult();
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = sRpcDescription)
    public synchronized void setResultCharArray(
            @PascalParameter(name = "resultCode", description = sCodeDescription) Integer resultCode,
            @PascalParameter(name = "resultValue") Character[] resultValue) {
        mResult = new Intent();
        char[] array = new char[resultValue.length];
        for (int i = 0; i < resultValue.length; i++) {
            array[i] = resultValue[i];
        }
        mResult.putExtra(Constants.EXTRA_RESULT, array);
        mResultCode = resultCode;
        if (mActivity != null) {
            setResult();
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = sRpcDescription)
    public synchronized void setResultIntegerArray(
            @PascalParameter(name = "resultCode", description = sCodeDescription) Integer resultCode,
            @PascalParameter(name = "resultValue") Integer[] resultValue) {
        mResult = new Intent();
        int[] array = new int[resultValue.length];
        for (int i = 0; i < resultValue.length; i++) {
            array[i] = resultValue[i];
        }
        mResult.putExtra(Constants.EXTRA_RESULT, array);
        mResultCode = resultCode;
        if (mActivity != null) {
            setResult();
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = sRpcDescription)
    public synchronized void setResultLongArray(
            @PascalParameter(name = "resultCode", description = sCodeDescription) Integer resultCode,
            @PascalParameter(name = "resultValue") Long[] resultValue) {
        mResult = new Intent();
        long[] array = new long[resultValue.length];
        for (int i = 0; i < resultValue.length; i++) {
            array[i] = resultValue[i];
        }
        mResult.putExtra(Constants.EXTRA_RESULT, array);
        mResultCode = resultCode;
        if (mActivity != null) {
            setResult();
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = sRpcDescription)
    public synchronized void setResultFloatArray(
            @PascalParameter(name = "resultCode", description = sCodeDescription) Integer resultCode,
            @PascalParameter(name = "resultValue") Float[] resultValue) {
        mResult = new Intent();
        float[] array = new float[resultValue.length];
        for (int i = 0; i < resultValue.length; i++) {
            array[i] = resultValue[i];
        }
        mResult.putExtra(Constants.EXTRA_RESULT, array);
        mResultCode = resultCode;
        if (mActivity != null) {
            setResult();
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = sRpcDescription)
    public synchronized void setResultDoubleArray(
            @PascalParameter(name = "resultCode", description = sCodeDescription) Integer resultCode,
            @PascalParameter(name = "resultValue") Double[] resultValue) {
        mResult = new Intent();
        double[] array = new double[resultValue.length];
        for (int i = 0; i < resultValue.length; i++) {
            array[i] = resultValue[i];
        }
        mResult.putExtra(Constants.EXTRA_RESULT, array);
        mResultCode = resultCode;
        if (mActivity != null) {
            setResult();
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = sRpcDescription)
    public synchronized void setResultStringArray(
            @PascalParameter(name = "resultCode", description = sCodeDescription) Integer resultCode,
            @PascalParameter(name = "resultValue") String[] resultValue) {
        mResult = new Intent();
        mResult.putExtra(Constants.EXTRA_RESULT, resultValue);
        mResultCode = resultCode;
        if (mActivity != null) {
            setResult();
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = sRpcDescription)
    public synchronized void setResultSerializable(
            @PascalParameter(name = "resultCode", description = sCodeDescription) Integer resultCode,
            @PascalParameter(name = "resultValue") Serializable resultValue) {
        mResult = new Intent();
        mResult.putExtra(Constants.EXTRA_RESULT, resultValue);
        mResultCode = resultCode;
        if (mActivity != null) {
            setResult();
        }
    }

    @SuppressWarnings("unused")
    public synchronized void setActivity(Activity activity) {
        mActivity = activity;
        if (mResult != null) {
            setResult();
        }
    }

    private void setResult() {
        mActivity.setResult(mResultCode, mResult);
        mActivity.finish();
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
    public String getName() {
        return null;
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
