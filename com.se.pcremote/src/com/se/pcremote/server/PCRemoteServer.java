/*
    This file is part of PC Remote.

    PC Remote is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    PC Remote is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with PC Remote. If not, see <http://www.gnu.org/licenses/>.
 */
package com.se.pcremote.server;

import java.awt.AWTException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import org.apache.log4j.Logger;

/**
 * <p>
 * A lightweight server that can control the input devices of a PC. A client connected to this server can communicate with it using either the TCP or
 * UDP protocol.
 * </p>
 * 
 * <p>
 * The commands that can be received by this server are detailed {@link com.se.pcremote.server.CommandExecuter here}.
 * 
 * <p>
 * Multiple commands can be received at once by simply concatenating them together e.g. "mousePress(1);mouseRelease(1);"
 * </p>
 * 
 * @author Gary Buyn
 */
public class PCRemoteServer
{
    /**
     * <p>
     * The default port on which this <code>PCRemoteServer</code> will listen.
     * </p>
     */
    public static final int DEFAULT_PORT = 10999;

    /**
     * <p>
     * Logs messages associated with this class.
     * </p>
     */
    private static Logger fLogger;

    /**
     * <p>
     * Checks the validity of the arguments provided to the command line utility.
     * </p>
     * 
     * @param args The arguments provided to the command line utility when it was started.
     */
    public static void checkArgs(final String[] args)
    {
        if (args.length == 0)
        {}
        else if (args.length == 1)
        {
            try
            {
                Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e)
            {
                throw new IllegalArgumentException("An invalid port was specified (must be an integer).");
            }
        }
        else
        {
            throw new IllegalArgumentException(
                    "The wrong number of arguments were specified (nothing must be specified, the port can be optionally specified).");
        }
    }

    /**
     * <p>
     * Runs the server until ENTER is pressed in the terminal.
     * </p>
     * 
     * @param args The arguments provided to the server when it was started.
     */
    public static void main(final String[] args)
    {
        fLogger = Logger.getLogger(PCRemoteServer.class);

        try
        {
            checkArgs(args);
        }
        catch (IllegalArgumentException e)
        {
            fLogger.fatal(e.getMessage());
            printUsage();
            System.exit(1);
        }

        try
        {
            // Determine server host and port.
            int port = DEFAULT_PORT;
            if (args.length == 1)
            {
                port = Integer.parseInt(args[0]);
            }

            fLogger.info("#########################");
            fLogger.info("PC Remote 1.0 Server");
            fLogger.info("simple entertainment");
            fLogger.info("Gary Buyn");
            fLogger.info("#########################");
            fLogger.info("Starting server...");

            PCRemoteServer server = new PCRemoteServer(port);
            server.start();

            fLogger.info("...Done.");
            fLogger.info("Server running at:");
            fLogger.info("Host: " + InetAddress.getLocalHost().getHostName());
            fLogger.info("IP: " + InetAddress.getLocalHost().getHostAddress()); // FIXME Incorrect IP address
            fLogger.info("Port: " + port);
            fLogger.info("Press ENTER in the terminal to stop this server.");

            // Wait until ENTER is pressed in the terminal.
            System.in.read();

            fLogger.info("Stopping server...");

            server.stop();

            fLogger.info("...Done.");
        }
        catch (Exception e)
        {
            fLogger.fatal("Epic fail!", e);
        }
    }

    /**
     * <p>
     * Prints usage instructions for the command line utility.
     * </p>
     */
    public static void printUsage()
    {
        fLogger.info("Usage:");
        fLogger.info("\tPCRemoteServer [port]");
        fLogger.info("Where:");
        fLogger.info("\t port = The port on which the PCRemoteServer will listen (default is 10999).");
    }

    /**
     * <p>
     * The accepter of new connections requested by clients.
     * </p>
     */
    private TcpServer fTcpServer;

    /**
     * <p>
     * Executes the commands.
     * </p>
     */
    private CommandExecuter fCommandExecuter;

    /**
     * <p>
     * The port on which this <code>PCRemoteServer</code> will listen.
     * </p>
     */
    private int fPort;

    /**
     * <p>
     * Creates an instance of <code>PCRemoteServer</code>.
     * </p>
     * 
     * @throws AWTException Thrown if the {@link java.awt.Robot Robot} is not supported by the platform configuration.
     */
    public PCRemoteServer() throws AWTException
    {
        fTcpServer = null;
        fCommandExecuter = new CommandExecuter();
        fLogger = Logger.getLogger(PCRemoteServer.class);
        fPort = DEFAULT_PORT;
    }

    /**
     * <p>
     * Creates an instance of <code>PCRemoteServer</code>.
     * </p>
     * 
     * @param port The port on which this <code>PCRemoteServer</code> will listen.
     * 
     * @throws AWTException Thrown if the {@link java.awt.Robot Robot} is not supported by the platform configuration.
     */
    public PCRemoteServer(final int port) throws AWTException
    {
        fTcpServer = null;
        fCommandExecuter = new CommandExecuter();
        fLogger = Logger.getLogger(PCRemoteServer.class);
        fPort = port;
    }

    /**
     * <p>
     * Retrieves the accepter of new connections requested by clients.
     * </p>
     * 
     * @return The accepter of new connections requested by clients.
     */
    public TcpServer getTcpServer()
    {
        return (fTcpServer);
    }

    /**
     * <p>
     * Binds the server to a port and starts the {@link ConnectionAccepter}. The default port is 10999.
     * </p>
     * 
     * @throws IOException Thrown if the server fails to be started.
     */
    public void start() throws IOException
    {
        fTcpServer = new TcpServer(new ServerSocket(fPort), fCommandExecuter);

        new Thread(fTcpServer).start();
    }

    /**
     * <p>
     * Closes all client connections and stops the server.
     * </p>
     * 
     * @throws IOException Thrown if the server fails to be stopped.
     */
    public void stop() throws IOException
    {
        fTcpServer.dispose();
    }
}
