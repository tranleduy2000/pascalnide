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

package com.duy.pascal.interperter.declaration.lang.function;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.runtime.references.PascalPointer;
import com.duy.pascal.interperter.ast.runtime.references.PascalReference;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.types.ArgumentType;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.PointerType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.types.VarargsType;
import com.duy.pascal.interperter.declaration.lang.types.set.ArrayType;
import com.duy.pascal.interperter.declaration.lang.types.subrange.IntegerSubrangeType;
import com.duy.pascal.interperter.declaration.lang.types.util.TypeUtils;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.libraries.annotations.ArrayBoundsInfo;
import com.duy.pascal.interperter.libraries.annotations.MethodTypeData;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.ui.utils.DLog;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MethodDeclaration extends AbstractCallableFunction {
    private static final String TAG = MethodDeclaration.class.getSimpleName();
    @Nullable
    private Object mInstance;
    @NonNull
    private Method mMethod;
    private ArgumentType[] mArgCache = null;
    private String mDescription = "";
    private Type mReturnType;

    /**
     * @param owner - parent class
     * @param m     - method of class
     */
    public MethodDeclaration(@Nullable Object owner, @NonNull Method m) {
        this.mInstance = owner;
        this.mMethod = m;
    }

    public MethodDeclaration(@Nullable Object owner, @NonNull Method m, @Nullable String description) {
        this.mInstance = owner;
        this.mMethod = m;
        this.mDescription = description;
    }

    public MethodDeclaration(@Nullable Object owner, @NonNull Method m, @Nullable String description,
                             @Nullable ArrayList<String> listParams) {
        this.mInstance = owner;
        mMethod = m;
        this.mDescription = description;
    }

    private static java.lang.reflect.Type getFirstGenericType(java.lang.reflect.Type t) {
        if (!(t instanceof ParameterizedType)) {
            return Object.class;
        }
        ParameterizedType param = (ParameterizedType) t;
        java.lang.reflect.Type[] parameters = param.getActualTypeArguments();
        if (parameters.length == 0) {
            return Object.class;
        }
        return parameters[0];
    }

    private static Type convertBasicType(java.lang.reflect.Type javatype) {
        if (javatype == PascalPointer.class
                || (javatype instanceof ParameterizedType && ((ParameterizedType) javatype)
                .getRawType() == PascalPointer.class)) {
            java.lang.reflect.Type subtype = getFirstGenericType(javatype);
            return new PointerType(convertBasicType(subtype));
        }
        if (javatype instanceof GenericArrayType) {

        }
        if (javatype instanceof Class) {
            Class<?> type = (Class<?>) javatype;
            return BasicType.create(type.isPrimitive() ? TypeUtils.getClassForType(type) : type);
        } else {
            //generic type, such as List<T>
            return BasicType.create(Object.class);
        }
    }

    private static Type convertArrayType(java.lang.reflect.Type javatype, Iterator<IntegerSubrangeType> arraysizes) {
        java.lang.reflect.Type subtype;
        IntegerSubrangeType arrayinfo = null;
        boolean isArray = false;
        if (javatype instanceof GenericArrayType) {
            subtype = ((GenericArrayType) javatype).getGenericComponentType();
            isArray = true;
        } else if (javatype instanceof Class<?> && ((Class<?>) javatype).isArray()) {
            subtype = ((Class<?>) javatype).getComponentType();
            isArray = true;
        } else {
            subtype = Object.class;
        }

        if (arraysizes.hasNext()) {
            arrayinfo = arraysizes.next();
        }
        if (!isArray) {
            return convertBasicType(javatype);
        } else {
            return new ArrayType<>(convertArrayType(subtype, arraysizes), arrayinfo);
        }
    }

    private static RuntimeType convertReferenceType(java.lang.reflect.Type javatype,
                                                    Iterator<IntegerSubrangeType> arraysizes) {
        java.lang.reflect.Type subtype = javatype;
        boolean reference_argument = javatype == PascalReference.class
                || (javatype instanceof ParameterizedType && ((ParameterizedType) javatype)
                .getRawType() == PascalReference.class);
        if (reference_argument) {
            subtype = getFirstGenericType(javatype);
        }
        Type arraytype = convertArrayType(subtype, arraysizes);
        return new RuntimeType(arraytype, reference_argument);
    }

    private static RuntimeType deducePascalTypeFromJavaTypeAndAnnotations(java.lang.reflect.Type javatype,
                                                                          ArrayBoundsInfo annotation) {


        List<IntegerSubrangeType> arrayinfo = new ArrayList<>();
        if (annotation != null && annotation.starts().length > 0) {
            DLog.d(TAG, "deducePascalTypeFromJavaTypeAndAnnotations() called with: javatype = ["
                    + javatype + "], annotation = [" + annotation + "]");
            int[] starts = annotation.starts();
            int[] lengths = annotation.lengths();
            for (int i = 0; i < starts.length; i++) {
                arrayinfo.add(new IntegerSubrangeType(starts[i], lengths[i]));
            }
        }
        Iterator<IntegerSubrangeType> iterator = arrayinfo.iterator();

        return convertReferenceType(javatype, iterator);
    }

    @Override
    public Object call(VariableContext f,
                       RuntimeExecutableCodeUnit<?> main, Object[] arguments)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException,
            RuntimePascalException {
        if (mInstance instanceof RuntimeValue) {
            mInstance = ((RuntimeValue) mInstance).getValue(f, main);
        }
        return mMethod.invoke(mInstance, arguments);
    }

    @Override
    public ArgumentType[] argumentTypes() {
        if (mArgCache != null) {
            return mArgCache;
        }
        java.lang.reflect.Type[] types = mMethod.getGenericParameterTypes();
        ArgumentType[] result = new ArgumentType[types.length];
        MethodTypeData tmp = mMethod.getAnnotation(MethodTypeData.class);
        ArrayBoundsInfo[] type_data = tmp == null ? null : tmp.info();
        for (int i = 0; i < types.length; i++) {
            RuntimeType argtype = deducePascalTypeFromJavaTypeAndAnnotations(
                    types[i], type_data == null ? null : type_data[i]);
            if (i == types.length - 1 && mMethod.isVarArgs()) {
                ArrayType<?> lastArgType = (ArrayType<?>) argtype.declType;
                result[i] = new VarargsType(new RuntimeType(
                        lastArgType.elementType, argtype.writable));
            } else {
                result[i] = argtype;
            }
        }
        mArgCache = result;
        return result;
    }

    @NonNull
    @Override
    public Name getName() {
        return Name.create(mMethod.getName());
    }

    @Override
    public String getDescription() {
        return mDescription;
    }

    @Override
    public Type returnType() {
        if (mReturnType == null) {
            Class<?> result = mMethod.getReturnType();
            if (result == PascalReference.class) {
                result = (Class<?>) ((ParameterizedType) mMethod
                        .getGenericReturnType()).getActualTypeArguments()[0];
            }
            if (result.isPrimitive()) {
                result = TypeUtils.getClassForType(result);
            }
            mReturnType = BasicType.create(result);
        }
        return mReturnType;
    }

    @NonNull
    @Override
    public String getEntityType() {
        return returnType() != null ? "function" : "procedure";
    }

    @Override
    public LineInfo getLineNumber() {
        if (mInstance != null) {
            return new LineInfo(-1, mInstance.getClass().getName());
        }else {
            return LineInfo.ANONYMOUS;
        }
    }

    public void setInstance(Object instance) {
        this.mInstance = instance;
    }
}
