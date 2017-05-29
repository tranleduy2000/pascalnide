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

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.function_declaretion.MethodDeclaration;
import com.duy.pascal.backend.function_declaretion.builtin.AbstractMethodDeclaration;
import com.duy.pascal.backend.function_declaretion.builtin.AddressFunction;
import com.duy.pascal.backend.function_declaretion.builtin.CastObjectFunction;
import com.duy.pascal.backend.function_declaretion.builtin.ExitFunction;
import com.duy.pascal.backend.function_declaretion.builtin.ExitNoneFunction;
import com.duy.pascal.backend.function_declaretion.builtin.HighFunction;
import com.duy.pascal.backend.function_declaretion.builtin.LengthFunction;
import com.duy.pascal.backend.function_declaretion.builtin.LowFunction;
import com.duy.pascal.backend.function_declaretion.builtin.NewFunction;
import com.duy.pascal.backend.function_declaretion.builtin.NewInstanceObject;
import com.duy.pascal.backend.function_declaretion.builtin.NewInstanceParamsObject;
import com.duy.pascal.backend.function_declaretion.builtin.SetLengthFunction;
import com.duy.pascal.backend.function_declaretion.builtin.SizeOfArrayFunction;
import com.duy.pascal.backend.function_declaretion.builtin.SizeOfObjectFunction;
import com.duy.pascal.backend.lib.android.AndroidLibraryManager;
import com.duy.pascal.backend.lib.android.barcode.ZXingAPI;
import com.duy.pascal.backend.lib.android.connection.bluetooth.AndroidBluetoothLib;
import com.duy.pascal.backend.lib.android.connection.socketio.SocketIOLib;
import com.duy.pascal.backend.lib.android.connection.web.HtmlLib;
import com.duy.pascal.backend.lib.android.connection.wifi.AndroidWifiLib;
import com.duy.pascal.backend.lib.android.hardware.AndroidSensorLib;
import com.duy.pascal.backend.lib.android.hardware.AndroidVibrateLib;
import com.duy.pascal.backend.lib.android.media.AndroidMediaPlayerLib;
import com.duy.pascal.backend.lib.android.media.AndroidToneGeneratorLib;
import com.duy.pascal.backend.lib.android.temp.AndroidSettingLib;
import com.duy.pascal.backend.lib.android.temp.AndroidUtilsLib;
import com.duy.pascal.backend.lib.android.utils.AndroidBatteryLib;
import com.duy.pascal.backend.lib.android.utils.AndroidClipboardLib;
import com.duy.pascal.backend.lib.android.view.AndroidDialogLib;
import com.duy.pascal.backend.lib.android.view.AndroidNotifyLib;
import com.duy.pascal.backend.lib.android.voice.AndroidSpeechRecognitionLib;
import com.duy.pascal.backend.lib.android.voice.AndroidTextToSpeechLib;
import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.duy.pascal.backend.lib.crt.CrtLib;
import com.duy.pascal.backend.lib.crt.WinCrt;
import com.duy.pascal.backend.lib.file.FileLib;
import com.duy.pascal.backend.lib.graph.GraphLib;
import com.duy.pascal.backend.lib.io.IOLib;
import com.duy.pascal.backend.lib.io.InOutListener;
import com.duy.pascal.backend.lib.math.MathLib;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.JavaClassBasedType;
import com.duy.pascal.backend.pascaltypes.PointerType;
import com.duy.pascal.frontend.activities.ExecHandler;
import com.duy.pascal.frontend.activities.IRunnablePascal;
import com.duy.pascal.frontend.code_editor.editor_view.adapters.InfoItem;
import com.duy.pascal.frontend.structure.viewholder.StructureType;
import com.js.interpreter.ConstantDefinition;
import com.js.interpreter.expressioncontext.ExpressionContextMixin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Duy on 08-Apr-17.
 */
public class PascalLibraryManager {
    public static final Map<String, Class<? extends PascalLibrary>> MAP_LIBRARIES = new Hashtable<>();

