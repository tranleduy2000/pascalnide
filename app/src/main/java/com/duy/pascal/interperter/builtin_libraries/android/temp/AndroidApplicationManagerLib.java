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

package com.duy.pascal.interperter.builtin_libraries.android.temp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.builtin_libraries.PascalLibrary;
import com.duy.pascal.interperter.builtin_libraries.android.AndroidLibraryManager;
import com.duy.pascal.interperter.builtin_libraries.annotations.PascalMethod;
import com.duy.pascal.interperter.builtin_libraries.annotations.PascalParameter;

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
public class AndroidApplicationManagerLib extends PascalLibrary {

    private AndroidUtilsLib mAndroidFacade;
    private ActivityManager mActivityManager;
    private PackageManager mPackageManager;

    public AndroidApplicationManagerLib(AndroidLibraryManager manager) {
        Context context = manager.getContext();
        mAndroidFacade = manager.getReceiver(AndroidUtilsLib.class);
        if (context != null) {
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            mPackageManager = context.getPackageManager();
        }
    }


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


    @PascalMethod(description = "Start activity with the given class name.")
    public void launch(@PascalParameter(name = "className") String className) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        String packageName = className.substring(0, className.lastIndexOf("."));
        intent.setClassName(packageName, className);
        mAndroidFacade.startActivity(intent);
    }


    @PascalMethod(description = "Returns a list of packages RUNNING activities or services.", returns = "List of packages RUNNING activities.")
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


    @PascalMethod(description = "Force stops a package.")
    public void forceStopPackage(
            @PascalParameter(name = "packageName", description = "name of package") String packageName) {
        mActivityManager.restartPackage(packageName);
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    @PascalMethod(description = "stop")

    public void shutdown() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void declareConstants(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareTypes(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareVariables(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin parentContext) {

    }
}
