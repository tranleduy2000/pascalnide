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

package com.duy.pascal.interperter.libraries.java.data;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.libraries.PascalLibrary;
import com.duy.pascal.interperter.declaration.lang.types.JavaClassBasedType;

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
@SuppressWarnings("unused")
public class JavaCollectionsAPI extends PascalLibrary {
    public static final String NAME = "JavaCollections".toLowerCase();

    public JavaCollectionsAPI() {

    }

    @Override
    public void declareConstants(ExpressionContextMixin context) {
        super.declareConstants(context);
    }

    @Override
    public void declareTypes(ExpressionContextMixin context) {
        super.declareTypes(context);
        context.declareTypedef("JArrayList", new JavaClassBasedType(ArrayList.class));
        context.declareTypedef("JArrayDeque", new JavaClassBasedType(ArrayDeque.class));
        context.declareTypedef("JHashMap", new JavaClassBasedType(HashMap.class));
        context.declareTypedef("JHashSet", new JavaClassBasedType(HashSet.class));
        context.declareTypedef("JHashtable", new JavaClassBasedType(Hashtable.class));
        context.declareTypedef("JLinkedHashMap", new JavaClassBasedType(LinkedHashMap.class));
        context.declareTypedef("JLinkedHashSet", new JavaClassBasedType(LinkedHashSet.class));
        context.declareTypedef("JLinkedList", new JavaClassBasedType(LinkedList.class));
        context.declareTypedef("JPriorityQueue", new JavaClassBasedType(PriorityQueue.class));
        context.declareTypedef("JStack", new JavaClassBasedType(Stack.class));
        context.declareTypedef("JTreeMap", new JavaClassBasedType(TreeMap.class));
        context.declareTypedef("JTreeSet", new JavaClassBasedType(TreeSet.class));
        context.declareTypedef("JVector", new JavaClassBasedType(Vector.class));
        context.declareTypedef("JWeakHashMap", new JavaClassBasedType(WeakHashMap.class));
        context.declareTypedef("JBitSet", new JavaClassBasedType(BitSet.class));
        context.declareTypedef("JArrayBlockingQueue", new JavaClassBasedType(ArrayBlockingQueue.class));
        context.declareTypedef("JLinkedBlockingDeque", new JavaClassBasedType(LinkedBlockingDeque.class));
        context.declareTypedef("JLinkedBlockingQueue", new JavaClassBasedType(LinkedBlockingQueue.class));
        context.declareTypedef("JConcurrentHashMap", new JavaClassBasedType(ConcurrentHashMap.class));
        context.declareTypedef("JConcurrentLinkedQueue", new JavaClassBasedType(ConcurrentLinkedQueue.class));
        context.declareTypedef("JPriorityBlockingQueue", new JavaClassBasedType(PriorityBlockingQueue.class));
        context.declareTypedef("JDelayQueue", new JavaClassBasedType(DelayQueue.class));
        context.declareTypedef("JIdentityHashMap", new JavaClassBasedType(IdentityHashMap.class));
        context.declareTypedef("JEnumeration", new JavaClassBasedType(Enumeration.class));
        context.declareTypedef("JProperties", new JavaClassBasedType(Properties.class));
        context.declareTypedef("JDictionary", new JavaClassBasedType(Dictionary.class));
        context.declareTypedef("JSet", new JavaClassBasedType(Set.class));
        context.declareTypedef("JIterator", new JavaClassBasedType(Iterator.class));
        context.declareTypedef("JEntry", new JavaClassBasedType(Map.Entry.class));
        context.declareTypedef("JEntrySet", new JavaClassBasedType(Set.class));
    }

    @Override
    public String getName() {
        return "JavaCollections".toLowerCase();
    }
}
