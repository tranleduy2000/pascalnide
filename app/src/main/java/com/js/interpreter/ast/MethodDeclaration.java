package com.js.interpreter.ast;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.lib.annotations.ArrayBoundsInfo;
import com.duy.pascal.backend.lib.annotations.MethodTypeData;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArgumentType;
import com.duy.pascal.backend.pascaltypes.ArrayType;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.PointerType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.pascaltypes.VarargsType;
import com.duy.pascal.backend.pascaltypes.rangetype.SubrangeType;
import com.js.interpreter.ast.returnsvalue.ReturnValue;
import com.js.interpreter.runtime.PascalPointer;
import com.js.interpreter.runtime.PascalReference;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
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
    private Object owner;
    private Method method;
    private ArgumentType[] argCache = null;
    private String description = "";
    private ArrayList<String> listParams;

    public MethodDeclaration(@NonNull Object owner, @NonNull Method m) {
        this.owner = owner;
        method = m;
    }


    public MethodDeclaration(@NonNull Object owner, @NonNull Method m, @Nullable String description) {
        this.owner = owner;
        this.method = m;
        this.description = description;
    }

    public MethodDeclaration(@NonNull Object owner, @NonNull Method m, @Nullable String description,
                             @Nullable ArrayList<String> listParams) {
        this.owner = owner;
        method = m;
        this.description = description;
        this.listParams = listParams;
    }

    @Override
    public Object call(VariableContext parentContext,
                       RuntimeExecutable<?> main, Object[] arguments)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException,
            RuntimePascalException {
        if (owner instanceof ReturnValue){
            owner = ((ReturnValue) owner).getValue(parentContext, main);
        }
        return method.invoke(owner, arguments);
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

    private DeclaredType convertBasicType(Type javatype) {
        if (javatype == PascalPointer.class
                || (javatype instanceof ParameterizedType && ((ParameterizedType) javatype)
                .getRawType() == PascalPointer.class)) {
            Type subtype = getFirstGenericType(javatype);
            return new PointerType(convertBasicType(subtype));
        }

        Class<?> type = (Class<?>) javatype;
        return BasicType.create(type.isPrimitive() ? TypeUtils.getClassForType(type) : type);
    }

    private DeclaredType convertArrayType(Type javatype,
                                          Iterator<SubrangeType> arraysizes) {
        Type subtype;
        SubrangeType arrayinfo;
        if (javatype instanceof GenericArrayType) {
            subtype = ((GenericArrayType) javatype).getGenericComponentType();
            arrayinfo = new SubrangeType();
        } else if (javatype instanceof Class<?>
                && ((Class<?>) javatype).isArray()) {
            subtype = ((Class<?>) javatype).getComponentType();
            arrayinfo = new SubrangeType();
        } else {
            subtype = Object.class;
            arrayinfo = null;
        }

        if (arraysizes.hasNext()) {
            arrayinfo = arraysizes.next();
        }
        if (arrayinfo == null) {
            return convertBasicType(javatype);
        } else {
            return new ArrayType<>(convertArrayType(subtype,
                    arraysizes), arrayinfo);
        }
    }

    private RuntimeType convertReferenceType(Type javatype,
                                             Iterator<SubrangeType> arraysizes) {
        Type subtype = javatype;
        boolean reference_argument = javatype == PascalReference.class
                || (javatype instanceof ParameterizedType && ((ParameterizedType) javatype)
                .getRawType() == PascalReference.class);
        if (reference_argument) {
            subtype = getFirstGenericType(javatype);
        }
        DeclaredType arraytype = convertArrayType(subtype, arraysizes);
        return new RuntimeType(arraytype, reference_argument);
    }

    private RuntimeType deducePascalTypeFromJavaTypeAndAnnotations(Type javatype,
                                                                   ArrayBoundsInfo annotation) {

        List<SubrangeType> arrayinfo = new ArrayList<>();
        if (annotation != null && annotation.starts().length > 0) {
            int[] starts = annotation.starts();
            int[] lengths = annotation.lengths();
            for (int i = 0; i < starts.length; i++) {
                arrayinfo.add(new SubrangeType(starts[i], lengths[i]));
            }
        }
        Iterator<SubrangeType> iterator = arrayinfo.iterator();

        return convertReferenceType(javatype, iterator);
    }

    @Override
    public ArgumentType[] argumentTypes() {
        if (argCache != null) {
            return argCache;
        }
        Type[] types = method.getGenericParameterTypes();
        ArgumentType[] result = new ArgumentType[types.length];
        MethodTypeData tmp = method.getAnnotation(MethodTypeData.class);
        ArrayBoundsInfo[] type_data = tmp == null ? null : tmp.info();
        for (int i = 0; i < types.length; i++) {
            RuntimeType argtype = deducePascalTypeFromJavaTypeAndAnnotations(
                    types[i], type_data == null ? null : type_data[i]);
            if (i == types.length - 1 && method.isVarArgs()) {
                ArrayType<?> lastArgType = (ArrayType<?>) argtype.declType;
                result[i] = new VarargsType(new RuntimeType(
                        lastArgType.element_type, argtype.writable));
            } else {
                result[i] = argtype;
            }
        }
        argCache = result;
        return result;
    }

    @Override
    public String name() {
        return method.getName();
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public DeclaredType returnType() {
        Class<?> result = method.getReturnType();
        if (result == PascalReference.class) {
            result = (Class<?>) ((ParameterizedType) method
                    .getGenericReturnType()).getActualTypeArguments()[0];
        }
        if (result.isPrimitive()) {
            result = TypeUtils.getClassForType(result);
        }
        return BasicType.create(result);
    }

    @Override
    public String getEntityType() {
        return "function";
    }

    @Override
    public LineInfo getLineNumber() {
        return new LineInfo(-1, owner.getClass().getCanonicalName());
    }

}
