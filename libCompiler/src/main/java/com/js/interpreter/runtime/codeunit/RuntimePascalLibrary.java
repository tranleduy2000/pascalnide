package com.js.interpreter.runtime.codeunit;

import com.js.interpreter.codeunit.library.LibraryPascal;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class RuntimePascalLibrary extends RuntimeExecutableCodeUnit<LibraryPascal> {

    private static final String TAG = "RuntimePascalLibrary";

    public RuntimePascalLibrary(LibraryPascal l) {
        super(l);
    }

    /**
     * run instruction initialization
     */
    public void runInit() throws RuntimePascalException {
        LibraryPascal.LibraryExpressionContext context =
                (LibraryPascal.LibraryExpressionContext) getDefinition().getContext();
        context.getInitInstruction().execute(this, this);
    }

    /**
     * run final instruction
     */
    public void runFinal() throws RuntimePascalException {
        LibraryPascal.LibraryExpressionContext context =
                (LibraryPascal.LibraryExpressionContext) getDefinition().getContext();
        context.getFinalInstruction().execute(this, this);
    }

    @Override
    public void runImpl() throws RuntimePascalException {
        //don't working
    }

    @Override
    public VariableContext getParentContext() {
        return null;
    }


}
