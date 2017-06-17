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

package com.duy.pascal.backend.builtin_libraries.android.gesture;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.duy.pascal.backend.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.backend.builtin_libraries.PascalLibrary;
import com.duy.pascal.backend.builtin_libraries.android.gesture.dectector.MoveGestureDetector;
import com.duy.pascal.backend.builtin_libraries.android.gesture.dectector.RotateGestureDetector;
import com.duy.pascal.backend.builtin_libraries.android.gesture.dectector.ShoveGestureDetector;
import com.duy.pascal.backend.builtin_libraries.android.gesture.listener.MoveListener;
import com.duy.pascal.backend.builtin_libraries.android.gesture.listener.RotateListener;
import com.duy.pascal.backend.builtin_libraries.android.gesture.listener.ScaleListener;
import com.duy.pascal.backend.builtin_libraries.android.gesture.listener.ShoveListener;
import com.duy.pascal.backend.builtin_libraries.android.gesture.model.BaseGestureEvent;
import com.duy.pascal.frontend.activities.IRunnablePascal;
import com.duy.pascal.frontend.view.exec_screen.console.ConsoleView;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Duy on 15-Jun-17.
 */

public class GestureAPI implements PascalLibrary, View.OnTouchListener {

    private Queue<BaseGestureEvent> gestureEventQueue = new ConcurrentLinkedQueue<>();
    private ScaleGestureDetector mScaleDetector;
    private RotateGestureDetector mRotateDetector;
    private MoveGestureDetector mMoveDetector;
    private ShoveGestureDetector mShoveDetector;

    @Nullable
    private IRunnablePascal handler;

    public GestureAPI(@Nullable IRunnablePascal handler) {
        this.handler = handler;
        if (handler != null && handler.getConsoleView() != null) {
            setup(handler.getConsoleView());
        }
    }

    private void setup(@NonNull ConsoleView consoleView) {

        // Setup Gesture Detectors
        mScaleDetector = new ScaleGestureDetector(consoleView.getContext(),
                new ScaleListener(gestureEventQueue));
        mRotateDetector = new RotateGestureDetector(consoleView.getContext(),
                new RotateListener(gestureEventQueue));
        mMoveDetector = new MoveGestureDetector(consoleView.getContext(),
                new MoveListener(gestureEventQueue));
        mShoveDetector = new ShoveGestureDetector(consoleView.getContext(),
                new ShoveListener(gestureEventQueue));

//        consoleView.addOnTouchListener(this);
//        consoleView.addLongClickListener(this);
//        consoleView.addClickListener(this);
//        consoleView.addDoubleClickListener(this);
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public String getName() {
        return "aGesture".toLowerCase();
    }

    @Override
    public void declareConstants(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareTypes(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareVariables(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin parentContext) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        mScaleDetector.onTouchEvent(event);
        mRotateDetector.onTouchEvent(event);
        mMoveDetector.onTouchEvent(event);
        mShoveDetector.onTouchEvent(event);

        return false;
    }
}
