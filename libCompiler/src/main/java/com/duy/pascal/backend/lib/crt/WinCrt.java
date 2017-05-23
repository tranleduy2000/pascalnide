package com.duy.pascal.backend.lib.crt;

import com.duy.pascal.backend.lib.PascalLibrary;
import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.duy.pascal.backend.lib.io.IOLib;
import com.duy.pascal.backend.lib.io.InOutListener;
import com.duy.pascal.frontend.activities.ExecHandler;
import com.js.interpreter.expressioncontext.ExpressionContextMixin;
import com.js.interpreter.runtime.exception.WrongArgsException;

import java.util.Map;

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
public class WinCrt implements PascalLibrary {
    public static final String NAME = "wincrt";
    private ExecHandler handler;
    private CrtLib crtLib;
    private IOLib ioLib;

    public WinCrt(ExecHandler handler) {
        this.handler = handler;
        this.crtLib = new CrtLib(handler);
        ioLib = new IOLib((InOutListener) handler);
    }


    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    public void shutdown() {

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
    public void declareConstants(ExpressionContextMixin parentContext) {
        crtLib.declareConstants(parentContext);
    }

    @Override
    public void declareTypes(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareVariables(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin parentContext) {

    }
}
