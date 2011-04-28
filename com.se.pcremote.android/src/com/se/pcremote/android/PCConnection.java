package com.se.pcremote.android;

import java.io.IOException;

import org.apache.log4j.Logger;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.se.pcremote.client.PCRemoteClient;

/**
 * <p>
 * Maintains a connection to a {@link com.se.pcremote.PCRemoteServer PCRemoteServer}.
 * </p>
 * 
 * @author Gary Buyn
 */
public class PCConnection extends Service
{
    /**
     * <p>
     * Allows clients to bind directly to this <code>PCConnection</code>.
     * </p>
     * 
     * @author Gary Buyn
     */
    public class PCConnectionBinder extends Binder
    {
        /**
         * <p>
         * Retrieves this <code>PCConnection</code> itself as the interface to this <code>PCConnection</code>.
         * </p>
         * 
         * @return This <code>PCConnection</code> itself.
         */
        public PCConnection getService()
        {
            return PCConnection.this;
        }
    }

    /**
     * <p>
     * The duration to display the 'PC disconnected' connection status notification for.
     * </p>
     */
    private static final int NOTIFY_CONNECTION_STATUS_DISCONNECTED_DURATION = 3000;

    /**
     * <p>
     * The ID of the connection status notification.
     * </p>
     */
    private static final int NOTIFY_CONNECTION_STATUS_ID = 0;

    /**
     * <p>
     * The client that creates the connection to the {@link com.se.pcremote.PCRemoteServer PCRemoteServer}.
     * </p>
     */
    private PCRemoteClient fClient;

    /**
     * <p>
     * The thread on which the connection to the {@link com.se.pcremote.PCRemoteServer PCRemoteServer} is made.
     * </p>
     */
    private Thread fConnectionThread;

    /**
     * <p>
     * Logs messages associated with this class.
     * </p>
     */
    private Logger fLogger;

    /**
     * <p>
     * Creates an instance of <code>PCConnection</code>.
     * </p>
     */
    public PCConnection()
    {
        fClient = null;
        fConnectionThread = null;
        fLogger = Logger.getLogger(this.getClass());
    }

    /**
     * <p>
     * Connects to the given {@link com.se.pcremote.android.PC PC} on a separate thread.
     * </p>
     * 
     * @param pc The <code>PC</code> to connect to.
     */
    public void connect(final PC pc)
    {
        if (pc == null)
        {
            return;
        }

        boolean connectionRequired = true;

        if (fClient != null)
        {
            // If a connection already exists and the active PCs host and port are the same as the existing client's host and port.
            if (fClient.isConnected() && fClient.getServerHost().equals(pc.getHost()) && fClient.getServerPort() == pc.getPort())
            {
                connectionRequired = false;
            }
            else
            {
                try
                {
                    fClient.dispose();
                }
                catch (IOException e)
                {
                    fLogger.error("Failed to disconnect from PC '" + pc.getName() + "'.", e);
                }
            }
        }

        if (connectionRequired)
        {
            notifyConnecting(pc);

            fConnectionThread = new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                        fClient = new PCRemoteClient(pc.getHost(), pc.getPort());
                        fClient.init();

                        if (!Thread.interrupted())
                        {
                            notifyConnected(pc);
                        }
                    }
                    catch (IOException e)
                    {
                        if (!Thread.interrupted())
                        {
                            notifyConnectionFailed(pc);
                        }

                        fLogger.error("Failed to connect to PC '" + pc.getName() + "'.", e);
                    }
                }
            };
            fConnectionThread.start();
        }
    }

    /**
     * <p>
     * Disconnects from the given {@link com.se.pcremote.android.PC PC}.
     * </p>
     * 
     * @param pc The <code>PC</code> to disconnect from.
     */
    public void disconnect(final PC pc)
    {
        if (fClient != null)
        {
            Thread disconnectionThread = new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                        if (fConnectionThread != null && fConnectionThread.isAlive())
                        {
                            fConnectionThread.interrupt();
                            synchronized (fConnectionThread)
                            {
                                fConnectionThread.wait();
                            }
                        }

                        if (fClient.isConnected())
                        {
                            fClient.dispose();
                            notifyDisconnected(pc);
                        }
                    }
                    catch (Exception e)
                    {
                        fLogger.error("Failed to disconnect from PC '" + pc.getName() + "'.", e);
                    }
                }
            };
            disconnectionThread.start();
        }
    }

    /**
     * <p>
     * Retrieves the client that creates the connection to the {@link com.se.pcremote.PCRemoteServer PCRemoteServer}.
     * </p>
     * 
     * @return The client that creates the connection to the <code>PCRemoteServer</code>.
     */
    public PCRemoteClient getClient()
    {
        return (fClient);
    }

    /**
     * <p>
     * Displays the 'PC connected' connection status notification.
     * </p>
     * 
     * @param pc The {@link com.se.pcremote.android.PC PC} that the maintained connection is to.
     */
    private void notifyConnected(final PC pc)
    {
        String connecting = "Connected to PC '" + pc.getName() + "'";
        Notification notification = new Notification(R.drawable.pc_connect, connecting, System.currentTimeMillis());
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, null, 0);
        notification.setLatestEventInfo(this, getString(R.string.connection_status), connecting, contentIntent);
        notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(NOTIFY_CONNECTION_STATUS_ID, notification);
    }

    /**
     * <p>
     * Displays the 'PC connecting' connection status notification.
     * </p>
     * 
     * @param pc The {@link com.se.pcremote.android.PC PC} that the maintained connection is to.
     */
    private void notifyConnecting(final PC pc)
    {
        String connecting = "Connecting to PC '" + pc.getName() + "'";
        Notification notification = new Notification(R.drawable.pc_connect, connecting, System.currentTimeMillis());
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, null, 0);
        notification.setLatestEventInfo(this, getString(R.string.connection_status), connecting, contentIntent);
        notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(NOTIFY_CONNECTION_STATUS_ID, notification);
    }

    /**
     * <p>
     * Displays the 'PC connection failed' connection status notification.
     * </p>
     * 
     * @param pc The {@link com.se.pcremote.android.PC PC} that the maintained connection is to.
     */
    private void notifyConnectionFailed(final PC pc)
    {
        String connecting = "Failed to connect to PC '" + pc.getName() + "'";
        Notification notification = new Notification(R.drawable.pc_disconnect, connecting, System.currentTimeMillis());
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, null, 0);
        notification.setLatestEventInfo(this, getString(R.string.connection_status), connecting, contentIntent);
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(NOTIFY_CONNECTION_STATUS_ID, notification);
    }

    /**
     * <p>
     * Displays the 'PC disconnected' connection status notification.
     * </p>
     * 
     * @param pc The {@link com.se.pcremote.android.PC PC} that the maintained connection is to.
     */
    private void notifyDisconnected(final PC pc)
    {
        String connecting = "Disconnected from PC '" + pc.getName() + "'";
        Notification notification = new Notification(R.drawable.pc_disconnect, connecting, System.currentTimeMillis());
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, null, 0);
        notification.setLatestEventInfo(this, getString(R.string.connection_status), connecting, contentIntent);
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(NOTIFY_CONNECTION_STATUS_ID, notification);

        Thread notifyDisconnectedThread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(NOTIFY_CONNECTION_STATUS_DISCONNECTED_DURATION);
                }
                catch (InterruptedException e)
                {}

                ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(NOTIFY_CONNECTION_STATUS_ID);
            }
        };
        notifyDisconnectedThread.start();
    }

    @Override
    public IBinder onBind(final Intent intent)
    {
        return (new PCConnectionBinder());
    }
}
