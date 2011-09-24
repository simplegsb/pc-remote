/*
    This file is part of PC Remote.

    PC Remote is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    PC Remote is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with PC Remote. If not, see <http://www.gnu.org/licenses/>.
 */
package com.se.pcremote.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import org.apache.log4j.Logger;

import com.se.pcremote.Client;
import com.se.pcremote.server.PCRemoteServer;

/**
 * <p>
 * A lightweight client designed to communicate with a {@link com.se.pcremote.server.PCRemoteServer PCRemoteServer} using either the TCP or UDP
 * protocol. This class can be thought of as the entire model portion for a richer MVC client with a GUI etc.
 * </p>
 * 
 * <p>
 * The client is very simple and easy to use:
 * </p>
 * 
 * <ul>
 * <li>Instantiate <code>PCRemoteClient</code> and call the {@link #init() init()} method to connect to the server.</li>
 * <li>Send any combination of TCP and UDP commands to the server using the {@link #sendCommandViaTcp(String) sendCommandViaTcp(String)} and
 * {@link #sendCommandViaUdp(String) sendCommandViaUdp(String)} methods respectively. For a description of the commands that can be sent to the
 * server, see {@link com.se.pcremote.server.PCRemoteServer PCRemoteServer}.</li>
 * <li>Call the {@link #dispose() dispose()} method to disconnect from the server.</li>
 * </ul>
 * 
 * <p>
 * This class also provides a basic client that can be run from the terminal.
 * </p>
 * 
 * @author Gary Buyn
 */
