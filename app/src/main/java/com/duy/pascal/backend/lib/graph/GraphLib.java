package com.duy.pascal.backend.lib.graph;

import android.graphics.Paint;
import android.graphics.Rect;

import com.duy.pascal.backend.lib.CrtLib;
import com.duy.pascal.backend.lib.PascalLibrary;
import com.duy.pascal.backend.lib.graph.graphic_model.ArcEllipseObject;
import com.duy.pascal.backend.lib.graph.graphic_model.ArcObject;
import com.duy.pascal.backend.lib.graph.graphic_model.BarObject;
import com.duy.pascal.backend.lib.graph.graphic_model.CircleObject;
import com.duy.pascal.backend.lib.graph.graphic_model.EllipseObject;
import com.duy.pascal.backend.lib.graph.graphic_model.LineObject;
import com.duy.pascal.backend.lib.graph.graphic_model.PieSliceObject;
import com.duy.pascal.backend.lib.graph.graphic_model.PixelObject;
import com.duy.pascal.backend.lib.graph.graphic_model.RectangleObject;
import com.duy.pascal.backend.lib.graph.graphic_model.SectorObject;
import com.duy.pascal.backend.lib.graph.text.TextGraphObject;
import com.duy.pascal.frontend.activities.ExecuteActivity;
import com.duy.pascal.frontend.view.exec_screen.console.CursorConsole;
import com.js.interpreter.runtime.VariableBoxer;

import java.util.Map;

/**
 * Created by Duy on 01-Mar-17.
 */

public class GraphLib implements PascalLibrary {
    private ExecuteActivity activity;

    public GraphLib(ExecuteActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return true;
    }

    /**
     * ArcObject draws part of a circle with center at (X,Y),
     * radius radius, starting from angle start, stopping at angle stop. T
     * hese angles are measured counterclockwise.
     */
    public void arc(int x, int y, int stAngle, int endAngle, int radius) {
        if (activity != null)
            activity.getConsoleView().addGraphObject(new ArcObject(x, y, stAngle, endAngle, radius));
    }

    /**
     * Draws a rectangle with corners at (X1,Y1) and (X2,Y2) and fills
     * it with the current color and fill-style.
     */
    public void bar(int x1, int y1, int x2, int y2) {
        if (activity != null)
            activity.getConsoleView().addGraphObject(new BarObject(x1, y1, x2, y2));
    }

    /**
     * Return maximal X coordinate
     *
     * @return
     */
    public int getMaxX() {
        return activity.getConsoleView().getWidth();
    }

    /**
     * * Return maximal Y coordinate
     *
     * @return
     */
    public int getMaxY() {
        return activity.getConsoleView().getHeight();
    }

    /**
     * Initialize grpahical system
     * this method never execute in android
     *
     * @param driver
     * @param mode
     * @param pathToDriver
     */
    public void initGraph(int driver, int mode, String pathToDriver) {
        if (activity != null) {
            activity.getConsoleView().setGraphMode(true);
        }
    }

    /**
     * Draw a rectangle on the screen
     */
    public void rectangle(int x1, int y1, int x2, int y2) {
        if (activity != null)
            activity.getConsoleView().addGraphObject(new RectangleObject(x1, y1, x2, y2));
    }

    private String errorMsg = "";

    public void line(int x1, int y1, int x2, int y2) {
        if (activity != null)
            activity.getConsoleView().addGraphObject(new LineObject(x1, y1, x2, y2));
    }

    /**
     * GetY returns the Y-coordinate of the current position of the graphical pointer
     */
    public int getY() {
        if (activity != null)
            return activity.getConsoleView().getYCursorPixel();
        else
            return 0;
    }

    /**
     * GetY returns the Y-coordinate of the current position of the graphical pointer
     */
    public int getX() {
        if (activity != null)
            return activity.getConsoleView().getXCursorPixel();
        else return 0;
    }

    /**
     * @return color in pixel x, y of screen
     */
    public int getPixel(int x, int y) {
        // TODO: 09-Apr-17
        return activity.getConsoleView().getColorPixel(x, y);
    }

