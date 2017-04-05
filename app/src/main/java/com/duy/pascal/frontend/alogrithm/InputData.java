package com.duy.pascal.frontend.alogrithm;

public class InputData {
    public static final int MAX_INPUT = 4 * 1024; //4 MB
    public String[] data = new String[MAX_INPUT]; // the array of the characters
    public int last;    // number of char in the input buffer
    public int first;    // index of the first character

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = first; i < last; i++) stringBuilder.append(data[i]);
        return stringBuilder.toString();
    }

    /**
     * clear text
     */
    public void clear() {
        first = 0;
        last = 0;
    }
}
