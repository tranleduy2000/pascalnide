package com.js.interpreter.ast;

import android.support.annotation.NonNull;

import com.duy.pascal.backend.exceptions.StackOverflowException;
import com.duy.pascal.backend.lib.annotations.ArrayBoundsInfo;
import com.duy.pascal.backend.lib.annotations.MethodTypeData;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArgumentType;
import com.duy.pascal.backend.pascaltypes.ArrayType;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.pascaltypes.VarargsType;
import com.duy.pascal.backend.pascaltypes.rangetype.IntegerSubrangeType;
import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.ScriptTerminatedException;
import com.ncsa.common.util.TypeUtils;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MethodDeclaration extends AbstractCallableFunction {
    private static final String TAG = MethodDeclaration.class.getSimpleName();
    private Object parent;
    private Method method;
    private ArgumentType[] argCache = null;

    public MethodDeclaration(@NonNull Object owner, @NonNull Method m) {
        this.parent = owner;
        method = m;
    }

    @Override
    public Object call(VariableContext parentContext,
                       RuntimeExecutable<?> main, Object[] arguments)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException,
            StackOverflowException, ScriptTerminatedException {
        return method.invoke(parent, arguments);
    }

    private Type getFirstGenericType(Type t) {
        if (!(t instanceof ParameterizedType)) {
            return Object.class;
        }
        ParameterizedType param = (ParameterizedType) t;
        Type[] parameters = param.getActualTypeArguments();
        if (parameters.length == 0) {
            return Object.class;
        }
        return parameters[0];
    }

    private DeclaredType convertBasicType(Type javaType) {
        Class<?> type = (Class<?>) javaType;
        return BasicType.anew(type.isPrimitive() ? TypeUtils.getClassForType(type) : type);
    }

    private DeclaredType convertArrayType(Type javaType, Iterator<IntegerSubrangeType> arraySize) {
        Type subtype;
        IntegerSubrangeType arrayInfo;
        if (javaType instanceof GenericArrayType) {
            subtype = ((GenericArrayType) javaType).getGenericComponentType();
            arrayInfo = new IntegerSubrangeType();
        } else if (javaType instanceof Class<?> && ((Class<?>) javaType).isArray()) {
            subtype = ((Class<?>) javaType).getComponentType();
            arrayInfo = new IntegerSubrangeType();
        } else {
            subtype = Object.class;
            arrayInfo = null;
        }

        if (arraySize.hasNext()) {
            arrayInfo = arraySize.next();
        }
        if (arrayInfo == null) {
            return convertBasicType(javaType);
        } else {
            return new ArrayType<>(convertArrayType(subtype, arraySize), arrayInfo);
        }
    }


    private RuntimeType deducePascalTypeFromJavaTypeAndAnnotations(Type javaType,
                                                                   ArrayBoundsInfo annotation) {

        List<IntegerSubrangeType> arrayInfo = new ArrayList<>();
        if (annotation != null && annotation.starts().length > 0) {
            int[] starts = annotation.starts();
            int[] lengths = annotation.lengths();
            for (int i = 0; i < starts.length; i++) {
                arrayInfo.add(new IntegerSubrangeType(starts[i], lengths[i]));
            }
        }
        Iterator<IntegerSubrangeType> iterator = arrayInfo.iterator();
        return convertReferenceType(javaType, iterator);
    }

    private RuntimeType convertReferenceType(Type javaType, Iterator<IntegerSubrangeType> arraySize) {
        Type subtype = javaType;
        boolean pointer = javaType == VariableBoxer.class ||
                (javaType instanceof ParameterizedType &&
                        ((ParameterizedType) javaType).getRawType() == VariableBoxer.class);
        if (pointer) {
            subtype = getFirstGenericType(javaType);
        }
        DeclaredType arrayType = convertArrayType(subtype, arraySize);
        return new RuntimeType(arrayType, pointer);
    }

    @Override
    public ArgumentType[] getArgumentTypes() {
        if (argCache != null) {
            return argCache;
        }
        Type[] types = method.getGenericParameterTypes();
        ArgumentType[] args = new ArgumentType[types.length];
        MethodTypeData methodTypeData = method.getAnnotation(MethodTypeData.class);
        ArrayBoundsInfo[] typeData = methodTypeData == null ? null : methodTypeData.info();

        for (int i = 0; i < types.length; i++) {
            RuntimeType argType = deducePascalTypeFromJavaTypeAndAnnotations(types[i],
                    typeData == null ? null : typeData[i]);
            if (i == types.length - 1 && method.isVarArgs()) {
                ArrayType<?> lastArgType = (ArrayType<?>) argType.declaredType;
                args[i] = new VarargsType(new RuntimeType(lastArgType.elementType, argType.writable));
            } else {
                args[i] = argType;
            }
        }
        argCache = args;
        return args;
    }

    @Override
    public String name() {
        return method.getName();
    }

    @Override
    public DeclaredType returnType() {
        Class<?> result = method.getReturnType();
        if (result == VariableBoxer.class) {
            result = (Class<?>) ((ParameterizedType) method.getGenericReturnType())
                    .getActualTypeArguments()[0];
        }
        if (result.isPrimitive()) {
            result = TypeUtils.getClassForType(result);
        }
        return BasicType.anew(result);
    }

    @Override
    public String getEntityType() {
        return "function";
    }

    @Override
    public LineInfo getLine() {
        return new LineInfo(-1, parent.getClass().getCanonicalName());
    }

}
