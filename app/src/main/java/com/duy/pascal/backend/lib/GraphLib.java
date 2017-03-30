package com.duy.pascal.backend.lib;

import com.duy.pascal.frontend.activities.ExecuteActivity;
import com.duy.pascal.frontend.view.screen.console.ConsoleView;
import com.duy.pascal.frontend.view.screen.console.CursorConsole;
import com.duy.pascal.frontend.view.screen.graph.molel.BarObject;
import com.duy.pascal.frontend.view.screen.graph.molel.CircleObject;
import com.duy.pascal.frontend.view.screen.graph.molel.LineObject;
import com.duy.pascal.frontend.view.screen.graph.molel.RectangleObject;
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
            activity.getConsoleView().arc(x, y, stAngle, endAngle, radius);
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
        // TODO: 01-Mar-17  do something
    }

    /**
     * Draw a rectangle on the screen
     */
    public void rectangle(int x1, int y1, int x2, int y2) {
        if (activity != null)
            activity.getConsoleView().addGraphObject(new RectangleObject(x1, y1, x2, y2));
    }

    /**
     * @param x
     * @param y
     * @return color in pixel x, y of {@link ConsoleView}
     */
    public int getPixel(int x, int y) {
        return activity.getConsoleView().getColorPixel(x, y);
    }

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
     * GetColor
     * <p>
     * Return current drawing color
     * <p>
     * Declaration
     * <p>
     * Source position: graphh.inc line 772
     * <p>
     * function GetColor: Word;
     * Description
     * <p>
     * GetColor returns the current drawing color (the palette entry).
     */
    public int getColor() {
        if (activity != null)
            return activity.getConsoleView().getForegroundGraphColor();
        else
            return 0;
    }

    /**
     * SetColor
     * <p>
     * Set foreground drawing color
     * <p>
     * Declaration
     * <p>
     * Source position: graphh.inc line 773
     * <p>
     * procedure SetColor(
     * Color: Word
     * );
     * Description
     * <p>
     * Sets the foreground color to Color.
     *
     * @param color
     */
    public void setColor(int color) {
        activity.getConsoleView().setCursorGraphColor(color);
    }

    /**
     * Closegraph
     * <p>
     * Close graphical system.
     * <p>
     * Declaration
     * <p>
     * Source position: graphh.inc line 736
     * <p>
     * procedure Closegraph;
     * Description
     * <p>
     * Closes the graphical system, and restores the screen modus which was active before the graphical modus was activated.
     */
    public void closeGraph() {
        activity.getConsoleView().closeGraph();
    }

    /**
     * DetectGraph
     * <p>
     * Detect correct graphical driver to use
     * <p>
     * Declaration
     * <p>
     * Source position: graphh.inc line 759
     * <p>
     * procedure DetectGraph(
     * var GraphDriver: SmallInt;
     * var GraphMode: SmallInt
     * );
     * Description
     * <p>
     * DetectGraph checks the hardware in the PC and determines the driver and screen-modus to be used. These are returned in Driver and Modus, and can be fed to InitGraph. See the InitGraph for a list of drivers and modi.
     */
    public void detectGraph(VariableBoxer<Integer> driver, VariableBoxer<Integer> mode) {
        driver.set(0);
        driver.set(0);
    }

    /**
     * ClearDevice
     * <p>
     * Clear the complete screen
     * <p>
     * Declaration
     * <p>
     * Source position: graphh.inc line 752
     * <p>
     * procedure ClearDevice;
     * Description
     * <p>
     * Clears the graphical screen (with the current background color), and sets the pointer at (0,0).
     */
    public void clearDevice() {
        activity.getConsoleView().clearGraph();
    }

    /**
     * MoveTo
     * <p>
     * Move cursor to absolute position.
     * <p>
     * Declaration
     * <p>
     * Source position: graphh.inc line 766
     * <p>
     * procedure MoveTo(
     * X: SmallInt;
     * Y: SmallInt
     * );
     * Description
     * <p>
     * MoveTo moves the pointer to the point (X,Y).
     *
     * @param x
     * @param y
     */
    public void moveTo(int x, int y) {
        activity.getConsoleView().setCursorGraphPosition(x, y);
    }


    /**
     * LineTo
     * <p>
     * Draw a line starting from current position to a given point
     * <p>
     * Declaration
     * <p>
     * Source position: graphh.inc line 789
     * <p>
     * procedure LineTo(
     * X: SmallInt;
     * Y: SmallInt
     * );
     * Description
     * <p>
     * LineTo draws a line starting from the current pointer position to the point(DX,DY), relative to the current position, in the current line style and color. The Current position is set to the end of the line.
     */
    public void lineTo(int x, int y) {
        CursorConsole point = activity.getConsoleView().getCursorGraph();
        activity.getConsoleView().addGraphObject(new LineObject(point.x, point.y, x, y));
        activity.getConsoleView().setCursorGraphPosition(x, y);
    }

    /**
     * MoveRel
     * <p>
     * Move cursor relative to current position
     * <p>
     * Declaration
     * <p>
     * Source position: graphh.inc line 765
     * <p>
     * procedure MoveRel(
     * Dx: SmallInt;
     * Dy: SmallInt
     * );
     * Description
     * <p>
     * MoveRel moves the pointer to the point (DX,DY), relative to the current pointer position
     */
    public void moveRel(int dx, int dy) {
        CursorConsole point = activity.getConsoleView().getCursorGraph();
        activity.getConsoleView().setCursorGraphPosition(point.x + dx, point.y + dy);
    }

    // TODO: 02-Mar-17  empty method
    public int detect() {
        return 1;
    }

    // TODO: 02-Mar-17 empty method
    public Integer graphResult() {
        return new Integer(1);
    }

    public void circle(int x, int y, int r) {
        if (activity != null)
            activity.getConsoleView().addGraphObject(new CircleObject(x, y, r));
    }
}
