package com.se.pcremote.android;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * <p>
 * A Layout of the controls that can be used to control a PC.
 * </p>
 * 
 * @author simple
 */
public class Layout
{
    /**
     * <p>
     * The default size of the button grid (both width and height).
     * </p>
     */
    private static final int DEFATULT_BUTTON_GRID_SIZE = 3;

    /**
     * <p>
     * The height of the button grid.
     * </p>
     */
    private int fButtonGridHeight;

    /**
     * <p>
     * The width of the button grid.
     * </p>
     */
    private int fButtonGridWidth;

    /**
     * <p>
     * Determines if this <code>Layout</code> displays a button grid.
     * </p>
     */
    private boolean fHasButtonGrid;

    /**
     * <p>
     * Determines if this <code>Layout</code> displays a keyboard button.
     * </p>
     */
    private boolean fHasKeyboardButton;

    /**
     * <p>
     * Determines if this <code>Layout</code> displays mouse buttons.
     * </p>
     */
    private boolean fHasMouseButtons;

    /**
     * <p>
     * Determines if this <code>Layout</code> displays a mouse pad.
     * </p>
     */
    private boolean fHasMousePad;

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
     * Determines if this <code>Layout</code> was fetched from the content provider or created manually.
     * </p>
     */
    private boolean fNewLayout;

    /**
     * <p>
     * Creates an instance of <code>Layout</code>.
     * </p>
     */
    public Layout()
    {
        fButtonGridHeight = DEFATULT_BUTTON_GRID_SIZE;
        fButtonGridWidth = DEFATULT_BUTTON_GRID_SIZE;
        fHasButtonGrid = true;
        fHasKeyboardButton = true;
        fHasMouseButtons = true;
        fHasMousePad = true;
        fId = 0;
        fName = null;
        fNewLayout = true;
    }

    /**
     * <p>
     * Deletes this <code>Layout</code> from the content provider.
     * </p>
     * 
     * @param context The context from which the content provider can be retrieved.
     */
    public void delete(final Context context)
    {
        ContentResolver contentResolver = context.getContentResolver();

        if (!fNewLayout)
        {
            contentResolver.delete(ContentUris.withAppendedId(PCRemoteProvider.LAYOUT_URI, fId), null, null);
            contentResolver.notifyChange(ContentUris.withAppendedId(PCRemoteProvider.LAYOUT_URI, fId), null);

            fNewLayout = true;
        }
    }

    /**
     * <p>
     * Retrieves the height of the button grid.
     * </p>
     * 
     * @return The height of the button grid.
     */
    public int getButtonGridHeight()
    {
        return (fButtonGridHeight);
    }

    /**
     * <p>
     * Retrieves the width of the button grid.
     * </p>
     * 
     * @return The width of the button grid.
     */
    public int getButtonGridWidth()
    {
        return (fButtonGridWidth);
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
        return (ContentUris.withAppendedId(PCRemoteProvider.LAYOUT_URI, fId));
    }

    /**
     * <p>
     * Determines if this <code>Layout</code> displays a button grid.
     * </p>
     * 
     * @return True if this <code>Layout</code> displays a button grid, false otherwise.
     */
    public boolean hasButtonGrid()
    {
        return (fHasButtonGrid);
    }

    /**
     * <p>
     * Determines if this <code>Layout</code> displays a keyboard button.
     * </p>
     * 
     * @return True if this <code>Layout</code> displays a keyboard button, false otherwise.
     */
    public boolean hasKeyboardButton()
    {
        return (fHasKeyboardButton);
    }

    /**
     * <p>
     * Determines if this <code>Layout</code> displays mouse buttons.
     * </p>
     * 
     * @return True if this <code>Layout</code> displays mouse buttons, false otherwise.
     */
    public boolean hasMouseButtons()
    {
        return (fHasMouseButtons);
    }

    /**
     * <p>
     * Determines if this <code>Layout</code> displays a mouse pad.
     * </p>
     * 
     * @return True if this <code>Layout</code> displays a mouse pad, false otherwise.
     */
    public boolean hasMousePad()
    {
        return (fHasMousePad);
    }

    /**
     * <p>
     * Determines if this <code>Layout</code> was fetched from the content provider or created manually.
     * </p>
     * 
     * @return True if this <code>Layout</code> was created manually, false otherwise.
     */
    public boolean isNewLayout()
    {
        return (fNewLayout);
    }

