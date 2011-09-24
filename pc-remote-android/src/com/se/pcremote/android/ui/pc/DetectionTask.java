package com.se.pcremote.android.ui.pc;

import java.net.InetAddress;
import java.util.List;

import android.database.Cursor;
import android.os.AsyncTask;

import com.se.devenvy.net.SnifferListener;
import com.se.pcremote.android.PCRemoteProvider;

/**
 * <p>
 * A task that detects devices on the local network.
 * </p>
 * 
 * @author Gary Buyn
 */
public abstract class DetectionTask extends AsyncTask<InetAddress, InetAddress, List<InetAddress>> implements SnifferListener
{
    /**
     * <p>
     * The amount of time to wait for a device to be found at an address.
     * </p>
     */
    protected static final int TIMEOUT = 500;

    /**
     * <p>
     * The 'Detect PCs' activity that this detection task is being run on behalf of.
     * </p>
     */
    private DetectPCs fDetectPcs;

    /**
     * <p>
     * Create an instance of <code>Detectiontask</code>.
     * </p>
     * 
     * @param detectPcs The 'Detect PCs' activity that this detection task is being run on behalf of.
     */
    public DetectionTask(final DetectPCs detectPcs)
    {
        fDetectPcs = detectPcs;
    }

    @Override
    public void deviceFound(final InetAddress address)
    {
        publishProgress(address);
    }

    /**
     * <p>
     * Retrieves the 'Detect PCs' activity that this detection task is being run on behalf of.
     * </p>
     * 
     * @return The 'Detect PCs' activity that this detection task is being run on behalf of.
     */
    protected DetectPCs getDetectPcs()
    {
        return (fDetectPcs);
    }

    @Override
    protected void onPostExecute(final List<InetAddress> addresses)
    {
        super.onPostExecute(addresses);

        fDetectPcs.removeInProgressView();
    }

    /**
     * <p>
     * Determines if a {@link com.se.pcremote.android.PC PC} already exists for the given address.
     * </p>
     * 
     * @param address The address to check for an existing {@link com.se.pcremote.android.PC PC}.
     * 
     * @return True if a {@link com.se.pcremote.android.PC PC} already exists for the given address, false otherwise.
     */
    protected boolean pcExistsForAddress(final InetAddress address)
    {
        boolean existsForAddress = false;

        String where = PCRemoteProvider.PC_COLUMN_HOST + " = '" + address.getHostAddress() + "' or " + PCRemoteProvider.PC_COLUMN_HOST + " = '"
                + address.getHostName() + "'";
        Cursor cursor = fDetectPcs.getContentResolver().query(PCRemoteProvider.PC_URI, null, where, null, null);

        if (cursor.moveToFirst())
        {
            existsForAddress = true;
        }

        cursor.close();

        return (existsForAddress);
    }

    @Override
    public void sniffComplete(final List<InetAddress> addresses)
    {
        onPostExecute(addresses);
    }
}
