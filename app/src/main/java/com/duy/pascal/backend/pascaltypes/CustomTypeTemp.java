package com.duy.pascal.backend.pascaltypes;

import com.android.dx.DexMaker;
import com.android.dx.TypeId;
import com.duy.pascal.backend.exceptions.NonArrayIndexed;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.pascaltypes.bytecode.RegisterAllocator;
import com.duy.pascal.backend.pascaltypes.bytecode.ScopedRegisterAllocator;
import com.duy.pascal.backend.pascaltypes.bytecode.SimpleRegisterAllocator;
import com.duy.pascal.backend.pascaltypes.bytecode.TransformationInput;
import com.duy.pascal.backend.pascaltypes.rangetype.IntegerSubrangeType;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.ast.returnsvalue.cloning.CloneableObjectCloner;
import com.js.interpreter.runtime.variables.ContainsVariables;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import serp.bytecode.BCClass;
import serp.bytecode.BCField;
import serp.bytecode.BCMethod;
import serp.bytecode.Code;
import serp.bytecode.Instruction;
import serp.bytecode.JumpInstruction;

public class CustomTypeTemp extends ObjectType {

    /**
     * This is a list of the defined variables in the custom type.
     */
    public List<VariableDeclaration> variableTypes;
    /**
     * This class represents a declaration of a new type in pascal.
     */
    private ByteClassLoader bcl = new ByteClassLoader();
    private Class cachedClass = null;

    public CustomTypeTemp() {
        variableTypes = new ArrayList<>();
    }

    /**
     * Adds another sub-variable to this user defined type.
     *
     * @param v The name and type of the variable to add.
     */
    public void add_variable_declaration(VariableDeclaration v) {
        variableTypes.add(v);
    }

    @Override
    public Object initialize() {
        try {
            return getTransferClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int hashCode() {
        return variableTypes.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CustomTypeTemp)) {
            return false;
        }
        CustomTypeTemp other = (CustomTypeTemp) obj;
        return variableTypes.equals(other.variableTypes);
    }

    @Override
    public boolean equals(DeclaredType obj) {
        return equals((Object) obj);
    }

    @Override
    public Class getTransferClass() {
        if (cachedClass != null) {
            return cachedClass;
        }
        String name =Integer.toHexString(hashCode());
        try {
            cachedClass = bcl.loadClass(name);
            return cachedClass;
        } catch (ClassNotFoundException ignored) {
            ignored.printStackTrace();
        }

        //cretew new class with name
        DexMaker dexMaker = new DexMaker();
        TypeId<?> recordType = TypeId.get("L" + name + ";");
        dexMaker.declare(recordType, name + ".generated", Modifier.PUBLIC, TypeId.OBJECT);

        //add declared interface
        c.setDeclaredInterfaces(new Class[]{ContainsVariables.class});
        for (VariableDeclaration v : variableTypes) {
            Class type = v.type.getStorageClass();
            c.declareField(v.name, type);
        }

        addConstructor(c);
        addGetVar(c);
        addSetVar(c);
        addClone(c);
        cachedClass = bcl.loadThisClass(c.toByteArray());
        return cachedClass;
    }

    @Override
    public ReturnsValue convert(ReturnsValue value, ExpressionContext f)
            throws ParsingException {
        RuntimeType other_type = value.getType(f);
        if (this.equals(other_type.declaredType)) {
            return cloneValue(value);
        }
        return null;
    }

    @Override
    public DeclaredType getMemberType(String name) {
        for (VariableDeclaration v : variableTypes) {
            if (v.name.equals(name)) {
                return v.type;
            }
        }
        System.err.println("Could not find member " + name);
        return null;
    }

