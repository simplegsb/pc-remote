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
     * The code that is provided by an android event.
     * </p>
     */
    private int fAndroidCode;

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
     * 
     * <p>
     * The code that is required by the {com.se.pcremote.server.PCRemoteServer PCRemoteServer}.
     * </p>
     */
    private int fServerCode;

    /**
     * <p>
     * Creates an instance of <code>Key</code>.
     * </p>
     */
    public Key()
    {
        fAndroidCode = -1;
        fId = 0;
        fName = null;
        fServerCode = -1;
    }

    /**
     * <p>
     * Retrieves the code that is provided by an android event.
     * </p>
     * 
     * @return The code that is provided by an android event.
     */
    public int getAndroidCode()
    {
        return (fAndroidCode);
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
     * Retrieves the code that is required by the {com.se.pcremote.server.PCRemoteServer PCRemoteServer}.
     * </p>
     * 
     * @return The code that is required by the <code>PCRemoteServer</code>.
     */
    public int getServerCode()
    {
        return (fServerCode);
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
     * Loads this <code>Key</code> from a content provider with the given android key code.
     * </p>
     * 
     * @param context The context from which the content provider can be retrieved.
     * @param androidCode The android key code.
     */
    public void load(final Context context, final int androidCode)
    {
        ContentResolver contentResolver = context.getContentResolver();

        Cursor cursor = contentResolver.query(PCRemoteProvider.KEY_URI, null, PCRemoteProvider.KEY_COLUMN_ANDROID_CODE + " = " + androidCode, null,
                null);

        if (cursor.moveToFirst())
        {
            fAndroidCode = cursor.getInt(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_ANDROID_CODE));
            fId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
            fName = cursor.getString(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_NAME));
            fServerCode = cursor.getInt(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_SERVER_CODE));
        }

        cursor.close();
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
                fAndroidCode = cursor.getInt(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_ANDROID_CODE));
                fId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
                fName = cursor.getString(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_NAME));
                fServerCode = cursor.getInt(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_SERVER_CODE));
            }

            cursor.close();
        }
    }
}
