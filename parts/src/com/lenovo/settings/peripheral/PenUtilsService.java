/*
 * Copyright (C) 2024 Paranoid Android
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.lenovo.settings.peripheral;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.hardware.input.InputManager;
import android.hardware.input.InputManager.InputDeviceListener;
import android.os.IBinder;
import android.util.Log;
import android.view.InputDevice;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.preference.PreferenceManager;

public class PenUtilsService extends Service {

    private static final String TAG = "LenovoPartsPenUtilsService";
    private static final boolean DEBUG = false;

    private static final String STYLUS_KEY = "stylus_switch_key";
    private static final String PEN_MODE_NODE = "/proc/support_pen";

    private static boolean mIsPenModeEnabled;
    private static boolean mIsPenModeForced;

    private static InputManager mInputManager;
    private static SharedPreferences mSharedPrefs;

    @Override
    public void onCreate() {
        super.onCreate();
        if (DEBUG) Log.d(TAG, "Creating service");
        mInputManager = (InputManager) getSystemService(Context.INPUT_SERVICE);
        mInputManager.registerInputDeviceListener(mInputDeviceListener, null);
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPrefs.registerOnSharedPreferenceChangeListener(mSharedPrefsListener);
        mIsPenModeForced = mSharedPrefs.getBoolean(STYLUS_KEY, false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (DEBUG) Log.d(TAG, "onStartCommand");
        refreshPenMode();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (DEBUG) Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void setValue(String path, String value) {
        if (path == null)
            return;

        File file = new File(path);
        if (file.exists() && file.canWrite()) {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(value.getBytes());
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updatePenMode() {
        Log.d(TAG, "refreshPenMode: " + mIsPenModeEnabled);
        setValue(PEN_MODE_NODE, mIsPenModeEnabled ? "1" : "0");
    }

    private void refreshPenMode() {
        if (mIsPenModeForced) {
            if (DEBUG) Log.d(TAG, "refreshPenMode: Pen Mode forced");
            if (!mIsPenModeEnabled) {
                mIsPenModeEnabled = true;
                updatePenMode();
            }
            return;
        }
        for (int id : mInputManager.getInputDeviceIds()) {
            if (isDeviceLenovoPen(id)) {
                if (DEBUG) Log.d(TAG, "refreshPenMode: Found Lenovo Pen");
                if (!mIsPenModeEnabled) {
                    mIsPenModeEnabled = true;
                    updatePenMode();
                }
                return;
            }
        }
        if (DEBUG) Log.d(TAG, "refreshPenMode: No Lenovo Pen found");
        if (mIsPenModeEnabled) {
            mIsPenModeEnabled = false;
            updatePenMode();
        }
    }

    private boolean isDeviceLenovoPen(int id) {
        InputDevice inputDevice = mInputManager.getInputDevice(id);
        return inputDevice.getVendorId() == 6127 && inputDevice.getProductId() == 24959;
    }

    private InputDeviceListener mInputDeviceListener = new InputDeviceListener() {
        @Override
        public void onInputDeviceAdded(int id) {
            refreshPenMode();
        }
        @Override
        public void onInputDeviceRemoved(int id) {
            refreshPenMode();
        }
        @Override
        public void onInputDeviceChanged(int id) {
            refreshPenMode();
        }
    };
    
    private OnSharedPreferenceChangeListener mSharedPrefsListener = new OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            if (key.equals(STYLUS_KEY)) {
                mIsPenModeForced = prefs.getBoolean(STYLUS_KEY, false);
                refreshPenMode();
            }
        }
    };

}
