/*
    This file is part of PC Remote.

    PC Remote is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    PC Remote is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with PC Remote. If not, see <http://www.gnu.org/licenses/>.
 */
package com.se.pcremote.server;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.junit.Test;

import com.se.pcremote.server.CommandExecuter;
import com.se.pcremote.server.TcpClient;

/**
 * <p>
 * Unit tests for the class {@link com.se.pcremote.server.TcpClient TcpClient}.
 * </p>
 * 
 * @author Gary Buyn
 */
public class TcpClientTest
{
    /**
     * An instance of the class being unit tested.
     */
    private TcpClient fTestObject;

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.TcpClient#receiveData() receiveData()}.
     * </p>
     * 
     * @throws IOException Thrown is an I/O error occurs.
     */
    @Test
    public void receiveData() throws IOException
    {
        // Create dependencies.
        Socket mockSocket = createMock(Socket.class);
        byte[] receiveBytes = "keyPress(0)".getBytes();
        InputStream inputStream = new ByteArrayInputStream(receiveBytes);
        CommandExecuter mockCommandExecuter = createMock(CommandExecuter.class);

        // Initialise test environment.
        fTestObject = new TcpClient(mockSocket, mockCommandExecuter);

        // Dictate correct behaviour.
        expect(mockSocket.getInputStream()).andReturn(inputStream).anyTimes();
        replay(mockSocket);

        // Dictate expected results.
        mockCommandExecuter.executeCommand("keyPress(0)");
        replay(mockCommandExecuter);

        // Perform test.
        fTestObject.receiveData();

        // Verify test results.
        verify(mockCommandExecuter);
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.TcpClient#receiveData() receiveData()} with the special condition that multiple commands are
     * being received at once.
     * </p>
     * 
     * @throws IOException Thrown is an I/O error occurs.
     */
    @Test
    public void receiveDataMultipleCommands() throws IOException
    {
        // Create dependencies.
        Socket mockSocket = createMock(Socket.class);
        byte[] receiveBytes = "keyPress(0);keyRelease(0);".getBytes();
        InputStream inputStream = new ByteArrayInputStream(receiveBytes);
        CommandExecuter mockCommandExecuter = createMock(CommandExecuter.class);

        // Initialise test environment.
        fTestObject = new TcpClient(mockSocket, mockCommandExecuter);

        // Dictate correct behaviour.
        expect(mockSocket.getInputStream()).andReturn(inputStream).anyTimes();
        replay(mockSocket);

        // Dictate expected results.
        mockCommandExecuter.executeCommand("keyPress(0)");
        mockCommandExecuter.executeCommand("keyRelease(0)");
        replay(mockCommandExecuter);

        // Perform test.
        fTestObject.receiveData();

        // Verify test results.
        verify(mockCommandExecuter);
    }
}
