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

package com.duy.pascal.backend.lib.graph.paint;

import android.graphics.Paint;

import com.duy.pascal.backend.lib.graph.style.LineStyle;
import com.duy.pascal.backend.lib.graph.style.LineWidth;

/**
 * Created by Duy on 20-Apr-17.
 */

public class LinePaint extends Paint {
    private int lineWidth = LineWidth.NormWidth;
    private int lineStyle = LineStyle.SolidLn;
    private int linePattern;

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setLineStyle(int lineStyle) {
        this.lineStyle = lineStyle;
    }
}
