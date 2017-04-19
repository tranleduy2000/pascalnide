package com.duy.pascal.backend.imageprocessing;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

import java.util.ArrayList;

/**
 * Drawing class which provides flood fill functionality. Found
 * on http://www.javagaming.org forum and adapted to be more generic. Antialiasing
 * and texture filling capability added.
 *
 * @author kingaschi (Christph Aschwanden - king@kingx.com)
 * @author moogie (Javagaming Forum)
 * @author tom  (Javagaming Forum)
 * @since April 26, 2005
 */
public class FloodFill {

    /**
     * The array used for fast flood fill. Instantiated only once to improve performance.
     */
    private ArrayList<LinePixel> linearNRTodo = new ArrayList<LinePixel>();
    /**
     * The index into linear non-recursive fill.
     */
    private int index;

    /**
     * The result data filled
     */
    private int[] imagePixels;

    /**
     * The raw pixels data
     */
    private int[] colorToFill;


    /**
     * The start x position for the fill.
     */
    private int startX;
    /**
     * The start y position for the fill.
     */
    private int startY;


    /**
     * The color to replace with the fill color.
     */
    private int startColor;
    /**
     * The width of the imagePixels to fill.
     */
    private int width;
    /**
     * The height of the imagePixels to fill.
     */
    private int height;

    private boolean usePattern = false;

    /**
     * Constructor for flood fill, requires the imagePixels for filling operation.
     *
     * @param imageToFill The imagePixels used for filling.
     */
    public FloodFill(Bitmap imageToFill) {
        this(imageToFill, null);
    }

    /**
     * Constructor for flood fill, requires the imagePixels and mask for filling operation.
     *
     * @param imageToFill The imagePixels used for filling.
     * @param pattern     The imagePixels containing the border lines.
     */
    public FloodFill(Bitmap imageToFill, Bitmap pattern) {
        // sets imagePixels to fill
        setImagePixels(imageToFill);

        // sets the mask
        setPattern(imageToFill, pattern);
    }


    /**
     * Returns the imagePixels that was filled.
     *
     * @return The imagePixels that was filled.
     */
    public int[] getImagePixels() {
        return imagePixels;
    }

    /**
     * Sets the imagePixels to be filled.
     *
     * @param imageToFill The imagePixels to be filled.
     */
    public void setImagePixels(Bitmap imageToFill) {
        // copy imagePixels to fill into buffered imagePixels first
        width = imageToFill.getWidth();
        height = imageToFill.getHeight();

        // get fill imagePixels
        imagePixels = new int[width * height];
        imageToFill.getPixels(imagePixels, 0, width, 0, 0, width, height);
    }

    /**
     * Sets the mask imagePixels which contains the borders.
     *
     * @param sourceBitmap - source imagePixels
     * @param pattern      The mask imagePixels to set. If null, the imagePixels to fill is used as
     */
    public void setPattern(Bitmap sourceBitmap, Bitmap pattern) {
        Bitmap bufferedMaskImage = Bitmap.createBitmap(width, height, sourceBitmap.getConfig());
        Canvas canvas = new Canvas(bufferedMaskImage);
        if (pattern == null) {

        } else {

            // if mask, use it
            Paint fillPaint = new Paint();
            fillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            BitmapShader bitmapShader;
            bitmapShader = new BitmapShader(pattern, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            fillPaint.setShader(bitmapShader);

            canvas.drawRect(0, 0, width, height, fillPaint);

        }
        this.colorToFill = new int[width * height];

        if (pattern != null) {
            bufferedMaskImage.getPixels(this.colorToFill, 0, width, 0, 0, width, height);
            usePattern = true;
        }
    }

    /**
     * Flood fills parts of an pixel
     * <p>
     * if use pattern fill, the color will be not effect
     *
     * @param x     The x coordinate to start filling.
     * @param y     The y coordinate to start filling.
     * @param color The new fill color.
     */
    public void fill(int x, int y, int color) {
        this.startX = x;
        this.startY = y;

        if (!usePattern) {
            for (int i = 0; i < colorToFill.length; i++) {
                colorToFill[i] = color;
            }
        }

        this.startColor = this.imagePixels[startY * width + startX];
        if (color != startColor) {
            floodFill();
        }
    }

    /**
     * Fills the line at (x, y). Then fills the line above and below the current line.
     * The border is defined as any color except the start color. Non-recursive version,
     * doesn't have JVM stack size limitations.
     */
    private void floodFill() {
        // init stack
        linearNRTodo.clear();
        index = 0;
        floodFill(startX, startY);

        // loop through todo list
        while (index < linearNRTodo.size()) {
            // get loop data
            LinePixel lineInfo = linearNRTodo.get(index);
            index++;
            int y = lineInfo.y;
            int left = lineInfo.left;
            int right = lineInfo.right;

            // check top
            if (y > 0) {
                int yOff = (y - 1) * width;
                int x = left;
                while (x <= right) {
                    if (imagePixels[yOff + x] == startColor) {
                        x = floodFill(x, y - 1);
                    }
                    x++;
                }
            }

            // check bottom
            if (y < height - 1) {
                int yOff = (y + 1) * width;
                int x = left;
                while (x <= right) {
                    if (imagePixels[yOff + x] == startColor) {
                        x = floodFill(x, y + 1);
                    }
                    x++;
                }
            }
        }
    }

    /**
     * Fills the line at (x, y). And adds to the stack.
     *
     * @param x The x-coordinate of the start position.
     * @param y The y-coordinate of the start position.
     * @return Right.
     */
    private int floodFill(int x, int y) {
        int yOff = y * width;

        // fill left of (x,y) until border or edge of imagePixels
        int left = x;
        do {
//            imagePixels[yOff + left] = fillColor;
            imagePixels[yOff + left] = colorToFill[yOff + left];
            left--;
        } while ((left >= 0) && (imagePixels[yOff + left] == startColor));

        left++;

        // fill right of (x, y) until border or edge of imagePixels
        int right = x;
        do {
//            imagePixels[yOff + right] = fillColor;
            imagePixels[yOff + right] = colorToFill[yOff + right];
            right++;
        }
        while ((right < width) && (imagePixels[yOff + right] == startColor));

        right--;

        // add to stack
        if (index == 0) {
            LinePixel linePixel = new LinePixel();
            linePixel.setInfo(left, right, y);
            linearNRTodo.add(linePixel);
        } else {
            index--;
            linearNRTodo.get(index).setInfo(left, right, y);
        }

        // return right position
        return right;
    }


}