public class PCRemoteClient
{
    /**
     * <p>
     * The command to enter into the command line utility to cause the client to quit.
     * </p>
     */
    private static final String QUIT_COMMAND = "quit";

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
        if (args.length == 1)
        {
            // The single argument should be the host name or IP address, not bothering to check validity of this argument.
        }
        else if (args.length == 2)
        {
            // The first argument should be the host name or IP address, not bothering to check validity of this argument.
            try
            {
                Integer.parseInt(args[1]);
            }
            catch (NumberFormatException e)
            {
                throw new IllegalArgumentException("An invalid port was specified (must be an integer).");
            }
        }
        else
        {
            throw new IllegalArgumentException("The wrong number of arguments were specified (the host must be specified and optionally the port).");
        }
    }

    /**
     * <p>
     * Provides a basic command line utility in which the user can enter commands manually.
     * </p>
     * 
     * @param args The arguments provided to the command line utility when it was started.
     */
    public static void main(final String[] args)
    {
        fLogger = Logger.getLogger(PCRemoteClient.class);

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
            String serverHost = args[0];
            int serverPort = PCRemoteServer.DEFAULT_PORT;
            if (args.length == 2)
            {
                serverPort = Integer.parseInt(args[1]);
            }

            fLogger.info("#########################");
            fLogger.info("PC Remote 1.0 Client");
            fLogger.info("simple entertainment");
            fLogger.info("Gary Buyn");
            fLogger.info("#########################");
            fLogger.info("Connecting to server at " + serverHost + ":" + serverPort + "...");

            PCRemoteClient client = new PCRemoteClient(serverHost, serverPort);
            client.init();

            fLogger.info("...Done.");
            fLogger.info("Type '" + QUIT_COMMAND + "' in the terminal to disconnect from the server.");

            processCommands(client, new BufferedReader(new InputStreamReader(System.in)));

            fLogger.info("Disconnecting from server...");

            client.dispose();

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
        fLogger.info("\tPCRemoteClient host [port]");
        fLogger.info("Where:");
        fLogger.info("\t host = The host name or IP address of the PC on which the PCRemoteServer is listening.");
        fLogger.info("\t port = The port on which the PCRemoteServer is listening (default is 10999).");
    }

    /**
     * <p>
     * Processes the commands entered by the user. All commands are sent to the server except for the 'quit' command which causes the program loop to
     * finish.
     * </p>
     * 
     * @param client The client to process commands for.
     * @param commandReader Reads the commands to be processed.
     * 
     * @throws IOException Thrown if a command fails to be read from the terminal.
     */
    public static void processCommands(final PCRemoteClient client, final BufferedReader commandReader) throws IOException
    {
        // Wait for command to be entered.
        System.out.print("PCRemoteClient> ");
        String command = commandReader.readLine();

        // For every command entered until the 'quit' command.
        while (!command.equalsIgnoreCase(QUIT_COMMAND))
        {
            try
            {
                client.sendCommandViaTcp(command);
            }
            catch (Exception e)
            {
                fLogger.error("Failed to send the command.", e);
            }

            // Wait for command to be entered.
            System.out.print("PCRemoteClient> ");
            command = commandReader.readLine();
        }
    }

    /**
     * <p>
     * The host on which the {@link com.se.pcremote.server.PCRemoteServer PCRemoteServer} resides.
     * </p>
     */
    private String fServerHost;

    /**
     * <p>
     * The port on which the {@link com.se.pcremote.server.PCRemoteServer PCRemoteServer} is listening.
     * </p>
     */
    private int fServerPort;

    /**
     * <p>
     * The {@link com.se.pcremote.Client Client} used to communicate with the {@link com.se.pcremote.server.PCRemoteServer PCRemoteServer} using
     * the TCP protocol.
     * </p>
     */
    private Client fTcpClient;

    /**
     * <p>
     * The socket used to communicate with the {@link com.se.pcremote.server.PCRemoteServer PCRemoteServer} using the UDP protocol.
     * </p>
     */
    private DatagramSocket fUdpSocket;

    /**
     * <p>
     * Creates an instance of <code>PCRemoteClient</code> with the default server port (10999).
     * </p>
     * 
     * @param serverHost The host on which the {@link com.se.pcremote.server.PCRemoteServer PCRemoteServer} resides.
     * 
     * @throws SocketException Thrown if the UDP socket fails to be opened.
     */
    public PCRemoteClient(final String serverHost) throws SocketException
    {
        fServerHost = serverHost;

        fLogger = Logger.getLogger(PCRemoteClient.class);
        fServerPort = PCRemoteServer.DEFAULT_PORT;
        fTcpClient = null;
        fUdpSocket = new DatagramSocket();
    }

    /**
     * <p>
     * Creates an instance of <code>PCRemoteClient</code>.
     * </p>
     * 
     * @param serverHost The host on which the {@link com.se.pcremote.server.PCRemoteServer PCRemoteServer} resides.
     * @param serverPort The port on which the {@link com.se.pcremote.server.PCRemoteServer PCRemoteServer} is listening.
     * 
     * @throws SocketException Thrown if the UDP socket fails to be opened.
     */
    public PCRemoteClient(final String serverHost, final int serverPort) throws SocketException
    {
        fServerHost = serverHost;
        fServerPort = serverPort;

        fLogger = Logger.getLogger(PCRemoteClient.class);
        fTcpClient = null;
        fUdpSocket = null;
    }

    /**
     * <p>
     * Closes the connection to the server.
     * </p>
     * 
     * @throws IOException Thrown if the connection to the server fails to be closed.
     */
    public void dispose() throws IOException
    {
        if (fTcpClient != null)
        {
            fTcpClient.dispose();
        }
        if (fUdpSocket != null)
        {
            fUdpSocket.disconnect();
        }
    }

    /**
     * <p>
     * Retrieves the host on which the {@link com.se.pcremote.server.PCRemoteServer PCRemoteServer} resides.
     * </p>
     * 
     * @return The host on which the <code>PCRemoteServer</code> resides.
     */
    public String getServerHost()
    {
        return (fServerHost);
    }

    /**
     * <p>
     * Retrieves the port on which the {@link com.se.pcremote.server.PCRemoteServer PCRemoteServer} is listening.
     * </p>
     * 
     * @return The port on which the <code>PCRemoteServer</code> is listening.
     */
    public int getServerPort()
    {
        return (fServerPort);
    }

    /**
     * <p>
     * Opens the connection to the server.
     * </p>
     * 
     * @throws IOException Thrown if the TCP socket fails to be opened.
     */
    public void init() throws IOException
    {
        fTcpClient = new TcpClient(new Socket(fServerHost, fServerPort));
        fUdpSocket = new DatagramSocket();
    }

    /**
     * <p>
     * Determines if the TCP socket is connected to a server.
     * </p>
     * 
     * @return True if the TCP socket is connected to a server, false otherwise.
     */
    public boolean isConnected()
    {
        return (fTcpClient != null && fTcpClient.isConnected());
    }

    /**
     * <p>
     * Sends a command to the server using the TCP protocol.
     * </p>
     * 
     * @param command The command to be sent to the server.
     * 
     * @throws IOException Thrown if the command fails to be sent.
     */
    public void sendCommandViaTcp(final String command) throws IOException
    {
        fLogger.debug("Sending command via TCP: " + command);

        fTcpClient.sendData(command.getBytes());
    }

    /**
     * <p>
     * Sends a command to the server using the UDP protocol.
     * </p>
     * 
     * @param command The command to be sent to the server.
     * 
     * @throws IOException Thrown if the command fails to be sent.
     */
    public void sendCommandViaUdp(final String command) throws IOException
    {
        fLogger.debug("Sending command via UDP: " + command);

        fUdpSocket.send(new DatagramPacket(command.getBytes(), command.getBytes().length, InetAddress.getByName(fServerHost), fServerPort - 1));
    }
}
