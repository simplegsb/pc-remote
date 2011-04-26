package com.se.pcremote.android.ui.controlpad;

import org.apache.log4j.Logger;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import com.se.pcremote.android.Layout;
import com.se.pcremote.android.PC;
import com.se.pcremote.android.PCConnection;
import com.se.pcremote.android.PCConnection.PCConnectionBinder;
import com.se.pcremote.android.PCRemoteProvider;
import com.se.pcremote.android.R;
import com.se.pcremote.android.ui.DialogFactory;
import com.se.pcremote.android.ui.MainPreferences;

/**
 * <p>
 * Sends commands to the currently targeted device.
 * </p>
 * 
 * @author Gary Buyn
 */
public class ControlPad extends Activity
{
    /**
     * <p>
     * The ID of a request for a new {@link com.se.pcremote.android.PC PC}.
     * </p>
     */
    public static final int INSERT_PC_REQUEST = 0;

    /**
     * <p>
     * The active {@link com.se.pcremote.android.Layout Layout}.
     * </p>
     */
    private Layout fLayout;

    /**
     * <p>
     * Logs messages associated with this class.
     * </p>
     */
    private Logger fLogger;

    /**
     * <p>
     * The active {@link com.se.pcremote.android.PC PC}.
     * </p>
     */
    private PC fPc;

    /**
     * <p>
     * The connection to the active {@link com.se.pcremote.android.PC PC}.
     * </p>
     */
    private PCConnection fPcConnection;

    /**
     * <p>
     * The connection to the {@link com.se.pcremote.android.PCConnection PCConnection} service.
     * </p>
     */
    private ServiceConnection fServiceConnection;

    /**
     * <p>
     * Creates an instance of <code>ControlPad</code>.
     * </p>
     */
    public ControlPad()
    {
        fLayout = null;
        fLogger = Logger.getLogger(this.getClass());
        fPc = null;
        fPcConnection = null;
        fServiceConnection = new ServiceConnection()
        {
            @Override
            public void onServiceConnected(final ComponentName className, final IBinder service)
            {
                fPcConnection = ((PCConnectionBinder) service).getService();
                connect();
            }

            @Override
            public void onServiceDisconnected(final ComponentName className)
            {
                fPcConnection = null;
            }
        };
    }

    /**
     * <p>
     * Builds the content {@link android.widget.View View}.
     * </p>
     */
    public void build()
    {
        ControlPadView view = new ControlPadView(this);
        view.build();

        setContentView(view);
    }

    /**
     * <p>
     * Connects to the active {@link com.se.pcremote.android.PC PC}.
     * </p>
     */
    public void connect()
    {
        fPcConnection.connect(fPc);
    }

    /**
     * <p>
     * Disconnects from the active {@link com.se.pcremote.android.PC PC}.
     * </p>
     */
    public void disconnect()
    {
        if (fPcConnection != null && fPc != null)
        {
            fPcConnection.disconnect(fPc);
        }
    }

    @Override
    public void finish()
    {
        disconnect();
        unbindService(fServiceConnection);

        super.finish();
    }

    /**
     * <p>
     * Retrieves the connection to the active {@link com.se.pcremote.android.PC PC}.
     * </p>
     * 
     * @return The connection to the active <code>PC</code>.
     */
    public PCConnection getConnection()
    {
        return (fPcConnection);
    }

    /**
     * <p>
     * Retrieves the active {@link com.se.pcremote.android.Layout Layout}.
     * </p>
     * 
     * @return The active {@link com.se.pcremote.android.Layout Layout}.
     */
    public Layout getLayout()
    {
        return (fLayout);
    }

    /**
     * <p>
     * Retrieves the active {@link com.se.pcremote.android.PC PC}.
     * </p>
     * 
     * @return The active {@link com.se.pcremote.android.PC PC}.
     */
    public PC getPc()
    {
        return (fPc);
    }

