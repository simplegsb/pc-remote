package com.se.pcremote.android.ui;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.se.pcremote.client.PCRemoteClient;

import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

public class ControlPadListener extends SimpleOnGestureListener
{
    private PCRemoteClient fClient;

    private Logger fLogger;

    public ControlPadListener(PCRemoteClient client)
    {
        fClient = client;
        fLogger = Logger.getLogger(this.getClass());
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
    {
        if (fClient != null && fClient.isConnected())
        {
            try
            {
                fClient.sendCommandViaUdp("mouseMoveRelative(" + distanceX + "," + distanceY + ")");
            }
            catch (IOException e)
            {
                fLogger.error("Failed to send the command to the PC.", e);
            }
        }

        return (true);
    }
}
