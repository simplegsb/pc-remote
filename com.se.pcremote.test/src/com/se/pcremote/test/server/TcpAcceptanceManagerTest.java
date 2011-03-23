/*
    This file is part of PC Remote.

    PC Remote is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    PC Remote is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with PC Remote. If not, see <http://www.gnu.org/licenses/>.
 */
package com.se.pcremote.test.server;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.makeThreadSafe;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Test;

import com.se.pcremote.server.TcpAcceptanceManager;
import com.se.pcremote.server.TcpClientManager;

/**
 * <p>
 * Unit tests for the class {@link com.se.pcremote.server.TcpAcceptanceManager TcpAcceptanceManager}.
 * </p>
 * 
 * @author Gary Buyn
 */
public class TcpAcceptanceManagerTest
{
    /**
     * An instance of the class being unit tested.
     */
    private TcpAcceptanceManager fTestObject;

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.TcpAcceptanceManager#dispose() dispose()}.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     */
    @Test
    public void dispose() throws IOException
    {
        // Create dependencies.
        ServerSocket mockServerSocket = createMock(ServerSocket.class);
        TcpClientManager mockClientManager = createMock(TcpClientManager.class);

        // Dictate correct results.
        mockServerSocket.close();
        mockClientManager.dispose();
        replay(mockServerSocket, mockClientManager);

        // Initialise test environment.
        fTestObject = new TcpAcceptanceManager(mockServerSocket, null);
        fTestObject.getTcpClientManagers().add(mockClientManager);

        // Perform test.
        fTestObject.dispose();

        // Verify test results.
        verify(mockServerSocket, mockClientManager);
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.TcpAcceptanceManager#run() run()}.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     */
    @Test
    public void run() throws IOException
    {
        // Create dependencies.
        ServerSocket mockServerSocket = createMock(ServerSocket.class);
        Socket mockSocket = createMock(Socket.class);
        InputStream mockInputStream = createMock(InputStream.class);

        // Dictate correct behaviour.
        expect(mockServerSocket.isClosed()).andReturn(false);
        expect(mockServerSocket.accept()).andReturn(mockSocket);
        expect(mockServerSocket.isClosed()).andReturn(true);
        replay(mockServerSocket);

        // Dictate expected results.
        expect(mockSocket.getInputStream()).andReturn(mockInputStream);
        expect(mockSocket.isClosed()).andReturn(true);
        makeThreadSafe(mockSocket, true);
        replay(mockSocket);

        // Initialise test environment.
        fTestObject = new TcpAcceptanceManager(mockServerSocket, null);

        // Perform test.
        fTestObject.run();

        // Verify test results.
        assertEquals(1, fTestObject.getTcpClientManagers().size());
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.TcpAcceptanceManager#run() run()} with the special condition that an error occurred during
     * an attempt to accept a new connection.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     */
    @Test
    public void runAcceptFailure() throws IOException
    {
        // Create dependencies.
        ServerSocket mockServerSocket = createMock(ServerSocket.class);
        Socket mockSocket = createMock(Socket.class);
        InputStream mockInputStream = createMock(InputStream.class);

        // Dictate correct behaviour.
        expect(mockServerSocket.isClosed()).andReturn(false);
        expect(mockServerSocket.accept()).andThrow(new IOException());
        expect(mockServerSocket.isClosed()).andReturn(false);
        expect(mockServerSocket.accept()).andReturn(mockSocket);
        expect(mockServerSocket.isClosed()).andReturn(true);
        replay(mockServerSocket);

        // Dictate expected results.
        expect(mockSocket.getInputStream()).andReturn(mockInputStream);
        expect(mockSocket.isClosed()).andReturn(true);
        makeThreadSafe(mockSocket, true);
        replay(mockSocket);

        // Initialise test environment.
        fTestObject = new TcpAcceptanceManager(mockServerSocket, null);

        // Perform test.
        fTestObject.run();

        // Verify test results.
        assertEquals(1, fTestObject.getTcpClientManagers().size());
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.TcpAcceptanceManager#run() run()} with the special condition that the
     * {@link java.net.ServeSocket ServerSocket} closed during an attempt to accept a new connection.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     */
    @Test
    public void runServerSocketClosedDuringAccept() throws IOException
    {
        // Create dependencies.
        ServerSocket mockServerSocket = createMock(ServerSocket.class);

        // Dictate correct behaviour.
        expect(mockServerSocket.isClosed()).andReturn(false);
        expect(mockServerSocket.accept()).andReturn(null);
        expect(mockServerSocket.isClosed()).andReturn(true);
        replay(mockServerSocket);

        // Initialise test environment.
        fTestObject = new TcpAcceptanceManager(mockServerSocket, null);

        // Perform test.
        fTestObject.run();

        // Verify test results.
        assertEquals(0, fTestObject.getTcpClientManagers().size());
    }
}
