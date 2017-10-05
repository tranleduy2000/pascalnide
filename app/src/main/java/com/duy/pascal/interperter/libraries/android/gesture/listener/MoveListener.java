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

package com.duy.pascal.interperter.libraries.android.gesture.listener;

import android.graphics.PointF;

import com.duy.pascal.interperter.libraries.android.gesture.dectector.MoveGestureDetector;
import com.duy.pascal.interperter.libraries.android.gesture.model.BaseGestureEvent;

import java.util.Queue;

/**
 * Created by Duy on 15-Jun-17.
 */
public class MoveListener extends MoveGestureDetector.SimpleOnMoveGestureListener {
    private float mFocusX = 0.f;
    private float mFocusY = 0.f;
    private Queue<BaseGestureEvent> gestureEventQueue;

    public MoveListener(Queue<BaseGestureEvent> gestureEventQueue) {

        this.gestureEventQueue = gestureEventQueue;
    }

    public float getFocusX() {
        return mFocusX;
    }

    public float getFocusY() {
        return mFocusY;
    }

    @Override
    public boolean onMove(MoveGestureDetector detector) {
        PointF d = detector.getFocusDelta();
        mFocusX += d.x;
        mFocusY += d.y;
        return true;
    }
}
