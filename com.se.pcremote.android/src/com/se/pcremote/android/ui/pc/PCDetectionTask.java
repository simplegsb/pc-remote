package com.se.pcremote.android.ui.pc;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.se.devenvy.net.Sniffer;
import com.se.pcremote.android.R;

/**
 * <p>
 * A task that detects {@link com.se.pcremote.android.PC PC}s on the local network.
 * </p>
 * 
 * @author Gary Buyn
 */
public class PCDetectionTask extends DetectionTask
{
    /**
     * <p>
     * Logs messages associated with this class.
     * </p>
     */
    private Logger fLogger;

    /**
     * <p>
     * Creates an instance of <code>PCDetectionTask</code>.
     * </p>
     * 
     * @param detectPcs The 'Detect PCs' activity that this detection task is being run on behalf of.
     */
    public PCDetectionTask(final DetectPCs detectPcs)
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
                sniffer.sniffSubnet();
            }
            catch (SocketException e)
            {
                fLogger.error("Failed to detect the PCs.", e);
            }
        }
        else
        {
            sniffer.sniffSubnet(addresses[0]);
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(final InetAddress... addresses)
    {
        super.onProgressUpdate(addresses);

        InetAddress address = addresses[0];

        // If the PC has NOT already been detected.
        if (getDetectPcs().getDetectedPcMap(address) == null)
        {
            HashMap<String, String> detectedPcMap = new HashMap<String, String>();
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

            detectedPcMap.put("server_detection_status", getDetectPcs().getString(R.string.pc_server_not_detected));
            getDetectPcs().addDetectedPcMap(detectedPcMap);
        }
    }
}
