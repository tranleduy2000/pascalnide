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

package com.duy.pascal.interperter.libraries.android.hardware;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.libraries.PascalLibrary;
import com.duy.pascal.interperter.libraries.android.AndroidLibraryManager;
import com.duy.pascal.interperter.libraries.annotations.PascalMethod;
import com.duy.pascal.interperter.libraries.annotations.PascalParameter;
import com.duy.pascal.ui.DLog;
import com.googlecode.sl4a.rpc.RpcStartEvent;

import java.util.Arrays;
import java.util.Map;

/**
 * Exposes the SensorManager related functionality. <br>
 * <br>
 * <b>Guidance notes</b> <br>
 * For reasons of economy the sensors on smart phones are usually low cost and, therefore, low
 * accuracyValue (usually represented by 10 bit data). The floating point data values obtained from
 * sensor readings have up to 16 decimal places, the majority of which are noise. On many phones the
 * accelerometer is limited (by the phone manufacturer) to a maximum reading of 2g. The magnetometer
 * (which also provides orientation readings) is strongly affected by the presence of ferrous metals
 * and can give large errors in vehicles, on board ship etc.
 * <p>
 * Following a startSensor(A,B) api call sensor events are entered into the Event Queue (see
 * EventFacade). For the A parameter: 1 = All Sensors, 2 = Accelerometer, 3 = Magnetometer and 4 =
 * Light. The B parameter is the minimum delay between recordings in milliseconds. To avoid
 * duplicate readings the minimum delay should be 20 milliseconds. The lightValue sensor will probably be
 * much slower (taking about 1 second to register a change in lightValue level). Note that if the lightValue
 * level is constant no sensor events will be registered by the lightValue sensor.
 * <p>
 * Following a startSensingThreshold(A,B,C) api call sensor events greater than a given threshold
 * are entered into the Event Queue. For the A parameter: 1 = Orientation, 2 = Accelerometer, 3 =
 * Magnetometer and 4 = Light. The B parameter is the integer value of the required threshold level.
 * For orientation sensing the integer threshold value is in milliradians. Since orientation events
 * can exceed the threshold value for long periods only crossing and return events are recorded. The
 * C parameter is the required axis (XYZ) of the sensor: 0 = No axis, 1 = X, 2 = Y, 3 = X+Y, 4 = Z,
 * 5= X+Z, 6 = Y+Z, 7 = X+Y+Z. For orientation X = azimuth, Y = pitch and Z = roll. <br>
 * <p>
 * <br>
 * <b>Example (python)</b>
 * <p>
 * <pre>
 * import android, time
 * droid = android.Android()
 * droid.startSensor(1, 250)
 * time.sleep(1)
 * s1 = droid.readSensors().result
 * s2 = droid.getAccuracyValue().result
 * s3 = droid.getLightValue().result
 * s4 = droid.sensorsReadAccelerometer().result
 * s5 = droid.sensorsReadMagnetometer().result
 * s6 = droid.sensorsReadOrientation().result
 * droid.stopSensor()
 * </pre>
 * <p>
 * Returns:<br>
 * s1 = {u'accuracyValue': 3, u'pitch': -0.47323511242866517, u'xmag': 1.75, u'azimuth':
 * -0.26701245009899138, u'zforce': 8.4718560000000007, u'yforce': 4.2495484000000001, u'time':
 * 1297160391.2820001, u'ymag': -8.9375, u'zmag': -41.0625, u'roll': -0.031366908922791481,
 * u'xforce': 0.23154590999999999}<br>
 * s2 = 3 (Highest accuracyValue)<br>
 * s3 = None ---(not available on many phones)<br>
 * s4 = [0.23154590999999999, 4.2495484000000001, 8.4718560000000007] ----(x, y, z accelerations)<br>
 * s5 = [1.75, -8.9375, -41.0625] -----(x, y, z magnetic readings)<br>
 * s6 = [-0.26701245009899138, -0.47323511242866517, -0.031366908922791481] ---(azimuth, pitch, roll
 * in radians)<br>
 *
 * @author Damon Kohler (damonkohler@gmail.com)
 * @author Felix Arends (felix.arends@gmail.com)
 * @author Alexey Reznichenko (alexey.reznichenko@gmail.com)
 * @author Robbie Mathews (rjmatthews62@gmail.com)
 * @author John Karwatzki (jokar49@gmail.com)
 */
