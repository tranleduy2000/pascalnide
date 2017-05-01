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

package com.duy.pascal.backend.pascaltypes;


import com.duy.pascal.backend.lib.graph.style.FillType;
import com.duy.pascal.backend.lib.graph.style.LineStyle;
import com.duy.pascal.backend.lib.graph.style.LineWidth;
import com.duy.pascal.backend.lib.graph.style.TextDirection;
import com.duy.pascal.backend.lib.graph.style.TextFont;
import com.duy.pascal.backend.lib.graph.style.TextJustify;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.ast.ConstantDefinition;
import com.js.interpreter.ast.expressioncontext.ExpressionContextMixin;

import java.util.Map;

/**
 * Define system constant when init parser
 * Created by Duy on 01-Mar-17.
 */
public class SystemConstants {

    public static void addSystemConstant(Map<String, ConstantDefinition> constantDefinitionMap) {
        defineColorConstants(constantDefinitionMap);
        defineGraphConstants(constantDefinitionMap);
        defineFillStyleGraphConstant(constantDefinitionMap);
    }

    private static void defineFillStyleGraphConstant(Map<String, ConstantDefinition> constants) {
        ConstantDefinition constant;
        constant = new ConstantDefinition("EmptyFill".toLowerCase(), FillType.EmptyFill,
                new LineInfo(-1, "grok = 1;".toLowerCase()));
        constants.put(constant.name(), constant);

        constant = new ConstantDefinition("SolidFill".toLowerCase(), FillType.SolidFill,
                new LineInfo(-1, "const NormWidth = 1;".toLowerCase()));
        constants.put(constant.name(), constant);

        constant = new ConstantDefinition("LineFill".toLowerCase(), FillType.LineFill,
                new LineInfo(-1, ""));
        constants.put(constant.name(), constant);
        constant = new ConstantDefinition("ltSlashFill".toLowerCase(), FillType.ltSlashFill,
                new LineInfo(-1, ""));
        constants.put(constant.name(), constant);
        constant = new ConstantDefinition("SlashFill".toLowerCase(), FillType.SlashFill,
                new LineInfo(-1, ""));
        constants.put(constant.name(), constant);
        constant = new ConstantDefinition("BkSlashFill".toLowerCase(), FillType.BkSlashFill,
                new LineInfo(-1, ""));
        constants.put(constant.name(), constant);
        constant = new ConstantDefinition("LtBkSlashFill".toLowerCase(), FillType.LtBkSlashFill,
                new LineInfo(-1, ""));
        constants.put(constant.name(), constant);
        constant = new ConstantDefinition("HatchFill".toLowerCase(), FillType.HatchFill,
                new LineInfo(-1, ""));
        constants.put(constant.name(), constant);

        constant = new ConstantDefinition("XHatchFill".toLowerCase(), FillType.XHatchFill,
                new LineInfo(-1, ""));
        constants.put(constant.name(), constant);

        constant = new ConstantDefinition("InterLeaveFill".toLowerCase(), FillType.InterLeaveFill,
                new LineInfo(-1, ""));
        constants.put(constant.name(), constant);

        constant = new ConstantDefinition("WideDotFill".toLowerCase(), FillType.WideDotFill,
                new LineInfo(-1, ""));
        constants.put(constant.name(), constant);

        constant = new ConstantDefinition("CloseDotFill".toLowerCase(), FillType.CloseDotFill,
                new LineInfo(-1, ""));
        constants.put(constant.name(), constant);
    }

