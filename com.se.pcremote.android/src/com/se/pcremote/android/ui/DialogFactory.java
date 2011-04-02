package com.se.pcremote.android.ui;

import org.apache.log4j.Logger;

import com.se.pcremote.android.Layout;
import com.se.pcremote.android.PC;
import com.se.pcremote.android.PCRemoteProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;

/**
 * <p>
 * A factory for all the dialogs displayed by the PC Remote for Android application.
 * </p>
 * 
 * @author simple
 */
public final class DialogFactory
{
    /**
     * <p>
     * The ID of the dialog to display when an active {@link com.se.pcremote.android.Layout Layout} has not been selected.
     * </p>
     */
    public static final int DIALOG_ACTIVE_LAYOUT_NOT_EXISTS_ID = 0;

    /**
     * <p>
     * The ID of the dialog to display when an active {@link com.se.pcremote.android.PC PC} has not been selected.
     * </p>
     */
    public static final int DIALOG_ACTIVE_PC_NOT_EXISTS_ID = 1;

    /**
     * <p>
     * The ID of the dialog to display to delete {@link com.se.pcremote.android.Layout Layout}s.
     * </p>
     */
    public static final int DIALOG_DELETE_LAYOUTS_ID = 2;

    /**
     * <p>
     * The ID of the dialog to display to delete {@link com.se.pcremote.android.PC PC}s.
     * </p>
     */
    public static final int DIALOG_DELETE_PCS_ID = 3;

    /**
     * <p>
     * The ID of the dialog to display when an invalid port is entered.
     * </p>
     */
    public static final int DIALOG_INVALID_PORT_ID = 4;

    /**
     * <p>
     * The ID of the dialog to display when the active {@link com.se.pcremote.android.PC PC} does not exist.
     * </p>
     */
    public static final int DIALOG_NO_ACTIVE_PC_ID = 5;

    /**
     * <p>
     * The ID of the dialog to display if an attempt is made to delete all {@link com.se.pcremote.android.Layout Layout}s.
     * </p>
     */
    public static final int DIALOG_NO_DELETE_ALL_LAYOUTS_ID = 6;

    /**
     * <p>
     * The ID of the dialog to display when no {@link com.se.pcremote.android.PC PC}s exist.
     * </p>
     */
    public static final int DIALOG_NO_PCS_ID = 7;

    /**
     * <p>
     * The ID of the dialog to display to select the active {@link com.se.pcremote.android.Layout Layout}.
     * </p>
     */
    public static final int DIALOG_SELECT_ACTIVE_LAYOUT_ID = 8;

    /**
     * <p>
     * The ID of the dialog to display to select the active {@link com.se.pcremote.android.PC PC}.
     * </p>
     */
    public static final int DIALOG_SELECT_ACTIVE_PC_ID = 9;

    /**
     * <p>
     * The singleton instance of <code>DialogFactory</code>.
     * </p>
     */
    private static DialogFactory fInstance;

    /**
     * <p>
     * Retrieves the singleton instance of <code>DialogFactory</code>.
     * </p>
     * 
     * @return The singleton instance of <code>DialogFactory</code>.
     */
    public static DialogFactory getInstance()
    {
        if (fInstance == null)
        {
            fInstance = new DialogFactory();
        }

        return (fInstance);
    }

    /**
     * <p>
     * The selection to delete from the currently displayed dialog.
     * </p>
     */
    private boolean[] fDeleteSelection;

    /**
     * <p>
     * Logs messages associated with this class.
     * </p>
     */
    private Logger fLogger;

    /**
     * <p>
     * The list of {@link com.se.pcremote.android.Layout Layout} IDs shown in the 'Select Layout' dialog.
     * </p>
     */
    private int[] fSelectLayoutIds;

    /**
     * <p>
     * The list of {@link com.se.pcremote.android.PC PC} IDs shown in the 'Select PC' dialog.
     * </p>
     */
    private int[] fSelectPcIds;

    /**
     * <p>
     * Creates an instance of <code>DialogFactory</code>.
     * </p>
     */
    private DialogFactory()
    {
        fDeleteSelection = null;
        fLogger = Logger.getLogger(this.getClass());
        fSelectLayoutIds = null;
        fSelectPcIds = null;
    }

