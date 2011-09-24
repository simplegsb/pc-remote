package com.se.pcremote.android.ui.layout;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.se.pcremote.android.PCRemoteProvider;
import com.se.pcremote.android.R;
import com.se.pcremote.android.ui.DialogFactory;

/**
 * <p>
 * Displays the list of Layouts.
 * </p>
 * 
 * @author Gary Buyn
 */
public class LayoutList extends ListActivity
{
    /**
     * <p>
     * Creates an instance of <code>LayoutList</code>.
     * </p>
     */
    public LayoutList()
    {}

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getListView().addHeaderView(LayoutInflater.from(this).inflate(R.layout.layout_list_insert, null));
        setListAdapter(new LayoutAdapter(this, managedQuery(PCRemoteProvider.LAYOUT_URI, null, null, null, null), true));

        getListView().setTextFilterEnabled(true);

        // Opens the 'Create/Edit Layout' activity.
        getListView().setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id)
            {
                Uri data = null;
                if (id == -1)
                {
                    data = PCRemoteProvider.LAYOUT_URI;
                }
                else
                {
                    data = ContentUris.withAppendedId(PCRemoteProvider.LAYOUT_URI, id);
                }

                startActivity(new Intent(Intent.ACTION_INSERT_OR_EDIT, data));
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(final int id)
    {
        return (DialogFactory.getInstance().createDialog(this, id));
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {
        getMenuInflater().inflate(R.menu.layout_list_options, menu);

        return (true);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item)
    {
        if (item.getItemId() == R.id.delete_layouts)
        {
            showDialog(DialogFactory.DIALOG_DELETE_LAYOUTS_ID);
            return (true);
        }
        else
        {
            return (super.onOptionsItemSelected(item));
        }
    }
}
