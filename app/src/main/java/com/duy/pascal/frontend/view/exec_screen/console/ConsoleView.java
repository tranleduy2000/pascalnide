package com.duy.pascal.frontend.view.exec_screen.console;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputContentInfo;
import android.view.inputmethod.InputMethodManager;

import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.setting.PascalPreferences;
import com.duy.pascal.frontend.view.exec_screen.graph.molel.ArcObject;
import com.duy.pascal.frontend.view.exec_screen.graph.molel.GraphObject;

import static com.duy.pascal.frontend.view.exec_screen.console.StringCompare.greaterEqual;
import static com.duy.pascal.frontend.view.exec_screen.console.StringCompare.lessThan;

public class ConsoleView extends View implements GestureDetector.OnGestureListener {
    public static final String TAG = ConsoleView.class.getSimpleName();
    public static final String THE_DELETE_COMMAND = "\u2764";
    public static final String THE_ENTER_KEY = "\u2713";
    public Handler handler = new Handler();
    public int firstLine;
    boolean isTextChange = true;
    private boolean graphMode = false;
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
    //    private String textScreen = "";
    private String mImeBuffer = "";
    private TextConsole[] textImeBuffer;

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

    public TextRenderer getTextRenderer() {
        return mTextRenderer;
    }

    public CursorConsole getCursorConsole() {
        return mCursor;
    }

    public boolean isGraphMode() {
        return graphMode;
    }

    public void setGraphMode(boolean graphMode) {
        this.graphMode = graphMode;
    }

    private void init(Context context) {
        mGestureDetector = new GestureDetector(this);
        mPascalPreferences = new PascalPreferences(context);
        frameRate = mPascalPreferences.getConsoleFrameRate();
//        mConsoleScreen.setMaxLines(mPascalPreferences.getConsoleMaxBuffer());

        mGraphScreen = new GraphScreen(context);

        mConsoleScreen.setBackgroundColor(Color.BLACK);
        mConsoleScreen.setMaxLines(100);

        mTextRenderer = new TextRenderer(getTextSize(TypedValue.COMPLEX_UNIT_SP,
                mPascalPreferences.getConsoleFontSize()));
        mTextRenderer.setTextColor(Color.WHITE);

        firstLine = 0;
        bufferData.firstIndex = 0;
        bufferData.textConsole = null;

        mCursor = new CursorConsole(0, 0, Color.DKGRAY);
        mCursor.setCoordinate(0, 0);
        mCursor.setCursorBlink(true);
        mCursor.setVisible(true);
    }

    public void putString(String c) {
        bufferData.stringBuffer.putString(c);
    }


    // move cursor to new line

    public String readString() {
        return bufferData.stringBuffer.getString();
    }

    public char readKey() {
        return (char) bufferData.keyBuffer.getByte();
    }

