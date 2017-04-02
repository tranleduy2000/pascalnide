package com.duy.pascal.frontend.view.exec_screen.console;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.duy.pascal.frontend.utils.FontManager;
import com.duy.pascal.frontend.view.exec_screen.graph.molel.GraphObject;

import java.util.ArrayList;

import static android.R.attr.textSize;
import static com.duy.pascal.frontend.view.exec_screen.graph.molel.TextGraphObject.HORIZONTAL_DIR;

/**
 * Created by Duy on 30-Mar-17.
 */

public class GraphScreen {
    private final Context context;
    private int width = 1;
    private int height = 1;
    //background
    private Paint mBackgroundPaint = new Paint();
    //cursor
    private Paint mCursorPaint = new Paint();
    //list object to restore
    private ArrayList<GraphObject> graphObjects = new ArrayList<>();
    /**
     * this object used to draw {@link com.duy.pascal.frontend.view.exec_screen.graph.molel.GraphObject}
     */
    private Bitmap mGraphBitmap;
    private CursorConsole mCursor = new CursorConsole(0, 0, 0xffffffff);
    private int textDirection = HORIZONTAL_DIR;
    private int textFontIndex = 0;

    public GraphScreen(Context context) {
        this.context = context;
        //setup cursor paint
        mCursorPaint.setColor(Color.WHITE);
        mCursorPaint.setTextSize(14f);
//        mCursorPaint.setTypeface(Typeface.SANS_SERIF);
        mCursorPaint.setAntiAlias(true);
        mCursorPaint.setTypeface(FontManager.getFontFromAsset(context, "triplex.ttf"));

        mBackgroundPaint.setColor(Color.BLACK);
    }

    public int getTextDirection() {
        return textDirection;
    }

    public void setTextDirection(int textDirection) {
        this.textDirection = textDirection;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        mCursorPaint.setTextSize(textSize);
    }

    public int getTextFontIndex() {
        return textFontIndex;
    }

    public void setTextFontIndex(int textFontIndex) {
        this.textFontIndex = textFontIndex;
    }

    public Paint getCursorPaint() {
        return mCursorPaint;
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
        // TODO: 30-Mar-17
        graphObject.setBackground(mBackgroundPaint.getColor());
        graphObject.setForeground(mCursorPaint.getColor());
        //end
        graphObject.setPaint(new Paint(mCursorPaint));
        graphObject.setBackgroundPaint(new Paint(mBackgroundPaint));
        graphObject.setTextDirection(textDirection);

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

    public void setPaintStyle(int style, int pattern, int width) {
        mCursorPaint.setStrokeWidth(width);
//        mCursor
//        mCursorPaint.set
    }
}
