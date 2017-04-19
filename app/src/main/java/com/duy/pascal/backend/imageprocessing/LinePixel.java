package com.duy.pascal.backend.imageprocessing;

/**
 * Line info class for linear non-recursive fill.
 *
 * @author king
 * @since April 27, 2005
 */
class LinePixel {

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