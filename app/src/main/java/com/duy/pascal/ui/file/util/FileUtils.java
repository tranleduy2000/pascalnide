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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.ui.common.utils.IOUtils;
import com.duy.pascal.ui.file.ExplorerException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class FileUtils {
    private static Boolean mRootAccess = null;


    /**
     * Indicates whether file is considered to be "text".
     *
     * @return {@code true} if file is text, {@code false} if not.
     */
    public static boolean isTextFile(File f) {
        return !f.isDirectory() && isMimeText(f.getPath());
    }

    /**
     * Indicates whether requested file path is "text". This is done by
     * comparing file extension to a static list of extensions known to be text.
     * If the file has no file extension, it is also considered to be text.
     *
     * @param file File path
     * @return {@code true} if file is text, {@code false} if not.
     */
    private static boolean isMimeText(String file) {
        if (file == null)
            return false;
        if (!file.contains("."))
            return false;
        file = file.substring(file.lastIndexOf("/") + 1);
//        String ext = file.substring(file.lastIndexOf(".") + 1);
        return MimeTypes.getInstance().getMimeType(file).startsWith("text/");
    }

    public static void copyDirectory(final File srcDir, File destDir
            , final boolean moveFile) {
        if (srcDir == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (!srcDir.exists()) {
            throw new ExplorerException("Source '" + srcDir + "' does not exist");
        }
        if (!srcDir.isDirectory()) {
            throw new ExplorerException("Source '" + srcDir + "' exists but is not a directory");
        }
        if (srcDir.getAbsolutePath().equals(destDir.getAbsolutePath())) {
            throw new ExplorerException("Source '" + srcDir + "' and destination '" + destDir + "' are the same");
        }

        final File destDir2 = new File(destDir, srcDir.getName());

        // Cater for destination being directory within the source directory (see IO-141)
        if (destDir.getAbsolutePath().startsWith(srcDir.getAbsolutePath())) {
            File[] srcFiles = srcDir.listFiles();
            List<File> exclusionList = null;
            if (srcFiles != null && srcFiles.length > 0) {
                exclusionList = new ArrayList<>(srcFiles.length);
                for (File srcFile : srcFiles) {
                    File copiedFile = new File(destDir2, srcFile.getName());
                    exclusionList.add(copiedFile.getAbsoluteFile());
                }
            }
            doCopyDirectory(srcDir, destDir2, moveFile, exclusionList);
        } else {
            doCopyDirectory(srcDir, destDir2, moveFile, null);
        }
    }

    private static void doCopyDirectory(final File srcDir, final File destDir,
                                        final boolean moveFile, final List<File> exclusionList) {
        File[] srcFiles = srcDir.listFiles();
        if (srcFiles == null) {  // null if abstract pathname does not denote a directory, or if an I/O error occurs
            throw new ExplorerException("Failed to list contents of " + srcDir);
        }
        if (destDir.exists()) {
            if (!destDir.isDirectory()) {
                throw new ExplorerException("Destination '" + destDir + "' exists but is not a directory");
            }
            doCopyDirectory(srcDir, destDir, srcFiles, moveFile, exclusionList);
        } else {
            boolean result = destDir.mkdirs();
            if (!result && !destDir.isDirectory()) {
                throw new ExplorerException("Destination '" + destDir + "' directory cannot be created");
            }
            doCopyDirectory(srcDir, destDir, srcFiles, moveFile, exclusionList);
        }
    }

    private static void doCopyDirectory(File srcDir, final File destDir, File[] srcFiles,
                                        final boolean moveFile, final List<File> exclusionList) throws ExplorerException {
        if (!destDir.canWrite()) {
            throw new ExplorerException("Destination '" + destDir + "' cannot be written to");
        }
        for (final File srcFile : srcFiles) {
            File dstFile = new File(destDir, srcFile.getName());
            if (exclusionList == null || !exclusionList.contains(srcFile.getAbsoluteFile())) {
                if (srcFile.isDirectory()) {
                    doCopyDirectory(srcFile, dstFile, moveFile, exclusionList);
                } else {
                    copyFile(srcFile, dstFile, moveFile);
                }
            }
        }
    }

    public static void copyFile(final File srcFile, final File dstFile, boolean moveFile) {
        if (moveFile) {
            boolean result = srcFile.renameTo(dstFile);
            if (!result) {
                throw new ExplorerException("Source '" + srcFile + "' move to destination '" + dstFile + "' fail");
            }
        } else {
            boolean result = IOUtils.copyFile(srcFile, dstFile);
            if (!result) {
                throw new ExplorerException("Source '" + srcFile + "' copy to destination '" + dstFile + "' fail");
            }
        }
    }

    public static boolean canEdit(@NonNull File file) {
        String extension = MimeTypes.getExtension(file.getAbsolutePath());
        return file.exists() && file.isFile() &&
                (extension.equalsIgnoreCase(".txt") ||
                        extension.equalsIgnoreCase(".pas") ||
                        extension.endsWith(".out") ||
                        extension.equalsIgnoreCase(".inp"));
    }

    /**
     * Gets the extension of a file name, like ".png" or ".jpg".
     *
     * @return Extension don't including the dot("."); "" if there is no extension;
     * null if uri was null.
     */
    public static String getExtension(@Nullable File file) {
        if (file == null) {
            return null;
        }
        String name = file.getName();
        int dot = name.lastIndexOf(".");
        if (dot >= 0) {
            return name.substring(dot + 1);
        } else {
            // No extension.
            return "";
        }
    }

    /**
     * delete fileOrDirectory and all child file
     *
     * @param fileOrDirectory - input file
     * @return true of delete success, otherwise return false
     */
    public static boolean deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        return fileOrDirectory.delete();
    }
}
