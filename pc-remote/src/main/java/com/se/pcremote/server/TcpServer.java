/*
    This file is part of PC Remote.

    PC Remote is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    PC Remote is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with PC Remote. If not, see <http://www.gnu.org/licenses/>.
 */
package com.se.pcremote.server;

import java.net.ServerSocket;
import java.net.Socket;

import com.se.pcremote.Client;

/**
 * <p>
 * Handles new PC Remote Client connection requests.
 * </p>
 * 
 * @author Gary Buyn
 */
public class TcpServer extends com.se.pcremote.TcpServer
{
    /**
     * <p>
     * Executes the commands.
     * </p>
     */
    private CommandExecuter fCommandExecuter;

    /**
     * <p>
     * Creates an instance of <code>TcpServer</code>.
     * </p>
     * 
     * @param serverSocket The {@link java.net.Socket Socket} listening for new connections.
     * @param commandExecuter Executes the commands.
     */
    public TcpServer(final ServerSocket serverSocket, final CommandExecuter commandExecuter)
    {
        super(serverSocket);

        fCommandExecuter = commandExecuter;
    }

    @Override
    protected Client getClientInstance(final Socket socket)
    {
        return (new TcpClient(socket, fCommandExecuter));
    }
}
