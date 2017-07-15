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
import android.graphics.Rect;

import com.duy.pascal.interperter.builtin_libraries.graphic.style.TextDirection;
import com.duy.pascal.interperter.builtin_libraries.graphic.style.TextJustify;

/**
 * Created by Duy on 02-Mar-17.
 */

public class TextGraphObject extends GraphObject {
    private String text;
    private int x, y;

    public TextGraphObject(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);

        //y coordinate in Pascal different y coordinate in Android
        y = bounds.height() + y; //add height of text

        //align text

        float deltaY = 0;
        switch (textPaint.getVertical()) {
            case TextJustify.VERTICAL_STYLE.CenterText:
                deltaY = -bounds.height() / 2f;
                break;
            case TextJustify.VERTICAL_STYLE.BottomText:
                deltaY = 0;
                break;
            case TextJustify.VERTICAL_STYLE.TopText:
                deltaY = -bounds.height();
                break;
        }
        y = (int) (y + deltaY);
        if (textPaint.getTextDirection() == TextDirection.HORIZONTAL_DIR) {
            canvas.drawText(text, x, y, textPaint);
        } else { //vertical
            canvas.save();
            canvas.rotate(90f, x, y);
            canvas.drawText(text, x, y, textPaint);
            canvas.restore();
        }
    }

}
