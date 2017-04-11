package com.duy.pascal.backend.pascaltypes;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.pascaltypes.bytecode.RegisterAllocator;
import com.duy.pascal.backend.pascaltypes.bytecode.ScopedRegisterAllocator;
import com.duy.pascal.backend.pascaltypes.bytecode.TransformationInput;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ArrayAccess;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.ast.returnsvalue.cloning.ArrayCloner;
import com.ncsa.common.util.TypeUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import serp.bytecode.Code;
import serp.bytecode.Instruction;
import serp.bytecode.JumpInstruction;

public class ArrayType<T extends DeclaredType> implements DeclaredType {

    public final T element_type;

    public SubrangeType bounds;

    public ArrayType(T element_class, SubrangeType bounds) {
        this.element_type = element_class;
        this.bounds = bounds;
    }

    public static void pushArrayOfNonArrayType(DeclaredType type, Code code,
                                               RegisterAllocator ra, List<SubrangeType> ranges) {
        for (SubrangeType i : ranges) {
            code.constant().setValue(i.size);
        }
        // For now we are storing as array of objects always
        code.multianewarray().setDimensions(ranges.size())
                .setType("[" + type.getTransferClass().getName());
        int array = ra.getNextFree();
        code.astore().setLocal(array);
        int[] indexes = new int[ranges.size()];
        JumpInstruction[] fjmp = new JumpInstruction[ranges.size()];
        Instruction[] jmpbackto = new Instruction[ranges.size() + 1];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = ra.getNextFree();
            jmpbackto[i] = code.constant().setValue(0);
            code.istore().setLocal(indexes[i]);
            fjmp[i] = code.go2();
        }
        jmpbackto[ranges.size()] = code.aload().setLocal(array);
        for (int i = 0; i < indexes.length - 1; i++) {
            code.iload().setLocal(indexes[i]);
            code.aaload();
        }
        code.iload().setLocal(indexes[indexes.length - 1]);
        type.pushDefaultValue(code, new ScopedRegisterAllocator(ra));
        type.convertStackToStorageType(code);
        // Because we are not compiling yet, we are using wrapper classes in
        // arrays.
        // arrayStoreOperation(code);
        code.aastore();
        for (int i = 0; i < indexes.length; i++) {
            code.iinc().setIncrement(1).setLocal(indexes[i]);
            fjmp[i].setTarget(code.iload().setLocal(indexes[i]));
            code.constant().setValue(ranges.get(i).size);
            code.ificmplt().setTarget(jmpbackto[i + 1]);
        }
        for (int i = 0; i < indexes.length; i++) {
            ra.free(indexes[i]);
        }
        code.aload().setLocal(array);
        ra.free(array);
    }

    /**
     * This basically tells if the types are assignable from each other
     * according to Pascal.
     */
    public boolean superset(DeclaredType obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ArrayType) {
            ArrayType<?> o = (ArrayType<?>) obj;
            if (o.element_type.equals(element_type)) {
                if (this.bounds.contains(o.bounds)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(DeclaredType obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ArrayType) {
            ArrayType<?> o = (ArrayType<?>) obj;
            if (o.element_type.equals(element_type)) {
                if (this.bounds.equals(o.bounds)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (element_type.hashCode() * 31 + bounds.hashCode());
    }

    /**
     * TODO: Must make this actually fill in array with default values
     */
    @Override
    public Object initialize() {
        Object result = Array.newInstance(element_type.getTransferClass(),
                bounds.size);
        for (int i = 0; i < bounds.size; i++) {
            Array.set(result, i, element_type.initialize());
        }
        return result;
    }

    @Override
    public Class<?> getTransferClass() {
        String s = element_type.getTransferClass().getName();
        StringBuilder b = new StringBuilder();
        b.append('[');
        b.append('L');
        b.append(s);
        b.append(';');
        try {
            return Class.forName(b.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return element_type.toString() + '[' + bounds + ']';
    }

    /**
     * This basically won't do any conversions, as array types have to be exact,
     * except variable length arrays, but they are checked in the {@link
     */
    @Override
    public ReturnsValue convert(ReturnsValue value, ExpressionContext f)
            throws ParsingException {
        RuntimeType other = value.getType(f);
        return this.superset(other.declaredType) ? cloneValue(value) : null;
    }

    @Override
    public void pushDefaultValue(Code code, RegisterAllocator ra) {
        ArrayList<SubrangeType> ranges = new ArrayList<>();
        ranges.add(bounds);
        element_type.pushArrayOfType(code, ra, ranges);
    }

    @Override
    public ReturnsValue cloneValue(final ReturnsValue r) {
        return new ArrayCloner<T>(r);
    }

    private void pushLengthOnStack(Code c, int varindex) {
        if (varindex >= 0) {
            c.iload().setLocal(varindex);
        } else {
            c.constant().setValue(bounds.size);
        }
    }

    @Override
    public void cloneValueOnStack(final TransformationInput t) {
        final Code c = t.getCode();
        t.pushInputOnStack();
        int varindex = -1;
        if (this.bounds.size == 0) {
            varindex = t.getFreeRegister();
            c.dup();
            c.arraylength();
            c.istore().setLocal(varindex);
        }
        // STACK=OLD
        final int cloneeindex = t.getFreeRegister();
        c.astore().setLocal(cloneeindex);
        pushLengthOnStack(c, varindex);
        // STACK=LEN
        c.dup();
        // STACK=LEN,LEN
        c.anewarray().setType(this.element_type.getTransferClass());
        // Stack=LEN,NEW
        c.dupx1();
        c.swap();
        // STACK=NEW,NEW,LEN
        final int index = t.getFreeRegister();
        c.constant().setValue(0);
        c.dup();
        c.istore().setLocal(index);
        // STACK= NEW,NEW,LEN,IND
        c.dupx1();
        // STACK=NEW,NEW,IND,LEN,IND
        JumpInstruction jmp = c.ificmple();
        // STACK=NEW,NEW,IND
        element_type.cloneValueOnStack(new TransformationInput() {

            @Override
            public void pushInputOnStack() {
                c.aload().setLocal(cloneeindex);
                c.iload().setLocal(index);
                c.aload();
            }

            @Override
            public int getFreeRegister() {
                return t.getFreeRegister();
            }

            @Override
            public Code getCode() {
                return c;
            }

            @Override
            public void freeRegister(int index) {
                t.freeRegister(index);
            }
        });
        // STACK=NEW,NEW,IND,NEWVALUE
        c.aastore();
        // STACK=NEW
        c.dup();
        pushLengthOnStack(c, varindex);
        c.iload().setLocal(index);
        c.iinc();
        // STACK=NEW,NEW,LEN,IND
        c.go2().setTarget(jmp);
        jmp.setTarget(c.pop2());
        c.pop2();
        t.freeRegister(cloneeindex);
        t.freeRegister(index);
        if (varindex != -1) {
            t.freeRegister(varindex);
        }
    }

    @Override
    public ReturnsValue generateArrayAccess(ReturnsValue array,
                                            ReturnsValue index) {
        return new ArrayAccess(array, index, bounds.lower);
    }

    public void pushArrayOfType(Code code, RegisterAllocator ra,
                                List<SubrangeType> ranges) {
        ranges.add(bounds);
        element_type.pushArrayOfType(code, ra, ranges);
    }

    @Override
    public Class<?> getStorageClass() {
        Class c = element_type.getStorageClass();
        if (c.isArray()) {
            try {
                return Class.forName("[" + c.getName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        } else if (c.isPrimitive()) {
            c = TypeUtils.getClassForType(c);
        }
        StringBuilder b = new StringBuilder();
        b.append('[');
        b.append('L');
        b.append(c.getName());
        b.append(';');
        try {
            return Class.forName(b.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void arrayStoreOperation(Code c) {
        c.aastore();
    }

    @Override
    public void convertStackToStorageType(Code c) {
        // Do nothing.
    }

}