    /**
     * <p>
     * Loads this <code>Layout</code> from a content provider with the given URI.
     * </p>
     * 
     * @param context The context from which the content provider can be retrieved.
     * @param uri The content provider URI.
     */
    public void load(final Context context, final Uri uri)
    {
        ContentResolver contentResolver = context.getContentResolver();

        int match = PCRemoteProvider.URI_MATCHER.match(uri);
        if (match == PCRemoteProvider.LAYOUT_ID)
        {
            Cursor cursor = contentResolver.query(uri, null, null, null, null);

            if (cursor.moveToFirst())
            {
                fNewLayout = false;

                fButtonGridHeight = cursor.getInt(cursor.getColumnIndex(PCRemoteProvider.LAYOUT_COLUMN_BUTTON_GRID_HEIGHT));
                fButtonGridWidth = cursor.getInt(cursor.getColumnIndex(PCRemoteProvider.LAYOUT_COLUMN_BUTTON_GRID_WIDTH));
                fHasButtonGrid = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PCRemoteProvider.LAYOUT_COLUMN_HAS_BUTTON_GRID)));
                fHasKeyboardButton = Boolean
                        .parseBoolean(cursor.getString(cursor.getColumnIndex(PCRemoteProvider.LAYOUT_COLUMN_HAS_KEYBOARD_BUTTON)));
                fHasMouseButtons = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PCRemoteProvider.LAYOUT_COLUMN_HAS_MOUSE_BUTTONS)));
                fHasMousePad = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PCRemoteProvider.LAYOUT_COLUMN_HAS_MOUSE_PAD)));
                fId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
                fName = cursor.getString(cursor.getColumnIndex(PCRemoteProvider.LAYOUT_COLUMN_NAME));
            }

            cursor.close();
        }
    }

    /**
     * <p>
     * Saves this <code>Layout</code> to a content provider.
     * </p>
     * 
     * @param context The context from which the content provider can be retrieved.
     */
    public void save(final Context context)
    {
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(PCRemoteProvider.LAYOUT_COLUMN_BUTTON_GRID_HEIGHT, fButtonGridHeight);
        values.put(PCRemoteProvider.LAYOUT_COLUMN_BUTTON_GRID_WIDTH, fButtonGridWidth);
        values.put(PCRemoteProvider.LAYOUT_COLUMN_HAS_BUTTON_GRID, String.valueOf(fHasButtonGrid));
        values.put(PCRemoteProvider.LAYOUT_COLUMN_HAS_KEYBOARD_BUTTON, String.valueOf(fHasKeyboardButton));
        values.put(PCRemoteProvider.LAYOUT_COLUMN_HAS_MOUSE_BUTTONS, String.valueOf(fHasMouseButtons));
        values.put(PCRemoteProvider.LAYOUT_COLUMN_HAS_MOUSE_PAD, String.valueOf(fHasMousePad));
        values.put(PCRemoteProvider.LAYOUT_COLUMN_NAME, fName);

        if (fNewLayout)
        {
            Uri insertedLayout = contentResolver.insert(PCRemoteProvider.LAYOUT_URI, values);
            contentResolver.notifyChange(PCRemoteProvider.LAYOUT_URI, null);

            fId = Integer.parseInt(insertedLayout.getLastPathSegment());
            fNewLayout = false;
        }
        else
        {
            contentResolver.update(ContentUris.withAppendedId(PCRemoteProvider.LAYOUT_URI, fId), values, null, null);
            contentResolver.notifyChange(ContentUris.withAppendedId(PCRemoteProvider.LAYOUT_URI, fId), null);
        }
    }

    /**
     * <p>
     * Sets the height of the button grid.
     * </p>
     * 
     * @param buttonGridHeight The height of the button grid.
     */
    public void setButtonGridHeight(final int buttonGridHeight)
    {
        fButtonGridHeight = buttonGridHeight;
    }

    /**
     * <p>
     * Sets the width of the button grid.
     * </p>
     * 
     * @param buttonGridWidth The width of the button grid.
     */
    public void setButtonGridWidth(final int buttonGridWidth)
    {
        fButtonGridWidth = buttonGridWidth;
    }

    /**
     * <p>
     * Sets the button grid display status of this <code>Layout</code>.
     * </p>
     * 
     * @param hasButtonGrid Determines if this <code>Layout</code> displays a button grid.
     */
    public void setHasButtonGrid(final boolean hasButtonGrid)
    {
        fHasButtonGrid = hasButtonGrid;
    }

    /**
     * <p>
     * Sets the keyboard button display status of this <code>Layout</code>.
     * </p>
     * 
     * @param hasKeyboardButton Determines if this <code>Layout</code> displays a keyboard button.
     */
    public void setHasKeyboardButton(final boolean hasKeyboardButton)
    {
        fHasKeyboardButton = hasKeyboardButton;
    }

    /**
     * <p>
     * Sets the mouse button display status of this <code>Layout</code>.
     * </p>
     * 
     * @param hasMouseButtons Determines if this <code>Layout</code> displays mouse buttons.
     */
    public void setHasMouseButtons(final boolean hasMouseButtons)
    {
        fHasMouseButtons = hasMouseButtons;
    }

    /**
     * <p>
     * Sets the mouse pad display status of this <code>Layout</code>.
     * </p>
     * 
     * @param hasMousePad Determines if this <code>Layout</code> displays a mouse pad.
     */
    public void setHasMousePad(final boolean hasMousePad)
    {
        fHasMousePad = hasMousePad;
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
}
