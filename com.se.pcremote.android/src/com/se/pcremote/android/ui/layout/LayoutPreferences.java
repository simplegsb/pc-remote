package com.se.pcremote.android.ui.layout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.se.pcremote.android.Key;
import com.se.pcremote.android.Layout;
import com.se.pcremote.android.R;

/**
 * <p>
 * Creates and edits Layouts.
 * </p>
 * 
 * @author Gary Buyn
 */
public class LayoutPreferences extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
    /**
     * <p>
     * The ID of a request for a {@link com.se.pcremote.android.Key Key}.
     * </p>
     */
    public static final int QUERY_KEYS_REQUEST = 0;

    /**
     * <p>
     * The column the button being edited is in.
     * </p>
     */
    private int fButtonGridColumnIndex;

    /**
     * <p>
     * The row the button being edited is in.
     * </p>
     */
    private int fButtonGridRowIndex;

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
        fButtonGridColumnIndex = -1;
        fButtonGridRowIndex = -1;
        fLayout = null;
    }

    @Override
    public void finish()
    {
        savePreferencesToLayout();

        setResult(RESULT_OK, new Intent(null, fLayout.getUri()));

        super.finish();
    }

    /**
     * <p>
     * Retrieves the Layout being created/edited.
     * </p>
     * 
     * @return The Layout being created/edited.
     */
    public Layout getLayout()
    {
        return (fLayout);
    }

    /**
     * <p>
     * Loads the preference values from the {@link com.se.pcremote.android.Layout Layout}.
     * </p>
     */
    private void loadPreferencesFromLayout()
    {
        fLayout = new Layout();
        fLayout.load(this, getIntent().getData());

        Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("layoutButtonGridHeight", String.valueOf(fLayout.getButtonGridHeight()));
        editor.putString("layoutButtonGridMap", fLayout.getButtonGridMap());
        editor.putString("layoutButtonGridWidth", String.valueOf(fLayout.getButtonGridWidth()));
        editor.putBoolean("layoutHasButtonGrid", fLayout.hasButtonGrid());
        editor.putBoolean("layoutHasKeyboardButton", fLayout.hasKeyboardButton());
        editor.putBoolean("layoutHasMouseButtons", fLayout.hasMouseButtons());
        editor.putBoolean("layoutHasMousePad", fLayout.hasMousePad());
        editor.putString("layoutName", fLayout.getName());
        editor.commit();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK)
        {
            if (requestCode == QUERY_KEYS_REQUEST)
            {
                Key key = new Key();
                key.load(this, intent.getData());

                if (key.getId() != 0)
                {
                    // Set/save the button grid map.
                    fLayout.setButtonGridKey(fButtonGridRowIndex, fButtonGridColumnIndex, key);
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putString("layoutButtonGridMap", fLayout.getButtonGridMap()).commit();

                    // Refresh the preferences on-screen.
                    finish();
                    startActivity(getIntent());
                }
            }
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        loadPreferencesFromLayout();
        addPreferencesFromResource(R.xml.layout_preference_fragment);
        setPreferenceSummaries();

        findPreference("layoutButtonGridHeight").setOnPreferenceChangeListener(new OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue)
            {
                // Save the button grid height.
                PreferenceManager.getDefaultSharedPreferences(LayoutPreferences.this).edit().putString("layoutButtonGridHeight", (String) newValue)
                        .commit();

                // Refresh the preferences on-screen.
                finish();
                startActivity(getIntent());

                return (true);
            }
        });
        ((ButtonGridMapPreference) findPreference("layoutButtonGridMap")).setLayoutPreferences(this);
        findPreference("layoutButtonGridWidth").setOnPreferenceChangeListener(new OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue)
            {
                // Save the button grid width.
                PreferenceManager.getDefaultSharedPreferences(LayoutPreferences.this).edit().putString("layoutButtonGridWidth", (String) newValue)
                        .commit();

                // Refresh the preferences on-screen.
                finish();
                startActivity(getIntent());

                return (true);
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        savePreferencesToLayout();

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
     * Saves the preference values to the {@link com.se.pcremote.android.Layout Layout}.
     * </p>
     */
    private void savePreferencesToLayout()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getString("layoutButtonGridHeight", "").length() != 0)
        {
            fLayout.setButtonGridHeight(Integer.parseInt(preferences.getString("layoutButtonGridHeight", "3")));
        }
        fLayout.setButtonGridMap(preferences.getString("layoutButtonGridMap", ""));
        if (preferences.getString("layoutButtonGridWidth", "").length() != 0)
        {
            fLayout.setButtonGridWidth(Integer.parseInt(preferences.getString("layoutButtonGridWidth", "3")));
        }
        fLayout.setHasButtonGrid(preferences.getBoolean("layoutHasButtonGrid", true));
        fLayout.setHasKeyboardButton(preferences.getBoolean("layoutHasKeyboardButton", true));
        fLayout.setHasMouseButtons(preferences.getBoolean("layoutHasMouseButtons", true));
        fLayout.setHasMousePad(preferences.getBoolean("layoutHasMousePad", true));
        fLayout.setName(preferences.getString("layoutName", "New Layout"));

        fLayout.save(this);
    }

    /**
     * <p>
     * Sets the column the button being edited is in.
     * </p>
     * 
     * @param columnIndex The column the button being edited is in.
     */
    public void setButtonGridColumnIndex(final int columnIndex)
    {
        fButtonGridColumnIndex = columnIndex;
    }

    /**
     * <p>
     * Sets the row the button being edited is in.
     * </p>
     * 
     * @param rowIndex The row the button being edited is in.
     */
    public void setButtonGridRowIndex(final int rowIndex)
    {
        fButtonGridRowIndex = rowIndex;
    }

    /**
     * <p>
     * Sets the preference summaries to the values of the preferences.
     * </p>
     */
    private void setPreferenceSummaries()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        findPreference("layoutName").setSummary(preferences.getString("layoutName", null));
        findPreference("layoutButtonGridHeight").setSummary(preferences.getString("layoutButtonGridHeight", null));
        findPreference("layoutButtonGridWidth").setSummary(preferences.getString("layoutButtonGridWidth", null));
    }
}
