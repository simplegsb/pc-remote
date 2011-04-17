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
                + PCRemoteProvider.PC_COLUMN_HOST + " text, " + PCRemoteProvider.PC_COLUMN_NAME + " text, " + PCRemoteProvider.PC_COLUMN_PORT
                + " integer)");

        // TODO remove test PC.
        db.execSQL("insert into " + PCRemoteProvider.PC_TABLE + " (" + PCRemoteProvider.PC_COLUMN_HOST + "," + PCRemoteProvider.PC_COLUMN_NAME + ","
                + PCRemoteProvider.PC_COLUMN_PORT + ") values ('192.168.1.9', 'Scimitar', 10999)");

        // Create Layout table.
        db.execSQL("create table " + PCRemoteProvider.LAYOUT_TABLE + " (" + BaseColumns._ID + " integer primary key autoincrement, "
                + PCRemoteProvider.LAYOUT_COLUMN_BUTTON_GRID_HEIGHT + " integer, " + PCRemoteProvider.LAYOUT_COLUMN_BUTTON_GRID_MAP + " text, "
                + PCRemoteProvider.LAYOUT_COLUMN_BUTTON_GRID_WIDTH + " integer, " + PCRemoteProvider.LAYOUT_COLUMN_HAS_BUTTON_GRID + " text, "
                + PCRemoteProvider.LAYOUT_COLUMN_HAS_KEYBOARD_BUTTON + " text, " + PCRemoteProvider.LAYOUT_COLUMN_HAS_MOUSE_BUTTONS + " text, "
                + PCRemoteProvider.LAYOUT_COLUMN_HAS_MOUSE_PAD + " text, " + PCRemoteProvider.LAYOUT_COLUMN_NAME + " text)");

        // Create Layouts.
        String layoutInsertInto = "insert into " + PCRemoteProvider.LAYOUT_TABLE + " (" + PCRemoteProvider.LAYOUT_COLUMN_BUTTON_GRID_HEIGHT + ","
                + PCRemoteProvider.LAYOUT_COLUMN_BUTTON_GRID_MAP + "," + PCRemoteProvider.LAYOUT_COLUMN_BUTTON_GRID_WIDTH + ","
                + PCRemoteProvider.LAYOUT_COLUMN_HAS_BUTTON_GRID + "," + PCRemoteProvider.LAYOUT_COLUMN_HAS_KEYBOARD_BUTTON + ","
                + PCRemoteProvider.LAYOUT_COLUMN_HAS_MOUSE_BUTTONS + "," + PCRemoteProvider.LAYOUT_COLUMN_HAS_MOUSE_PAD + ","
                + PCRemoteProvider.LAYOUT_COLUMN_NAME + ")";
        // 'Standard' Layout.
        db.execSQL(layoutInsertInto
                + " values (3, '0:0:16,0:1:4,0:2:18,1:0:2,1:1:15,1:2:3,2:0:12,2:1:1,2:2:45', 3, 'true', 'true', 'true', 'true', 'Standard')");
        //db.execSQL(layoutInsertInto + " values (2, '0:0:9,0:1:10,0:2:5,1:0:8,1:1:7,1:2:6', 3, 'true', 'true', 'true', 'true', 'Media')");
        db.execSQL(layoutInsertInto + " values (3, null, 3, 'false', 'false', 'true', 'true', 'Mouse Only')");

        // Create Key table.
        db.execSQL("create table " + PCRemoteProvider.KEY_TABLE + " (" + BaseColumns._ID + " integer primary key autoincrement, "
                + PCRemoteProvider.KEY_COLUMN_ANDROID_CODE + " integer, " + PCRemoteProvider.KEY_COLUMN_IMAGE + " text, "
                + PCRemoteProvider.KEY_COLUMN_IME_ALT_REQUIRED + " text, " + PCRemoteProvider.KEY_COLUMN_IME_SHIFT_REQUIRED + " text, "
                + PCRemoteProvider.KEY_COLUMN_NAME + " text, " + PCRemoteProvider.KEY_COLUMN_SERVER_CODE + " integer, "
                + PCRemoteProvider.KEY_COLUMN_SERVER_SHIFT_REQUIRED + " text)");

        // Create Keys.
        String keyInsertInto = "insert into " + PCRemoteProvider.KEY_TABLE + " (" + PCRemoteProvider.KEY_COLUMN_ANDROID_CODE + ","
                + PCRemoteProvider.KEY_COLUMN_IMAGE + "," + PCRemoteProvider.KEY_COLUMN_IME_ALT_REQUIRED + ","
                + PCRemoteProvider.KEY_COLUMN_IME_SHIFT_REQUIRED + "," + PCRemoteProvider.KEY_COLUMN_NAME + ","
                + PCRemoteProvider.KEY_COLUMN_SERVER_CODE + "," + PCRemoteProvider.KEY_COLUMN_SERVER_SHIFT_REQUIRED + ")";
        // Directional Keys.
        db.execSQL(keyInsertInto + " values (20, 'image.png', 'false', 'false', 'Down', 40, 'false')");
        db.execSQL(keyInsertInto + " values (21, 'image.png', 'false', 'false', 'Left', 37, 'false')");
        db.execSQL(keyInsertInto + " values (22, 'image.png', 'false', 'false', 'Right', 39, 'false')");
        db.execSQL(keyInsertInto + " values (19, 'image.png', 'false', 'false', 'Up', 38, 'false')");

        // Multimedia Keys.
        db.execSQL(keyInsertInto + " values (90, 'image.png', 'false', 'false', 'Fast Forward', -1, 'false')"); // TODO
        db.execSQL(keyInsertInto + " values (87, 'image.png', 'false', 'false', 'Next', -1, 'false')"); // TODO
        db.execSQL(keyInsertInto + " values (85, 'image.png', 'false', 'false', 'Play/Pause', 19, 'false')"); // TODO
        db.execSQL(keyInsertInto + " values (88, 'image.png', 'false', 'false', 'Previous', -1, 'false')"); // TODO
        db.execSQL(keyInsertInto + " values (89, 'image.png', 'false', 'false', 'Rewind', -1, 'false')"); // TODO
        db.execSQL(keyInsertInto + " values (86, 'image.png', 'false', 'false', 'Stop', 65480, 'false')"); // TODO

        // Functional Keys.
        db.execSQL(keyInsertInto + " values (57, 'image.png', 'false', 'false', 'Alt', 18, 'false')");
        db.execSQL(keyInsertInto + " values (67, 'image.png', 'false', 'false', 'Backspace', 8, 'false')");
        db.execSQL(keyInsertInto + " values (-1, 'image.png', 'false', 'false', 'Caps', 20, 'false')");
        db.execSQL(keyInsertInto + " values (-1, 'image.png', 'false', 'false', 'Del', 127, 'false')");
        db.execSQL(keyInsertInto + " values (66, 'image.png', 'false', 'false', 'Enter', 10, 'false')");
        db.execSQL(keyInsertInto + " values (-1, 'image.png', 'false', 'false', 'Esc', 27, 'false')");
        db.execSQL(keyInsertInto + " values (59, 'image.png', 'false', 'false', 'Shift', 16, 'false')");
        db.execSQL(keyInsertInto + " values (-1, 'image.png', 'false', 'false', 'Tab', 9, 'false')");

        // Android Keyboard ABC (lower case).
        db.execSQL(keyInsertInto + " values (29, 'image.png', 'false', 'false', 'a', 65, 'false')");
        db.execSQL(keyInsertInto + " values (30, 'image.png', 'false', 'false', 'b', 66, 'false')");
        db.execSQL(keyInsertInto + " values (31, 'image.png', 'false', 'false', 'c', 67, 'false')");
        db.execSQL(keyInsertInto + " values (32, 'image.png', 'false', 'false', 'd', 68, 'false')");
        db.execSQL(keyInsertInto + " values (33, 'image.png', 'false', 'false', 'e', 69, 'false')");
        db.execSQL(keyInsertInto + " values (34, 'image.png', 'false', 'false', 'f', 70, 'false')");
        db.execSQL(keyInsertInto + " values (35, 'image.png', 'false', 'false', 'g', 71, 'false')");
        db.execSQL(keyInsertInto + " values (36, 'image.png', 'false', 'false', 'h', 72, 'false')");
        db.execSQL(keyInsertInto + " values (37, 'image.png', 'false', 'false', 'i', 73, 'false')");
        db.execSQL(keyInsertInto + " values (38, 'image.png', 'false', 'false', 'j', 74, 'false')");
        db.execSQL(keyInsertInto + " values (39, 'image.png', 'false', 'false', 'k', 75, 'false')");
        db.execSQL(keyInsertInto + " values (40, 'image.png', 'false', 'false', 'l', 76, 'false')");
        db.execSQL(keyInsertInto + " values (41, 'image.png', 'false', 'false', 'm', 77, 'false')");
        db.execSQL(keyInsertInto + " values (42, 'image.png', 'false', 'false', 'n', 78, 'false')");
        db.execSQL(keyInsertInto + " values (43, 'image.png', 'false', 'false', 'o', 79, 'false')");
        db.execSQL(keyInsertInto + " values (44, 'image.png', 'false', 'false', 'p', 80, 'false')");
        db.execSQL(keyInsertInto + " values (45, 'image.png', 'false', 'false', 'q', 81, 'false')");
        db.execSQL(keyInsertInto + " values (46, 'image.png', 'false', 'false', 'r', 82, 'false')");
        db.execSQL(keyInsertInto + " values (47, 'image.png', 'false', 'false', 's', 83, 'false')");
        db.execSQL(keyInsertInto + " values (48, 'image.png', 'false', 'false', 't', 84, 'false')");
        db.execSQL(keyInsertInto + " values (49, 'image.png', 'false', 'false', 'u', 85, 'false')");
        db.execSQL(keyInsertInto + " values (50, 'image.png', 'false', 'false', 'v', 86, 'false')");
        db.execSQL(keyInsertInto + " values (51, 'image.png', 'false', 'false', 'w', 87, 'false')");
        db.execSQL(keyInsertInto + " values (52, 'image.png', 'false', 'false', 'x', 88, 'false')");
        db.execSQL(keyInsertInto + " values (53, 'image.png', 'false', 'false', 'y', 89, 'false')");
        db.execSQL(keyInsertInto + " values (54, 'image.png', 'false', 'false', 'z', 90, 'false')");
        db.execSQL(keyInsertInto + " values (62, 'image.png', 'false', 'false', 'Space', 32, 'false')");

        // Android Keyboard ABC (upper case).
        db.execSQL(keyInsertInto + " values (29, 'image.png', 'false', 'true', 'A', 65, 'true')");
        db.execSQL(keyInsertInto + " values (30, 'image.png', 'false', 'true', 'B', 66, 'true')");
        db.execSQL(keyInsertInto + " values (31, 'image.png', 'false', 'true', 'C', 67, 'true')");
        db.execSQL(keyInsertInto + " values (32, 'image.png', 'false', 'true', 'D', 68, 'true')");
        db.execSQL(keyInsertInto + " values (33, 'image.png', 'false', 'true', 'E', 69, 'true')");
        db.execSQL(keyInsertInto + " values (34, 'image.png', 'false', 'true', 'F', 70, 'true')");
        db.execSQL(keyInsertInto + " values (35, 'image.png', 'false', 'true', 'G', 71, 'true')");
        db.execSQL(keyInsertInto + " values (36, 'image.png', 'false', 'true', 'H', 72, 'true')");
        db.execSQL(keyInsertInto + " values (37, 'image.png', 'false', 'true', 'I', 73, 'true')");
        db.execSQL(keyInsertInto + " values (38, 'image.png', 'false', 'true', 'J', 74, 'true')");
        db.execSQL(keyInsertInto + " values (39, 'image.png', 'false', 'true', 'K', 75, 'true')");
        db.execSQL(keyInsertInto + " values (40, 'image.png', 'false', 'true', 'L', 76, 'true')");
        db.execSQL(keyInsertInto + " values (41, 'image.png', 'false', 'true', 'M', 77, 'true')");
        db.execSQL(keyInsertInto + " values (42, 'image.png', 'false', 'true', 'N', 78, 'true')");
        db.execSQL(keyInsertInto + " values (43, 'image.png', 'false', 'true', 'O', 79, 'true')");
        db.execSQL(keyInsertInto + " values (44, 'image.png', 'false', 'true', 'P', 80, 'true')");
        db.execSQL(keyInsertInto + " values (45, 'image.png', 'false', 'true', 'Q', 81, 'true')");
        db.execSQL(keyInsertInto + " values (46, 'image.png', 'false', 'true', 'R', 82, 'true')");
        db.execSQL(keyInsertInto + " values (47, 'image.png', 'false', 'true', 'S', 83, 'true')");
        db.execSQL(keyInsertInto + " values (48, 'image.png', 'false', 'true', 'T', 84, 'true')");
        db.execSQL(keyInsertInto + " values (49, 'image.png', 'false', 'true', 'U', 85, 'true')");
        db.execSQL(keyInsertInto + " values (50, 'image.png', 'false', 'true', 'V', 86, 'true')");
        db.execSQL(keyInsertInto + " values (51, 'image.png', 'false', 'true', 'W', 87, 'true')");
        db.execSQL(keyInsertInto + " values (52, 'image.png', 'false', 'true', 'X', 88, 'true')");
        db.execSQL(keyInsertInto + " values (53, 'image.png', 'false', 'true', 'Y', 89, 'true')");
        db.execSQL(keyInsertInto + " values (54, 'image.png', 'false', 'true', 'Z', 90, 'true')");

        // Android Keyboard 123 (1/4).
        db.execSQL(keyInsertInto + " values (7, 'image.png', 'false', 'false', '0', 48, 'false')");
        db.execSQL(keyInsertInto + " values (8, 'image.png', 'false', 'false', '1', 49, 'false')");
        db.execSQL(keyInsertInto + " values (9, 'image.png', 'false', 'false', '2', 50, 'false')");
        db.execSQL(keyInsertInto + " values (10, 'image.png', 'false', 'false', '3', 51, 'false')");
        db.execSQL(keyInsertInto + " values (11, 'image.png', 'false', 'false', '4', 52, 'false')");
        db.execSQL(keyInsertInto + " values (12, 'image.png', 'false', 'false', '5', 53, 'false')");
        db.execSQL(keyInsertInto + " values (13, 'image.png', 'false', 'false', '6', 54, 'false')");
        db.execSQL(keyInsertInto + " values (14, 'image.png', 'false', 'false', '7', 55, 'false')");
        db.execSQL(keyInsertInto + " values (15, 'image.png', 'false', 'false', '8', 56, 'false')");
        db.execSQL(keyInsertInto + " values (16, 'image.png', 'false', 'false', '9', 57, 'false')");
        db.execSQL(keyInsertInto + " values (56, 'image.png', 'false', 'false', '.', 46, 'false')");
        db.execSQL(keyInsertInto + " values (55, 'image.png', 'false', 'false', ',', 44, 'false')");
        db.execSQL(keyInsertInto + " values (69, 'image.png', 'false', 'false', '-', 45, 'false')");
        // Java constant for '!' prints '1', need to use shift.
        db.execSQL(keyInsertInto + " values (8, 'image.png', 'false', 'true', '!', 517, 'true')");
        // No Java constant for '?', need to use shift.
        db.execSQL(keyInsertInto + " values (76, 'image.png', 'false', 'true', '?', 47, 'true')");
        // Java constant for '@' prints '2', need to use shift.
        db.execSQL(keyInsertInto + " values (77, 'image.png', 'false', 'false', '@', 512, 'true')");
        // Java constant for '~' (131?) prints '+', using shift (with the button to the left of 1 (96)) changes the insert mode.
        db.execSQL(keyInsertInto + " values (68, 'image.png', 'false', 'true', '~', -1, 'false')");
        // Java constant for '_' prints '-', need to use shift.
        db.execSQL(keyInsertInto + " values (69, 'image.png', 'false', 'true', '_', 523, 'true')");
        // Java constant for '+' prints '=', need to use shift.
        db.execSQL(keyInsertInto + " values (81, 'image.png', 'false', 'false', '+', 521, 'true')");
        db.execSQL(keyInsertInto + " values (76, 'image.png', 'false', 'false', '/', 47, 'false')");
        // Java constant for '&' prints '7', need to use shift.
        db.execSQL(keyInsertInto + " values (14, 'image.png', 'false', 'true', '&', 150, 'true')");
        db.execSQL(keyInsertInto + " values (75, 'image.png', 'false', 'true', '\"', 152, 'true')");
        db.execSQL(keyInsertInto + " values (75, 'image.png', 'false', 'false', '''', 152, 'false')");
        db.execSQL(keyInsertInto + " values (74, 'image.png', 'false', 'false', ';', 59, 'false')");
        // No Java constant for '^', need to use shift.
        db.execSQL(keyInsertInto + " values (13, 'image.png', 'false', 'true', '^', 54, 'true')");

        // Android Keyboard 123 (2/4).
        // No Java constant for '|', need to use shift.
        db.execSQL(keyInsertInto + " values (73, 'image.png', 'false', 'true', '|', 92, 'true')");
        // Java constant for ':' prints ';', need to use shift.
        db.execSQL(keyInsertInto + " values (56, 'image.png', 'false', 'true', ':', 513, 'true')");
        db.execSQL(keyInsertInto + " values (16, 'image.png', 'false', 'true', '(', 519, 'false')");
        db.execSQL(keyInsertInto + " values (7, 'image.png', 'false', 'true', ')', 522, 'false')");
        db.execSQL(keyInsertInto + " values (30, 'image.png', 'true', 'false', '<', 153, 'false')");
        // Java constant for '>' prints '.', need to use shift.
        db.execSQL(keyInsertInto + " values (42, 'image.png', 'true', 'false', '>', 160, 'true')");
        // No Java constant for '{', need to use shift.
        db.execSQL(keyInsertInto + " values (71, 'image.png', 'false', 'true', '{', 91, 'true')");
        // No Java constant for '}', need to use shift.
        db.execSQL(keyInsertInto + " values (72, 'image.png', 'false', 'true', '}', 93, 'true')");
        db.execSQL(keyInsertInto + " values (71, 'image.png', 'false', 'false', '[', 91, 'false')");
        db.execSQL(keyInsertInto + " values (72, 'image.png', 'false', 'false', ']', 93, 'false')");
        db.execSQL(keyInsertInto + " values (70, 'image.png', 'false', 'false', '=', 61, 'false')");
        // Not available in USA layout.
        db.execSQL(keyInsertInto + " values (46, 'image.png', 'false', 'false', '€', -1, 'false')");
        // Java constant for '$' prints '4', need to use shift.
        db.execSQL(keyInsertInto + " values (11, 'image.png', 'false', 'true', '$', 515, 'true')");
        // Not available in USA layout.
        db.execSQL(keyInsertInto + " values (12, 'image.png', 'false', 'false', '£', -1, 'false')");
        // Not available in USA layout.
        db.execSQL(keyInsertInto + " values (53, 'image.png', 'false', 'false', '§', -1, 'false')");
        // No Java constant for '%', need to use shift.
        db.execSQL(keyInsertInto + " values (12, 'image.png', 'false', 'true', '%', 53, 'true')");
        // Not available in USA layout.
        db.execSQL(keyInsertInto + " values (34, 'image.png', 'false', 'true', '¥', -1, 'false')");
        // Java constant for '*' (151) prints '8', need to use shift.
        db.execSQL(keyInsertInto + " values (17, 'image.png', 'false', 'false', '*', 56, 'true')");
        // Not available in USA layout.
        db.execSQL(keyInsertInto + " values (53, 'image.png', 'false', 'false', '₩', -1, 'false')");
        // No Java constant for '#', need to use shift.
        db.execSQL(keyInsertInto + " values (18, 'image.png', 'false', 'false', '#', 51, 'true')");
        // Not available in USA layout.
        db.execSQL(keyInsertInto + " values (53, 'image.png', 'false', 'false', '¿', -1, 'false')");
        // Not available in USA layout.
        db.execSQL(keyInsertInto + " values (53, 'image.png', 'false', 'false', '¡', -1, 'false')");
        // Not available in USA layout.
        db.execSQL(keyInsertInto + " values (53, 'image.png', 'false', 'false', '¤', -1, 'false')");
        // Not available in USA layout.
        db.execSQL(keyInsertInto + " values (53, 'image.png', 'false', 'false', '¢', -1, 'false')");
        db.execSQL(keyInsertInto + " values (73, 'image.png', 'false', 'false', '\\', 92, 'false')");

        // Android Keyboard 123 (3/4).
        // Not available in USA layout.
        db.execSQL(keyInsertInto + " values (53, 'image.png', 'false', 'false', '«', -1, 'false')");
        // Not available in USA layout.
        db.execSQL(keyInsertInto + " values (53, 'image.png', 'false', 'false', '»', -1, 'false')");
        // Not available in USA layout.
        db.execSQL(keyInsertInto + " values (53, 'image.png', 'false', 'false', '®', -1, 'false')");
        // Not available in USA layout.
        db.execSQL(keyInsertInto + " values (53, 'image.png', 'false', 'false', '©', -1, 'false')");
        // Not available in USA layout.
        db.execSQL(keyInsertInto + " values (53, 'image.png', 'false', 'false', '°', -1, 'false')");

        // Function 'F' Keys
        db.execSQL(keyInsertInto + " values (-1, 'image.png', 'false', 'false', 'F1', 112, 'false')");
        db.execSQL(keyInsertInto + " values (-1, 'image.png', 'false', 'false', 'F2', 113, 'false')");
        db.execSQL(keyInsertInto + " values (-1, 'image.png', 'false', 'false', 'F3', 114, 'false')");
        db.execSQL(keyInsertInto + " values (-1, 'image.png', 'false', 'false', 'F4', 115, 'false')");
        db.execSQL(keyInsertInto + " values (-1, 'image.png', 'false', 'false', 'F5', 116, 'false')");
        db.execSQL(keyInsertInto + " values (-1, 'image.png', 'false', 'false', 'F6', 117, 'false')");
        db.execSQL(keyInsertInto + " values (-1, 'image.png', 'false', 'false', 'F7', 118, 'false')");
        db.execSQL(keyInsertInto + " values (-1, 'image.png', 'false', 'false', 'F8', 119, 'false')");
        db.execSQL(keyInsertInto + " values (-1, 'image.png', 'false', 'false', 'F9', 120, 'false')");
        db.execSQL(keyInsertInto + " values (-1, 'image.png', 'false', 'false', 'F10', 121, 'false')");
        db.execSQL(keyInsertInto + " values (-1, 'image.png', 'false', 'false', 'F11', 122, 'false')");
        db.execSQL(keyInsertInto + " values (-1, 'image.png', 'false', 'false', 'F12', 123, 'false')");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion)
    {}
}
