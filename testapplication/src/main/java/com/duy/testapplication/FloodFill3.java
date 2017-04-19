package com.duy.testapplication;

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
public class FloodFill3 {

    /**
     * The array used for fast flood fill. Instantiated only once to improve performance.
     */
    private ArrayList<LineInfo> linearNRTodo = new ArrayList<LineInfo>();
    /**
     * The index into linear non-recursive fill.
     */
    private int index;
    /**
     * True, if antialised fill should be used.
     */
    private boolean antialiased = true;
    /**
     * The raw image data to fill.
     */
    private int[] image;

    /**
     * The raw mask image data with the borders.
     */
    private int[] maskImage;
    /**
     * The start x position for the fill.
     */
    private int startX;
    /**
     * The start y position for the fill.
     */
    private int startY;
    /**
     * The fill color to use for the original image.
     */
    private int fillColor;
    /**
     * The fill color to use for the mask image.
     */
    private int maskColor;
    /**
     * The pattern fill color to use.
     */
    private int patternColor;
    /**
     * The width of the chessboard pattern.
     */
    private int patternWidth;
    /**
     * The height of the chessboard pattern.
     */
    private int patternHeight;
    /**
     * The color to replace with the fill color.
     */
    private int startColor;
    /**
     * The width of the image to fill.
     */
    private int width;
    /**
     * The height of the image to fill.
     */
    private int height;

    /**
     * Constructor for flood fill, requires the image for filling operation.
     *
     * @param imageToFill The image used for filling.
     */
    public FloodFill3(Bitmap imageToFill) {
        this(imageToFill, null);
    }

    /**
     * Constructor for flood fill, requires the image and mask for filling operation.
     *
     * @param imageToFill The image used for filling.
     * @param maskImage   The image containing the border lines.
     */
    public FloodFill3(Bitmap imageToFill, Bitmap maskImage) {
        // sets image to fill
        setImage(imageToFill);

        // sets the mask
        setMask(imageToFill, maskImage);
    }


    /**
     * Returns true, if antialiased filling is used.
     *
     * @return True, for antialiased filling.
     */
    public boolean isAntialiased() {
        return this.antialiased;
    }

    /**
     * Sets if antialiased filling is used.
     *
     * @param antialiased True, for antialiased filling.
     */
    public void setAntialiased(boolean antialiased) {
        this.antialiased = antialiased;
    }

    /**
     * Returns the width of the fill area.
     *
     * @return The width of the fill area.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Returns the height of the fill area.
     *
     * @return The height of the fill area.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Returns the image that was filled.
     *
     * @return The image that was filled.
     */
    public int[] getImage() {
        return image;
    }

    /**
     * Sets the image to be filled.
     *
     * @param imageToFill The image to be filled.
     */
    public void setImage(Bitmap imageToFill) {
        // copy image to fill into buffered image first
        width = imageToFill.getWidth();
        height = imageToFill.getHeight();

        // get fill image
        image = new int[width * height];
        imageToFill.getPixels(image, 0, width, 0, 0, width, height);
    }


