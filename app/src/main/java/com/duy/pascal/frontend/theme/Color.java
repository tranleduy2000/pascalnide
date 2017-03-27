package com.duy.pascal.frontend.theme;

public class Color {
    private int foreground;

    public Color(int foreground) {
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
