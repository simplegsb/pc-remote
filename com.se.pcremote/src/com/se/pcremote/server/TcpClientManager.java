/*
    This file is part of PC Remote.

    PC Remote is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    PC Remote is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with PC Remote. If not, see <http://www.gnu.org/licenses/>.
 */
package com.se.pcremote.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.se.pcremote.net.SocketReader;

/**
 * <p>
 * Handles commands received from a single client via TCP.
 * </p>
 * 
 * @author Gary Buyn
 */
public class TcpClientManager implements Runnable
{
    /**
     * <p>
     * Executes the commands.
     * </p>
     */
    private CommandExecuter fCommandExecuter;

    /**
     * <p>
     * Reads commands from the client.
     * </p>
     */
    private BufferedReader fCommandReader;

    /**
     * <p>
     * Logs messages associated with this class.
     * </p>
     */
    private Logger fLogger;

    /**
     * <p>
     * The connection to the client.
     * </p>
     */
    private Socket fSocket;

    /**
     * <p>
     * Creates an instance of <code>TcpClientManager</code>.
     * </p>
     * 
     * @param socket The connection to the client.
     * @param commandExecuter Executes the commands.
     * 
     * @throws IOException Thrown if the {@link java.io.InputStream InputStream} from the client cannot be retrieved.
     */
    public TcpClientManager(final Socket socket, final CommandExecuter commandExecuter) throws IOException
    {
        fCommandExecuter = commandExecuter;
        fSocket = socket;

        fLogger = Logger.getLogger(getClass());
        fCommandReader = new BufferedReader(new SocketReader(socket));
    }

    /**
     * <p>
     * Closes the connection to the client.
     * </p>
     * 
     * @throws IOException Thrown if the client connection closure fails.
     */
    public void dispose() throws IOException
    {
        fSocket.close();
    }

    @Override
    public void run()
    {
        try
        {
            // While the connection to the client is open.
            while (!fSocket.isClosed())
            {
                // If a read fails, continue to attempt more reads.
                try
                {
                    String commands = fCommandReader.readLine();

                    // If the socket has not been closed.
                    if (commands != null)
                    {
                        for (String command : commands.split(";"))
                        {
                            fCommandExecuter.executeCommand(command);
                        }
                    }
                }
                catch (Exception e)
                {
                    fLogger.error("Failed to read command from client.", e);
                }
            }
        }
        catch (Exception e)
        {
            fLogger.fatal("Epic fail!", e);
        }
    }
}
