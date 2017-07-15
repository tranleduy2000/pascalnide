/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.interperter.builtin_libraries.android.gesture.dectector;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public abstract class TwoFingerGestureDetector extends BaseGestureDetector {

    private final float mEdgeSlop;
    protected float mPrevFingerDiffX;
    protected float mPrevFingerDiffY;
    protected float mCurrFingerDiffX;
    protected float mCurrFingerDiffY;
    private float mRightSlopEdge;
    private float mBottomSlopEdge;
    private float mCurrLen;
    private float mPrevLen;

    public TwoFingerGestureDetector(Context context) {
        super(context);

        ViewConfiguration config = ViewConfiguration.get(context);
        mEdgeSlop = config.getScaledEdgeSlop();
    }

    /**
     * MotionEvent has no getRawX(int) method; simulate it pending future API approval.
     *
     * @param event
     * @param pointerIndex
     * @return
     */
    protected static float getRawX(MotionEvent event, int pointerIndex) {
        float offset = event.getX() - event.getRawX();
        if (pointerIndex < event.getPointerCount()) {
            return event.getX(pointerIndex) + offset;
        }
        return 0f;
    }

    /**
     * MotionEvent has no getRawY(int) method; simulate it pending future API approval.
     *
     * @param event
     * @param pointerIndex
     * @return
     */
    protected static float getRawY(MotionEvent event, int pointerIndex) {
        float offset = event.getY() - event.getRawY();
        if (pointerIndex < event.getPointerCount()) {
            return event.getY(pointerIndex) + offset;
        }
        return 0f;
    }

    @Override
    protected abstract void handleStartProgressEvent(int actionCode, MotionEvent event);

    @Override
    protected abstract void handleInProgressEvent(int actionCode, MotionEvent event);

    protected void updateStateByEvent(MotionEvent curr) {
        super.updateStateByEvent(curr);

        final MotionEvent prev = mPrevEvent;

        mCurrLen = -1;
        mPrevLen = -1;

        // Previous
        final float px0 = prev.getX(0);
        final float py0 = prev.getY(0);
        final float px1 = prev.getX(1);
        final float py1 = prev.getY(1);
        final float pvx = px1 - px0;
        final float pvy = py1 - py0;
        mPrevFingerDiffX = pvx;
        mPrevFingerDiffY = pvy;

        // Current
        final float cx0 = curr.getX(0);
        final float cy0 = curr.getY(0);
        final float cx1 = curr.getX(1);
        final float cy1 = curr.getY(1);
        final float cvx = cx1 - cx0;
        final float cvy = cy1 - cy0;
        mCurrFingerDiffX = cvx;
        mCurrFingerDiffY = cvy;
    }

    /**
     * Return the current distance between the two pointers forming the
     * gesture in progress.
     *
     * @return Distance between pointers in pixels.
     */
    public float getCurrentSpan() {
        if (mCurrLen == -1) {
            final float cvx = mCurrFingerDiffX;
            final float cvy = mCurrFingerDiffY;
            mCurrLen = (float) Math.sqrt(cvx * cvx + cvy * cvy);
        }
        return mCurrLen;
    }

    /**
     * Return the previous distance between the two pointers forming the
     * gesture in progress.
     *
     * @return Previous distance between pointers in pixels.
     */
    public float getPreviousSpan() {
        if (mPrevLen == -1) {
            final float pvx = mPrevFingerDiffX;
            final float pvy = mPrevFingerDiffY;
            mPrevLen = (float) Math.sqrt(pvx * pvx + pvy * pvy);
        }
        return mPrevLen;
    }

    /**
     * Check if we have a sloppy gesture. Sloppy gestures can happen if the edge
     * of the user's hand is touching the screen, for example.
     *
     * @param event
     * @return
     */
    protected boolean isSloppyGesture(MotionEvent event) {
        // As orientation can change, query the metrics in touch down
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        mRightSlopEdge = metrics.widthPixels - mEdgeSlop;
        mBottomSlopEdge = metrics.heightPixels - mEdgeSlop;

        final float edgeSlop = mEdgeSlop;
        final float rightSlop = mRightSlopEdge;
        final float bottomSlop = mBottomSlopEdge;

        final float x0 = event.getRawX();
        final float y0 = event.getRawY();
        final float x1 = getRawX(event, 1);
        final float y1 = getRawY(event, 1);

        boolean p0sloppy = x0 < edgeSlop || y0 < edgeSlop
                || x0 > rightSlop || y0 > bottomSlop;
        boolean p1sloppy = x1 < edgeSlop || y1 < edgeSlop
                || x1 > rightSlop || y1 > bottomSlop;

        if (p0sloppy && p1sloppy) {
            return true;
        } else if (p0sloppy) {
            return true;
        } else if (p1sloppy) {
            return true;
        }
        return false;
    }

}
