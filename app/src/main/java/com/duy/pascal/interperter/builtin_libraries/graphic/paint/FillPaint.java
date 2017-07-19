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

package com.duy.pascal.interperter.builtin_libraries.graphic.paint;

import android.graphics.Paint;

import com.duy.pascal.interperter.builtin_libraries.graphic.style.FillType;

/**
 * Created by Duy on 20-Apr-17.
 */
public class FillPaint extends Paint implements Cloneable {
    protected int fillPattern = FillType.EmptyFill;
    protected int fillColor = -1;//white

    public void setFillPattern(int fillPattern) {
        this.fillPattern = fillPattern;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
