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

package com.duy.pascal.backend.function_declaretion;


import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.function_declaretion.abstract_class.IMethodDeclaration;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArgumentType;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.JavaClassBasedType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.pascaltypes.VarargsType;
import com.duy.pascal.backend.pascaltypes.typeconversion.TypeConverter;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.returnsvalue.FunctionCall;
import com.js.interpreter.ast.returnsvalue.ReturnValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class NewInstanceParamsObject implements IMethodDeclaration {

    private ArgumentType[] argumentTypes =
            {new RuntimeType(new JavaClassBasedType(Object.class), false),
                    new VarargsType(new RuntimeType(BasicType.create(Object.class), false))};

    @Override
    public String name() {
        return "newInstance".toLowerCase();
    }

    @Override
    public FunctionCall generateCall(LineInfo line, ReturnValue[] arguments,
                                     ExpressionContext f) throws ParsingException {
        ReturnValue clazz = arguments[0];
        JavaClassBasedType type = (JavaClassBasedType) clazz.getType(f).declType;
        return new InstanceObjectCall(clazz, type, arguments[1], line);
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineInfo line, ReturnValue[] values, ExpressionContext f) throws ParsingException {
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

        private ReturnValue mClass;
        private JavaClassBasedType type;
        private ReturnValue listArg;
        private LineInfo line;

        InstanceObjectCall(ReturnValue clazz, JavaClassBasedType type, ReturnValue listArg, LineInfo line) {
            this.mClass = clazz;
            this.type = type;
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
        public ReturnValue compileTimeExpressionFold(CompileTimeContext context)
                throws ParsingException {
            return new InstanceObjectCall(mClass, type, listArg, line);
        }

        @Override
        public Executable compileTimeConstantTransform(CompileTimeContext c)
                throws ParsingException {
            return new InstanceObjectCall(mClass, type, listArg, line);
        }

        @Override
        protected String getFunctionName() {
            return "newInstance";
        }

        @Override
        public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
                throws RuntimePascalException {
            Object[] targetObjects = (Object[]) listArg.getValue(f, main);
            Class<?> clazz = type.getStorageClass();
            Constructor<?>[] constructors = clazz.getConstructors();

            Object[] convertedObjects = new Object[targetObjects.length];
            for (Constructor<?> constructor : constructors) {
                Type[] parameterTypes = constructor.getGenericParameterTypes();
                if (TypeConverter.autoConvert(targetObjects, convertedObjects, parameterTypes)) {
                    try {
                        return constructor.newInstance(convertedObjects);
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
