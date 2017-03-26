package com.duy.pascal.frontend.view.screen.graph.molel;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Duy on 02-Mar-17.
 */

public abstract class GraphObject {
    public Paint paint = new Paint();
    protected int foreground;
    protected int background;

    public int getForeground() {
        return foreground;
    }

    public void setForeground(int foreground) {
        this.foreground = foreground;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public abstract void draw(Canvas canvas);

    public void setPaintColor(int color) {
        this.paint.setColor(color);
    }
}
