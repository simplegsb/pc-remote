package com.se.pcremote.android;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * <p>
 * Provides content for the PC Remote application.
 * </p>
 * 
 * @author simple
 */
public class PCRemoteProvider extends ContentProvider
{
    /**
     * <p>
     * The base URI for content in this provider.
     * </p>
     */
    public static final Uri CONTENT_URI = Uri.parse("content://com.se.pcremote.android.pcremoteprovider");

    /**
     * <p>
     * The code for a URI that matches one or more layouts.
     * </p>
     */
    public static final int LAYOUT = 2;

    /**
     * <p>
     * The name of the column that contains 'button grid height' for Layouts.
     * </p>
     */
    public static final String LAYOUT_COLUMN_BUTTON_GRID_HEIGHT = "button_grid_height";

    /**
     * <p>
     * The name of the column that contains 'button grid width' for Layouts.
     * </p>
     */
    public static final String LAYOUT_COLUMN_BUTTON_GRID_WIDTH = "button_grid_width";

    /**
     * <p>
     * The name of the column that contains 'has button grid' for Layouts.
     * </p>
     */
    public static final String LAYOUT_COLUMN_HAS_BUTTON_GRID = "has_button_grid";

    /**
     * <p>
     * The name of the column that contains 'has keyboard button' for Layouts.
     * </p>
     */
    public static final String LAYOUT_COLUMN_HAS_KEYBOARD_BUTTON = "has_keyboard_button";

    /**
     * <p>
     * The name of the column that contains 'has button grid' for Layouts.
     * </p>
     */
    public static final String LAYOUT_COLUMN_HAS_MOUSE_BUTTONS = "has_mouse_buttons";

    /**
     * <p>
     * The name of the column that contains 'has mouse pad' for Layouts.
     * </p>
     */
    public static final String LAYOUT_COLUMN_HAS_MOUSE_PAD = "has_mouse_pad";

    /**
     * <p>
     * The name of the column that contains 'names' for Layouts.
     * </p>
     */
    public static final String LAYOUT_COLUMN_NAME = "name";

    /**
     * <p>
     * The code for a URI that matches a layout with a specific ID.
     * </p>
     */
    public static final int LAYOUT_ID = 3;

    /**
     * <p>
     * The name of the table that contains layouts.
     * </p>
     */
    public static final String LAYOUT_TABLE = "layout";

    /**
     * <p>
     * The base URI for layouts in this provider.
     * </p>
     */
    public static final Uri LAYOUT_URI = Uri.parse("content://com.se.pcremote.android.pcremoteprovider/layout");

    /**
     * <p>
     * The code for a URI that matches one or more PCs.
     * </p>
     */
    public static final int PC = 0;

    /**
     * <p>
     * The name of the column that contains 'hosts' for PCs.
     * </p>
     */
    public static final String PC_COLUMN_HOST = "host";

    /**
     * <p>
     * The name of the column that contains 'names' for PCs.
     * </p>
     */
    public static final String PC_COLUMN_NAME = "name";

    /**
     * <p>
     * The name of the column that contains 'ports' for PCs.
     * </p>
     */
    public static final String PC_COLUMN_PORT = "port";

    /**
     * <p>
     * The code for a URI that matches a PC with a specific ID.
     * </p>
     */
    public static final int PC_ID = 1;

    /**
     * <p>
     * The name of the table that contains PCs.
     * </p>
     */
    public static final String PC_TABLE = "pc";

    /**
     * <p>
     * The base URI for PCs in this provider.
     * </p>
     */
    public static final Uri PC_URI = Uri.parse("content://com.se.pcremote.android.pcremoteprovider/pc");

    /**
     * <p>
     * Matches URIs against the data types provided by this <code>PCRemoteProvider</code>. The resultsof matching a URI can be compared against the
     * codes provided by this <code>PCRemoteProvider</code>.
     * </p>
     */
    public static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    /**
     * <p>
     * Initialises the URI matcher.
     * </p>
     */
    static
    {
        URI_MATCHER.addURI("com.se.pcremote.android.pcremoteprovider", PC_TABLE, PC);
        URI_MATCHER.addURI("com.se.pcremote.android.pcremoteprovider", PC_TABLE + "/#", PC_ID);
        URI_MATCHER.addURI("com.se.pcremote.android.pcremoteprovider", LAYOUT_TABLE, LAYOUT);
        URI_MATCHER.addURI("com.se.pcremote.android.pcremoteprovider", LAYOUT_TABLE + "/#", LAYOUT_ID);
    }

    /**
     * <p>
     * Creates and manages the database used by this <code>PCRemoteProvider</code>.
     * </p>
     */
    private PCRemoteOpenHelper fOpenHelper;

    /**
     * <p>
     * Creates an instance of <code>PCRemoteProvider</code>.
     * </p>
     */
    public PCRemoteProvider()
    {
        fOpenHelper = null;
    }

