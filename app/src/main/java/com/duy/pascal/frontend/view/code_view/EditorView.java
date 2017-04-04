package com.duy.pascal.frontend.view.code_view;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.duy.pascal.frontend.EditorSetting;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.activities.EditorActivity;
import com.duy.pascal.frontend.file.PreferenceHelper;
import com.duy.pascal.frontend.theme.CodeThemeUtils;
import com.duy.pascal.frontend.theme.ThemeFromAssets;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.duy.pascal.frontend.data.PatternsUtils.comments;
import static com.duy.pascal.frontend.data.PatternsUtils.keywords;
import static com.duy.pascal.frontend.data.PatternsUtils.numbers;
import static com.duy.pascal.frontend.data.PatternsUtils.strings;
import static com.duy.pascal.frontend.data.PatternsUtils.symbols;


public class EditorView extends AutoSuggestsEditText {

    public static final int ID_SELECT_ALL = android.R.id.selectAll;
    public static final int ID_CUT = android.R.id.cut;
    public static final int ID_COPY = android.R.id.copy;
    public static final int ID_PASTE = android.R.id.paste;
    private static final String TAG = EditorView.class.getSimpleName();
    public boolean showLineNumbers = true;
    public float textSize = 13;
    public boolean wordWrap = true;
    public boolean flingToScroll = true;
    //Colors
    protected int COLOR_ERROR;
    protected int COLOR_NUMBER;
    protected int COLOR_KEYWORD;
    protected int COLOR_COMMENT;
    protected int COLOR_OPT;
    protected int COLOR_BOOLEANS;
    protected int COLOR_STRINGS;
    protected int mPaddingDP = 4;
    protected int mPadding, mLinePadding;
    protected float mScale;
    //    protected Scroller mScroller;
    protected GestureDetector mGestureDetector;
    protected int mHighlightedLine;
    protected int mHighlightStart;
    protected Rect mDrawingRect, mLineBounds;
    protected Paint mPaintHighlight;
    private Paint mPaintNumbers = new Paint();
    /**
     * The edit history.
     */
    private EditHistory mEditHistory;
    /**
     * The change listener.
     */
    private EditTextChangeListener
            mChangeListener;
    /**
     * Disconnect this undo/redo from the text
     * view.
     */
    private boolean enabledChangeListener;
    private int paddingTop;
    private int numbersWidth;
    private int lineHeight;
    private EditorActivity activity;
    /**
     * Is undo/redo being performed? This member
     * signals if an undo/redo operation is
     * currently being performed. Changes in the
     * text during undo/redo are not recorded
     * because it would mess up the undo history.
     */
    private boolean mIsUndoOrRedo;
    private boolean mShowUndo, mShowRedo;
    private boolean canSaveFile;
    private KeyListener keyListener;
    private boolean[] isGoodLineArray;
    private int[] realLines;
    private boolean wrapContent;
    private Context mContext;
    private int scrollY = 0;
    private EditorSetting mEditorSetting;
    private boolean canEdit = true;
    private LineUtils lineUtils;
    private int lineCount, realLine, startingLine;
    private int deviceHeight;
    private int firstVisibleIndex;
    private int lastVisibleIndex;
    private int editorHeight;
    private int firstColoredIndex;
    private GoodScrollView verticalScroll;
    private int errorLine = -1;
    private final Runnable colorRunnableDuringEditing =
            new Runnable() {
                @Override
                public void run() {
                    replaceTextKeepCursor(null);
                }
            };
    private final Runnable colorRunnableDuringScroll =
            new Runnable() {
                @Override
                public void run() {
                    replaceTextKeepCursor(null);
                }
            };
    private int mLastKeyCode = 0;
    private Handler handler = new Handler();