    private static void defineGraphConstants(Map<String, ConstantDefinition> constants) {
        ConstantDefinition constant;
        constant = new ConstantDefinition("grok".toLowerCase(), 1, new LineInfo(-1, "grok = 1;".toLowerCase()));
        constants.put(constant.name(), constant);

        constant = new ConstantDefinition("NormWidth".toLowerCase(), LineWidth.NormWidth,
                new LineInfo(-1, "const NormWidth = 1;".toLowerCase()));
        constants.put(constant.name(), constant);
        constant = new ConstantDefinition("ThickWidth".toLowerCase(), LineWidth.ThickWidth,
                new LineInfo(-1, ""));
        constants.put(constant.name(), constant);

        constant = new ConstantDefinition("SolidLn".toLowerCase(), LineStyle.SolidLn,
                new LineInfo(-1, "const SolidLn = 0;".toLowerCase()));
        constants.put(constant.name(), constant);
        constant = new ConstantDefinition("DottedLn".toLowerCase(), LineStyle.DottedLn,
                new LineInfo(-1, "const DottedLn = 1;".toLowerCase()));
        constants.put(constant.name(), constant);
        constant = new ConstantDefinition("CenterLn".toLowerCase(), LineStyle.CenterLn,
                new LineInfo(-1, "const CenterLn = 2;".toLowerCase()));
        constants.put(constant.name(), constant);
        constant = new ConstantDefinition("DashedLn".toLowerCase(), LineStyle.DashedLn,
                new LineInfo(-1, "const DashedLn = 3;".toLowerCase()));
        constants.put(constant.name(), constant);

        //Font number: Normal font
        constant = new ConstantDefinition("DefaultFont".toLowerCase(), TextFont.TriplexFont,
                new LineInfo(-1, "const DefaultFont = 0;".toLowerCase()));
        constants.put(constant.name(), constant);

        //        Font number: Triplex font
        constant = new ConstantDefinition("TriplexFont".toLowerCase(), TextFont.TriplexFont,
                new LineInfo(-1, "const TriplexFont = 1;".toLowerCase()));
        constants.put(constant.name(), constant);

        constant = new ConstantDefinition("SmallFont".toLowerCase(), TextFont.SmallFont,
                new LineInfo(-1, "const SmallFont = 2;".toLowerCase()));
        constants.put(constant.name(), constant);

        constant = new ConstantDefinition("SansSerifFont".toLowerCase(), TextFont.SansSerifFont,
                new LineInfo(-1, "const SansSerifFont = 3;".toLowerCase()));
        constants.put(constant.name(), constant);

        constant = new ConstantDefinition("GothicFont".toLowerCase(), TextFont.GothicFont,
                new LineInfo(-1, "const GothicFont = 4;".toLowerCase()));
        constants.put(constant.name(), constant);

        constant = new ConstantDefinition("ScriptFont".toLowerCase(), TextFont.ScriptFont,
                new LineInfo(-1, "const ScriptFont = 5;".toLowerCase()));
        constants.put(constant.name(), constant);
        constant = new ConstantDefinition("SimpleFont".toLowerCase(), TextFont.SimpleFont,
                new LineInfo(-1, "const SimpleFont = 6;".toLowerCase()));
        constants.put(constant.name(), constant);
        constant = new ConstantDefinition("TSCRFont".toLowerCase(), TextFont.TSCRFont,
                new LineInfo(-1, "const TSCRFont = 7;".toLowerCase()));
        constants.put(constant.name(), constant);

        constant = new ConstantDefinition("LCOMFont".toLowerCase(), TextFont.LCOMFont,
                new LineInfo(-1, "const LCOMFont = 8;".toLowerCase()));
        constants.put(constant.name(), constant);

        constant = new ConstantDefinition("EuroFont".toLowerCase(), TextFont.EuroFont,
                new LineInfo(-1, "const EuroFont  = 9;".toLowerCase()));
        constants.put(constant.name(), constant);
        constant = new ConstantDefinition("BoldFont".toLowerCase(), TextFont.BoldFont,
                new LineInfo(-1, "const EuroFont  = 10;".toLowerCase()));
        constants.put(constant.name(), constant);

        //text direction
        constant = new ConstantDefinition("HorizDir".toLowerCase(), TextDirection.HORIZONTAL_DIR,
                new LineInfo(-1, "const HorizDir = 0;".toLowerCase()));
        constants.put(constant.name(), constant);

        constant = new ConstantDefinition("VertDir".toLowerCase(), TextDirection.VERTICAL_DIR,
                new LineInfo(-1, "const VertDir   = 1;".toLowerCase()));
        constants.put(constant.name(), constant);
        constant = new ConstantDefinition("HorizDir".toLowerCase(), TextDirection.HORIZONTAL_DIR,
                new LineInfo(-1, "const HorizDir = 0;".toLowerCase()));
        constants.put(constant.name(), constant);

        ///////////////////////////
        constant = new ConstantDefinition("LeftText".toLowerCase(), TextJustify.HORIZONTAL_STYLE.LeftText,
                new LineInfo(-1, "const LeftText   = 0;".toLowerCase()));
        constants.put(constant.name(), constant);
        constant = new ConstantDefinition("CenterText".toLowerCase(), TextJustify.HORIZONTAL_STYLE.CenterText,
                new LineInfo(-1, "const CenterText   = 1;".toLowerCase()));
        constants.put(constant.name(), constant);
        constant = new ConstantDefinition("RightText".toLowerCase(), TextJustify.HORIZONTAL_STYLE.RightText,
                new LineInfo(-1, "const RightText   = 2;".toLowerCase()));
        constants.put(constant.name(), constant);
        constant = new ConstantDefinition("BottomText".toLowerCase(), TextJustify.VERTICAL_STYLE.BottomText,
                new LineInfo(-1, "const BottomText   = 0;".toLowerCase()));
        constants.put(constant.name(), constant);
        constant = new ConstantDefinition("TopText".toLowerCase(), TextJustify.VERTICAL_STYLE.TopText,
                new LineInfo(-1, "const TopText   = 2;".toLowerCase()));
        constants.put(constant.name(), constant);

        constant = new ConstantDefinition("topoff".toLowerCase(), false,
                new LineInfo(-1, "const TopText   = 2;".toLowerCase()));
        constants.put(constant.name(), constant);
        constant = new ConstantDefinition("topon".toLowerCase(), true,
                new LineInfo(-1, "const TopText   = 2;".toLowerCase()));
        constants.put(constant.name(), constant);


    }

