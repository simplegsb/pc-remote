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
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.junit.Test;

import com.se.pcremote.server.CommandExecuter;
import com.se.pcremote.server.TcpClientManager;

/**
 * <p>
 * Unit tests for the class {@link com.se.pcremote.server.TcpClientManager TcpClientManager}.
 * </p>
 * 
 * @author Gary Buyn
 */
public class TcpClientManagerTest
{
    /**
     * An instance of the class being unit tested.
     */
    private TcpClientManager fTestObject;

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.TcpClientManager#dispose() dispose()}.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     */
    @Test
    public void dispose() throws IOException
    {
        // Create dependencies.
        Socket mockSocket = createMock(Socket.class);
        InputStream mockInputStream = createMock(InputStream.class);

        // Dictate correct behaviour.
        expect(mockSocket.getInputStream()).andReturn(mockInputStream);

        // Dictate correct results.
        mockSocket.close();
        replay(mockSocket);

        // Initialise test environment.
        fTestObject = new TcpClientManager(mockSocket, null);

        // Perform test.
        fTestObject.dispose();

        // Verify test results.
        verify(mockSocket);
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
        Socket mockSocket = createMock(Socket.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[] {'X', 'Y', 'Z', ';'});
        CommandExecuter mockCommandExecuter = createMock(CommandExecuter.class);

        // Dictate correct behaviour.
        expect(mockSocket.getInputStream()).andReturn(inputStream);
        expect(mockSocket.isClosed()).andReturn(false);
        mockSocket.close();
        expect(mockSocket.isClosed()).andReturn(true);
        replay(mockSocket);

        // Dictate expected results.
        mockCommandExecuter.executeCommand("XYZ");
        replay(mockCommandExecuter);

        // Initialise test environment.
        fTestObject = new TcpClientManager(mockSocket, mockCommandExecuter);

        // Perform test.
        fTestObject.run();

        // Verify test results.
        verify(mockCommandExecuter);
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.TcpAcceptanceManager#run() run()} with the special condition that there are multiple
     * commands separated by the ';' character.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     */
    @Test
    public void runMultipleCommands() throws IOException
    {
        // Create dependencies.
        Socket mockSocket = createMock(Socket.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[] {'X', 'Y', 'Z', ';', 'A', 'B', 'C', ';'});
        CommandExecuter mockCommandExecuter = createMock(CommandExecuter.class);

        // Dictate correct behaviour.
        expect(mockSocket.getInputStream()).andReturn(inputStream);
        expect(mockSocket.isClosed()).andReturn(false);
        mockSocket.close();
        expect(mockSocket.isClosed()).andReturn(true);
        replay(mockSocket);

        // Dictate expected results.
        mockCommandExecuter.executeCommand("XYZ");
        mockCommandExecuter.executeCommand("ABC");
        replay(mockCommandExecuter);

        // Initialise test environment.
        fTestObject = new TcpClientManager(mockSocket, mockCommandExecuter);

        // Perform test.
        fTestObject.run();

        // Verify test results.
        verify(mockCommandExecuter);
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.TcpAcceptanceManager#run() run()} with the special condition that an error occurred during
     * an attempt to execute a command.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     */
    @Test
    public void runExecuteFailure() throws IOException
    {
        // Create dependencies.
        Socket mockSocket = createMock(Socket.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[] {'X', 'Y', 'Z', ';', '\n', 'A', 'B', 'C', ';'});
        CommandExecuter mockCommandExecuter = createMock(CommandExecuter.class);

        // Dictate correct behaviour.
        expect(mockSocket.getInputStream()).andReturn(inputStream);
        expect(mockSocket.isClosed()).andReturn(false);
        mockCommandExecuter.executeCommand("XYZ");
        expectLastCall().andThrow(new RuntimeException());
        expect(mockSocket.isClosed()).andReturn(false);
        mockSocket.close();
        expect(mockSocket.isClosed()).andReturn(true);
        replay(mockSocket);

        // Dictate expected results.
        mockCommandExecuter.executeCommand("ABC");
        replay(mockCommandExecuter);

        // Initialise test environment.
        fTestObject = new TcpClientManager(mockSocket, mockCommandExecuter);

        // Perform test.
        fTestObject.run();

        // Verify test results.
        verify(mockCommandExecuter);
    }
}
