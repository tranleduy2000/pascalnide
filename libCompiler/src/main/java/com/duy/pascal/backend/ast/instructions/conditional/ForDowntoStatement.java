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

public class ForDowntoStatement extends DebuggableExecutable {


    private Executable command;
    private AssignableValue tempVar;
    private RuntimeValue first;
    private RuntimeValue last;
    private LineInfo line;

    //for enum
    private boolean isEnum = false;
    private EnumGroupType enumGroupType;

    //for long
    private AssignExecutable setfirst;
    private RuntimeValue condition;
    private AssignExecutable increment;

    public ForDowntoStatement(ExpressionContext f, AssignableValue tempVar,
                              RuntimeValue first, RuntimeValue last, Executable command,
                              LineInfo line) throws ParsingException {
        this.line = line;
        this.tempVar = tempVar;
        this.first = first;
        this.last = last;
        DeclaredType declType = tempVar.getType(f).declType;
        if (declType instanceof EnumGroupType) {
            this.isEnum = true;
            this.enumGroupType = (EnumGroupType) declType;
        } else if (BasicType.Long.convert(tempVar, f) != null) {
            setfirst = new AssignStatement(tempVar, first, line);
            condition = BinaryOperatorEval.generateOp(f, tempVar, last, OperatorTypes.GREATEREQ, this.line);
            increment = new AssignStatement(tempVar,
                    BinaryOperatorEval.generateOp(f, tempVar, new ConstantAccess<>(1, BasicType.Integer,
                            this.line), OperatorTypes.MINUS, line), line);

        } else {
            throw new UnConvertibleTypeException(tempVar, BasicType.Long, tempVar.getType(f).declType, f);
        }
        this.command = command;
    }

    public ForDowntoStatement(AssignExecutable setfirst,
                              RuntimeValue condition, AssignExecutable increment,
                              Executable command, LineInfo line) {
        super();
        this.setfirst = setfirst;
        this.condition = condition;
        this.increment = increment;
        this.command = command;
        this.line = line;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext context,
                                       RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        if (isEnum) {
            LinkedList<EnumElementValue> list = enumGroupType.getList();
            Reference reference = tempVar.getReference(context, main);
            Integer start = ((EnumElementValue) this.first.getValue(context, main)).getIndex();
            Integer end = ((EnumElementValue) this.last.getValue(context, main)).getIndex();

            forLoop:
            for (int i = start; i <= end; i++) {
                reference.set(list.get(i));
                ExecutionResult result = command.execute(context, main, contextName);
                switch (result) {
                    case EXIT:
                        return ExecutionResult.EXIT;
                    case BREAK:
                        break forLoop;
                    case CONTINUE:
                }
            }
        } else {
            setfirst.execute(context, main, contextName);

            while_loop:
            while ((Boolean) condition.getValue(context, main)) {
                switch (command.execute(context, main, contextName)) {
                    case EXIT:
                        return ExecutionResult.EXIT;
                    case BREAK:
                        break while_loop;
                    case CONTINUE:
                        continue while_loop;
                }
                increment.execute(context, main, contextName);
            }
        }
        return ExecutionResult.NOPE;
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
            }
            comp = new ConstantAccess<>(val, condition.getLineNumber());
        }
        return new ForDowntoStatement(first, comp, inc, comm, line);
    }
}
