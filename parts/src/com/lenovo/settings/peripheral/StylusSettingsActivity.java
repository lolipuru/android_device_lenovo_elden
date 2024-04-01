/*
 * Copyright (C) 2024 Paranoid Android
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.lenovo.settings.peripheral;

import android.os.Bundle;

import com.android.settingslib.collapsingtoolbar.CollapsingToolbarBaseActivity;

public class StylusSettingsActivity extends CollapsingToolbarBaseActivity {

    private static final String TAG_STYLUS = "stylus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(com.android.settingslib.collapsingtoolbar.R.id.content_frame,
                new StylusSettingsFragment(), TAG_STYLUS).commit();
    }
}
