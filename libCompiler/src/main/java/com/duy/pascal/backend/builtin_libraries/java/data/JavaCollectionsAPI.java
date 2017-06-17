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

package com.duy.pascal.backend.builtin_libraries.java.data;

import com.duy.pascal.backend.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.backend.builtin_libraries.PascalLibraryImpl;
import com.duy.pascal.backend.types.JavaClassBasedType;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by Duy on 17-Jun-17.
 */

public class JavaCollectionsAPI extends PascalLibraryImpl {
    public static final String NAME = "JavaCollections".toLowerCase();

    @Override
    public void declareConstants(ExpressionContextMixin parentContext) {
        super.declareConstants(parentContext);
    }

    @Override
    public void declareTypes(ExpressionContextMixin parentContext) {
        super.declareTypes(parentContext);
        parentContext.declareTypedef("JArrayList", new JavaClassBasedType(ArrayList.class));
        parentContext.declareTypedef("JArrayDeque", new JavaClassBasedType(ArrayDeque.class));
        parentContext.declareTypedef("JHashMap", new JavaClassBasedType(HashMap.class));
        parentContext.declareTypedef("JHashSet", new JavaClassBasedType(HashSet.class));
        parentContext.declareTypedef("JHashtable", new JavaClassBasedType(Hashtable.class));
        parentContext.declareTypedef("JLinkedHashMap", new JavaClassBasedType(LinkedHashMap.class));
        parentContext.declareTypedef("JLinkedHashSet", new JavaClassBasedType(LinkedHashSet.class));
        parentContext.declareTypedef("JLinkedList", new JavaClassBasedType(LinkedList.class));
        parentContext.declareTypedef("JPriorityQueue", new JavaClassBasedType(PriorityQueue.class));
        parentContext.declareTypedef("JStack", new JavaClassBasedType(Stack.class));
        parentContext.declareTypedef("JTreeMap", new JavaClassBasedType(TreeMap.class));
        parentContext.declareTypedef("JTreeSet", new JavaClassBasedType(TreeSet.class));
        parentContext.declareTypedef("JVector", new JavaClassBasedType(Vector.class));
        parentContext.declareTypedef("JWeakHashMap", new JavaClassBasedType(WeakHashMap.class));
        parentContext.declareTypedef("JBitSet", new JavaClassBasedType(BitSet.class));
        parentContext.declareTypedef("JArrayBlockingQueue", new JavaClassBasedType(ArrayBlockingQueue.class));
        parentContext.declareTypedef("JLinkedBlockingDeque", new JavaClassBasedType(LinkedBlockingDeque.class));
        parentContext.declareTypedef("JLinkedBlockingQueue", new JavaClassBasedType(LinkedBlockingQueue.class));
        parentContext.declareTypedef("JConcurrentHashMap", new JavaClassBasedType(ConcurrentHashMap.class));
        parentContext.declareTypedef("JConcurrentLinkedQueue", new JavaClassBasedType(ConcurrentLinkedQueue.class));
        parentContext.declareTypedef("JPriorityBlockingQueue", new JavaClassBasedType(PriorityBlockingQueue.class));
        parentContext.declareTypedef("JDelayQueue", new JavaClassBasedType(DelayQueue.class));
        parentContext.declareTypedef("JIdentityHashMap", new JavaClassBasedType(IdentityHashMap.class));
        parentContext.declareTypedef("JEnumeration", new JavaClassBasedType(Enumeration.class));
        parentContext.declareTypedef("JProperties", new JavaClassBasedType(Properties.class));
        parentContext.declareTypedef("JDictionary", new JavaClassBasedType(Dictionary.class));
        parentContext.declareTypedef("JSet", new JavaClassBasedType(Set.class));
        parentContext.declareTypedef("JIterator", new JavaClassBasedType(Iterator.class));
        parentContext.declareTypedef("JEntry", new JavaClassBasedType(Map.Entry.class));
    }

    @Override
    public String getName() {
        return "JavaCollections".toLowerCase();
    }
}
