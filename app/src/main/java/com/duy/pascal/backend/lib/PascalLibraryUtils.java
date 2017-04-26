/*
 *  Copyright 2017 Tran Le Duy
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

package com.duy.pascal.backend.lib;

import com.duy.pascal.backend.lib.android.AndroidBatteryLib;
import com.duy.pascal.backend.lib.android.AndroidClipboard;
import com.duy.pascal.backend.lib.android.AndroidNotifyLib;
import com.duy.pascal.backend.lib.android.AndroidSensorLib;
import com.duy.pascal.backend.lib.android.AndroidSpeechRecognitionLib;
import com.duy.pascal.backend.lib.android.AndroidTextToSpeechLib;
import com.duy.pascal.backend.lib.android.AndroidVibrateLib;
import com.duy.pascal.backend.lib.android.temp.AndroidBluetoothLib;
import com.duy.pascal.backend.lib.android.temp.AndroidMediaPlayerLib;
import com.duy.pascal.backend.lib.android.temp.AndroidSettingLib;
import com.duy.pascal.backend.lib.android.temp.AndroidToneGeneratorLib;
import com.duy.pascal.backend.lib.android.temp.AndroidUtilsLib;
import com.duy.pascal.backend.lib.android.temp.AndroidWifiLib;
import com.duy.pascal.backend.lib.android.utils.AndroidLibraryManager;
import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.duy.pascal.backend.lib.graph.GraphLib;
import com.duy.pascal.backend.lib.io.InOutListener;
import com.duy.pascal.backend.lib.math.MathLib;
import com.duy.pascal.frontend.activities.ExecHandler;
import com.duy.pascal.frontend.activities.RunnableActivity;
import com.google.common.collect.ListMultimap;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.MethodDeclaration;
import com.js.interpreter.ast.expressioncontext.ExpressionContextMixin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Duy on 08-Apr-17.
 */

public class PascalLibraryUtils {
    public static final Map<String, Class<? extends PascalLibrary>> mSupportLibraries = new HashMap<>();

    static {
        mSupportLibraries.put(CrtLib.NAME, CrtLib.class);
        mSupportLibraries.put(DosLib.NAME, DosLib.class);
        mSupportLibraries.put(MathLib.NAME, MathLib.class);
        mSupportLibraries.put("graph", GraphLib.class);
        mSupportLibraries.put("strutils", StrUtilsLibrary.class);
        mSupportLibraries.put("abattery", AndroidBatteryLib.class);
        mSupportLibraries.put("amedia", AndroidMediaPlayerLib.class);
        mSupportLibraries.put("asensor", AndroidSensorLib.class);
        mSupportLibraries.put("autils", AndroidUtilsLib.class);
        mSupportLibraries.put("atone", AndroidToneGeneratorLib.class);
        mSupportLibraries.put("aspeech", AndroidTextToSpeechLib.class);
        mSupportLibraries.put("awifi", AndroidWifiLib.class);
        mSupportLibraries.put("asetting", AndroidSettingLib.class);
        mSupportLibraries.put("abluetooth", AndroidBluetoothLib.class);
        mSupportLibraries.put(AndroidClipboard.NAME, AndroidClipboard.class);
        mSupportLibraries.put("anotify", AndroidNotifyLib.class);
        mSupportLibraries.put(AndroidVibrateLib.NAME, AndroidVibrateLib.class);
        mSupportLibraries.put(AndroidSpeechRecognitionLib.NAME, AndroidSpeechRecognitionLib.class);
    }

    /**
     * get method of class, call by java reflect
     *
     * @param classes  - list class
     * @param modifier - allow method modifier
     */
    public static void addMethodFromClasses(ArrayList<Class<?>> classes,
                                            int modifier,
                                            RunnableActivity handler,
                                            ExpressionContextMixin program) {

        AndroidLibraryManager facadeManager = new AndroidLibraryManager(AndroidLibraryUtils.getSdkLevel(), handler);

        for (Class<?> pascalPlugin : classes) {
            Constructor constructor;
            Object o = null;
            try {
                constructor = pascalPlugin.getConstructor(InOutListener.class);
                o = constructor.newInstance(handler);
            } catch (Exception ignored) {
            }
            if (o == null) {
                try {
                    constructor = pascalPlugin.getConstructor(ExecHandler.class);
                    o = constructor.newInstance(handler);
                } catch (Exception ignored) {
                }
            }
            if (o == null) {
                try {
                    constructor = pascalPlugin.getConstructor(AndroidLibraryManager.class);
                    o = constructor.newInstance(facadeManager);
                } catch (Exception ignored) {
                }
            }
            if (o == null) {
                try {
                    constructor = pascalPlugin.getConstructor();
                    o = constructor.newInstance();
                } catch (Exception ignored) {
                }
            }
            for (Method m : pascalPlugin.getDeclaredMethods()) {
                if (AndroidLibraryUtils.getSdkLevel() >= 18) {
                    if (m.isAnnotationPresent(PascalMethod.class)) {
                        System.out.println(m.getName());
                        MethodDeclaration methodDeclaration = new MethodDeclaration(o, m);
                        program.declareFunction(methodDeclaration);
                    }
                } else {
                    if (Modifier.isPublic(m.getModifiers())) {
                        System.out.println(m.getName());
                        MethodDeclaration methodDeclaration = new MethodDeclaration(o, m);
                        program.declareFunction(methodDeclaration);
                    }
                }
            }

        }
    }

    /**
     * load method from a class
     */
    public static void addMethodFromClass(Class<?> pascalPlugin,
                                          int modifier,
                                          RunnableActivity handler,
                                          ListMultimap<String, AbstractFunction> callableFunctions) {
        Object o = null;
        try {
            Constructor constructor = pascalPlugin.getConstructor(InOutListener.class);
            o = constructor.newInstance(handler);
        } catch (Exception ignored) {
        }
        if (o == null) {
            try {
                Constructor constructor;
                constructor = pascalPlugin.getConstructor(ExecHandler.class);
                o = constructor.newInstance(handler);
            } catch (Exception ignored) {
            }
        }
        if (o == null) {
            try {
                Constructor constructor;
                constructor = pascalPlugin.getConstructor();
                o = constructor.newInstance();
            } catch (Exception ignored) {
            }
        }
        for (Method m : pascalPlugin.getDeclaredMethods()) {
            if (Modifier.isPublic(m.getModifiers())) {
                MethodDeclaration tmp = new MethodDeclaration(o, m);
                callableFunctions.put(tmp.name().toLowerCase(), tmp);
            }
        }
    }

    /**
     * load all method of the list classes
     *
     * @param source       - current library
     * @param newLibraries - list name of libraries to import
     * @param handler-     - activity for handle output, input, graph, ...
     * @param program      - main program for declare function
     */
    public static void loadLibrary(ArrayList<String> source, ArrayList<String> newLibraries,
                                   RunnableActivity handler,
                                   ExpressionContextMixin program) {
        source.addAll(newLibraries);
        ArrayList<Class<?>> classes = new ArrayList<>();
        for (String name : newLibraries) {
            classes.add(mSupportLibraries.get(name));
        }
        PascalLibraryUtils.addMethodFromClasses(classes, Modifier.PUBLIC, handler, program);
    }

}
