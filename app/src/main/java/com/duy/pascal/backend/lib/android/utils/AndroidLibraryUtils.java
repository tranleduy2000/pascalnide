/*
 * Copyright (C) 2010 Google Inc.
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

package com.duy.pascal.backend.lib.android.utils;

import android.os.Build;

import com.duy.pascal.backend.lib.android.AndroidApplicationManagerLib;
import com.duy.pascal.backend.lib.android.AndroidBatteryLib;
import com.duy.pascal.backend.lib.android.AndroidBluetoothLib;
import com.duy.pascal.backend.lib.android.AndroidMediaPlayerLib;
import com.duy.pascal.backend.lib.android.AndroidSensorLib;
import com.duy.pascal.backend.lib.android.AndroidSettingLib;
import com.duy.pascal.backend.lib.android.AndroidTextToSpeakLib;
import com.duy.pascal.backend.lib.android.AndroidToneGeneratorLib;
import com.duy.pascal.backend.lib.android.AndroidUtilsLib;
import com.duy.pascal.backend.lib.android.AndroidWifiLib;
import com.google.common.collect.Maps;
import com.googlecode.sl4a.facade.ActivityResultFacade;
import com.googlecode.sl4a.facade.CameraFacade;
import com.googlecode.sl4a.facade.CommonIntentsFacade;
import com.googlecode.sl4a.facade.ContactsFacade;
import com.googlecode.sl4a.facade.EventFacade;
import com.googlecode.sl4a.facade.LocationFacade;
import com.googlecode.sl4a.facade.MediaRecorderFacade;
import com.googlecode.sl4a.facade.PhoneFacade;
import com.googlecode.sl4a.facade.PreferencesFacade;
import com.googlecode.sl4a.facade.SignalStrengthFacade;
import com.googlecode.sl4a.facade.SmsFacade;
import com.googlecode.sl4a.facade.SpeechRecognitionFacade;
import com.googlecode.sl4a.facade.WakeLockFacade;
import com.googlecode.sl4a.facade.WebCamFacade;
import com.googlecode.sl4a.facade.ui.UiFacade;
import com.googlecode.sl4a.jsonrpc.RpcReceiver;
import com.googlecode.sl4a.rpc.MethodDescriptor;
import com.googlecode.sl4a.rpc.RpcDeprecated;
import com.googlecode.sl4a.rpc.RpcStartEvent;
import com.googlecode.sl4a.rpc.RpcStopEvent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Encapsulates the list of supported facades and their construction.
 *
 * @author Damon Kohler (damonkohler@gmail.com)
 * @author Igor Karp (igor.v.karp@gmail.com)
 */
public class AndroidLibraryUtils {
    private final static Set<Class<? extends RpcReceiver>> sFacadeClassList;
    private final static SortedMap<String, MethodDescriptor> sRpcs =
            new TreeMap<>();

    static {
        sFacadeClassList = new HashSet<>();
        sFacadeClassList.add(AndroidUtilsLib.class);
        sFacadeClassList.add(AndroidApplicationManagerLib.class);
        sFacadeClassList.add(CameraFacade.class);
        sFacadeClassList.add(CommonIntentsFacade.class);
        sFacadeClassList.add(ContactsFacade.class);
        sFacadeClassList.add(EventFacade.class);
        sFacadeClassList.add(LocationFacade.class);
        sFacadeClassList.add(PhoneFacade.class);
        sFacadeClassList.add(MediaRecorderFacade.class);
        sFacadeClassList.add(AndroidSensorLib.class);
        sFacadeClassList.add(AndroidSettingLib.class);
        sFacadeClassList.add(SmsFacade.class);
        sFacadeClassList.add(SpeechRecognitionFacade.class);
        sFacadeClassList.add(AndroidToneGeneratorLib.class);
        sFacadeClassList.add(WakeLockFacade.class);
        sFacadeClassList.add(AndroidWifiLib.class);
        sFacadeClassList.add(UiFacade.class);
        sFacadeClassList.add(ActivityResultFacade.class);
        sFacadeClassList.add(AndroidMediaPlayerLib.class);
        sFacadeClassList.add(PreferencesFacade.class);
        sFacadeClassList.add(AndroidTextToSpeakLib.class);
        sFacadeClassList.add(AndroidBluetoothLib.class);
        sFacadeClassList.add(SignalStrengthFacade.class);
        sFacadeClassList.add(AndroidBatteryLib.class);
        sFacadeClassList.add(WebCamFacade.class);
        for (Class<? extends RpcReceiver> recieverClass : sFacadeClassList) {
            for (MethodDescriptor rpcMethod : MethodDescriptor.collectFrom(recieverClass)) {
                sRpcs.put(rpcMethod.getName(), rpcMethod);
            }
        }
    }

    private AndroidLibraryUtils() {
        // Utility class.
    }

    public static int getSdkLevel() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * Returns a list of {@link MethodDescriptor} objects for all facades.
     */
    public static List<MethodDescriptor> collectMethodDescriptors() {
        return new ArrayList<>(sRpcs.values());
    }

    /**
     * Returns a list of not deprecated {@link MethodDescriptor} objects for facades supported by the
     * current SDK version.
     */
    public static List<MethodDescriptor> collectSupportedMethodDescriptors() {
        List<MethodDescriptor> list = new ArrayList<>();
        for (MethodDescriptor descriptor : sRpcs.values()) {
            Method method = descriptor.getMethod();
            if (method.isAnnotationPresent(RpcDeprecated.class)) {
                continue;
            }
            list.add(descriptor);
        }
        return list;
    }

    public static Map<String, MethodDescriptor> collectStartEventMethodDescriptors() {
        Map<String, MethodDescriptor> map = Maps.newHashMap();
        for (MethodDescriptor descriptor : sRpcs.values()) {
            Method method = descriptor.getMethod();
            if (method.isAnnotationPresent(RpcStartEvent.class)) {
                String eventName = method.getAnnotation(RpcStartEvent.class).value();
                if (map.containsKey(eventName)) {
                    throw new RuntimeException("Duplicate start event method descriptor found.");
                }
                map.put(eventName, descriptor);
            }
        }
        return map;
    }

    public static Map<String, MethodDescriptor> collectStopEventMethodDescriptors() {
        Map<String, MethodDescriptor> map = Maps.newHashMap();
        for (MethodDescriptor descriptor : sRpcs.values()) {
            Method method = descriptor.getMethod();
            if (method.isAnnotationPresent(RpcStopEvent.class)) {
                String eventName = method.getAnnotation(RpcStopEvent.class).value();
                if (map.containsKey(eventName)) {
                    throw new RuntimeException("Duplicate stop event method descriptor found.");
                }
                map.put(eventName, descriptor);
            }
        }
        return map;
    }

    /**
     * Returns a method by name.
     */
    public static MethodDescriptor getMethodDescriptor(String name) {
        return sRpcs.get(name);
    }

    public static Collection<Class<? extends RpcReceiver>> getFacadeClasses() {
        return sFacadeClassList;
    }
}
