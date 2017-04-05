package com.duy.pascal.frontend.view.exec_screen.console;

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

import com.duy.pascal.backend.utils.ArrayUtils;
import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.setting.PascalPreferences;
import com.duy.pascal.frontend.view.exec_screen.graph.molel.ArcObject;
import com.duy.pascal.frontend.view.exec_screen.graph.molel.GraphObject;

import static com.duy.pascal.frontend.view.exec_screen.console.StringCompare.greaterEqual;
import static com.duy.pascal.frontend.view.exec_screen.console.StringCompare.lessThan;

public class ConsoleView extends View implements GestureDetector.OnGestureListener {
    public static final String TAG = ConsoleView.class.getSimpleName();
    public static final String THE_DELETE_CHAR = "\u2764";
    public static final String THE_ENTER_KEY = "\u2713";
    public Handler handler = new Handler();
    public int firstLine;
    boolean graphMode = false;
    boolean isTextChange = true;
    private GraphScreen mGraphScreen;
    //    text style, size of console
    private TextRenderer mTextRenderer;
    //      store screen size and dimen
    private ConsoleScreen mConsoleScreen = new ConsoleScreen();
    //     Cursor of console
    private CursorConsole mCursor;
    //     Parent mActivity
    private Activity mActivity;
    //      Data of console
    private ScreenBuffer bufferData = new ScreenBuffer();
    private int frameRate = 60;
    private Rect visibleRect = new Rect();
    private Runnable checkSize = new Runnable() {
        public void run() {
            if (updateSize()) {
                invalidate();
            }
            handler.postDelayed(this, 1000);
        }
    };
    private Runnable blink = new Runnable() {
        public void run() {
            mCursor.toggleState();
            invalidate();
            handler.postDelayed(this, 1000);
        }
    };
    private float mScrollRemainder;
    private GestureDetector mGestureDetector;
    private boolean filterKey = false;
    private PascalPreferences mPascalPreferences;
    private String textScreen = "";

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

    private void init(Context context) {
        mGestureDetector = new GestureDetector(this);
        mCursor = new CursorConsole(0, 0, Color.DKGRAY);
        mPascalPreferences = new PascalPreferences(context);
        frameRate = mPascalPreferences.getConsoleFrameRate();
//        mConsoleScreen.setMaxLines(mPascalPreferences.getConsoleMaxBuffer());
        mConsoleScreen.setMaxLines(100);
        mGraphScreen = new GraphScreen(context);
    }

    public void putString(String c) {
        bufferData.stringBuffer.putString(c);
    }

    public String readString() {
        return bufferData.stringBuffer.getString();
    }

    public char readKey() {
        return (char) bufferData.keyBuffer.getByte();
    }


    // move cursor to new line

    //set cursor index
    public void setConsoleCursorPosition(int x, int y) {
        int index, i;
        mCursor.y = y;
        index = bufferData.firstIndex + mCursor.y * mConsoleScreen.consoleRow;
        if (index >= mConsoleScreen.getScreenSize()) index -= mConsoleScreen.getScreenSize();
        i = index;

        while (i - index <= x) {
            if (lessThan(bufferData.textConsole[i].getSingleString(), " ")) break;
            i++;
        }

        while (i - index < x) {
            if (lessThan(bufferData.textConsole[i].getSingleString(), " ")) {
                bufferData.textConsole[i].setText(" ");
            }
            i++;
        }
        mCursor.x = x;
    }

    public void commitChar(String c) {
        int index = bufferData.firstIndex + mCursor.y * mConsoleScreen.consoleRow + mCursor.x;
        if (index >= mConsoleScreen.getScreenSize()) {
            index -= mConsoleScreen.getScreenSize();
        }
        switch (c) {
            case "\n":
                bufferData.textConsole[index].setText("\n");
                bufferData.textConsole[index].setTextBackground(mTextRenderer.getBackgroundColor());
                bufferData.textConsole[index].setTextColor(mTextRenderer.getTextColor());
                nextLine();
                break;
            case "\177":
            case THE_DELETE_CHAR:
                deleteChar(index);
                break;
            default:
                makeCursorVisible();
                if (greaterEqual(c, " ")) {
                    bufferData.textConsole[index].setText(c);
                    bufferData.textConsole[index].setTextBackground(mTextRenderer.getBackgroundColor());
                    bufferData.textConsole[index].setTextColor(mTextRenderer.getTextColor());
                    mCursor.x++;
                    if (mCursor.x >= mConsoleScreen.consoleRow) {
                        nextLine();
                    }
                }
        }

        postInvalidate();
    }

    public void deleteChar(int index) {
        if (mCursor.x > 0) {
            mCursor.x--;
            bufferData.textConsole[index - 1].setText("\0");
        } else {
            if (mCursor.y > 0) {
                if (greaterEqual(bufferData.textConsole[index - 1].getSingleString(), " ")) {
                    bufferData.textConsole[index - 1].setText("\0");
                    mCursor.x = mConsoleScreen.consoleRow - 1;
                    mCursor.y--;
                    makeCursorVisible();
                }
            }
        }
    }

