/*
    This file is part of PC Remote.

    PC Remote is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    PC Remote is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with PC Remote. If not, see <http://www.gnu.org/licenses/>.
 */
package com.se.pcremote.test.net;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Test;

import com.se.pcremote.net.ConnectionAccepter;

/**
 * <p>
 * Unit tests for the class {@link com.se.pcremote.net.ConnectionAccepter ConnectionAccepter}.
 * </p>
 * 
 * @author Gary Buyn
 */
public class ConnectionAccepterTest
{
    /**
     * An instance of the class being unit tested.
     */
    private ConnectionAccepter fTestObject;

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.net.ConnectionAccepter#acceptConnection() acceptConnection()}.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     */
    @Test
    public void acceptConnection() throws IOException
    {
        // Create dependencies.
        ServerSocket server = new ServerSocket(10999);
        Socket client = new Socket("localhost", 10999);

        // Initialise test environment.
        fTestObject = new ConnectionAccepter(server);

        // Perform test.
        Socket clientConnection = fTestObject.acceptConnection();

        // Verify test results.
        assertNotNull(clientConnection);
        assertTrue(clientConnection.isConnected());
        assertTrue(client.isConnected());

        // Cleanup.
        client.close();
        clientConnection.close();
        server.close();
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.net.ConnectionAccepter#acceptConnection() acceptConnection()}.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     */
    @Test
    public void acceptConnectionServerSocketClosed() throws IOException
    {
        // Create dependencies.
        ServerSocket server = new ServerSocket(10999);

        // Initialise test environment.
        fTestObject = new ConnectionAccepter(server);

        // Perform test - Verify test results.
        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    assertNull(fTestObject.acceptConnection());
                }
                catch (IOException e)
                {
                    System.err.println("Test failed!");
                    e.printStackTrace();
                }
            }
        }.start();
        server.close();
    }
}
