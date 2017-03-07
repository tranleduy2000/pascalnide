package com.js.interpreter.runtime.exception;

import com.js.interpreter.ast.AbstractFunction;
import com.duy.interpreter.linenumber.LineInfo;

public class PluginCallException extends RuntimePascalException {
	public Throwable cause;
	public AbstractFunction function;

	public PluginCallException(LineInfo line, Throwable cause,
			AbstractFunction function) {
		super(line);
		this.cause = cause;
		this.function = function;
	}

	@Override
	public String getMessage() {
		return "When calling plugin " + function.name()
				+ ", The following java exception occured: "
				+ cause;
	}
}
