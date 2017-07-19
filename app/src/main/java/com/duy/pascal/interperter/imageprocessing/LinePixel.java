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

package com.duy.pascal.interperter.imageprocessing;

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