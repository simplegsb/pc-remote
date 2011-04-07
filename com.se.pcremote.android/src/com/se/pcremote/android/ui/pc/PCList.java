package com.se.pcremote.android.ui.pc;

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
 * Displays the list of PCs.
 * </p>
 * 
 * @author simple
 */
public class PCList extends ListActivity
{
    /**
     * <p>
     * Creates an instance of <code>PCList</code>.
     * </p>
     */
    public PCList()
    {}

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getListView().addHeaderView(LayoutInflater.from(this).inflate(R.layout.pc_list_insert, null));
        setListAdapter(new PCAdapter(this, managedQuery(PCRemoteProvider.PC_URI, null, null, null, null), true));

        getListView().setTextFilterEnabled(true);

        // Opens the 'Create/Edit PC' activity.
        getListView().setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id)
            {
                Uri data = null;
                if (id == -1)
                {
                    data = PCRemoteProvider.PC_URI;
                }
                else
                {
                    data = ContentUris.withAppendedId(PCRemoteProvider.PC_URI, id);
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
        getMenuInflater().inflate(R.menu.pc_list_options, menu);

        return (true);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item)
    {
        if (item.getItemId() == R.id.delete_pcs)
        {
            showDialog(DialogFactory.DIALOG_DELETE_PCS_ID);
            return (true);
        }
        else
        {
            return (super.onOptionsItemSelected(item));
        }
    }
}
