package com.duy.pascal.backend.bytecodetest;


import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.ContainsVariables;

/**
 * This class serves no purpose except to generate bytecode that we can then
 * examine and imitate. Basically, this is the outline of what a custom
 * generated class should look like.
 * 
 * @author jeremy
 * 
 */
public class point implements ContainsVariables {

	double x;

	int y;

	public point(double x2, int y2) {
		this.x = x2;
		this.y = y2;
	}

	@Override
	public Object getVariable(String name) throws RuntimePascalException {
		name = name.intern();
		if (name == "x") {
			return x;
		}
		if (name == "y") {
			return y;
		}
		return null;
	}

	@Override
	public void setVariable(String name, Object val) {
		if (name.equals("x")) {
			x = (Double) val;
		}
		if (name.equals("y")) {
			y = (Integer) val;
		}
	}

	@Override
	public ContainsVariables clone() {
		return new point(x, y);
	}

}