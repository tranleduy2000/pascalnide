package com.duy.pascal.compiler.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import com.duy.pascal.compiler.alogrithm.Queue;
import com.duy.pascal.compiler.view.graph_model.GraphObject;

import java.util.ArrayList;

public class ConsoleView extends View implements GestureDetector.OnGestureListener {
    public static final int QUEUE_SIZE = 1000;
    final public int CursorPaint = Color.DKGRAY;
    public int maxLines = 100;
    public Handler handler = new Handler();
    public int xCursorConsole;
    public int yCursorConsole;
    public int consoleColumn;
    public int consoleRow;
    public int firstLine;
    public boolean fullscreen = false;
    boolean cursorVisible;
    int foreColor;
    int CharHeight;
    int CharAscent;
    int CharDescent;
    int CharWidth;
    private Paint mBackgroundPaint;
    private Paint mTextPaint;
    private Activity activity;
    private int backgroundColor;
    private int visibleWidth = 0;
    private int visibleHeight = 0;
    private int topVisible = 0;
    private int leftVisible = 0;
    private Rect visibleRect;
    private int newHeight;
    private int newWidth;
    private int newTop;
    private int newLeft;
    private int firstIndex;
    private char[] screenBuffer;
    private int screenSize;

    Runnable checkSize = new Runnable() {
        public void run() {
            if (updateSize()) {
                invalidate();
            }
            handler.postDelayed(this, 100);
        }
    };
    private boolean cursorBlink;
    Runnable Blink = new Runnable() {
        public void run() {
            cursorBlink = !cursorBlink;
            invalidate();
            handler.postDelayed(this, 300);
        }
    };
    private Queue inputBuffer;

    private float scrollRemainder;

    private GestureDetector gestureDetector;
    private String TAG = "ConsoleView";
    private ArrayList<GraphObject> graphObjects = new ArrayList<>();
    private int foregroundGraphColor = Color.WHITE;
    private Point cursorGraph = new Point(0, 0);

