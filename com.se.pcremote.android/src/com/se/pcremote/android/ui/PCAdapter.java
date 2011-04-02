package com.se.pcremote.android.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.se.pcremote.android.PCRemoteProvider;
import com.se.pcremote.android.R;

/**
 * <p>
 * Exposes PCs to a {@link android.widget.ListView ListView}.
 * </p>
 * 
 * @author simple
 */
public class PCAdapter extends CursorAdapter
{
    /**
     * <p>
     * Creates an instance of <code>PCAdapter</code>.
     * </p>
     * 
     * @param context Used to access the database.
     * @param cursor The PC data to expose.
     * @param autoRequery Determines if the data will be re-queried after every change.
     */
    public PCAdapter(final Context context, final Cursor cursor, final boolean autoRequery)
    {
        super(context, cursor, autoRequery);
    }

    @Override
    public final void bindView(final View view, final Context context, final Cursor cursor)
    {
        ((TextView) view).setText(cursor.getString(cursor.getColumnIndex(PCRemoteProvider.PC_COLUMN_NAME)));
    }

    @Override
    public final View newView(final Context context, final Cursor cursor, final ViewGroup parent)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.pc_list_item, parent, false);
        bindView(view, context, cursor);

        return (view);
    }
}
