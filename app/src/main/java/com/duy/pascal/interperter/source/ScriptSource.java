package com.duy.pascal.interperter.source;

import android.support.annotation.Nullable;

import com.duy.pascal.interperter.tokens.Token;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;

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

    String getContent();

    LinkedList<Token> toTokens() throws IOException;
}