package com.duy.pascal.frontend.sample;

import java.util.ArrayList;

/**
 * Created by Duy on 08-Apr-17.
 */

class CodeCategory {
    private String title;
    private String description;
    private String imagePath;

    private ArrayList<CodeSampleEntry> codeSampleEntries = new ArrayList<>();

    public CodeCategory(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public CodeCategory(String title, String description, String imagePath) {
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;

    }

    public int getCodeSize() {
        return codeSampleEntries.size();
    }

    public void addCodeItem(CodeSampleEntry entry) {
        codeSampleEntries.add(entry);
    }

    public void removeCode(CodeSampleEntry entry) {
        codeSampleEntries.remove(entry);
    }

    public void removeCode(int position) {
        if (position > codeSampleEntries.size() - 1) return;
        codeSampleEntries.remove(position);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public CodeSampleEntry getCode(int childPosition) {
        return codeSampleEntries.get(childPosition);
    }
}
