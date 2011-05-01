package com.se.pcremote.android.ui.controlpad;

import java.io.IOException;

import org.apache.log4j.Logger;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.se.pcremote.android.Key;

/**
 * <p>
 * Sends commands to the active {@link com.se.pcremote.android.PC PC} in response to the button grid UI events.
 * </p>
 * 
 * @author Gary Buyn
 */
public class ButtonGridListener implements OnTouchListener
{
    /**
     * <p>
     * The Java constant for the Shift Key.
     * </p>
     */
    private static final int SHIFT_KEY_SERVER_CODE = 16;

    /**
     * <p>
     * The {@link com.se.pcremote.android.ui.controlpad.ControlPad ControlPad} this <code>ButtonGridListener</code> sends commands to the active
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
     * Creates an instance of <code>ButtonGridListener</code>.
     * </p>
     * 
     * @param controlPad The {@link com.se.pcremote.android.ui.controlpad.ControlPad ControlPad} this <code>ButtonGridListener</code> sends commands
     * to the active {@link com.se.pcremote.android.PC PC} for.
     */
    public ButtonGridListener(final ControlPad controlPad)
    {
        fControlPad = controlPad;
        fLogger = Logger.getLogger(getClass());
    }

    @Override
    public boolean onTouch(final View view, final MotionEvent event)
    {
        // If the Control Pad has been connected to the PC Connection service.
        if (fControlPad.getConnection() != null)
        {
            // If the PC Remote Client is currently connected to a server.
            if (fControlPad.getConnection().checkConnection())
            {
                try
                {
                    Key key = new Key();
                    key.loadFromId(fControlPad, view.getId());

                    // If the button has been pressed.
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                    {
                        if (key.isServerShiftRequired())
                        {
                            fControlPad.getConnection().getClient().sendCommandViaTcp("keyPress(" + SHIFT_KEY_SERVER_CODE + ");");
                        }

                        fControlPad.getConnection().getClient().sendCommandViaTcp("keyPress(" + key.getServerCode() + ");");
                    }
                    // If the button has been released.
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                    {
                        fControlPad.getConnection().getClient().sendCommandViaTcp("keyRelease(" + key.getServerCode() + ");");

                        if (key.isServerShiftRequired())
                        {
                            fControlPad.getConnection().getClient().sendCommandViaTcp("keyRelease(" + SHIFT_KEY_SERVER_CODE + ");");
                        }
                    }
                }
                catch (IOException e)
                {
                    fLogger.error("Failed to send the command to PC '" + fControlPad.getPc().getName() + "'.", e);
                }
            }
        }

        return (false);
    }
}
