package com.duy.pascal.backend.lib.graph.graphic_model.style;

/**
 * Created by Duy on 09-Apr-17.
 */

public class FillType {
    public static final int EmptyFill = 0;
    //    Uses backgroundcolor.

    public static final int SolidFill = 1;
    //    Uses filling color

    public static final int LineFill = 2;
    //    Fills with horizontal lines.
    public static final int ltSlashFill = 3;
    //    Fills with lines from left-under to top-right.
    public static final int SlashFill = 4;
    //    Idem as previous, thick lines.
    public static final int BkSlashFill = 5;
    //    Fills with thick lines from left-Top to bottom-right.
    public static final int LtBkSlashFill = 6;
    //    Idem as previous, normal lines.
    public static final int HatchFill = 7;
    //    Fills with a hatch-like pattern.
    public static final int XHatchFill = 8;
    //    Fills with a hatch pattern, rotated 45 degrees.
    public static final int InterLeaveFill = 9;
    public static final int WideDotFill = 10;
    //    Fills with dots, wide spacing.
    public static final int CloseDotFill = 11;
    //    Fills with dots, narrow spacing.
    public static final int UserFill = 12;
//    Fills with a user-defined pattern.
}