    public EditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public EditorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context);
    }

    public EditorView(Context context) {
        super(context);
        setup(context);
    }

    @Override
    public void onPopupSuggestChangeSize() {
        try {
            Layout layout = getLayout();
            if (layout != null) {
                int pos = getSelectionStart();
                int line = layout.getLineForOffset(pos);
                int baseline = layout.getLineBaseline(line);
                int ascent = layout.getLineAscent(line);

                float x = layout.getPrimaryHorizontal(pos);
                float y = baseline + ascent;

                int offsetHorizontal = (int) x + mLinePadding;
                setDropDownHorizontalOffset(offsetHorizontal);

                int heightVisible = getHeightVisible();
                int offsetVertical = (int) ((y + mCharHeight) - verticalScroll.getScrollY());

                int tmp = offsetVertical + getDropDownHeight() + mCharHeight;
                if (tmp < heightVisible) {
                    tmp = offsetVertical /*+ mCharHeight / 2*/;
                    setDropDownVerticalOffset(tmp);
                } else {
                    tmp = offsetVertical - getDropDownHeight() - mCharHeight;
                    setDropDownVerticalOffset(tmp);
                }
//            if (DLog.DEBUG) Log.d(TAG, "onPopupSuggestChangeSize: " + offsetVertical + " " + tmp + " " + scrollY);
            }
        } catch (Exception ignored) {
        }
    }

    public int getHeightVisible() {
        Rect r = new Rect();
        // r will be populated with the coordinates of     your view
        // that area still visible.
        getWindowVisibleDisplayFrame(r);
        return r.bottom - r.top;
    }

    private void setup(Context context) {
        mContext = context;

        //setting line number
        mPaintNumbers.setAntiAlias(true);
        mPaintNumbers.setDither(false);
        mPaintNumbers.setColor(getResources().getColor(R.color.number_color));
        mPaintNumbers.setTextSize(getTextSize() * 0.85f);

        mPaintHighlight = new Paint();

        mScale = context.getResources().getDisplayMetrics().density;
        mPadding = (int) (mPaddingDP * mScale);
        mHighlightedLine = mHighlightStart = -1;
        mDrawingRect = new Rect();
        mLineBounds = new Rect();
        lineUtils = new LineUtils();
        deviceHeight = getResources().getDisplayMetrics().heightPixels;
        setupTheme();
///////////////////////////////////
        mEditHistory = new EditHistory();
        mChangeListener = new EditTextChangeListener();
        enableTextChangedListener();

        lineUtils = new LineUtils();
        deviceHeight = getResources().getDisplayMetrics().heightPixels;
        paddingTop = EditTextPadding.getPaddingTop(getContext());

        // update the padding of the editor
//        updatePadding();

        if (PreferenceHelper.getReadOnly(getContext())) {
            setReadOnly(true);
        } else {
            setReadOnly(false);
            if (PreferenceHelper.getSuggestionActive(getContext())) {
                setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE
                        | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
            } else {
                setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE
                        | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType
                        .TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType
                        .TYPE_TEXT_FLAG_IME_MULTI_LINE);
            }
        }
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PreferenceHelper.getReadOnly(getContext())) {
                    verticalScroll.tempDisableListener(1000);
                    ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .showSoftInput(EditorView.this, InputMethodManager.SHOW_IMPLICIT);
                }

            }
        });
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !PreferenceHelper.getReadOnly(getContext())) {
                    verticalScroll.tempDisableListener(1000);
                    ((InputMethodManager) getContext().getSystemService(Context
                            .INPUT_METHOD_SERVICE))
                            .showSoftInput(EditorView.this, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        setMaxHistorySize(100);
        resetVariables();
        initAutoIndentCode();
    }

    private void initAutoIndentCode() {
        setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source,
                                               int start, int end, Spanned dest, int dstart, int dend) {
                        if (end - start == 1 && start < source.length() &&
                                dstart < dest.length()) {
                            char c = source.charAt(start);
                            if (c == '\n')
                                return autoIndent(source, start, end, dest, dstart, dend);
                        }
                        return source;
                    }
                }
        });
    }

    public void setupTheme() {
        mEditorSetting = new EditorSetting(mContext);
        String nameTheme = mEditorSetting.getString(mContext.getString(R.string.key_code_theme));
        try {
            Integer id = Integer.parseInt(nameTheme);
            setTheme(id);
        } catch (Exception e) {
            setTheme(nameTheme);
        }
        setTypeface(mEditorSetting.getFont());
        setTextSize(mEditorSetting.getTextSize());
    }

    /**
     * set theme by id
     *
     * @param id
     */
    public void setTheme(int id) {
        ThemeFromAssets theme = ThemeFromAssets.getTheme(id, mContext);
        setBackgroundColor(theme.getBackground());
        setTextColor(theme.getColor(0));
        COLOR_ERROR = theme.getColor(7);
        COLOR_NUMBER = theme.getColor(1);
        COLOR_KEYWORD = theme.getColor(2);
        COLOR_COMMENT = theme.getColor(6);
        COLOR_STRINGS = theme.getColor(3);
        COLOR_BOOLEANS = theme.getColor(8);
        COLOR_OPT = theme.getColor(9);

        int style = CodeThemeUtils.getCodeTheme(mContext, "");
        TypedArray typedArray = mContext.obtainStyledAttributes(style,
                R.styleable.CodeTheme);
        this.canEdit = typedArray.getBoolean(R.styleable.CodeTheme_can_edit, true);
        typedArray.recycle();
//        setTypeface(FontManager.getInstance(mContext));
    }

    /**
     * set theme by name
     *
     * @param name
     */
    public void setTheme(String name) {
        /**
         * load theme from xml
         */

        int style = CodeThemeUtils.getCodeTheme(mContext, name);
        TypedArray typedArray = mContext.obtainStyledAttributes(style,
                R.styleable.CodeTheme);
        typedArray.getInteger(R.styleable.CodeTheme_bg_editor_color,
                R.color.bg_editor_color);
        COLOR_ERROR = typedArray.getInteger(R.styleable.CodeTheme_error_color,
                R.color.error_color);
        COLOR_NUMBER = typedArray.getInteger(R.styleable.CodeTheme_number_color,
                R.color.number_color);
        COLOR_KEYWORD = typedArray.getInteger(R.styleable.CodeTheme_key_word_color,
                R.color.key_word_color);
        COLOR_COMMENT = typedArray.getInteger(R.styleable.CodeTheme_comment_color,
                R.color.comment_color);
        COLOR_STRINGS = typedArray.getInteger(R.styleable.CodeTheme_string_color,
                R.color.string_color);
        COLOR_BOOLEANS = typedArray.getInteger(R.styleable.CodeTheme_boolean_color,
                R.color.boolean_color);
        COLOR_OPT = typedArray.getInteger(R.styleable.CodeTheme_opt_color,
                R.color.opt_color);
        setBackgroundColor(typedArray.getInteger(R.styleable.CodeTheme_bg_editor_color,
                R.color.bg_editor_color));
        setTextColor(typedArray.getInteger(R.styleable.CodeTheme_normal_text_color,
                R.color.normal_text_color));

        this.canEdit = typedArray.getBoolean(R.styleable.CodeTheme_can_edit, true);
        typedArray.recycle();

//        setTypeface(FontManager.getInstance(mContext));
    }

    /**
     * refresh state
     */
    public void refresh() {
        replaceTextKeepCursor(null);
    }

    public void setReadOnly(boolean value) {
        if (value) {
            keyListener = getKeyListener();
            setKeyListener(null);
        } else {
            if (keyListener != null)
                setKeyListener(keyListener);
        }
    }

    @Override
    public void setTextSize(float size) {
        super.setTextSize(size);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        mPaintNumbers.setTextSize((int) (size * scale * 0.85f));
        numbersWidth = (int) (EditTextPadding.getPaddingWithLineNumbers(getContext(),
                PreferenceHelper.getFontSize(getContext())) * 0.8);
        lineHeight = getLineHeight();
    }

    @Override
    public void onDraw(@NonNull final Canvas canvas) {
        int lineX, baseline;
        lineCount = getLineCount();
        if (showLineNumbers) {
            int padding = (int) (Math.floor(Math.log10(lineCount)) + 1);
            padding = (int) ((padding * mPaintNumbers.getTextSize()) + mPadding + (textSize * mScale * 0.5));
            if (mLinePadding != padding) {
                mLinePadding = padding;
                Log.d(TAG, "onDraw: " + mLinePadding);
                setPadding(mLinePadding, mPadding, mPadding, mPadding);
            }
        }

        getDrawingRect(mDrawingRect);
        lineX = (int) (mDrawingRect.left + mLinePadding - (textSize * mScale * 0.5));
        int min = 0;
        int max = lineCount;
        getLineBounds(0, mLineBounds);
        int startBottom = mLineBounds.bottom;
        int startTop = mLineBounds.top;
        getLineBounds(lineCount - 1, mLineBounds);
        int endBottom = mLineBounds.bottom;
        int endTop = mLineBounds.top;
        if (lineCount > 1 && endBottom > startBottom && endTop > startTop) {
            min = Math.max(min, ((mDrawingRect.top - startBottom) * (lineCount - 1)) / (endBottom - startBottom));
            max = Math.min(max, ((mDrawingRect.bottom - startTop) * (lineCount - 1)) / (endTop - startTop) + 1);
        }
        for (int i = min; i < max; i++) {
            baseline = getLineBounds(i, mLineBounds);
            if ((i == mHighlightedLine) && (!wordWrap)) {
                canvas.drawRect(mLineBounds, mPaintHighlight);
            }
            if (showLineNumbers) {
                canvas.drawText("" + (i), mDrawingRect.left + mPadding, baseline, mPaintNumbers);
            }
            if (showLineNumbers) {
                canvas.drawLine(lineX, mDrawingRect.top, lineX, mDrawingRect.bottom, mPaintNumbers);
            }
        }
        getLineBounds(lineCount - 1, mLineBounds);
        super.onDraw(canvas);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (event.isSystem()) return super.onKeyUp(keyCode, event);
        mLastKeyCode = keyCode;
        if (event.isCtrlPressed()) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_A:
                    return onTextContextMenuItem(ID_SELECT_ALL);
                case KeyEvent.KEYCODE_X:
                    return onTextContextMenuItem(ID_CUT);
                case KeyEvent.KEYCODE_C:
                    return onTextContextMenuItem(ID_COPY);
                case KeyEvent.KEYCODE_V:
                    return onTextContextMenuItem(ID_PASTE);
                case KeyEvent.KEYCODE_Z:
                    if (getCanUndo()) {
                        return onTextContextMenuItem(R.id.action_undo);
                    }
                case KeyEvent.KEYCODE_Y:
                    if (getCanRedo()) {
                        return onTextContextMenuItem(R.id.action_redo);
                    }
                case KeyEvent.KEYCODE_S:
                    ((EditorActivity) getContext()).saveFile();
                    return true;
                default:
                    return super.onKeyDown(keyCode, event);
            }
        } else {
            switch (keyCode) {
                case KeyEvent.KEYCODE_TAB:
                    insert("\t");
                    return true;
                default:
                    return super.onKeyDown(keyCode, event);
            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        if (event.isSystem()) return super.onKeyUp(keyCode, event);
        if (event.isCtrlPressed()) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_A:
                case KeyEvent.KEYCODE_X:
                case KeyEvent.KEYCODE_C:
                case KeyEvent.KEYCODE_V:
                case KeyEvent.KEYCODE_Z:
                case KeyEvent.KEYCODE_Y:
                case KeyEvent.KEYCODE_S:
                    return true;
                default:
                    return false;
            }
        } else {
            switch (keyCode) {
                case KeyEvent.KEYCODE_TAB:
                    return true;
                default:
                    return super.onKeyUp(keyCode, event);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onTextContextMenuItem(
            final int id) {
        if (id == R.id.action_undo || id == android.R.id.undo) {
            undo();
            return true;
        } else if (id == R.id.action_redo || id == android.R.id.redo) {
            redo();
            return true;
        } else {
            return super.onTextContextMenuItem(id);
        }
    }

    public void insert(String text) {
        Log.d(TAG, "insert: " + text);
//        int start, end;
//        start = Math.max(getSelectionStart(), 0);
//        end = Math.max(getSelectionEnd(), 0);
        getEditableText().replace(getSelectionStart(), getSelectionEnd(), text);
    }

    /**
     * Can undo be performed?
     */
    public boolean getCanUndo() {
        return (mEditHistory.mmPosition > 0);
    }

    /**
     * Can redo be performed?
     */
    public boolean getCanRedo() {
        return (mEditHistory.mmPosition < mEditHistory.mmHistory.size());
    }

    /**
     * Perform undo.
     */
    public void undo() {
        EditItem edit = mEditHistory.getPrevious();
        if (edit == null) {
            return;
        }

        Editable text = getEditableText();
        int start = edit.mmStart;
        int end = start + (edit.mmAfter != null
                ? edit.mmAfter.length() : 0);

        mIsUndoOrRedo = true;
        text.replace(start, end, edit.mmBefore);
        mIsUndoOrRedo = false;

        // This will get rid of underlines inserted when editor tries to come
        // up with a suggestion.
        for (Object o : text.getSpans(0,
                text.length(), UnderlineSpan.class)) {
            text.removeSpan(o);
        }

        Selection.setSelection(text,
                edit.mmBefore == null ? start
                        : (start + edit.mmBefore.length()));
    }

    /**
     * Perform redo.
     */
    public void redo() {
        EditItem edit = mEditHistory.getNext();
        if (edit == null) {
            return;
        }

        Editable text = getEditableText();
        int start = edit.mmStart;
        int end = start + (edit.mmBefore != null
                ? edit.mmBefore.length() : 0);

        mIsUndoOrRedo = true;
        text.replace(start, end, edit.mmAfter);
        mIsUndoOrRedo = false;

        // This will get rid of underlines inserted when editor tries to come
        // up with a suggestion.
        for (Object o : text.getSpans(0,
                text.length(), UnderlineSpan.class)) {
            text.removeSpan(o);
        }

        Selection.setSelection(text,
                edit.mmAfter == null ? start
                        : (start + edit.mmAfter.length()));
    }

    public void replaceAll(String what, String replace, boolean regex, boolean matchCase) {
        Pattern pattern;
        if (regex) {
            if (matchCase) {
                pattern = Pattern.compile(what);
            } else {
                pattern = Pattern.compile(what, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            }
        } else {
            if (matchCase) {
                pattern = Pattern.compile(Pattern.quote(what));
            } else {
                pattern = Pattern.compile(Pattern.quote(what), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            }
        }
//        String clone = getText().toString();
        //replace with white space
//        Matcher m = pattern.matcher(clone);
        String textToReplace = getText().toString().replaceAll(pattern.toString(), replace);
        replaceTextKeepCursor(textToReplace);
    }

    /**
     * highlight find word
     *
     * @param what     - input
     * @param regex    - is java regex
     * @param wordOnly - find word only
     */
    public void find(String what, boolean regex, boolean wordOnly, boolean matchCase) {
        Pattern pattern;
        if (regex) {
            if (matchCase) {
                pattern = Pattern.compile(what);
            } else {
                pattern = Pattern.compile(what, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            }
        } else {
            if (wordOnly) {
                if (matchCase) {
                    pattern = Pattern.compile("\\s" + what + "\\s");
                } else {
                    pattern = Pattern.compile("\\s" + Pattern.quote(what) + "\\s", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
                }
            } else {
                if (matchCase) {
                    pattern = Pattern.compile(Pattern.quote(what));
                } else {
                    pattern = Pattern.compile(Pattern.quote(what), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
                }
            }
        }
        Editable e = getEditableText();
        //remove all span
        BackgroundColorSpan spans[] = e.getSpans(0, e.length(), BackgroundColorSpan.class);
        for (int n = spans.length; n-- > 0; )
            e.removeSpan(spans[n]);
        //set span

        for (Matcher m = pattern.matcher(e); m.find(); ) {
            e.setSpan(new BackgroundColorSpan(COLOR_ERROR),
                    m.start(),
                    m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public void setLineError(int lineError) {
        this.errorLine = lineError;
    }

    /**
     * Set the maximum history size. If size is
     * negative, then history size is only limited
     * by the device memory.
     */
    public void setMaxHistorySize(int maxHistorySize) {
        mEditHistory.setMaxHistorySize(
                maxHistorySize);
    }

    /**
     * Clear history.
     */
    public void clearHistory() {
        mEditHistory.clear();
        mShowUndo = getCanUndo();
        mShowRedo = getCanRedo();
    }

    public void resetVariables() {
        mEditHistory.clear();
        enabledChangeListener = false;
        lineCount = 0;
        realLine = 0;
        startingLine = 0;
        mIsUndoOrRedo = false;
        mShowUndo = false;
        mShowRedo = false;
        canSaveFile = false;
        firstVisibleIndex = 0;
        firstColoredIndex = 0;
    }

    public void replaceTextKeepCursor(String textToUpdate) {


        int cursorPos;
        int cursorPosEnd;

        if (textToUpdate != null) {
            cursorPos = 0;
            cursorPosEnd = 0;
        } else {
            cursorPos = getSelectionStart();
            cursorPosEnd = getSelectionEnd();
        }

        disableTextChangedListener();

        if (PreferenceHelper.getSyntaxHighlight(getContext())) {
            setText(highlight(textToUpdate == null ? getEditableText() : Editable.Factory
                    .getInstance().newEditable(textToUpdate), textToUpdate != null));
        } else {
            setText(textToUpdate == null ? getEditableText() : textToUpdate);
        }

        enableTextChangedListener();

        int newCursorPos;

        boolean cursorOnScreen = cursorPos >= firstVisibleIndex && cursorPos <= lastVisibleIndex;

        if (cursorOnScreen) { // if the cursor is on screen
            newCursorPos = cursorPos; // we dont change its position
        } else {
            newCursorPos = firstVisibleIndex; // else we set it to the first visible pos
        }

        if (newCursorPos > -1 && newCursorPos <= length()) {
            if (cursorPosEnd != cursorPos)
                setSelection(cursorPos, cursorPosEnd);
            else
                setSelection(newCursorPos);
        }

    }

    public void disableTextChangedListener() {
        enabledChangeListener = false;
        removeTextChangedListener(mChangeListener);
    }

    public Editable highlight(Editable editable, boolean newText) {
        editable.clearSpans();

        if (editable.length() == 0) {
            return editable;
        }

        editorHeight = getHeight();

        int CHARS_TO_COLOR = 2500;
        if (!newText && editorHeight > 0) {
            firstVisibleIndex = getLayout().getLineStart(LineUtils.getFirstVisibleLine(verticalScroll, editorHeight, lineCount));
            lastVisibleIndex = getLayout().getLineEnd(LineUtils.getLastVisibleLine(verticalScroll, editorHeight, lineCount, deviceHeight) - 1);
        } else {
            firstVisibleIndex = 0;
            lastVisibleIndex = CHARS_TO_COLOR;
        }

        firstColoredIndex = firstVisibleIndex - (CHARS_TO_COLOR / 5);

        // normalize
        if (firstColoredIndex < 0)
            firstColoredIndex = 0;
        if (lastVisibleIndex > editable.length())
            lastVisibleIndex = editable.length();
        if (firstColoredIndex > lastVisibleIndex)
            firstColoredIndex = lastVisibleIndex;

//        CharSequence textToHighlight = editable.subSequence(firstColoredIndex, lastVisibleIndex);
//
//        highlightColor(keywords, editable, textToHighlight, firstColoredIndex);
//        highlightColor(numbers, editable, textToHighlight, firstColoredIndex);
//        highlightColor(strings, editable, textToHighlight, firstColoredIndex);
//        highlightColor(comments, editable, textToHighlight, firstColoredIndex);
//        highlightColor(symbols, editable, textToHighlight, firstColoredIndex);

//        return editable;
        return highlight(editable, firstColoredIndex, lastVisibleIndex);
    }

    private CharSequence autoIndent(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String indent = "";
        int istart = dstart - 1;
        int iend = -1;
        boolean dataBefore = false;
        int pt = 0;

        for (; istart > -1; --istart) {
            char c = dest.charAt(istart);
            if (c == '\n')
                break;
            if (c != ' ' && c != '\t') {
                if (!dataBefore) {
                    if (c == '{' ||
                            c == '+' ||
                            c == '-' ||
                            c == '*' ||
                            c == '/' ||
                            c == '%' ||
                            c == '^' ||
                            c == '=')
                        --pt;
                    dataBefore = true;
                }
                if (c == '(')
                    --pt;
                else if (c == ')')
                    ++pt;
            }
        }
        if (istart > -1) {
            char charAtCursor = dest.charAt(dstart);
            for (iend = ++istart; iend < dend; ++iend) {
                char c = dest.charAt(iend);
                if (charAtCursor != '\n' && c == '/' && iend + 1 < dend && dest.charAt(iend) == c) {
                    iend += 2;
                    break;
                }
                if (c != ' ' && c != '\t')
                    break;
            }
            indent += dest.subSequence(istart, iend);
        }
        if (pt < 0)
            indent += "\t";
        return source + indent;
    }

    /**
     * high light text from start to end
     *
     * @param start - start index
     * @param end   - end index
     */
    private Editable highlight(Editable e, int start, int end) {
        try {
            if (e.length() == 0)
                return e;
            //clear spannable
//            clearSpans(e, start, end);
            CharSequence input = e.subSequence(start, end);
            //high light error light
            int count = 0;
            if (errorLine > -1) {
                loop1:
                for (int i = 0; i < e.length(); i++) {
                    if (e.charAt(i) == '\n') count++;
                    if (count == errorLine) {
                        for (int j = i + 1; j < e.length(); j++) {
                            if (e.charAt(j) == '\n') {
                                e.setSpan(new BackgroundColorSpan(COLOR_ERROR), i, j,
                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                break loop1;
                            }
                        }
                        try {
                            e.setSpan(new BackgroundColorSpan(COLOR_ERROR), i, e.length(),
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } catch (Exception e1) {
                            Toast.makeText(mContext, e1.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            //high light number
            for (Matcher m = numbers.matcher(input); m.find(); ) {
                e.setSpan(new ForegroundColorSpan(COLOR_NUMBER),
                        start + m.start(),
                        start + m.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            for (Matcher m = keywords.matcher(input); m.find(); ) {
//                e.setSpan(new StyleSpan(BOLD),
//                        start + m.start(),
//                        start + m.end(),
//                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                e.setSpan(new ForegroundColorSpan(COLOR_KEYWORD),
                        start + m.start(),
                        start + m.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            //find it
            for (Matcher m = symbols.matcher(input); m.find(); ) {
                //if match, you can replace text with other style
                e.setSpan(new ForegroundColorSpan(COLOR_OPT),
                        start + m.start(),
                        start + m.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
            for (Matcher m = strings.matcher(input); m.find(); ) {
                ForegroundColorSpan spans[] = e.getSpans(start + m.start(), start + m.end(),
                        ForegroundColorSpan.class);

                for (int n = spans.length; n-- > 0; )
                    e.removeSpan(spans[n]);

                e.setSpan(new ForegroundColorSpan(COLOR_STRINGS),
                        start + m.start(),
                        start + m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            for (Matcher m = comments.matcher(input); m.find(); ) {
                ForegroundColorSpan spans[] = e.getSpans(start + m.start(), start + m.end(),
                        ForegroundColorSpan.class);
                for (int n = spans.length; n-- > 0; )
                    e.removeSpan(spans[n]);

                e.setSpan(new ForegroundColorSpan(COLOR_COMMENT),
                        start + m.start(),
                        start + m.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } catch (Exception ignored) {
//            ignored.printStackTrace();
        }
        return e;
    }

    public void enableTextChangedListener() {
        if (!enabledChangeListener) {
            addTextChangedListener(mChangeListener);
            enabledChangeListener = true;
        }
    }

    public LineUtils getLineUtils() {
        return lineUtils;
    }

    private void highlightColor(Pattern pattern, Editable allText, CharSequence textToHighlight, int start) {
        int color = 0;
        if (pattern.equals(comments)) {
            color = COLOR_COMMENT;
        } else if (pattern.equals(strings)) {
            color = COLOR_STRINGS;
        } else if (pattern.equals(numbers)) {
            color = COLOR_NUMBER;
        } else if (pattern.equals(keywords)) {
            color = COLOR_KEYWORD;
        } else if (pattern.equals(symbols)) {
            color = COLOR_OPT;
        }
        Matcher m = pattern.matcher(textToHighlight);
        while (m.find()) {
            allText.setSpan(
                    new ForegroundColorSpan(color),
                    start + m.start(),
                    start + m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    /**
     * Store preferences.
     */
    public void storePersistentState(SharedPreferences.Editor editor, String prefix) {
        // Store hash code of text in the editor so that we can check if the
        // editor contents has changed.
        editor.putString(prefix + ".hash",
                String.valueOf(
                        getText().toString().hashCode()));
        editor.putInt(prefix + ".maxSize",
                mEditHistory.mmMaxHistorySize);
        editor.putInt(prefix + ".position",
                mEditHistory.mmPosition);
        editor.putInt(prefix + ".size",
                mEditHistory.mmHistory.size());

        int i = 0;
        for (EditItem ei : mEditHistory.mmHistory) {
            String pre = prefix + "." + i;

            editor.putInt(pre + ".start", ei.mmStart);
            editor.putString(pre + ".before",
                    ei.mmBefore.toString());
            editor.putString(pre + ".after",
                    ei.mmAfter.toString());

            i++;
        }
    }

    /**
     * Restore preferences.
     *
     * @param prefix The preference key prefix
     *               used when state was stored.
     * @return did restore succeed? If this is
     * false, the undo history will be empty.
     */
    public boolean restorePersistentState(SharedPreferences sp, String prefix) throws IllegalStateException {

        boolean ok =
                doRestorePersistentState(sp, prefix);
        if (!ok) {
            mEditHistory.clear();
        }

        return ok;
    }

    private boolean doRestorePersistentState(SharedPreferences sp, String prefix) {

        String hash =
                sp.getString(prefix + ".hash", null);
        if (hash == null) {
            // No state to be restored.
            return true;
        }

        if (Integer.valueOf(hash)
                != getText().toString().hashCode()) {
            return false;
        }

        mEditHistory.clear();
        mEditHistory.mmMaxHistorySize =
                sp.getInt(prefix + ".maxSize", -1);

        int count = sp.getInt(prefix + ".size", -1);
        if (count == -1) {
            return false;
        }

        for (int i = 0; i < count; i++) {
            String pre = prefix + "." + i;

            int start = sp.getInt(pre + ".start", -1);
            String before =
                    sp.getString(pre + ".before", null);
            String after =
                    sp.getString(pre + ".after", null);

            if (start == -1
                    || before == null
                    || after == null) {
                return false;
            }
            mEditHistory.add(
                    new EditItem(start, before, after));
        }

        mEditHistory.mmPosition =
                sp.getInt(prefix + ".position", -1);
        return mEditHistory.mmPosition != -1;

    }

    public void setVerticalScroll(GoodScrollView verticalScroll) {
        this.verticalScroll = verticalScroll;
    }

    public String getCleanText() {
        return (getText() == null) ? "" : getText().toString();
    }


    /**
     * Class that listens to changes in the text.
     */
    private final class EditTextChangeListener implements TextWatcher {

        /**
         * The text that will be removed by the
         * change event.
         */
        private CharSequence mBeforeChange;

        /**
         * The text that was inserted by the change
         * event.
         */
        private CharSequence mAfterChange;

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (mIsUndoOrRedo) {
                return;
            }
            mBeforeChange = s.subSequence(start, start + count);
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (mIsUndoOrRedo) {
                return;
            }
            mAfterChange = s.subSequence(start, start + count);
            mEditHistory.add(new EditItem(start, mBeforeChange, mAfterChange));
        }

        public void afterTextChanged(Editable s) {
            if (errorLine != -1) {
                refresh();
                errorLine = -1;
            }

            boolean showUndo = getCanUndo();
            boolean showRedo = getCanRedo();
            if (!canSaveFile)
                canSaveFile = getCanUndo();
            if (showUndo != mShowUndo || showRedo != mShowRedo) {
                mShowUndo = showUndo;
                mShowRedo = showRedo;
            }

            handler.removeCallbacks(colorRunnableDuringEditing);
            handler.removeCallbacks(colorRunnableDuringScroll);
            handler.postDelayed(colorRunnableDuringEditing, SYNTAX_DELAY_MILLIS_LONG);
        }
    }

    private static final long SYNTAX_DELAY_MILLIS_LONG = 2000;
    private static final long SYNTAX_DELAY_MILLIS_SHORT = 150;
    /**
     * Keeps track of all the edit history of a
     * text.
     */
    private final class EditHistory {

        /**
         * The list of edits in chronological
         * order.
         */
        private final LinkedList<EditItem>
                mmHistory = new LinkedList<>();
        /**
         * The position from which an EditItem will
         * be retrieved when getNext() is called. If
         * getPrevious() has not been called, this
         * has the same value as mmHistory.size().
         */
        private int mmPosition = 0;
        /**
         * Maximum undo history size.
         */
        private int mmMaxHistorySize = -1;

        private int size() {
            return mmHistory.size();
        }

        /**
         * Clear history.
         */
        private void clear() {
            mmPosition = 0;
            mmHistory.clear();
        }

        /**
         * Adds a new edit operation to the history
         * at the current position. If executed
         * after a call to getPrevious() removes all
         * the future history (elements with
         * positions >= current history position).
         */
        private void add(EditItem item) {
            while (mmHistory.size() > mmPosition) {
                mmHistory.removeLast();
            }
            mmHistory.add(item);
            mmPosition++;

            if (mmMaxHistorySize >= 0) {
                trimHistory();
            }
        }

        /**
         * Trim history when it exceeds max history
         * size.
         */
        private void trimHistory() {
            while (mmHistory.size()
                    > mmMaxHistorySize) {
                mmHistory.removeFirst();
                mmPosition--;
            }

            if (mmPosition < 0) {
                mmPosition = 0;
            }
        }

        /**
         * Set the maximum history size. If size is
         * negative, then history size is only
         * limited by the device memory.
         */
        private void setMaxHistorySize(
                int maxHistorySize) {
            mmMaxHistorySize = maxHistorySize;
            if (mmMaxHistorySize >= 0) {
                trimHistory();
            }
        }

        /**
         * Traverses the history backward by one
         * position, returns and item at that
         * position.
         */
        private EditItem getPrevious() {
            if (mmPosition == 0) {
                return null;
            }
            mmPosition--;
            return mmHistory.get(mmPosition);
        }

        /**
         * Traverses the history forward by one
         * position, returns and item at that
         * position.
         */
        private EditItem getNext() {
            if (mmPosition >= mmHistory.size()) {
                return null;
            }

            EditItem item = mmHistory.get(mmPosition);
            mmPosition++;
            return item;
        }
    }

    /**
     * Represents the changes performed by a
     * single edit operation.
     */
    private final class EditItem {
        private final int mmStart;
        private final CharSequence mmBefore;
        private final CharSequence mmAfter;

        /**
         * Constructs EditItem of a modification
         * that was applied at position start and
         * replaced CharSequence before with
         * CharSequence after.
         */
        public EditItem(int start,
                        CharSequence before, CharSequence after) {
            mmStart = start;
            mmBefore = before;
            mmAfter = after;
        }
    }
    //endregion


}
