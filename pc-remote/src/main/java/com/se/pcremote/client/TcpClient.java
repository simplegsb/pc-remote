package com.se.pcremote.client;

import java.net.Socket;

/**
 * <p>
 * Handles commands received from a PC Remote Server via TCP.
 * </p>
 * 
 * @author Gary Buyn
 */
public class TcpClient extends com.se.devenvy.net.TcpClient
{
    /**
     * <p>
     * Creates an instance of <code>PCRemoteTcpClient</code>.
     * </p>
     * 
     * @param socket The socket over which the TCP connection is made.
     */
    public TcpClient(final Socket socket)
    {
        super(socket);
    }

    @Override
    protected void onReceiveData(final byte[] data, final int dataLength)
    {}
}
