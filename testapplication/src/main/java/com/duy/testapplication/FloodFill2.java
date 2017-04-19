package com.duy.testapplication;

import android.graphics.Bitmap;
import android.util.Log;


/**
 * Created by Duy on 19-Apr-17.
 */

public class FloodFill2 {
    private static final String TAG = "FloodFill2";
    private final Bitmap source;
    private final int startX;
    private final int startY;
    private final int targetColor;
    private boolean[][] mark;
    private int width;
    private int height;
    private int sourceColor;

    public FloodFill2(Bitmap source, int startX, int startY, int targetColor) {
        this.width = source.getWidth();
        this.height = source.getHeight();
        this.mark = new boolean[width][height];
        this.source = source;
        this.startX = startX;
        this.startY = startY;
        this.targetColor = targetColor;
        this.sourceColor = source.getPixel(startX, startY);
    }

    public void fill() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!mark[x][y]) {
                    fill(x, y, targetColor);
                }
            }
        }
    }

    private void fill(int x, int y, int targetColor) {
        if (x < 0 || x > width - 1) return;
        if (y < 0 || y > height - 1) return;
        if (mark[x][y]) return;
        if (source.getPixel(x, y) == sourceColor) return;

        Log.d(TAG, "fill: " + x + " c = " + y);

        source.setPixel(x, y, targetColor);
        mark[x][y] = true;

        fill(x - 1, y, targetColor);
        fill(x, y + 1, targetColor);
        fill(x + 1, y, targetColor);
        fill(x, y - 1, targetColor);
    }
}
