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

package com.duy.pascal.interperter.builtin_libraries.graphic.model;

import android.graphics.Canvas;
import android.graphics.Path;

import com.duy.pascal.interperter.ast.runtime_value.value.RecordValue;

/**
 * Created by Duy on 09-Jun-17.
 */

public class PolygonObject extends GraphObject {

    private final RecordValue[] point;
    private final int numPoint;

    public PolygonObject(RecordValue[] point, int numPoint) {
        this.point = point;
        this.numPoint = numPoint;
    }

    @Override
    public void draw(Canvas canvas) {
        Path path = new Path();
        int x = (int) point[0].getVar("x");
        int y = (int) point[0].getVar("y");
        path.moveTo(x, y);
        for (int i = 1; i < numPoint; i++) {
            x = (int) point[i].getVar("x");
            y = (int) point[i].getVar("y");
            path.lineTo(x, y);
        }
        canvas.drawPath(path, fillPaint);
    }
}
