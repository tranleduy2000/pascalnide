package com.duy.pascal.frontend.program_structure.viewholder;

import java.io.Serializable;
import java.util.ArrayList;

import static android.app.ApplicationErrorReport.TYPE_NONE;

public class StructureItem implements Serializable {

    public String text;
    public int type;
    public ArrayList<StructureItem> listNode = new ArrayList<>();

    public StructureItem(int type, String text) {
        this.type = type;
        this.text = text;
    }

    public StructureItem(String text) {
        this.type = TYPE_NONE;
        this.text = text;
    }

    public void addNode(StructureItem node) {
        listNode.add(node);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}