    /**
     * <p>
     * Initialises this <code>ControlPad</code> based on the active {@link com.se.pcremote.android.Layout Layout} and
     * {@link com.se.pcremote.android.PC PC}.
     * </p>
     * 
     * @return True if the initialisation was successful, false otherwise.
     */
    public boolean init()
    {
        // Check that the active Layout exists.
        String layoutUri = PreferenceManager.getDefaultSharedPreferences(this).getString("activeLayout", null);
        fLayout = new Layout();
        fLayout.load(this, Uri.parse(layoutUri));
        if (fLayout.getId() == 0)
        {
            fLogger.error("Layout for URI '" + layoutUri + "' not found.");

            showDialog(DialogFactory.DIALOG_ACTIVE_LAYOUT_NOT_EXISTS_ID);
            return (false);
        }

        build();

        // Check that at least one PC exists.
        Cursor pcCursor = getContentResolver().query(PCRemoteProvider.PC_URI, null, null, null, null);
        int count = pcCursor.getCount();
        pcCursor.close();
        if (count == 0)
        {
            showDialog(DialogFactory.DIALOG_NO_PCS_ID);
            return (false);
        }

        // Check that an active PC has been selected.
        String pcUri = PreferenceManager.getDefaultSharedPreferences(this).getString("activePc", null);
        if (pcUri == null)
        {
            showDialog(DialogFactory.DIALOG_NO_ACTIVE_PC_ID);
            return (false);
        }

        // Check that the active PC exists.
        fPc = new PC();
        fPc.load(this, Uri.parse(pcUri));
        if (fPc.getId() == 0)
        {
            fLogger.error("PC for URI '" + pcUri + "' not found.");

            showDialog(DialogFactory.DIALOG_ACTIVE_PC_NOT_EXISTS_ID);
            fPc = null;
        }

        return (true);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK)
        {
            if (requestCode == INSERT_PC_REQUEST)
            {
                try
                {
                    // Save the active PC.
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putString("activePc", intent.getDataString()).commit();
                }
                catch (Exception e)
                {
                    fLogger.error("Failed to save the active PC.", e);
                }
            }
        }
    }

    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Initialise preferences. Only needs to happen the first time this activity (the main activity) is created.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getString("activeLayout", null) == null)
        {
            preferences.edit().putString("activeLayout", ContentUris.withAppendedId(PCRemoteProvider.LAYOUT_URI, 1).toString()).commit();
        }

        // Bind to the PC connection service.
        bindService(new Intent(this, PCConnection.class), fServiceConnection, BIND_AUTO_CREATE);

        init();
    }

    @Override
    protected Dialog onCreateDialog(final int id)
    {
        return (DialogFactory.getInstance().createDialog(this, id));
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {
        getMenuInflater().inflate(R.menu.control_pad_options, menu);

        return (true);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item)
    {
        if (item.getItemId() == R.id.switch_layout)
        {
            showDialog(DialogFactory.DIALOG_SELECT_ACTIVE_LAYOUT_ID);
            return (true);
        }
        else if (item.getItemId() == R.id.switch_pc)
        {
            showDialog(DialogFactory.DIALOG_SELECT_ACTIVE_PC_ID);
            return (true);
        }
        else if (item.getItemId() == R.id.settings)
        {
            startActivity(new Intent(this, MainPreferences.class));
            return (true);
        }
        else
        {
            return (super.onOptionsItemSelected(item));
        }
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();

        if (init())
        {
            connect();
        }
    }

    /**
     * <p>
     * Sets the active {@link com.se.pcremote.android.Layout Layout}.
     * </p>
     * 
     * @param layout The active {@link com.se.pcremote.android.Layout Layout}.
     */
    public void setLayout(final Layout layout)
    {
        fLayout = layout;
    }

    /**
     * <p>
     * Sets the active {@link com.se.pcremote.android.PC PC}.
     * </p>
     * 
     * @param pc The active {@link com.se.pcremote.android.PC PC}.
     */
    public void setPc(final PC pc)
    {
        fPc = pc;
    }
}
