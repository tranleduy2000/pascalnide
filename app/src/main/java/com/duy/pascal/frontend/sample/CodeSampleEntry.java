package com.duy.pascal.frontend.sample;

/**
 * Created by Duy on 08-Apr-17.
 */

public class CodeSampleEntry {
    /**
     * name of file code
     */
    private String name;

    /**
     * code
     */
    private String content;

    public CodeSampleEntry(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
