package com.se.pcremote.android.ui.controlpad;

import java.io.IOException;

import org.apache.log4j.Logger;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 * <p>
 * Sends commands to the active {@link com.se.pcremote.android.PC PC} in response to the vertical scrolling mouse pad UI events.
 * </p>
 * 
 * @author Gary Buyn
 */
public class MousePadVerticalListener extends SimpleOnGestureListener
{
    /**
     * <p>
     * The factor to apply to the raw event when converting to scroll 'units'.
     * </p>
     */
    private static final float SCROLL_FACTOR = -0.1f;

    /**
     * <p>
     * The {@link com.se.pcremote.android.ui.controlpad.ControlPad ControlPad} this <code>MousePadVerticalListener</code> sends commands to the active
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
     * The current amount of vertical scroll. This is maintained so that events that result in less than 1 'unit' of scroll still contribute to the
     * final amount of scrolling that is performed.
     * </p>
     */
    private float fVerticalScroll;

    /**
     * <p>
     * Creates an instance of <code>MousePadVerticalListener</code>.
     * </p>
     * 
     * @param controlPad The {@link com.se.pcremote.android.ui.controlpad.ControlPad ControlPad} this <code>MousePadVerticalListener</code> sends
     * commands to the active {@link com.se.pcremote.android.PC PC} for.
     */
    public MousePadVerticalListener(final ControlPad controlPad)
    {
        fControlPad = controlPad;
        fLogger = Logger.getLogger(getClass());
        fVerticalScroll = 0.0f;
    }

    @Override
    public boolean onScroll(final MotionEvent event1, final MotionEvent event2, final float distanceX, final float distanceY)
    {
        // If the Control Pad is currently connected to the PC Connection service.
        if (fControlPad.getConnection() != null && fControlPad.getConnection().checkConnection())
        {
            fVerticalScroll += distanceY * SCROLL_FACTOR;

            if (fVerticalScroll > 1.0f)
            {
                try
                {
                    fControlPad.getConnection().getClient().sendCommandViaTcp("mouseWheel(" + fVerticalScroll + ");");
                    fVerticalScroll -= Math.floor(fVerticalScroll);
                    fLogger.debug("Resetting to: " + fVerticalScroll);
                }
                catch (IOException e)
                {
                    fLogger.error("Failed to send the command to PC '" + fControlPad.getPc().getName() + "'.", e);
                }
            }
            else if (fVerticalScroll < -1.0f)
            {
                try
                {
                    fControlPad.getConnection().getClient().sendCommandViaTcp("mouseWheel(" + fVerticalScroll + ");");
                    fVerticalScroll -= Math.ceil(fVerticalScroll);
                    fLogger.debug("Resetting to: " + fVerticalScroll);
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
