package com.duy.pascal.frontend.view.screen.console;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
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
    public Handler handler = new Handler();
    public int firstLine;

    /**
     * text style, size of console
     */
    private TextRenderer mTextRenderer;

    /**
     * store screen size and dimen
     */
    private Screen mConsoleScreen = new Screen();
    /**
     * Cursor of console
     */
    private CursorConsole mCursorConsole;

    /**
     * Parent mActivity
     */
    private Activity mActivity;

    /**
     * Data of console
     */
    private ScreenBuffer bufferData = new ScreenBuffer();

    private Rect visibleRect = new Rect();


    private Runnable checkSize = new Runnable() {
        public void run() {
            if (updateSize()) {
                invalidate();
            }
            handler.postDelayed(this, 100);
        }
    };

    private Runnable blink = new Runnable() {
        public void run() {
            mCursorConsole.toggleState();
            invalidate();
            handler.postDelayed(this, 300);
        }
    };
    private float scrollRemainder;

    private GestureDetector mGestureDetector;

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
        bufferData.inputBuffer.putByte((byte) c);
    }

    public char readChar() {
        return (char) bufferData.inputBuffer.getByte();
    }

    public boolean keyPressed() {
        return bufferData.inputBuffer.rear > bufferData.inputBuffer.front;
    }

    /**
     * set cursor index
     */
    public void setCursorCoordinates(int x, int y) {
        int index, i;
        mCursorConsole.yCoordinate = y;
        index = bufferData.firstIndex + mCursorConsole.yCoordinate * mConsoleScreen.consoleRow;
        if (index >= mConsoleScreen.getScreenSize()) index -= mConsoleScreen.getScreenSize();
        i = index;

        while (i - index <= x) {
            if (bufferData.screenBuffer[i] < ' ') break;
            i++;
        }

        while (i - index < x) {
            if (bufferData.screenBuffer[i] < ' ') bufferData.screenBuffer[i] = ' ';
            i++;
        }
        mCursorConsole.xCoordinate = x;
    }

    public void commitChar(char c) {
        int index = bufferData.firstIndex + mCursorConsole.yCoordinate * mConsoleScreen.consoleRow + mCursorConsole.xCoordinate;
        if (index >= mConsoleScreen.getScreenSize()) index -= mConsoleScreen.getScreenSize();
        switch (c) {
            case '\n':
                bufferData.screenBuffer[index] = '\n';
                nextLine();
                break;
            case '\177':
            case 8:
                if (mCursorConsole.xCoordinate > 0) {
                    mCursorConsole.xCoordinate--;
                    bufferData.screenBuffer[index - 1] = '\0';
                } else {
                    if (mCursorConsole.yCoordinate > 0) {
                        if (bufferData.screenBuffer[index - 1] >= ' ') {
                            bufferData.screenBuffer[index - 1] = '\0';
                            mCursorConsole.xCoordinate = mConsoleScreen.consoleRow - 1;
                            mCursorConsole.yCoordinate--;
                            makeCursorVisible();
                        }
                    }
                }
                break;
            default:
                makeCursorVisible();
                if (c >= ' ') {
                    bufferData.screenBuffer[index] = c;
                    mCursorConsole.xCoordinate++;
                    if (mCursorConsole.xCoordinate >= mConsoleScreen.consoleRow) {
                        nextLine();
                    }
                }
        }
        postInvalidate();
    }

    public void commitString(String msg) {
        for (int i = 0; i < msg.length(); i++)
            commitChar(msg.charAt(i));
    }

    /**
     * move cursor to new line
     */
    private void nextLine() {
        mCursorConsole.xCoordinate = 0;
        mCursorConsole.yCoordinate++;
        if (mCursorConsole.yCoordinate >= mConsoleScreen.maxLines) {
            mCursorConsole.yCoordinate = mConsoleScreen.maxLines - 1;
            for (int i = 0; i < mConsoleScreen.consoleRow; i++)
                bufferData.screenBuffer[bufferData.firstIndex + i] = '\0';
            bufferData.firstIndex += mConsoleScreen.consoleRow;
            if (bufferData.firstIndex >= mConsoleScreen.getScreenSize()) bufferData.firstIndex = 0;
        }
        makeCursorVisible();
    }


    public void showPrompt() {
        commitString("Initialize the console screen..." + "\n");
        commitString("Width = " + mConsoleScreen.consoleColumn + " ; " + " height = " + mConsoleScreen.consoleRow + "\n");
        commitString("---------------------------" + "\n");
    }

    public void initConsole(Activity a, float fontSize, int textColor, int backColor) {
        mActivity = a;

        mConsoleScreen.setBackgroundColor(backColor);

        mTextRenderer = new TextRenderer(fontSize);
        mTextRenderer.setTextColor(textColor);

        mCursorConsole.xCoordinate = 0;
        mCursorConsole.yCoordinate = 0;

        firstLine = 0;
        bufferData.firstIndex = 0;
        bufferData.screenBuffer = null;

        mCursorConsole.setCursorBlink(true);
        mCursorConsole.setVisible(true);
    }

    /**
     * clear screen
     * clrscr; in pascal
     */
    public void clearScreen() {
        for (int i = 0; i < mConsoleScreen.getScreenSize(); i++)
            bufferData.screenBuffer[i] = '\0';
        mCursorConsole.setCoordinate(0, 0);
        firstLine = 0;
        bufferData.firstIndex = 0;
        postInvalidate();
    }

    /**
     * Update text size
     *
     * @return
     */
    public boolean updateSize() {
        boolean invalid = false;

        getWindowVisibleDisplayFrame(visibleRect);
        int newHeight;
        int newWidth;
        int newTop;
        int newLeft;

        if (mConsoleScreen.isFullScreen()) {
            newTop = Math.min(getTop(), visibleRect.top);
            newHeight = visibleRect.bottom - newTop;
        } else {
            newTop = getTop();
            newHeight = visibleRect.height();
        }
        newWidth = visibleRect.width();
        newLeft = visibleRect.left;

        if ((newWidth != mConsoleScreen.getVisibleWidth()) || (newHeight != mConsoleScreen.getVisibleHeight())) {
            mConsoleScreen.setVisibleWidth(newWidth);
            mConsoleScreen.setVisibleHeight(newHeight);
            tUpdateSize(mConsoleScreen.getVisibleWidth(), mConsoleScreen.getVisibleHeight());
            invalid = true;
        }
        if ((newLeft != mConsoleScreen.getLeftVisible()) || (newTop != mConsoleScreen.getTopVisible())) {
            mConsoleScreen.setLeftVisible(newLeft);
            mConsoleScreen.setTopVisible(newTop);
            invalid = true;
        }

        if (invalid) postInvalidate();
        return invalid;

    }

    public void makeCursorVisible() {
        if (mCursorConsole.yCoordinate - firstLine >= mConsoleScreen.consoleColumn) {
            firstLine = mCursorConsole.yCoordinate - mConsoleScreen.consoleColumn + 1;
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
        int newNbRows = newWidth / mTextRenderer.charWidth;
        int i, j;
        int newFirstIndex = 0;
        int newNbLines = newHeight / mTextRenderer.charHeight;
        boolean value = newNbRows != mConsoleScreen.consoleRow || newNbLines != mConsoleScreen.consoleRow;
        mConsoleScreen.consoleColumn = newNbLines;
        if (newNbRows != mConsoleScreen.consoleRow) {
            int newScreenSize = mConsoleScreen.maxLines * newNbRows;
            char newScreenBuffer[] = new char[newScreenSize];

            for (i = 0; i < newScreenSize; i++) {
                newScreenBuffer[i] = '\0';
            }
            if (bufferData.screenBuffer != null) {
                i = 0;
                int nextj = 0;
                int endi = mCursorConsole.yCoordinate * mConsoleScreen.consoleRow + mCursorConsole.xCoordinate;
                char c;
                do {

                    j = nextj;
                    do {
                        c = bufferData.screenBuffer[trueIndex(i++, bufferData.firstIndex, mConsoleScreen.getScreenSize())];
                        newScreenBuffer[trueIndex(j++, newFirstIndex, newScreenSize)] = c;
                        newFirstIndex = Math.max(0, j / newNbRows - mConsoleScreen.maxLines + 1) * newNbRows;
                    }
                    while (c >= ' ');
                    i--;
                    j--;

                    i += (mConsoleScreen.consoleRow - i % mConsoleScreen.consoleRow);
                    nextj = j + (newNbRows - j % newNbRows);
                }
                while (i < endi);
                if (c == '\n') j = nextj;
                mCursorConsole.yCoordinate = j / newNbRows;
                mCursorConsole.xCoordinate = j % newNbRows;

            }
            mConsoleScreen.consoleRow = newNbRows;
            mConsoleScreen.setScreenSize(newScreenSize);
            bufferData.screenBuffer = newScreenBuffer;
            bufferData.firstIndex = newFirstIndex;
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

    public void tDraw(Canvas canvas, int x, int y) {
        int index = bufferData.firstIndex + firstLine * mConsoleScreen.consoleRow;
        if (index >= mConsoleScreen.getScreenSize()) index -= mConsoleScreen.getScreenSize();
        y -= mTextRenderer.charAscent;

        //draw cursor
        mCursorConsole.drawCursor(canvas,
                x + mCursorConsole.xCoordinate * mTextRenderer.charWidth,
                y + (mCursorConsole.yCoordinate - firstLine) * mTextRenderer.charHeight,
                mTextRenderer.charHeight, mTextRenderer.charWidth, mTextRenderer.charDescent);

        for (int i = 0; i < mConsoleScreen.consoleColumn; i++) {
            if (i > mCursorConsole.yCoordinate - firstLine) break;

            int count = 0;
            while ((count < mConsoleScreen.consoleRow) && (bufferData.screenBuffer[count + index] >= ' '))
                count++;
            mTextRenderer.draw(canvas, x, y, bufferData.screenBuffer, index, count);
            y += mTextRenderer.charHeight;
            index += mConsoleScreen.consoleRow;
            if (index >= mConsoleScreen.getScreenSize()) index = 0;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth();
        int h = getHeight();
        mConsoleScreen.draw(canvas, mConsoleScreen.getLeftVisible(), mConsoleScreen.getTopVisible(), w, h);
        tDraw(canvas, mConsoleScreen.getLeftVisible(), mConsoleScreen.getTopVisible());
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
        int deltaRows = (int) (distanceY / mTextRenderer.charHeight);
        scrollRemainder = distanceY - deltaRows * mTextRenderer.charHeight;
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
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this, 0);
    }

    ///////////////////////////////////////////////////////////////////////////////
    ///////////           THIS METHOD USES BY PASCAL LIBRARY         //////////////////
    ///////////////////////////////////////////////////////////////////////////////
    //pascal
    public void setTextColor(int textColor) {
        mTextRenderer.setTextColor(textColor);
        postInvalidate();
    }

    //pascal
    public void setConsoleColor(int color) {
        mConsoleScreen.setBackgroundColor(color);
        postInvalidate();
    }

    /**
     * Draw part of a circle
     *
     */
    public void arc(int x, int y, int stAngle, int endAngle, int radius) {
        // TODO: 01-Mar-17 write draw arc
    }

    /**
     * move cursor to (x, y)
     */
    public void gotoXY(int x, int y) {
        if (x <= 0) x = 1;
        else if (x > mConsoleScreen.consoleRow) x = mConsoleScreen.consoleRow;
        if (y <= 0) y = 1;
        else if (y > mConsoleScreen.maxLines) y = mConsoleScreen.maxLines;
        setCursorCoordinates(x - 1, y - 1);
        makeCursorVisible();
        postInvalidate();
    }

    /**
     * return x coordinate of cursor in console
     *
     * @return
     */
    public int whereX() {
        return mCursorConsole.xCoordinate + 1;
    }

    /**
     * return y coordinate of cursor in console*
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
        return mConsoleScreen.getLeftVisible() + mCursorConsole.xCoordinate * mTextRenderer.charWidth;
    }

    //pascal
    public int getYCursorPixel() {
        return mConsoleScreen.getTopVisible() + (mCursorConsole.yCoordinate - firstLine) * mTextRenderer.charHeight;
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