    public ConsoleView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        commonInit();
    }

    public ConsoleView(Context context, AttributeSet attrs, int defStyles) {
        super(context, attrs, defStyles);
        commonInit();
    }

    public ConsoleView(Context context) {
        super(context);
        commonInit();
    }


    public Point getCursorGraph() {
        return cursorGraph;
    }

    public void setCursorGraph(Point cursorGraph) {
        this.cursorGraph = cursorGraph;
    }

    public void inputChar(char c) {
//        Log.d(TAG, "inputChar: " + c);
        inputBuffer.putByte((byte) c);
    }

    public char getChar() {
        return (char) inputBuffer.getByte();
    }

    public boolean keyPressed() {
        System.out.println(inputBuffer.getRear() + " " + inputBuffer.getFront());
        return inputBuffer.getRear() > inputBuffer.getFront();
    }

    public void setCursor(int x, int y) {
        int index, i;
        yCursorConsole = y;
        index = firstIndex + yCursorConsole * consoleRow;
        if (index >= screenSize) index -= screenSize;
        i = index;

        while (i - index <= x) {
            if (screenBuffer[i] < ' ') break;
            i++;
        }

        while (i - index < x) {
            if (screenBuffer[i] < ' ') screenBuffer[i] = ' ';
            i++;
        }
        xCursorConsole = x;
    }

    public void emitChar(char c) {
        int index = firstIndex + yCursorConsole * consoleRow + xCursorConsole;
        if (index >= screenSize) index -= screenSize;
        switch (c) {
            case '\n':
                screenBuffer[index] = '\n';
                nextLine();
                break;
            case '\177':
            case 8:
                if (xCursorConsole > 0) {
                    xCursorConsole--;
                    screenBuffer[index - 1] = '\0';
                } else {
                    if (yCursorConsole > 0) {
                        if (screenBuffer[index - 1] >= ' ') {
                            screenBuffer[index - 1] = '\0';
                            xCursorConsole = consoleRow - 1;
                            yCursorConsole--;
                            makeCursorVisible();
                        }
                    }
                }
                break;
            default:
                makeCursorVisible();
                if (c >= ' ') {
                    screenBuffer[index] = c;
                    xCursorConsole++;
                    if (xCursorConsole >= consoleRow) {
                        nextLine();
                    }
                }
        }
        postInvalidate();
    }

    public void emitString(String msg) {
        for (int i = 0; i < msg.length(); i++)
            emitChar(msg.charAt(i));
    }

    private void nextLine() {
        xCursorConsole = 0;
        yCursorConsole++;
        if (yCursorConsole >= maxLines) {
            yCursorConsole = maxLines - 1;
            for (int i = 0; i < consoleRow; i++)
                screenBuffer[firstIndex + i] = '\0';
            firstIndex += consoleRow;
            if (firstIndex >= screenSize) firstIndex = 0;
        }
        makeCursorVisible();
    }

    private void getNewDim() {
        getWindowVisibleDisplayFrame(visibleRect);
        if (fullscreen) {
            newTop = Math.min(getTop(), visibleRect.top);
            newHeight = visibleRect.bottom - newTop;
        } else {
            newTop = getTop();
            newHeight = visibleRect.height();
        }
        newWidth = visibleRect.width();
        newLeft = visibleRect.left;
    }

    private void commonInit() {
        mBackgroundPaint = new Paint();
        visibleRect = new Rect();
        gestureDetector = new GestureDetector(this);
        inputBuffer = new Queue(QUEUE_SIZE);
    }

    public void initConsole(Activity a, float fontSize, int foreColor, int backColor) {
        activity = a;
        this.foreColor = foreColor;
        this.backgroundColor = backColor;
        mTextPaint = new Paint();

//        mTextPaint.setTypeface(Typeface.createFromAsset(a.getAssets(), "fonts/courier_new_bold.ttf"));
        mTextPaint.setTypeface(Typeface.MONOSPACE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(fontSize);

        CharHeight = (int) Math.ceil(mTextPaint.getFontSpacing());
        CharAscent = (int) Math.ceil(mTextPaint.ascent());
        CharDescent = CharHeight + CharAscent;
        CharWidth = (int) mTextPaint.measureText(new char[]{'M'}, 0, 1);

        xCursorConsole = 0;
        yCursorConsole = 0;

        firstLine = 0;
        firstIndex = 0;

        screenBuffer = null;
        cursorBlink = true;
        cursorVisible = true;

        mBackgroundPaint.setColor(this.backgroundColor);
    }

    public void clearScreen() {
        int i;
        for (i = 0; i < screenSize; i++) screenBuffer[i] = '\0';
        xCursorConsole = 0;
        yCursorConsole = 0;
        firstLine = 0;
        firstIndex = 0;

        postInvalidate();
    }

    public boolean updateSize() {
        boolean invalid = false;

        getNewDim();

        if ((newWidth != visibleWidth) || (newHeight != visibleHeight)) {
            visibleWidth = newWidth;
            visibleHeight = newHeight;
            tUpdateSize(visibleWidth, visibleHeight);
            invalid = true;
        }
        if ((newLeft != leftVisible) || (newTop != topVisible)) {
            leftVisible = newLeft;
            topVisible = newTop;
            invalid = true;
        }

        if (invalid) postInvalidate();
        return invalid;

    }

    public void makeCursorVisible() {
        if (yCursorConsole - firstLine >= consoleColumn) {
            firstLine = yCursorConsole - consoleColumn + 1;
        } else if (yCursorConsole < firstLine) {
            firstLine = yCursorConsole;
        }

    }

    private int trueIndex(int i, int first, int max) {
        i += first;
        if (i > max) i -= max;
        return i;
    }

    public boolean tUpdateSize(int newWidth, int newHeight) {
        int newNbRows = newWidth / CharWidth;
        int i, j;
        int newFirstIndex = 0;
        int newNbLines = newHeight / CharHeight;
        boolean value = newNbRows != consoleRow || newNbLines != consoleRow;
        consoleColumn = newNbLines;
        if (newNbRows != consoleRow) {

            int newScreenSize = maxLines * newNbRows;
            char newScreenBuffer[] = new char[newScreenSize];

            for (i = 0; i < newScreenSize; i++) {
                newScreenBuffer[i] = '\0';
            }
            if (screenBuffer != null) {
                i = 0;
                int nextj = 0;
                int endi = yCursorConsole * consoleRow + xCursorConsole;
                char c;
                do {

                    j = nextj;
                    do {
                        c = screenBuffer[trueIndex(i++, firstIndex, screenSize)];
                        newScreenBuffer[trueIndex(j++, newFirstIndex, newScreenSize)] = c;
                        newFirstIndex = Math.max(0, j / newNbRows - maxLines + 1) * newNbRows;
                    }
                    while (c >= ' ');
                    i--;
                    j--;

                    i += (consoleRow - i % consoleRow);
                    nextj = j + (newNbRows - j % newNbRows);
                }
                while (i < endi);
                if (c == '\n') j = nextj;
                yCursorConsole = j / newNbRows;
                xCursorConsole = j % newNbRows;

            }
            consoleRow = newNbRows;
            screenSize = newScreenSize;
            screenBuffer = newScreenBuffer;
            firstIndex = newFirstIndex;
        }
        makeCursorVisible();
        return value;
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new BaseInputConnection(this, false) {
            @Override
            public boolean commitText(CharSequence text, int newCursorPosition) {
                int n = text.length();
                for (int i = 0; i < n; i++) {
                    inputChar(text.charAt(i));
                }
                return true;
            }

            @Override
            public boolean performEditorAction(int actionCode) {
                if (actionCode == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    inputChar('\n');
                    return true;
                }
                return false;
            }

            @Override
            public boolean sendKeyEvent(KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    int keyCode = event.getKeyCode();
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        inputChar((char) 8);
                        return true;
                    }
                    char c = (char) event.getUnicodeChar();
                    if (c > 0) {
                        inputChar(c);
                    }
                }
                return true;
            }

            @Override
            public boolean setComposingText(CharSequence text, int newCursorPosition) {
                return true;
            }

            @Override
            public boolean setSelection(int start, int end) {
                return true;
            }

        };
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.isSystem()) {
            return super.onKeyDown(keyCode, event);
        }
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            inputChar((char) 8);
            return true;
        }
        char c = (char) event.getUnicodeChar();
        if (c != '\0') {
            inputChar(c);
            return true;
        }
        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        updateSize();
    }

    private void DrawCursor(Canvas canvas, int x, int y) {
        if (cursorBlink) {

            mTextPaint.setColor(CursorPaint);
            canvas.drawRect(x, y - 1/*+CharAscent*/, x + CharWidth, y + CharDescent, mTextPaint);
        }

    }

    private void RenderChars(Canvas canvas, int x, int y, char[] text, int start, int count) {
        mTextPaint.setColor(foreColor);
        canvas.drawText(text, start, count, x, y, mTextPaint);
    }

    public void tDraw(Canvas canvas, int x, int y) {
        int index = firstIndex + firstLine * consoleRow;
        if (index >= screenSize) index -= screenSize;
        y -= CharAscent;
        if (cursorVisible) {
            DrawCursor(canvas, x + xCursorConsole * CharWidth, y + (yCursorConsole - firstLine) * CharHeight);
        }
        for (int i = 0; i < consoleColumn; i++) {
            if (i > yCursorConsole - firstLine) break;

            int count = 0;
            while ((count < consoleRow) && (screenBuffer[count + index] >= ' ')) count++;
            RenderChars(canvas,
                    x,
                    y,
                    screenBuffer,
                    index,
                    count);
            y += CharHeight;
            index += consoleRow;
            if (index >= screenSize) index = 0;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth();
        int h = getHeight();
        canvas.drawRect(leftVisible, topVisible, w, h, mBackgroundPaint);
        tDraw(canvas, leftVisible, topVisible);
        for (GraphObject graphObject : graphObjects) {
            graphObject.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return gestureDetector.onTouchEvent(ev);
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        distanceY += scrollRemainder;
        int deltaRows = (int) (distanceY / CharHeight);
        scrollRemainder = distanceY - deltaRows * CharHeight;
        firstLine = Math.max(0, Math.min(firstLine + deltaRows, yCursorConsole));
        invalidate();
        return true;
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        scrollRemainder = 0.0f;
        onScroll(e1, e2, /* 2 * */ velocityX, -/*2 * */ velocityY);
        return true;
    }

    public void onShowPress(MotionEvent e) {
    }

    public boolean onSingleTapUp(MotionEvent e) {
        doShowSoftKeyboard();
        return true;
    }

    public boolean onDown(MotionEvent e) {
        scrollRemainder = 0.0f;
        return true;
    }

    public void onLongPress(MotionEvent e) {
        showContextMenu();
    }

    public void onPause() {
        handler.removeCallbacks(checkSize);
        handler.removeCallbacks(Blink);
    }

    public void onResume() {
        handler.postDelayed(checkSize, 1000);
        handler.postDelayed(Blink, 500);
        updateSize();
    }

    private void doShowSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this, 0);
    }

    ///////////////////////////////////////////////////////////////////////////////
    ///////////           THIS METHOD USES BY PASCAL LIB         //////////////////
    ///////////////////////////////////////////////////////////////////////////////
    //pascal
    public void setTextColor(int textColor) {
        Log.d(TAG, "setTextColor: ");
        this.foreColor = textColor;
        invalidate();

    }

    //pascal
    public void setConsoleColor(int color) {
        this.backgroundColor = color;
        mBackgroundPaint.setColor(color);
        invalidate();
    }

    /**
     * Draw part of a circle
     *
     * @param x
     * @param y
     * @param stAngle
     * @param endAngle
     * @param radius
     */
    public void arc(int x, int y, int stAngle, int endAngle, int radius) {
        // TODO: 01-Mar-17 write draw arc
    }

    public void gotoXY(int x, int y) {
        if (x <= 0) x = 1;
        else if (x > consoleRow) x = consoleRow;
        if (y <= 0) y = 1;
        else if (y > maxLines) y = maxLines;
        setCursor(x - 1, y - 1);
        makeCursorVisible();

        postInvalidate();
    }

    /**
     * return x coordinate of cursor in console
     *
     * @return
     */
    public int whereX() {
        return xCursorConsole + 1;
    }

    /**
     * return y coordinate of cursor in console*
     *
     * @return
     */
    public int whereY() {
        return yCursorConsole + 1;
    }

    //pascal
    public int getColorPixel(int x, int y) {
        Bitmap bitmap = getDrawingCache();
        return bitmap.getPixel(x, y);
    }

    //pascal
    public void addGraphObject(GraphObject graphObject) {
        graphObject.setPaintColor(foregroundGraphColor);
        graphObjects.add(graphObject);
    }

    //pascal
    public int getXCursorPixel() {
        return leftVisible + xCursorConsole * CharWidth;
    }

    //pascal
    public int getYCursorPixel() {
        return topVisible + (yCursorConsole - firstLine) * CharHeight;
    }

    /**
     * mode graph
     *
     * @return Return current drawing color
     */
    public int getForegroundGraphColor() {
        return foregroundGraphColor;
    }

    /**
     * set draw {@link GraphObject} color
     *
     * @param currentColor
     */
    public void setForegroundGraphColor(int currentColor) {
        this.foregroundGraphColor = currentColor;
    }

    //pascal
    public void closeGraph() {
        graphObjects.clear();
        invalidate();
    }

    //pascal
    public void clearGraph() {
        graphObjects.clear();
        cursorGraph.set(0, 0);
        clearScreen();

    }

    //pascal
    public void setPointGraph(int x, int y) {
        cursorGraph.set(x, y);
    }
}





