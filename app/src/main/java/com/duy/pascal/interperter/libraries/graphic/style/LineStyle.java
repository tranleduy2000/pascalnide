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

package com.duy.pascal.interperter.libraries.graphic.style;

import android.graphics.DashPathEffect;

/**
 * Created by Duy on 08-Apr-17.
 */

public class LineStyle {
    //    draws a solid line.
    public static final int SolidLn = 0;

    //    Draws a dotted line.
    public static final int DottedLn = 1;

    //    draws a non-broken centered line.
    public static final int CenterLn = 2;

    //    draws a dashed line.
    public static final int DashedLn = 3;

    //2 - 2
    public static DashPathEffect dottedPath = new DashPathEffect(new float[]{10, 20}, 0);

    //3 - 2
    public static DashPathEffect dashedPath = new DashPathEffect(new float[]{10, 20}, 0);


}
