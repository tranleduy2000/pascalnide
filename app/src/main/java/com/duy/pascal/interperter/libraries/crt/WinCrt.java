package com.duy.pascal.interperter.libraries.crt;

import com.duy.pascal.ui.runnable.ConsoleHandler;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.libraries.PascalLibrary;
import com.duy.pascal.interperter.libraries.annotations.PascalMethod;
import com.duy.pascal.interperter.libraries.io.IOLib;
import com.duy.pascal.interperter.libraries.io.InOutListener;
import com.duy.pascal.interperter.exceptions.runtime.WrongArgsException;

/**
 * Created by Duy on 24-May-17.
 */

/**
 * The wincrt unit provides some auxiliary routines for use with the graph unit,
 * namely keyboard support. It has no connection with the crt unit, nor with the
 * Turbo-Pascal for Windows WinCrt unit. As such, it should not be used by end users.
 * Refer to the crt unit instead.
 * <p>
 * See in https://www.freepascal.org/docs-html/rtl/wincrt/index.html
 */
@SuppressWarnings("unused")
public class WinCrt extends PascalLibrary {
    public static final String NAME = "wincrt";
    private ConsoleHandler mHandler;
    private CrtLib crtLib;
    private IOLib ioLib;

    public WinCrt() {

    }

    public WinCrt(ConsoleHandler handler) {
        this.mHandler = handler;
        this.crtLib = new CrtLib(handler);
        ioLib = new IOLib((InOutListener) handler);
    }


    @Override
    public void onFinalize() {

    }

    @Override
    public String getName() {
        return null;
    }

    @PascalMethod(description = "Pause program execution")
    public void delay(long ms) {
        crtLib.delay(ms);
    }

    @PascalMethod(description = "Check if a key was pressed.")
    public boolean keyPressed() {
        return ioLib.keyPressed();
    }

    @PascalMethod(description = "Read a key from the keyboard")
    public char readKey() {
        return ioLib.readKey();
    }


    @PascalMethod(description = "Sound system speaker")
    public void sound(Integer frequency) throws WrongArgsException {
        crtLib.sound(frequency);
    }

    @PascalMethod(description = "Stop system speaker")
    public void noSound() {
        crtLib.noSound();
    }

    @PascalMethod(description = "Stet indicated text mode")
    public void textMode(int mode) {
        crtLib.textMode(mode);
    }


    @Override
    public void declareConstants(ExpressionContextMixin context) {
        crtLib.declareConstants(context);
    }

    @Override
    public void declareTypes(ExpressionContextMixin context) {

    }

    @Override
    public void declareVariables(ExpressionContextMixin context) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin context) {

    }
}
