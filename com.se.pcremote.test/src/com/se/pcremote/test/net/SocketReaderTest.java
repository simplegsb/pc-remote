/*
    This file is part of PC Remote.

    PC Remote is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    PC Remote is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with PC Remote. If not, see <http://www.gnu.org/licenses/>.
 */
package com.se.pcremote.test.net;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.CharBuffer;

import org.junit.Test;

import com.se.pcremote.net.SocketReader;

/**
 * <p>
 * Unit tests for the class {@link com.se.pcremote.net.SocketReader SocketReader}.
 * </p>
 * 
 * @author Gary Buyn
 */
public class SocketReaderTest
{
    /**
     * An instance of the class being unit tested.
     */
    private SocketReader fTestObject;

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.net.SocketReader#close() close()}.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     */
    @Test
    public void close() throws IOException
    {
        // Create dependencies.
        Socket mockSocket = createMock(Socket.class);
        InputStream mockInputStream = createMock(InputStream.class);

        // Dictate correct behaviour.
        expect(mockSocket.getInputStream()).andReturn(mockInputStream);

        // Dictate expected results.
        mockSocket.close();
        replay(mockSocket);

        // Initialise test environment.
        fTestObject = new SocketReader(mockSocket);

        // Perform test.
        fTestObject.close();

        // Verify test results.
        verify(mockSocket);
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.net.SocketReader#read() read()}.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     */
    @Test
    public void readSingle() throws IOException
    {
        // Create dependencies.
        Socket mockSocket = createMock(Socket.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[] {'X'});

        // Dictate correct behaviour.
        expect(mockSocket.getInputStream()).andReturn(inputStream);
        replay(mockSocket);

        // Initialise test environment.
        fTestObject = new SocketReader(mockSocket);

        // Perform test.
        int result = fTestObject.read();

        // Verify test results.
        assertEquals('X', result);
    }

    /**
     * <p>
     * Unit test the methods {@link com.se.pcremote.net.SocketReader#read(char[]) read(char[])} and
     * {@link com.se.pcremote.net.SocketReader#read(char[], int, int) read(char[], int, int)}.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     */
    @Test
    public void readMultipleToArray() throws IOException
    {
        // Create dependencies.
        Socket mockSocket = createMock(Socket.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[] {'X', 'Y', 'Z', 'X', 'Y', 'Z'});

        // Dictate correct behaviour.
        expect(mockSocket.getInputStream()).andReturn(inputStream);
        replay(mockSocket);

        // Initialise test environment.
        fTestObject = new SocketReader(mockSocket);

        // Perform test 1.
        char[] chars = new char[] {'A', 'B', 'C'};
        int result = fTestObject.read(chars);

        // Verify test 1 results.
        assertEquals(3, result);
        assertArrayEquals(new char[] {'X', 'Y', 'Z'}, chars);

        // Perform test 2.
        chars = new char[] {'A', 'B', 'C'};
        result = fTestObject.read(chars, 1, 2);

        // Verify test 2 results.
        assertEquals(2, result);
        assertArrayEquals(new char[] {'A', 'X', 'Y'}, chars);
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.net.SocketReader#read(CharBuffer) read(CharBuffer)}.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     */
    @Test
    public void readMultipleToBuffer() throws IOException
    {
        // Create dependencies.
        Socket mockSocket = createMock(Socket.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[] {'X', 'Y', 'Z', 'X', 'Y', 'Z'});

        // Dictate correct behaviour.
        expect(mockSocket.getInputStream()).andReturn(inputStream);
        replay(mockSocket);

        // Initialise test environment.
        fTestObject = new SocketReader(mockSocket);

        // Perform test.
        CharBuffer chars = CharBuffer.allocate(3);
        chars.put("ABC");
        chars.flip();
        int result = fTestObject.read(chars);
        chars.flip();

        // Verify test results.
        assertEquals(3, result);
        assertEquals("XYZ", chars.toString());
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.net.SocketReader#read() read()} with the special condition that the {@link java.net.Socket Socket}
     * being read from is closed locally while the read operation is waiting for it.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     */
    @Test
    public void readSocketClosedLocally() throws IOException
    {
        // Create dependencies.
        ServerSocket server = new ServerSocket(10999);
        Socket client = new Socket("localhost", 10999);
        Socket clientConnection = server.accept();

        // Initialise test environment.
        fTestObject = new SocketReader(client);

        // Perform test - Verify test results.
        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    assertEquals(-1, fTestObject.read());
                }
                catch (IOException e)
                {
                    System.err.println("Test failed!");
                    e.printStackTrace();
                }
            }
        }.start();
        client.close();

        // Cleanup.
        clientConnection.close();
        server.close();
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.net.SocketReader#read() read()} with the special condition that the {@link java.net.Socket Socket}
     * being read from is closed remotely while the read operation is waiting for it.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     */
    @Test
    public void readSocketClosedRemotely() throws IOException
    {
        // Create dependencies.
        ServerSocket server = new ServerSocket(10999);
        Socket client = new Socket("localhost", 10999);
        Socket clientConnection = server.accept();

        // Initialise test environment.
        fTestObject = new SocketReader(client);

        // Perform test - Verify test results.
        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    assertEquals(-1, fTestObject.read());
                }
                catch (IOException e)
                {
                    System.err.println("Test failed!");
                    e.printStackTrace();
                }
            }
        }.start();
        clientConnection.close();

        // Cleanup.
        client.close();
        server.close();
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.net.SocketReader#read() read()} with the special condition that the read operation results in an
     * {@link java.net.SocketException SocketException}.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     */
    @Test(expected = SocketException.class)
    public void readIOError() throws IOException
    {
        // Initialise test environment.
        fTestObject = new SocketReader(new Socket());

        // Perform test.
        fTestObject.read();
    }
}
