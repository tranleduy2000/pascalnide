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

package com.duy.pascal.backend.ast.function_declaretion.io;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.value.boxing.ArrayBoxer;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by Duy on 09-Jun-17.
 */

public class OutputFormatter {
    public static Object[] format(ArrayBoxer args, VariableContext f,
                                  RuntimeExecutableCodeUnit main) throws RuntimePascalException {
        RuntimeValue[] runtimeValues = args.getValues();
        Object[] values = new Object[runtimeValues.length];

        //format value
        for (int i = 0; i < runtimeValues.length; i++) {
            RuntimeValue raw = runtimeValues[i];
            RuntimeValue[] outputFormat = raw.getOutputFormat();
            Object value = raw.getValue(f, main);
            StringBuilder out = new StringBuilder(value instanceof Object[] ?
                    Arrays.toString((Object[]) value) : String.valueOf(value));

            if (outputFormat != null) {
                if (outputFormat[1] != null) {
                    int sizeOfReal = (int) outputFormat[1].getValue(f, main);
                    StringBuilder round = new StringBuilder();
                    for (int j = 0; j < sizeOfReal; j++) round.append("0");
                    DecimalFormat decimalFormat = new DecimalFormat("#0." + round.toString());
                    decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
                    Double d = Double.parseDouble(out.toString());
                    out = new StringBuilder(decimalFormat.format(d));
                }

                if (outputFormat[0] != null) {
                    int column = (int) outputFormat[0].getValue(f, main);
                    while (out.length() < column) {
                        out.insert(0, " ");
                    }
                }
            }
            values[i] = out;
        }
        return values;
    }
}
