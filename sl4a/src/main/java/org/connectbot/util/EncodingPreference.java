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

package org.connectbot.util;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class EncodingPreference extends ListPreference {

    public EncodingPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        List<CharSequence> charsetIdsList = new LinkedList<>();
        List<CharSequence> charsetNamesList = new LinkedList<>();

        for (Entry<String, Charset> entry : Charset.availableCharsets().entrySet()) {
            Charset c = entry.getValue();
            if (c.canEncode() && c.isRegistered()) {
                String key = entry.getKey();
                if (key.startsWith("cp")) {
                    // Custom CP437 charset changes
                    charsetIdsList.add("CP437");
                    charsetNamesList.add("CP437");
                }
                charsetIdsList.add(entry.getKey());
                charsetNamesList.add(c.displayName());
            }
        }

        this.setEntryValues(charsetIdsList.toArray(new CharSequence[charsetIdsList.size()]));
        this.setEntries(charsetNamesList.toArray(new CharSequence[charsetNamesList.size()]));
    }
}
