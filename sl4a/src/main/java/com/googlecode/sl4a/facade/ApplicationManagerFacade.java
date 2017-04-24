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

package com.googlecode.sl4a.facade;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.googlecode.sl4a.jsonrpc.RpcReceiver;
import com.googlecode.sl4a.rpc.PascalMethod;
import com.googlecode.sl4a.rpc.RpcParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Facade for managing Applications.
 */
public class ApplicationManagerFacade extends RpcReceiver {

    private final AndroidFacade mAndroidFacade;
    private final ActivityManager mActivityManager;
    private final PackageManager mPackageManager;

    public ApplicationManagerFacade(FacadeManager manager) {
        super(manager);
        Context context = manager.getContext();
        mAndroidFacade = manager.getReceiver(AndroidFacade.class);
        mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        mPackageManager = context.getPackageManager();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns a list of all launchable application class names.")
    public Map<String, String> getLaunchableApplications() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = mPackageManager.queryIntentActivities(intent, 0);
        Map<String, String> applications = new HashMap<>();
        for (ResolveInfo info : resolveInfos) {
            applications.put(info.loadLabel(mPackageManager).toString(), info.activityInfo.name);
        }
        return applications;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Start activity with the given class name.")
    public void launch(@RpcParameter(name = "className") String className) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        String packageName = className.substring(0, className.lastIndexOf("."));
        intent.setClassName(packageName, className);
        mAndroidFacade.startActivity(intent);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns a list of packages running activities or services.", returns = "List of packages running activities.")
    public List<String> getRunningPackages() {
        Set<String> runningPackages = new HashSet<>();
        List<ActivityManager.RunningAppProcessInfo> appProcesses =
                mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : appProcesses) {
            runningPackages.addAll(Arrays.asList(info.pkgList));
        }
        List<ActivityManager.RunningServiceInfo> serviceProcesses =
                mActivityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo info : serviceProcesses) {
            runningPackages.add(info.service.getPackageName());
        }
        return new ArrayList<>(runningPackages);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Force stops a package.")
    public void forceStopPackage(
            @RpcParameter(name = "packageName", description = "name of package") String packageName) {
        mActivityManager.restartPackage(packageName);
    }

    @Override
    public void shutdown() {
    }
}
