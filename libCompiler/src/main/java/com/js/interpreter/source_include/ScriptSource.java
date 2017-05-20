package com.js.interpreter.source_include;

import java.io.Reader;

public interface ScriptSource {
    /**
     * List possible script contents by name.
     *
     * @return list of source names
     */
    String[] list();

    /**
     * Open a stream to a given source content.
     *
     * @param scriptname The name of the source
     * @return A reader attached to that source
     */
    Reader read(String scriptname);
}