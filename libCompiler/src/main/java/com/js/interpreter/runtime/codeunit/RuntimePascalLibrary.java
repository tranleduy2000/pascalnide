package com.js.interpreter.runtime.codeunit;

import com.js.interpreter.codeunit.library.LibraryPascal;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class RuntimePascalLibrary extends RuntimeExecutableCodeUnit<LibraryPascal> {

    public RuntimePascalLibrary(LibraryPascal l) {
        super(l);
    }

    @Override
    public void runImpl() throws RuntimePascalException {

    }


    @Override
    public VariableContext getParentContext() {
        return null;
    }

}
