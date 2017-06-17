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

package com.duy.pascal.backend.builtin_libraries;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.duy.pascal.backend.ast.ConstantDefinition;
import com.duy.pascal.backend.ast.MethodDeclaration;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.backend.system_function.builtin.AbstractMethodDeclaration;
import com.duy.pascal.backend.system_function.builtin.AddressFunction;
import com.duy.pascal.backend.system_function.builtin.CastObjectFunction;
import com.duy.pascal.backend.system_function.builtin.CopyFunction;
import com.duy.pascal.backend.system_function.builtin.ExitFunction;
import com.duy.pascal.backend.system_function.builtin.ExitNoneFunction;
import com.duy.pascal.backend.system_function.builtin.HighFunction;
import com.duy.pascal.backend.system_function.builtin.LengthFunction;
import com.duy.pascal.backend.system_function.builtin.LowFunction;
import com.duy.pascal.backend.system_function.builtin.NewFunction;
import com.duy.pascal.backend.system_function.builtin.NewInstanceObject;
import com.duy.pascal.backend.system_function.builtin.NewInstanceParamsObject;
import com.duy.pascal.backend.system_function.builtin.SetLengthFunction;
import com.duy.pascal.backend.system_function.builtin.SizeOfArrayFunction;
import com.duy.pascal.backend.system_function.builtin.SizeOfObjectFunction;
import com.duy.pascal.backend.system_function.io.ReadFileFunction;
import com.duy.pascal.backend.system_function.io.ReadFunction;
import com.duy.pascal.backend.system_function.io.ReadLineFunction;
import com.duy.pascal.backend.system_function.io.ReadlnFileFunction;
import com.duy.pascal.backend.system_function.io.WriteFileFunction;
import com.duy.pascal.backend.system_function.io.WriteFunction;
import com.duy.pascal.backend.system_function.io.WriteLineFunction;
import com.duy.pascal.backend.system_function.io.WritelnFileFunction;
import com.duy.pascal.backend.ast.runtime_value.value.NullValue;
import com.duy.pascal.backend.builtin_libraries.android.AndroidLibraryManager;
import com.duy.pascal.backend.builtin_libraries.android.barcode.ZXingAPI;
import com.duy.pascal.backend.builtin_libraries.android.connection.bluetooth.AndroidBluetoothLib;
import com.duy.pascal.backend.builtin_libraries.android.connection.socketio.SocketIOLib;
import com.duy.pascal.backend.builtin_libraries.android.connection.web.HtmlLib;
import com.duy.pascal.backend.builtin_libraries.android.connection.wifi.AndroidWifiLib;
import com.duy.pascal.backend.builtin_libraries.android.gps.AndroidLocationLib;
import com.duy.pascal.backend.builtin_libraries.android.hardware.AndroidSensorLib;
import com.duy.pascal.backend.builtin_libraries.android.hardware.AndroidVibrateLib;
import com.duy.pascal.backend.builtin_libraries.android.media.AndroidMediaPlayerLib;
import com.duy.pascal.backend.builtin_libraries.android.media.AndroidToneGeneratorLib;
import com.duy.pascal.backend.builtin_libraries.android.temp.AndroidSettingLib;
import com.duy.pascal.backend.builtin_libraries.android.temp.AndroidUtilsLib;
import com.duy.pascal.backend.builtin_libraries.android.utils.AndroidBatteryLib;
import com.duy.pascal.backend.builtin_libraries.android.utils.AndroidClipboardLib;
import com.duy.pascal.backend.builtin_libraries.android.view.AndroidDialogLib;
import com.duy.pascal.backend.builtin_libraries.android.view.AndroidNotifyLib;
import com.duy.pascal.backend.builtin_libraries.android.voice.AndroidSpeechRecognitionLib;
import com.duy.pascal.backend.builtin_libraries.android.voice.AndroidTextToSpeechLib;
import com.duy.pascal.backend.builtin_libraries.annotations.PascalMethod;
import com.duy.pascal.backend.builtin_libraries.crt.CrtLib;
import com.duy.pascal.backend.builtin_libraries.crt.WinCrt;
import com.duy.pascal.backend.builtin_libraries.graph.GraphLib;
import com.duy.pascal.backend.builtin_libraries.io.InOutListener;
import com.duy.pascal.backend.builtin_libraries.java.data.JavaCollectionsAPI;
import com.duy.pascal.backend.builtin_libraries.math.MathLib;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.PermissionDeniedException;
import com.duy.pascal.backend.parse_exception.io.LibraryNotFoundException;
import com.duy.pascal.backend.types.BasicType;
import com.duy.pascal.backend.types.JavaClassBasedType;
import com.duy.pascal.backend.types.PointerType;
import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.activities.ConsoleHandler;
import com.duy.pascal.frontend.activities.IRunnablePascal;
import com.duy.pascal.frontend.editor.editor_view.adapters.InfoItem;
import com.duy.pascal.frontend.structure.viewholder.StructureType;

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
        MAP_LIBRARIES.put(AndroidLocationLib.NAME, AndroidLocationLib.class);
        MAP_LIBRARIES.put(JavaCollectionsAPI.NAME, JavaCollectionsAPI.class);

        //socket library
        MAP_LIBRARIES.put(SocketIOLib.NAME, SocketIOLib.class);

        MAP_LIBRARIES.put(AndroidDialogLib.NAME, AndroidDialogLib.class);
    }

    @NonNull
    private ExpressionContextMixin program;
    @NonNull
    private IRunnablePascal handler;
    private AndroidLibraryManager facadeManager;

    public PascalLibraryManager(@NonNull ExpressionContextMixin program,
                                @NonNull IRunnablePascal handler) {
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
     * load method from a class
     */

    public void addMethodFromClass(Class<? extends PascalLibrary> t, LineInfo lineNumber) throws PermissionDeniedException, LibraryNotFoundException {
        Object parent = null;
        Constructor constructor;
        try {
            constructor = t.getConstructor(InOutListener.class);
            parent = constructor.newInstance(handler);
        } catch (Exception ignored) {
        }
        if (parent == null) {
            try {
                constructor = t.getConstructor(ConsoleHandler.class);
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
            addMethodFromLibrary(parent, lineNumber);
        } else {
            throw new LibraryNotFoundException(lineNumber, t.getName());
        }

    }

    /**
     * load system method
     */
    public void loadSystemLibrary() throws PermissionDeniedException, LibraryNotFoundException {
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
        program.declareFunction(new AbstractMethodDeclaration(new CopyFunction()));

        program.declareFunction(new AbstractMethodDeclaration(new CastObjectFunction()));
        program.declareFunction(new AbstractMethodDeclaration(new NewInstanceParamsObject()));
        program.declareFunction(new AbstractMethodDeclaration(new NewInstanceObject()));
        program.declareFunction(new AbstractMethodDeclaration(new AddressFunction()));

        //io region
        program.declareFunction(new AbstractMethodDeclaration(new ReadFileFunction()));
        program.declareFunction(new AbstractMethodDeclaration(new ReadlnFileFunction()));
        program.declareFunction(new AbstractMethodDeclaration(new ReadLineFunction()));
        program.declareFunction(new AbstractMethodDeclaration(new ReadFunction()));

        program.declareFunction(new AbstractMethodDeclaration(new WriteFileFunction()));
        program.declareFunction(new AbstractMethodDeclaration(new WritelnFileFunction()));
        program.declareFunction(new AbstractMethodDeclaration(new WriteLineFunction()));
        program.declareFunction(new AbstractMethodDeclaration(new WriteFunction()));
        //end region

        program.declareConst(new ConstantDefinition("null", new JavaClassBasedType(null), NullValue.get(), null));
        program.declareConst(new ConstantDefinition("nil", new PointerType(null), NullValue.get(), null));

        program.declareConst(new ConstantDefinition("maxint", BasicType.Integer, Integer.MAX_VALUE, null));
        program.declareConst(new ConstantDefinition("maxlongint", BasicType.Long, Long.MAX_VALUE, null));

        program.declareConst(new ConstantDefinition("pi", BasicType.Double, Math.PI, null));

        addMethodFromClass(SystemLibrary.class, new LineInfo(-1, "system"));
    }

    public void addMethodFromLibrary(@NonNull Object o, LineInfo line) throws PermissionDeniedException {
        if (o instanceof IAndroidLibrary) {
            String[] permissions = ((IAndroidLibrary) o).needPermission();
            for (String permission : permissions) {
                if (DLog.ANDROID) {
                    int i = ActivityCompat.checkSelfPermission(handler.getApplicationContext(), permission);
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        throw new PermissionDeniedException(((IAndroidLibrary) o).getName(), permission, line);
                    }
                }
            }

        }

        ((PascalLibrary) o).declareConstants(program);
        ((PascalLibrary) o).declareFunctions(program);
        ((PascalLibrary) o).declareTypes(program);
        ((PascalLibrary) o).declareVariables(program);
        for (Method method : o.getClass().getDeclaredMethods()) {
            if (AndroidLibraryUtils.getSdkVersion() >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                if (method.isAnnotationPresent(PascalMethod.class)) {
                    PascalMethod annotation = method.getAnnotation(PascalMethod.class);
                    String description = annotation.description();
                    MethodDeclaration methodDeclaration = new MethodDeclaration(o, method, description);
                    program.declareFunction(methodDeclaration);
                }
            } else {
                if (Modifier.isPublic(method.getModifiers())) {
                    MethodDeclaration methodDeclaration = new MethodDeclaration(o, method);
                    program.declareFunction(methodDeclaration);
                }
            }
        }
    }

}
