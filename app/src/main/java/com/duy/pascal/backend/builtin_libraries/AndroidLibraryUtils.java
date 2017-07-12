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

package com.duy.pascal.backend.builtin_libraries;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.googlecode.sl4a.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Encapsulates the list of supported facades and their construction.
 *
 * @author Damon Kohler (damonkohler@gmail.com)
 * @author Igor Karp (igor.v.karp@gmail.com)
 */
public class AndroidLibraryUtils {

    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }


    public static void putExtrasFromJsonObject(JSONObject extras, Intent intent) throws JSONException {
        JSONArray names = extras.names();
        for (int i = 0; i < names.length(); i++) {
            String name = names.getString(i);
            Object data = extras.get(name);
            if (data == null) {
                continue;
            }
            if (data instanceof Integer) {
                intent.putExtra(name, (Integer) data);
            }
            if (data instanceof Float) {
                intent.putExtra(name, (Float) data);
            }
            if (data instanceof Double) {
                intent.putExtra(name, (Double) data);
            }
            if (data instanceof Long) {
                intent.putExtra(name, (Long) data);
            }
            if (data instanceof String) {
                intent.putExtra(name, (String) data);
            }
            if (data instanceof Boolean) {
                intent.putExtra(name, (Boolean) data);
            }
            // Nested JSONObject
            if (data instanceof JSONObject) {
                Bundle nestedBundle = new Bundle();
                intent.putExtra(name, nestedBundle);
                putNestedJSONObject((JSONObject) data, nestedBundle);
            }
            // Nested JSONArray. Doesn't support mixed types in single array
            if (data instanceof JSONArray) {
                // Empty array. No way to tell what operator of data to pass on, so skipping
                if (((JSONArray) data).length() == 0) {
                    Log.e("Empty array not supported in JSONObject, skipping");
                    continue;
                }
                // Integer
                if (((JSONArray) data).get(0) instanceof Integer) {
                    Integer[] integerArrayData = new Integer[((JSONArray) data).length()];
                    for (int j = 0; j < ((JSONArray) data).length(); ++j) {
                        integerArrayData[j] = ((JSONArray) data).getInt(j);
                    }
                    intent.putExtra(name, integerArrayData);
                }
                // Double
                if (((JSONArray) data).get(0) instanceof Double) {
                    Double[] doubleArrayData = new Double[((JSONArray) data).length()];
                    for (int j = 0; j < ((JSONArray) data).length(); ++j) {
                        doubleArrayData[j] = ((JSONArray) data).getDouble(j);
                    }
                    intent.putExtra(name, doubleArrayData);
                }
                // Long
                if (((JSONArray) data).get(0) instanceof Long) {
                    Long[] longArrayData = new Long[((JSONArray) data).length()];
                    for (int j = 0; j < ((JSONArray) data).length(); ++j) {
                        longArrayData[j] = ((JSONArray) data).getLong(j);
                    }
                    intent.putExtra(name, longArrayData);
                }
                // String
                if (((JSONArray) data).get(0) instanceof String) {
                    String[] stringArrayData = new String[((JSONArray) data).length()];
                    for (int j = 0; j < ((JSONArray) data).length(); ++j) {
                        stringArrayData[j] = ((JSONArray) data).getString(j);
                    }
                    intent.putExtra(name, stringArrayData);
                }
                // Boolean
                if (((JSONArray) data).get(0) instanceof Boolean) {
                    Boolean[] booleanArrayData = new Boolean[((JSONArray) data).length()];
                    for (int j = 0; j < ((JSONArray) data).length(); ++j) {
                        booleanArrayData[j] = ((JSONArray) data).getBoolean(j);
                    }
                    intent.putExtra(name, booleanArrayData);
                }
            }
        }
    }

    // Contributed by Emmanuel T
    // Nested Array handling contributed by Sergey Zelenev
    public static void putNestedJSONObject(JSONObject jsonObject, Bundle bundle)
            throws JSONException {
        JSONArray names = jsonObject.names();
        for (int i = 0; i < names.length(); i++) {
            String name = names.getString(i);
            Object data = jsonObject.get(name);
            if (data == null) {
                continue;
            }
            if (data instanceof Integer) {
                bundle.putInt(name, (Integer) data);
            }
            if (data instanceof Float) {
                bundle.putFloat(name, (Float) data);
            }
            if (data instanceof Double) {
                bundle.putDouble(name, (Double) data);
            }
            if (data instanceof Long) {
                bundle.putLong(name, (Long) data);
            }
            if (data instanceof String) {
                bundle.putString(name, (String) data);
            }
            if (data instanceof Boolean) {
                bundle.putBoolean(name, (Boolean) data);
            }
            // Nested JSONObject
            if (data instanceof JSONObject) {
                Bundle nestedBundle = new Bundle();
                bundle.putBundle(name, nestedBundle);
                putNestedJSONObject((JSONObject) data, nestedBundle);
            }
            // Nested JSONArray. Doesn't support mixed types in single array
            if (data instanceof JSONArray) {
                // Empty array. No way to tell what operator of data to pass on, so skipping
                if (((JSONArray) data).length() == 0) {
                    Log.e("Empty array not supported in nested JSONObject, skipping");
                    continue;
                }
                // Integer
                if (((JSONArray) data).get(0) instanceof Integer) {
                    int[] integerArrayData = new int[((JSONArray) data).length()];
                    for (int j = 0; j < ((JSONArray) data).length(); ++j) {
                        integerArrayData[j] = ((JSONArray) data).getInt(j);
                    }
                    bundle.putIntArray(name, integerArrayData);
                }
                // Double
                if (((JSONArray) data).get(0) instanceof Double) {
                    double[] doubleArrayData = new double[((JSONArray) data).length()];
                    for (int j = 0; j < ((JSONArray) data).length(); ++j) {
                        doubleArrayData[j] = ((JSONArray) data).getDouble(j);
                    }
                    bundle.putDoubleArray(name, doubleArrayData);
                }
                // Long
                if (((JSONArray) data).get(0) instanceof Long) {
                    long[] longArrayData = new long[((JSONArray) data).length()];
                    for (int j = 0; j < ((JSONArray) data).length(); ++j) {
                        longArrayData[j] = ((JSONArray) data).getLong(j);
                    }
                    bundle.putLongArray(name, longArrayData);
                }
                // String
                if (((JSONArray) data).get(0) instanceof String) {
                    String[] stringArrayData = new String[((JSONArray) data).length()];
                    for (int j = 0; j < ((JSONArray) data).length(); ++j) {
                        stringArrayData[j] = ((JSONArray) data).getString(j);
                    }
                    bundle.putStringArray(name, stringArrayData);
                }
                // Boolean
                if (((JSONArray) data).get(0) instanceof Boolean) {
                    boolean[] booleanArrayData = new boolean[((JSONArray) data).length()];
                    for (int j = 0; j < ((JSONArray) data).length(); ++j) {
                        booleanArrayData[j] = ((JSONArray) data).getBoolean(j);
                    }
                    bundle.putBooleanArray(name, booleanArrayData);
                }
            }
        }
    }

}
