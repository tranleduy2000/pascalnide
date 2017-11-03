package com.duy.pascal.interperter.source;

import android.support.annotation.Nullable;

import java.io.Reader;

public interface ScriptSource {
    /**
     * List possible script contents by name.
     *
     * @return list of source names
     */
    @Nullable
    String[] list();

    /**
     * Open a stream to a given source content.
     *
     * @param fileName The name of the source
     * @return A reader attached to that source
     */
    @Nullable
    Reader read(String fileName);

    Reader stream();

    String getName();
}