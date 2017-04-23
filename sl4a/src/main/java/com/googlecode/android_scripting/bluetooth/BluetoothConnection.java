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

package com.googlecode.android_scripting.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import com.googlecode.android_scripting.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class BluetoothConnection {
    private BluetoothSocket mSocket;
    private BluetoothDevice mDevice;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private BufferedReader mReader;
    private BluetoothServerSocket mServerSocket;
    private String UUID;

    public BluetoothConnection(BluetoothSocket mSocket) throws IOException {
        this(mSocket, null);
    }

    public BluetoothConnection(BluetoothSocket mSocket, BluetoothServerSocket mServerSocket)
            throws IOException {
        this.mSocket = mSocket;
        mOutputStream = mSocket.getOutputStream();
        mInputStream = mSocket.getInputStream();
        mDevice = mSocket.getRemoteDevice();
        mReader = new BufferedReader(new InputStreamReader(mInputStream, "ASCII"));
        this.mServerSocket = mServerSocket;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getRemoteBluetoothAddress() {
        return mDevice.getAddress();
    }

    public boolean isConnected() {
        if (mSocket == null) {
            return false;
        }
        try {
            mSocket.getRemoteDevice();
            mInputStream.available();
            mReader.ready();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void write(byte[] out) throws IOException {
        if (mOutputStream != null) {
            mOutputStream.write(out);
        } else {
            throw new IOException("Bluetooth not ready.");
        }
    }

    public void write(String out) throws IOException {
        this.write(out.getBytes());
    }

    public Boolean readReady() throws IOException {
        if (mReader != null) {
            return mReader.ready();
        }
        throw new IOException("Bluetooth not ready.");
    }

    public byte[] readBinary() throws IOException {
        return this.readBinary(4096);
    }

    public byte[] readBinary(int bufferSize) throws IOException {
        if (mReader != null) {
            byte[] buffer = new byte[bufferSize];
            int bytesRead = mInputStream.read(buffer);
            if (bytesRead == -1) {
                Log.e("Read failed.");
                throw new IOException("Read failed.");
            }
            byte[] truncatedBuffer = new byte[bytesRead];
            System.arraycopy(buffer, 0, truncatedBuffer, 0, bytesRead);
            return truncatedBuffer;
        }

        throw new IOException("Bluetooth not ready.");

    }

    public String read() throws IOException {
        return this.read(4096);
    }

    public String read(int bufferSize) throws IOException {
        if (mReader != null) {
            char[] buffer = new char[bufferSize];
            int bytesRead = mReader.read(buffer);
            if (bytesRead == -1) {
                Log.e("Read failed.");
                throw new IOException("Read failed.");
            }
            return new String(buffer, 0, bytesRead);
        }
        throw new IOException("Bluetooth not ready.");
    }

    public String readLine() throws IOException {
        if (mReader != null) {
            return mReader.readLine();
        }
        throw new IOException("Bluetooth not ready.");
    }

    public String getConnectedDeviceName() {
        return mDevice.getName();
    }

    public void stop() {
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                Log.e(e);
            }
        }
        mSocket = null;
        if (mServerSocket != null) {
            try {
                mServerSocket.close();
            } catch (IOException e) {
                Log.e(e);
            }
        }
        mServerSocket = null;

        if (mInputStream != null) {
            try {
                mInputStream.close();
            } catch (IOException e) {
                Log.e(e);
            }
        }
        mInputStream = null;
        if (mOutputStream != null) {
            try {
                mOutputStream.close();
            } catch (IOException e) {
                Log.e(e);
            }
        }
        mOutputStream = null;
        if (mReader != null) {
            try {
                mReader.close();
            } catch (IOException e) {
                Log.e(e);
            }
        }
        mReader = null;
    }
}
