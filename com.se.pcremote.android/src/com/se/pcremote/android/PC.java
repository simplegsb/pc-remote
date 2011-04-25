package com.se.pcremote.android;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.se.pcremote.server.PCRemoteServer;

/**
 * <p>
 * A PC hosting the {@link com.se.pcremote.server.PCRemoteServer PCRemoteServer} that can be controlled by this client.
 * </p>
 * 
 * @author Gary Buyn
 */
public class PC
{
    /**
     * <p>
     * The host on which the {@link com.se.pcremote.server.PCRemoteServer PCRemoteServer} resides.
     * </p>
     */
    private String fHost;

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
     * Determines if this <code>PC</code> was fetched from the content provider or created manually.
     * </p>
     */
    private boolean fNewPc;

    /**
     * <p>
     * The port on which the {@link com.se.pcremote.server.PCRemoteServer PCRemoteServer} is listening.
     * </p>
     */
    private int fPort;

    /**
     * <p>
     * Creates an instance of <code>PC</code>.
     * </p>
     */
    public PC()
    {
        fHost = null;
        fId = 0;
        fName = null;
        fNewPc = true;
        fPort = PCRemoteServer.DEFAULT_PORT;
    }

    /**
     * <p>
     * Deletes this <code>PC</code> from the content provider.
     * </p>
     * 
     * @param context The context from which the content provider can be retrieved.
     */
    public void delete(final Context context)
    {
        ContentResolver contentResolver = context.getContentResolver();

        if (!fNewPc)
        {
            contentResolver.delete(ContentUris.withAppendedId(PCRemoteProvider.PC_URI, fId), null, null);
            contentResolver.notifyChange(ContentUris.withAppendedId(PCRemoteProvider.PC_URI, fId), null);

            fNewPc = true;
        }
    }

    /**
     * <p>
     * Retrieves the host on which the {@link com.se.pcremote.server.PCRemoteServer PCRemoteServer} resides.
     * </p>
     * 
     * @return The host on which the <code>PCRemoteServer</code> resides.
     */
    public String getHost()
    {
        return (fHost);
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
     * Retrieves the port on which the {@link com.se.pcremote.server.PCRemoteServer PCRemoteServer} is listening.
     * </p>
     * 
     * @return The port on which the <code>PCRemoteServer</code> is listening.
     */
    public int getPort()
    {
        return (fPort);
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
        return (ContentUris.withAppendedId(PCRemoteProvider.PC_URI, fId));
    }

    /**
     * <p>
     * Determines if this <code>PC</code> was fetched from the content provider or created manually.
     * </p>
     * 
     * @return True if this <code>PC</code> was created manually, false otherwise.
     */
    public boolean isNewPc()
    {
        return (fNewPc);
    }

    /**
     * <p>
     * Loads this <code>PC</code> from a content provider with the given URI.
     * </p>
     * 
     * @param context The context from which the content provider can be retrieved.
     * @param uri The content provider URI.
     */
    public void load(final Context context, final Uri uri)
    {
        ContentResolver contentResolver = context.getContentResolver();

        int match = PCRemoteProvider.URI_MATCHER.match(uri);
        if (match == PCRemoteProvider.PC_ID)
        {
            Cursor cursor = contentResolver.query(uri, null, null, null, null);

            if (cursor.moveToFirst())
            {
                fNewPc = false;

                fId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
                fName = cursor.getString(cursor.getColumnIndex(PCRemoteProvider.PC_COLUMN_NAME));
                fHost = cursor.getString(cursor.getColumnIndex(PCRemoteProvider.PC_COLUMN_HOST));
                fPort = cursor.getInt(cursor.getColumnIndex(PCRemoteProvider.PC_COLUMN_PORT));
            }

            cursor.close();
        }
    }

    /**
     * <p>
     * Saves this <code>PC</code> to a content provider.
     * </p>
     * 
     * @param context The context from which the content provider can be retrieved.
     */
    public void save(final Context context)
    {
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(PCRemoteProvider.PC_COLUMN_NAME, fName);
        values.put(PCRemoteProvider.PC_COLUMN_HOST, fHost);
        values.put(PCRemoteProvider.PC_COLUMN_PORT, fPort);

        if (fNewPc)
        {
            Uri insertedPc = contentResolver.insert(PCRemoteProvider.PC_URI, values);
            contentResolver.notifyChange(PCRemoteProvider.PC_URI, null);

            fId = Integer.parseInt(insertedPc.getLastPathSegment());
            fNewPc = false;
        }
        else
        {
            contentResolver.update(ContentUris.withAppendedId(PCRemoteProvider.PC_URI, fId), values, null, null);
            contentResolver.notifyChange(ContentUris.withAppendedId(PCRemoteProvider.PC_URI, fId), null);
        }
    }

    /**
     * <p>
     * Sets the host on which the {@link com.se.pcremote.server.PCRemoteServer PCRemoteServer} resides.
     * </p>
     * 
     * @param host The host on which the <code>PCRemoteServer</code> resides.
     */
    public void setHost(final String host)
    {
        fHost = host;
    }

    /**
     * <p>
     * Sets the human-readable name.
     * </p>
     * 
     * @param name The human-readable name.
     */
    public void setName(final String name)
    {
        fName = name;
    }

    /**
     * <p>
     * Sets the port on which the {@link com.se.pcremote.server.PCRemoteServer PCRemoteServer} is listening.
     * </p>
     * 
     * @param port The port on which the <code>PCRemoteServer</code> is listening.
     */
    public void setPort(final int port)
    {
        fPort = port;
    }
}
