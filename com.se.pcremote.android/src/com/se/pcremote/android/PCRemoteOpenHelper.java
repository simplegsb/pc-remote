package com.se.pcremote.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * <p>
 * A helper class to create and manage the PC Remote database.
 * </p>
 * 
 * @author simple
 */
public class PCRemoteOpenHelper extends SQLiteOpenHelper
{
    /**
     * <p>
     * The name of the PC Remote database.
     * </p>
     */
    public static final String DATABASE_NAME = "com.se.pcremote.android.provider.PCRemote";

    /**
     * <p>
     * The version of the PC Remote database.
     * </p>
     */
    public static final int DATABASE_VERSION = 1;

    /**
     * <p>
     * Creates an instance of <code>PCRemoteOpenHelper</code>.
     * </p>
     * 
     * @param context Used to create or open the database.
     */
    public PCRemoteOpenHelper(final Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db)
    {
        // Create PC table.
        db.execSQL("create table " + PCRemoteProvider.PC_TABLE + " (" + BaseColumns._ID + " integer primary key autoincrement, "
                + PCRemoteProvider.PC_COLUMN_NAME + " text, " + PCRemoteProvider.PC_COLUMN_HOST + " text, " + PCRemoteProvider.PC_COLUMN_PORT
                + " integer)");

        // Create Layout table.
        db.execSQL("create table " + PCRemoteProvider.LAYOUT_TABLE + " (" + BaseColumns._ID + " integer primary key autoincrement, "
                + PCRemoteProvider.LAYOUT_COLUMN_BUTTON_GRID_HEIGHT + " integer, " + PCRemoteProvider.LAYOUT_COLUMN_BUTTON_GRID_WIDTH + " integer, "
                + PCRemoteProvider.LAYOUT_COLUMN_HAS_BUTTON_GRID + " text, " + PCRemoteProvider.LAYOUT_COLUMN_HAS_KEYBOARD_BUTTON + " text, "
                + PCRemoteProvider.LAYOUT_COLUMN_HAS_MOUSE_BUTTONS + " text, " + PCRemoteProvider.LAYOUT_COLUMN_HAS_MOUSE_PAD + " text, "
                + PCRemoteProvider.LAYOUT_COLUMN_NAME + " text)");

        // Create 'Standard Layout'.
        db.execSQL("insert into " + PCRemoteProvider.LAYOUT_TABLE + " (" + PCRemoteProvider.LAYOUT_COLUMN_BUTTON_GRID_HEIGHT + ", "
                + PCRemoteProvider.LAYOUT_COLUMN_BUTTON_GRID_WIDTH + ", " + PCRemoteProvider.LAYOUT_COLUMN_HAS_BUTTON_GRID + ", "
                + PCRemoteProvider.LAYOUT_COLUMN_HAS_KEYBOARD_BUTTON + ", " + PCRemoteProvider.LAYOUT_COLUMN_HAS_MOUSE_BUTTONS + ", "
                + PCRemoteProvider.LAYOUT_COLUMN_HAS_MOUSE_PAD + ", " + PCRemoteProvider.LAYOUT_COLUMN_NAME
                + ") values (3, 3, 'true', 'true', 'true', 'true', 'Standard Layout')");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion)
    {}
}
