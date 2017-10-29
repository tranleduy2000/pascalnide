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

package com.duy.pascal.interperter.systemfunction.builtin;


import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.instructions.Node;
import com.duy.pascal.interperter.ast.instructions.FieldReference;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.declaration.lang.types.ArgumentType;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.types.JavaClassBasedType;
import com.duy.pascal.interperter.declaration.lang.types.PointerType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.VarargsType;
import com.duy.pascal.interperter.declaration.lang.types.converter.TypeConverter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class NewInstanceParamsObject implements IMethodDeclaration {
    private ArgumentType[] argumentTypes =
            {new RuntimeType(new JavaClassBasedType(Object.class), true),
                    new VarargsType(new RuntimeType(BasicType.create(Object.class), false))};

    @Override
     public Name getName() {
        return Name.create("New");

    }

    @Override
    public FunctionCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws Exception {
        RuntimeValue pointer = arguments[0];
        return new InstanceObjectCall(pointer, pointer.getRuntimeType(f), arguments[1], line);
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineInfo line, RuntimeValue[] values, ExpressionContext f) throws Exception {
        return generateCall(line, values, f);
    }

    @Override
    public ArgumentType[] argumentTypes() {
        return argumentTypes;
    }

    @Override
    public Type returnType() {
        return new JavaClassBasedType(Object.class);
    }

    @Override
    public String description() {
        return null;
    }

    private class InstanceObjectCall extends BuiltinFunctionCall {

        private RuntimeValue pointer;
        private RuntimeType runtimeType;
        private RuntimeValue listArg;
        private LineInfo line;

        InstanceObjectCall(RuntimeValue pointer, RuntimeType runtimeType, RuntimeValue listArg, LineInfo line) {
            this.pointer = pointer;
            this.runtimeType = runtimeType;
            this.listArg = listArg;
            this.line = line;
        }

        @Override
        public RuntimeType getRuntimeType(ExpressionContext f) throws Exception {
            return new RuntimeType(new JavaClassBasedType(Object.class), false);

        }

        @NonNull
        @Override
        public LineInfo getLineNumber() {
            return line;
        }

        @Override
        public void setLineNumber(LineInfo lineNumber) {

        }

        @Override
        public Object compileTimeValue(CompileTimeContext context) {
            return null;
        }

        @Override
        public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
                throws Exception {
            return new InstanceObjectCall(pointer, runtimeType, listArg, line);
        }

        @Override
        public Node compileTimeConstantTransform(CompileTimeContext c)
                throws Exception {
            return new InstanceObjectCall(pointer, runtimeType, listArg, line);
        }

        @Override
        protected String getFunctionNameImpl() {
            return "new";
        }

        @Override
        public Object getValueImpl(@NonNull VariableContext f, @NonNull RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {
            //get references of variable
            FieldReference pointer = (FieldReference) this.pointer.getValue(f, main);

            //get class type of variable
            JavaClassBasedType javaType = (JavaClassBasedType) ((PointerType) runtimeType.declType).pointedToType;

            Class<?> clazz = javaType.getStorageClass();
            Constructor<?>[] constructors = clazz.getConstructors();

            Object[] targetObjects = (Object[]) listArg.getValue(f, main);
            Object[] convertedObjects = new Object[targetObjects.length];
            for (Constructor<?> constructor : constructors) {
                java.lang.reflect.Type[] parameterTypes = constructor.getGenericParameterTypes();
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
