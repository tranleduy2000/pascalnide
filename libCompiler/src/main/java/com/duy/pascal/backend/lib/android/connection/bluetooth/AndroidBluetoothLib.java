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

package com.duy.pascal.backend.lib.android.connection.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

import com.duy.pascal.backend.lib.PascalLibrary;
import com.duy.pascal.backend.lib.android.AndroidLibraryManager;
import com.duy.pascal.backend.lib.android.temp.AndroidUtilsLib;
import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.duy.pascal.backend.lib.annotations.PascalParameter;
import com.googlecode.sl4a.Constants;
import com.googlecode.sl4a.MainThread;
import com.googlecode.sl4a.rpc.RpcDefault;
import com.googlecode.sl4a.rpc.RpcOptional;
import com.js.interpreter.expressioncontext.ExpressionContextMixin;

import org.apache.commons.codec.binary.Base64Codec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Bluetooth functions.
 */
// Discovery functions added by Eden Sayag


public class AndroidBluetoothLib implements PascalLibrary {

    public static final String NAME = "aBluetooth".toLowerCase();
    // UUID for SL4A.
    private static final String DEFAULT_UUID = "457807c0-4897-11df-9879-0800200c9a66";
    private static final String SDP_NAME = "SL4A";
    private Map<String, BluetoothConnection> connections = new HashMap<>();
    private AndroidUtilsLib mAndroidFacade;
    private BluetoothAdapter mBluetoothAdapter;

