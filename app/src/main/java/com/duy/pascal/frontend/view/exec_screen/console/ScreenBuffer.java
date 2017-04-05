package com.duy.pascal.frontend.view.exec_screen.console;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Duy on 26-Mar-17.
 */

public class ScreenBuffer {
    public int firstIndex;
    //    public TextConsole[] textOnScreenBuffer;
    public char[] textOnScreenBuffer;
    public TextConsole[] textConsole;
    public int[] colorScreenBuffer;
    /**
     * store text input
     */
//    public ByteQueue charBuffer = new ByteQueue();
    /**
     * store text input, with unicode character
     */
    public StringQueue stringBuffer = new StringQueue();
    /**
     * store key code event
     */
    public ByteQueue keyBuffer = new ByteQueue();
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;

    public TextConsole[] getTextConsole() {
        return textConsole;
    }

    public void setTextConsole(TextConsole[] textConsole) {
        this.textConsole = textConsole;
    }

    public char[] getTextOnScreenBuffer() {
        return textOnScreenBuffer;
    }

    public void setTextOnScreenBuffer(char[] textOnScreenBuffer) {
        this.textOnScreenBuffer = textOnScreenBuffer;
    }
//
//    public TextConsole[] getTextOnScreenBuffer() {
//        return textOnScreenBuffer;
//    }
//
//    public void setTextOnScreenBuffer(TextConsole[] textOnScreenBuffer) {
//        this.textOnScreenBuffer = textOnScreenBuffer;
//    }

    public int[] getColorScreenBuffer() {
        return colorScreenBuffer;
    }

    public void setColorScreenBuffer(int[] colorScreenBuffer) {
        this.colorScreenBuffer = colorScreenBuffer;
    }

    /**
     * save current text to file
     */
    public void store() {
// TODO: 26-Mar-17
    }

    /**
     * read text from then file
     */
    public void restore() {
// TODO: 26-Mar-17
    }

    /**
     * clear text
     */
    public void clearBuffer() {
    }

    public int getFirstIndex() {
        return firstIndex;
    }


}
