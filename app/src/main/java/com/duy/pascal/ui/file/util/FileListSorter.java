/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.ui.file.util;

import android.support.annotation.IntDef;

import java.io.File;
import java.util.Comparator;

public class FileListSorter implements Comparator<File> {
    private boolean dirsOnTop = true;
    private boolean asc = true;
    int sort = 0;

    public final static int SORT_NAME = 0;
    public final static int SORT_DATE = 1;
    public final static int SORT_SIZE = 2;
    public final static int SORT_TYPE = 3;
    @IntDef({SORT_NAME, SORT_DATE, SORT_SIZE, SORT_TYPE})
    public @interface SortType {}

    public FileListSorter() {
        this(true, SORT_NAME, true);
    }
    /**
     *
     * @param dirsOnTop 1 dir on top, 0 dir on bottom
     * @param sort 0 name, 1 time 2 size 3 type
     * @param asc -1 asc, 1 desc
     */
    public FileListSorter(boolean dirsOnTop, @SortType int sort, boolean asc) {
        this.dirsOnTop = dirsOnTop;
        this.asc = asc;
        this.sort = sort;
    }

    @Override
    public int compare(File file1, File file2) {
        if (file1.isDirectory() && !file2.isDirectory()) {
            return dirsOnTop ? -1 : 1;
        } else if (file2.isDirectory() && !file1.isDirectory()) {
            return dirsOnTop ? 1 : -1;
        }

        int res = 0;
        if (sort == SORT_NAME) {
            res = file1.getName().compareToIgnoreCase(file2.getName());
        } else if (sort == SORT_DATE) {
            res = compare(file1.lastModified(), file2.lastModified());
        } else if (sort == SORT_SIZE) {
            if (file1.isFile() && file2.isFile()) {
                res = compare(file1.length(), file2.length());
            } else {
                res = file1.getName().compareToIgnoreCase(file2.getName());
            }
        } else if (sort == SORT_TYPE) {
            if (file1.isFile() && file2.isFile()) {
                final String ext_a = getExtension(file1.getName());
                final String ext_b = getExtension(file2.getName());

                res = ext_a.compareTo(ext_b);
            } else {
                res = file1.getName().compareToIgnoreCase(file2.getName());
            }
        }

        if(res == 0)
            return 0;

        return asc && res < 0 ? -1 : 1;

    }

    public static int compare(long lhs, long rhs) {
        return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
    }

    static String getExtension(String a) {
        return a.substring(a.lastIndexOf(".") + 1).toLowerCase();
    }

}
