package com.se.pcremote.android.ui.key;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.se.pcremote.android.PCRemoteProvider;
import com.se.pcremote.android.R;

/**
 * <p>
 * Exposes Keys to a {@link android.widget.ListView ListView}.
 * </p>
 * 
 * @author Gary Buyn
 */
public class KeyAdapter extends CursorAdapter
{
    /**
     * <p>
     * Creates an instance of <code>KeyAdapter</code>.
     * </p>
     * 
     * @param context Used to access the database.
     * @param cursor The Key data to expose.
     * @param autoRequery Determines if the data will be re-queried after every change.
     */
    public KeyAdapter(final Context context, final Cursor cursor, final boolean autoRequery)
    {
        super(context, cursor, autoRequery);
    }

    @Override
    public final void bindView(final View view, final Context context, final Cursor cursor)
    {
        TextView textView = (TextView) view.findViewById(R.id.key_list_item_text);
        textView.setText(cursor.getString(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_NAME)));

        ImageView imageView = (ImageView) view.findViewById(R.id.key_list_item_image);
        imageView.setImageResource(cursor.getInt(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_IMAGE)));
    }

    @Override
    public final View newView(final Context context, final Cursor cursor, final ViewGroup parent)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.key_list_item, parent, false);
        bindView(view, context, cursor);

        return (view);
    }
}
