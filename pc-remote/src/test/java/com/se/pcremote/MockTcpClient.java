/*
    This file is part of PC Remote.

    PC Remote is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    PC Remote is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with PC Remote. If not, see <http://www.gnu.org/licenses/>.
 */
package com.se.pcremote;

import java.net.Socket;

import com.se.pcremote.TcpClient;

/**
 * <p>
 * A mock implementation of a {@link com.se.devenvy.net.TcpClient TcpClient}.
 * </p>
 * 
 * @author Gary Buyn
 */
public class MockTcpClient extends TcpClient
{
    /**
     * <p>
     * The data received during the last call to {@link #receiveData()}.
     * </p>
     */
    private byte[] fReceivedData;

    /**
     * <p>
     * Creates an instance of <code>MockTcpClient</code>.
     * </p>
     * 
     * @param socket The socket over which the TCP connection is made.
     */
    public MockTcpClient(final Socket socket)
    {
        super(socket);

        fReceivedData = null;
    }

    /**
     * <p>
     * Retrieves the data received during the last call to {@link #receiveData()}.
     * </p>
     * 
     * @return The data received during the last call to <code>receiveData()</code>.
     */
    public byte[] getReceivedData()
    {
        return (fReceivedData);
    }

    @Override
    protected void onReceiveData(final byte[] data, final int dataLength)
    {
        fReceivedData = data;
    }
}