    @Override
    public void pushDefaultValue(Code constructor_code, RegisterAllocator ra) {
        constructor_code.anew().setType(this.getTransferClass());
        try {
            constructor_code.invokespecial().setMethod(this.getTransferClass().getConstructor());
        } catch (SecurityException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void addConstructor() {
    }

    private void addConstructor(BCClass b) {
        BCMethod constructor = b.addDefaultConstructor();
        constructor.removeCode();
        Code constructor_code = constructor.getCode(true);
        // "this" takes up one local slot.
        RegisterAllocator ra = new SimpleRegisterAllocator(1);
        constructor_code.aload().setThis();
        try {
            constructor_code.invokespecial().setMethod(Object.class.getDeclaredConstructor());
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        for (VariableDeclaration v : variableTypes) {
            constructor_code.aload().setThis();
            v.type.pushDefaultValue(constructor_code, new ScopedRegisterAllocator(ra));
            constructor_code.putfield().setField(v.get_name(), v.type.getStorageClass());
        }
        constructor_code.vreturn();
        constructor_code.calculateMaxLocals();
        constructor_code.calculateMaxStack();
    }

    /**
     * Adds the getVariable method to a specified class. This method will conform to
     * the ideas of the contains_variables interface, and will allow access to
     * all declared fields.
     *
     * @param b The class to modify.
     */
    private void addGetVar(BCClass b) {
        BCMethod get_var = b.declareMethod("getVariable", Object.class, new Class[]{String.class});
        get_var.makePublic();
        Code getVarCode = get_var.getCode(true);
        getVarCode.aload().setParam(0);
        getVarCode.invokevirtual().setMethod(String.class, "intern", String.class, new Class[]{});
        getVarCode.astore().setParam(0);
        JumpInstruction previousIf = null;
        for (BCField f : b.getFields()) {
            Instruction codeBlock = getVarCode.constant().setValue(f.getName());
            if (previousIf != null) {
                previousIf.setTarget(codeBlock);
            }
            getVarCode.aload().setParam(0);
            previousIf = getVarCode.ifacmpne();
            getVarCode.aload().setThis();
            getVarCode.getfield().setField(f);
            Class return_type = f.getType();
            if (return_type == int.class) {
                getVarCode.invokestatic().setMethod(Integer.class, "valueOf",
                        Integer.class, new Class[]{int.class});
            } else if (return_type == double.class) {
                getVarCode.invokestatic().setMethod(Double.class,
                        "valueOf", Double.class,
                        new Class[]{double.class});
            } else if (return_type == char.class) {
                getVarCode.invokestatic().setMethod(Character.class,
                        "valueOf", Character.class, new Class[]{char.class});
            } else if (return_type == boolean.class) {
                getVarCode.invokestatic().setMethod(Boolean.class, "valueOf",
                        Boolean.class, new Class[]{boolean.class});
            }
            getVarCode.areturn();
        }
        Instruction i = getVarCode.constant().setNull();
        if (previousIf != null) {
            previousIf.setTarget(i);
        }
        getVarCode.areturn();
        getVarCode.calculateMaxLocals();
        getVarCode.calculateMaxStack();
    }

    /**
     * Adds the setVariable method to a specified class. This method will conform to
     * the ideas of the contains_variables interface, and will allow access to
     * all declared fields.
     *
     * @param b The class to modify.
     */
    private void addSetVar(BCClass b) {
        BCMethod set_var = b.declareMethod("setVariable", void.class, new Class[]{String.class, Object.class});
        set_var.makePublic();
        Code set_var_code = set_var.getCode(true);
        set_var_code.aload().setParam(0);
        set_var_code.invokevirtual().setMethod(String.class, "intern", String.class, new Class[]{});
        set_var_code.astore().setParam(0);
        JumpInstruction previous_if = null;
        for (BCField f : b.getFields()) {
            Instruction jump_to = set_var_code.constant().setValue(f.getName());
            if (previous_if != null) {
                previous_if.setTarget(jump_to);
            }
            set_var_code.aload().setParam(0);
            previous_if = set_var_code.ifacmpne();
            set_var_code.aload().setThis();
            set_var_code.aload().setParam(1);
            Class field_class = f.getType();
            if (field_class == int.class) {
                set_var_code.checkcast().setType(Integer.class);
                set_var_code.invokevirtual().setMethod(Integer.class,
                        "intValue", int.class, new Class[]{});
            } else if (field_class == double.class) {
                set_var_code.checkcast().setType(Double.class);
                set_var_code.invokevirtual().setMethod(Double.class,
                        "doubleValue", double.class, new Class[]{});
            } else if (field_class == boolean.class) {
                set_var_code.checkcast().setType(Boolean.class);
                set_var_code.invokevirtual().setMethod(Boolean.class,
                        "booleanValue", boolean.class, new Class[]{});
            } else if (field_class == char.class) {
                set_var_code.checkcast().setType(Character.class);
                set_var_code.invokevirtual().setMethod(Character.class,
                        "charValue", char.class, new Class[]{});
            } else {
                set_var_code.checkcast().setType(field_class);
            }
            set_var_code.putfield().setField(f);
        }
        Instruction jump_to = set_var_code.vreturn();
        if (previous_if != null) {
            previous_if.setTarget(jump_to);
        }
        set_var_code.calculateMaxLocals();
        set_var_code.calculateMaxStack();
    }

    private void addClone(BCClass b) {
        BCMethod clone_method = b.declareMethod("clone",
                ContainsVariables.class, new Class[0]);
        clone_method.makePublic();
        Code clone_code = clone_method.getCode(true);
        try {
            clone_code.anew().setType(b);
            clone_code.dup();
            clone_code.invokespecial().setMethod(b.addDefaultConstructor());
            clone_code.astore().setLocal(1);
            for (BCField f : b.getFields()) {

                clone_code.aload().setLocal(1);
                if (f.getType() == StringBuilder.class) {
                    clone_code.anew().setType(StringBuilder.class);
                    clone_code.dup();

                    clone_code.aload().setThis();
                    clone_code.getfield().setField(f);

                    clone_code.invokespecial().setMethod(
                            StringBuilder.class
                                    .getConstructor(CharSequence.class));
                } else if (f.getType().isPrimitive()) {
                    clone_code.aload().setThis();
                    clone_code.getfield().setField(f);
                } else if (f.getType().isArray()) {
                    clone_code.aload().setThis();
                    clone_code.getfield().setField(f);
                } else {
                    clone_code.aload().setThis();
                    clone_code.getfield().setField(f);
                    clone_code.invokevirtual().setMethod(
                            f.getType().getMethod("clone", new Class[0]));
                }

                clone_code.putfield().setField(f);
            }

            clone_code.aload().setLocal(1);
            clone_code.areturn();
            clone_code.calculateMaxLocals();
            clone_code.calculateMaxStack();
        } catch (SecurityException e) {
            e.printStackTrace();

        } catch (NoSuchMethodException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void cloneValueOnStack(TransformationInput t) {
        t.pushInputOnStack();
        try {
            t.getCode().invokeinterface().setMethod("clone", ContainsVariables.class, new Class[0]);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ReturnsValue generateArrayAccess(ReturnsValue array,
                                            ReturnsValue index) throws NonArrayIndexed {
        throw new NonArrayIndexed(array.getLine(), this);
    }

    @Override
    public ReturnsValue cloneValue(ReturnsValue r) {
        return new CloneableObjectCloner(r);
    }

    @Override
    public Class<?> getStorageClass() {
        return getTransferClass();
    }

    @Override
    public void arrayStoreOperation(Code c) {
        c.aastore();
    }

    @Override
    public void convertStackToStorageType(Code c) {
        // do nothing.
    }

    @Override
    public void pushArrayOfType(Code code, RegisterAllocator ra,
                                List<IntegerSubrangeType> ranges) {
        //Because I cannot mix this method into DeclaredType (no multiple inheritance) I have to duplicate it.
        ArrayType.pushArrayOfNonArrayType(this, code, ra, ranges);

    }

    public static class ByteClassLoader extends ClassLoader {

        public Class<?> loadThisClass(byte[] bytes) {
            Class<?> c = defineClass(null, bytes, 0, bytes.length);
            resolveClass(c);
            return c;
        }
    }
}
