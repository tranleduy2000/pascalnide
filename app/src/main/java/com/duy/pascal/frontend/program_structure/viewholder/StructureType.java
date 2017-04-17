package com.duy.pascal.frontend.program_structure.viewholder;

/**
 * Created by Duy on 17-Apr0xFFFFFFFF7.
 */

public class StructureType {
    public static final int TYPE_PROGRAM = 0;
    public static final int TYPE_FUNCTION = 1;
    public static final int TYPE_PROCEDURE = 2;
    public static final int TYPE_VARIABLE = 3;
    public static final int TYPE_CONST = 4;
    public static final int TYPE_LIBRARY = 5;
    public static final int TYPE_NONE = 6;
    public static final String[] ICONS = new String[]{"P", "f", "p", "v", "c", "L", "@"};
    public static final int[] COLORS_BACKGROUND = new int[]{
            0xffF44336,//red
            0xffE91E63,//pink
            0xff9C27B0,//purple
            0xff673AB7,//deep purple
            0xff3F51B5,//indigo
            0xff2196F3, //blue
            0xff009688//teal
    };
    public static final int[] COLORS_FOREGROUND = new int[]{
            0xFFFFFFFF,
            0xFFFFFFFF,
            0xFFFFFFFF,
            0xFFFFFFFF,
            0xFFFFFFFF,
            0xFFFFFFFF,
            0xFFFFFFFF
    };
}