    //set cursor index
    public void setConsoleCursorPosition(int x, int y) {
        int index, i;
        mCursor.y = y;
        index = bufferData.firstIndex + mCursor.y * mConsoleScreen.consoleColumn;
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

    public void commitChar(String c, boolean isMaskBuffer) {
        int index = bufferData.firstIndex + mCursor.y * mConsoleScreen.consoleColumn + mCursor.x;
        if (index >= mConsoleScreen.getScreenSize()) {
            index -= mConsoleScreen.getScreenSize();
        }
        switch (c) {
            case "\n":
                bufferData.textConsole[index].setText("\n");
                bufferData.textConsole[index].setTextBackground(mTextRenderer.getBackgroundColor());
                bufferData.textConsole[index].setTextColor(mTextRenderer.getTextColor());
                bufferData.textConsole[index].setAlpha(mTextRenderer.getAlpha());
                nextLine();
                break;
            case "\177":
            case THE_DELETE_COMMAND:
                deleteChar(index);
                break;
            default:
                makeCursorVisible();
                if (greaterEqual(c, " ")) {
                    bufferData.textConsole[index].setText(c);
                    bufferData.textConsole[index].setTextBackground(
                            isMaskBuffer ? Color.DKGRAY : mTextRenderer.getBackgroundColor());
                    bufferData.textConsole[index].setTextColor(mTextRenderer.getTextColor());
                    bufferData.textConsole[index].setAlpha(mTextRenderer.getAlpha());
                    mCursor.x++;
                    if (mCursor.x >= mConsoleScreen.consoleColumn) {
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
                    mCursor.x = mConsoleScreen.consoleColumn - 1;
                    mCursor.y--;
                    makeCursorVisible();
                }
            }
        }
    }

    public void commitString(String msg) {
        for (int i = 0; i < msg.length(); i++)
            commitChar(msg.substring(i, i + 1), false);
//        textScreen = ArrayUtils.arrayToString(bufferData.textConsole);
    }

    private void nextLine() {
        mCursor.x = 0;
        mCursor.y++;
        if (mCursor.y >= mConsoleScreen.maxLines) {
            mCursor.y = mConsoleScreen.maxLines - 1;
            for (int i = 0; i < mConsoleScreen.consoleColumn; i++)
                bufferData.textConsole[bufferData.firstIndex + i].setText("\0");
            bufferData.firstIndex += mConsoleScreen.consoleColumn;
            if (bufferData.firstIndex >= mConsoleScreen.getScreenSize()) bufferData.firstIndex = 0;
        }
        makeCursorVisible();
    }

    public void showPrompt() {
        commitString("Initialize the console screen..." + "\n");
        commitString("Size: " + mConsoleScreen.consoleRow + "x" + mConsoleScreen.consoleColumn + "\n");
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

    public void initConsole(Activity a) {
        mActivity = a;

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
        if (mCursor.y - firstLine >= mConsoleScreen.consoleRow) {
            firstLine = mCursor.y - mConsoleScreen.consoleRow + 1;
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
        int newColumn = newWidth / mTextRenderer.charWidth;
        int i, j;
        int newFirstIndex = 0;
        int newRow = newHeight / mTextRenderer.charHeight;
        boolean value = newColumn != mConsoleScreen.consoleColumn || newRow != mConsoleScreen.consoleColumn;
        mConsoleScreen.consoleRow = newRow;
        if (newColumn != mConsoleScreen.consoleColumn) {
            int newScreenSize = mConsoleScreen.maxLines * newColumn;
            TextConsole newScreenBuffer[] = new TextConsole[newScreenSize];
            for (i = 0; i < newScreenSize; i++) {
                newScreenBuffer[i] = new TextConsole();
                newScreenBuffer[i].setText("\0");
            }
            Log.d(TAG, "tUpdateSize: " + newScreenSize);
            if (bufferData.textConsole != null) {
                i = 0;
                int nextj = 0;
                int endi = mCursor.y * mConsoleScreen.consoleColumn + mCursor.x;
                String c;
                do {
                    j = nextj;
                    do {
                        c = bufferData.textConsole[trueIndex(i++, bufferData.firstIndex, mConsoleScreen.getScreenSize())].getSingleString();
                        newScreenBuffer[trueIndex(j++, newFirstIndex, newScreenSize)].setText(c);
                        newFirstIndex = Math.max(0, j / newColumn - mConsoleScreen.maxLines + 1) * newColumn;
                    }
                    while (greaterEqual(c, " "));
                    i--;
                    j--;

                    i += (mConsoleScreen.consoleColumn - i % mConsoleScreen.consoleColumn);
                    nextj = j + (newColumn - j % newColumn);
                }
                while (i < endi);
                if (c.equals("\n")) j = nextj;
                mCursor.y = j / newColumn;
                mCursor.x = j % newColumn;
            }
            mConsoleScreen.setConsoleColumn(newColumn);
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
        outAttrs.inputType = InputType.TYPE_NULL;
//        outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE;


        return new InputConnection() {
            /**
             * Used to handle composing text requests
             */
            private int mCursor;
            private int mComposingTextStart;
            private int mComposingTextEnd;
            private int mSelectedTextStart = 0;
            private int mSelectedTextEnd = 0;
            private boolean mInBatchEdit;

            private void sendText(CharSequence text) {
                Log.d(TAG, "sendText: " + text);
                int n = text.length();
                for (int i = 0; i < n; i++) {
                    putString(text.subSequence(i, i + 1).toString());
                }
            }


            @Override
            public boolean performEditorAction(int actionCode) {
                Log.d(TAG, "performEditorAction: " + actionCode);
                /*if (actionCode == EditorInfo.IME_ACTION_DONE
                        || actionCode == EditorInfo.IME_ACTION_GO
                        || actionCode == EditorInfo.IME_ACTION_NEXT
                        || actionCode == EditorInfo.IME_ACTION_SEND
                        || actionCode == EditorInfo.IME_ACTION_UNSPECIFIED) {*/
                if (actionCode == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    sendText("\n");
                    return true;
                }
                return false;
            }


            public boolean beginBatchEdit() {
                if (DLog.DEBUG) {
                    Log.w(TAG, "beginBatchEdit");
                }
                setImeBuffer("");
                mCursor = 0;
                mComposingTextStart = 0;
                mComposingTextEnd = 0;
                mInBatchEdit = true;
                return true;
            }

            public boolean clearMetaKeyStates(int arg0) {
                if (DLog.DEBUG) {
                    Log.w(TAG, "clearMetaKeyStates " + arg0);
                }
                return false;
            }

            public boolean commitCompletion(CompletionInfo arg0) {
                if (DLog.DEBUG) {
                    Log.w(TAG, "commitCompletion " + arg0);
                }
                return false;
            }

            @Override
            public boolean commitCorrection(CorrectionInfo correctionInfo) {
                return false;
            }

            public boolean endBatchEdit() {
                if (DLog.DEBUG) {
                    Log.w(TAG, "endBatchEdit");
                }
                mInBatchEdit = false;
                return true;
            }

            public boolean finishComposingText() {
                if (DLog.DEBUG) {
                    Log.w(TAG, "finishComposingText");
                }
                sendText(mImeBuffer);
                setImeBuffer("");
                mComposingTextStart = 0;
                mComposingTextEnd = 0;
                mCursor = 0;
                return true;
            }

            public int getCursorCapsMode(int arg0) {
                if (DLog.DEBUG) {
                    Log.w(TAG, "getCursorCapsMode(" + arg0 + ")");
                }
                return 0;
            }

            public ExtractedText getExtractedText(ExtractedTextRequest arg0,
                                                  int arg1) {
                if (DLog.DEBUG) {
                    Log.w(TAG, "getExtractedText" + arg0 + "," + arg1);
                }
                return null;
            }

            public CharSequence getTextAfterCursor(int n, int flags) {
                if (DLog.DEBUG) {
                    Log.w(TAG, "getTextAfterCursor(" + n + "," + flags + ")");
                }
                int len = Math.min(n, mImeBuffer.length() - mCursor);
                if (len <= 0 || mCursor < 0 || mCursor >= mImeBuffer.length()) {
                    return "";
                }
                return mImeBuffer.substring(mCursor, mCursor + len);
            }

            public CharSequence getTextBeforeCursor(int n, int flags) {
                if (DLog.DEBUG) {
                    Log.w(TAG, "getTextBeforeCursor(" + n + "," + flags + ")");
                }
                int len = Math.min(n, mCursor);
                if (len <= 0 || mCursor < 0 || mCursor >= mImeBuffer.length()) {
                    return "";
                }
                return mImeBuffer.substring(mCursor - len, mCursor);
            }

            public boolean performContextMenuAction(int arg0) {
                if (DLog.DEBUG) {
                    Log.w(TAG, "performContextMenuAction" + arg0);
                }
                return true;
            }

            public boolean performPrivateCommand(String arg0, Bundle arg1) {
                if (DLog.DEBUG) {
                    Log.w(TAG, "performPrivateCommand" + arg0 + "," + arg1);
                }
                return true;
            }

            @Override
            public boolean requestCursorUpdates(int cursorUpdateMode) {
                return false;
            }

            @Override
            public Handler getHandler() {
                return null;
            }

            @Override
            public void closeConnection() {

            }

            @Override
            public boolean commitContent(InputContentInfo inputContentInfo, int flags, Bundle opts) {
                return false;
            }

            public boolean reportFullscreenMode(boolean arg0) {
                if (DLog.DEBUG) {
                    Log.w(TAG, "reportFullscreenMode" + arg0);
                }
                return true;
            }

//            public boolean commitCorrection (CorrectionInfo correctionInfo) {
//                if (DLog.DEBUG) {
//                    Log.w(TAG, "commitCorrection");
//                }
//                return true;
//            }

            public boolean commitText(CharSequence text, int newCursorPosition) {
                if (DLog.DEBUG) {
                    Log.w(TAG, "commitText(\"" + text + "\", " + newCursorPosition + ")");
                }
                clearComposingText();
                sendText(text);
                setImeBuffer("");
                mCursor = 0;
                return true;
            }

            private void clearComposingText() {
                setImeBuffer(mImeBuffer.substring(0, mComposingTextStart) +
                        mImeBuffer.substring(mComposingTextEnd));
                if (mCursor < mComposingTextStart) {
                    // do nothing
                } else if (mCursor < mComposingTextEnd) {
                    mCursor = mComposingTextStart;
                } else {
                    mCursor -= mComposingTextEnd - mComposingTextStart;
                }
                mComposingTextEnd = mComposingTextStart = 0;
            }

            public boolean deleteSurroundingText(int leftLength, int rightLength) {
                if (DLog.DEBUG) {
                    Log.w(TAG, "deleteSurroundingText(" + leftLength +
                            "," + rightLength + ")");
                }
                if (leftLength > 0) {
                    for (int i = 0; i < leftLength; i++) {
                        sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                    }
                } else if ((leftLength == 0) && (rightLength == 0)) {
                    // Delete key held down / repeating
                    sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                }
                // TODO: handle forward deletes.
                return true;
            }

            @Override
            public boolean deleteSurroundingTextInCodePoints(int beforeLength, int afterLength) {
                return false;
            }


            public boolean sendKeyEvent(KeyEvent event) {
                if (DLog.DEBUG) {
                    Log.w(TAG, "sendKeyEvent(" + event + ")");
                }
                // Some keys are sent here rather than to commitText.
                // In particular, del and the digit keys are sent here.
                // (And I have reports that the HTC Magic also sends Return here.)
                // As a bit of defensive programming, handle every key.
                dispatchKeyEvent(event);
                return true;
            }

            public boolean setComposingText(CharSequence text, int newCursorPosition) {
                if (DLog.DEBUG) {
                    Log.w(TAG, "setComposingText(\"" + text + "\", " + newCursorPosition + ")");
                }
                setImeBuffer(mImeBuffer.substring(0, mComposingTextStart) +
                        text + mImeBuffer.substring(mComposingTextEnd));
                mComposingTextEnd = mComposingTextStart + text.length();
                mCursor = newCursorPosition > 0 ? mComposingTextEnd + newCursorPosition - 1
                        : mComposingTextStart - newCursorPosition;
                return true;
            }

            public boolean setSelection(int start, int end) {
                if (DLog.DEBUG) {
                    Log.w(TAG, "setSelection" + start + "," + end);
                }
                int length = mImeBuffer.length();
                if (start == end && start > 0 && start < length) {
                    mSelectedTextStart = mSelectedTextEnd = 0;
                    mCursor = start;
                } else if (start < end && start > 0 && end < length) {
                    mSelectedTextStart = start;
                    mSelectedTextEnd = end;
                    mCursor = start;
                }
                return true;
            }

            public boolean setComposingRegion(int start, int end) {
                if (DLog.DEBUG) {
                    Log.w(TAG, "setComposingRegion " + start + "," + end);
                }
                if (start < end && start > 0 && end < mImeBuffer.length()) {
                    clearComposingText();
                    mComposingTextStart = start;
                    mComposingTextEnd = end;
                }
                return true;
            }

            public CharSequence getSelectedText(int flags) {

                try {

                    if (DLog.DEBUG) {
                        Log.w(TAG, "getSelectedText " + flags);
                    }

                    if (mImeBuffer.length() < 1) {
                        return "";
                    }

                    return mImeBuffer.substring(mSelectedTextStart, mSelectedTextEnd + 1);

                } catch (Exception e) {

                }

                return "";
            }

        };
    }

    private void setImeBuffer(String buffer) {
        Log.d(TAG, "setImeBuffer: " + buffer);
        //delete last buffer in screen
        for (int i = 0; i < mImeBuffer.length(); i++) {
            commitChar(THE_DELETE_COMMAND, false);
        }
        mImeBuffer = buffer;
        for (int i = 0; i < mImeBuffer.length(); i++)
            commitChar(mImeBuffer.substring(i, i + 1), true);
//        textScreen = ArrayUtils.arrayToString(bufferData.textConsole);
//        textImeBuffer = new TextConsole[mImeBuffer.length()];
//        for (int i = 0; i < textImeBuffer.length; i++) {
//            textImeBuffer[i] = new TextConsole(mImeBuffer.substring(i, i + 1), Color.DKGRAY,
//                    mTextRenderer.getTextColor());
//        }
//        invalidate();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (DLog.DEBUG) Log.d(TAG, "onKeyDown: " + event);
        if (event.isSystem()) {
            return super.onKeyDown(keyCode, event);
        }
        if (filterKey) {
            bufferData.keyBuffer.putByte((byte) keyCode);
            return true;
        } else {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                putString(THE_DELETE_COMMAND);
                return true;
            }
            String c = event.getCharacters();
            if (c == null) {
                c = Character.valueOf((char) event.getUnicodeChar()).toString();
            }
            putString(c);
            return true;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (DLog.DEBUG) Log.d(TAG, "onKeyUp: " + event);
        if (event.isSystem()) {
            return super.onKeyUp(keyCode, event);
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

    public void drawText(Canvas canvas, int left, int top) {
        int index = bufferData.firstIndex + firstLine * mConsoleScreen.consoleColumn;
        if (index >= mConsoleScreen.getScreenSize()) {
            index -= mConsoleScreen.getScreenSize();
        }
        top -= mTextRenderer.charAscent;

        //drawBackground cursor
        mCursor.drawCursor(canvas,
                left + mCursor.x * mTextRenderer.charWidth,
                top + (mCursor.y - firstLine) * mTextRenderer.charHeight,
                mTextRenderer.charHeight, mTextRenderer.charWidth, mTextRenderer.charDescent);

        Log.d(TAG, "drawText: " + mConsoleScreen.consoleRow + " row = " + mConsoleScreen.consoleColumn);
        int count = 0;
        for (int row = 0; row < mConsoleScreen.consoleRow; row++) {
            if (row > mCursor.y - firstLine) break;
            count = 0;
            while ((count < mConsoleScreen.consoleColumn) &&
                    greaterEqual(bufferData.textConsole[count + index].getSingleString(), " "))
                count++;

            mTextRenderer.draw(canvas, left, top, bufferData.textConsole, index, count);

            top += mTextRenderer.charHeight;
            index += mConsoleScreen.consoleColumn;

            if (index >= mConsoleScreen.getScreenSize()) index = 0;
        }

        /**
         * draw ime buffer
         */
        /*TextConsole[] textImeBuffer = new TextConsole[mImeBuffer.length()];
        for (int i = 0; i < textImeBuffer.length; i++) {
            textImeBuffer[i] = new TextConsole(mImeBuffer.substring(i, i + 1), Color.DKGRAY,
                    mTextRenderer.getTextColor());
        }
*/
//        int mIndex = 0;
//        int mLastCount = count;
        top -= mTextRenderer.charHeight;
//        mTextRenderer.draw(canvas, left, top, textImeBuffer, 0, textImeBuffer.length);
//        index += mConsoleScreen.consoleColumn;
//        mIndex += count - mLastCount;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth();
        int h = getHeight();

        // drawBackground bitmap graph
        mConsoleScreen.drawBackground(canvas, mConsoleScreen.getLeftVisible(),
                mConsoleScreen.getTopVisible(), w, h);
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
        handler.removeCallbacks(blink);
    }

    public void onResume() {
        handler.postDelayed(checkSize, 1000);
        handler.postDelayed(blink, 1000);
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
        else if (x > mConsoleScreen.consoleColumn) x = mConsoleScreen.consoleColumn;
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
        graphMode = false;
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





