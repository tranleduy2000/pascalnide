package com.duy.pascal.backend.exceptions.grouping;

import com.duy.pascal.backend.linenumber.LineInfo;

public class GroupingExceptionType extends GroupingException {
    public Exception caused;
    public GroupExceptionType exceptionTypes;

    public GroupingExceptionType(LineInfo line, GroupExceptionType t) {
        super(line);
        this.exceptionTypes = t;
    }

    @Override
    public String getMessage() {
        return exceptionTypes.message + ((caused == null) ? ("") : (": " + caused.getMessage()));
    }

    public enum GroupExceptionType {
        MISMATCHED_PARENTHESES("Mismatched parentheses"),
        MISMATCHED_BRACKETS("Mismatched brackets"),
        MISMATCHED_BEGIN_END("Mismatched begin - end construct"),
        UNFINISHED_BEGIN_END("Unfinished begin - end construct"),
        UNFINISHED_PARENTHESES("You forgot to close your parentheses"),
        UNFINISHED_BRACKETS("You forgot to close your brackets"),
        EXTRA_END("You have an extra 'end' in your program"),
        UNFINISHED_CONSTRUCT("You forgot to complete the structure you started here"),
        IO_EXCEPTION("IOException occurred while reading the input"),
        INCOMPLETE_CHAR("Incomplete character literal"),
        MISSING_INCLUDE("Missing file to include"),
        NEWLINE_IN_QUOTES("You must close your quotes before starting a new line");
        public String message;

        GroupExceptionType(String message) {
            this.message = message;
        }
    }


}