    /**
     * value  color
     * 0    Black
     * 1    Blue
     * 2    Green
     * 3    Cyan
     * 4    Red
     * 5    Magenta
     * 6    Brown
     * 7    White
     * 8    Grey
     * 9    Light Blue
     * 10    Light Green
     * 11    Light Cyan
     * 12    Light Red
     * 13    Light Magenta
     * 14    Yellow
     * 15    High-intensity white
     *
     * @param context
     */
    private static void defineColorConstants(Map<String, ConstantDefinition> context) {
        ConstantDefinition colorConst;
        colorConst = new ConstantDefinition("black".toLowerCase(), 0, new LineInfo(-1, "black = 0".toLowerCase()));
        context.put(colorConst.name(), colorConst);
        colorConst = new ConstantDefinition("Blue".toLowerCase(), 1, new LineInfo(-1, "Blue = 1".toLowerCase()));
        context.put(colorConst.name(), colorConst);
        colorConst = new ConstantDefinition("Green".toLowerCase(), 2, new LineInfo(-1, "Green = 2".toLowerCase()));
        context.put(colorConst.name(), colorConst);
        colorConst = new ConstantDefinition("Cyan".toLowerCase(), 3, new LineInfo(-1, "Cyan = 3".toLowerCase()));
        context.put(colorConst.name(), colorConst);
        colorConst = new ConstantDefinition("Red".toLowerCase(), 4, new LineInfo(-1, "Red = 4".toLowerCase()));
        context.put(colorConst.name(), colorConst);
        colorConst = new ConstantDefinition("Magenta".toLowerCase(), 5, new LineInfo(-1, "Magenta = 5".toLowerCase()));
        context.put(colorConst.name(), colorConst);
        colorConst = new ConstantDefinition("Brown".toLowerCase(), 6, new LineInfo(-1, "Brown = 6".toLowerCase()));
        context.put(colorConst.name(), colorConst);
        colorConst = new ConstantDefinition("LightGray".toLowerCase(), 7, new LineInfo(-1, "LightGray  = 7".toLowerCase()));
        context.put(colorConst.name(), colorConst);
        colorConst = new ConstantDefinition("DarkGray".toLowerCase(), 8, new LineInfo(-1, "DarkGray = 8".toLowerCase()));
        context.put(colorConst.name(), colorConst);
        colorConst = new ConstantDefinition("LightBlue".toLowerCase(), 9, new LineInfo(-1, "LightBlue = 9".toLowerCase()));
        context.put(colorConst.name(), colorConst);
        colorConst = new ConstantDefinition("LightGreen".toLowerCase(), 10, new LineInfo(-1, "LightGreen = 10".toLowerCase()));
        context.put(colorConst.name(), colorConst);
        colorConst = new ConstantDefinition("LightCyan".toLowerCase(), 11, new LineInfo(-1, "LightCyan = 11".toLowerCase()));
        context.put(colorConst.name(), colorConst);
        colorConst = new ConstantDefinition("LightRed".toLowerCase(), 12, new LineInfo(-1, "LightRed = 12".toLowerCase()));
        context.put(colorConst.name(), colorConst);
        colorConst = new ConstantDefinition("LightMagenta".toLowerCase(), 13, new LineInfo(-1, "LightMagenta = 13".toLowerCase()));
        context.put(colorConst.name(), colorConst);
        colorConst = new ConstantDefinition("Yellow".toLowerCase(), 14, new LineInfo(-1, " Yellow = 14".toLowerCase()));
        context.put(colorConst.name(), colorConst);
        colorConst = new ConstantDefinition("White".toLowerCase(), 15, new LineInfo(-1, "White = 15".toLowerCase()));
        context.put(colorConst.name(), colorConst);
        colorConst = new ConstantDefinition("pi".toLowerCase(), Math.PI, new LineInfo(-1, " pi = 3.14159265358979323846".toLowerCase()));
        context.put(colorConst.name(), colorConst);
    }

    /**
     * add some missing operator
     *
     * @param context
     */
    public static void addSystemType(ExpressionContextMixin context) {

    }
}
