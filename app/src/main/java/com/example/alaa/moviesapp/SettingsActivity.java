package com.example.alaa.moviesapp;

import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;

public class SettingsActivity extends PreferenceActivity  implements Preference.OnPreferenceChangeListener {

   // ActionBarHelperBase mActionBarHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

       // mActionBarHelper.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_movie);

     //   Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
       // myToolbar.setTitle("Settings");
     //  setActionBar();

        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_sortOrder_key)));


    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {

        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
        return true;
    }
}
