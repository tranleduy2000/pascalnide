package com.duy.pascal.backend.ast.instructions.case_statement;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.rangetype.Containable;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;

class SingleValue implements Containable {
    private Object mValue;
    private LineInfo line;

    SingleValue(Object value, LineInfo line) {
        this.mValue = value;
        this.line = line;
    }

    @Override
    public boolean contain(VariableContext f, RuntimeExecutableCodeUnit<?> main, Object value) throws RuntimePascalException {
        if (value.equals(mValue)) return true;

        if (value instanceof Number && mValue instanceof Number) {
            if (mValue instanceof Double || value instanceof Double //real mValue
                    || mValue instanceof Float || value instanceof Float) {
                double v1 = ((Number) mValue).doubleValue();
                double v2 = ((Number) mValue).doubleValue();
                return v1 == v2;
            } else { //integer mValue
                long v1 = ((Number) value).longValue();
                long v2 = ((Number) mValue).longValue();
                return v1 == v2;
            }
        }
        //other type, include string, object
        return value.equals(mValue);
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }
}
