package com.se.pcremote.android.ui.controlpad;

import java.io.IOException;

import org.apache.log4j.Logger;

import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.View.OnClickListener;

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
        // If the Control Pad is currently connected to the PC Connection service.
        if (fControlPad.getConnection() != null && fControlPad.getConnection().checkConnection())
        {
            try
            {
                if (view.getId() == ControlPadView.MOUSE_BUTTON_LEFT)
                {
                    fControlPad.getConnection().getClient().sendCommandViaTcp("mousePress(1);mouseRelease(1);");
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                }
                else if (view.getId() == ControlPadView.MOUSE_BUTTON_RIGHT)
                {
                    fControlPad.getConnection().getClient().sendCommandViaTcp("mousePress(3);mouseRelease(3);");
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                }
            }
            catch (IOException e)
            {
                fLogger.error("Failed to send the command to PC '" + fControlPad.getPc().getName() + "'.", e);
            }
        }
    }
}
