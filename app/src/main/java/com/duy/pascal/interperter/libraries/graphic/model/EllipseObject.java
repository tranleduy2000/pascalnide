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
import android.graphics.RectF;

/**
 * Created by Duy on 09-Apr-17.
 */


public class EllipseObject extends GraphObject {
    private int x, y, rx, ry;

    /**
     * @param x  - x coordinate
     * @param y  - y coordinate
     * @param rx - horizontal radius
     * @param ry - vertical radius
     */
    public EllipseObject(int x, int y, int rx, int ry) {
        this.x = x;
        this.y = y;
        this.rx = rx;
        this.ry = ry;
    }


    @Override
    public void draw(Canvas canvas) {
        float dx = rx;
        float dy = ry;

        //bound
        RectF rectF = new RectF(x - dx, y - dy, x + dx, y + dy);
        canvas.drawOval(rectF, linePaint);
    }
}
