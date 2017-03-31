package com.duy.pascal.frontend.view.screen.console;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.data.PascalPreferences;
import com.duy.pascal.frontend.view.screen.graph.molel.ArcObject;
import com.duy.pascal.frontend.view.screen.graph.molel.GraphObject;

public class ConsoleView extends View implements GestureDetector.OnGestureListener {
    public static final String TAG = ConsoleView.class.getSimpleName();
    public Handler handler = new Handler();
    public int firstLine;
    boolean graphMode = true;
    private GraphScreen mGraphScreen;

    public GraphScreen getGraphScreen() {
        return mGraphScreen;
    }

    /**
     * text style, size of console
     */
    private TextRenderer mTextRenderer;
    /**
     * store screen size and dimen
     */
    private ConsoleScreen mScreen = new ConsoleScreen();
    /**
     * Cursor of console
     */
    private CursorConsole mCursor;
    /**
     * Parent mActivity
     */
    private Activity mActivity;
    /**
     * Data of console
     */
    private ScreenBuffer bufferData = new ScreenBuffer();
    private int frameRate = 60;
    private Rect visibleRect = new Rect();
    private Runnable checkSize = new Runnable() {
        public void run() {
            if (updateSize()) {
                invalidate();
            }
            handler.postDelayed(this, frameRate);
        }
    };
    private Runnable blink = new Runnable() {
        public void run() {
            mCursor.toggleState();
            invalidate();
            handler.postDelayed(this, 300);
        }
    };
    private float scrollRemainder;
    private GestureDetector mGestureDetector;
    private int foregroundGraphColor = Color.WHITE;
    private boolean filterKey = false;
    private PascalPreferences mPascalPreferences;

    public ConsoleView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    public ConsoleView(Context context, AttributeSet attrs, int defStyles) {
        super(context, attrs, defStyles);
        init(context);
    }

    public ConsoleView(Context context) {
        super(context);
        init(context);
    }

    public boolean isFilterKey() {
        return filterKey;
    }

    public void setFilterKey(boolean filterKey) {
        this.filterKey = filterKey;
    }

    private void init(Context context) {
        mGestureDetector = new GestureDetector(this);
        mCursor = new CursorConsole(0, 0, Color.DKGRAY);
        mPascalPreferences = new PascalPreferences(context);
        frameRate = mPascalPreferences.getConsoleFrameRate();
        mScreen.setMaxLines(mPascalPreferences.getConsoleMaxBuffer());
        mGraphScreen = new GraphScreen(context);
    }

    public void putChar(char c) {
        bufferData.textBuffer.putByte((byte) c);
    }

    public char readChar() {
        return (char) bufferData.textBuffer.getByte();
    }

    public char readKey() {
        return (char) bufferData.keyBuffer.getByte();
    }

    public boolean keyPressed() {
        return bufferData.textBuffer.rear > bufferData.textBuffer.front;
    }

    /**
     * set cursor index
     */
    public void setConsoleCursorPosition(int x, int y) {
        int index, i;
        mCursor.y = y;
        index = bufferData.firstIndex + mCursor.y * mScreen.row;
        if (index >= mScreen.getScreenSize()) index -= mScreen.getScreenSize();
        i = index;

        while (i - index <= x) {
            if (bufferData.screenBuffer[i] < ' ') break;
            i++;
        }

        while (i - index < x) {
            if (bufferData.screenBuffer[i] < ' ') bufferData.screenBuffer[i] = ' ';
            i++;
        }
        mCursor.x = x;
    }

