package com.duy.pascal.frontend.theme;

public class ForegroundColor {
    private int foreground;

    public ForegroundColor(int foreground) {
        this.foreground = foreground;
    }

    @Override
    public String toString() {
        return "color: " + Integer.toHexString(foreground) + "; ";
    }

    public int getForeground() {
        return this.foreground;
    }

    public void setForeground(int foreground) {
        this.foreground = foreground;
    }
}
