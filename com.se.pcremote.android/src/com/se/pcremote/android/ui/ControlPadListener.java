package com.se.pcremote.android.ui;

import java.io.IOException;

import org.apache.log4j.Logger;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.se.pcremote.client.PCRemoteClient;

/**
 * <p>
 * Sends commands to the active {@link com.se.pcremote.android.PC PC} in response to UI events.
 * </p>
 * 
 * @author simple
 */
public class ControlPadListener extends SimpleOnGestureListener implements OnClickListener
{
    /**
     * <p>
     * The ID of the key click command.
     * </p>
     */
    public static final int KEY_CLICK = 0;

    /**
     * <p>
     * The ID of the mouse button click command.
     * </p>
     */
    public static final int MOUSE_BUTTON_CLICK = 1;

    /**
     * <p>
     * The ID of the mouse movement command.
     * </p>
     */
    public static final int MOUSE_MOVE_RELATIVE = 2;

    /**
     * <p>
     * The command to send to the active {@link com.se.pcremote.android.PC PC}.
     * </p>
     */
    private int fCommand;

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
     * The first parameter to send with the command.
     * </p>
     */
    private int fParameter1;

    /**
     * <p>
     * The second parameter to send with the command.
     * </p>
     */
    private int fParameter2;

    /**
     * <p>
     * Creates an instance of <code>ControlPadListener</code>.
     * </p>
     * 
     * @param controlPad The {@link com.se.pcremote.android.ui.ControlPad ControlPad} this <code>ControlPadListener</code> sends commands to the
     * active {@link com.se.pcremote.android.PC PC} for.
     * @param command The command to send to the active {@link com.se.pcremote.android.PC PC}.
     */
    public ControlPadListener(final ControlPad controlPad, final int command)
    {
        fCommand = command;
        fControlPad = controlPad;
        fLogger = Logger.getLogger(this.getClass());
        fParameter1 = 0;
        fParameter2 = 0;
    }

    /**
     * <p>
     * Creates an instance of <code>ControlPadListener</code>.
     * </p>
     * 
     * @param controlPad The {@link com.se.pcremote.android.ui.ControlPad ControlPad} this <code>ControlPadListener</code> sends commands to the
     * active {@link com.se.pcremote.android.PC PC} for.
     * @param command The command to send to the active {@link com.se.pcremote.android.PC PC}.
     * @param parameter1 The first parameter to send with the command.
     */
    public ControlPadListener(final ControlPad controlPad, final int command, final int parameter1)
    {
        fCommand = command;
        fControlPad = controlPad;
        fLogger = Logger.getLogger(this.getClass());
        fParameter1 = parameter1;
        fParameter2 = 0;
    }

    /**
     * <p>
     * Creates an instance of <code>ControlPadListener</code>.
     * </p>
     * 
     * @param controlPad The {@link com.se.pcremote.android.ui.ControlPad ControlPad} this <code>ControlPadListener</code> sends commands to the
     * active {@link com.se.pcremote.android.PC PC} for.
     * @param command The command to send to the active {@link com.se.pcremote.android.PC PC}.
     * @param parameter1 The first parameter to send with the command.
     * @param parameter2 The second parameter to send with the command.
     */
    public ControlPadListener(final ControlPad controlPad, final int command, final int parameter1, final int parameter2)
    {
        fCommand = command;
        fControlPad = controlPad;
        fLogger = Logger.getLogger(this.getClass());
        fParameter1 = parameter1;
        fParameter2 = parameter2;
    }

    @Override
    public void onClick(final View view)
    {
        sendCommand();
    }

    @Override
    public boolean onScroll(final MotionEvent event1, final MotionEvent event2, final float distanceX, final float distanceY)
    {
        fParameter1 = (int) distanceX;
        fParameter2 = (int) distanceY;

        sendCommand();

        return (true);
    }

    /**
     * <p>
     * Sends the command to the active {@link com.se.pcremote.android.PC PC}.
     * </p>
     */
    private void sendCommand()
    {
        if (fControlPad.getConnection() != null)
        {
            PCRemoteClient client = fControlPad.getConnection().getClient();
            if (client != null && client.isConnected())
            {
                try
                {
                    if (fCommand == KEY_CLICK)
                    {
                        client.sendCommandViaTcp("keyPress(" + fParameter1 + ");keyRelease(" + fParameter1 + ");");
                    }
                    else if (fCommand == MOUSE_BUTTON_CLICK)
                    {
                        client.sendCommandViaTcp("mousePress(" + fParameter1 + ");mouseRelease(" + fParameter1 + ");");
                    }
                    else if (fCommand == MOUSE_MOVE_RELATIVE)
                    {
                        client.sendCommandViaTcp("mouseMoveRelative(" + fParameter1 + "," + fParameter2 + ");");
                    }
                }
                catch (IOException e)
                {
                    fLogger.error("Failed to send the command to PC '" + fControlPad.getPc().getName() + "'.", e);
                }
            }
        }
    }
}
