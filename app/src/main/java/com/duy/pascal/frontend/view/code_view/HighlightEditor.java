package com.duy.pascal.frontend.view.code_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Scroller;

import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.EditorSetting;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.editor.EditorListener;
import com.duy.pascal.frontend.theme.CodeThemeUtils;
import com.duy.pascal.frontend.theme.ThemeFromAssets;
import com.duy.pascal.frontend.utils.FontManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

import static android.graphics.Typeface.BOLD;
import static com.duy.pascal.frontend.data.PatternsUtils.comments;
import static com.duy.pascal.frontend.data.PatternsUtils.general_strings;
import static com.duy.pascal.frontend.data.PatternsUtils.keywords;
import static com.duy.pascal.frontend.data.PatternsUtils.line;
import static com.duy.pascal.frontend.data.PatternsUtils.numbers;
import static com.duy.pascal.frontend.data.PatternsUtils.symbols;
import static com.duy.pascal.frontend.data.PatternsUtils.trailingWhiteSpace;

public abstract class HighlightEditor extends AutoSuggestsEditText implements EditorListener, View.OnKeyListener, GestureDetector.OnGestureListener {
    public static final String TAG = HighlightEditor.class.getSimpleName();
    private static final String INDEX_CHAR = "m";
    private static final int TAB_NUMBER = 3;
    private final Handler updateHandler = new Handler();
    public boolean showLineNumbers = true;
    public boolean syntaxHighlighting = true;
    public float textSize = 13;
    public boolean wordWrap = true;
    public boolean flingToScroll = true;
    public OnTextChangedListener onTextChangedListener = null;
    public int updateDelay = 100;
    public int errorLine = -1;
    public boolean dirty = false;
    protected Paint mPaintNumbers;
    protected Paint mPaintHighlight;
    protected int mPaddingDP = 4;
    protected int mPadding, mLinePadding;
    protected float mScale;
    protected Scroller mScroller;
    protected GestureDetector mGestureDetector;
    protected Point mMaxSize;
    protected int mHighlightedLine;
    protected int mHighlightStart;
    protected Rect mDrawingRect, mLineBounds;
    //Colors
    protected int COLOR_ERROR;
    protected int COLOR_NUMBER;
    protected int COLOR_KEYWORD;
    protected int COLOR_COMMENT;
    protected int COLOR_OPT;
    protected int COLOR_BOOLEANS;
    protected int COLOR_STRINGS;
    private Context mContext;
    private CRC32 mCRC32;
    private boolean modified = true;
    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            Editable e = getText();
            if (onTextChangedListener != null)
                onTextChangedListener.onTextChanged(e.toString());
            if (syntaxHighlighting)
                highlightWithoutChange(e);
        }
    };
    private int mOldTextlength = 0;
    private long mOldTextCrc32 = 0;
    private int scrollX = 0, scrollY = 0;
    private EditorSetting mPreferences;
    private boolean canEdit = true;

    public HighlightEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
        init();
    }

    public HighlightEditor(Context context) {
        super(context);
        setup(context);
        init();
    }

    public HighlightEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context);
        init();
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public boolean isFlingToScroll() {
        return flingToScroll;
    }

    public void setFlingToScroll(boolean flingToScroll) {
        this.flingToScroll = flingToScroll;
    }

    private void setup(Context context) {
        this.mContext = context;
        mPaintNumbers = new Paint();
        mPaintNumbers.setColor(getResources().getColor(R.color.number_color));
        mPaintNumbers.setAntiAlias(true);
        mPaintHighlight = new Paint();
        mScale = context.getResources().getDisplayMetrics().density;
        mPadding = (int) (mPaddingDP * mScale);
        mHighlightedLine = mHighlightStart = -1;
        mDrawingRect = new Rect();
        mLineBounds = new Rect();
        mGestureDetector = new GestureDetector(getContext(), this);
        updateFromSettings(context);
    }


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

        setTypeface(FontManager.getInstance(mContext));
    }

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

        setTypeface(FontManager.getInstance(mContext));
    }

    public void computeScroll() {
        if (mScroller != null) {
            if (mScroller.computeScrollOffset()) {
                scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            }
        } else {
            super.computeScroll();
        }
    }

    public void setLineError(int lineError) {
        this.errorLine = lineError;
    }

    @Override
    public void onDraw(Canvas canvas) {
        int count, lineX, baseline;
        count = getLineCount();
        if (showLineNumbers) {
            int padding = (int) (Math.floor(Math.log10(count)) + 1);
            padding = (int) ((padding * mPaintNumbers.getTextSize())
                    + mPadding + (textSize * mScale * 0.5));
            if (mLinePadding != padding) {
                mLinePadding = padding;
                setPadding(mLinePadding, mPadding, mPadding, mPadding);
            }
        }

        getDrawingRect(mDrawingRect);
        lineX = (int) (mDrawingRect.left + mLinePadding - (textSize * mScale * 0.5));
        int min = 0;
        int max = count;
        getLineBounds(0, mLineBounds);
        int startBottom = mLineBounds.bottom;
        int startTop = mLineBounds.top;
        getLineBounds(count - 1, mLineBounds);
        int endBottom = mLineBounds.bottom;
        int endTop = mLineBounds.top;
        if (count > 1 && endBottom > startBottom && endTop > startTop) {
            min = Math.max(min, ((mDrawingRect.top - startBottom) * (count - 1)) / (endBottom - startBottom));
            max = Math.min(max, ((mDrawingRect.bottom - startTop) * (count - 1)) / (endTop - startTop) + 1);
        }
        for (int i = min; i < max; i++) {
            baseline = getLineBounds(i, mLineBounds);
            if ((mMaxSize != null) && (mMaxSize.x < mLineBounds.right)) {
                mMaxSize.x = mLineBounds.right;
            }
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
        getLineBounds(count - 1, mLineBounds);
        if (mMaxSize != null) {
            mMaxSize.y = mLineBounds.bottom;
            mMaxSize.x = Math.max(mMaxSize.x + mPadding - mDrawingRect.width(), 0);
            mMaxSize.y = Math.max(mMaxSize.y + mPadding - mDrawingRect.height(), 0);
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (mGestureDetector != null) {
            return mGestureDetector.onTouchEvent(event);
        }
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override

    public boolean onSingleTapUp(MotionEvent e) {
        if (canEdit) {
            ((InputMethodManager) getContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE)).showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        mScroller.setFriction(0);
        return true;
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (!flingToScroll) {
            return true;
        }
        if (mScroller != null) {
            mScroller.fling(getScrollX(), getScrollY(), -(int) velocityX,
                    -(int) velocityY, 0, mMaxSize.x, 0, mMaxSize.y);
        }
        return true;
    }

    public void updateFromSettings(Context c) {
        mPreferences = new EditorSetting(c);
        String name = mPreferences.getString(mContext.getString(R.string.key_code_theme));
        try {
            Integer id = Integer.parseInt(name);
            setTheme(id);
        } catch (Exception e) {
            setTheme(name);
        }
        setHorizontallyScrolling(!mPreferences.isWrapText());
//        mPaintHighlight.setAlpha(48);
        setTextSize(mPreferences.getTextSize());
        mPaintNumbers.setTextSize(getTextSize() * 0.85f);
        showLineNumbers = mPreferences.isShowLineNumbers();
        postInvalidate();
        refreshDrawableState();

        if (flingToScroll) {
            mScroller = new Scroller(getContext());
            mMaxSize = new Point();
        } else {
            mScroller = null;
            mMaxSize = null;
        }

        mLinePadding = mPadding;
        int count = getLineCount();
        if (showLineNumbers) {
            mLinePadding = (int) (Math.floor(Math.log10(count)) + 1);
            mLinePadding = (int) ((mLinePadding * mPaintNumbers.getTextSize())
                    + mPadding + (textSize * mScale * 0.5));
            setPadding(mLinePadding, mPadding, mPadding, mPadding);
        } else {
            setPadding(mPadding, mPadding, mPadding, mPadding);
        }
        highlightWithoutChange(getEditableText());
    }

//    public void setSelection(int start, int stop) {
//        try {
//            Selection.setSelection(getText(), start, stop);
//        } catch (Exception e) {
//            e.printStackTrace();
//            // TODO: 09-Mar-17 fix selection
//        }
//    }

//    public void setSelection(int index) {
//        try {
//            Selection.setSelection(getText(), index);
//        } catch (Exception e) {
//            e.printStackTrace();
//            // TODO: 09-Mar-17 fix selection
//        }
//    }

//    public void selectAll() {
//        Selection.selectAll(getText());
//    }

    @Override
    protected boolean getDefaultEditable() {
        return true;
    }

//    public void extendSelection(int index) {
//        Selection.extendSelection(getText(), index);
//    }

    @Override
    protected MovementMethod getDefaultMovementMethod() {
        return ArrowKeyMovementMethod.getInstance();
    }

    @Override
    public Editable getText() {
        return super.getText();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, BufferType.EDITABLE);
    }

    public void setTextHighlighted(CharSequence text) {
        cancelUpdate();
        errorLine = -1;
        dirty = false;
        modified = false;
        setText(highlight(new SpannableStringBuilder(text)));
        modified = true;
        if (onTextChangedListener != null)
            onTextChangedListener.onTextChanged(text.toString());
    }

    public String getCleanText() {
        return trailingWhiteSpace.matcher(getText()).replaceAll("");
    }

    public void refresh() {
        cancelUpdate();
        dirty = false;
        modified = false;
        highlightWithoutChange(getText());
        modified = true;
    }

    @Override
    public void init() {
        super.init();
        mCRC32 = new CRC32();
        setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source,
                                               int start, int end, Spanned dest, int dstart, int dend) {
                        if (modified && end - start == 1 && start < source.length() &&
                                dstart < dest.length()) {
                            char c = source.charAt(start);
                            if (c == '\n')
                                return autoIndent(source, start, end, dest, dstart, dend);
                        }
                        return source;
                    }
                }
        });

        addTextChangedListener(new TextWatcher() {
            int start = 0, end = 0;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                this.start = start;
                this.end = start + count;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable e) {
                errorLine = -1;
                cancelUpdate();
                if (!modified)
                    return;
                dirty = true;
                applyTabWidth(e, start, end);
                updateHandler.postDelayed(updateRunnable, updateDelay);
            }
        });
    }

    @Override
    public void show() {
        setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        setVisibility(View.GONE);
    }

    @Override
    public String getString() {
        String text = "";
        try {
            text = getText().toString();
        } catch (OutOfMemoryError e) {
        }
        return text;
    }

    @Override
    public void updateTextFinger() {
        mOldTextlength = getText().length();
        byte bytes[] = getString().getBytes();
        mCRC32.reset();
        mCRC32.update(bytes, 0, bytes.length);
        mOldTextCrc32 = mCRC32.getValue();
    }

    @Override
    public boolean isTextChanged() {
        CharSequence text = getText();
        int hash = text.length();
        if (mOldTextlength != hash) {
            return true;
        }
        mCRC32.reset();
        byte bytes[] = getString().getBytes();
        mCRC32.update(bytes, 0, bytes.length);
        return mOldTextCrc32 != mCRC32.getValue();
    }

    private void cancelUpdate() {
        updateHandler.removeCallbacks(updateRunnable);
    }

    private void highlightWithoutChange(Editable e) {
        modified = false;
        highlight(e);
        modified = true;
    }

    private Editable highlight(Editable e) {
        long time = System.currentTimeMillis();
        try {
            clearSpans(e);
            if (e.length() == 0)
                return e;
            if (errorLine > -1) {
                Matcher m = line.matcher(e);
                int count = 0;
                while (m.find()) {
                    if (count == errorLine) {
                        e.setSpan(new BackgroundColorSpan(COLOR_ERROR),
                                m.start(),
                                m.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        break;
                    }
                    count++;
                }
            }

            for (Matcher m = numbers.matcher(e); m.find(); ) {
                e.setSpan(new ForegroundColorSpan(COLOR_NUMBER),
                        m.start(),
                        m.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            for (Matcher m = keywords.matcher(e); m.find(); ) {
                e.setSpan(new StyleSpan(BOLD), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                e.setSpan(new ForegroundColorSpan(COLOR_KEYWORD),
                        m.start(),
                        m.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            //find it
            for (Matcher m = symbols.matcher(e); m.find(); ) {
                //if match, you can replace text with other style
                e.setSpan(new ForegroundColorSpan(COLOR_OPT),
                        m.start(),
                        m.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            for (Matcher m = general_strings.matcher(e); m.find(); ) {
                ForegroundColorSpan spans[] = e.getSpans(m.start(), m.end(), ForegroundColorSpan.class);
                for (int n = spans.length; n-- > 0; )
                    e.removeSpan(spans[n]);
                e.setSpan(new ForegroundColorSpan(COLOR_STRINGS), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            for (Matcher m = comments.matcher(e); m.find(); ) {
                ForegroundColorSpan spans[] = e.getSpans(m.start(), m.end(), ForegroundColorSpan.class);
                for (int n = spans.length; n-- > 0; )
                    e.removeSpan(spans[n]);
                e.setSpan(new ForegroundColorSpan(COLOR_COMMENT),
                        m.start(),
                        m.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        return e;
    }

    /**
     * remove all spanned
     */
    private void clearSpans(Editable e) {
        {
            ForegroundColorSpan spans[] = e.getSpans(0, e.length(), ForegroundColorSpan.class);
            for (int n = spans.length; n-- > 0; )
                e.removeSpan(spans[n]);
        }
        {
            BackgroundColorSpan spans[] = e.getSpans(0, e.length(), BackgroundColorSpan.class);
            for (int n = spans.length; n-- > 0; )
                e.removeSpan(spans[n]);
        }
        {
            StyleSpan[] spans = e.getSpans(0, e.length(), StyleSpan.class);
            for (int n = spans.length; n-- > 0; )
                e.removeSpan(spans[n]);
        }

    }

    /**
     * auto tab
     */
    private CharSequence autoIndent(CharSequence source,
                                    int start, int end, Spanned dest, int dstart, int dend) {
        if (DLog.DEBUG) Log.d(TAG, "autoIndent: " + source);
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
        String clone = getText().toString();
        //replace with white space
//        Matcher m = pattern.matcher(clone);
        setText(getText().toString().replaceAll(pattern.toString(), replace));
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

    /**
     * move cursor to line
     *
     * @param line - line in editor, begin at 1
     */
    public void goToLine(int line) {
        String text = getText().toString();
        int c = 0;
        int index = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n') {
                c++;
                if (c == line) {
                    index = i;
                    break;
                }
            }
        }
        if (index == -1) {
            setSelection(text.length());
        } else {
            setSelection(index);
        }
    }

    /**
     * set position for list popup of {@link android.widget.AutoCompleteTextView}
     */
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        showPopupSuggest();
    }

    @Override
    public void showPopupSuggest() {
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
                int offsetVertical = (int) ((y + mCharHeight) - scrollY);

                int tmp = offsetVertical + getDropDownHeight() + mCharHeight;
                if (tmp < heightVisible) {
                    tmp = offsetVertical + mCharHeight / 2;
                    setDropDownVerticalOffset(tmp);
                } else {
                    tmp = offsetVertical - getDropDownHeight() - mCharHeight;
                    setDropDownVerticalOffset(tmp);
                }
                Log.d(TAG, "showPopupSuggest: " + offsetVertical + " " + tmp + " " + scrollY);
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

    /**
     * This method call when scroll view scroll
     */
    public void onMove(int l, int t) {
        this.scrollX = l;
        this.scrollY = t;
    }


    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return super.onCreateInputConnection(outAttrs);
    }

    public void applyTabWidth(Editable text, int start, int end) {
        String str = text.toString();
        float tabWidth = getPaint().measureText(INDEX_CHAR) * TAB_NUMBER;
        while (start < end) {
            int index = str.indexOf("\t", start);
            if (index < 0)
                break;
            text.setSpan(new CustomTabWidthSpan(Float.valueOf(tabWidth).intValue()), index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            start = index + 1;
        }
    }

    public void applyTabWidth() {
        applyTabWidth(getEditableText(), 0, getEditableText().length());
    }

    public interface OnTextChangedListener {
        void onTextChanged(String text);
    }
}
