package com.se.pcremote.android.ui;

import android.app.Dialog;
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
 * Creates and edits PCs.
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
        try
        {
            // Load the PC details from the preferences.
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            fPc.setName(preferences.getString("pcName", "New PC"));
            fPc.setHost(preferences.getString("pcHost", null));
            fPc.setPort(Integer.parseInt(preferences.getString("pcPort", String.valueOf(PCRemoteServer.DEFAULT_PORT))));

            fPc.save(this);

            setResult(RESULT_OK, new Intent(null, fPc.getUri()));

            super.finish();
        }
        catch (NumberFormatException e)
        {
            showDialog(DialogFactory.DIALOG_INVALID_PORT_ID);
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        fPc = new PC();
        fPc.load(this, getIntent().getData());

        // Save the PC details to the preferences.
        Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("pcName", fPc.getName());
        editor.putString("pcHost", fPc.getHost());
        editor.putString("pcPort", String.valueOf(fPc.getPort()));
        editor.commit();

        addPreferencesFromResource(R.xml.pc_preference_fragment);
    }

    @Override
    protected Dialog onCreateDialog(final int id)
    {
        return (DialogFactory.getInstance().createDialog(this, DialogFactory.DIALOG_INVALID_PORT_ID));
    }
}
