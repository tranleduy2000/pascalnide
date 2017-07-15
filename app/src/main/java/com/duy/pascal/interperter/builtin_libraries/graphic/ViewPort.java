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

package com.duy.pascal.interperter.builtin_libraries.graphic;

import android.graphics.Rect;

/**
 * Created by Duy on 08-Apr-17.
 */

public class ViewPort {
    private Rect rect = new Rect();
    private boolean clip = false;

    public ViewPort(int i, int i1, int width, int height) {
        rect.set(i, i1, width, height);
    }

    /**
     * Set the current graphic viewport to the retangle define by then top-left (x1, y1) and then
     * bottom-right (x2, y2). If clip
     */
    public void setViewPort(int x1, int y1, int x2, int y2, boolean clip) {
        rect.set(x1, y1, x2, y2);
        this.clip = clip;
    }

    public boolean isClip() {
        return clip;
    }

    public void setClip(boolean clip) {
        this.clip = clip;
    }

    public void set(int i, int i1, int width, int height) {
        setViewPort(i, i1, width, height, clip);
    }
}