    static {
        MAP_LIBRARIES.put(CrtLib.NAME, CrtLib.class);
        MAP_LIBRARIES.put(WinCrt.NAME, WinCrt.class);
        MAP_LIBRARIES.put(DosLib.NAME, DosLib.class);
        MAP_LIBRARIES.put(MathLib.NAME, MathLib.class);
        MAP_LIBRARIES.put(GraphLib.NAME, GraphLib.class);
        MAP_LIBRARIES.put(StrUtilsLibrary.NAME, StrUtilsLibrary.class);
        MAP_LIBRARIES.put(SysUtilsLibrary.NAME, SysUtilsLibrary.class);

        MAP_LIBRARIES.put(AndroidMediaPlayerLib.NAME, AndroidMediaPlayerLib.class);
        MAP_LIBRARIES.put(AndroidUtilsLib.NAME, AndroidUtilsLib.class);
        MAP_LIBRARIES.put(AndroidToneGeneratorLib.NAME, AndroidToneGeneratorLib.class);
        MAP_LIBRARIES.put(AndroidWifiLib.NAME, AndroidWifiLib.class);
        MAP_LIBRARIES.put(AndroidSettingLib.NAME, AndroidSettingLib.class);
        MAP_LIBRARIES.put(AndroidBluetoothLib.NAME, AndroidBluetoothLib.class);

        MAP_LIBRARIES.put(AndroidBatteryLib.NAME, AndroidBatteryLib.class);
        MAP_LIBRARIES.put(AndroidTextToSpeechLib.NAME, AndroidTextToSpeechLib.class);
        MAP_LIBRARIES.put(AndroidSensorLib.NAME, AndroidSensorLib.class);
        MAP_LIBRARIES.put(AndroidClipboardLib.NAME, AndroidClipboardLib.class);
        MAP_LIBRARIES.put(AndroidNotifyLib.NAME, AndroidNotifyLib.class);
        MAP_LIBRARIES.put(AndroidVibrateLib.NAME, AndroidVibrateLib.class);
        MAP_LIBRARIES.put(AndroidSpeechRecognitionLib.NAME, AndroidSpeechRecognitionLib.class);
        MAP_LIBRARIES.put(ZXingAPI.NAME, ZXingAPI.class);
        MAP_LIBRARIES.put(AndroidMediaPlayerLib.NAME, AndroidMediaPlayerLib.class);
        MAP_LIBRARIES.put(HtmlLib.NAME, HtmlLib.class);

        //socket library
        MAP_LIBRARIES.put(SocketIOLib.NAME, SocketIOLib.class);

        MAP_LIBRARIES.put(AndroidDialogLib.NAME, AndroidDialogLib.class);
    }

    @NonNull
    private ExpressionContextMixin program;
    @Nullable
    private IRunnablePascal handler;
    private AndroidLibraryManager facadeManager;

    public PascalLibraryManager(@NonNull ExpressionContextMixin program,
                                @Nullable IRunnablePascal handler) {
        this.program = program;
        this.handler = handler;
        facadeManager = new AndroidLibraryManager(AndroidLibraryUtils.getSdkVersion(), handler);
    }

