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

package com.duy.pascal.backend.builtin_libraries.graphic.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Duy on 09-Apr-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class PieSliceObject extends GraphObject {
    private int x, y, radius, startAngel, endAngle;

    public PieSliceObject(int x, int y, int startAngel, int endAngle, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.startAngel = startAngel;
        this.endAngle = endAngle;
        linePaint.setStyle(Paint.Style.STROKE);
    }


    @Override
    public void draw(Canvas canvas) {
        float dx = radius;
        //bound
        RectF rectF = new RectF(x - dx, y - dx, x + dx, y + dx);

        canvas.save();
        //rotate canvas by 180 degree
        canvas.rotate(-180, x, y);
        //reverse canvas
        canvas.scale(-1, 1, x, y);
        canvas.drawArc(rectF, startAngel, endAngle - startAngel, true, fillPaint);
        canvas.drawArc(rectF, startAngel, endAngle - startAngel, true, linePaint);
        canvas.restore();
    }
}
