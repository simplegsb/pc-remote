package com.se.pcremote.android.ui.pc;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import android.widget.BaseAdapter;

import com.se.devenvy.net.Sniffer;
import com.se.pcremote.android.R;
import com.se.pcremote.server.PCRemoteServer;

/**
 * <p>
 * A task that detects {@link com.se.pcremote.android.PC PC}s running the PC Remote Server on the local network.
 * </p>
 * 
 * @author Gary Buyn
 */
public class PCRemoteServerDetectionTask extends DetectionTask
{
    /**
     * <p>
     * Logs messages associated with this class.
     * </p>
     */
    private Logger fLogger;

    /**
     * <p>
     * Creates an instance of <code>PCRemoteServerDetectionTask</code>.
     * </p>
     * 
     * @param detectPcs The 'Detect PCs' activity that this detection task is being run on behalf of.
     */
    public PCRemoteServerDetectionTask(final DetectPCs detectPcs)
    {
        super(detectPcs);

        fLogger = Logger.getLogger(getClass());
    }

    @Override
    protected List<InetAddress> doInBackground(final InetAddress... addresses)
    {
        Sniffer sniffer = new Sniffer();
        sniffer.setTimeout(TIMEOUT);
        sniffer.addSnifferListener(this);

        if (addresses.length == 0)
        {
            try
            {
                sniffer.sniffSubnetForTcpConnection(PCRemoteServer.DEFAULT_PORT);
            }
            catch (SocketException e)
            {
                fLogger.error("Failed to detect the PCs running the PC Remote Server.", e);
            }
        }
        else
        {
            sniffer.sniffSubnetForTcpConnection(PCRemoteServer.DEFAULT_PORT, addresses[0]);
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(final InetAddress... addresses)
    {
        super.onProgressUpdate(addresses);

        InetAddress address = addresses[0];
        HashMap<String, String> detectedPcMap = getDetectPcs().getDetectedPcMap(address);

        // If the PC has NOT already been detected.
        if (detectedPcMap == null)
        {
            detectedPcMap = new HashMap<String, String>();
            detectedPcMap.put("host_name", address.getHostName());

            if (pcExistsForAddress(address))
            {
                detectedPcMap.put("pc_exists", String.valueOf(true));
                detectedPcMap.put("pc_exists_status", getDetectPcs().getString(R.string.pc_exists));
            }
            else
            {
                detectedPcMap.put("pc_exists", String.valueOf(false));
            }

            getDetectPcs().addDetectedPcMap(detectedPcMap);
        }

        detectedPcMap.put("server_detection_status", getDetectPcs().getString(R.string.pc_server_detected));
        ((BaseAdapter) getDetectPcs().getListAdapter()).notifyDataSetChanged();
    }
}
