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
 * Exposes Layouts to a {@link android.widget.ListView ListView}.
 * </p>
 * 
 * @author simple
 */
public class LayoutAdapter extends CursorAdapter
{
    /**
     * <p>
     * Creates an instance of <code>LayoutAdapter</code>.
     * </p>
     * 
     * @param context Used to access the database.
     * @param cursor The PC data to expose.
     * @param autoRequery Determines if the data will be re-queried after every change.
     */
    public LayoutAdapter(final Context context, final Cursor cursor, final boolean autoRequery)
    {
        super(context, cursor, autoRequery);
    }

    @Override
    public final void bindView(final View view, final Context context, final Cursor cursor)
    {
        ((TextView) view).setText(cursor.getString(cursor.getColumnIndex(PCRemoteProvider.LAYOUT_COLUMN_NAME)));
    }

    @Override
    public final View newView(final Context context, final Cursor cursor, final ViewGroup parent)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_list_item, parent, false);
        bindView(view, context, cursor);

        return (view);
    }
}
