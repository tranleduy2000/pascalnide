package com.duy.pascal.frontend.setting;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.duy.pascal.frontend.R;

import static com.duy.pascal.frontend.setting.SettingsActivity.bindPreferenceSummaryToValue;

public class FragmentSetting extends PreferenceFragment {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_editor);

        // Bind the summaries of EditText/List/Dialog/Ringtone preferences
        // to their values. When their values change, their summaries are
        // updated to reflect the new value, per the Android Design
        // guidelines.
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_pref_font_size)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_pref_font_size)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_code_theme)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_pref_font)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_pref_lang)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_console_font_size)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_console_frame_rate)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_console_max_buffer_size)));
    }
}