    /**
     * Sets the mask image which contains the borders.
     *
     * @param sourceImage - source image
     * @param maskImage   The mask image to set. If null, the image to fill is used as
     */
    public void setMask(Bitmap sourceImage, Bitmap maskImage) {
        Bitmap bufferedMaskImage = Bitmap.createBitmap(width, height, sourceImage.getConfig());
        Canvas canvas = new Canvas(bufferedMaskImage);
        if (maskImage == null) {
            // if no mask, use the original image to fill
            canvas.drawBitmap(sourceImage, 0, 0, new Paint());
        } else {
            // if mask, use it
            Paint fillPaint = new Paint();
            fillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            BitmapShader bitmapShader;
            bitmapShader = new BitmapShader(maskImage, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            fillPaint.setShader(bitmapShader);

            canvas.drawRect(0, 0, width, height, fillPaint);
        }
        this.maskImage = new int[width * height];
        bufferedMaskImage.getPixels(this.maskImage, 0, width, 0, 0, width, height);
    }

    /**
     * Flood fills parts of an image.
     *
     * @param x     The x coordinate to start filling.
     * @param y     The y coordinate to start filling.
     * @param color The new fill color.
     */
    public void fill(int x, int y, int color) {
        this.startX = x;
        this.startY = y;
//        this.fillColor = color.getRGB();
//        this.maskColor = color.getRGB();  // the fill color for the mask
        this.fillColor = color;
        this.maskColor = color;
        this.startColor = this.maskImage[startY * width + startX];
        if (fillColor != startColor) {
            floodFill();
        }
    }

    /**
     * Fills a bounded area of an image. Uses a chessboard pattern. The width in pixels
     * of the chessboard pattern can be specified.
     *
     * @param x             The x coordinate to start filling.
     * @param y             The y coordinate to start filling.
     * @param color         The new fill color.
     * @param patternColor  The color of the pattern to use.
     * @param patternWidth  The width of the chessboard pattern.
     * @param patternHeight The height of the chessboard pattern.
     */
    public void fill(int x, int y, int color, int patternColor, int patternWidth, int patternHeight) {
        this.startX = x;
        this.startY = y;
//        this.fillColor = color.getRGB();
        this.fillColor = color;
//        this.patternColor = patternColor.getRGB();
        this.patternColor = patternColor;

        this.patternWidth = (patternWidth > 0) ? patternWidth : Integer.MAX_VALUE;
        this.patternHeight = (patternHeight > 0) ? patternHeight : Integer.MAX_VALUE;
        this.startColor = this.maskImage[startY * width + startX];

        // the fill color for the mask
        this.maskColor = ((this.fillColor >> 2) & 0x3F3F3F3F)
                + ((this.patternColor >> 1) & 0x7F7F7F7F);

        if (this.maskColor != this.startColor) {
            // mask color ok (=different)
            floodFill2();
        } else {
            // create new mask color first
            this.maskColor = ((this.fillColor >> 1) & 0x7F7F7F7F)
                    + ((this.patternColor >> 2) & 0x3F3F3F3F);
            floodFill2();
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
            LineInfo lineInfo = linearNRTodo.get(index);
            index++;
            int y = lineInfo.y;
            int left = lineInfo.left;
            int right = lineInfo.right;

            // check top
            if (y > 0) {
                int yOff = (y - 1) * width;
                int x = left;
                while (x <= right) {
                    if (maskImage[yOff + x] == startColor) {
                        x = floodFill(x, y - 1);
                    } else {
                        // fill antialised if allowed
                        if (antialiased) {
                            int antialiasedColor = maskImage[yOff + x];
                            antialiasedColor = (antialiasedColor >> 1) & 0x7F7F7F7F;
                            antialiasedColor = antialiasedColor + ((fillColor >> 1) & 0x7F7F7F7F);
                            if (antialiasedColor != startColor) {
                                image[yOff + x] = antialiasedColor;
                            }
                        }
                    }
                    x++;
                }
            }

            // check bottom
            if (y < height - 1) {
                int yOff = (y + 1) * width;
                int x = left;
                while (x <= right) {
                    if (maskImage[yOff + x] == startColor) {
                        x = floodFill(x, y + 1);
                    } else {
                        // fill antialised if allowed
                        if (antialiased) {
                            int antialiasedColor = maskImage[yOff + x];
                            antialiasedColor = (antialiasedColor >> 1) & 0x7F7F7F7F;
                            antialiasedColor = antialiasedColor + ((fillColor >> 1) & 0x7F7F7F7F);
                            if (antialiasedColor != startColor) {
                                image[yOff + x] = antialiasedColor;
                            }
                        }
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

        // fill left of (x,y) until border or edge of image
        int left = x;
        do {
            image[yOff + left] = fillColor;
            maskImage[yOff + left] = maskColor;
            left--;
        }
        while ((left >= 0) && (maskImage[yOff + left] == startColor));
        // fill antialised if allowed
        if ((antialiased) && (left >= 0)) {
            int antialiasedColor = maskImage[yOff + left];
            antialiasedColor = (antialiasedColor >> 1) & 0x7F7F7F7F;
            antialiasedColor = antialiasedColor + ((fillColor >> 1) & 0x7F7F7F7F);
            if (antialiasedColor != startColor) {
                image[yOff + left] = antialiasedColor;
            }
        }
        left++;

        // fill right of (x, y) until border or edge of image
        int right = x;
        do {
            image[yOff + right] = fillColor;
            maskImage[yOff + right] = maskColor;
            right++;
        }
        while ((right < width) && (maskImage[yOff + right] == startColor));
        // fill antialised if allowed
        if ((antialiased) && (right < width)) {
            int antialiasedColor = maskImage[yOff + right];
            antialiasedColor = (antialiasedColor >> 1) & 0x7F7F7F7F;
            antialiasedColor = antialiasedColor + ((fillColor >> 1) & 0x7F7F7F7F);
            if (antialiasedColor != startColor) {
                image[yOff + right] = antialiasedColor;
            }
        }
        right--;

        // add to stack
        if (index == 0) {
            LineInfo lineInfo = new LineInfo();
            lineInfo.setInfo(left, right, y);
            linearNRTodo.add(lineInfo);
        } else {
            index--;
            linearNRTodo.get(index).setInfo(left, right, y);
        }

        // return right position
        return right;
    }

    /**
     * Fills the line at (x, y). Then fills the line above and below the current line.
     * The border is defined as any color except the start color. Non-recursive version,
     * doesn't have JVM stack size limitations.
     */
    private void floodFill2() {
        // init stack
        linearNRTodo.clear();
        index = 0;
        floodFill2(startX, startY);

        // loop through todo list
        while (index < linearNRTodo.size()) {
            // get loop data
            LineInfo lineInfo = linearNRTodo.get(index);
            index++;
            int y = lineInfo.y;
            int left = lineInfo.left;
            int right = lineInfo.right;

            // check top
            if (y > 0) {
                int yOff = (y - 1) * width;
                int x = left;
                while (x <= right) {
                    if (maskImage[yOff + x] == startColor) {
                        x = floodFill2(x, y - 1);
                    } else {
                        // fill antialised if allowed
                        if (antialiased) {
                            int antialiasedColor = maskImage[yOff + x];
                            antialiasedColor = (antialiasedColor >> 1) & 0x7F7F7F7F;
                            antialiasedColor = antialiasedColor + ((fillColor2(x, y - 1) >> 1) & 0x7F7F7F7F);
                            if (antialiasedColor != startColor) {
                                image[yOff + x] = antialiasedColor;
                            }
                        }
                    }
                    x++;
                }
            }

            // check bottom
            if (y < height - 1) {
                int yOff = (y + 1) * width;
                int x = left;
                while (x <= right) {
                    if (maskImage[yOff + x] == startColor) {
                        x = floodFill2(x, y + 1);
                    } else {
                        // fill antialised if allowed
                        if (antialiased) {
                            int antialiasedColor = maskImage[yOff + x];
                            antialiasedColor = (antialiasedColor >> 1) & 0x7F7F7F7F;
                            antialiasedColor = antialiasedColor + ((fillColor2(x, y + 1) >> 1) & 0x7F7F7F7F);
                            if (antialiasedColor != startColor) {
                                image[yOff + x] = antialiasedColor;
                            }
                        }
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
    private int floodFill2(int x, int y) {
        int yOff = y * width;

        // fill left of (x,y) until border or edge of image
        int left = x;
        do {
            image[yOff + left] = fillColor2(left, y);
            maskImage[yOff + left] = maskColor;
            left--;
        }
        while ((left >= 0) && (maskImage[yOff + left] == startColor));
        // fill antialised if allowed
        if ((antialiased) && (left >= 0)) {
            int antialiasedColor = maskImage[yOff + left];
            antialiasedColor = (antialiasedColor >> 1) & 0x7F7F7F7F;
            antialiasedColor = antialiasedColor + ((fillColor2(left, y) >> 1) & 0x7F7F7F7F);
            if (antialiasedColor != startColor) {
                image[yOff + left] = antialiasedColor;
            }
        }
        left++;

        // fill right of (x, y) until border or edge of image
        int right = x;
        do {
            image[yOff + right] = fillColor2(right, y);
            maskImage[yOff + right] = maskColor;
            right++;
        }
        while ((right < width) && (maskImage[yOff + right] == startColor));
        // fill antialised if allowed
        if ((antialiased) && (right < width)) {
            int antialiasedColor = maskImage[yOff + right];
            antialiasedColor = (antialiasedColor >> 1) & 0x7F7F7F7F;
            antialiasedColor = antialiasedColor + ((fillColor2(right, y) >> 1) & 0x7F7F7F7F);
            if (antialiasedColor != startColor) {
                image[yOff + right] = antialiasedColor;
            }
        }
        right--;

        // add to stack
        if (index == 0) {
            LineInfo lineInfo = new LineInfo();
            lineInfo.setInfo(left, right, y);
            linearNRTodo.add(lineInfo);
        } else {
            index--;
            linearNRTodo.get(index).setInfo(left, right, y);
        }

        // return right position
        return right;
    }

    /**
     * Returns the fill color for a given x and y value.
     *
     * @param x The x position to return the color for.
     * @param y The y position to return the color for.
     * @return The color for the given position.
     */
    private int fillColor2(int x, int y) {
        x /= this.patternWidth;
        y /= this.patternHeight;
        if ((x + y) % 2 == 0) {
            return this.fillColor;
        } else {
            return this.patternColor;
        }
    }

    /**
     * Line info class for linear non-recursive fill.
     *
     * @author king
     * @since April 27, 2005
     */
    class LineInfo {

        /**
         * The left position.
         */
        int left;
        /**
         * The right position.
         */
        int right;
        /**
         * The y position.
         */
        int y;

        /**
         * Sets the line info.
         *
         * @param left  Previous left position.
         * @param right Previous right position.
         * @param y     Y position.
         */
        void setInfo(int left, int right, int y) {
            this.left = left;
            this.right = right;
            this.y = y;
        }
    }
}