    public AndroidBluetoothLib(AndroidLibraryManager manager) {
        mAndroidFacade = manager.getReceiver(AndroidUtilsLib.class);
        mBluetoothAdapter = MainThread.run(manager.getContext(), new Callable<BluetoothAdapter>() {
            @Override
            public BluetoothAdapter call() throws Exception {
                return BluetoothAdapter.getDefaultAdapter();
            }
        });
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns active Bluetooth connections.")
    public Map<String, String> bluetoothActiveConnections() {
        Map<String, String> out = new HashMap<>();
        for (Map.Entry<String, BluetoothConnection> entry : connections.entrySet()) {
            if (entry.getValue().isConnected()) {
                out.put(entry.getKey(), entry.getValue().getRemoteBluetoothAddress());
            }
        }

        return out;
    }

    private BluetoothConnection getConnection(String connID) throws IOException {
        BluetoothConnection conn = null;
        if (connID.trim().length() > 0) {
            conn = connections.get(connID);
        } else if (connections.size() == 1) {
            conn = (BluetoothConnection) connections.values().toArray()[0];
        }
        if (conn == null) {
            throw new IOException("Bluetooth not ready for this connID.");
        }
        return conn;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Send bytes over the currently open Bluetooth connection.")
    public void bluetoothWriteBinary(
            @PascalParameter(name = "base64", description = "A base64 encoded String of the bytes to be sent.")
                    String base64,
            @PascalParameter(name = "connID", description = "Connection id") @RpcDefault("")
            @RpcOptional String connID)
            throws IOException {
        BluetoothConnection conn = getConnection(connID);
        try {
            conn.write(Base64Codec.decodeBase64(base64));
        } catch (IOException e) {
            connections.remove(conn.getUUID());
            throw e;
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Read up to bufferSize bytes and return a chunked, base64 encoded string.")
    public String bluetoothReadBinary(
            @PascalParameter(name = "bufferSize") @RpcDefault("4096") Integer bufferSize,
            @PascalParameter(name = "connID", description = "Connection id") @RpcDefault("") @RpcOptional String connID)
            throws IOException {

        BluetoothConnection conn = getConnection(connID);
        try {
            return Base64Codec.encodeBase64String(conn.readBinary(bufferSize));
        } catch (IOException e) {
            connections.remove(conn.getUUID());
            throw e;
        }
    }

    private String addConnection(BluetoothConnection conn) {
        String uuid = UUID.randomUUID().toString();
        connections.put(uuid, conn);
        conn.setUUID(uuid);
        return uuid;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Connect to a device over Bluetooth. Blocks until the connection is established or fails.", returns = "True if the connection was established successfully.")
    public String bluetoothConnect(
            @PascalParameter(name = "uuid", description = "The UUID passed here must match the UUID used by the server device.") @RpcDefault(DEFAULT_UUID) String uuid,
            @PascalParameter(name = "address", description = "The user will be presented with a list of discovered devices to choose from if an address is not provided.") @RpcOptional String address)
            throws IOException {
        if (address == null) {
            Intent deviceChooserIntent = new Intent();
            deviceChooserIntent.setComponent(Constants.BLUETOOTH_DEVICE_LIST_COMPONENT_NAME);
            Intent result = mAndroidFacade.startActivityForResult(deviceChooserIntent);
            if (result != null && result.hasExtra(Constants.EXTRA_DEVICE_ADDRESS)) {
                address = result.getStringExtra(Constants.EXTRA_DEVICE_ADDRESS);
            } else {
                return null;
            }
        }
        BluetoothDevice mDevice;
        BluetoothSocket mSocket;
        BluetoothConnection conn;
        mDevice = mBluetoothAdapter.getRemoteDevice(address);
        mSocket = mDevice.createRfcommSocketToServiceRecord(UUID.fromString(uuid));
        // Always cancel discovery because it will slow down a connection.
        mBluetoothAdapter.cancelDiscovery();
        mSocket.connect();
        conn = new BluetoothConnection(mSocket);
        return addConnection(conn);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Listens for and accepts a Bluetooth connection. Blocks until the connection is established or fails.")
    public String bluetoothAccept(
            @PascalParameter(name = "uuid") @RpcDefault(DEFAULT_UUID) String uuid,
            @PascalParameter(name = "timeout", description = "How long to wait for a new connection, 0 is wait for ever") @RpcDefault("0") Integer timeout)
            throws IOException {
        BluetoothServerSocket mServerSocket;
        mServerSocket =
                mBluetoothAdapter.listenUsingRfcommWithServiceRecord(SDP_NAME, UUID.fromString(uuid));
        BluetoothSocket mSocket = mServerSocket.accept(timeout);
        BluetoothConnection conn = new BluetoothConnection(mSocket, mServerSocket);
        return addConnection(conn);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Requests that the device be discoverable for Bluetooth connections.")
    public void bluetoothMakeDiscoverable(
            @PascalParameter(name = "duration", description = "period of time, in seconds, during which the device should be discoverable") @RpcDefault("300") Integer duration) {
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, duration);
            // Use startActivityForResult to make this a synchronous call.
            mAndroidFacade.startActivityForResult(discoverableIntent);
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Sends ASCII characters over the currently open Bluetooth connection.")
    public void bluetoothWrite(@PascalParameter(name = "ascii") String ascii,
                               @PascalParameter(name = "connID", description = "Connection id") @RpcDefault("") String connID)
            throws IOException {
        BluetoothConnection conn = getConnection(connID);
        try {
            conn.write(ascii);
        } catch (IOException e) {
            connections.remove(conn.getUUID());
            throw e;
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns True if the next read is guaranteed not to block.")
    public Boolean bluetoothReadReady(
            @PascalParameter(name = "connID", description = "Connection id") @RpcDefault("") @RpcOptional String connID)
            throws IOException {
        BluetoothConnection conn = getConnection(connID);
        try {
            return conn.readReady();
        } catch (IOException e) {
            connections.remove(conn.getUUID());
            throw e;
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Read up to bufferSize ASCII characters.")
    public String bluetoothRead(
            @PascalParameter(name = "bufferSize") @RpcDefault("4096") Integer bufferSize,
            @PascalParameter(name = "connID", description = "Connection id") @RpcOptional @RpcDefault("") String connID)
            throws IOException {
        BluetoothConnection conn = getConnection(connID);
        try {
            return conn.read(bufferSize);
        } catch (IOException e) {
            connections.remove(conn.getUUID());
            throw e;
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Read the next lineInfo.")
    public String bluetoothReadLine(
            @PascalParameter(name = "connID", description = "Connection id") @RpcOptional @RpcDefault("") String connID)
            throws IOException {
        BluetoothConnection conn = getConnection(connID);
        try {
            return conn.readLine();
        } catch (IOException e) {
            connections.remove(conn.getUUID());
            throw e;
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Queries a remote device for it's name or null if it can't be resolved")
    public String bluetoothGetRemoteDeviceName(
            @PascalParameter(name = "address", description = "Bluetooth Address For Target Device") String address) {
        try {
            BluetoothDevice mDevice;
            mDevice = mBluetoothAdapter.getRemoteDevice(address);
            return mDevice.getName();
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Gets the Bluetooth Visible device name")
    public String bluetoothGetLocalName() {
        return mBluetoothAdapter.getName();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Sets the Bluetooth Visible device name, returns True on success")
    public boolean bluetoothSetLocalName(
            @PascalParameter(name = "name", description = "New local name") String name) {
        return mBluetoothAdapter.setName(name);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Gets the scan mode for the local dongle.\r\n" + "Return values:\r\n"
            + "\t-1 when Bluetooth is disabled.\r\n" + "\t0 if non discoverable and non connectable.\r\n"
            + "\r1 connectable non discoverable." + "\r3 connectable and discoverable.")
    public int bluetoothGetScanMode() {
        if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF
                || mBluetoothAdapter.getState() == BluetoothAdapter.STATE_TURNING_OFF) {
            return -1;
        }

        switch (mBluetoothAdapter.getScanMode()) {
            case BluetoothAdapter.SCAN_MODE_NONE:
                return 0;
            case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                return 1;
            case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                return 3;
            default:
                return mBluetoothAdapter.getScanMode() - 20;
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the name of the connected device.")
    public String bluetoothGetConnectedDeviceName(
            @PascalParameter(name = "connID", description = "Connection id") @RpcOptional @RpcDefault("") String connID)
            throws IOException {
        BluetoothConnection conn = getConnection(connID);
        return conn.getConnectedDeviceName();
    }

    @PascalMethod(description = "Checks Bluetooth state.", returns = "True if Bluetooth is enabled.")
    public Boolean checkBluetoothState() {
        return mBluetoothAdapter.isEnabled();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Toggle Bluetooth on and off.", returns = "True if Bluetooth is enabled.")
    public Boolean toggleBluetoothState(
            @PascalParameter(name = "enabled") @RpcOptional Boolean enabled,
            @PascalParameter(name = "prompt", description = "Prompt the user to confirm changing the Bluetooth state.") @RpcDefault("true") Boolean prompt) {
        if (enabled == null) {
            enabled = !checkBluetoothState();
        }
        if (enabled) {
            if (prompt) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                // TODO(damonkohler): Use the result to determine if this was successful. At any rate, keep
                // using startActivityForResult in order to synchronize this call.
                mAndroidFacade.startActivityForResult(intent);
            } else {
                // TODO(damonkohler): Make this synchronous as well.
                mBluetoothAdapter.enable();
            }
        } else {
            // TODO(damonkohler): Add support for prompting on disable.
            // TODO(damonkohler): Make this synchronous as well.
            shutdown();
            mBluetoothAdapter.disable();
        }
        return enabled;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Stops Bluetooth connection.")
    public void bluetoothStop(
            @PascalParameter(name = "connID", description = "Connection id") @RpcOptional @RpcDefault("") String connID) {
        BluetoothConnection conn;
        try {
            conn = getConnection(connID);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        if (conn == null) {
            return;
        }

        conn.stop();
        connections.remove(conn.getUUID());
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the hardware address of the local Bluetooth adapter. ")
    public String bluetoothGetLocalAddress() {
        return mBluetoothAdapter.getAddress();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Start the remote device discovery process. ", returns = "true on success, false on error")
    public Boolean bluetoothDiscoveryStart() {
        return mBluetoothAdapter.startDiscovery();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Cancel the current device discovery process.", returns = "true on success, false on error")
    public Boolean bluetoothDiscoveryCancel() {
        return mBluetoothAdapter.cancelDiscovery();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Return true if the local Bluetooth adapter is currently in the device discovery process. ")
    public Boolean bluetoothIsDiscovering() {
        return mBluetoothAdapter.isDiscovering();
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    @PascalMethod(description = "stop")

    public void shutdown() {
        for (Map.Entry<String, BluetoothConnection> entry : connections.entrySet()) {
            entry.getValue().stop();
        }
        connections.clear();
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

