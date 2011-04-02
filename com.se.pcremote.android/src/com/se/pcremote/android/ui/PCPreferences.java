package com.se.pcremote.android.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
 * @author simple
 */
public class PCPreferences extends PreferenceActivity
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
        editor.commit();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        loadPreferencesFromPc();

        addPreferencesFromResource(R.xml.pc_preference_fragment);
    }

    @Override
    protected void onDestroy()
    {
        savePreferencesToPc();

        super.onDestroy();
    }

    /**
     * <p>
     * Saves the preference values to the {@link com.se.pcremote.android.PC PC}.
     * </p>
     */
    public void savePreferencesToPc()
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
}
