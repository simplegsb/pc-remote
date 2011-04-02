package com.se.pcremote.android.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.se.pcremote.android.Layout;
import com.se.pcremote.android.R;

/**
 * <p>
 * Creates and edits Layouts.
 * </p>
 * 
 * @author simple
 */
public class LayoutPreferences extends PreferenceActivity
{
    /**
     * <p>
     * The Layout being created/edited.
     * </p>
     */
    private Layout fLayout;

    /**
     * <p>
     * Creates an instance of <code>LayoutPreferences</code>.
     * </p>
     */
    public LayoutPreferences()
    {
        fLayout = null;
    }

    @Override
    public void finish()
    {
        // Load the Layout details from the preferences.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        fLayout.setButtonGridHeight(Integer.parseInt(preferences.getString("layoutButtonGridHeight", "3")));
        fLayout.setButtonGridWidth(Integer.parseInt(preferences.getString("layoutButtonGridWidth", "3")));
        fLayout.setHasButtonGrid(preferences.getBoolean("layoutHasButtonGrid", true));
        fLayout.setHasKeyboardButton(preferences.getBoolean("layoutHasKeyboardButton", true));
        fLayout.setHasMouseButtons(preferences.getBoolean("layoutHasMouseButtons", true));
        fLayout.setHasMousePad(preferences.getBoolean("layoutHasMousePad", true));
        fLayout.setName(preferences.getString("layoutName", "New Layout"));

        fLayout.save(this);

        setResult(RESULT_OK, new Intent(null, fLayout.getUri()));

        super.finish();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        fLayout = new Layout();
        fLayout.load(this, getIntent().getData());

        // Save the Layout details to the preferences.
        Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("layoutButtonGridHeight", String.valueOf(fLayout.getButtonGridHeight()));
        editor.putString("layoutButtonGridWidth", String.valueOf(fLayout.getButtonGridWidth()));
        editor.putBoolean("layoutHasButtonGrid", fLayout.hasButtonGrid());
        editor.putBoolean("layoutHasKeyboardButton", fLayout.hasKeyboardButton());
        editor.putBoolean("layoutHasMouseButtons", fLayout.hasMouseButtons());
        editor.putBoolean("layoutHasMousePad", fLayout.hasMousePad());
        editor.putString("layoutName", fLayout.getName());
        editor.commit();

        addPreferencesFromResource(R.xml.layout_preference_fragment);
    }
}
