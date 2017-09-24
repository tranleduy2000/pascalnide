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

package com.duy.pascal.interperter.builtin_libraries;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.autocomplete.completion.model.DescriptionImpl;
import com.duy.pascal.frontend.runnable.ConsoleHandler;
import com.duy.pascal.frontend.runnable.ProgramHandler;
import com.duy.pascal.frontend.structure.viewholder.StructureType;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.ast.runtime_value.value.NullValue;
import com.duy.pascal.interperter.builtin_libraries.android.AndroidLibraryManager;
import com.duy.pascal.interperter.builtin_libraries.android.barcode.ZXingAPI;
import com.duy.pascal.interperter.builtin_libraries.android.connection.bluetooth.AndroidBluetoothLib;
import com.duy.pascal.interperter.builtin_libraries.android.connection.socketio.SocketIOLib;
import com.duy.pascal.interperter.builtin_libraries.android.connection.web.HtmlLib;
import com.duy.pascal.interperter.builtin_libraries.android.connection.wifi.AndroidWifiLib;
import com.duy.pascal.interperter.builtin_libraries.android.gps.AndroidLocationLib;
import com.duy.pascal.interperter.builtin_libraries.android.hardware.AndroidSensorLib;
import com.duy.pascal.interperter.builtin_libraries.android.hardware.AndroidVibrateLib;
import com.duy.pascal.interperter.builtin_libraries.android.media.AndroidMediaPlayerLib;
import com.duy.pascal.interperter.builtin_libraries.android.media.AndroidToneGeneratorLib;
import com.duy.pascal.interperter.builtin_libraries.android.temp.AndroidSettingLib;
import com.duy.pascal.interperter.builtin_libraries.android.temp.AndroidUtilsLib;
import com.duy.pascal.interperter.builtin_libraries.android.utils.AndroidBatteryLib;
import com.duy.pascal.interperter.builtin_libraries.android.utils.AndroidClipboardLib;
import com.duy.pascal.interperter.builtin_libraries.android.view.AndroidDialogLib;
import com.duy.pascal.interperter.builtin_libraries.android.view.AndroidNotifyLib;
import com.duy.pascal.interperter.builtin_libraries.android.voice.AndroidSpeechRecognitionLib;
import com.duy.pascal.interperter.builtin_libraries.android.voice.AndroidTextToSpeechLib;
import com.duy.pascal.interperter.builtin_libraries.annotations.PascalMethod;
import com.duy.pascal.interperter.builtin_libraries.crt.CrtLib;
import com.duy.pascal.interperter.builtin_libraries.crt.WinCrt;
import com.duy.pascal.interperter.builtin_libraries.graphic.BasicGraphicAPI;
import com.duy.pascal.interperter.builtin_libraries.io.InOutListener;
import com.duy.pascal.interperter.builtin_libraries.java.data.JavaCollectionsAPI;
import com.duy.pascal.interperter.builtin_libraries.math.MathLib;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.function.MethodDeclaration;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.JavaClassBasedType;
import com.duy.pascal.interperter.declaration.lang.types.PointerType;
import com.duy.pascal.interperter.declaration.lang.value.ConstantDefinition;
import com.duy.pascal.interperter.exceptions.parsing.PermissionDeniedException;
import com.duy.pascal.interperter.exceptions.parsing.io.LibraryNotFoundException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.systemfunction.builtin.AbstractMethodDeclaration;
import com.duy.pascal.interperter.systemfunction.builtin.AddressFunction;
import com.duy.pascal.interperter.systemfunction.builtin.AssignedPointerFunction;
import com.duy.pascal.interperter.systemfunction.builtin.CastObjectFunction;
import com.duy.pascal.interperter.systemfunction.builtin.CopyFunction;
import com.duy.pascal.interperter.systemfunction.builtin.ExitFunction;
import com.duy.pascal.interperter.systemfunction.builtin.ExitNoneFunction;
import com.duy.pascal.interperter.systemfunction.builtin.HighFunction;
import com.duy.pascal.interperter.systemfunction.builtin.LengthFunction;
import com.duy.pascal.interperter.systemfunction.builtin.LowFunction;
import com.duy.pascal.interperter.systemfunction.builtin.NewFunction;
import com.duy.pascal.interperter.systemfunction.builtin.NewInstanceObject;
import com.duy.pascal.interperter.systemfunction.builtin.NewInstanceParamsObject;
import com.duy.pascal.interperter.systemfunction.builtin.SetLengthFunction;
import com.duy.pascal.interperter.systemfunction.builtin.SizeOfArrayFunction;
import com.duy.pascal.interperter.systemfunction.builtin.SizeOfObjectFunction;
import com.duy.pascal.interperter.systemfunction.io.ReadFileFunction;
import com.duy.pascal.interperter.systemfunction.io.ReadFunction;
import com.duy.pascal.interperter.systemfunction.io.ReadLineFunction;
import com.duy.pascal.interperter.systemfunction.io.ReadlnFileFunction;
import com.duy.pascal.interperter.systemfunction.io.WriteFileFunction;
import com.duy.pascal.interperter.systemfunction.io.WriteFunction;
import com.duy.pascal.interperter.systemfunction.io.WriteLineFunction;
import com.duy.pascal.interperter.systemfunction.io.WritelnFileFunction;

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
    public static final Map<Name, Class<? extends PascalLibrary>> MAP_LIBRARIES = new Hashtable<>();

    static {
        put(CrtLib.NAME, CrtLib.class);
        put(WinCrt.NAME, WinCrt.class);
        put(DosLib.NAME, DosLib.class);
        put(MathLib.NAME, MathLib.class);
        put(BasicGraphicAPI.NAME, BasicGraphicAPI.class);
        put(StrUtilsLibrary.NAME, StrUtilsLibrary.class);
        put(SysUtilsLibrary.NAME, SysUtilsLibrary.class);

        put(AndroidMediaPlayerLib.NAME, AndroidMediaPlayerLib.class);
        put(AndroidUtilsLib.NAME, AndroidUtilsLib.class);
        put(AndroidToneGeneratorLib.NAME, AndroidToneGeneratorLib.class);
        put(AndroidWifiLib.NAME, AndroidWifiLib.class);
        put(AndroidSettingLib.NAME, AndroidSettingLib.class);
        put(AndroidBluetoothLib.NAME, AndroidBluetoothLib.class);

        put(AndroidBatteryLib.NAME, AndroidBatteryLib.class);
        put(AndroidTextToSpeechLib.NAME, AndroidTextToSpeechLib.class);
        put(AndroidSensorLib.NAME, AndroidSensorLib.class);
        put(AndroidClipboardLib.NAME, AndroidClipboardLib.class);
        put(AndroidNotifyLib.NAME, AndroidNotifyLib.class);
        put(AndroidVibrateLib.NAME, AndroidVibrateLib.class);
        put(AndroidSpeechRecognitionLib.NAME, AndroidSpeechRecognitionLib.class);
        put(ZXingAPI.NAME, ZXingAPI.class);
        put(AndroidMediaPlayerLib.NAME, AndroidMediaPlayerLib.class);
        put(HtmlLib.NAME, HtmlLib.class);
        put(AndroidLocationLib.NAME, AndroidLocationLib.class);
        put(JavaCollectionsAPI.NAME, JavaCollectionsAPI.class);

        put(SocketIOLib.NAME, SocketIOLib.class);
        put(AndroidDialogLib.NAME, AndroidDialogLib.class);
    }

    @NonNull
    private ExpressionContextMixin program;
    @NonNull
    private ProgramHandler handler;
    private AndroidLibraryManager facadeManager;

    public PascalLibraryManager(@NonNull ExpressionContextMixin program,
                                @NonNull ProgramHandler handler) {
        this.program = program;
        this.handler = handler;
        facadeManager = new AndroidLibraryManager(AndroidLibraryUtils.getSdkVersion(), handler);
    }

    private static void put(String name, Class<? extends PascalLibrary> claszz) {
        MAP_LIBRARIES.put(Name.create(name), claszz);
    }

    public static ArrayList<DescriptionImpl> getAllMethodDescription(Class<?>... classes) {
        ArrayList<DescriptionImpl> suggestItems = new ArrayList<>();
        for (Class<?> aClass : classes) {
            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                if (AndroidLibraryUtils.getSdkVersion() >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (method.getAnnotation(PascalMethod.class) != null) {
                        PascalMethod annotation = method.getAnnotation(PascalMethod.class);
                        String description = annotation.description();
                        // TODO: 17-Aug-17
//                        suggestItems.add(new FunctionDescription(StructureType.TYPE_FUNCTION,
//                                Name.create(method.getName()), description, method.));
                    }
                } else {
                    if (Modifier.isPublic(method.getModifiers())) {
                        suggestItems.add(new DescriptionImpl(StructureType.TYPE_FUNCTION, method.getName()));
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
            throw new LibraryNotFoundException(lineNumber, Name.create(t.getName()));
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
        program.declareFunction(new AbstractMethodDeclaration(new AssignedPointerFunction()));

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

        PascalLibrary library = (PascalLibrary) o;
        library.declareConstants(program);
        library.declareFunctions(program);
        library.declareTypes(program);
        library.declareVariables(program);
        for (Method method : o.getClass().getDeclaredMethods()) {
            if (AndroidLibraryUtils.getSdkVersion() >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                if (method.getAnnotation(PascalMethod.class) != null) {
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
