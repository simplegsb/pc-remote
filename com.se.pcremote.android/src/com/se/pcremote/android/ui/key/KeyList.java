package com.se.pcremote.android.ui.key;

import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.se.pcremote.android.PCRemoteProvider;

/**
 * <p>
 * Displays the list of Keys.
 * </p>
 * 
 * @author simple
 */
public class KeyList extends ListActivity
{
    /**
     * <p>
     * Creates an instance of <code>KeyList</code>.
     * </p>
     */
    public KeyList()
    {}

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setListAdapter(new KeyAdapter(this, managedQuery(PCRemoteProvider.KEY_URI, null, null, null, null), true));

        getListView().setTextFilterEnabled(true);

        // Returns the selected Key to the calling activity.
        getListView().setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id)
            {
                setResult(RESULT_OK, new Intent(null, ContentUris.withAppendedId(PCRemoteProvider.KEY_URI, id)));
                finish();
            }
        });
    }
}
