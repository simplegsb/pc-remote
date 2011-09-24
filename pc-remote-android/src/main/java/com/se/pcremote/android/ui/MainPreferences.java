package com.se.pcremote.android.ui;

import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;

import com.se.pcremote.android.Layout;
import com.se.pcremote.android.PC;
import com.se.pcremote.android.PCRemoteProvider;
import com.se.pcremote.android.R;

/**
 * <p>
 * Displays the general preferences for the PC Remote application.
 * </p>
 * 
 * @author Gary Buyn
 */
public class MainPreferences extends PreferenceActivity implements OnSharedPreferenceChangeListener
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
        setPreferenceSummaries();

        // Open the 'Report Bug' activity.
        getPreferenceScreen().findPreference("reportBug").setOnPreferenceClickListener(new OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(final Preference preference)
            {
                startActivity(new Intent(MainPreferences.this, ReportBug.class));
                return (true);
            }
        });

        // Open the 'Request Enhancement' activity.
        getPreferenceScreen().findPreference("requestEnhancement").setOnPreferenceClickListener(new OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(final Preference preference)
            {
                startActivity(new Intent(MainPreferences.this, RequestEnhancement.class));
                return (true);
            }
        });

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

        // Open the 'View Layouts' activity.
        getPreferenceScreen().findPreference("manageLayouts").setOnPreferenceClickListener(new OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(final Preference preference)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, PCRemoteProvider.LAYOUT_URI));
                return (true);
            }
        });

        // Open the 'View PCs' activity.
        getPreferenceScreen().findPreference("managePcs").setOnPreferenceClickListener(new OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(final Preference preference)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, PCRemoteProvider.PC_URI));
                return (true);
            }
        });

        // Open the 'Install Server' activity.
        getPreferenceScreen().findPreference("installServer").setOnPreferenceClickListener(new OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(final Preference preference)
            {
                startActivity(new Intent(MainPreferences.this, InstallServer.class));
                return (true);
            }
        });
    }

    @Override
    public void onPause()
    {
        super.onPause();

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key)
    {
        setPreferenceSummaries();
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

    /**
     * <p>
     * Sets the preference summaries to the values of the preferences.
     * </p>
     */
    private void setPreferenceSummaries()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        PC pc = new PC();
        pc.load(this, Uri.parse(preferences.getString("activePc", "")));
        findPreference("activePc").setSummary(pc.getName());

        Layout layout = new Layout();
        layout.load(this, Uri.parse(preferences.getString("activeLayout", "")));
        findPreference("activeLayout").setSummary(layout.getName());

        findPreference("mouseSensitivity").setSummary(preferences.getString("mouseSensitivity", ""));
    }
}
