package com.js.interpreter.ast;

import com.duy.pascal.backend.lib.annotations.ArrayBoundsInfo;
import com.duy.pascal.backend.lib.annotations.MethodTypeData;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArgumentType;
import com.duy.pascal.backend.pascaltypes.ArrayType;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.pascaltypes.SubrangeType;
import com.duy.pascal.backend.pascaltypes.VarargsType;
import com.duy.pascal.frontend.debug.DebugManager;
import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
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
    private Object parent;

    private Method method;

    private ArgumentType[] argCache = null;

    public MethodDeclaration(Object owner, Method m) {
        this.parent = owner;
        method = m;
    }

    @Override
    public Object call(VariableContext parentContext,
                       RuntimeExecutable<?> main,
                       Object[] arguments)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

        DebugManager.outputMethod(main.getDebugListener(), method);

//        for (Object o: arguments){
//            System.out.println("debug + " + o.getClass().getSimpleName() + " " + o.toString());;
//        }

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

    private DeclaredType convertBasicType(Type javatype) {
        Class<?> type = (Class<?>) javatype;
        return BasicType.anew(type.isPrimitive() ? TypeUtils.getClassForType(type) : type);
    }

    private DeclaredType convertArrayType(Type javatype, Iterator<SubrangeType> arraysizes) {
        Type subtype;
        SubrangeType arrayInfo;
        if (javatype instanceof GenericArrayType) {
            subtype = ((GenericArrayType) javatype).getGenericComponentType();
            arrayInfo = new SubrangeType();
        } else if (javatype instanceof Class<?> && ((Class<?>) javatype).isArray()) {
            subtype = ((Class<?>) javatype).getComponentType();
            arrayInfo = new SubrangeType();
        } else {
            subtype = Object.class;
            arrayInfo = null;
        }

        if (arraysizes.hasNext()) {
            arrayInfo = arraysizes.next();
        }
        if (arrayInfo == null) {
            return convertBasicType(javatype);
        } else {
            return new ArrayType<>(convertArrayType(subtype, arraysizes), arrayInfo);
        }
    }

    private RuntimeType convertReferenceType(Type javatype, Iterator<SubrangeType> arraysizes) {
        Type subtype = javatype;
        boolean pointer = javatype == VariableBoxer.class ||
                (javatype instanceof ParameterizedType
                        && ((ParameterizedType) javatype).getRawType() == VariableBoxer.class);
        if (pointer) {
            subtype = getFirstGenericType(javatype);
        }
        DeclaredType arrayType = convertArrayType(subtype, arraysizes);
        return new RuntimeType(arrayType, pointer);
    }

    private RuntimeType deducePascalTypeFromJavaTypeAndAnnotations(Type javatype,
                                                                   ArrayBoundsInfo annotation) {

        List<SubrangeType> arrayinfo = new ArrayList<SubrangeType>();
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
            RuntimeType argtype = deducePascalTypeFromJavaTypeAndAnnotations(types[i], type_data == null ? null : type_data[i]);
            if (i == types.length - 1 && method.isVarArgs()) {
                ArrayType<?> lastArgType = (ArrayType<?>) argtype.declType;
                result[i] = new VarargsType(new RuntimeType(lastArgType.element_type, argtype.writable));
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
    public DeclaredType return_type() {
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
        return "plugin";
    }

    @Override
    public LineInfo getLineNumber() {
        return new LineInfo(-1, parent.getClass().getCanonicalName());
    }

}
