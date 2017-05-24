package com.js.interpreter.instructions.case_statement;

import com.duy.pascal.backend.linenumber.LineInfo;

class SingleValue implements CaseCondition {
    private Object value;
    private LineInfo line;

    SingleValue(Object value, LineInfo line) {
        this.value = value;
        this.line = line;
    }

    @Override
    public boolean fits(Object val) {
        if (value.equals(val)) return true;

        if (value instanceof Number && val instanceof Number) {
            if (val instanceof Double || value instanceof Double //real value
                    || val instanceof Float || value instanceof Float) {
                double v1 = ((Number) val).doubleValue();
                double v2 = ((Number) val).doubleValue();
                return v1 == v2;
            } else { //integer value
                long v1 = ((Number) value).longValue();
                long v2 = ((Number) val).longValue();
                return v1 == v2;
            }
        }
        //other type, include string, object
        return value.equals(val);
    }

    @Override
    public LineInfo getLine() {
        return line;
    }

}
