/*
 *  Copyright 2017 Tran Le Duy
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

package com.duy.pascal.interperter.builtin_libraries.graphic.model;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.duy.pascal.ui.DLog;

/**
 * Created by Duy on 02-Mar-17.
 */

public class RectangleObject extends GraphObject {
    private int x1, y1, x2, y2;

    public RectangleObject(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        // border
        linePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        DLog.d(TAG, "RectangleObject: (" + x1 + "," + y1 + ") (" + x2 + "," + y2 + ")");
        canvas.drawRect(x1, y1, x2, y2, linePaint);
    }
}
