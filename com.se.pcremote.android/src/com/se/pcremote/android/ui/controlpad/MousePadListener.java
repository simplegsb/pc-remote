package com.se.pcremote.android.ui.controlpad;

import java.io.IOException;

import org.apache.log4j.Logger;

import android.preference.PreferenceManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;

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
     * The {@link android.view.View View} of the mouse pad.
     * </p>
     */
    private View fMousePadView;

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
     * @param mousePadView The {@link android.view.View View} of the mouse pad.
     */
    public MousePadListener(final ControlPad controlPad, final View mousePadView)
    {
        fControlPad = controlPad;
        fLogger = Logger.getLogger(getClass());
        fMousePadView = mousePadView;
        fMouseSensitivity = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(fControlPad).getString("mouseSensitivity", "1"));
    }

    @Override
    public boolean onDoubleTap(final MotionEvent event)
    {
        // If the Control Pad is currently connected to the PC Connection service.
        if (fControlPad.getConnection() != null && fControlPad.getConnection().checkConnection())
        {
            try
            {
                fControlPad.getConnection().getClient().sendCommandViaTcp("mousePress(1);mouseRelease(1);mousePress(1);mouseRelease(1);");
            }
            catch (IOException e)
            {
                fLogger.error("Failed to send the command to PC '" + fControlPad.getPc().getName() + "'.", e);
            }
        }

        return (true);
    }

    @Override
    public boolean onScroll(final MotionEvent event1, final MotionEvent event2, final float distanceX, final float distanceY)
    {
        // If the Control Pad is currently connected to the PC Connection service.
        if (fControlPad.getConnection() != null && fControlPad.getConnection().checkConnection())
        {
            try
            {
                fControlPad.getConnection().getClient()
                        .sendCommandViaTcp("mouseMoveRelative(" + distanceX * fMouseSensitivity + "," + distanceY * fMouseSensitivity + ");");
            }
            catch (IOException e)
            {
                fLogger.error("Failed to send the command to PC '" + fControlPad.getPc().getName() + "'.", e);
            }
        }

        return (true);
    }

    @Override
    public void onLongPress(final MotionEvent event)
    {
        super.onLongPress(event);

        // If the Control Pad is currently connected to the PC Connection service.
        if (fControlPad.getConnection() != null && fControlPad.getConnection().checkConnection())
        {
            try
            {
                fControlPad.getConnection().getClient().sendCommandViaTcp("mousePress(1);");
                fMousePadView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            }
            catch (IOException e)
            {
                fLogger.error("Failed to send the command to PC '" + fControlPad.getPc().getName() + "'.", e);
            }
        }
    }

    @Override
    public boolean onSingleTapConfirmed(final MotionEvent event)
    {
        // If the Control Pad is currently connected to the PC Connection service.
        if (fControlPad.getConnection() != null && fControlPad.getConnection().checkConnection())
        {
            try
            {
                fControlPad.getConnection().getClient().sendCommandViaTcp("mousePress(1);mouseRelease(1);");
            }
            catch (IOException e)
            {
                fLogger.error("Failed to send the command to PC '" + fControlPad.getPc().getName() + "'.", e);
            }
        }

        return (true);
    }
}
