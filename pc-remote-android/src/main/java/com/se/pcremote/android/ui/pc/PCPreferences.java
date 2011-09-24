package com.se.pcremote.android.ui.pc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.se.pcremote.android.PC;
import com.se.pcremote.android.R;
import com.se.pcremote.server.PCRemoteServer;

/**
 * <p>
 * Creates and edits {@link com.se.pcremote.android.PC PC}s.
 * </p>
 * 
 * @author Gary Buyn
 */
public class PCPreferences extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
    /**
     * <p>
     * The PC being created/edited.
     * </p>
     */
    private PC fPc;

    /**
     * <p>
     * Creates an instance of <code>PCPreferences</code>.
     * </p>
     */
    public PCPreferences()
    {
        fPc = null;
    }

    @Override
    public void finish()
    {
        savePreferencesToPc();

        setResult(RESULT_OK, new Intent(null, fPc.getUri()));

        super.finish();
    }

    /**
     * <p>
     * Loads the preference values from the {@link com.se.pcremote.android.PC PC}.
     * </p>
     */
    private void loadPreferencesFromPc()
    {
        fPc = new PC();
        fPc.load(this, getIntent().getData());

        Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("pcName", fPc.getName());
        editor.putString("pcHost", fPc.getHost());
        editor.putString("pcPort", String.valueOf(fPc.getPort()));

        if (getIntent().hasExtra("host"))
        {
            editor.putString("pcHost", getIntent().getExtras().getString("host"));
        }

        editor.commit();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        loadPreferencesFromPc();
        addPreferencesFromResource(R.xml.pc_preference_fragment);
        setPreferenceSummaries();
    }

    @Override
    protected void onDestroy()
    {
        savePreferencesToPc();

        super.onDestroy();
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

    /**
     * <p>
     * Saves the preference values to the {@link com.se.pcremote.android.PC PC}.
     * </p>
     */
    private void savePreferencesToPc()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        fPc.setName(preferences.getString("pcName", "New PC"));
        fPc.setHost(preferences.getString("pcHost", null));
        if (preferences.getString("pcPort", "").length() != 0)
        {
            fPc.setPort(Integer.parseInt(preferences.getString("pcPort", String.valueOf(PCRemoteServer.DEFAULT_PORT))));
        }

        fPc.save(this);
    }

    /**
     * <p>
     * Sets the preference summaries to the values of the preferences.
     * </p>
     */
    private void setPreferenceSummaries()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        findPreference("pcName").setSummary(preferences.getString("pcName", null));
        findPreference("pcHost").setSummary(preferences.getString("pcHost", null));
        findPreference("pcPort").setSummary(preferences.getString("pcPort", null));
    }
}