    /**
     * Set foreground drawing color
     * @param index
     */
    public void setColor(int index) {
        activity.getConsoleView().setPaintGraphColor(CrtLib.getColorPascal(index));
    }

    /**

     * Closes the graphical system, and restores the screen modus which was active before
     * the graphical modus was activated.
     */
    public void closeGraph() {
        activity.getConsoleView().closeGraph();
    }

    /**
     * GetColor returns the current drawing color (the palette entry).
     */
    public int getColor() {
        if (activity != null)
            return activity.getConsoleView().getForegroundGraphColor();
        else
            return 0; //black
    }

    /**
     * Clears the graphical screen (with the current background color), and sets the pointer at (0,0).
     */
    public void clearDevice() {
        activity.getConsoleView().clearGraph();
    }

    /**
     * MoveTo moves the pointer to the point (X,Y).
     */
    public void moveTo(int x, int y) {
        activity.getConsoleView().setCursorGraphPosition(x, y);
    }

    /**

     * DetectGraph checks the hardware in the PC and determines the driver and screen-modus
     * to be used. These are returned in Driver and Modus, and can be fed to InitGraph. See
     * the InitGraph for a list of drivers and module.
     */
    public void detectGraph(VariableBoxer<Integer> driver, VariableBoxer<Integer> mode) {
        driver.set(0);
        mode.set(0);
    }


    /**
     * MoveRel moves the pointer to the point (DX,DY), relative to the current pointer position
     */
    public void moveRel(int dx, int dy) {
        CursorConsole point = activity.getConsoleView().getCursorGraph();
        activity.getConsoleView().setCursorGraphPosition(point.x + dx, point.y + dy);
    }

    public int detect() {
        return 1;
    }

    public Integer graphResult() {
        return 1;
    }

    public void circle(int x, int y, int r) {
        if (activity != null)
            activity.getConsoleView().addGraphObject(new CircleObject(x, y, r));
    }

    /**
     * LineTo draws a line starting from the current pointer position to the point(DX,DY),
     * relative to the current position, in the current line style and color. The Current
     * position is set to the end of the line.
     */
    public void lineTo(int x, int y) {
        if (activity != null) {
            CursorConsole point = activity.getConsoleView().getCursorGraph();
            activity.getConsoleView().addGraphObject(new LineObject(point.x, point.y, x, y));
            activity.getConsoleView().setCursorGraphPosition(x, y);
        }
    }

    public void ellipse(int x, int y, int start, int end, int rx, int ry) {
        if (activity != null)
            activity.getConsoleView().addGraphObject(new ArcEllipseObject(x, y, start, end, rx, ry));
    }

    public void fillEllipse(int x, int y, int rx, int ry) {
        if (activity != null)
            activity.getConsoleView().addGraphObject(new EllipseObject(x, y, rx, ry));
    }

    public int getMaxColor() {
        return 15;
    }

    public void putPixel(int x, int y, int color) {
        if (activity != null)
            activity.getConsoleView().addGraphObject(new PixelObject(x, y, CrtLib.getColorPascal(color)));
    }

    public void setLineStyle(int style, int fillPattern, int width) {
        if (activity != null) {
            GraphScreen graphScreen = activity.getConsoleView().getGraphScreen();
            graphScreen.setFillStyle(style);
            graphScreen.setFillPattern(fillPattern);
            graphScreen.setLineWidth(width);
        }
    }

    public void outTextXY(int x, int y, String text) {
        if (activity != null) {
            activity.getConsoleView().addGraphObject(new TextGraphObject(text, x, y));
            //get current paint
            Paint textPaint = activity.getConsoleView().getGraphScreen().getTextPaint();
            //get width of text
            int width = (int) textPaint.measureText(text);
            //move cursor to the end of the text (bottom-right)
            CursorConsole cursorGraph = activity.getConsoleView().getCursorGraph();
            activity.getConsoleView().setCursorGraphPosition(cursorGraph.getX(),
                    cursorGraph.getY() + width);
        }
    }

