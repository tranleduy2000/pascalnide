package com.duy.pascal.backend.lib.graph.style;

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
