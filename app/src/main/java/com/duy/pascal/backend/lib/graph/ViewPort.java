package com.duy.pascal.backend.lib.graph;

import android.graphics.Rect;

/**
 * Created by Duy on 08-Apr-17.
 */

public class ViewPort {
    private Rect rect = new Rect();
    private boolean clip = false;

    public ViewPort(int i, int i1, int width, int height) {
        rect.set(i, i1, width, height);
    }

    /**
     * Set the current graphic viewport to the retangle define by then top-left (x1, y1) and then
     * bottom-right (x2, y2). If clip
     */
    public void setViewPort(int x1, int y1, int x2, int y2, boolean clip) {
        rect.set(x1, y1, x2, y2);
        this.clip = clip;
    }

    public boolean isClip() {
        return clip;
    }

    public void setClip(boolean clip) {
        this.clip = clip;
    }

    public void set(int i, int i1, int width, int height) {
        setViewPort(i, i1, width, height, clip);
    }
}
