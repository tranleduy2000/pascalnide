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

package com.duy.pascal.interperter.libraries;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.ast.runtime_value.value.NullValue;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.function.AbstractFunction;
import com.duy.pascal.interperter.declaration.lang.function.MethodDeclaration;
import com.duy.pascal.interperter.declaration.lang.types.ArgumentType;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.JavaClassBasedType;
import com.duy.pascal.interperter.declaration.lang.types.PointerType;
import com.duy.pascal.interperter.declaration.lang.types.converter.TypeConverter;
import com.duy.pascal.interperter.declaration.lang.value.ConstantDefinition;
import com.duy.pascal.interperter.exceptions.parsing.PermissionDeniedException;
import com.duy.pascal.interperter.exceptions.parsing.io.LibraryNotFoundException;
import com.duy.pascal.interperter.libraries.android.AndroidLibraryManager;
import com.duy.pascal.interperter.libraries.android.barcode.ZXingAPI;
import com.duy.pascal.interperter.libraries.android.connection.bluetooth.AndroidBluetoothLib;
import com.duy.pascal.interperter.libraries.android.connection.socketio.SocketIOLib;
import com.duy.pascal.interperter.libraries.android.connection.web.HtmlLib;
import com.duy.pascal.interperter.libraries.android.connection.wifi.AndroidWifiLib;
import com.duy.pascal.interperter.libraries.android.gps.AndroidLocationLib;
import com.duy.pascal.interperter.libraries.android.hardware.AndroidSensorLib;
import com.duy.pascal.interperter.libraries.android.hardware.AndroidVibrateLib;
import com.duy.pascal.interperter.libraries.android.media.AndroidMediaPlayerLib;
import com.duy.pascal.interperter.libraries.android.media.AndroidToneGeneratorLib;
import com.duy.pascal.interperter.libraries.android.temp.AndroidSettingLib;
import com.duy.pascal.interperter.libraries.android.temp.AndroidUtilsLib;
import com.duy.pascal.interperter.libraries.android.utils.AndroidBatteryLib;
import com.duy.pascal.interperter.libraries.android.utils.AndroidClipboardLib;
import com.duy.pascal.interperter.libraries.android.view.AndroidDialogLib;
import com.duy.pascal.interperter.libraries.android.view.AndroidNotifyLib;
import com.duy.pascal.interperter.libraries.android.voice.AndroidSpeechRecognitionLib;
import com.duy.pascal.interperter.libraries.android.voice.AndroidTextToSpeechLib;
import com.duy.pascal.interperter.libraries.annotations.PascalMethod;
import com.duy.pascal.interperter.libraries.crt.CrtLib;
import com.duy.pascal.interperter.libraries.crt.WinCrt;
import com.duy.pascal.interperter.libraries.graphic.GraphicAPI;
import com.duy.pascal.interperter.libraries.io.InOutListener;
import com.duy.pascal.interperter.libraries.java.data.JavaCollectionsAPI;
import com.duy.pascal.interperter.libraries.math.MathLib;
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
import com.duy.pascal.ui.autocomplete.completion.model.DescriptionImpl;
import com.duy.pascal.ui.runnable.ConsoleHandler;
import com.duy.pascal.ui.runnable.ProgramHandler;
import com.duy.pascal.ui.structure.viewholder.StructureType;
import com.duy.pascal.ui.utils.DLog;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Duy on 08-Apr-17.
 */
public class PascalLibraryManager {
    public static final Map<Name, Class<? extends IPascalLibrary>> MAP_LIBRARIES = new Hashtable<>();
    private static final String TAG = "PascalLibraryManager";

