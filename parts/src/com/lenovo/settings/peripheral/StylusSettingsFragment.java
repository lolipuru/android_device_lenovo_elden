/*
 * Copyright (C) 2024 Paranoid Android
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.lenovo.settings.peripheral;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;
import android.util.Log;

import android.preference.PreferenceManager;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceFragment;
import androidx.preference.SwitchPreference;
import com.android.settingslib.widget.MainSwitchPreference;

import com.lenovo.settings.R;

public class StylusSettingsFragment extends PreferenceFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "LenovoPeripheralManagerPenUtils";
    private static final String STYLUS_KEY = "stylus_switch_key";

    private SharedPreferences mStylusPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.stylus_settings);

        mStylusPreference = PreferenceManager.getDefaultSharedPreferences(getContext());
        SwitchPreference switchPreference = (SwitchPreference) findPreference(STYLUS_KEY);

        switchPreference.setChecked(mStylusPreference.getBoolean(STYLUS_KEY, false));
        switchPreference.setEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mStylusPreference.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mStylusPreference.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreference, String key) {
    }
}
