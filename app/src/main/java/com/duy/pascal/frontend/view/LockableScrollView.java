package com.duy.pascal.frontend.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class LockableScrollView extends ScrollView {
    public static final String TAG = LockableScrollView.class.getSimpleName();
    int lastY;
    private boolean scrollable = true;
    private ScrollListener scrollListener;

    public LockableScrollView(Context context) {
        super(context);
    }

    public LockableScrollView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public void setScrollingEnabled(boolean enabled) {
        scrollable = enabled;
    }

    public boolean isScrollable() {
        return scrollable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return scrollable && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return !(ev.getAction() == MotionEvent.ACTION_DOWN && !scrollable) && super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (scrollListener == null || !scrollable) return;

        if (Math.abs(lastY - t) > 120) {
            lastY = t;
            if (scrollListener != null) scrollListener.onScroll(l, t);
        }
    }


    public ScrollListener getScrollListener() {
        return scrollListener;
    }

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public interface ScrollListener {
        void onScroll(int x, int y);
    }
}