    static {
        put(CrtLib.NAME, CrtLib.class);
        put(WinCrt.NAME, WinCrt.class);
        put(DosLib.NAME, DosLib.class);
        put(MathLib.NAME, MathLib.class);
        put(GraphicAPI.NAME, GraphicAPI.class);
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
    private ExpressionContextMixin mProgram;
    @NonNull
    private ProgramHandler mHandler;
    private AndroidLibraryManager mFacadeManager;

    public PascalLibraryManager(@NonNull ExpressionContextMixin program,
                                @NonNull ProgramHandler handler) {
        this.mProgram = program;
        this.mHandler = handler;
        this.mFacadeManager = new AndroidLibraryManager(AndroidLibraryUtils.getSdkVersion(), handler);
    }

    private static void put(String name, Class<? extends IPascalLibrary> claszz) {
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

    public void addMethodFromClass(Class<? extends IPascalLibrary> clazz, LineInfo lineNumber) throws PermissionDeniedException, LibraryNotFoundException {
        Object parent = null;
        Constructor constructor;
        try {
            constructor = clazz.getConstructor(InOutListener.class);
            parent = constructor.newInstance(mHandler);
        } catch (Exception ignored) {
        }
        if (parent == null) {
            try {
                constructor = clazz.getConstructor(ConsoleHandler.class);
                parent = constructor.newInstance(mHandler);
            } catch (Exception ignored) {
            }
        }
        if (parent == null) {
            try {
                constructor = clazz.getConstructor(AndroidLibraryManager.class);
                parent = constructor.newInstance(mFacadeManager);
            } catch (Exception ignored) {
            }
        }
        if (parent == null) {
            try {
                constructor = clazz.getConstructor();
                parent = constructor.newInstance();
            } catch (Exception ignored) {
            }
        }
        addMethodFromLibrary(clazz, parent, lineNumber);
    }

    /**
     * load system method
     */
    public void loadSystemLibrary() throws PermissionDeniedException, LibraryNotFoundException {
        //load builtin function
        mProgram.declareFunction(new AbstractMethodDeclaration(new SetLengthFunction()));
        mProgram.declareFunction(new AbstractMethodDeclaration(new LengthFunction()));
        mProgram.declareFunction(new AbstractMethodDeclaration(new SizeOfObjectFunction()));
        mProgram.declareFunction(new AbstractMethodDeclaration(new SizeOfArrayFunction()));
        mProgram.declareFunction(new AbstractMethodDeclaration(new ExitFunction()));
        mProgram.declareFunction(new AbstractMethodDeclaration(new ExitNoneFunction()));
        mProgram.declareFunction(new AbstractMethodDeclaration(new HighFunction()));
        mProgram.declareFunction(new AbstractMethodDeclaration(new LowFunction()));
        mProgram.declareFunction(new AbstractMethodDeclaration(new NewFunction()));
        mProgram.declareFunction(new AbstractMethodDeclaration(new CopyFunction()));

        mProgram.declareFunction(new AbstractMethodDeclaration(new CastObjectFunction()));
        mProgram.declareFunction(new AbstractMethodDeclaration(new NewInstanceParamsObject()));
        mProgram.declareFunction(new AbstractMethodDeclaration(new NewInstanceObject()));
        mProgram.declareFunction(new AbstractMethodDeclaration(new AddressFunction()));
        mProgram.declareFunction(new AbstractMethodDeclaration(new AssignedPointerFunction()));

        //io region
        mProgram.declareFunction(new AbstractMethodDeclaration(new ReadFileFunction()));
        mProgram.declareFunction(new AbstractMethodDeclaration(new ReadlnFileFunction()));
        mProgram.declareFunction(new AbstractMethodDeclaration(new ReadLineFunction()));
        mProgram.declareFunction(new AbstractMethodDeclaration(new ReadFunction()));

        mProgram.declareFunction(new AbstractMethodDeclaration(new WriteFileFunction()));
        mProgram.declareFunction(new AbstractMethodDeclaration(new WritelnFileFunction()));
        mProgram.declareFunction(new AbstractMethodDeclaration(new WriteLineFunction()));
        mProgram.declareFunction(new AbstractMethodDeclaration(new WriteFunction()));
        //end region

        mProgram.declareConst(new ConstantDefinition("null", new JavaClassBasedType(NullValue.get().getClass()), NullValue.get(), null));
        mProgram.declareConst(new ConstantDefinition("nil", new PointerType(null), NullValue.get(), null));

        mProgram.declareConst(new ConstantDefinition("maxint", BasicType.Integer, Integer.MAX_VALUE, null));
        mProgram.declareConst(new ConstantDefinition("maxlongint", BasicType.Long, Long.MAX_VALUE, null));

        mProgram.declareConst(new ConstantDefinition("pi", BasicType.Double, Math.PI, null));

        addMethodFromClass(SystemLibrary.class, new LineInfo(-1, "system"));
    }

    public void addMethodFromLibrary(Class<? extends IPascalLibrary> clazz,
                                     @Nullable Object instance, @Nullable LineInfo line) throws PermissionDeniedException {
        if (instance instanceof IAndroidLibrary) {
            String[] permissions = ((IAndroidLibrary) instance).needPermission();
            for (String permission : permissions) {
                if (DLog.ANDROID) {
                    int i = ActivityCompat.checkSelfPermission(mHandler.getApplicationContext(), permission);
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        throw new PermissionDeniedException(((IAndroidLibrary) instance).getName(), permission, line);
                    }
                }
            }
        }

        PascalLibrary library = (PascalLibrary) instance;
        if (library != null) {
            library.declareConstants(mProgram);
            library.declareFunctions(mProgram);
            library.declareTypes(mProgram);
            library.declareVariables(mProgram);
        }
        ArrayList<AbstractFunction> declarations = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers()) && !isHiddenSystemMethod(method.getName())) {
                if (AndroidLibraryUtils.getSdkVersion() >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (method.getAnnotation(PascalMethod.class) != null) {
                        PascalMethod annotation = method.getAnnotation(PascalMethod.class);
                        String description = annotation.description();
                        MethodDeclaration methodDecl = new MethodDeclaration(instance, method, description);
                        declarations.add(methodDecl);
                    }
                } else {
                    MethodDeclaration methodDeclaration = new MethodDeclaration(instance, method);
                    declarations.add(methodDeclaration);
                }
            }
        }
        DLog.d(TAG, "addMethodFromLibrary: sort");
        Collections.sort(declarations, new Comparator<AbstractFunction>() {
            @Override
            public int compare(AbstractFunction o1, AbstractFunction o2) {
                if (o1.getName().equals(o2.getName())) {
                    ArgumentType[] types1 = o1.argumentTypes();
                    ArgumentType[] types2 = o2.argumentTypes();
                    if (types1.length != types2.length) {
                        return -1;
                    }

                    for (int i = 0; i < types1.length; i++) {
                        Class<?> t1Class = types1[i].getRuntimeClass();
                        Class<?> t2Class = types2[i].getRuntimeClass();
                        if (t1Class.equals(t2Class)) {
                            continue;
                        }
                        if (TypeConverter.isPrimitive(t1Class) && TypeConverter.isPrimitive(t2Class)) {
                            if (!types1[i].equals(types2[i])) {
                                if (TypeConverter.isLowerThanPrecedence(t1Class, t2Class)) {
                                    return -1;
                                }
                            }
                        } else {
                            return -1;
                        }
                    }
                }
                return o1.getName().compareTo(o2.getName());
            }
        });
        for (AbstractFunction declaration : declarations) {
            System.out.println(declaration);
            mProgram.declareFunction(declaration);
        }
    }

    private boolean isHiddenSystemMethod(String name) {
        return name.equals("declareConstants")
                || name.equals("declareFunctions")
                || name.equals("declareTypes")
                || name.equals("shutdown")
                || name.equals("instantiate")
                || name.equals("declareVariables");
    }

}
