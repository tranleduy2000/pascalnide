package com.duy.interpreter.exceptions.grouping;

import com.duy.interpreter.linenumber.LineInfo;

public class EnumeratedGroupingException extends GroupingException {
    /**
     *
     */
    private static final long serialVersionUID = 5878580280861132626L;
    public Exception caused;
    public GroupingExceptionTypes exceptionTypes;

    public EnumeratedGroupingException(LineInfo line, GroupingExceptionTypes t) {
        super(line);
        this.exceptionTypes = t;
    }

    @Override
    public String getMessage() {
        return exceptionTypes.message + ((caused == null) ? ("") : (": " + caused.getMessage()));
    }

    public enum GroupingExceptionTypes {
        MISMATCHED_PARENS("Mismatched parentheses"),
        MISMATCHED_BRACKETS("Mismatched brackets"),
        MISMATCHED_BEGIN_END("Mismatched begin - end construct"),
        UNFINISHED_BEGIN_END("Unfinished begin - end construct"),
        UNFINISHED_PARENS("You forgot to close your parentheses"),
        UNFINISHED_BRACKETS("You forgot to close your brackets"),
        EXTRA_END("You have an extra 'end' in your program"),
        UNFINISHED_CONSTRUCT("You forgot to complete the structure you started here"),
        IO_EXCEPTION("IOException occured while reading the input"),
        INCOMPLETE_CHAR("Incomplete character literal"),
        MISSING_INCLUDE("Missing file to include"),
        NEWLINE_IN_QUOTES("You must close your quotes before starting a new line");
        public String message;

        GroupingExceptionTypes(String message) {
            this.message = message;
        }
    }


}
