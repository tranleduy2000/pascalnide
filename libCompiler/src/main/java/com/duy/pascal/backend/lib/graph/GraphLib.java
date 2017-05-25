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

package com.duy.pascal.backend.lib.graph;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.duy.pascal.backend.imageprocessing.FloodFill;
import com.duy.pascal.backend.lib.PascalLibrary;
import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.duy.pascal.backend.lib.crt.ColorUtils;
import com.duy.pascal.backend.lib.crt.CrtLib;
import com.duy.pascal.backend.lib.graph.model.ArcEllipseObject;
import com.duy.pascal.backend.lib.graph.model.ArcObject;
import com.duy.pascal.backend.lib.graph.model.Bar3DObject;
import com.duy.pascal.backend.lib.graph.model.BarObject;
import com.duy.pascal.backend.lib.graph.model.CircleObject;
import com.duy.pascal.backend.lib.graph.model.EllipseObject;
import com.duy.pascal.backend.lib.graph.model.FillEllipseObject;
import com.duy.pascal.backend.lib.graph.model.LineObject;
import com.duy.pascal.backend.lib.graph.model.PieSliceObject;
import com.duy.pascal.backend.lib.graph.model.PixelObject;
import com.duy.pascal.backend.lib.graph.model.RectangleObject;
import com.duy.pascal.backend.lib.graph.model.SectorObject;
import com.duy.pascal.backend.lib.graph.model.TextGraphObject;
import com.duy.pascal.backend.lib.graph.paint.TextPaint;
import com.duy.pascal.backend.lib.graph.style.FillType;
import com.duy.pascal.backend.lib.graph.style.LineStyle;
import com.duy.pascal.backend.lib.graph.style.LineWidth;
import com.duy.pascal.backend.lib.graph.style.TextDirection;
import com.duy.pascal.backend.lib.graph.style.TextFont;
import com.duy.pascal.backend.lib.graph.style.TextJustify;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.RecordType;
import com.duy.pascal.frontend.activities.ExecHandler;
import com.duy.pascal.frontend.view.exec_screen.console.ConsoleCursor;
import com.duy.pascal.frontend.view.exec_screen.console.ConsoleView;
import com.js.interpreter.ConstantDefinition;
import com.js.interpreter.VariableDeclaration;
import com.js.interpreter.expressioncontext.ExpressionContextMixin;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.references.PascalReference;
import com.js.interpreter.runtime.variables.ContainsVariables;
import com.js.interpreter.runtime.variables.CustomVariable;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Duy on 01-Mar-17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class GraphLib implements PascalLibrary {
    public static final String NAME = "graph";
    private static final String TAG = "GraphLib";
    private ExecHandler handler;

    public GraphLib(ExecHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return true;
    }

    @Override
    @PascalMethod(description = "stop")

    public void shutdown() {

    }

    @Override
    public void declareConstants(ExpressionContextMixin parentContext) {
        Map<String, ConstantDefinition> constants = parentContext.getConstants();
        ConstantDefinition constant;
        constant = new ConstantDefinition("grok".toLowerCase(), 1, new LineInfo(-1, "grok = 1;".toLowerCase()));
        constants.put(constant.getName(), constant);

        constant = new ConstantDefinition("NormWidth".toLowerCase(), LineWidth.NormWidth,
                new LineInfo(-1, "const NormWidth = 1;".toLowerCase()));
        constants.put(constant.getName(), constant);
        constant = new ConstantDefinition("ThickWidth".toLowerCase(), LineWidth.ThickWidth,
                new LineInfo(-1, ""));
        constants.put(constant.getName(), constant);

        constant = new ConstantDefinition("SolidLn".toLowerCase(), LineStyle.SolidLn,
                new LineInfo(-1, "const SolidLn = 0;".toLowerCase()));
        constants.put(constant.getName(), constant);
        constant = new ConstantDefinition("DottedLn".toLowerCase(), LineStyle.DottedLn,
                new LineInfo(-1, "const DottedLn = 1;".toLowerCase()));
        constants.put(constant.getName(), constant);
        constant = new ConstantDefinition("CenterLn".toLowerCase(), LineStyle.CenterLn,
                new LineInfo(-1, "const CenterLn = 2;".toLowerCase()));
        constants.put(constant.getName(), constant);
        constant = new ConstantDefinition("DashedLn".toLowerCase(), LineStyle.DashedLn,
                new LineInfo(-1, "const DashedLn = 3;".toLowerCase()));
        constants.put(constant.getName(), constant);

        //Font number: Normal font
        constant = new ConstantDefinition("DefaultFont".toLowerCase(), TextFont.TriplexFont,
                new LineInfo(-1, "const DefaultFont = 0;".toLowerCase()));
        constants.put(constant.getName(), constant);

        //        Font number: Triplex font
        constant = new ConstantDefinition("TriplexFont".toLowerCase(), TextFont.TriplexFont,
                new LineInfo(-1, "const TriplexFont = 1;".toLowerCase()));
        constants.put(constant.getName(), constant);

        constant = new ConstantDefinition("SmallFont".toLowerCase(), TextFont.SmallFont,
                new LineInfo(-1, "const SmallFont = 2;".toLowerCase()));
        constants.put(constant.getName(), constant);

        constant = new ConstantDefinition("SansSerifFont".toLowerCase(), TextFont.SansSerifFont,
                new LineInfo(-1, "const SansSerifFont = 3;".toLowerCase()));
        constants.put(constant.getName(), constant);

        constant = new ConstantDefinition("GothicFont".toLowerCase(), TextFont.GothicFont,
                new LineInfo(-1, "const GothicFont = 4;".toLowerCase()));
        constants.put(constant.getName(), constant);

        constant = new ConstantDefinition("ScriptFont".toLowerCase(), TextFont.ScriptFont,
                new LineInfo(-1, "const ScriptFont = 5;".toLowerCase()));
        constants.put(constant.getName(), constant);
        constant = new ConstantDefinition("SimpleFont".toLowerCase(), TextFont.SimpleFont,
                new LineInfo(-1, "const SimpleFont = 6;".toLowerCase()));
        constants.put(constant.getName(), constant);
        constant = new ConstantDefinition("TSCRFont".toLowerCase(), TextFont.TSCRFont,
                new LineInfo(-1, "const TSCRFont = 7;".toLowerCase()));
        constants.put(constant.getName(), constant);

        constant = new ConstantDefinition("LCOMFont".toLowerCase(), TextFont.LCOMFont,
                new LineInfo(-1, "const LCOMFont = 8;".toLowerCase()));
        constants.put(constant.getName(), constant);

        constant = new ConstantDefinition("EuroFont".toLowerCase(), TextFont.EuroFont,
                new LineInfo(-1, "const EuroFont  = 9;".toLowerCase()));
        constants.put(constant.getName(), constant);
        constant = new ConstantDefinition("BoldFont".toLowerCase(), TextFont.BoldFont,
                new LineInfo(-1, "const EuroFont  = 10;".toLowerCase()));
        constants.put(constant.getName(), constant);

        //text direction
        constant = new ConstantDefinition("HorizDir".toLowerCase(), TextDirection.HORIZONTAL_DIR,
                new LineInfo(-1, "const HorizDir = 0;".toLowerCase()));
        constants.put(constant.getName(), constant);

        constant = new ConstantDefinition("VertDir".toLowerCase(), TextDirection.VERTICAL_DIR,
                new LineInfo(-1, "const VertDir   = 1;".toLowerCase()));
        constants.put(constant.getName(), constant);
        constant = new ConstantDefinition("HorizDir".toLowerCase(), TextDirection.HORIZONTAL_DIR,
                new LineInfo(-1, "const HorizDir = 0;".toLowerCase()));
        constants.put(constant.getName(), constant);

        ///////////////////////////
        constant = new ConstantDefinition("LeftText".toLowerCase(), TextJustify.HORIZONTAL_STYLE.LeftText,
                new LineInfo(-1, "const LeftText   = 0;".toLowerCase()));
        constants.put(constant.getName(), constant);
        constant = new ConstantDefinition("CenterText".toLowerCase(), TextJustify.HORIZONTAL_STYLE.CenterText,
                new LineInfo(-1, "const CenterText   = 1;".toLowerCase()));
        constants.put(constant.getName(), constant);
        constant = new ConstantDefinition("RightText".toLowerCase(), TextJustify.HORIZONTAL_STYLE.RightText,
                new LineInfo(-1, "const RightText   = 2;".toLowerCase()));
        constants.put(constant.getName(), constant);
        constant = new ConstantDefinition("BottomText".toLowerCase(), TextJustify.VERTICAL_STYLE.BottomText,
                new LineInfo(-1, "const BottomText   = 0;".toLowerCase()));
        constants.put(constant.getName(), constant);
        constant = new ConstantDefinition("TopText".toLowerCase(), TextJustify.VERTICAL_STYLE.TopText,
                new LineInfo(-1, "const TopText   = 2;".toLowerCase()));
        constants.put(constant.getName(), constant);

        constant = new ConstantDefinition("topoff".toLowerCase(), false,
                new LineInfo(-1, "const TopText   = 2;".toLowerCase()));
        constants.put(constant.getName(), constant);
        constant = new ConstantDefinition("topon".toLowerCase(), true,
                new LineInfo(-1, "const TopText   = 2;".toLowerCase()));
        constants.put(constant.getName(), constant);

        constant = new ConstantDefinition("EmptyFill".toLowerCase(), FillType.EmptyFill,
                new LineInfo(-1, "grok = 1;".toLowerCase()));
        constants.put(constant.getName(), constant);

        constant = new ConstantDefinition("SolidFill".toLowerCase(), FillType.SolidFill,
                new LineInfo(-1, "const NormWidth = 1;".toLowerCase()));
        constants.put(constant.getName(), constant);

        constant = new ConstantDefinition("LineFill".toLowerCase(), FillType.LineFill,
                new LineInfo(-1, ""));
        constants.put(constant.getName(), constant);
        constant = new ConstantDefinition("ltSlashFill".toLowerCase(), FillType.ltSlashFill,
                new LineInfo(-1, ""));
        constants.put(constant.getName(), constant);
        constant = new ConstantDefinition("SlashFill".toLowerCase(), FillType.SlashFill,
                new LineInfo(-1, ""));
        constants.put(constant.getName(), constant);
        constant = new ConstantDefinition("BkSlashFill".toLowerCase(), FillType.BkSlashFill,
                new LineInfo(-1, ""));
        constants.put(constant.getName(), constant);
        constant = new ConstantDefinition("LtBkSlashFill".toLowerCase(), FillType.LtBkSlashFill,
                new LineInfo(-1, ""));
        constants.put(constant.getName(), constant);
        constant = new ConstantDefinition("HatchFill".toLowerCase(), FillType.HatchFill,
                new LineInfo(-1, ""));
        constants.put(constant.getName(), constant);

        constant = new ConstantDefinition("XHatchFill".toLowerCase(), FillType.XHatchFill,
                new LineInfo(-1, ""));
        constants.put(constant.getName(), constant);

        constant = new ConstantDefinition("InterLeaveFill".toLowerCase(), FillType.InterLeaveFill,
                new LineInfo(-1, ""));
        constants.put(constant.getName(), constant);

        constant = new ConstantDefinition("WideDotFill".toLowerCase(), FillType.WideDotFill,
                new LineInfo(-1, ""));
        constants.put(constant.getName(), constant);

        constant = new ConstantDefinition("CloseDotFill".toLowerCase(), FillType.CloseDotFill,
                new LineInfo(-1, ""));
        constants.put(constant.getName(), constant);

        new CrtLib(handler).declareConstants(parentContext);

        constant = new ConstantDefinition("NormalPut".toLowerCase(), 0, new LineInfo(-1, ""));
        constants.put(constant.getName(), constant);
    }

    @Override
    public void declareTypes(ExpressionContextMixin parentContext) {
        RecordType arcType = new RecordType();
        ArrayList<VariableDeclaration> vars = new ArrayList<>();
        vars.add(new VariableDeclaration("x", BasicType.Integer));
        vars.add(new VariableDeclaration("y", BasicType.Integer));
        vars.add(new VariableDeclaration("xstart", BasicType.Integer));
        vars.add(new VariableDeclaration("ystart", BasicType.Integer));
        vars.add(new VariableDeclaration("xend", BasicType.Integer));
        vars.add(new VariableDeclaration("yend", BasicType.Integer));
        arcType.setVariableDeclarations(vars);
        parentContext.declareTypedef("ArcCoordsType", arcType);

        RecordType point = new RecordType();
        vars = new ArrayList<>();
        vars.add(new VariableDeclaration("x", BasicType.Integer));
        vars.add(new VariableDeclaration("y", BasicType.Integer));
        point.setVariableDeclarations(vars);
        parentContext.declareTypedef("pointtype", point);

    }

    @Override
    public void declareVariables(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin parentContext) {

    }

    /**
     * ArcObject draws part of a circle with center at (X,Y),
     * radius radius, starting from angle start, stopping at angle stop. T
     * hese angles are measured counterclockwise.
     */

    @PascalMethod(description = "graph library", returns = "void")
    public void arc(int x, int y, int stAngle, int endAngle, int radius) {
        if (handler != null)
            handler.getConsoleView().addGraphObject(new ArcObject(x, y, stAngle, endAngle, radius));
    }

    /**
     * Draws a rectangle with corners at (X1,Y1) and (X2,Y2) and fills
     * it with the current color and fill-style.
     */

    @PascalMethod(description = "graph library", returns = "void")
    public void bar(int x1, int y1, int x2, int y2) {
        if (handler != null)
            handler.getConsoleView().addGraphObject(new BarObject(x1, y1, x2, y2));
    }


    @PascalMethod(description = "Return maximal X coordinate", returns = "void")
    public int getMaxX() {
        return handler.getConsoleView().getWidth();
    }


    @PascalMethod(description = "Return maximal Y coordinate", returns = "void")
    public int getMaxY() {
        return handler.getConsoleView().getHeight();
    }

    /**
     * Initialize grpahical system
     * this method never execute in android
     */

    @PascalMethod(description = "graph library", returns = "void")
    public void initGraph(int driver, int mode, String pathToDriver) {
        if (handler != null) {
            handler.getConsoleView().setGraphMode(true);
        }
    }

    /**
     * Draw a rectangle on the screen
     */

    @PascalMethod(description = "graph library", returns = "void")
    public void rectangle(int x1, int y1, int x2, int y2) {
        if (handler != null)
            handler.getConsoleView().addGraphObject(new RectangleObject(x1, y1, x2, y2));
    }


    @PascalMethod(description = "graph library", returns = "void")
    public void line(int x1, int y1, int x2, int y2) {
        if (handler != null)
            handler.getConsoleView().addGraphObject(new LineObject(x1, y1, x2, y2));
    }

    /**
     * GetY returns the Y-coordinate of the current position of the graphical cursor
     */

    @PascalMethod(description = "graph library", returns = "void")
    public int getY() {
        if (handler != null)
            return handler.getConsoleView().getYCursorPixel();
        else
            return 0;
    }

    /**
     * GetY returns the Y-coordinate of the current position of the graphical cursor
     */

    @PascalMethod(description = "graph library", returns = "void")
    public int getX() {
        if (handler != null)
            return handler.getConsoleView().getXCursorPixel();
        else return 0;
    }

    /**
     * @return color in pixel x, y of screen
     */

    @PascalMethod(description = "graph library", returns = "void")
    public int getPixel(int x, int y) {
        // TODO: 09-Apr-17
        int colorPixel = handler.getConsoleView().getColorPixel(x, y);
        return ColorUtils.androidColorToPascalColor(colorPixel);
    }

    /**
     * Closes the graphical system, and restores the screen modus which was active before
     * the graphical modus was activated.
     */

    @PascalMethod(description = "graph library", returns = "void")
    public void closeGraph() {
        handler.getConsoleView().closeGraph();
    }

    /**
     * GetColor returns the current drawing color (the palette entry).
     */

    @PascalMethod(description = "graph library", returns = "void")
    public int getColor() {
        if (handler != null)
            return handler.getConsoleView().getForegroundGraphColor();
        else
            return 0; //black
    }

    /**
     * Set foreground drawing color
     */
    @PascalMethod(description = "graph library", returns = "void")
    public void setColor(int index) {
        Log.d(TAG, "setColor: " + index + ColorUtils.pascalColorToAndroidColor(index));
        handler.getConsoleView().setPaintGraphColor(ColorUtils.pascalColorToAndroidColor(index));
    }

    @PascalMethod(description = "Clears the graphical screen (with the current background color), and sets the cursor at (0,0).", returns = "void")
    public void clearDevice() {
        handler.getConsoleView().clearGraph();
    }

    @PascalMethod(description = "moves the cursor to the point (X,Y).", returns = "void")
    public void moveTo(int x, int y) {
        handler.getConsoleView().setCursorGraphPosition(x, y);
    }

    /**
     * DetectGraph checks the hardware in the PC and determines the driver and screen-modus
     * to be used. These are returned in Driver and Modus, and can be fed to InitGraph. See
     * the InitGraph for a list of drivers and module.
     */

    @PascalMethod(description = "graph library", returns = "void")
    public void detectGraph(PascalReference<Integer> driver, PascalReference<Integer> mode) {
        driver.set(0);
        mode.set(0);
    }

    /**
     * MoveRel moves the cursor to the point (DX,DY), relative to the current cursor position
     */

    @PascalMethod(description = "graph library", returns = "void")
    public void moveRel(int dx, int dy) {
        ConsoleCursor point = handler.getConsoleView().getCursorGraph();
        handler.getConsoleView().setCursorGraphPosition(point.x + dx, point.y + dy);
    }


    @PascalMethod(description = "graph library", returns = "void")
    public int detect() {
        return 1;
    }


    @PascalMethod(description = "graph library", returns = "void")
    public Integer graphResult() {
        return 1;
    }


    @PascalMethod(description = "graph library", returns = "void")
    public void circle(int x, int y, int r) {
        if (handler != null)
            handler.getConsoleView().addGraphObject(new CircleObject(x, y, r));
    }

    /**
     * LineTo draws a line starting from the current cursor position to the point(DX,DY),
     * relative to the current position, in the current line style and color. The Current
     * position is set to the end of the line.
     */

    @PascalMethod(description = "graph library", returns = "void")
    public void lineTo(int x, int y) {
        if (handler != null) {
            ConsoleCursor point = handler.getConsoleView().getCursorGraph();
            handler.getConsoleView().addGraphObject(new LineObject(point.x, point.y, x, y));
            handler.getConsoleView().setCursorGraphPosition(x, y);
        }
    }


    @PascalMethod(description = "graph library", returns = "void")
    public void ellipse(int x, int y, int start, int end, int rx, int ry) {
        if (handler != null)
            handler.getConsoleView().addGraphObject(new ArcEllipseObject(x, y, start, end, rx, ry));
    }


    @PascalMethod(description = "graph library", returns = "void")
    public void fillEllipse(int x, int y, int rx, int ry) {
        if (handler != null)
            handler.getConsoleView().addGraphObject(new EllipseObject(x, y, rx, ry));
    }


    @PascalMethod(description = "graph library", returns = "void")
    public int getMaxColor() {
        //max RGB color
        return 255 * 255 * 255;
    }


    @PascalMethod(description = "graph library", returns = "void")
    public void putPixel(int x, int y, int color) {
        if (handler != null) {
            handler.getConsoleView().addGraphObject(new PixelObject(x, y,
                    ColorUtils.pascalColorToAndroidColor(color)));
        }
    }


    @PascalMethod(description = "graph library", returns = "void")
    public void setLineStyle(int style, int linePattern, int width) {
        if (handler != null) {
            GraphScreen graphScreen = handler.getConsoleView().getGraphScreen();
            graphScreen.setLineStyle(style);
            graphScreen.setLinePattern(linePattern);
            graphScreen.setLineWidth(width);
        }
    }


    @PascalMethod(description = "graph library", returns = "void")
    public void outTextXY(int x, int y, String text) {
        if (handler != null) {

            handler.getConsoleView().addGraphObject(new TextGraphObject(text, x, y));
           /* //indexOf current paint
            Paint textPaint = handler.getConsoleView().getGraphScreen().getTextPaint();
            //indexOf width of text
            int width = (int) textPaint.measureText(text);
            //move cursor to the end of the text (bottom-right)
            CursorConsole cursorGraph = handler.getConsoleView().getCursorGraph();
            handler.getConsoleView().setCursorGraphPosition(cursorGraph.getX(),
                    cursorGraph.getY() + width);*/
        }
    }


    @PascalMethod(description = "graph library", returns = "void")
    public void outText(String text) {
        ConsoleCursor cursorGraph = handler.getConsoleView().getCursorGraph();
        handler.getConsoleView().addGraphObject(new TextGraphObject(text, cursorGraph.getX(),
                cursorGraph.getY()));
        //indexOf current paint
        Paint textPaint = handler.getConsoleView().getGraphScreen().getTextPaint();
        //indexOf width of text
        int width = (int) textPaint.measureText(text);
        //move cursor to the end of the text (bottom-right)
        handler.getConsoleView().setCursorGraphPosition(cursorGraph.getX(),
                cursorGraph.getY() + width);
    }


    @PascalMethod(description = "graph library", returns = "void")
    public void installUserFont(String path) {
        // TODO: 09-Apr-17
    }


    @PascalMethod(description = "graph library", returns = "void")
    public int getBkColor() {
        if (handler != null)
            return handler.getConsoleView().getGraphScreen().getBackgroundColor();
        else
            return 0;
    }


    @PascalMethod(description = "graph library", returns = "void")
    public void setBkColor(int color) {
        if (handler != null)
            handler.getConsoleView().setGraphBackground(ColorUtils.pascalColorToAndroidColor(color));
    }

    /**
     * return width of the text (pixel) in the current font size
     */

    @PascalMethod(description = "graph library", returns = "void")
    public int textWidth(String text) {
        if (handler != null) {
            Rect rect = new Rect();
            handler.getConsoleView().getGraphScreen().getTextBound(text, rect);
            return rect.width();
        }
        return 0;
    }

    /**
     * Set the current graphic viewport to the retangle define by then top-left (x1, y1) and then
     * bottom-right (x2, y2). If clip
     */

    @PascalMethod(description = "graph library", returns = "void")
    public void setViewPort(int x1, int y1, int x2, int y2, boolean clip) {
        if (handler != null) {
            handler.getConsoleView().getGraphScreen().setViewPort(x1, y1, x2, y2, clip);
        }
    }

    /**
     * return height of the text (in pixel) in the current font size
     */

    @PascalMethod(description = "graph library", returns = "void")
    public int textHeight(String text) {
        if (handler != null) {
            Rect rect = new Rect();
            handler.getConsoleView().getGraphScreen().getTextBound(text, rect);
            return rect.height();
        }
        return 0;
    }


    @PascalMethod(description = "graph library", returns = "void")
    public synchronized void setTextStyle(int fontID, int direction, int size) {
        // TODO: 09-Apr-17
        if (handler != null) {
            GraphScreen graphScreen = handler.getConsoleView().getGraphScreen();
            graphScreen.setTextSize(size);
            graphScreen.setTextDirection(direction);
            graphScreen.setFontID(fontID);
        }
    }


    @PascalMethod(description = "graph library", returns = "void")
    public void setFillStyle(int pattern, int color) {
        Log.d(TAG, "setFillPattern: " + pattern + " " + color);
        // TODO: 09-Apr-17
        if (handler != null) {
            GraphScreen graphScreen = handler.getConsoleView().getGraphScreen();
            graphScreen.setFillPattern(pattern);
            graphScreen.setFillColor(ColorUtils.pascalColorToAndroidColor(color));
        }

    }


    @PascalMethod(description = "graph library", returns = "void")
    public void setDirectVideo(boolean assess) {
        // TODO: 09-Apr-17
    }

    /**
     * Procedure Sector(X, Y : Integer; StAngle, EndAngle, XRadius, YRadius : Word)
     */

    @PascalMethod(description = "graph library", returns = "void")
    public void sector(int x, int y, int start, int end, int rx, int ry) {
        if (handler != null) {
            handler.getConsoleView().addGraphObject(new SectorObject(x, y, start, end, rx, ry));
        }
    }

    /**
     * PieSlice draws and fills a sector of a circle with center (X,Y) and radius Radius,
     * starting at angle Start and ending at angle Stop.
     */

    @PascalMethod(description = "graph library", returns = "void")
    public void pieSlice(int x, int y, int start, int end, int radius) {
        if (handler != null) {
            handler.getConsoleView().addGraphObject(new PieSliceObject(x, y, start, end, radius));
        }
    }


    @PascalMethod(description = "graph library", returns = "void")
    public String graphErrorMsg(int errorCode) {
        // TODO: 09-Apr-17
        return "";
    }


    @PascalMethod(description = "graph library", returns = "void")
    public void graphDefaults() {
        //// TODO: 09-Apr-17
    }


    @PascalMethod(description = "graph library", returns = "void")
    public void getFillPattern() {

    }

    /**
     * GetAspectRatio determines the effective resolution of the screen. The aspect ration can then
     * be calculated as Xasp/Yasp.
     */
    @PascalMethod(description = "Return screen resolution")
    public void getAspectRatio(PascalReference<Integer> x, PascalReference<Integer> y) {
        x.set(getMaxX());
        y.set(getMaxY());
    }


    @PascalMethod(description = "graph library", returns = "void")
    public int getGraphMode() {
        return 1;
    }


    @PascalMethod(description = "graph library", returns = "void")
    public String getModeName(int mode) {
        return "android_graphics";
    }


    @PascalMethod(description = "graph library", returns = "void")
    public int getMaxMode() {
        return 1;
    }


    @PascalMethod(description = "graph library", returns = "void")
    public void setTextJustify(int horizontal, int vertical) {
        if (handler != null) {
            TextJustify textJustify = handler.getConsoleView().getGraphScreen().getTextJustify();
            textJustify.setHorizontal(horizontal);
            textJustify.setVertical(vertical);
        }
    }


    @PascalMethod(description = "graph library", returns = "void")
    public void Bar3D(int x1, int y1, int x2, int y2, int depth, boolean topOn) {
        if (handler != null) {
            handler.getConsoleView().addGraphObject(new Bar3DObject(x1, y1, x2, y2, depth, topOn));
        }
    }


    @PascalMethod(description = "graph library", returns = "void")
    public void FillEllipse(int x, int y, int rx, int ry) {
        Log.d(TAG, "FillEllipse: ");
        if (handler != null) {
            handler.getConsoleView().addGraphObject(new FillEllipseObject(x, y, rx, ry));
        }
    }


    @PascalMethod(description = "graph library", returns = "void")
    public void FloodFill(int x, int y, int borderColorIndex) {
        Log.d(TAG, "FloodFill: ");
        if (handler != null) {
            GraphScreen graphScreen = handler.getConsoleView().getGraphScreen();
            Bitmap graphBitmap = graphScreen.getGraphBitmap();

            Bitmap fillBitmap = graphScreen.getFillBitmap();

            FloodFill floodFill = new FloodFill(graphBitmap, fillBitmap);
            floodFill.fill(x, y, Color.BLUE, ColorUtils.pascalColorToAndroidColor(borderColorIndex));

            int[] imagePixels = floodFill.getImagePixels();

            graphBitmap.setPixels(imagePixels.clone(), 0, graphBitmap.getWidth(), 0, 0, graphBitmap.getWidth(),
                    graphBitmap.getHeight());
            floodFill.gc();
            //fillBitmap.recycle();
        }
    }


    @PascalMethod(description = "graph library", returns = "void")
    public void SetTextJustify(int horizontal, int vertical) {
        if (handler != null) {
            GraphScreen graphScreen = handler.getConsoleView().getGraphScreen();
            TextPaint textPaint = graphScreen.getTextPaint();
            textPaint.setTextJustify(new TextJustify(horizontal, vertical));
        }
    }

    @PascalMethod(description = "Return size to store image")
    public long ImageSize(int x1, int y1, int x2, int y2) {
        return Math.abs((x2 - x1) * (y2 - y1));
    }

    @PascalMethod(description = "Return a copy of a screen area")
    @SuppressWarnings("unchecked")
    public void getImage(int x1, int y1, int x2, int y2, PascalReference pascalPointer) {
        GraphScreen graphScreen = handler.getConsoleView().getGraphScreen();
        Bitmap graphBitmap = graphScreen.getGraphBitmap();

        Bitmap crop = Bitmap.createBitmap(graphBitmap, x1, y1, (x2 - x1), (y2 - y1));
        pascalPointer.set(crop);
    }

    @PascalMethod(description = "Draw an in-memory image to the screen")
    public void putImage(int x1, int y1, PascalReference pascalPointer, int mode)
            throws RuntimePascalException {
        //indexOf graph bitmap
        GraphScreen graphScreen = handler.getConsoleView().getGraphScreen();
        Bitmap graphBitmap = graphScreen.getGraphBitmap();

        //draw bitmap
        Canvas canvas = new Canvas(graphBitmap);
        Bitmap bitmap = (Bitmap) pascalPointer.get();
        canvas.drawBitmap(bitmap, x1, y1, null);

        //invailidate screen
        handler.getConsoleView().postInvalidate();
    }

    @PascalMethod(description = "Return coordinates of last drawn arc or ellipse.")
    public void getArcCoords(PascalReference<ContainsVariables> var) {
        ArrayList<VariableDeclaration> vars = new ArrayList<>();
        vars.add(new VariableDeclaration("x", BasicType.Integer, 1, null));
        vars.add(new VariableDeclaration("y", BasicType.Integer, 1, null));
        vars.add(new VariableDeclaration("xstart", BasicType.Integer, 1, null));
        vars.add(new VariableDeclaration("ystart", BasicType.Integer, 1, null));
        vars.add(new VariableDeclaration("xend", BasicType.Integer, 1, null));
        vars.add(new VariableDeclaration("yend", BasicType.Integer, 1, null));

        CustomVariable customVariable = new CustomVariable(vars);
        var.set(customVariable);
    }

    @PascalMethod(description = "Return height (in pixels) of the given string")
    public int TextHeight(String text) {
        GraphScreen graphScreen = handler.getConsoleView().getGraphScreen();
        TextPaint textPaint = graphScreen.getTextPaint();
        Rect rect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    @PascalMethod(description = "Return width (in pixels) of the given string")
    public int TextWidth(String text) {
        GraphScreen graphScreen = handler.getConsoleView().getGraphScreen();
        TextPaint textPaint = graphScreen.getTextPaint();
        Rect rect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }

    @PascalMethod(description = "Restore text screen")
    public void RestoreCrtMode() {
        ConsoleView consoleView = handler.getConsoleView();
        consoleView.setGraphMode(false);
        consoleView.postInvalidate();
    }

    @PascalMethod(description = "Set graphical mode")
    public void SetGraphMode(int mode) {
        ConsoleView consoleView = handler.getConsoleView();
        consoleView.setGraphMode(true);
        consoleView.postInvalidate();
    }

    // ignore
    @PascalMethod(description = "Return lowest and highest modus of current driver")
    public void GetModeRange(int graphMode, PascalReference<Integer> low, PascalReference<Integer> high) {
        low.set(-1);
        high.set(-1);
    }

}