    /**
     * <p>
     * Checks that not all the {@link com.se.pcremote.android.Layout Layout}s are being deleted. If they are it removes the 'Delete Layouts' dialog
     * and replaces it with an error dialog.
     * </p>
     * 
     * @param activity The activity the dialog is being created for.
     * 
     * @return False if all the <code>Layout</code>s are being deleted, true otherwise.
     */
    private boolean checkNotDeletingAllLayouts(final Activity activity)
    {
        boolean deleteAll = true;

        for (int index = 0; index < fDeleteSelection.length; index++)
        {
            if (!fDeleteSelection[index])
            {
                deleteAll = false;
                break;
            }
        }

        if (deleteAll)
        {
            activity.removeDialog(DIALOG_DELETE_LAYOUTS_ID);
            activity.showDialog(DIALOG_NO_DELETE_ALL_LAYOUTS_ID);
        }

        return (!deleteAll);
    }

    /**
     * <p>
     * Creates the dialog to display when an active {@link com.se.pcremote.android.Layout Layout} has not been selected.
     * </p>
     * 
     * @param activity The activity the dialog is being created for.
     * 
     * @return The dialog to display when an active <code>Layout</code> has not been selected.
     */
    private Dialog createActiveLayoutNotExistsDialog(final Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Layout Not Found");
        builder.setMessage("Could not find the active Layout.");

        // Default active Layout.
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(final DialogInterface dialog, final int which)
            {
                dialog.dismiss();
                ((ControlPad) activity).build();
            }
        });

        // Opens the 'Select Active Layout' dialog.
        builder.setPositiveButton("Set Active Layout", new DialogInterface.OnClickListener()
        {
            public void onClick(final DialogInterface dialog, final int which)
            {
                dialog.dismiss();
                activity.showDialog(DIALOG_SELECT_ACTIVE_LAYOUT_ID);
            }
        });

        return (builder.create());
    }

    /**
     * <p>
     * Creates the dialog to display when an active {@link com.se.pcremote.android.PC PC} has not been selected.
     * </p>
     * 
     * @param activity The activity the dialog is being created for.
     * 
     * @return The dialog to display when an active <code>PC</code> has not been selected.
     */
    private Dialog createActivePcNotExistsDialog(final Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("PC Not Found");
        builder.setMessage("Could not find the active PC.");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(final DialogInterface dialog, final int which)
            {
                dialog.cancel();
            }
        });

        // Opens the 'Select Active PC' dialog.
        builder.setPositiveButton("Set Active PC", new DialogInterface.OnClickListener()
        {
            public void onClick(final DialogInterface dialog, final int which)
            {
                dialog.dismiss();
                activity.showDialog(DIALOG_SELECT_ACTIVE_PC_ID);
            }
        });

        return (builder.create());
    }

    /**
     * <p>
     * Creates the dialog to display to delete {@link com.se.pcremote.android.Layout Layout}s.
     * </p>
     * 
     * @param activity The activity the dialog is being created for.
     * 
     * @return The dialog to display to delete <code>Layout</code>s.
     */
    private Dialog createDeleteLayoutsDialog(final Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Delete Layouts");

        // Create Layout list.
        Cursor layoutCursor = activity.getContentResolver().query(PCRemoteProvider.LAYOUT_URI, null, null, null, null);
        String[] deleteNames = new String[layoutCursor.getCount()];
        fDeleteSelection = new boolean[layoutCursor.getCount()];

        for (int index = 0; index < deleteNames.length; index++)
        {
            layoutCursor.moveToNext();
            deleteNames[index] = layoutCursor.getString(layoutCursor.getColumnIndex(PCRemoteProvider.LAYOUT_COLUMN_NAME));
            fDeleteSelection[index] = false;
        }

        layoutCursor.close();

        builder.setMultiChoiceItems(deleteNames, fDeleteSelection, new OnMultiChoiceClickListener()
        {
            @Override
            public void onClick(final DialogInterface dialog, final int which, final boolean isChecked)
            {}
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(final DialogInterface dialog, final int id)
            {
                activity.removeDialog(DIALOG_DELETE_LAYOUTS_ID);
            }
        });

        // Delete the selected Layout(s).
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(final DialogInterface dialog, final int id)
            {
                if (checkNotDeletingAllLayouts(activity))
                {
                    Cursor layoutCursor = activity.getContentResolver().query(PCRemoteProvider.LAYOUT_URI, null, null, null, null);

                    for (int index = 0; index < fDeleteSelection.length; index++)
                    {
                        if (fDeleteSelection[index])
                        {
                            layoutCursor.moveToPosition(index);

                            Layout layout = new Layout();
                            layout.load(
                                    activity,
                                    ContentUris.withAppendedId(PCRemoteProvider.LAYOUT_URI,
                                            layoutCursor.getInt(layoutCursor.getColumnIndex(BaseColumns._ID))));
                            layout.delete(activity);
                        }
                    }

                    layoutCursor.close();
                    activity.removeDialog(DIALOG_DELETE_LAYOUTS_ID);
                }
            }
        });

        return (builder.create());
    }

    /**
     * <p>
     * Creates the dialog to display to delete {@link com.se.pcremote.android.PC PC}s.
     * </p>
     * 
     * @param activity The activity the dialog is being created for.
     * 
     * @return The dialog to display to delete <code>PC</code>s.
     */
    private Dialog createDeletePcsDialog(final Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Delete PCs");

        // Create PC list.
        Cursor pcCursor = activity.getContentResolver().query(PCRemoteProvider.PC_URI, null, null, null, null);
        String[] deleteNames = new String[pcCursor.getCount()];
        fDeleteSelection = new boolean[pcCursor.getCount()];

        for (int index = 0; index < deleteNames.length; index++)
        {
            pcCursor.moveToNext();
            deleteNames[index] = pcCursor.getString(pcCursor.getColumnIndex(PCRemoteProvider.PC_COLUMN_NAME));
            fDeleteSelection[index] = false;
        }

        pcCursor.close();

        builder.setMultiChoiceItems(deleteNames, fDeleteSelection, new OnMultiChoiceClickListener()
        {
            @Override
            public void onClick(final DialogInterface dialog, final int which, final boolean isChecked)
            {}
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(final DialogInterface dialog, final int id)
            {
                activity.removeDialog(DIALOG_DELETE_PCS_ID);
            }
        });

        // Delete the selected PC(s).
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(final DialogInterface dialog, final int id)
            {
                Cursor pcCursor = activity.getContentResolver().query(PCRemoteProvider.PC_URI, null, null, null, null);

                for (int index = 0; index < fDeleteSelection.length; index++)
                {
                    if (fDeleteSelection[index])
                    {
                        pcCursor.moveToPosition(index);

                        PC pc = new PC();
                        pc.load(activity,
                                ContentUris.withAppendedId(PCRemoteProvider.PC_URI, pcCursor.getInt(pcCursor.getColumnIndex(BaseColumns._ID))));
                        pc.delete(activity);
                    }
                }

                pcCursor.close();
                activity.removeDialog(DIALOG_DELETE_PCS_ID);
            }
        });

        return (builder.create());
    }

    /**
     * <p>
     * Creates the dialog with the given ID.
     * </p>
     * 
     * @param activity The activity the dialog is being created for.
     * @param id The ID of the dialog to create.
     * 
     * @return The dialog with the given ID.
     */
    public Dialog createDialog(final Activity activity, final int id)
    {
        Dialog dialog = null;

        if (id == DIALOG_ACTIVE_LAYOUT_NOT_EXISTS_ID)
        {
            dialog = createActiveLayoutNotExistsDialog(activity);
        }
        else if (id == DIALOG_ACTIVE_PC_NOT_EXISTS_ID)
        {
            dialog = createActivePcNotExistsDialog(activity);
        }
        else if (id == DIALOG_DELETE_LAYOUTS_ID)
        {
            dialog = createDeleteLayoutsDialog(activity);
        }
        else if (id == DIALOG_DELETE_PCS_ID)
        {
            dialog = createDeletePcsDialog(activity);
        }
        else if (id == DIALOG_INVALID_PORT_ID)
        {
            dialog = createInvalidPortDialog(activity);
        }
        else if (id == DIALOG_NO_ACTIVE_PC_ID)
        {
            dialog = createNoActivePcDialog(activity);
        }
        else if (id == DIALOG_NO_DELETE_ALL_LAYOUTS_ID)
        {
            dialog = createNoDeleteAllLayoutsDialog(activity);
        }
        else if (id == DIALOG_NO_PCS_ID)
        {
            dialog = createNoPcsDialog(activity);
        }
        else if (id == DIALOG_SELECT_ACTIVE_LAYOUT_ID)
        {
            dialog = createSelectActiveLayoutDialog(activity);
        }
        else if (id == DIALOG_SELECT_ACTIVE_PC_ID)
        {
            dialog = createSelectActivePcDialog(activity);
        }

        return (dialog);
    }

    /**
     * <p>
     * Creates the dialog to display when an invalid port is entered.
     * </p>
     * 
     * @param activity The activity the dialog is being created for.
     * 
     * @return The dialog to display when an invalid port is entered.
     */
    private Dialog createInvalidPortDialog(final Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Invalid Port");
        builder.setMessage("Failed to save the PC. You have chosen an invalid port number ("
                + PreferenceManager.getDefaultSharedPreferences(activity).getString("pcPort", null)
                + "). The port number must only contain the numbers 0-9 e.g. 10999.");

        builder.setNegativeButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(final DialogInterface dialog, final int id)
            {
                activity.removeDialog(DIALOG_INVALID_PORT_ID);
            }
        });

        return (builder.create());
    }

    /**
     * <p>
     * Creates the dialog to display when the active {@link com.se.pcremote.android.PC PC} does not exist.
     * </p>
     * 
     * @param activity The activity the dialog is being created for.
     * 
     * @return The dialog to display when the active <code>PC</code> does not exist.
     */
    private Dialog createNoActivePcDialog(final Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("No Active PC");
        builder.setMessage("None of the PCs are active.");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(final DialogInterface dialog, final int which)
            {
                dialog.cancel();
            }
        });

        // Opens the 'Select Active PC' dialog.
        builder.setPositiveButton("Set Active PC", new DialogInterface.OnClickListener()
        {
            public void onClick(final DialogInterface dialog, final int which)
            {
                dialog.dismiss();
                activity.showDialog(DIALOG_SELECT_ACTIVE_PC_ID);
            }
        });

        return (builder.create());
    }

    /**
     * <p>
     * Creates the dialog to display if an attempt is made to delete all {@link com.se.pcremote.android.Layout Layout}s.
     * </p>
     * 
     * @param activity The activity the dialog is being created for.
     * 
     * @return The dialog to display if an attempt is made to delete all <code>Layout</code>s.
     */
    private Dialog createNoDeleteAllLayoutsDialog(final Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Cannot Delete All Layouts");
        builder.setMessage("You must keep at least one layout.");

        builder.setNegativeButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(final DialogInterface dialog, final int id)
            {
                dialog.cancel();
            }
        });

        return (builder.create());
    }

    /**
     * <p>
     * Creates the dialog to display when no {@link com.se.pcremote.android.PC PC}s exist.
     * </p>
     * 
     * @param activity The activity the dialog is being created for.
     * 
     * @return The dialog to display when no <code>PC</code>s exist.
     */
    private Dialog createNoPcsDialog(final Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("No PCs");
        builder.setMessage("No PCs have been created.");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(final DialogInterface dialog, final int which)
            {
                dialog.cancel();
            }
        });

        // Opens the 'Insert PC' activity.
        builder.setPositiveButton("Create PC", new DialogInterface.OnClickListener()
        {
            public void onClick(final DialogInterface dialog, final int which)
            {
                dialog.dismiss();
                activity.startActivityForResult(new Intent(Intent.ACTION_INSERT, PCRemoteProvider.PC_URI), ControlPad.INSERT_PC_REQUEST);
            }
        });

        return (builder.create());
    }

    /**
     * <p>
     * Creates the dialog to display to select the active {@link com.se.pcremote.android.Layout Layout}.
     * </p>
     * 
     * @param activity The activity the dialog is being created for.
     * 
     * @return The dialog to display to select the active <code>Layout</code>.
     */
    private Dialog createSelectActiveLayoutDialog(final Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Select Active Layout");

        // Create Layout list.
        Cursor layoutCursor = activity.getContentResolver().query(PCRemoteProvider.LAYOUT_URI, null, null, null, null);
        String[] selectLayoutNames = new String[layoutCursor.getCount()];
        fSelectLayoutIds = new int[layoutCursor.getCount()];
        int selectLayout = -1;

        for (int index = 0; index < selectLayoutNames.length; index++)
        {
            layoutCursor.moveToNext();
            selectLayoutNames[index] = layoutCursor.getString(layoutCursor.getColumnIndex(PCRemoteProvider.LAYOUT_COLUMN_NAME));
            fSelectLayoutIds[index] = layoutCursor.getInt(layoutCursor.getColumnIndex(BaseColumns._ID));

            Layout layout = ((ControlPad) activity).getLayout();
            if (layout != null && layout.getId() == fSelectLayoutIds[index])
            {
                selectLayout = index;
            }
        }

        layoutCursor.close();

        // Saves and sets the selected Layout.
        builder.setSingleChoiceItems(selectLayoutNames, selectLayout, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(final DialogInterface dialog, final int which)
            {
                try
                {
                    Layout layout = new Layout();
                    layout.load(activity, ContentUris.withAppendedId(PCRemoteProvider.LAYOUT_URI, fSelectLayoutIds[which]));
                    PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("activeLayout", layout.getUri().toString()).commit();

                    ((ControlPad) activity).setLayout(layout);
                    ((ControlPad) activity).build();
                }
                catch (Exception e)
                {
                    fLogger.error("Failed to save/set the active Layout.", e);
                }

                activity.removeDialog(DIALOG_SELECT_ACTIVE_LAYOUT_ID);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(final DialogInterface dialog, final int which)
            {
                activity.removeDialog(DIALOG_SELECT_ACTIVE_PC_ID);
                ((ControlPad) activity).build();
            }
        });

        return (builder.create());
    }

    /**
     * <p>
     * Creates the dialog to display to select the active {@link com.se.pcremote.android.PC PC}.
     * </p>
     * 
     * @param activity The activity the dialog is being created for.
     * 
     * @return The dialog to display to select the active <code>PC</code>.
     */
    private Dialog createSelectActivePcDialog(final Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Select Active PC");

        // Create PC list.
        Cursor pcCursor = activity.getContentResolver().query(PCRemoteProvider.PC_URI, null, null, null, null);
        String[] selectPcNames = new String[pcCursor.getCount()];
        fSelectPcIds = new int[pcCursor.getCount()];
        int selectPc = -1;

        for (int index = 0; index < selectPcNames.length; index++)
        {
            pcCursor.moveToNext();
            selectPcNames[index] = pcCursor.getString(pcCursor.getColumnIndex(PCRemoteProvider.PC_COLUMN_NAME));
            fSelectPcIds[index] = pcCursor.getInt(pcCursor.getColumnIndex(BaseColumns._ID));

            PC pc = ((ControlPad) activity).getPc();
            if (pc != null && pc.getId() == fSelectPcIds[index])
            {
                selectPc = index;
            }
        }

        pcCursor.close();

        // Saves and sets the active PC.
        builder.setSingleChoiceItems(selectPcNames, selectPc, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(final DialogInterface dialog, final int which)
            {
                try
                {
                    PC pc = new PC();
                    pc.load(activity, ContentUris.withAppendedId(PCRemoteProvider.PC_URI, fSelectPcIds[which]));
                    PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("activePc", pc.getUri().toString()).commit();

                    ((ControlPad) activity).setPc(pc);
                    ((ControlPad) activity).connect();
                }
                catch (Exception e)
                {
                    fLogger.error("Failed to save/set the active PC.", e);
                }

                activity.removeDialog(DIALOG_SELECT_ACTIVE_PC_ID);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(final DialogInterface dialog, final int which)
            {
                activity.removeDialog(DIALOG_SELECT_ACTIVE_PC_ID);
            }
        });

        return (builder.create());
    }
}