public class AndroidSensorLib extends PascalLibrary {
    public static final String NAME = "aSensor".toLowerCase();

    private SensorManager mSensorManager;

    private int accuracyValue;
    private int mSensorNumber;
    private int mXAxis = 0;
    private int mYAxis = 0;
    private int mZAxis = 0;
    private int mThreshing = 0;
    private int mThreshOrientation = 0;

    private int mXCrossed = 0;
    private int mYCrossed = 0;
    private int mZCrossed = 0;

    private double mThreshold;
    private double xAccelerometer = 0d;
    private double yAccelerometer = 0d;
    private double zAccelerometer = 0d;

    private double xMagnetic = 0d;
    private double yMagnetic = 0d;
    private double zMagnetic = 0d;

    private double lightValue;

    private double azimuthValue;
    private double pitchValue;
    private double rollValue;
    private SensorEventListener mSensorListener;
    private double pressureValue;
    private double gravityValue;
    private double humidityValue;
    private double tempValue;
    private long delayTime = 20;

    public AndroidSensorLib(AndroidLibraryManager manager) {
        if (manager.getContext() != null) {
            mSensorManager = (SensorManager) manager.getContext().getSystemService(Context.SENSOR_SERVICE);
        }
    }

    @PascalMethod(description = "Pitch, rotation around x-axis (-180 to 180), with positive values when the z-axis moves toward the y-axis.")
    public double getPitchValue() {
        return pitchValue;
    }

    @PascalMethod(description = "Roll, rotation around the y-axis (-90 to 90) increasing as the device moves clockwise")
    public double getRollValue() {
        return rollValue;
    }

    @PascalMethod(description = "Azimuth, angle between the magnetic north direction and the y-axis, around the z-axis (0 to 359). 0=North, 90=East, 180=South, 270=West")
    public double getAzimuthValue() {
        return azimuthValue;
    }

    @PascalMethod(description = "Proximity sensor distance measured in centimeters")
    public double getGravityValue() {
        return gravityValue;
    }

