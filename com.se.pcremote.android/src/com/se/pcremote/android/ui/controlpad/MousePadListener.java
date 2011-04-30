package com.se.pcremote.android.ui.controlpad;

import java.io.IOException;

import org.apache.log4j.Logger;

import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

import com.se.pcremote.client.PCRemoteClient;

/**
 * <p>
 * Sends commands to the active {@link com.se.pcremote.android.PC PC} in response to the mouse pad UI events.
 * </p>
 * 
 * @author Gary Buyn
 */
public class MousePadListener extends SimpleOnGestureListener
{
    /**
     * <p>
     * The {@link com.se.pcremote.android.ui.controlpad.ControlPad ControlPad} this <code>MousePadListener</code> sends commands to the active
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
     * The factor to multiply the mouse movements by.
     * </p>
     */
    private int fMouseSensitivity;

    /**
     * <p>
     * Creates an instance of <code>MousePadListener</code>.
     * </p>
     * 
     * @param controlPad The {@link com.se.pcremote.android.ui.controlpad.ControlPad ControlPad} this <code>MousePadListener</code> sends commands to
     * the active {@link com.se.pcremote.android.PC PC} for.
     */
    public MousePadListener(final ControlPad controlPad)
    {
        fControlPad = controlPad;
        fLogger = Logger.getLogger(getClass());
        fMouseSensitivity = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(fControlPad).getString("mouseSensitivity", "1"));
    }

    @Override
    public boolean onDoubleTap(final MotionEvent event)
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
                    client.sendCommandViaTcp("mousePress(1);mouseRelease(1);mousePress(1);mouseRelease(1);");
                }
                catch (IOException e)
                {
                    fLogger.error("Failed to send the command to PC '" + fControlPad.getPc().getName() + "'.", e);
                }
            }
            // Otherwise, make sure the user has been notified that the PC Remote Client is not currently connected to a server.
            else
            {
                fControlPad.getConnection().disconnect();
            }
        }

        return (true);
    }

    @Override
    public boolean onScroll(final MotionEvent event1, final MotionEvent event2, final float distanceX, final float distanceY)
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
                    client.sendCommandViaTcp("mouseMoveRelative(" + distanceX * fMouseSensitivity + "," + distanceY * fMouseSensitivity + ");");
                }
                catch (IOException e)
                {
                    fLogger.error("Failed to send the command to PC '" + fControlPad.getPc().getName() + "'.", e);
                }
            }
            // Otherwise, make sure the user has been notified that the PC Remote Client is not currently connected to a server.
            else
            {
                fControlPad.getConnection().disconnect();
            }
        }

        return (true);
    }

    @Override
    public boolean onSingleTapConfirmed(final MotionEvent event)
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
                    client.sendCommandViaTcp("mousePress(1);mouseRelease(1);");
                }
                catch (IOException e)
                {
                    fLogger.error("Failed to send the command to PC '" + fControlPad.getPc().getName() + "'.", e);
                }
            }
            // Otherwise, make sure the user has been notified that the PC Remote Client is not currently connected to a server.
            else
            {
                fControlPad.getConnection().disconnect();
            }
        }

        return (true);
    }
}
