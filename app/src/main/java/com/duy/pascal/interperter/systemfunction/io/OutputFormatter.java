/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.interperter.systemfunction.io;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.runtime.value.RecordValue;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime.value.boxing.ArrayBoxer;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by Duy on 09-Jun-17.
 */

public class OutputFormatter {
    private static final String TAG = "OutputFormatter";

    public static Object[] format(ArrayBoxer args, VariableContext f,
                                  RuntimeExecutableCodeUnit main) throws RuntimePascalException {
        RuntimeValue[] runtimeValues = args.getValues();
        Object[] values = new Object[runtimeValues.length];

        //format value
        for (int i = 0; i < runtimeValues.length; i++) {
            RuntimeValue raw = runtimeValues[i];
            Object value = raw.getValue(f, main);
            StringBuilder out = new StringBuilder(getValueOutput(value));
            values[i] = out;
        }
        return values;
    }

    public static String getValueOutput(Object value) {
        if (value instanceof Object[]) {
            return Arrays.toString((Object[]) value);
        }
        if (value instanceof RecordValue) {
            Set<Map.Entry<Name, Object>> entries = ((RecordValue) value).getVariableMap().entrySet();
            StringBuilder res = new StringBuilder();
            for (Map.Entry<Name, Object> entry : entries) {
                res.append(entry.getValue()).append("\n");
            }
            return res.toString();
        }
        if (value instanceof Number) {
            if (value.equals(((Number) value).longValue())) {
                return String.valueOf(((Number) value).longValue());
            }
        }
        return String.valueOf(value);
    }

    public static StringBuilder formatDecimal(int decimal, Object value) {
        StringBuilder out = new StringBuilder(value instanceof Object[] ?
                Arrays.toString((Object[]) value) : String.valueOf(value));

        StringBuilder pattern = new StringBuilder("#0.");
        for (int j = 0; j < decimal; j++) {
            pattern.append("0");
        }

        DecimalFormat decimalFormat = new DecimalFormat(pattern.toString());
        decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));

        Double d = Double.parseDouble(out.toString());
//        if (d - d.longValue() == 0.d) {
//            return new StringBuilder(String.valueOf(d.longValue()));
//        } else {
        return new StringBuilder(decimalFormat.format(d));
//        }
    }
}
