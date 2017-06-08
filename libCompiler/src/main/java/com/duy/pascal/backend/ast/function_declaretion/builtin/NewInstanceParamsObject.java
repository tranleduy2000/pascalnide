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

package com.duy.pascal.backend.ast.function_declaretion.builtin;


import android.support.annotation.NonNull;

import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.data_types.ArgumentType;
import com.duy.pascal.backend.data_types.BasicType;
import com.duy.pascal.backend.data_types.DeclaredType;
import com.duy.pascal.backend.data_types.JavaClassBasedType;
import com.duy.pascal.backend.data_types.RuntimeType;
import com.duy.pascal.backend.data_types.VarargsType;
import com.duy.pascal.backend.data_types.converter.TypeConverter;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.instructions.FieldReference;
import com.duy.pascal.backend.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class NewInstanceParamsObject implements IMethodDeclaration {

    private ArgumentType[] argumentTypes =
            {new RuntimeType(new JavaClassBasedType(Object.class), true),
                    new VarargsType(new RuntimeType(BasicType.create(Object.class), false))};

    @Override
   public String getName() {
        return "new".toLowerCase();
    }

    @Override
    public FunctionCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                                      ExpressionContext f) throws ParsingException {
        RuntimeValue pointer = arguments[0];
        return new InstanceObjectCall(pointer, arguments[1], line);
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineInfo line, RuntimeValue[] values, ExpressionContext f) throws ParsingException {
        return generateCall(line, values, f);
    }

    @Override
    public ArgumentType[] argumentTypes() {
        return argumentTypes;
    }

    @Override
    public DeclaredType returnType() {
        return new JavaClassBasedType(Object.class);
    }

    @Override
    public String description() {
        return null;
    }

    private class InstanceObjectCall extends FunctionCall {

        private RuntimeValue pointer;
        private RuntimeValue listArg;
        private LineInfo line;

        InstanceObjectCall(RuntimeValue pointer, RuntimeValue listArg, LineInfo line) {
            this.pointer = pointer;
            this.listArg = listArg;
            this.line = line;
        }

        @Override
        public RuntimeType getType(ExpressionContext f) throws ParsingException {
            return new RuntimeType(new JavaClassBasedType(Object.class), false);

        }

        @Override
        public LineInfo getLineNumber() {
            return line;
        }


        @Override
        public Object compileTimeValue(CompileTimeContext context) {
            return null;
        }

        @Override
        public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
                throws ParsingException {
            return new InstanceObjectCall(pointer, listArg, line);
        }

        @Override
        public void setLineNumber(LineInfo lineNumber) {

        }

        @Override
        public Executable compileTimeConstantTransform(CompileTimeContext c)
                throws ParsingException {
            return new InstanceObjectCall(pointer, listArg, line);
        }

        @Override
        protected String getFunctionName() {
            return "new";
        }

        @Override
        public Object getValueImpl(@NonNull VariableContext f, @NonNull RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {
            //get references of variable
            FieldReference pointer = (FieldReference) this.pointer.getValue(f, main);
            RuntimeType type = pointer.getType();

            //get class type of variable
            JavaClassBasedType javaType = (JavaClassBasedType) type.declType;

            Class<?> clazz = javaType.getStorageClass();
            Constructor<?>[] constructors = clazz.getConstructors();

            Object[] targetObjects = (Object[]) listArg.getValue(f, main);
            Object[] convertedObjects = new Object[targetObjects.length];
            for (Constructor<?> constructor : constructors) {
                Type[] parameterTypes = constructor.getGenericParameterTypes();
                if (TypeConverter.autoConvert(targetObjects, convertedObjects, parameterTypes)) {
                    try {
                        Object newObject = constructor.newInstance(convertedObjects);
                        pointer.set(newObject);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }


}
