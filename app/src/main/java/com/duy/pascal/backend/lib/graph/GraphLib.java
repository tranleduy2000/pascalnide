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
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.duy.pascal.backend.imageprocessing.FloodFill;
import com.duy.pascal.backend.lib.ColorUtils;
import com.duy.pascal.backend.lib.PascalLibrary;
import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.duy.pascal.backend.lib.graph.graphic_model.ArcEllipseObject;
import com.duy.pascal.backend.lib.graph.graphic_model.ArcObject;
import com.duy.pascal.backend.lib.graph.graphic_model.Bar3DObject;
import com.duy.pascal.backend.lib.graph.graphic_model.BarObject;
import com.duy.pascal.backend.lib.graph.graphic_model.CircleObject;
import com.duy.pascal.backend.lib.graph.graphic_model.EllipseObject;
import com.duy.pascal.backend.lib.graph.graphic_model.FillEllipseObject;
import com.duy.pascal.backend.lib.graph.graphic_model.LineObject;
import com.duy.pascal.backend.lib.graph.graphic_model.PieSliceObject;
import com.duy.pascal.backend.lib.graph.graphic_model.PixelObject;
import com.duy.pascal.backend.lib.graph.graphic_model.RectangleObject;
import com.duy.pascal.backend.lib.graph.graphic_model.SectorObject;
import com.duy.pascal.backend.lib.graph.graphic_model.TextGraphObject;
import com.duy.pascal.backend.lib.graph.paint.TextPaint;
import com.duy.pascal.backend.lib.graph.style.TextJustify;
import com.duy.pascal.frontend.activities.ExecHandler;
import com.duy.pascal.frontend.view.exec_screen.console.CursorConsole;
import com.js.interpreter.runtime.PascalReference;

import java.util.Map;

import static com.duy.pascal.backend.lib.ColorUtils.pascalColorToAndroidColor;

/**
 * Created by Duy on 01-Mar-17.
 */
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
        Log.d(TAG, "setColor: " + index + pascalColorToAndroidColor(index));
        handler.getConsoleView().setPaintGraphColor(pascalColorToAndroidColor(index));
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
        CursorConsole point = handler.getConsoleView().getCursorGraph();
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
            CursorConsole point = handler.getConsoleView().getCursorGraph();
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
        return 15;
    }


    @PascalMethod(description = "graph library", returns = "void")
    public void putPixel(int x, int y, int color) {
        if (handler != null)
            handler.getConsoleView().addGraphObject(new PixelObject(x, y, pascalColorToAndroidColor(color)));
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
           /* //get current paint
            Paint textPaint = handler.getConsoleView().getGraphScreen().getTextPaint();
            //get width of text
            int width = (int) textPaint.measureText(text);
            //move cursor to the end of the text (bottom-right)
            CursorConsole cursorGraph = handler.getConsoleView().getCursorGraph();
            handler.getConsoleView().setCursorGraphPosition(cursorGraph.getX(),
                    cursorGraph.getY() + width);*/
        }
    }


    @PascalMethod(description = "graph library", returns = "void")
    public void outText(String text) {
        CursorConsole cursorGraph = handler.getConsoleView().getCursorGraph();
        handler.getConsoleView().addGraphObject(new TextGraphObject(text, cursorGraph.getX(),
                cursorGraph.getY()));
        //get current paint
        Paint textPaint = handler.getConsoleView().getGraphScreen().getTextPaint();
        //get width of text
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
            handler.getConsoleView().setGraphBackground(pascalColorToAndroidColor(color));
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
            graphScreen.setFillColor(pascalColorToAndroidColor(color));
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
        String errorMsg = "";
        return errorMsg;
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

    @PascalMethod(description = "graph library", returns = "void")
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
            floodFill.fill(x, y, Color.BLUE, pascalColorToAndroidColor(borderColorIndex));

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

}
