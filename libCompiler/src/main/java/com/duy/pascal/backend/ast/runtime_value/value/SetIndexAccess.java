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

package com.duy.pascal.backend.ast.runtime_value.value;

import com.duy.pascal.backend.debugable.DebuggableAssignableValue;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.pascaltypes.set.SetType;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.ast.runtime_value.references.Reference;
import com.duy.pascal.backend.ast.runtime_value.references.SetPreference;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;

import java.util.LinkedList;

/**
 * Created by Duy on 25-May-17.
 */

public class SetIndexAccess extends DebuggableAssignableValue {
    private RuntimeValue container;
    private RuntimeValue index;

    public SetIndexAccess(RuntimeValue container, RuntimeValue index) {
        this.container = container;
        this.index = index;
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        RuntimeType r = (container.getType(f));
        return new RuntimeType(((SetType<?>) r.declType).getElementType(), r.writable);
    }

    @Override
    public LineInfo getLineNumber() {
        return index.getLineNumber();
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        LinkedList cont = (LinkedList) container.compileTimeValue(context);
        Integer ind = (Integer) index.compileTimeValue(context);
        if (ind == null || cont == null) {
            return null;
        } else {
            return cont.get(ind);
        }
    }


    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        LinkedList cont = (LinkedList) container.getValue(f, main);
        Integer ind = (Integer) index.getValue(f, main);
        if (ind == null || cont == null) {
            return null;
        } else {
            return cont.get(ind); //index out of bound
        }

    }

    @Override
    public Reference<?> getReferenceImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        LinkedList cont = (LinkedList) container.getValue(f, main);
        int ind = Integer.valueOf(index.getValue(f, main).toString());
        return new SetPreference(cont, ind);
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new SetIndexAccess(container.compileTimeExpressionFold(context),
                index.compileTimeExpressionFold(context));
    }
}