    public void commitString(String msg) {
        for (int i = 0; i < msg.length(); i++)
            commitChar(msg.substring(i, i + 1));
        textScreen = ArrayUtils.arrayToString(bufferData.textConsole);
    }

    private void nextLine() {
        mCursor.x = 0;
        mCursor.y++;
        if (mCursor.y >= mConsoleScreen.maxLines) {
            mCursor.y = mConsoleScreen.maxLines - 1;
            for (int i = 0; i < mConsoleScreen.consoleRow; i++)
                bufferData.textConsole[bufferData.firstIndex + i].setText("\0");
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
        mConsoleScreen.setBackgroundColor(backColor);
        mTextRenderer = new TextRenderer(getTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize));
        mTextRenderer.setTextColor(textColor);

        mCursor.x = 0;
        mCursor.y = 0;

        firstLine = 0;
        bufferData.firstIndex = 0;
        bufferData.textConsole = null;

        mCursor.setCursorBlink(true);
        mCursor.setVisible(true);
    }

    /**
     * clear screen
     * clrscr command in pascal
     */
    public void clearScreen() {
        for (int i = 0; i < mConsoleScreen.getScreenSize(); i++)
            bufferData.textConsole[i].setText("\0");
        mCursor.setCoordinate(0, 0);
        firstLine = 0;
        bufferData.firstIndex = 0;
        mConsoleScreen.setBackgroundColor(mTextRenderer.getBackgroundColor());
        postInvalidate();
    }

