package com.duy.pascal.interperter.exceptions.runtime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spanned;

import com.duy.pascal.ui.R;

import static com.duy.pascal.ui.code.ExceptionManager.formatMessageFromResource;

/**
 * Created by Duy on 07-Apr-17.
 */


public class InvalidNumericFormatException extends RuntimePascalException {
    public InvalidNumericFormatException(String s) {
        super(s);
    }

    @Override
    public Spanned getFormattedMessage(@NonNull Context context) {
        return formatMessageFromResource(this, context, R.string.InvalidNumericFormatException);

    }
}
