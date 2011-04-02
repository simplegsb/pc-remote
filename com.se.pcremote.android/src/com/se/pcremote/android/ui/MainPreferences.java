package com.se.pcremote.android.ui;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;

import com.se.pcremote.android.PCRemoteProvider;
import com.se.pcremote.android.R;

/**
 * <p>
 * Displays the general preferences for the PC Remote application.
 * </p>
 * 
 * @author simple
 */
public class MainPreferences extends PreferenceActivity
{
    /**
     * <p>
     * The name of the action that this activity accepts.
     * </p>
     */
    public static final String ACTION_PREFERENCES_MAIN = "com.se.pcremote.android.ui.MainPreferences.ACTION_PREFERENCES_MAIN";

    /**
     * <p>
     * Creates an instance of <code>MainPreferences</code>.
     * </p>
     */
    public MainPreferences()
    {}

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.main_preference_fragment);

        // Save the active Layout.
        getPreferenceScreen().findPreference("activeLayout").setOnPreferenceChangeListener(new OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue)
            {
                PreferenceManager.getDefaultSharedPreferences(MainPreferences.this).edit().putString("activeLayout", (String) newValue).commit();
                return (true);
            }
        });

        // Save the active PC.
        getPreferenceScreen().findPreference("activePc").setOnPreferenceChangeListener(new OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue)
            {
                PreferenceManager.getDefaultSharedPreferences(MainPreferences.this).edit().putString("activePc", (String) newValue).commit();
                return (true);
            }
        });

        // Opens the 'View Layouts' activity.
        getPreferenceScreen().findPreference("manageLayouts").setOnPreferenceClickListener(new OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(final Preference preference)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, PCRemoteProvider.LAYOUT_URI));
                return (true);
            }
        });

        // Opens the 'View PCs' activity.
        getPreferenceScreen().findPreference("managePcs").setOnPreferenceClickListener(new OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(final Preference preference)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, PCRemoteProvider.PC_URI));
                return (true);
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        // Initialise the list of PCs that can be activated.
        ListPreference activePcPreference = (ListPreference) getPreferenceScreen().findPreference("activePc");

        Cursor pcCursor = getContentResolver().query(PCRemoteProvider.PC_URI, null, null, null, null);
        String[] selectPcNames = new String[pcCursor.getCount()];
        String[] selectPcUris = new String[pcCursor.getCount()];

        for (int index = 0; index < selectPcNames.length; index++)
        {
            pcCursor.moveToNext();
            selectPcNames[index] = pcCursor.getString(pcCursor.getColumnIndex(PCRemoteProvider.PC_COLUMN_NAME));
            selectPcUris[index] = ContentUris.withAppendedId(PCRemoteProvider.PC_URI, pcCursor.getInt(pcCursor.getColumnIndex(BaseColumns._ID)))
                    .toString();
        }

        pcCursor.close();

        activePcPreference.setEntries(selectPcNames);
        activePcPreference.setEntryValues(selectPcUris);

        // Initialise the list of Layouts that can be activated.
        ListPreference activeLayoutPreference = (ListPreference) getPreferenceScreen().findPreference("activeLayout");

        Cursor layoutCursor = getContentResolver().query(PCRemoteProvider.LAYOUT_URI, null, null, null, null);
        String[] selectLayoutNames = new String[layoutCursor.getCount()];
        String[] selectLayoutUris = new String[layoutCursor.getCount()];

        for (int index = 0; index < selectLayoutNames.length; index++)
        {
            layoutCursor.moveToNext();
            selectLayoutNames[index] = layoutCursor.getString(layoutCursor.getColumnIndex(PCRemoteProvider.LAYOUT_COLUMN_NAME));
            selectLayoutUris[index] = ContentUris.withAppendedId(PCRemoteProvider.LAYOUT_URI,
                    layoutCursor.getInt(layoutCursor.getColumnIndex(BaseColumns._ID))).toString();
        }

        layoutCursor.close();

        activeLayoutPreference.setEntries(selectLayoutNames);
        activeLayoutPreference.setEntryValues(selectLayoutUris);
    }
}
