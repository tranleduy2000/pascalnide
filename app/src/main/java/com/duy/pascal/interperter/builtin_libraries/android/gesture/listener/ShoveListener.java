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

package com.duy.pascal.interperter.builtin_libraries.android.gesture.listener;

import com.duy.pascal.interperter.builtin_libraries.android.gesture.dectector.ShoveGestureDetector;
import com.duy.pascal.interperter.builtin_libraries.android.gesture.model.BaseGestureEvent;

import java.util.Queue;

/**
 * Created by Duy on 15-Jun-17.
 */
public class ShoveListener extends ShoveGestureDetector.SimpleOnShoveGestureListener {
    private int mAlpha = 255;
    private Queue<BaseGestureEvent> gestureEventQueue;

    public ShoveListener(Queue<BaseGestureEvent> gestureEventQueue) {
        this.gestureEventQueue = gestureEventQueue;
    }

    public int getAlpha() {
        return mAlpha;
    }

    @Override
    public boolean onShove(ShoveGestureDetector detector) {
        mAlpha += detector.getShovePixelsDelta();
        if (mAlpha > 255)
            mAlpha = 255;
        else if (mAlpha < 0)
            mAlpha = 0;

        return true;
    }
}
