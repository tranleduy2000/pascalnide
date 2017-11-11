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

package com.duy.pascal.ui.autocomplete.completion;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.ui.autocomplete.completion.model.Description;
import com.duy.pascal.ui.autocomplete.completion.model.DescriptionImpl;
import com.duy.pascal.ui.autocomplete.completion.model.KeyWordDescription;
import com.duy.pascal.ui.autocomplete.completion.util.KeyWord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Duy on 11/11/2017.
 */

public class SuggestProvider {
    @NonNull
    public static ArrayList<Description> sort(ArrayList<Description> items) {
        //sort by type -> name
        Collections.sort(items, new Comparator<Description>() {
            @Override
            public int compare(Description o1, Description o2) {
                if (!o1.getKind().equals(o2.getKind())) {
                    return o1.getKind().compareTo(o2.getKind());
                } else {
                    return o1.getHeader().compareTo(o2.getHeader());
                }
            }
        });
        return items;
    }

    public static void completeSuggestType(String mIncomplete, ArrayList<Description> toAdd, ExpressionContextMixin exprContext) {
        for (String str : KeyWord.DATA_TYPE) {
            toAdd.add(new DescriptionImpl(DescriptionImpl.KIND_UNDEFINED, str));
        }
        toAdd.add(new DescriptionImpl(DescriptionImpl.KIND_UNDEFINED, "array"));
    }

    public static void completeAddToken(String mIncomplete, ArrayList<Description> toAdd,
                                        ExpressionContextMixin exprContext, String... token) {
        for (String str : token) {
            if (str.toLowerCase().startsWith(mIncomplete.toLowerCase())
                    && !str.equalsIgnoreCase(mIncomplete))
                toAdd.add(new DescriptionImpl(DescriptionImpl.KIND_UNDEFINED, str));
        }
    }

    public static void completeAddKeyWordToken(String mIncomplete, ArrayList<Description> toAdd,
                                               ExpressionContextMixin exprContext, String... token) {
        for (String str : token) {
            if (str.toLowerCase().startsWith(mIncomplete.toLowerCase())
                    && !str.equalsIgnoreCase(mIncomplete))
                toAdd.add(new KeyWordDescription(str, null));
        }
    }

    public static void completeUses(String mIncomplete, ArrayList<Description> toAdd, ExpressionContextMixin exprContext) {
        for (String str : KeyWord.BUILTIN_LIBS) {
            if (str.toLowerCase().startsWith(mIncomplete.toLowerCase())
                    && !str.equalsIgnoreCase(mIncomplete)) {
                toAdd.add(new KeyWordDescription(str, null));
            }
        }
    }
}
