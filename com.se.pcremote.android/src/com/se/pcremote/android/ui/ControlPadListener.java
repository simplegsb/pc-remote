package com.se.pcremote.android.ui;

import java.io.IOException;

import org.apache.log4j.Logger;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

import com.se.pcremote.client.PCRemoteClient;

/**
 * <p>
 * Sends commands to the active {@link com.se.pcremote.android.PC PC} in response to UI events.
 * </p>
 * 
 * @author simple
 */
public class ControlPadListener extends SimpleOnGestureListener
{
    /**
     * <p>
     * The {@link com.se.pcremote.android.ui.ControlPad ControlPad} this <code>ControlPadListener</code> sends commands to the active
     * {@link com.se.pcremote.android.PC PC} for.
     * </p>
     */
    private ControlPad fControlPad;

    /**
     * <p>
     * Logs messages associated with this class.
     * </p>
     */
    private Logger fLogger;

    /**
     * <p>
     * Creates an instance of <code>ControlPadListener</code>.
     * </p>
     * 
     * @param controlPad The {@link com.se.pcremote.android.ui.ControlPad ControlPad} this <code>ControlPadListener</code> sends commands to the
     * active {@link com.se.pcremote.android.PC PC} for.
     */
    public ControlPadListener(final ControlPad controlPad)
    {
        fControlPad = controlPad;
        fLogger = Logger.getLogger(this.getClass());
    }

    @Override
    public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float distanceX, final float distanceY)
    {
        if (fControlPad.getConnection() != null)
        {
            PCRemoteClient client = fControlPad.getConnection().getClient();
            if (client != null && client.isConnected())
            {
                try
                {
                    client.sendCommandViaUdp("mouseMoveRelative(" + distanceX + "," + distanceY + ");");
                }
                catch (IOException e)
                {
                    fLogger.error("Failed to send the command to the PC.", e);
                }
            }
        }

        return (true);
    }
}
