package com.se.pcremote.android.ui.controlpad;

import java.io.IOException;

import org.apache.log4j.Logger;

import android.view.View;
import android.view.View.OnClickListener;

import com.se.pcremote.client.PCRemoteClient;

/**
 * <p>
 * Sends commands to the active {@link com.se.pcremote.android.PC PC} in response to the mouse button UI events.
 * </p>
 * 
 * @author Gary Buyn
 */
public class MouseButtonListener implements OnClickListener
{
    /**
     * <p>
     * The {@link com.se.pcremote.android.ui.controlpad.ControlPad ControlPad} this <code>MouseButtonListener</code> sends commands to the active
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
     * Creates an instance of <code>MouseButtonListener</code>.
     * </p>
     * 
     * @param controlPad The {@link com.se.pcremote.android.ui.controlpad.ControlPad ControlPad} this <code>MouseButtonListener</code> sends commands
     * to the active {@link com.se.pcremote.android.PC PC} for.
     */
    public MouseButtonListener(final ControlPad controlPad)
    {
        fControlPad = controlPad;
        fLogger = Logger.getLogger(getClass());
    }

    @Override
    public void onClick(final View view)
    {
        // If the Control Pad has been connected to the PC Connection service.
        if (fControlPad.getConnection() != null)
        {
            // If the PC Remote Client is currently connected to a server.
            PCRemoteClient client = fControlPad.getConnection().getClient();
            if (client != null && client.isConnected())
            {
                try
                {
                    if (view.getId() == ControlPadView.MOUSE_BUTTON_LEFT)
                    {
                        client.sendCommandViaTcp("mousePress(1);mouseRelease(1);");
                    }
                    else if (view.getId() == ControlPadView.MOUSE_BUTTON_RIGHT)
                    {
                        client.sendCommandViaTcp("mousePress(3);mouseRelease(3);");
                    }
                }
                catch (IOException e)
                {
                    fLogger.error("Failed to send the command to PC '" + fControlPad.getPc().getName() + "'.", e);
                }
            }
            // Otherwise, make sure the user has been notified that the PC Remote Client is not currently connected to a server.
            else
            {
                fControlPad.getConnection().disconnect(fControlPad.getPc());
            }
        }
    }
}