    public void outText(String text) {
        CursorConsole cursorGraph = activity.getConsoleView().getCursorGraph();
        activity.getConsoleView().addGraphObject(new TextGraphObject(text, cursorGraph.getX(),
                cursorGraph.getY()));
        //get current paint
        Paint textPaint = activity.getConsoleView().getGraphScreen().getTextPaint();
        //get width of text
        int width = (int) textPaint.measureText(text);
        //move cursor to the end of the text (bottom-right)
        activity.getConsoleView().setCursorGraphPosition(cursorGraph.getX(),
                cursorGraph.getY() + width);
    }

    public void installUserFont(String path) {
        // TODO: 09-Apr-17
    }
    public int getBkColor() {
        if (activity != null)
            return activity.getConsoleView().getGraphScreen().getBackgroundColor();
        else
            return 0;
    }

    public void setBkColor(int color) {
        if (activity != null)
            activity.getConsoleView().setGraphBackground(CrtLib.getColorPascal(color));
    }

    /**
     * return width of the text (pixel) in the current font size
     */
    public int textWidth(String text) {
        if (activity != null) {
            Rect rect = new Rect();
            activity.getConsoleView().getGraphScreen().getTextBound(text, rect);
            return rect.width();
        }
        return 0;
    }

    /**
     * Set the current graphic viewport to the retangle define by then top-left (x1, y1) and then
     * bottom-right (x2, y2). If clip
     */
    public void setViewPort(int x1, int y1, int x2, int y2, boolean clip) {
        if (activity != null) {
            activity.getConsoleView().getGraphScreen().setViewPort(x1, y1, x2, y2, clip);
        }
    }


    /**
     * return height of the text (in pixel) in the current font size
     */
    public int textHeight(String text) {
        if (activity != null) {
            Rect rect = new Rect();
            activity.getConsoleView().getGraphScreen().getTextBound(text, rect);
            return rect.height();
        }
        return 0;
    }

    public void setTextStyle(int font, int direction, int size) {
        // TODO: 09-Apr-17
        if (activity != null) activity.getConsoleView().setGraphTextStyle(font, direction, size);
    }

    public void setFillStyle(int pattern, int color) {
        // TODO: 09-Apr-17
        if (activity != null) {
            GraphScreen graphScreen = activity.getConsoleView().getGraphScreen();
            graphScreen.setFillStyle(pattern);
            graphScreen.setFillColor(CrtLib.getColorPascal(color));
        }

    }

    public void setDirectVideo(boolean assess) {
        // TODO: 09-Apr-17
    }

    /**
     * Procedure Sector(X, Y : Integer; StAngle, EndAngle, XRadius, YRadius : Word)
     */
    public void sector(int x, int y, int start, int end, int rx, int ry) {
        if (activity != null) {
            activity.getConsoleView().addGraphObject(new SectorObject(x, y, start, end, rx, ry));
        }
    }

    /**
     * PieSlice draws and fills a sector of a circle with center (X,Y) and radius Radius,
     * starting at angle Start and ending at angle Stop.
     */
    public void pieSlice(int x, int y, int start, int end, int radius) {
        if (activity != null) {
            activity.getConsoleView().addGraphObject(new PieSliceObject(x, y, start, end, radius));
        }
    }

    public String graphErrorMsg() {
        // TODO: 09-Apr-17
        return errorMsg;
    }

    public void graphDefaults() {
        //// TODO: 09-Apr-17
    }

    public void getFillPattern() {

    }

    /**
     * GetAspectRatio determines the effective resolution of the screen. The aspect ration can then
     * be calculated as Xasp/Yasp.
     */
    public void getAspectRatio(VariableBoxer<Integer> x, VariableBoxer<Integer> y) {
        x.set(getMaxX());
        y.set(getMaxY());
    }

    public int getGraphMode() {
        return 1;
    }

    public String getModeName(int mode) {
        return "android_graphics";
    }

    public int getMaxMode() {
        return 1;
    }
}