    @PascalMethod(description = "Starts recording sensor data to be available for polling.")
    public void startSensor(
            @PascalParameter(name = "sensorNumber",
                    description =
                            "1 = All, " +
                                    " 2 = Accelerometer, " +
                                    "3 = Magnetometer, " +
                                    "4 = Light, " +
                                    "5 = Pressure, " +
                                    "6 = Gravity, " +
                                    "7 = Humidity, " +
                                    "8 = Temperature") int sensorNumber) {
        mSensorNumber = sensorNumber;
        if (mSensorListener == null) {
            mSensorListener = new SensorValuesCollector();
            switch (mSensorNumber) {
                case 1:
                    for (Sensor sensor : mSensorManager.getSensorList(Sensor.TYPE_ALL)) {
                        mSensorManager.registerListener(mSensorListener, sensor,
                                SensorManager.SENSOR_DELAY_FASTEST);
                    }
                    break;
                case 2:
                    for (Sensor sensor : mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER)) {
                        mSensorManager.registerListener(mSensorListener, sensor,
                                SensorManager.SENSOR_DELAY_FASTEST);
                    }
                    break;
                case 3:
                    for (Sensor sensor : mSensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD)) {
                        mSensorManager.registerListener(mSensorListener, sensor,
                                SensorManager.SENSOR_DELAY_FASTEST);
                    }
                    break;
                case 4:
                    for (Sensor sensor : mSensorManager.getSensorList(Sensor.TYPE_LIGHT)) {
                        mSensorManager.registerListener(mSensorListener, sensor,
                                SensorManager.SENSOR_DELAY_FASTEST);
                    }
                    break;
                case 5:
                    for (Sensor sensor : mSensorManager.getSensorList(Sensor.TYPE_PRESSURE)) {
                        mSensorManager.registerListener(mSensorListener, sensor,
                                SensorManager.SENSOR_DELAY_FASTEST);
                    }
                    break;
                case 6:
                    for (Sensor sensor : mSensorManager.getSensorList(Sensor.TYPE_GRAVITY)) {
                        mSensorManager.registerListener(mSensorListener, sensor,
                                SensorManager.SENSOR_DELAY_FASTEST);
                    }
                    break;
                case 7:
                    for (Sensor sensor : mSensorManager.getSensorList(Sensor.TYPE_RELATIVE_HUMIDITY)) {
                        mSensorManager.registerListener(mSensorListener, sensor,
                                SensorManager.SENSOR_DELAY_FASTEST);
                    }
                    break;
                case 8:
                    for (Sensor sensor : mSensorManager.getSensorList(Sensor.TYPE_AMBIENT_TEMPERATURE)) {
                        mSensorManager.registerListener(mSensorListener, sensor,
                                SensorManager.SENSOR_DELAY_FASTEST);
                    }
                    break;
            }
        }
    }

    @PascalMethod(description = "Records to the Event Queue sensor data exceeding a chosen threshold.")
    @RpcStartEvent("threshold")
    public void startSensingThreshold(
            @PascalParameter(name = "sensorNumber", description = "1 = Orientation, 2 = Accelerometer, 3 = Magnetometer and 4 = Light") int sensorNumber,
            @PascalParameter(name = "threshold", description = "Threshold level for chosen sensor (integer)") int threshold,
            @PascalParameter(name = "axis", description = "0 = No axis, 1 = X, 2 = Y, 3 = X+Y, 4 = Z, 5= X+Z, 6 = Y+Z, 7 = X+Y+Z") int axis) {
        mSensorNumber = sensorNumber;
        mXAxis = axis & 1;
        mYAxis = axis & 2;
        mZAxis = axis & 4;
        if (mSensorNumber == 1) {
            mThreshing = 0;
            mThreshOrientation = 1;
            mThreshold = ((float) threshold) / ((float) 1000);
        } else {
            mThreshing = 1;
            mThreshold = (float) threshold;
        }
        startSensor(mSensorNumber);
    }

    @PascalMethod(description = "Stops collecting sensor data.")
    public void stopSensor() {
        mSensorManager.unregisterListener(mSensorListener);
        mSensorListener = null;
        mThreshing = 0;
        mThreshOrientation = 0;
    }

    @PascalMethod(description = "Returns the most recently received accuracyValue value.")
    public int getAccuracyValue() {
        return accuracyValue;
    }

    @PascalMethod(description = "Ambient light level in SI lux units")
    public double getLightValue() {
        return lightValue;
    }

    @PascalMethod(description = "Atmospheric pressureValue in hPa (millibar)")
    public double getPressure() {
        return pressureValue;
    }

    @PascalMethod(description = "Starts recording sensor data to be available for polling.")
    public void startAllSensor() {
        if (mSensorListener == null) {
            startSensor(1);
        }
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    @PascalMethod(description = "stop")
    public void shutdown() {
        stopSensor();
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

    @PascalMethod(description = "Return X value of accelerometer sensor")
    public double getXAccelerometer() {
        return xAccelerometer;
    }

    @PascalMethod(description = "Return Y value of accelerometer sensor")
    public double getYAccelerometer() {
        return yAccelerometer;
    }

    @PascalMethod(description = "Return Z value of accelerometer sensor")
    public double getZAccelerometer() {
        return zAccelerometer;
    }

    @PascalMethod(description = "Return X value of magnetic sensor")
    public double getXMagnetic() {
        return xMagnetic;
    }

    @PascalMethod(description = "Return Y value of magnetic sensor")
    public double getYMagnetic() {
        return yMagnetic;
    }

    @PascalMethod(description = "Return Z value of magnetic sensor")
    public double getZMagnetic() {
        return zMagnetic;
    }

    @PascalMethod(description = "Relative ambient air humidity in percent")
    public double getHumidityValue() {
        return humidityValue;
    }

    @PascalMethod(description = "ambient (room) temperature in degree Celsius.")
    public double getTempValue() {
        return tempValue;
    }

    @PascalMethod(description = "Set delay time")
    public void setDelayTime(long ms) {
        delayTime = ms;
    }

    private class RollingAverage {
        private final int mmSampleSize;
        private final double mmData[];
        private int mmIndex = 0;
        private boolean mmFilled = false;
        private double mmSum = 0.0;

        RollingAverage() {
            mmSampleSize = 5;
            mmData = new double[mmSampleSize];
        }

        public void add(double value) {
            mmSum -= mmData[mmIndex];
            mmData[mmIndex] = value;
            mmSum += mmData[mmIndex];
            ++mmIndex;
            mmIndex %= mmSampleSize;
            mmFilled = mmFilled || mmIndex == 0;
        }

        public double get() throws IllegalStateException {
            if (!mmFilled && mmIndex == 0) {
                throw new IllegalStateException("No values to average.");
            }
            return (mmFilled) ? (mmSum / mmSampleSize) : (mmSum / mmIndex);
        }
    }

    private class SensorValuesCollector implements SensorEventListener {
        private final static int MATRIX_SIZE = 9;
        private static final String TAG = "SensorValuesCollector";
        private final RollingAverage mmAzimuth;
        private final RollingAverage mmPitch;
        private final RollingAverage mmRoll;
        private float[] mmGeomagneticValues;
        private float[] mmGravityValues;
        private float[] mmR;
        private float[] mmOrientation;
        private double timestamp;
        private long lastTime = 0;

        SensorValuesCollector() {
            mmAzimuth = new RollingAverage();
            mmPitch = new RollingAverage();
            mmRoll = new RollingAverage();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            AndroidSensorLib.this.accuracyValue = accuracy;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            DLog.d(TAG, "onSensorChanged: " + Arrays.toString(event.values));
            if (System.currentTimeMillis() - lastTime >= delayTime) {
                switch (event.sensor.getType()) {
                    case Sensor.TYPE_ACCELEROMETER:
                        xAccelerometer = event.values[0];
                        yAccelerometer = event.values[1];
                        zAccelerometer = event.values[2];
                        mmGravityValues = event.values.clone();
                        break;
                    case Sensor.TYPE_MAGNETIC_FIELD:
                        xMagnetic = event.values[0];
                        yMagnetic = event.values[1];
                        zMagnetic = event.values[2];
                        mmGeomagneticValues = event.values.clone();
                        break;
                    case Sensor.TYPE_LIGHT:
                        lightValue = event.values[0];
                        break;
                    case Sensor.TYPE_PRESSURE:
                        pressureValue = event.values[0];
                        break;
                    case Sensor.TYPE_GRAVITY:
                        gravityValue = event.values[0];
                        break;
                    case Sensor.TYPE_RELATIVE_HUMIDITY:
                        humidityValue = event.values[0];
                        break;
                    case Sensor.TYPE_AMBIENT_TEMPERATURE:
                        tempValue = event.values[0];
                        break;
                }
                if (mSensorNumber == 1) {
                    if (mmGeomagneticValues != null && mmGravityValues != null) {
                        if (mmR == null) {
                            mmR = new float[MATRIX_SIZE];
                        }
                        if (SensorManager.getRotationMatrix(mmR, null, mmGravityValues, mmGeomagneticValues)) {
                            if (mmOrientation == null) {
                                mmOrientation = new float[3];
                            }
                            SensorManager.getOrientation(mmR, mmOrientation);
                            mmAzimuth.add(mmOrientation[0]);
                            mmPitch.add(mmOrientation[1]);
                            mmRoll.add(mmOrientation[2]);

                            azimuthValue = mmAzimuth.get();
                            pitchValue = mmPitch.get();
                            rollValue = mmRoll.get();
                            if ((mThreshOrientation == 1) && (mSensorNumber == 1)) {
                                if ((mXAxis == 1) && (mXCrossed == 0)) {
                                    if (Math.abs(azimuthValue) > mThreshold) {
                                        mXCrossed = 1;
                                    }
                                }
                                if ((mXAxis == 1) && (mXCrossed == 1)) {
                                    if (Math.abs(azimuthValue) < mThreshold) {
                                        mXCrossed = 0;
                                    }
                                }
                                if ((mYAxis == 2) && (mYCrossed == 0)) {
                                    if (Math.abs(pitchValue) > mThreshold) {
                                        mYCrossed = 1;
                                    }
                                }
                                if ((mYAxis == 2) && (mYCrossed == 1)) {
                                    if (Math.abs(pitchValue) < mThreshold) {
                                        mYCrossed = 0;
                                    }
                                }
                                if ((mZAxis == 4) && (mZCrossed == 0)) {
                                    if (Math.abs(rollValue) > mThreshold) {
                                        mZCrossed = 1;
                                    }
                                }
                                if ((mZAxis == 4) && (mZCrossed == 1)) {
                                    if (Math.abs(rollValue) < mThreshold) {
                                        mZCrossed = 0;
                                    }
                                }
                            }
                        }
                    }
                }
                lastTime = System.currentTimeMillis();
            }
        }
    }
}
