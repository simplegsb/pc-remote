package com.se.pcremote.android.ui.controlpad;

import java.io.IOException;

import org.apache.log4j.Logger;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

import com.se.pcremote.android.Key;
import com.se.pcremote.client.PCRemoteClient;

/**
 * <p>
 * Sends commands to the active {@link com.se.pcremote.android.PC PC} in response to the IME events.
 * </p>
 * 
 * @author Gary Buyn
 */
public class ImeListener implements OnKeyListener
{
    /**
     * <p>
     * The Java constant for the Shift Key.
     * </p>
     */
    private static final int SHIFT_KEY_SERVER_CODE = 16;

    /**
     * <p>
     * The {@link com.se.pcremote.android.ui.controlpad.ControlPad ControlPad} this <code>KeyboardButtonListener</code> sends commands to the active
     * {@link com.se.pcremote.android.PC PC} for.
     * </p>
     */
    private ControlPad fControlPad;

    /**
     * <p>
     * Determines whether the ALT key is currently pressed by the IME.
     * </p>
     */
    private boolean fImeAltPressed;

    /**
     * <p>
     * Determines whether the Shift key is currently pressed by the IME.
     * </p>
     */
    private boolean fImeShiftPressed;

    /**
     * <p>
     * Logs messages associated with this class.
     * </p>
     */
    private Logger fLogger;

    /**
     * <p>
     * Creates an instance of <code>KeyboardButtonListener</code>.
     * </p>
     * 
     * @param controlPad The {@link com.se.pcremote.android.ui.controlpad.ControlPad ControlPad} this <code>KeyboardButtonListener</code> sends
     * commands to the active {@link com.se.pcremote.android.PC PC} for.
     */
    public ImeListener(final ControlPad controlPad)
    {
        fControlPad = controlPad;
        fImeAltPressed = false;
        fImeShiftPressed = false;
        fLogger = Logger.getLogger(getClass());
    }

    @Override
    public boolean onKey(final View view, final int keyCode, final KeyEvent event)
    {
        // If the Control Pad has been connected to the PC Connection service.
        if (fControlPad.getConnection() != null)
        {
            // If the PC Remote Client is currently connected to a server.
            PCRemoteClient client = fControlPad.getConnection().getClient();
            if (client != null && client.isConnected())
            {
                // If the Key has been pressed.
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    try
                    {
                        // Do not send ALT Key commands.
                        if (keyCode != KeyEvent.KEYCODE_ALT_LEFT)
                        {
                            Key key = new Key();
                            key.loadFromAndroidKeyCode(fControlPad, keyCode, fImeAltPressed, fImeShiftPressed);

                            // If shift is required but has not been pressed by the IME, press is manually.
                            if (key.isServerShiftRequired() && !fImeShiftPressed)
                            {
                                client.sendCommandViaTcp("keyPress(" + SHIFT_KEY_SERVER_CODE + ");");
                            }

                            client.sendCommandViaTcp("keyPress(" + key.getServerCode() + ");");
                        }
                    }
                    catch (IOException e)
                    {
                        fLogger.error("Failed to send the command to PC '" + fControlPad.getPc().getName() + "'.", e);
                    }

                    // Register changes in the state of the modifier Keys.
                    if (keyCode == KeyEvent.KEYCODE_ALT_LEFT)
                    {
                        fImeAltPressed = true;
                    }
                    else if (keyCode == KeyEvent.KEYCODE_SHIFT_LEFT)
                    {
                        fImeShiftPressed = true;
                    }
                }
                // If the Key has been released.
                else if (event.getAction() == KeyEvent.ACTION_UP)
                {
                    // Register changes in the state of the modifier Keys.
                    if (keyCode == KeyEvent.KEYCODE_ALT_LEFT)
                    {
                        fImeAltPressed = false;
                    }
                    else if (keyCode == KeyEvent.KEYCODE_SHIFT_LEFT)
                    {
                        fImeShiftPressed = false;
                    }

                    try
                    {
                        // Do not send ALT Key commands.
                        if (keyCode != KeyEvent.KEYCODE_ALT_LEFT)
                        {
                            Key key = new Key();
                            key.loadFromAndroidKeyCode(fControlPad, keyCode, fImeAltPressed, fImeShiftPressed);

                            client.sendCommandViaTcp("keyRelease(" + key.getServerCode() + ");");

                            // If shift is required but was not been pressed by the IME (it must have been pressed manually), release is manually.
                            if (key.isServerShiftRequired() && !fImeShiftPressed)
                            {
                                client.sendCommandViaTcp("keyRelease(" + SHIFT_KEY_SERVER_CODE + ");");
                            }
                        }
                    }
                    catch (IOException e)
                    {
                        fLogger.error("Failed to send the command to PC '" + fControlPad.getPc().getName() + "'.", e);
                    }
                }
            }
            // Otherwise, make sure the user has been notified that the PC Remote Client is not currently connected to a server.
            else
            {
                fControlPad.getConnection().disconnect(fControlPad.getPc());
            }
        }

        return (false);
    }
}
