package com.duy.pascal.backend.ast.instructions.conditional;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.instructions.ExecutionResult;
import com.duy.pascal.backend.ast.instructions.assign_statement.AssignExecutable;
import com.duy.pascal.backend.ast.instructions.assign_statement.AssignStatement;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.operators.BinaryOperatorEval;
import com.duy.pascal.backend.ast.runtime_value.references.Reference;
import com.duy.pascal.backend.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.backend.ast.runtime_value.value.EnumElementValue;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.value.access.ConstantAccess;
import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.convert.UnConvertibleTypeException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.types.BasicType;
import com.duy.pascal.backend.types.DeclaredType;
import com.duy.pascal.backend.types.OperatorTypes;
import com.duy.pascal.backend.types.set.EnumGroupType;

import java.util.LinkedList;

/**
 * For to do loop
 * <p>
 * see in https://www.freepascal.org/docs-html/ref/refsu58.html#x164-18600013.2.4
 */
public class ForToStatement extends DebuggableExecutable {
    private AssignExecutable setfirst;
    private RuntimeValue condition;
    private AssignExecutable increment;
    private Executable command;
    private AssignableValue tempVar;
    private RuntimeValue first;
    private RuntimeValue last;
    private LineInfo line;

    private boolean isEnum = false;
    private EnumGroupType enumGroupType;

    public ForToStatement(ExpressionContext f, AssignableValue tempVar,
                          RuntimeValue first, RuntimeValue last, Executable command,
                          LineInfo line) throws ParsingException {
        this.tempVar = tempVar;
        this.first = first;
        this.last = last;
        this.line = line;

        DeclaredType declType = tempVar.getType(f).declType;
        if (declType instanceof EnumGroupType) {
            this.isEnum = true;
            this.enumGroupType = (EnumGroupType) declType;
        } else if (BasicType.Long.convert(tempVar, f) != null) {
            setfirst = new AssignStatement(tempVar, first, line);
            condition = BinaryOperatorEval.generateOp(f, tempVar, last,
                    OperatorTypes.LESSEQ, this.line);

            increment = new AssignStatement(tempVar, BinaryOperatorEval.generateOp(
                    f, tempVar, new ConstantAccess<>(1, this.line),
                    OperatorTypes.PLUS, this.line), line);
        } else {
            throw new UnConvertibleTypeException(tempVar, BasicType.Long, tempVar.getType(f).declType, f);
        }
        this.command = command;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        if (isEnum) {
            LinkedList<EnumElementValue> list = enumGroupType.getList();
            Reference reference = tempVar.getReference(f, main);
            Integer start = ((EnumElementValue) this.first.getValue(f, main)).getIndex();
            Integer end = ((EnumElementValue) this.last.getValue(f, main)).getIndex();

            forLoop:
            for (int i = start; i <= end; i++) {
                reference.set(list.get(i));
                ExecutionResult result = command.execute(f, main);
                switch (result) {
                    case EXIT:
                        return ExecutionResult.EXIT;
                    case BREAK:
                        break forLoop;
                    case CONTINUE:
                }
            }
        } else {
            setfirst.execute(f, main);
            whileLoop:
            while ((Boolean) condition.getValue(f, main)) {
                ExecutionResult result = command.execute(f, main);
                switch (result) {
                    case EXIT:
                        return ExecutionResult.EXIT;
                    case BREAK:
                        break whileLoop;
                    case CONTINUE:

                }
                increment.execute(f, main);
            }
        }
        return ExecutionResult.NONE;
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        AssignExecutable first = setfirst.compileTimeConstantTransform(c);
        AssignExecutable inc = increment.compileTimeConstantTransform(c);
        Executable comm = command.compileTimeConstantTransform(c);
        RuntimeValue comp = condition;
        Object val = condition.compileTimeValue(c);
        if (val != null) {
            if (((Boolean) val)) {
                return first;
            } else {
                comp = new ConstantAccess<>(val, condition.getLineNumber());
            }
        }
        return new ForDowntoStatement(first, comp, inc, comm, line);
    }
}
