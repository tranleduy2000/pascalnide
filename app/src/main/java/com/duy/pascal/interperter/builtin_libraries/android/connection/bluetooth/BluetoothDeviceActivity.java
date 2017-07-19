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

package com.duy.pascal.interperter.builtin_libraries.android.connection.bluetooth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.activities.AbstractAppCompatActivity;
import com.googlecode.sl4a.Constants;

import java.util.ArrayList;

public class BluetoothDeviceActivity extends AbstractAppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listView;
    private DeviceListAdapter mAdapter;
    private BluetoothDiscoveryHelper mBluetoothHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_device);

        mAdapter = new DeviceListAdapter(this, 0);
        mBluetoothHelper = new BluetoothDiscoveryHelper(this, mAdapter);

        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBluetoothHelper.startDiscovery();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBluetoothHelper.cancel();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DeviceInfo device = (DeviceInfo) mAdapter.getItem(position);
        Intent result = new Intent();
        if (device != null) {
            result.putExtra(Constants.EXTRA_DEVICE_ADDRESS, device.mmAddress);
        }
        setResult(RESULT_OK, result);
        finish();
    }

    private class DeviceInfo {
        final String mmName;
        final String mmAddress;

        DeviceInfo(String name, String address) {
            mmName = name;
            mmAddress = address;
        }
    }

    private class DeviceListAdapter extends ArrayAdapter implements BluetoothDiscoveryHelper.BluetoothDiscoveryListener {
        @NonNull
        ArrayList<DeviceInfo> mmDeviceList = new ArrayList<>();
        private LayoutInflater inflater;

        DeviceListAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
            this.inflater = LayoutInflater.from(context);
        }

        public void addDevice(String name, String address) {
            mmDeviceList.add(new DeviceInfo(name, address));
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mmDeviceList.size();
        }

        @Override
        @Nullable
        public Object getItem(int position) {
            return mmDeviceList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup);
            }
            DeviceInfo device = mmDeviceList.get(position);
            TextView view = (TextView) convertView.findViewById(android.R.id.text1);
            view.setText(device.mmName + " (" + device.mmAddress + ")");
            return view;
        }

        @Override
        public void addBondedDevice(String name, String address) {
            addDevice(name, address);
        }

        @Override
        public void scanDone() {
        }
    }
}
