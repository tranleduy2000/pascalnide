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
import android.graphics.Paint;

/**
 * Created by Duy on 02-Mar-17.
 */

public class PixelObject extends GraphObject {
    private final Paint paint = new Paint();
    private int x;
    private int y;

    public PixelObject(int x, int y, int color) {
        this.x = x;
        this.y = y;
        paint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPoint(x, y, paint);
    }
}
