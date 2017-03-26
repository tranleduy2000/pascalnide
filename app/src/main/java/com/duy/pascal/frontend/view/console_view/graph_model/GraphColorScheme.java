package com.duy.pascal.frontend.view.console_view.graph_model;

import com.duy.pascal.frontend.view.console_view.ConsoleColorScheme;

/**
 * Created by Duy on 26-Mar-17.
 */

public class GraphColorScheme extends ConsoleColorScheme {

    public static final String TAG = GraphColorScheme.class.getSimpleName();

    public GraphColorScheme(int foreColor, int backColor) {
        super(foreColor, backColor);
    }

    public GraphColorScheme(int foreColor, int backColor, int cursorForeColor, int cursorBackColor) {
        super(foreColor, backColor, cursorForeColor, cursorBackColor);
    }

    public GraphColorScheme(int[] scheme) {
        super(scheme);
    }
}
