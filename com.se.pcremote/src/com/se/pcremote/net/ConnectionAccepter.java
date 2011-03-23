/*
    This file is part of PC Remote.

    PC Remote is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    PC Remote is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with PC Remote. If not, see <http://www.gnu.org/licenses/>.
 */
package com.se.pcremote.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * <p>
 * Accepts requests on behalf of a {@link java.net.ServerSocket ServerSocket} and handles closure of the <code>ServerSocket</code>.
 * </p>
 * 
 * @author Gary Buyn
 */
public class ConnectionAccepter
{
    /**
     * <p>
     * The message of a {@link java.net.SocketException SocketException} that signifies a {@link java.net.Socket Socket} has been closed.
     * </p>
     */
    private static final String SOCKET_CLOSED_MESSAGE = "Socket is closed";

    /**
     * <p>
     * The {@link java.net.ServerSocket ServerSocket} to accept connections for.
     * </p>
     */
    private ServerSocket fSocket;

    /**
     * <p>
     * Creates an instance of <code>ConnectionAccepter</code>.
     * </p>
     * 
     * @param socket The {@link java.net.ServerSocket ServerSocket} to accept connections for.
     */
    public ConnectionAccepter(final ServerSocket socket)
    {
        fSocket = socket;
    }

    /**
     * <p>
     * Accepts a connection from a new client and handles closure of the {@link java.net.ServerSocket ServerSocket}, returning <code>null</code> when
     * it occurs.
     * </p>
     * 
     * @throws IOException Thrown if the connection fails.
     * 
     * @return The connection to the new client, or <code>null</code> if the {@link java.net.ServerSocket ServerSocket} has been closed.
     */
    public Socket acceptConnection() throws IOException
    {
        try
        {
            return (fSocket.accept());
        }
        catch (SocketException e)
        {
            // If the server socket was closed.
            if (e.getMessage().equals(SOCKET_CLOSED_MESSAGE))
            {
                return (null);
            }
            else
            {
                throw e;
            }
        }
    }
}
