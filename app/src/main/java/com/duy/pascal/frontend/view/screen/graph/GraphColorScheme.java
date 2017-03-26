package com.duy.pascal.frontend.view.screen.graph;

import com.duy.pascal.frontend.view.screen.console.ConsoleColorScheme;

/**
 * Created by Duy on 26-Mar-17.
 */

public class GraphColorScheme extends ConsoleColorScheme {

    public static final String TAG = GraphColorScheme.class.getSimpleName();


    public GraphColorScheme(int foreColor, int backColor) {
        super(foreColor, backColor);
    }

    public GraphColorScheme(int foreColor, int backColor, int cursorColor) {
        super(foreColor, backColor, cursorColor);
    }
}
