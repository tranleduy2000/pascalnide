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

package com.duy.pascal.interperter.libraries.graphic.model;

import android.graphics.Canvas;

import com.duy.pascal.interperter.libraries.graphic.style.FillType;

/**
 * Created by Duy on 02-Mar-17.
 */


public class Bar3DObject extends GraphObject {

    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;
    private final int depth;
    private final boolean topOn;

    public Bar3DObject(int x1, int y1, int x2, int y2, int depth, boolean topOn) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.depth = depth;
        this.topOn = topOn;
    }

    @Override
    public void draw(Canvas canvas) {
        //find x3, y3 position
        //start is (x2, y2)
        //depth ^ 2 = 2 * (x3 - x2) ^ 2
        //==> depth = sqrt(2)(x3-x2)
        //<=> depth/sqrt(2) = x3 - x2
        //<=> depth/sqrt(2) + x2 = x3
        int x3 = (int) (depth / Math.sqrt(2) + x2);
        int delta = x3 - x2;
        int y3 = y2 - delta;

        int x4 = x3;
        int y4 = y1 - delta;

        int x5 = x1 + delta;
        int y5 = y1 - delta;

        canvas.drawLine(x2, y2, x3, y3, linePaint);
        canvas.drawLine(x3, y3, x4, y4, linePaint);

        if (topOn) {
            canvas.drawLine(x2, y1, x4, y4, linePaint);
            canvas.drawLine(x4, y4, x5, y5, linePaint);
            canvas.drawLine(x5, y5, x1, y1, linePaint);
        }

        if (fillStyle != FillType.EmptyFill) {
            canvas.drawRect(x1, y1, x2, y2, fillPaint);
        }
        canvas.drawRect(x1, y1, x2, y2, linePaint);
    }
}
