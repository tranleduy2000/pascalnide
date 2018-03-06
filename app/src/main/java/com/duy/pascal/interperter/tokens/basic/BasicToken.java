package com.duy.pascal.interperter.tokens.basic;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.tokens.Token;

import java.io.Serializable;

public abstract class BasicToken extends Token implements Serializable, Cloneable {

    public BasicToken(@NonNull LineNumber line) {
        super(line);
        if (lineNumber != null) {
            this.lineNumber.setLength(toString().length());
        }
    }

    public abstract String toString();

}
