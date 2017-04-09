package com.duy.pascal.backend.lib.graph.graphic_model.style;

import android.graphics.DashPathEffect;

/**
 * Created by Duy on 08-Apr-17.
 */

public class LineStyle {
    //    draws a solid line.
    public static final int Solidln = 0;

    //    Draws a dotted line.
    public static final int Dottedln = 1;

    //    draws a non-broken centered line.
    public static final int Centerln = 3;

    //    draws a dashed line.
    public static final int Dashedln = 4;

    //2 - 2
    public static DashPathEffect dottedPath = new DashPathEffect(new float[]{10, 20}, 0);

    //3 - 2
    public static DashPathEffect ashedPath = new DashPathEffect(new float[]{10, 20}, 0);


}
