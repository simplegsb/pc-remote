/*
    This file is part of PC Remote.

    PC Remote is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    PC Remote is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with PC Remote. If not, see <http://www.gnu.org/licenses/>.
 */
package com.se.pcremote.server;

import java.net.Socket;

import org.apache.log4j.Logger;

/**
 * <p>
 * Handles commands received from a single PC Remote Client via TCP.
 * </p>
 * 
 * @author Gary Buyn
 */
public class TcpClient extends com.se.devenvy.net.TcpClient
{
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
     * Creates an instance of <code>PCRemoteTcpClient</code>.
     * </p>
     * 
     * @param socket The socket over which the TCP connection is made.
     * @param commandExecuter Executes the commands.
     */
    public TcpClient(final Socket socket, final CommandExecuter commandExecuter)
    {
        super(socket);
        fCommandExecuter = commandExecuter;

        fLogger = Logger.getLogger(getClass());
    }

    @Override
    protected void onReceiveData(final byte[] data, final int dataLength)
    {
        String commands = new String(data, 0, dataLength);

        for (String command : commands.split(";"))
        {
            try
            {
                fCommandExecuter.executeCommand(command);
            }
            catch (Exception e)
            {
                fLogger.error("Failed to execute the command '" + command + "'", e);
            }
        }
    }
}
