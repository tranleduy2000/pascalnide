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

package com.googlecode.sl4a.facade;

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
import android.widget.Toast;

import com.duy.pascal.backend.lib.android.utils.FacadeManager;
import com.google.common.collect.Maps;
import com.googlecode.sl4a.jsonrpc.RpcReceiver;
import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.googlecode.sl4a.rpc.RpcDefault;
import com.googlecode.sl4a.rpc.RpcParameter;
import com.googlecode.sl4a.rpc.RpcStartEvent;
import com.googlecode.sl4a.rpc.RpcStopEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This facade exposes the LocationManager related functionality.<br>
 * <br>
 * <b>Overview</b><br>
 * Once activated by 'startLocating' the LocationFacade attempts to return location data collected
 * via GPS or the cell network. If neither are available the last known location may be retrieved.
 * If both are available the format of the returned data is:<br>
 * {u'network': {u'altitude': 0, u'provider': u'network', u'longitude': -0.38509020000000002,
 * u'time': 1297079691231L, u'latitude': 52.410557300000001, u'speed': 0, u'accuracy': 75}, u'gps':
 * {u'altitude': 51, u'provider': u'gps', u'longitude': -0.38537094593048096, u'time':
 * 1297079709000L, u'latitude': 52.41076922416687, u'speed': 0, u'accuracy': 24}}<br>
 * If neither are available {} is returned. <br>
 * Example (python):<br>
 * <p>
 * <pre>
 * import android, time
 * droid = android.Android()
 * droid.startLocating()
 * time.sleep(15)
 * loc = droid.readLocation().result
 * if loc = {}:
 *   loc = getLastKnownLocation().result
 * if loc != {}:
 *   try:
 *     n = loc['gps']
 *   except KeyError:
 *     n = loc['network']
 *   la = n['latitude']
 *   lo = n['longitude']
 *   address = droid.geocode(la, lo).result
 * droid.stopLocating()
 * </pre>
 * <p>
 * The address format is:<br>
 * [{u'thoroughfare': u'Some Street', u'locality': u'Some Town', u'sub_admin_area': u'Some Borough',
 * u'admin_area': u'Some City', u'feature_name': u'House Numbers', u'country_code': u'GB',
 * u'country_name': u'United Kingdom', u'postal_code': u'ST1 1'}]
 *
 * @author Damon Kohler (damonkohler@gmail.com)
 * @author Felix Arends (felix.arends@gmail.com)
 */
public class LocationFacade extends RpcReceiver {
    private final EventFacade mEventFacade;
    private final Context mContext;
    private final Map<String, Location> mLocationUpdates;
    private final LocationManager mLocationManager;
    private final Geocoder mGeocoder;

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public synchronized void onLocationChanged(Location location) {
            mLocationUpdates.put(location.getProvider(), location);
            Map<String, Location> copy = Maps.newHashMap();
            for (Entry<String, Location> entry : mLocationUpdates.entrySet()) {
                copy.put(entry.getKey(), entry.getValue());
            }
            mEventFacade.postEvent("location", copy);
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

    public LocationFacade(FacadeManager manager) {
        super(manager);
        mContext = manager.getContext();
        mEventFacade = manager.getReceiver(EventFacade.class);
        mGeocoder = new Geocoder(mContext);
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mLocationUpdates = new HashMap<>();
    }

    @Override
    public void shutdown() {
        stopLocating();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns availables providers on the phone")
    public List<String> locationProviders() {
        return mLocationManager.getAllProviders();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Ask if provider is enabled")
    public boolean locationProviderEnabled(
            @RpcParameter(name = "provider", description = "Name of location provider") String provider) {
        return mLocationManager.isProviderEnabled(provider);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Starts collecting location data.")
    @RpcStartEvent("location")
    public void startLocating(
            @RpcParameter(name = "minDistance", description = "minimum time between updates in milliseconds") @RpcDefault("60000") Integer minUpdateTime,
            @RpcParameter(name = "minUpdateDistance", description = "minimum distance between updates in meters") @RpcDefault("30") Integer minUpdateDistance) {
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

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the current location as indicated by all available providers.", returns = "A map of location information by provider.")
    public Map<String, Location> readLocation() {
        return mLocationUpdates;
    }

    @PascalMethod(description = "Stops collecting location data.")
    @RpcStopEvent("location")
    public synchronized void stopLocating() {
        mLocationManager.removeUpdates(mLocationListener);
        mLocationUpdates.clear();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the last known location of the device.", returns = "A map of location information by provider.")
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

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns a list of addresses for the given latitude and longitude.", returns = "A list of addresses.")
    public List<Address> geocode(
            @RpcParameter(name = "latitude") Double latitude,
            @RpcParameter(name = "longitude") Double longitude,
            @RpcParameter(name = "maxResults", description = "maximum number of results") @RpcDefault("1") Integer maxResults)
            throws IOException {
        return mGeocoder.getFromLocation(latitude, longitude, maxResults);
    }
}
