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

package com.duy.pascal.interperter.builtin_libraries.android.gps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.builtin_libraries.IAndroidLibrary;
import com.duy.pascal.interperter.builtin_libraries.android.AndroidLibraryManager;
import com.duy.pascal.interperter.builtin_libraries.annotations.PascalMethod;
import com.duy.pascal.interperter.builtin_libraries.annotations.PascalParameter;
import com.googlecode.sl4a.rpc.RpcDefault;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AndroidLocationLib implements IAndroidLibrary {
    public static final String NAME = "aLocation".toLowerCase();
    private static final String TAG = "AndroidLocationLib";
    private Context mContext;
    private Map<String, Location> mLocationUpdates;
    private Geocoder mGeocoder;
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public synchronized void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged() called with: location = [" + location + "]");
            mLocationUpdates.put(location.getProvider(), location);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    private LocationManager mLocationManager;

    public AndroidLocationLib(AndroidLibraryManager manager) {
        if (manager.getContext() != null) {
            mContext = manager.getContext();
            mGeocoder = new Geocoder(mContext);
            mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            mLocationUpdates = new HashMap<>();
        }
    }

    public AndroidLocationLib() {

    }

    @Override
    public void shutdown() {
        stopLocating();
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

    @Override
    public String[] needPermission() {
        return new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }


    @PascalMethod(description = "Returns available providers on the phone")
    public List<String> locationProviders() {
        return mLocationManager.getAllProviders();
    }


    @PascalMethod(description = "Ask if provider is enabled")
    public boolean locationProviderEnabled(
            @PascalParameter(name = "provider", description = "Name of location provider") StringBuilder provider) {
        return mLocationManager.isProviderEnabled(provider.toString());
    }


    @PascalMethod(description = "Starts collecting location data.")
    public void startLocating(
            @PascalParameter(name = "minDistance", description = "minimum time between updates in milliseconds")
            @RpcDefault("5000") Integer minUpdateTime,
            @PascalParameter(name = "minUpdateDistance", description = "minimum distance between updates in meters")
            @RpcDefault("30") Integer minUpdateDistance) {
        for (String provider : mLocationManager.getAllProviders()) {
            if (ActivityCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(mContext, "Request permission failed, please granted permission!", Toast.LENGTH_SHORT).show();
                return;
            }
            mLocationManager.requestLocationUpdates(provider, minUpdateTime, minUpdateDistance,
                    mLocationListener, mContext.getMainLooper());
        }
    }


    @PascalMethod(description = "Returns the current location as indicated by all available providers.", returns = "A map of location information by provider.")
    public Map<String, Location> readLocation() {
        return (mLocationUpdates);
    }

    @PascalMethod(description = "Stops collecting location data.")
    public synchronized void stopLocating() {
        mLocationManager.removeUpdates(mLocationListener);
        clearDataLocation();
    }

    @PascalMethod(description = "Clear all data location")
    public void clearDataLocation() {
        mLocationUpdates.clear();
    }


    @PascalMethod(description = "Returns the last known location of the device.",
            returns = "A map of location information by provider.")
    public Map<String, Location> getLastKnownLocation() {
        Map<String, Location> location = new HashMap<>();
        for (String provider : mLocationManager.getAllProviders()) {
            if (ActivityCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(mContext, "Request permission failed, please granted permission!", Toast.LENGTH_SHORT).show();
                return location;
            }
            location.put(provider, mLocationManager.getLastKnownLocation(provider));
        }
        return location;
    }


    @PascalMethod(description = "Returns a list of addresses for the given latitude and longitude.", returns = "A list of addresses.")
    public List<Address> geocode(
            @PascalParameter(name = "latitude") Double latitude,
            @PascalParameter(name = "longitude") Double longitude,
            @PascalParameter(name = "maxResults", description = "maximum number of results") @RpcDefault("1") Integer maxResults)
            throws IOException {
        return mGeocoder.getFromLocation(latitude, longitude, maxResults);
    }
}
