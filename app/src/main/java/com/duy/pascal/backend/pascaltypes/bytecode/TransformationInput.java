package com.duy.pascal.backend.pascaltypes.bytecode;

import serp.bytecode.Code;

public interface TransformationInput {
    void pushInputOnStack();

    Code getCode();

    int getFreeRegister();

    void freeRegister(int index);
}
