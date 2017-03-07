package com.duy.interpreter.pascaltypes.bytecode;

import serp.bytecode.Code;

public interface TransformationInput {
    public void pushInputOnStack();

    public Code getCode();

    public int getFreeRegister();

    public void freeRegister(int index);
}
