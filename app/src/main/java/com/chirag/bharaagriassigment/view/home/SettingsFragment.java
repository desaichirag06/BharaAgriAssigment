package com.chirag.bharaagriassigment.view.home;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.chirag.bharaagriassigment.R;


public class SettingsFragment extends PreferenceFragmentCompat {


    public SettingsFragment() {
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

    }

}