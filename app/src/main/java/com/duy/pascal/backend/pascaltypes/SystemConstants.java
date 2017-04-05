package com.duy.pascal.backend.pascaltypes;


import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.ast.ConstantDefinition;
import com.js.interpreter.ast.expressioncontext.ExpressionContextMixin;

/**
 * Define system constant when init parser
 * Created by Duy on 01-Mar-17.
 */
public class SystemConstants {

    public static void addSystemConstant(ExpressionContextMixin context) {
        defineColorCode(context);
        defineGraphConstant(context);
    }

    private static void defineGraphConstant(ExpressionContextMixin context) {
        ConstantDefinition colorConst;
        colorConst = new ConstantDefinition("grok".toLowerCase(), 1, new LineInfo(-1, "grok = 1;".toLowerCase()));
        context.declareConst(colorConst);
        colorConst = new ConstantDefinition("NormWidth".toLowerCase(), 1, new LineInfo(-1, "const NormWidth = 1;".toLowerCase()));
        context.declareConst(colorConst);
        colorConst = new ConstantDefinition("ThickWidth".toLowerCase(), 1, new LineInfo(-1, "const ThickWidth = 3;".toLowerCase()));
        context.declareConst(colorConst);
        colorConst = new ConstantDefinition("SolidLn".toLowerCase(), 1, new LineInfo(-1, "const SolidLn = 0;".toLowerCase()));
        context.declareConst(colorConst);
        colorConst = new ConstantDefinition("SolidLn".toLowerCase(), 1, new LineInfo(-1, "const SolidLn = 0;".toLowerCase()));
        context.declareConst(colorConst);
        colorConst = new ConstantDefinition("DottedLn".toLowerCase(), 1, new LineInfo(-1, "const DottedLn = 1;".toLowerCase()));
        context.declareConst(colorConst);
        colorConst = new ConstantDefinition("CenterLn".toLowerCase(), 1, new LineInfo(-1, "const CenterLn = 2;".toLowerCase()));
        context.declareConst(colorConst);
        colorConst = new ConstantDefinition("DashedLn".toLowerCase(), 1, new LineInfo(-1, "const DashedLn = 3;".toLowerCase()));
        context.declareConst(colorConst);
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
     */
    private static void defineColorCode(ExpressionContextMixin context) {
        ConstantDefinition colorConst;
        colorConst = new ConstantDefinition("black".toLowerCase(), 0, new LineInfo(-1, "black = 0".toLowerCase()));
        context.declareConst(colorConst);
        colorConst = new ConstantDefinition("Blue".toLowerCase(), 1, new LineInfo(-1, "Blue = 1".toLowerCase()));
        context.declareConst(colorConst);
        colorConst = new ConstantDefinition("Green".toLowerCase(), 2, new LineInfo(-1, "Green = 2".toLowerCase()));
        context.declareConst(colorConst);
        colorConst = new ConstantDefinition("Cyan".toLowerCase(), 3, new LineInfo(-1, "Cyan = 3".toLowerCase()));
        context.declareConst(colorConst);
        colorConst = new ConstantDefinition("Red".toLowerCase(), 4, new LineInfo(-1, "Red = 4".toLowerCase()));
        context.declareConst(colorConst);
        colorConst = new ConstantDefinition("Magenta".toLowerCase(), 5, new LineInfo(-1, "Magenta = 5".toLowerCase()));
        context.declareConst(colorConst);
        colorConst = new ConstantDefinition("Brown".toLowerCase(), 6, new LineInfo(-1, "Brown = 6".toLowerCase()));
        context.declareConst(colorConst);
        colorConst = new ConstantDefinition("LightGray".toLowerCase(), 7, new LineInfo(-1, "LightGray  = 7".toLowerCase()));
        context.declareConst(colorConst);
        colorConst = new ConstantDefinition("DarkGray".toLowerCase(), 8, new LineInfo(-1, "DarkGray = 8".toLowerCase()));
        context.declareConst(colorConst);
        colorConst = new ConstantDefinition("LightBlue".toLowerCase(), 9, new LineInfo(-1, "LightBlue = 9".toLowerCase()));
        context.declareConst(colorConst);
        colorConst = new ConstantDefinition("LightGreen".toLowerCase(), 10, new LineInfo(-1, "LightGreen = 10".toLowerCase()));
        context.declareConst(colorConst);
        colorConst = new ConstantDefinition("LightCyan".toLowerCase(), 11, new LineInfo(-1, "LightCyan = 11".toLowerCase()));
        context.declareConst(colorConst);
        colorConst = new ConstantDefinition("LightRed".toLowerCase(), 12, new LineInfo(-1, "LightRed = 12".toLowerCase()));
        context.declareConst(colorConst);
        colorConst = new ConstantDefinition("LightMagenta".toLowerCase(), 13, new LineInfo(-1, "LightMagenta = 13".toLowerCase()));
        context.declareConst(colorConst);
        colorConst = new ConstantDefinition("Yellow".toLowerCase(), 14, new LineInfo(-1, " Yellow = 14".toLowerCase()));
        context.declareConst(colorConst);
        colorConst = new ConstantDefinition("White".toLowerCase(), 15, new LineInfo(-1, "White = 15".toLowerCase()));
        context.declareConst(colorConst);

        colorConst = new ConstantDefinition("pi".toLowerCase(), Math.PI, new LineInfo(-1, " pi = 3.14159265358979323846".toLowerCase()));
        context.declareConst(colorConst);
    }

    /**
     * add some missing type
     *
     * @param context
     */
    public static void addSystemType(ExpressionContextMixin context) {
        context.declareTypedef("text", BasicType.Integer);
    }
}
