package com.duy.pascal.backend.lib.graph;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.duy.pascal.backend.lib.graph.graphic_model.GraphObject;
import com.duy.pascal.backend.lib.graph.line_style.FillType;
import com.duy.pascal.backend.lib.graph.line_style.LineStyle;
import com.duy.pascal.backend.lib.graph.line_style.LineWidth;
import com.duy.pascal.backend.lib.graph.text_model.TextDirection;
import com.duy.pascal.backend.lib.graph.text_model.TextFont;
import com.duy.pascal.backend.lib.graph.text_model.TextJustify;
import com.duy.pascal.frontend.utils.FontManager;
import com.duy.pascal.frontend.view.exec_screen.console.CursorConsole;

import java.util.ArrayList;

import static android.R.attr.textSize;

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
    private Paint mForegroundPaint = new Paint();

    //list object to restore
    private ArrayList<GraphObject> graphObjects = new ArrayList<>();

    /**
     * this object used to drawBackground {@link GraphObject}
     */
    private Bitmap mGraphBitmap;
    private CursorConsole mCursor = new CursorConsole(0, 0, 0xffffffff);

    private static final String TAG = GraphScreen.class.getSimpleName();
    private int textStyle = TextFont.DefaultFont;
    private int textDirection = TextDirection.HORIZONTAL_DIR;
    private int lineWidth = LineWidth.NormWidth;
    private int lineStyle = LineStyle.SolidLn;
    private int fillStyle = FillType.EmptyFill;
    private int fillPattern;

    public int getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(int textStyle) {
        this.textStyle = textStyle;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public int getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(int lineStyle) {
        this.lineStyle = lineStyle;
    }

    public int getFillStyle() {
        return fillStyle;
    }

    public int getFillColor() {
        return fillColor;
    }

    protected int fillColor = -1;//white


    public GraphScreen(Context context) {
        this.context = context;
        //setup cursor paint
        mForegroundPaint.setColor(Color.WHITE);
        mForegroundPaint.setTextSize(16f);
        mForegroundPaint.setAntiAlias(true);
        mForegroundPaint.setTypeface(Typeface.MONOSPACE);

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
        mForegroundPaint.setTextSize(textSize);
    }


    public Paint getCursorPaint() {
        return mForegroundPaint;
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
        mGraphBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(mGraphBitmap);
        for (GraphObject object : graphObjects) {
            object.draw(canvas);
        }
    }

    public Bitmap getGraphBitmap() {
        return mGraphBitmap;
    }

    protected TextJustify textJustify = new TextJustify();
    private Typeface currentFont;

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
        return mForegroundPaint.getColor();
    }

    public void setPaintColor(int paintColor) {
        this.mForegroundPaint.setColor(paintColor);
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

    public void addGraphObject(GraphObject graphObject) {
        // TODO: 30-Mar-17
        graphObject.setBackgroundColor(mBackgroundPaint.getColor());
        graphObject.setForegroundColor(mForegroundPaint.getColor());
        graphObject.setTextDirection(textDirection);
        graphObject.setFillStyle(fillStyle);
        graphObject.setFillColor(fillColor);
        graphObject.setLineWidth(lineWidth);
        graphObject.setLineStyle(lineStyle);
        graphObject.setTextDirection(textDirection);
        graphObject.setTextStyle(textStyle);
        graphObject.setTextFont(currentFont);

        //end

        //save to list
        graphObjects.add(graphObject);

        //add to screen
        graphObject.draw(mGraphBitmap);
    }

    public Paint getBackgroundPaint() {
        return mBackgroundPaint;
    }

    public void getTextBound(String text, Rect store) {
        mForegroundPaint.getTextBounds(text, 0, text.length(), store);
    }

    public Paint getTextPaint() {
        return mForegroundPaint;
    }

    /**
     * Set the current graphic viewport to the retangle define by then top-left (x1, y1) and then
     * bottom-right (x2, y2). If clip
     */
    public void setViewPort(int x1, int y1, int x2, int y2, boolean clip) {
        viewPort.setViewPort(x1, y1, x2, y2, clip);
    }

    public void setPaintStyle(int style, int pattern, int width) {
        mForegroundPaint.setStrokeWidth(width);
//        mCursor
//        foregroundPaint.set
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public void setFillStyle(int fillStyle) {
        this.fillStyle = fillStyle;
    }

    public void setFont(int font) {
        this.textStyle = font;
        switch (font) {
            case TextFont.DefaultFont:
                currentFont = Typeface.MONOSPACE;
                break;
            case TextFont.SansSerifFont:
                currentFont = Typeface.SANS_SERIF;
                break;
            case TextFont.TriplexFont:
                currentFont = FontManager.getFontFromAsset(context, "triple.ttf");
                break;
            case TextFont.EuroFont:
                currentFont = FontManager.getFontFromAsset(context, "graph_euro.ttf");
                break;
            case TextFont.ScriptFont:
                currentFont = FontManager.getFontFromAsset(context, "graph_script.ttf");
                break;
            case TextFont.BoldFont:
                currentFont = Typeface.DEFAULT_BOLD;
                break;
            case TextFont.GothicFont:
                currentFont = FontManager.getFontFromAsset(context, "gothic.ttf");
                break;
            default:
                currentFont = Typeface.DEFAULT;
                break;
        }

    }

    public void setFillPattern(int fillPattern) {
        this.fillPattern = fillPattern;
    }
}
