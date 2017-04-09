package com.duy.pascal.frontend.view.exec_screen.console;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.duy.pascal.backend.lib.graph.ViewPort;
import com.duy.pascal.frontend.utils.FontManager;
import com.duy.pascal.backend.lib.graph.molel.GraphObject;

import java.util.ArrayList;

import static android.R.attr.textSize;
import static com.duy.pascal.backend.lib.graph.molel.TextGraphObject.HORIZONTAL_DIR;

/**
 * Created by Duy on 30-Mar-17.
 */

public class GraphScreen {
    private final Context context;
    private int width = 1;
    private int height = 1;

    private ViewPort viewPort = new ViewPort(0, 0, width, height);

    //background
    private Paint mBackgroundPaint = new Paint();
    //cursor
    private Paint mCursorPaint = new Paint();

    private Paint mTextPaint = new Paint();
    private Paint mLinePaint = new Paint();
    //list object to restore
    private ArrayList<GraphObject> graphObjects = new ArrayList<>();
    /**
     * this object used to drawBackground {@link com.duy.pascal.backend.lib.graph.molel.GraphObject}
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
        mBackgroundPaint.setAlpha(255);
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
        if (width < 1) width = 1;
        this.width = visibleWidth;
    }

    public int getHeight() {
        return mGraphBitmap.getHeight();
    }

    public void setHeight(int visibleHeight) {
        if (height < 1) height = 1;
        this.height = visibleHeight;
    }

    public void onSizeChange(int width, int height) {
        setWidth(width);
        setHeight(height);
        //set view port
        viewPort.set(0, 0, width, height);

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
        graphObjects.clear(); //GC
//        invalidateBitmap();
    }

    public void clear() {
        graphObjects.clear();
        mCursor.setCoordinate(0, 0);
        invalidateBitmap();
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

    public Paint getBackgroundPaint() {
        return mBackgroundPaint;
    }

    public void getTextBound(String text, Rect store) {
        mTextPaint.getTextBounds(text, 0, text.length(), store);
    }

    public Paint getTextPaint() {
        return mTextPaint;
    }

    public void setTextPaint(Paint mTextPaint) {
        this.mTextPaint = mTextPaint;
    }

    public Paint getLinePaint() {
        return mLinePaint;
    }

    public void setLinePaint(Paint mLinePaint) {
        this.mLinePaint = mLinePaint;
    }


    /**
     * Set the current graphic viewport to the retangle define by then top-left (x1, y1) and then
     * bottom-right (x2, y2). If clip
     */
    public void setViewPort(int x1, int y1, int x2, int y2, boolean clip) {
        viewPort.setViewPort(x1, y1, x2, y2, clip);
    }
}
