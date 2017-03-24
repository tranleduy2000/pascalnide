package com.duy.pascal.frontend.alogrithm;

public class InputData {
    public static final int MAX_INPUT = 1000;
    public char[] data = new char[MAX_INPUT]; // the array of the caracters
    public int last;    // number of char in the input buffer
    public int first;    // index of the first character

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = first; i < last; i++) stringBuilder.append(data[i]);
        return stringBuilder.toString();
    }
}
