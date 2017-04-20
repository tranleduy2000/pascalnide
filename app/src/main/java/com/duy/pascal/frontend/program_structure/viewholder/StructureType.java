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
