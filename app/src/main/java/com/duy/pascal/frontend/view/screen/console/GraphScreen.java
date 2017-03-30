package com.duy.pascal.frontend.view.screen.console;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.duy.pascal.frontend.view.screen.graph.molel.GraphObject;

import java.util.ArrayList;

/**
 * Created by Duy on 30-Mar-17.
 */

public class GraphScreen {
    private int width = 1;
    private int height = 1;

    //background
    private Paint mBackgroundPaint = new Paint();

    //cursor
    private Paint mCursorPaint = new Paint();

    //list object to restore
    private ArrayList<GraphObject> graphObjects = new ArrayList<>();

    /**
     * this object used to draw {@link com.duy.pascal.frontend.view.screen.graph.molel.GraphObject}
     */
    private Bitmap mGraphBitmap;


    private CursorConsole mCursor = new CursorConsole(0, 0, 0xffffffff);

    public GraphScreen() {
        mCursorPaint.setColor(Color.WHITE);
        mBackgroundPaint.setColor(Color.BLACK);
    }

    public int getBackgroundColor() {
        return mBackgroundPaint.getColor();
    }

    public void setBackgroundColor(int backgroundColor) {
        mBackgroundPaint.setColor(backgroundColor);
    }

    public int getWidth() {
        return mGraphBitmap.getWidth();
    }

    public void setWidth(int visibleWidth) {
        this.width = visibleWidth;
        invalidateBitmap();
    }

    public int getHeight() {
        return mGraphBitmap.getHeight();
    }

    public void setHeight(int visibleHeight) {
        this.height = visibleHeight;
        invalidateBitmap();
    }

    public void onSizeChange(int width, int height) {
        this.width = width;
        this.height = height;
        invalidateBitmap();
    }

    private void invalidateBitmap() {
        mGraphBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(mGraphBitmap);
        for (GraphObject object : graphObjects) {
            object.draw(canvas);
        }
    }

    public Bitmap getGraphBitmap() {
        return mGraphBitmap;
    }

    public void addGraphObject(GraphObject graphObject) {
        graphObject.setBackground(mBackgroundPaint.getColor());
        graphObject.setForeground(mCursorPaint.getColor());
        //save to list
        graphObjects.add(graphObject);

        graphObject.draw(mGraphBitmap);
    }

    public int getColorPixel(int x, int y) {
        return mGraphBitmap.getPixel(x, y);
    }

    public int getYCursor() {
        return mCursor.getY();
    }

    public int getXCursor() {
        return mCursor.getX();
    }

    public int getPaintColor() {
        return mCursorPaint.getColor();
    }

    public void setPaintColor(int paintColor) {
        this.mCursorPaint.setColor(paintColor);
    }

    public void closeGraph() {
        graphObjects.clear();
        invalidateBitmap();
    }

    public void clear() {
        graphObjects.clear();
        mCursor.setCoordinate(0, 0);
    }

    public void setCursorPostion(int x, int y) {
        this.mCursor.setCoordinate(x, y);
    }

    public CursorConsole getCursor() {
        return mCursor;
    }
}
