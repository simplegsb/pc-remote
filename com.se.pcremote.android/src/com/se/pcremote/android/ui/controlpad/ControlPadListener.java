package com.se.pcremote.android.ui.controlpad;

import java.io.IOException;

import org.apache.log4j.Logger;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;

import com.se.pcremote.android.Key;
import com.se.pcremote.client.PCRemoteClient;

/**
 * <p>
 * Sends commands to the active {@link com.se.pcremote.android.PC PC} in response to UI events.
 * </p>
 * 
 * @author simple
 */
public class ControlPadListener extends SimpleOnGestureListener implements OnClickListener, OnKeyListener
{
    /**
     * <p>
     * The {@link com.se.pcremote.android.ui.controlpad.ControlPad ControlPad} this <code>ControlPadListener</code> sends commands to the active
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
     * @param controlPad The {@link com.se.pcremote.android.ui.controlpad.ControlPad ControlPad} this <code>ControlPadListener</code> sends commands to the
     * active {@link com.se.pcremote.android.PC PC} for.
     */
    public ControlPadListener(final ControlPad controlPad)
    {
        fControlPad = controlPad;
        fLogger = Logger.getLogger(this.getClass());
    }

    @Override
    public void onClick(final View view)
    {
        if (fControlPad.getConnection() != null)
        {
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
                    else
                    {
                        client.sendCommandViaTcp("keyPress(" + view.getId() + ");keyRelease(" + view.getId() + ");");
                    }
                }
                catch (IOException e)
                {
                    fLogger.error("Failed to send the command to PC '" + fControlPad.getPc().getName() + "'.", e);
                }
            }
        }
    }

    @Override
    public boolean onKey(final View view, final int keyCode, final KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if (fControlPad.getConnection() != null)
            {
                PCRemoteClient client = fControlPad.getConnection().getClient();
                if (client != null && client.isConnected())
                {
                    try
                    {
                        Key key = new Key();
                        key.load(fControlPad, event.getKeyCode());
                        client.sendCommandViaTcp("keyPress(" + key.getServerCode() + ");");
                    }
                    catch (IOException e)
                    {
                        fLogger.error("Failed to send the command to PC '" + fControlPad.getPc().getName() + "'.", e);
                    }
                }
            }
        }
        else if (event.getAction() == KeyEvent.ACTION_UP)
        {
            if (fControlPad.getConnection() != null)
            {
                PCRemoteClient client = fControlPad.getConnection().getClient();
                if (client != null && client.isConnected())
                {
                    try
                    {
                        Key key = new Key();
                        key.load(fControlPad, event.getKeyCode());
                        client.sendCommandViaTcp("keyRelease(" + key.getServerCode() + ");");
                    }
                    catch (IOException e)
                    {
                        fLogger.error("Failed to send the command to PC '" + fControlPad.getPc().getName() + "'.", e);
                    }
                }
            }
        }

        return (false);
    }

    @Override
    public boolean onScroll(final MotionEvent event1, final MotionEvent event2, final float distanceX, final float distanceY)
    {
        if (fControlPad.getConnection() != null)
        {
            PCRemoteClient client = fControlPad.getConnection().getClient();
            if (client != null && client.isConnected())
            {
                try
                {
                    client.sendCommandViaTcp("mouseMoveRelative(" + distanceX + "," + distanceY + ");");
                }
                catch (IOException e)
                {
                    fLogger.error("Failed to send the command to PC '" + fControlPad.getPc().getName() + "'.", e);
                }
            }
        }

        return (true);
    }
}
