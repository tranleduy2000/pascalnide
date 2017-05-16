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

package com.duy.pascal.backend.lib.graph.graphic_model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Duy on 02-Mar-17.
 */

public class ArcObject extends GraphObject {
    private int x, y, stAngle, enAngle, radius;

    public ArcObject(int x, int y, int stAngle, int enAngle, int radius) {
        this.x = x;
        this.y = y;
        this.stAngle = stAngle;
        this.enAngle = enAngle;
        this.radius = radius;
        linePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        //bound of arc
        RectF rectF = new RectF(x - radius, y - radius, x + radius, y + radius);

        //rotate
        canvas.save();
        canvas.rotate(-90, x, y);
        canvas.drawArc(rectF, stAngle, enAngle, false, linePaint);
        canvas.restore();
    }

}
