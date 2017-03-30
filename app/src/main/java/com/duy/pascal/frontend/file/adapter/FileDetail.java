package com.duy.pascal.frontend.file.adapter;

import android.text.TextUtils;

public  class FileDetail {
        private final String name;
        private final String size;
        private final String dateModified;
        private final boolean isFolder;

        public FileDetail(String name, String size,
                          String dateModified) {
            this.name = name;
            this.size = size;
            this.dateModified = dateModified;
            isFolder = TextUtils.isEmpty(dateModified);
        }

        public String getDateModified() {
            return dateModified;
        }

        public String getSize() {
            return size;
        }

        public String getName() {
            return name;
        }

        public boolean isFolder() {
            return isFolder;
        }
    }