    //  Update text size
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
        if (mCursor.y - firstLine >= mConsoleScreen.consoleColumn) {
            firstLine = mCursor.y - mConsoleScreen.consoleColumn + 1;
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
        long startTime = System.currentTimeMillis();
        int newRow = newWidth / mTextRenderer.charWidth;
        int i, j;
        int newFirstIndex = 0;
        int newColumn = newHeight / mTextRenderer.charHeight;
        boolean value = newRow != mConsoleScreen.consoleRow || newColumn != mConsoleScreen.consoleRow;
        mConsoleScreen.consoleColumn = newColumn;
        if (newRow != mConsoleScreen.consoleRow) {
            int newScreenSize = mConsoleScreen.maxLines * newRow;
            TextConsole newScreenBuffer[] = new TextConsole[newScreenSize];
            for (i = 0; i < newScreenSize; i++) {
                newScreenBuffer[i] = new TextConsole();
                newScreenBuffer[i].setText("\0");
            }
            Log.d(TAG, "tUpdateSize: " + newScreenSize);
            if (bufferData.textConsole != null) {
                i = 0;
                int nextj = 0;
                int endi = mCursor.y * mConsoleScreen.consoleRow + mCursor.x;
                String c;
                do {
                    j = nextj;
                    do {
                        c = bufferData.textConsole[trueIndex(i++, bufferData.firstIndex, mConsoleScreen.getScreenSize())].getSingleString();
                        newScreenBuffer[trueIndex(j++, newFirstIndex, newScreenSize)].setText(c);
                        newFirstIndex = Math.max(0, j / newRow - mConsoleScreen.maxLines + 1) * newRow;
                    }
                    while (greaterEqual(c, " "));
                    i--;
                    j--;

                    i += (mConsoleScreen.consoleRow - i % mConsoleScreen.consoleRow);
                    nextj = j + (newRow - j % newRow);
                }
                while (i < endi);
                if (c.equals("\n")) j = nextj;
                mCursor.y = j / newRow;
                mCursor.x = j % newRow;
            }
            mConsoleScreen.setConsoleRow(newRow);
            mConsoleScreen.setScreenSize(newScreenSize);
            bufferData.setTextConsole(newScreenBuffer);
            bufferData.firstIndex = newFirstIndex;
        }
        makeCursorVisible();
        Log.d(TAG, "tUpdateSize: " + (System.currentTimeMillis() - startTime));
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
                    putString(Character.valueOf(text.charAt(i)).toString());
                }
                return true;
            }

            @Override
            public boolean performEditorAction(int actionCode) {
                if (actionCode == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    putString("\n"); //new line
                    Log.d(TAG, "performEditorAction: new line");
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
                        putString(THE_DELETE_CHAR);
                        return true;
                    }

                    String c = event.getCharacters();
                    if (c != null) {
                        putString(c);
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
        event.getKeyCode();
        if (event.isSystem()) {
            return super.onKeyDown(keyCode, event);
        }
        if (filterKey) {
            bufferData.keyBuffer.putByte((byte) keyCode);
            return true;
        } else {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                putString(THE_DELETE_CHAR);
                return true;
            }
            String c = event.getCharacters();
            Log.d(TAG, "onKeyDown: " + c);
            if (c != null) {
                putString(c);
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

    public void drawText(Canvas canvas, int x, int y) {
        int index = bufferData.firstIndex + firstLine * mConsoleScreen.consoleRow;
        if (index >= mConsoleScreen.getScreenSize()) index -= mConsoleScreen.getScreenSize();
        y -= mTextRenderer.charAscent;

        //drawBackground cursor
        mCursor.drawCursor(canvas,
                x + mCursor.x * mTextRenderer.charWidth,
                y + (mCursor.y - firstLine) * mTextRenderer.charHeight,
                mTextRenderer.charHeight, mTextRenderer.charWidth, mTextRenderer.charDescent);

        for (int i = 0; i < mConsoleScreen.consoleColumn; i++) {
            if (i > mCursor.y - firstLine) break;

            int count = 0;
            while ((count < mConsoleScreen.consoleRow) &&
                    greaterEqual(bufferData.textConsole[count + index].getSingleString(), " "))
                count++;
            mTextRenderer.draw(canvas, x, y, bufferData.textConsole, index, count);
            y += mTextRenderer.charHeight;
            index += mConsoleScreen.consoleRow;
            if (index >= mConsoleScreen.getScreenSize()) index = 0;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth();
        int h = getHeight();

        // drawBackground bitmap graph
        mConsoleScreen.drawBackground(canvas, mConsoleScreen.getLeftVisible(), mConsoleScreen.getTopVisible(), w, h);
        drawText(canvas, mConsoleScreen.getLeftVisible(), mConsoleScreen.getTopVisible());

        if (graphMode)
            canvas.drawBitmap(mGraphScreen.getGraphBitmap(), 0, 0, null);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mGestureDetector.onTouchEvent(ev);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        distanceY += mScrollRemainder;
        int deltaRows = (int) (distanceY / mTextRenderer.charHeight);
        mScrollRemainder = distanceY - deltaRows * mTextRenderer.charHeight;
        firstLine = Math.max(0, Math.min(firstLine + deltaRows, mCursor.y));

        invalidate();
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        mScrollRemainder = 0.0f;
        onScroll(e1, e2, velocityX, -velocityY);
        return true;
    }

    public void onShowPress(MotionEvent e) {
    }

    public boolean onSingleTapUp(MotionEvent e) {
        doShowSoftKeyboard();
        return true;
    }

    public boolean onDown(MotionEvent e) {
        mScrollRemainder = 0.0f;
        return true;
    }

    public void onLongPress(MotionEvent e) {
        showContextMenu();
    }

    public void onPause() {
        handler.removeCallbacks(checkSize);
//        handler.removeCallbacks(blink);
    }

    public void onResume() {
        handler.postDelayed(checkSize, 1000);
//        handler.postDelayed(blink, 500);
        updateSize();
    }

    private void doShowSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this, 0);
    }

    public GraphScreen getGraphScreen() {
        return mGraphScreen;
    }

    public boolean isFilterKey() {
        return filterKey;
    }

    public void setFilterKey(boolean filterKey) {
        this.filterKey = filterKey;
    }

    ///////////////////////////////////////////////////////////////////////////////
    ///////////           THIS METHOD USES BY PASCAL LIBRARY      //////////////////
    ///////////                      CRT LIB                      //////////////////
    ///////////////////////////////////////////////////////////////////////////////
    //pascal
    public void setConsoleTextColor(int textColor) {
        Log.d(TAG, "setConsoleTextColor: " + textColor);
        mTextRenderer.setTextColor(textColor);
//        postInvalidate();
    }

    //pascal
    public void setConsoleTextBackground(int color) {
//        mConsoleScreen.setBackgroundColor(color);
//        mConsoleScreen.setBackgroundColor();
        mTextRenderer.setTextBackgroundColor(color);
//        postInvalidate();
    }

    /**
     * Draw part of a circle
     */

    public void arc(int x, int y, int stAngle, int endAngle, int radius) {
        mGraphScreen.addGraphObject(new ArcObject(x, y, stAngle, endAngle, radius));
    }

    // move cursor to (x, y)
    public void gotoXY(int x, int y) {
        if (x <= 0) x = 1;
        else if (x > mConsoleScreen.consoleRow) x = mConsoleScreen.consoleRow;
        if (y <= 0) y = 1;
        else if (y > mConsoleScreen.maxLines) y = mConsoleScreen.maxLines;
        setConsoleCursorPosition(x - 1, y - 1);
        makeCursorVisible();
        postInvalidate();
    }

    // `return x coordinate of cursor in console
    public int whereX() {
        return mCursor.x + 1;
    }

    /**
     * return y coordinate of cursor in console*
     */
    public int whereY() {
        return mCursor.y + 1;
    }


    public boolean keyPressed() {
        return bufferData.stringBuffer.rear > bufferData.stringBuffer.front;
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
     * set drawBackground {@link GraphObject} color
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