    public static ArrayList<InfoItem> getAllMethodDescription(Class<?>... classes) {
        ArrayList<InfoItem> suggestItems = new ArrayList<>();
        for (Class<?> aClass : classes) {
            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                if (AndroidLibraryUtils.getSdkVersion() >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (method.isAnnotationPresent(PascalMethod.class)) {
                        PascalMethod annotation = method.getAnnotation(PascalMethod.class);
                        String description = annotation.description();
                        suggestItems.add(new InfoItem(StructureType.TYPE_FUNCTION, method.getName(), description));
                    }
                } else {
                    if (Modifier.isPublic(method.getModifiers())) {
                        suggestItems.add(new InfoItem(StructureType.TYPE_FUNCTION, method.getName()));
                    }
                }
            }
        }
        return suggestItems;
    }

    /**
     * indexOf method of class, call by java reflect
     *
     * @param classes  - list class
     * @param modifier - allow method modifier
     */
    public void addMethodFromClasses(ArrayList<Class<? extends PascalLibrary>> classes,
                                     int modifier) {

        for (Class<? extends PascalLibrary> t : classes) {
            addMethodFromClass(t);
        }
    }

    /**
     * load method from a class
     */

    public void addMethodFromClass(Class<? extends PascalLibrary> t) {
        Object parent = null;
        Constructor constructor;
        try {
            constructor = t.getConstructor(InOutListener.class);
            parent = constructor.newInstance(handler);
        } catch (Exception ignored) {
        }
        if (parent == null) {
            try {
                constructor = t.getConstructor(ExecHandler.class);
                parent = constructor.newInstance(handler);
            } catch (Exception ignored) {
            }
        }
        if (parent == null) {
            try {
                constructor = t.getConstructor(AndroidLibraryManager.class);
                parent = constructor.newInstance(facadeManager);
            } catch (Exception ignored) {
            }
        }
        if (parent == null) {
            try {
                constructor = t.getConstructor();
                parent = constructor.newInstance();
            } catch (Exception ignored) {
            }
        }

        if (parent != null) {
            ((PascalLibrary) parent).declareConstants(program);
            ((PascalLibrary) parent).declareFunctions(program);
            ((PascalLibrary) parent).declareTypes(program);
            ((PascalLibrary) parent).declareVariables(program);
            for (Method method : t.getDeclaredMethods()) {
                if (AndroidLibraryUtils.getSdkVersion() >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (method.isAnnotationPresent(PascalMethod.class)) {
                        PascalMethod annotation = method.getAnnotation(PascalMethod.class);
                        String description = annotation.description();
                        MethodDeclaration methodDeclaration = new MethodDeclaration(parent, method, description);
                        program.declareFunction(methodDeclaration);
                    }
                } else {
                    if (Modifier.isPublic(method.getModifiers())) {
                        MethodDeclaration methodDeclaration = new MethodDeclaration(parent, method);
                        program.declareFunction(methodDeclaration);
                    }
                }
            }
        }

    }

    /**
     * load system method
     */
    public void loadSystemLibrary() {
        //load builtin function
        program.declareFunction(new AbstractMethodDeclaration(new SetLengthFunction()));
        program.declareFunction(new AbstractMethodDeclaration(new LengthFunction()));
        program.declareFunction(new AbstractMethodDeclaration(new SizeOfObjectFunction()));
        program.declareFunction(new AbstractMethodDeclaration(new SizeOfArrayFunction()));
        program.declareFunction(new AbstractMethodDeclaration(new ExitFunction()));
        program.declareFunction(new AbstractMethodDeclaration(new ExitNoneFunction()));
        program.declareFunction(new AbstractMethodDeclaration(new HighFunction()));
        program.declareFunction(new AbstractMethodDeclaration(new LowFunction()));
        program.declareFunction(new AbstractMethodDeclaration(new NewFunction()));

        program.declareFunction(new AbstractMethodDeclaration(new CastObjectFunction()));
        program.declareFunction(new AbstractMethodDeclaration(new NewInstanceParamsObject()));
        program.declareFunction(new AbstractMethodDeclaration(new NewInstanceObject()));
        program.declareFunction(new AbstractMethodDeclaration(new AddressFunction()));

        program.declareConst(new ConstantDefinition("null", new JavaClassBasedType(null), null, null));
        program.declareConst(new ConstantDefinition("nil", new PointerType(null), null, null));

        program.declareConst(new ConstantDefinition("maxint", BasicType.Integer, Integer.MAX_VALUE, null));
        program.declareConst(new ConstantDefinition("maxlongint", BasicType.Long, Long.MAX_VALUE, null));

        program.declareConst(new ConstantDefinition("pi", BasicType.Long, Long.MAX_VALUE, null));

        //Important: load file library before io lib. Because  method readln(file, ...)
        //in {@link FileLib} will be override method readln(object...) in {@link IOLib}

        addMethodFromClass(FileLib.class);
        addMethodFromClass(IOLib.class);
        addMethodFromClass(SystemLib.class);
    }

}
