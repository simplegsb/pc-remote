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
     * The name to display for the 'Null' Key.
     * </p>
     */
    public static final String NULL_NAME = "<None>";

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
     * The ID of the image resource to display the button.
     * </p>
     */
    private int fImageResourceId;

    /**
     * 
     * <p>
     * Determines whether the ALT key must be pressed by the IME while sending the key.
     * </p>
     */
    private boolean fImeAltRequired;

    /**
     * 
     * <p>
     * Determines whether the shift key must be pressed by the IME while sending the key.
     * </p>
     */
    private boolean fImeShiftRequired;
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
     * 
     * <p>
     * Determines whether the shift key must be pressed at the server while sending the key.
     * </p>
     */
    private boolean fServerShiftRequired;

    /**
     * <p>
     * Creates an instance of <code>Key</code>.
     * </p>
     */
    public Key()
    {
        fAndroidCode = -1;
        fId = 0;
        fImageResourceId = -1;
        fImeAltRequired = false;
        fImeShiftRequired = false;
        fName = null;
        fServerCode = -1;
        fServerShiftRequired = false;
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
     * Retrieves the ID of the image resource to display the button.
     * </p>
     * 
     * @return The ID of the image resource to display the button.
     */
    public int getImageResourceId()
    {
        return (fImageResourceId);
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
     * Determines whether the ALT key must be pressed by the IME while sending the key.
     * </p>
     * 
     * @return True if the ALT key must be pressed by the IME while sending the key, false otherwise.
     */
    public boolean isImeAltRequired()
    {
        return (fImeAltRequired);
    }

    /**
     * <p>
     * Determines whether the shift key must be pressed by the IME while sending the key.
     * </p>
     * 
     * @return True if the shift key must be pressed by the IME while sending the key, false otherwise.
     */
    public boolean isImeShiftRequired()
    {
        return (fImeShiftRequired);
    }

    /**
     * <p>
     * Determines whether the shift key must be pressed at the server while sending the key.
     * </p>
     * 
     * @return True if the shift key must be pressed at the server while sending the key, false otherwise.
     */
    public boolean isServerShiftRequired()
    {
        return (fServerShiftRequired);
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
                fImageResourceId = cursor.getInt(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_IMAGE));
                fImeAltRequired = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_IME_ALT_REQUIRED)));
                fImeShiftRequired = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_IME_SHIFT_REQUIRED)));
                fName = cursor.getString(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_NAME));
                fServerCode = cursor.getInt(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_SERVER_CODE));
                fServerShiftRequired = Boolean
                        .parseBoolean(cursor.getString(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_SERVER_SHIFT_REQUIRED)));
            }

            cursor.close();
        }
    }

    /**
     * <p>
     * Loads this <code>Key</code> from a content provider with the given android key code.
     * </p>
     * 
     * @param context The context from which the content provider can be retrieved.
     * @param androidKeyCode The android key code.
     * @param imeAltRequired Determines whether the ALT key must be pressed by the IME while sending the key.
     * @param imeShiftRequired Determines whether the shift key must be pressed by the IME while sending the key.
     */
    public void loadFromAndroidKeyCode(final Context context, final int androidKeyCode, final boolean imeAltRequired, final boolean imeShiftRequired)
    {
        ContentResolver contentResolver = context.getContentResolver();

        Cursor cursor = contentResolver.query(PCRemoteProvider.KEY_URI, null, PCRemoteProvider.KEY_COLUMN_ANDROID_CODE + " = " + androidKeyCode
                + " and " + PCRemoteProvider.KEY_COLUMN_IME_ALT_REQUIRED + " = '" + imeAltRequired + "'" + " and "
                + PCRemoteProvider.KEY_COLUMN_IME_SHIFT_REQUIRED + " = '" + imeShiftRequired + "'", null, null);

        if (cursor.moveToFirst())
        {
            fAndroidCode = cursor.getInt(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_ANDROID_CODE));
            fId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
            fImageResourceId = cursor.getInt(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_IMAGE));
            fImeAltRequired = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_IME_ALT_REQUIRED)));
            fImeShiftRequired = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_IME_SHIFT_REQUIRED)));
            fName = cursor.getString(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_NAME));
            fServerCode = cursor.getInt(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_SERVER_CODE));
            fServerShiftRequired = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_SERVER_SHIFT_REQUIRED)));
        }

        cursor.close();
    }

    /**
     * <p>
     * Loads this <code>Key</code> from a content provider with the given unique identifier.
     * </p>
     * 
     * @param context The context from which the content provider can be retrieved.
     * @param id The unique identifier.
     */
    public void loadFromId(final Context context, final int id)
    {
        ContentResolver contentResolver = context.getContentResolver();

        Cursor cursor = contentResolver.query(PCRemoteProvider.KEY_URI, null, BaseColumns._ID + " = " + id, null, null);

        if (cursor.moveToFirst())
        {
            fAndroidCode = cursor.getInt(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_ANDROID_CODE));
            fId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
            fImageResourceId = cursor.getInt(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_IMAGE));
            fImeAltRequired = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_IME_ALT_REQUIRED)));
            fImeShiftRequired = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_IME_SHIFT_REQUIRED)));
            fName = cursor.getString(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_NAME));
            fServerCode = cursor.getInt(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_SERVER_CODE));
            fServerShiftRequired = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PCRemoteProvider.KEY_COLUMN_SERVER_SHIFT_REQUIRED)));
        }

        cursor.close();
    }
}