    public void commitChar(char c) {
        int index = bufferData.firstIndex + mCursor.y * mScreen.row + mCursor.x;
//        Log.d(TAG, "commitChar: " + index);
        if (index >= mScreen.getScreenSize()) index -= mScreen.getScreenSize();
        switch (c) {
            case '\n':
                bufferData.screenBuffer[index] = '\n';
                nextLine();
                break;
            case '\177':
            case 8:
                if (mCursor.x > 0) {
                    mCursor.x--;
                    bufferData.screenBuffer[index - 1] = '\0';
                } else {
                    if (mCursor.y > 0) {
                        if (bufferData.screenBuffer[index - 1] >= ' ') {
                            bufferData.screenBuffer[index - 1] = '\0';
                            mCursor.x = mScreen.row - 1;
                            mCursor.y--;
                            makeCursorVisible();
                        }
                    }
                }
                break;
            default:
                makeCursorVisible();
                if (c >= ' ') {
                    bufferData.screenBuffer[index] = c;
                    mCursor.x++;
                    if (mCursor.x >= mScreen.row) {
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
        mCursor.x = 0;
        mCursor.y++;
        if (mCursor.y >= mScreen.maxLines) {
            mCursor.y = mScreen.maxLines - 1;
            for (int i = 0; i < mScreen.row; i++)
                bufferData.screenBuffer[bufferData.firstIndex + i] = '\0';
            bufferData.firstIndex += mScreen.row;
            if (bufferData.firstIndex >= mScreen.getScreenSize()) bufferData.firstIndex = 0;
        }
        makeCursorVisible();
    }

    public void showPrompt() {
        commitString("Initialize the console screen..." + "\n");
        commitString("Width = " + mScreen.consoleColumn + " ; " + " height = " + mScreen.row + "\n");
        commitString("---------------------------" + "\n");
    }

    public float getTextSize(int unit, float size) {
        Context c = getContext();
        Resources r;
        if (c == null)
            r = Resources.getSystem();
        else
            r = c.getResources();
        return TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
    }

    public void initConsole(Activity a, float fontSize, int textColor, int backColor) {
        mActivity = a;
        mScreen.setBackgroundColor(backColor);
        mTextRenderer = new TextRenderer(getTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize));
        mTextRenderer.setTextColor(textColor);

        mCursor.x = 0;
        mCursor.y = 0;

        firstLine = 0;
        bufferData.firstIndex = 0;
        bufferData.screenBuffer = null;

        mCursor.setCursorBlink(true);
        mCursor.setVisible(true);
    }

    /**
     * clear screen
     * clrscr; in pascal
     */
    public void clearScreen() {
        for (int i = 0; i < mScreen.getScreenSize(); i++)
            bufferData.screenBuffer[i] = '\0';
        mCursor.setCoordinate(0, 0);
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

        if (mScreen.isFullScreen()) {
            newTop = Math.min(getTop(), visibleRect.top);
            newHeight = visibleRect.bottom - newTop;
        } else {
            newTop = getTop();
            newHeight = visibleRect.height();
        }
        newWidth = visibleRect.width();
        newLeft = visibleRect.left;

        if ((newWidth != mScreen.getVisibleWidth()) || (newHeight != mScreen.getVisibleHeight())) {
            mScreen.setVisibleWidth(newWidth);
            mScreen.setVisibleHeight(newHeight);
            tUpdateSize(mScreen.getVisibleWidth(), mScreen.getVisibleHeight());
            invalid = true;
        }
        if ((newLeft != mScreen.getLeftVisible()) || (newTop != mScreen.getTopVisible())) {
            mScreen.setLeftVisible(newLeft);
            mScreen.setTopVisible(newTop);
            invalid = true;
        }

        if (invalid) postInvalidate();
        return invalid;

    }

    public void makeCursorVisible() {
        if (mCursor.y - firstLine >= mScreen.consoleColumn) {
            firstLine = mCursor.y - mScreen.consoleColumn + 1;
        } else if (mCursor.y < firstLine) {
            firstLine = mCursor.y;
        }
    }

    private int trueIndex(int i, int first, int max) {
        i += first;
        if (i > max) i -= max;
        return i;
    }

    public boolean tUpdateSize(int newWidth, int newHeight) {
        int newRow = newWidth / mTextRenderer.charWidth;
        int i, j;
        int newFirstIndex = 0;
        int newColumn = newHeight / mTextRenderer.charHeight;
        boolean value = newRow != mScreen.row || newColumn != mScreen.row;
        mScreen.consoleColumn = newColumn;
        if (newRow != mScreen.row) {
            int newScreenSize = mScreen.maxLines * newRow;
            char newScreenBuffer[] = new char[newScreenSize];

            for (i = 0; i < newScreenSize; i++) {
                newScreenBuffer[i] = '\0';
            }

            if (bufferData.screenBuffer != null) {
                i = 0;
                int nextj = 0;
                int endi = mCursor.y * mScreen.row
                        + mCursor.x;
                char c;
                do {
                    j = nextj;
                    do {
                        c = bufferData.screenBuffer[trueIndex(i++, bufferData.firstIndex, mScreen.getScreenSize())];
                        newScreenBuffer[trueIndex(j++, newFirstIndex, newScreenSize)] = c;
                        newFirstIndex = Math.max(0, j / newRow - mScreen.maxLines + 1) * newRow;
                    }
                    while (c >= ' ');
                    i--;
                    j--;

                    i += (mScreen.row - i % mScreen.row);
                    nextj = j + (newRow - j % newRow);
                }
                while (i < endi);
                if (c == '\n') j = nextj;
                mCursor.y = j / newRow;
                mCursor.x = j % newRow;
            }
            mScreen.row = newRow;
            mScreen.setScreenSize(newScreenSize);
            bufferData.setScreenBuffer(newScreenBuffer);
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
        outAttrs.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
        return new BaseInputConnection(this, false) {
            @Override
            public boolean commitText(CharSequence text, int newCursorPosition) {
                int n = text.length();
                for (int i = 0; i < n; i++) {
                    putChar(text.charAt(i));
                }
                return true;
            }

            @Override
            public boolean performEditorAction(int actionCode) {
                if (actionCode == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    putChar('\n');
                    return true;
                }
                return false;
            }

            @Override
            public boolean deleteSurroundingText(int beforeLength, int afterLength) {
                // magic: in latest Android, deleteSurroundingText(1, 0) will be called for backspace
                if (beforeLength == 1 && afterLength == 0) {
                    // backspace
                    return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                            && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
                }

                return super.deleteSurroundingText(beforeLength, afterLength);
            }

            @Override
            public boolean sendKeyEvent(KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    int keyCode = event.getKeyCode();
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        putChar((char) 8);
                        return true;
                    }
                    char c = (char) event.getUnicodeChar();
                    if (c > 0) {
                        putChar(c);
                        return true;
                    }
                }
//                return true;
                return super.sendKeyEvent(event);
            }

        };
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (DLog.DEBUG) Log.d(TAG, "onKeyDown: " + event);

        if (event.isSystem()) {
            return super.onKeyDown(keyCode, event);
        }
        if (filterKey) {
            bufferData.keyBuffer.putByte((byte) event.getUnicodeChar());
            return true;
        } else {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                putChar((char) 8);
                return true;
            }
            char c = (char) event.getUnicodeChar();
            if (c != '\0') {
                putChar(c);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (DLog.DEBUG) Log.d(TAG, "onKeyUp: " + event);
        if (event.isSystem()) {
            return super.onKeyDown(keyCode, event);
        }
        if (filterKey) {
            bufferData.keyBuffer.putByte((byte) event.getUnicodeChar());
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mGraphScreen.onSizeChange(w, h);
        updateSize();
    }

    public void tDraw(Canvas canvas, int x, int y) {
        int index = bufferData.firstIndex + firstLine * mScreen.row;
        if (index >= mScreen.getScreenSize()) index -= mScreen.getScreenSize();
        y -= mTextRenderer.charAscent;

        //draw cursor
        mCursor.drawCursor(canvas,
                x + mCursor.x * mTextRenderer.charWidth,
                y + (mCursor.y - firstLine) * mTextRenderer.charHeight,
                mTextRenderer.charHeight, mTextRenderer.charWidth, mTextRenderer.charDescent);

        for (int i = 0; i < mScreen.consoleColumn; i++) {
            if (i > mCursor.y - firstLine) break;

            int count = 0;
            while ((count < mScreen.row) && (bufferData.screenBuffer[count + index] >= ' '))
                count++;
            mTextRenderer.draw(canvas, x, y, bufferData.screenBuffer, index, count);
            y += mTextRenderer.charHeight;
            index += mScreen.row;
            if (index >= mScreen.getScreenSize()) index = 0;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth();
        int h = getHeight();

        /**
         * draw bitmap graph
         */

        mScreen.draw(canvas, mScreen.getLeftVisible(), mScreen.getTopVisible(), w, h);
        tDraw(canvas, mScreen.getLeftVisible(), mScreen.getTopVisible());

        if (graphMode)
            canvas.drawBitmap(mGraphScreen.getGraphBitmap(), 0, 0, null);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mGestureDetector.onTouchEvent(ev);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        distanceY += scrollRemainder;
        int deltaRows = (int) (distanceY / mTextRenderer.charHeight);
        scrollRemainder = distanceY - deltaRows * mTextRenderer.charHeight;
        firstLine = Math.max(0, Math.min(firstLine + deltaRows, mCursor.y));
        invalidate();
        return true;
    }

    @Override
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
    ///////////           THIS METHOD USES BY PASCAL LIBRARY      //////////////////
    ///////////                      CRT LIB                      //////////////////
    ///////////////////////////////////////////////////////////////////////////////
    //pascal
    public void setTextColor(int textColor) {
        mTextRenderer.setTextColor(textColor);
        postInvalidate();
    }

    //pascal
    public void setConsoleColor(int color) {
        mScreen.setBackgroundColor(color);
        postInvalidate();
    }

    /**
     * Draw part of a circle
     */
    public void arc(int x, int y, int stAngle, int endAngle, int radius) {
        mGraphScreen.addGraphObject(new ArcObject(x, y, stAngle, endAngle, radius));
    }

    /**
     * move cursor to (x, y)
     */
    public void gotoXY(int x, int y) {
        if (x <= 0) x = 1;
        else if (x > mScreen.row) x = mScreen.row;
        if (y <= 0) y = 1;
        else if (y > mScreen.maxLines) y = mScreen.maxLines;
        setConsoleCursorPosition(x - 1, y - 1);
        makeCursorVisible();
        postInvalidate();
    }

    /**
     * return x coordinate of cursor in console
     *
     * @return
     */
    public int whereX() {
        return mCursor.x + 1;
    }

    /**
     * return y coordinate of cursor in console*
     *
     * @return
     */
    public int whereY() {
        return mCursor.y + 1;
    }

    ///////////////////////////////////////////////////////////////////////////////
    ///////////           THIS METHOD USES BY PASCAL LIBRARY      //////////////////
    ///////////                    GRAPH LIB                      //////////////////
    ///////////////////////////////////////////////////////////////////////////////
    //pascal
    public int getColorPixel(int x, int y) {
        return mGraphScreen.getColorPixel(x, y);
    }

    //pascal
    public synchronized void addGraphObject(GraphObject graphObject) {
        mGraphScreen.addGraphObject(graphObject);
        postInvalidate();
    }

    //pascal
    public int getXCursorPixel() {
        return mGraphScreen.getXCursor();
    }

    //pascal
    public int getYCursorPixel() {
        return mGraphScreen.getYCursor();
    }

    /**
     * mode graph
     *
     * @return Return current drawing color
     */
    public int getForegroundGraphColor() {
        return mGraphScreen.getPaintColor();
    }

    /**
     * set draw {@link GraphObject} color
     *
     * @param currentColor
     */
    public void setCursorGraphColor(int currentColor) {
        mGraphScreen.setPaintColor(currentColor);
    }

    //pascal
    public void closeGraph() {
        mGraphScreen.closeGraph();
        postInvalidate();
    }

    //pascal
    public void clearGraph() {
        mGraphScreen.clear();
        clearScreen();
    }

    //pascal
    public void setCursorGraphPosition(int x, int y) {
        mGraphScreen.setCursorPostion(x, y);
    }

    public CursorConsole getCursorGraph() {
        return mGraphScreen.getCursor();
    }

    public void setCursorGraphStyle(int style, int pattern, int width) {
        mGraphScreen.setPaintStyle(style, pattern, width);
    }

    public void setGraphBackground(int colorPascal) {
        mGraphScreen.setBackgroundColor(colorPascal);
    }


    public void setGraphTextStyle(int font, int direction, int size) {
        mGraphScreen.setTextSize(size);
        mGraphScreen.setTextDirection(direction);
//        mGraphScreen.setFont(font);
//        mGraphScreen.getCursorPaint().setDir(direction);
    }
}