    @Override
    public final synchronized int delete(final Uri uri, final String selection, final String[] selectionArgs)
    {
        int affectedRows = 0;

        int match = URI_MATCHER.match(uri);
        if (match == PC)
        {
            SQLiteDatabase database = fOpenHelper.getWritableDatabase();
            affectedRows = database.delete(PC_TABLE, selection, selectionArgs);
        }
        else if (match == PC_ID)
        {
            SQLiteDatabase database = fOpenHelper.getWritableDatabase();
            affectedRows = database.delete(PC_TABLE, BaseColumns._ID + " = " + uri.getLastPathSegment(), null);
        }
        else if (match == LAYOUT)
        {
            SQLiteDatabase database = fOpenHelper.getWritableDatabase();
            affectedRows = database.delete(LAYOUT_TABLE, selection, selectionArgs);
        }
        else if (match == LAYOUT_ID)
        {
            SQLiteDatabase database = fOpenHelper.getWritableDatabase();
            affectedRows = database.delete(LAYOUT_TABLE, BaseColumns._ID + " = " + uri.getLastPathSegment(), null);
        }

        return (affectedRows);
    }

    @Override
    public final String getType(final Uri uri)
    {
        String type = null;

        int match = URI_MATCHER.match(uri);
        if (match == PC)
        {
            type = "vnd.android.cursor.dir/com.se.pcremote.pc";
        }
        else if (match == PC_ID)
        {
            type = "vnd.android.cursor.item/com.se.pcremote.pc";
        }
        else if (match == LAYOUT)
        {
            type = "vnd.android.cursor.dir/com.se.pcremote.layout";
        }
        else if (match == LAYOUT_ID)
        {
            type = "vnd.android.cursor.item/com.se.pcremote.layout";
        }

        return (type);
    }

    @Override
    public final Uri insert(final Uri uri, final ContentValues values)
    {
        Uri insertedData = null;

        int match = URI_MATCHER.match(uri);
        if (match == PC)
        {
            SQLiteDatabase database = fOpenHelper.getWritableDatabase();
            long id = database.insert(PC_TABLE, null, values);
            insertedData = ContentUris.withAppendedId(PC_URI, id);
        }
        else if (match == LAYOUT)
        {
            SQLiteDatabase database = fOpenHelper.getWritableDatabase();
            long id = database.insert(LAYOUT_TABLE, null, values);
            insertedData = ContentUris.withAppendedId(LAYOUT_URI, id);
        }

        return (insertedData);
    }

    @Override
    public final boolean onCreate()
    {
        fOpenHelper = new PCRemoteOpenHelper(getContext());

        return (true);
    }

    @Override
    public final Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder)
    {
        Cursor cursor = null;

        if (URI_MATCHER.match(uri) == PC)
        {
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            queryBuilder.setTables(PC_TABLE);
            cursor = queryBuilder.query(fOpenHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        else if (URI_MATCHER.match(uri) == PC_ID)
        {
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            queryBuilder.setTables(PC_TABLE);
            queryBuilder.appendWhere(BaseColumns._ID + " = " + uri.getLastPathSegment());
            cursor = queryBuilder.query(fOpenHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        else if (URI_MATCHER.match(uri) == LAYOUT)
        {
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            queryBuilder.setTables(LAYOUT_TABLE);
            cursor = queryBuilder.query(fOpenHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        else if (URI_MATCHER.match(uri) == LAYOUT_ID)
        {
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            queryBuilder.setTables(LAYOUT_TABLE);
            queryBuilder.appendWhere(BaseColumns._ID + " = " + uri.getLastPathSegment());
            cursor = queryBuilder.query(fOpenHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return (cursor);
    }

    @Override
    public final synchronized int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs)
    {
        int affectedRows = 0;

        int match = URI_MATCHER.match(uri);
        if (match == PC)
        {
            SQLiteDatabase database = fOpenHelper.getWritableDatabase();
            affectedRows = database.update(PC_TABLE, values, selection, selectionArgs);
        }
        else if (match == PC_ID)
        {
            String selectionWithId = null;
            if (selection == null)
            {
                selectionWithId = BaseColumns._ID + " = " + uri.getLastPathSegment();
            }
            else
            {
                selectionWithId = "(" + selectionWithId + ") and " + BaseColumns._ID + " = " + uri.getLastPathSegment();
            }

            SQLiteDatabase database = fOpenHelper.getWritableDatabase();
            affectedRows = database.update(PC_TABLE, values, selectionWithId, selectionArgs);
        }
        else if (match == LAYOUT)
        {
            SQLiteDatabase database = fOpenHelper.getWritableDatabase();
            affectedRows = database.update(LAYOUT_TABLE, values, selection, selectionArgs);
        }
        else if (match == LAYOUT_ID)
        {
            String selectionWithId = null;
            if (selection == null)
            {
                selectionWithId = BaseColumns._ID + " = " + uri.getLastPathSegment();
            }
            else
            {
                selectionWithId = "(" + selectionWithId + ") and " + BaseColumns._ID + " = " + uri.getLastPathSegment();
            }

            SQLiteDatabase database = fOpenHelper.getWritableDatabase();
            affectedRows = database.update(LAYOUT_TABLE, values, selectionWithId, selectionArgs);
        }

        return (affectedRows);
    }
}
