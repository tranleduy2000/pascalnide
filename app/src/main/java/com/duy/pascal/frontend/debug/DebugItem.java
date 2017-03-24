package com.duy.pascal.frontend.debug;

/**
 * Created by Duy on 24-Mar-17.
 */

public class DebugItem {
    public static final int TYPE_VAR = 1;
    public static final int TYPE_MSG = 2;
    private int type = TYPE_VAR;
    private String msg1 = "", msg2 = "";

    public DebugItem(int type, String msg1, String msg2) {
        this.type = type;
        this.msg1 = msg1;
        this.msg2 = msg2;
    }

    public DebugItem(int type, String msg1) {
        this.type = type;
        this.msg1 = msg1;
        this.msg2 = msg2;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg1() {
        return msg1;
    }

    public void setMsg1(String msg1) {
        this.msg1 = msg1;
    }

    public String getMsg2() {
        return msg2;
    }

    public void setMsg2(String msg2) {
        this.msg2 = msg2;
    }

    @Override
    public String toString() {

        return type == TYPE_VAR ? this.msg1 + " = " + msg2 : msg1;
    }
}
