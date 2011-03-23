/*
    This file is part of PC Remote.

    PC Remote is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    PC Remote is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with PC Remote. If not, see <http://www.gnu.org/licenses/>.
 */
package com.se.pcremote.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.se.pcremote.net.ConnectionAccepter;

/**
 * <p>
 * Handles new TCP connection requests.
 * </p>
 * 
 * @author Gary Buyn
 */
public class TcpAcceptanceManager implements Runnable
{

    /**
     * <p>
     * The managers of the individual client connections.
     * </p>
     */
    private Collection<TcpClientManager> fClientManagers;

    /**
     * <p>
     * Executes the commands.
     * </p>
     */
    private CommandExecuter fCommandExecuter;

    /**
     * <p>
     * Logs messages associated with this class.
     * </p>
     */
    private Logger fLogger;

    /**
     * <p>
     * The {@link java.net.Socket Socket} listening for new connections.
     * </p>
     */
    private ServerSocket fServerSocket;

    /**
     * <p>
     * Creates an instance of <code>TcpAcceptanceManager</code>.
     * </p>
     * 
     * @param serverSocket The {@link java.net.Socket Socket} listening for new connections.
     * @param commandExecuter Executes the commands.
     */
    public TcpAcceptanceManager(final ServerSocket serverSocket, final CommandExecuter commandExecuter)
    {
        fCommandExecuter = commandExecuter;
        fServerSocket = serverSocket;

        fClientManagers = new ArrayList<TcpClientManager>();
        fLogger = Logger.getLogger(getClass());
    }

    /**
     * <p>
     * Disposes of the {@link com.se.pcremote.server.TcpClientManager TcpClientManager}s and closes the {@link java.net.ServerSocket ServerSocket}.
     * </p>
     * 
     * @throws IOException Thrown if the <code>ServerSocket</code> closure fails.
     */
    public void dispose() throws IOException
    {
        for (TcpClientManager clientManager : fClientManagers)
        {
            try
            {
                clientManager.dispose();
            }
            catch (IOException e)
            {
                fLogger.error("Failed to dispose of TcpClientManager '" + clientManager + "'", e);
            }
        }

        fServerSocket.close();
    }

    /**
     * <p>
     * Retrieves the managers of the individual client connections.
     * </p>
     * 
     * @return The managers of the individual client connections.
     */
    public Collection<TcpClientManager> getTcpClientManagers()
    {
        return (fClientManagers);
    }

    @Override
    public void run()
    {
        try
        {
            ConnectionAccepter accepter = new ConnectionAccepter(fServerSocket);

            // While the server is still accepting new connections.
            while (!fServerSocket.isClosed())
            {
                // If a connection fails, continue to allow new connection attempts.
                try
                {
                    // Wait for a new connection to be requested. When it is requested, assign a new TcpClientManager to the connection and start it.
                    Socket clientConnection = accepter.acceptConnection();
                    if (clientConnection != null)
                    {
                        TcpClientManager clientManager = new TcpClientManager(clientConnection, fCommandExecuter);
                        new Thread(clientManager).start();

                        fClientManagers.add(clientManager);

                        fLogger.debug("Accepted connection.");
                    }
                }
                catch (Exception e)
                {
                    fLogger.error("Failed to accept connection.", e);
                }
            }
        }
        catch (Exception e)
        {
            fLogger.fatal("Epic fail!", e);
        }
    }
}
