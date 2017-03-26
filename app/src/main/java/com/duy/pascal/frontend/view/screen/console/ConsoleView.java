package com.duy.pascal.frontend.view.screen.console;

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

import com.duy.pascal.frontend.view.screen.graph.molel.GraphObject;

import java.util.ArrayList;

public class ConsoleView extends View implements GestureDetector.OnGestureListener {
    public final int cursorColor = Color.DKGRAY;
    public int maxLines = 200;
    public Handler handler = new Handler();
    public int consoleColumn;
    public int consoleRow;
    public int firstLine;
    public boolean fullscreen = false;
    boolean cursorVisible;
    int foregroundColor;
    int CharHeight;
    int charAscent;
    int charDescent;
    int charWidth;
    private CursorConsole mCursorConsole;
    private Paint mBackgroundPaint = new Paint();
    private Paint mTextPaint = new Paint();
    private Activity activity;
    private int backgroundColor;
    private int visibleWidth = 0;
    private int visibleHeight = 0;
    private int topVisible = 0;
    private int leftVisible = 0;
    private Rect visibleRect = new Rect();
    private int newHeight;
    private int newWidth;
    private int newTop;
    private int newLeft;
    private int firstIndex;
    private char[] screenBuffer;
    private int screenSize;
    private Runnable checkSize = new Runnable() {
        public void run() {
            if (updateSize()) {
                invalidate();
            }
            handler.postDelayed(this, 100);
        }
    };
    private boolean cursorBlink;
    private Runnable blink = new Runnable() {
        public void run() {
            cursorBlink = !cursorBlink;
            invalidate();
            handler.postDelayed(this, 300);
        }
    };
    private ByteQueue inputBuffer = new ByteQueue();

    private float scrollRemainder;

    private GestureDetector mGestureDetector;
    private String TAG = ConsoleView.class.getSimpleName();
    private ArrayList<GraphObject> graphObjects = new ArrayList<>();
    private int foregroundGraphColor = Color.WHITE;
    private Point cursorGraph = new Point(0, 0);

    public ConsoleView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public ConsoleView(Context context, AttributeSet attrs, int defStyles) {
        super(context, attrs, defStyles);
        init();
    }

    public ConsoleView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mGestureDetector = new GestureDetector(this);
        mCursorConsole = new CursorConsole(0, 0, Color.DKGRAY);
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

    public char readChar() {
        return (char) inputBuffer.getByte();
    }

    public boolean keyPressed() {
        return inputBuffer.rear > inputBuffer.front;
    }

    /**
     * set cursor index
     *
     * @param x
     * @param y
     */
    public void setCursor(int x, int y) {
        int index, i;
        mCursorConsole.yCoordinate = y;
        index = firstIndex + mCursorConsole.yCoordinate * consoleRow;
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
        mCursorConsole.xCoordinate = x;
    }

