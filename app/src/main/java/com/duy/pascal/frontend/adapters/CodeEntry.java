package com.duy.pascal.frontend.adapters;

/**
 * Created by Duy on 04-Mar-17.
 */

class CodeEntry {
    private String name;
    private String content;

    public CodeEntry(String name, String content) {
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
