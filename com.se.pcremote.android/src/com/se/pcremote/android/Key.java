package com.se.pcremote.android;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * <p>
 * A Key that can be included in the button grid of a Layout.
 * </p>
 * 
 * @author simple
 */
public class Key
{
    /**
     * <p>
     * The code that is required by the {com.se.pcremote.server.PCRemoteServer PCRemoteServer}.
     * </p>
     */
    private int fCode;

    /**
     * <p>
     * A unique identifier.
     * </p>
     */
    private int fId;

    /**
     * <p>
     * The human-readable name.
     * </p>
     */
    private String fName;

    /**
     * <p>
     * Creates an instance of <code>Key</code>.
     * </p>
     */
    public Key()
    {
        fCode = -1;
        fId = 0;
        fName = null;
    }

    /**
     * <p>
     * Retrieves the code that is required by the {com.se.pcremote.server.PCRemoteServer PCRemoteServer}.
     * </p>
     * 
     * @return The code that is required by the <code>PCRemoteServer</code>.
     */
    public int getCode()
    {
        return (fCode);
    }

    /**
     * <p>
     * Retrieves the unique identifier.
     * </p>
     * 
     * @return The unique identifier.
     */
    public int getId()
    {
        return (fId);
    }

    /**
     * <p>
     * Retrieves the human-readable name.
     * </p>
     * 
     * @return The human-readable name.
     */
    public String getName()
    {
        return (fName);
    }

    /**
     * <p>
     * Retrieves the content provider URI.
     * </p>
     * 
     * @return The content provider URI.
     */
    public Uri getUri()
    {
        return (ContentUris.withAppendedId(PCRemoteProvider.KEY_URI, fId));
    }

    /**
     * <p>
     * Loads this <code>Key</code> from a content provider with the given URI.
     * </p>
     * 
     * @param context The context from which the content provider can be retrieved.
     * @param uri The content provider URI.
     */
    public void load(final Context context, final Uri uri)
    {
        ContentResolver contentResolver = context.getContentResolver();

        int match = PCRemoteProvider.URI_MATCHER.match(uri);
        if (match == PCRemoteProvider.KEY_ID)
        {
            Cursor cursor = contentResolver.query(uri, null, null, null, null);

            if (cursor.moveToFirst())
            {
                fCode = cursor.getInt(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_CODE));
                fId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
                fName = cursor.getString(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_NAME));
            }

            cursor.close();
        }
    }
}