    public void emitChar(char c) {
        int index = firstIndex + mCursorConsole.yCoordinate * consoleRow + mCursorConsole.xCoordinate;
        if (index >= screenSize) index -= screenSize;
        switch (c) {
            case '\n':
                screenBuffer[index] = '\n';
                nextLine();
                break;
            case '\177':
            case 8:
                if (mCursorConsole.xCoordinate > 0) {
                    mCursorConsole.xCoordinate--;
                    screenBuffer[index - 1] = '\0';
                } else {
                    if (mCursorConsole.yCoordinate > 0) {
                        if (screenBuffer[index - 1] >= ' ') {
                            screenBuffer[index - 1] = '\0';
                            mCursorConsole.xCoordinate = consoleRow - 1;
                            mCursorConsole.yCoordinate--;
                            makeCursorVisible();
                        }
                    }
                }
                break;
            default:
                makeCursorVisible();
                if (c >= ' ') {
                    screenBuffer[index] = c;
                    mCursorConsole.xCoordinate++;
                    if (mCursorConsole.xCoordinate >= consoleRow) {
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
        mCursorConsole.xCoordinate = 0;
        mCursorConsole.yCoordinate++;
        if (mCursorConsole.yCoordinate >= maxLines) {
            mCursorConsole.yCoordinate = maxLines - 1;
            for (int i = 0; i < consoleRow; i++)
                screenBuffer[firstIndex + i] = '\0';
            firstIndex += consoleRow;
            if (firstIndex >= screenSize) firstIndex = 0;
        }
        makeCursorVisible();
    }

    private void getNewDimen() {
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


    public void showPrompt() {
        emitString("Initialize the console screen..." + "\n");
        emitString("Width = " + consoleColumn + " ; " + " height = " + consoleRow + "\n");
        emitString("---------------------------" + "\n");
    }

    public void initConsole(Activity a, float fontSize, int foreColor, int backColor) {
        activity = a;
        this.foregroundColor = foreColor;
        this.backgroundColor = backColor;
        mTextPaint = new Paint();

        mTextPaint.setTypeface(Typeface.MONOSPACE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(fontSize);

        CharHeight = (int) Math.ceil(mTextPaint.getFontSpacing());
        charAscent = (int) Math.ceil(mTextPaint.ascent());
        charDescent = CharHeight + charAscent;
        charWidth = (int) mTextPaint.measureText(new char[]{'M'}, 0, 1);

        mCursorConsole.xCoordinate = 0;
        mCursorConsole.yCoordinate = 0;

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
        mCursorConsole.xCoordinate = 0;
        mCursorConsole.yCoordinate = 0;
        firstLine = 0;
        firstIndex = 0;
        postInvalidate();
    }

    /**
     * Update text size
     *
     * @return
     */
    public boolean updateSize() {
        boolean invalid = false;
        getNewDimen();
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
        if (mCursorConsole.yCoordinate - firstLine >= consoleColumn) {
            firstLine = mCursorConsole.yCoordinate - consoleColumn + 1;
        } else if (mCursorConsole.yCoordinate < firstLine) {
            firstLine = mCursorConsole.yCoordinate;
        }

    }

    private int trueIndex(int i, int first, int max) {
        i += first;
        if (i > max) i -= max;
        return i;
    }

    public boolean tUpdateSize(int newWidth, int newHeight) {
        int newNbRows = newWidth / charWidth;
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
                int endi = mCursorConsole.yCoordinate * consoleRow + mCursorConsole.xCoordinate;
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
                mCursorConsole.yCoordinate = j / newNbRows;
                mCursorConsole.xCoordinate = j % newNbRows;

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

    private void drawCursor(Canvas canvas, int x, int y) {
        if (cursorBlink) {
            mTextPaint.setColor(cursorColor);
//            canvas.drawRect(xCoordinate, yCoordinate - 1, xCoordinate + charWidth, yCoordinate - charDescent, mTextPaint);
            canvas.drawRect(x + 1, y - CharHeight + charDescent, x + charWidth, y + 1, mTextPaint);
        }
    }

    private void renderChars(Canvas canvas, int x, int y, char[] text, int start, int count) {
        mTextPaint.setColor(foregroundColor);
        canvas.drawText(text, start, count, x, y, mTextPaint);
    }

    public void tDraw(Canvas canvas, int x, int y) {
        int index = firstIndex + firstLine * consoleRow;
        if (index >= screenSize) index -= screenSize;
        y -= charAscent;
        if (cursorVisible) {
            drawCursor(canvas, x + mCursorConsole.xCoordinate * charWidth, y + (mCursorConsole.yCoordinate - firstLine) * CharHeight);
        }
        for (int i = 0; i < consoleColumn; i++) {
            if (i > mCursorConsole.yCoordinate - firstLine) break;

            int count = 0;
            while ((count < consoleRow) && (screenBuffer[count + index] >= ' ')) count++;
            renderChars(canvas,
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

        if (graphObjects != null) {
            for (GraphObject graphObject : graphObjects) {
                graphObject.draw(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mGestureDetector.onTouchEvent(ev);
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        distanceY += scrollRemainder;
        int deltaRows = (int) (distanceY / CharHeight);
        scrollRemainder = distanceY - deltaRows * CharHeight;
        firstLine = Math.max(0, Math.min(firstLine + deltaRows, mCursorConsole.yCoordinate));
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
        handler.removeCallbacks(blink);
    }

    public void onResume() {
        handler.postDelayed(checkSize, 1000);
        handler.postDelayed(blink, 500);
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
        this.foregroundColor = textColor;
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
     * return xCoordinate coordinate of cursor in console
     *
     * @return
     */
    public int whereX() {
        return mCursorConsole.xCoordinate + 1;
    }

    /**
     * return yCoordinate coordinate of cursor in console*
     *
     * @return
     */
    public int whereY() {
        return mCursorConsole.yCoordinate + 1;
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
        return leftVisible + mCursorConsole.xCoordinate * charWidth;
    }

    //pascal
    public int getYCursorPixel() {
        return topVisible + (mCursorConsole.yCoordinate - firstLine) * CharHeight;
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





