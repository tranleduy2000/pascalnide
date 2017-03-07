package com.js.interpreter.ast;

public class StaticMethods {

	public static int indexOf(Object[] array, Object o) {
		for (int i = 0; i < array.length; i++) {
			if (o.equals(array[i])) {
				return i;
			}
		}
		return -1;
	}
}
