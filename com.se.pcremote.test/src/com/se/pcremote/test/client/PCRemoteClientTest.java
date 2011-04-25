/*
    This file is part of PC Remote.

    PC Remote is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    PC Remote is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with PC Remote. If not, see <http://www.gnu.org/licenses/>.
 */
package com.se.pcremote.test.client;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.junit.Before;
import org.junit.Test;

import com.se.pcremote.client.PCRemoteClient;

/**
 * <p>
 * Unit tests for the class {@link com.se.pcremote.client.PCRemoteClient PCRemoteClient}.
 * </p>
 * 
 * @author Gary Buyn
 */
public class PCRemoteClientTest
{
    /**
     * An instance of the class being unit tested.
     */
    private PCRemoteClient fTestObject;

    /**
     * <p>
     * Setup to perform before each unit test.
     * </p>
     * 
     * @throws SocketException Thrown if the UDP socket fails to be opened.
     */
    @Before
    public void before() throws SocketException
    {
        fTestObject = new PCRemoteClient("localhost");
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.client.PCRemoteClient#checkArgs(String[]) checkArgs(String[])} with the special condition that not
     * enough arguments are supplied.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void checkArgsNotEnough()
    {
        PCRemoteClient.checkArgs(new String[0]);
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.client.PCRemoteClient#checkArgs(String[]) checkArgs(String[])} with the special condition that the
     * port given is not an integer.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void checkArgsPortNotInt()
    {
        PCRemoteClient.checkArgs(new String[] {"ip address", "port"});
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.client.PCRemoteClient#checkArgs(String[]) checkArgs(String[])} with the special condition that too
     * many arguments are supplied.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void checkArgsTooMany()
    {
        PCRemoteClient.checkArgs(new String[3]);
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.client.PCRemoteClient#dispose() dispose()} with the special condition that the method
     * {@link com.se.pcremote.client.PCRemoteClient#init() init()} has not yet been called.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     */
    @Test
    public void disposeNoInit() throws IOException
    {
        // Perform test - Verify test results.
        fTestObject.dispose(); // Verify that no errors occur.
    }

    /**
     * <p>
     * Unit test the methods {@link com.se.pcremote.client.PCRemoteClient#init() init()} and {@link com.se.pcremote.client.PCRemoteClient#dispose()
     * dispose()}.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     */
    @Test
    public void initDispose() throws IOException
    {
        // Initialise test environment.
        ServerSocket server = new ServerSocket(10999);

        // Perform test 1.
        fTestObject.init();
        Socket client = server.accept();

        // Verify test 1 results.
        assertTrue(fTestObject.isConnected());

        // Perform test 2.
        fTestObject.dispose();

        // Verify test 2 results.
        assertFalse(fTestObject.isConnected());

        // Cleanup
        client.close();
        server.close();
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.client.PCRemoteClient#processCommands(PCRemoteClient) processCommands(PCRemoteClient)}.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     */
    @Test
    public void processCommands() throws IOException
    {
        // Create dependencies.
        PCRemoteClient mockClient = createMock(PCRemoteClient.class);
        BufferedReader mockReader = createMock(BufferedReader.class);

        // Dictate correct behaviour.
        expect(mockReader.readLine()).andReturn("mouseMove(1,1)");
        expect(mockReader.readLine()).andReturn("quit");
        replay(mockReader);

        // Dictate expected results.
        mockClient.sendCommandViaTcp("mouseMove(1,1)");
        replay(mockClient);

        // Perform test
        PCRemoteClient.processCommands(mockClient, mockReader);

        // Verify test results.
        verify(mockClient);
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.client.PCRemoteClient#sendCommandViaTcp() sendCommandViaTcp()}.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     */
    @Test
    public void sendCommandViaTcp() throws IOException
    {
        // Create dependencies.
        byte[] serverData = new byte[4];
        ServerSocket server = new ServerSocket(10999);

        // Initialise test environment.
        fTestObject.init();
        Socket serverConnection = server.accept();

        // Perform test.
        fTestObject.sendCommandViaTcp("test");
        serverConnection.getInputStream().read(serverData);

        // Verify test results.
        assertEquals("test", new String(serverData));

        // Cleanup
        fTestObject.dispose();
        serverConnection.close();
        server.close();
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.client.PCRemoteClient#sendCommandViaTcp() sendCommandViaTcp()}.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     */
    @Test(expected = SocketException.class)
    public void sendCommandViaTcpSocketClosed() throws IOException
    {
        // Create dependencies.
        ServerSocket server = new ServerSocket(10999);

        // Initialise test environment.
        fTestObject.init();
        Socket client = server.accept();
        fTestObject.dispose();
        client.close();
        server.close();

        // Perform test.
        fTestObject.sendCommandViaTcp("test");
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.client.PCRemoteClient#sendCommandViaUdp() sendCommandViaUdp()}.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     */
    @Test
    public void sendCommandViaUdp() throws IOException
    {
        // Create dependencies.
        byte[] serverData = new byte[4];
        DatagramSocket server = new DatagramSocket(10998);
        DatagramPacket serverPacket = new DatagramPacket(serverData, serverData.length);

        // Perform test.
        fTestObject.sendCommandViaUdp("test");
        server.receive(serverPacket);

        // Verify test results.
        assertEquals("test", new String(serverPacket.getData()));

        // Cleanup
        server.close();
    }
}
