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
     * The duration to display the automatically cancelling notifications for.
     * </p>
     */
    private static final int AUTO_CANCEL_DURATION = 3000;

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
     * Determines if a notification that the connection is closed has been displayed to the user since the last connection attempt.
     * </p>
     */
    private boolean fNotifiedOfDisconnection;

    /**
     * <p>
     * The {@link com.se.pcremote.android.PC PC} that the maintained connection is to.
     * </p>
     */
    private PC fPc;

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
        fPc = null;
        fNotifiedOfDisconnection = false;
    }

    /**
     * <p>
     * Determines if the connection is open. If it is not it creates a 'PC disconnected' notification.
     * </p>
     * 
     * @return True if the connection is open, false otherwise.
     */
    public boolean checkConnection()
    {
        boolean connected = false;

        if (fClient != null)
        {
            if (fClient.isConnected())
            {
                connected = true;
            }
            else if (fConnectionThread != null && !fConnectionThread.isAlive())
            {
                notifyDisconnected();
            }
        }

        return (connected);
    }

    /**
     * <p>
     * Connects to the {@link com.se.pcremote.android.PC PC} on a separate thread.
     * </p>
     */
    public void connect()
    {
        if (fClient == null || !fClient.isConnected())
        {
            notifyConnecting();
            fNotifiedOfDisconnection = false;

            fConnectionThread = new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                        fClient = new PCRemoteClient(fPc.getHost(), fPc.getPort());
                        fClient.init();

                        if (!Thread.interrupted())
                        {
                            notifyConnected();
                        }
                    }
                    catch (IOException e)
                    {
                        if (!Thread.interrupted())
                        {
                            notifyConnectionFailed();
                        }

                        fLogger.error("Failed to connect to PC '" + fPc.getName() + "'.", e);
                    }
                }
            };
            fConnectionThread.start();
        }
    }

    /**
     * <p>
     * Disconnects from the {@link com.se.pcremote.android.PC PC}.
     * </p>
     */
    public void disconnect()
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
                            fConnectionThread.join();
                        }

                        fClient.dispose();
                        notifyDisconnected();
                    }
                    catch (Exception e)
                    {
                        fLogger.error("Failed to disconnect from PC '" + fPc.getName() + "'.", e);
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
     */
    private void notifyConnected()
    {
        String connecting = "Connected to PC '" + fPc.getName() + "'";
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
     */
    private void notifyConnecting()
    {
        String connecting = "Connecting to PC '" + fPc.getName() + "'";
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
     */
    private void notifyConnectionFailed()
    {
        if (!fNotifiedOfDisconnection)
        {
            String connecting = "Failed to connect to PC '" + fPc.getName() + "'";
            Notification notification = new Notification(R.drawable.pc_disconnect, connecting, System.currentTimeMillis());
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, null, 0);
            notification.setLatestEventInfo(this, getString(R.string.connection_status), connecting, contentIntent);
            notification.flags = Notification.FLAG_AUTO_CANCEL;

            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(NOTIFY_CONNECTION_STATUS_ID, notification);

            Thread notifyConnectionFailedThread = new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                        Thread.sleep(AUTO_CANCEL_DURATION);
                    }
                    catch (InterruptedException e)
                    {}

                    ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(NOTIFY_CONNECTION_STATUS_ID);
                }
            };
            notifyConnectionFailedThread.start();

            fNotifiedOfDisconnection = true;
        }
    }

    /**
     * <p>
     * Displays the 'PC disconnected' connection status notification.
     * </p>
     */
    private void notifyDisconnected()
    {
        if (!fNotifiedOfDisconnection)
        {
            String connecting = "Disconnected from PC '" + fPc.getName() + "'";
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
                        Thread.sleep(AUTO_CANCEL_DURATION);
                    }
                    catch (InterruptedException e)
                    {}

                    ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(NOTIFY_CONNECTION_STATUS_ID);
                }
            };
            notifyDisconnectedThread.start();

            fNotifiedOfDisconnection = true;
        }
    }

    @Override
    public IBinder onBind(final Intent intent)
    {
        return (new PCConnectionBinder());
    }

    @Override
    public void onDestroy()
    {
        disconnect();

        super.onDestroy();
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId)
    {
        super.onStartCommand(intent, flags, startId);

        fPc = new PC();
        fPc.load(this, intent.getData());
        connect();

        return (START_STICKY);
    }
}
