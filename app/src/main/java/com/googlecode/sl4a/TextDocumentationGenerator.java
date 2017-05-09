/*
 * Copyright (C) 2009 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.googlecode.sl4a;

import com.duy.pascal.backend.lib.AndroidLibraryUtils;
import com.duy.pascal.backend.lib.PascalLibrary;
import com.googlecode.sl4a.rpc.MethodDescriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TextDocumentationGenerator {

    public static void main(String args[]) {
        List<MethodDescriptor> descriptors = AndroidLibraryUtils.collectMethodDescriptors();
        List<Class<? extends PascalLibrary>> classes =
                new ArrayList<>(AndroidLibraryUtils.getFacadeClasses());

        sortMethodDescriptors(descriptors);
        sortClasses(classes);

        HashMap<Class<? extends PascalLibrary>, Set<MethodDescriptor>> map =
                new HashMap<>();

        for (MethodDescriptor descriptor : descriptors) {
            Class<? extends PascalLibrary> clazz = descriptor.getDeclaringClass();
            if (!map.containsKey(clazz)) {
                map.put(clazz, new HashSet<MethodDescriptor>());
            }
            map.get(clazz).add(descriptor);
        }

        for (Class<? extends PascalLibrary> clazz : classes) {
            List<MethodDescriptor> list = new ArrayList<>(map.get(clazz));
            sortMethodDescriptors(list);
            for (MethodDescriptor descriptor : list) {
                System.out.println(String.format("  * [#%1$s %1$s]", descriptor.getName()));
            }
        }
    }


    private static void sortMethodDescriptors(List<MethodDescriptor> list) {
        Collections.sort(list, new Comparator<MethodDescriptor>() {
            @Override
            public int compare(MethodDescriptor md1, MethodDescriptor md2) {
                return md1.getName().compareTo(md2.getName());
            }
        });
    }

    private static void sortClasses(List<Class<? extends PascalLibrary>> list) {
        Collections.sort(list, new Comparator<Class<? extends PascalLibrary>>() {
            @Override
            public int compare(Class<? extends PascalLibrary> clazz1, Class<? extends PascalLibrary> clazz2) {
                return clazz1.getSimpleName().compareTo(clazz2.getSimpleName());
            }
        });
    }

}
