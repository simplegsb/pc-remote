/*
    This file is part of PC Remote.

    PC Remote is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    PC Remote is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with PC Remote. If not, see <http://www.gnu.org/licenses/>.
 */
package com.se.pcremote.net;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.net.SocketException;
import java.nio.CharBuffer;

/**
 * <p>
 * Reads from the {@link java.io.InputStream InputStream} of a {@link java.net.Socket Socket} and handles closure of the <code>Socket</code> both
 * locally and remotely as if it is the end of the stream.
 * </p>
 * 
 * @author Gary Buyn
 */
public class SocketReader extends Reader
{
    /**
     * <p>
     * The message of a {@link java.net.SocketException SocketException} that signifies a {@link java.net.Socket Socket} has been closed.
     * </p>
     */
    private static final String SOCKET_CLOSED_MESSAGE = "Socket closed";

    /**
     * <p>
     * The internal reader responsible for performing the actual reads.
     * </p>
     */
    private InputStreamReader fReader;

    /**
     * <p>
     * The {@link java.net.Socket Socket} to read.
     * </p>
     */
    private Socket fSocket;

    /**
     * <p>
     * Creates an instance of <code>SocketReader</code>.
     * </p>
     * 
     * @param socket The {@link java.net.Socket Socket} to read.
     * 
     * @throws IOException Thrown if the {@link java.io.InputStream InputStream} of the <code>Socket</code> cannot be retrieved.
     */
    public SocketReader(final Socket socket) throws IOException
    {
        fSocket = socket;

        fReader = new InputStreamReader(fSocket.getInputStream());
    }

    @Override
    public void close() throws IOException
    {
        fReader.close();
        fSocket.close();
    }

    @Override
    public void mark(final int readAheadLimit) throws IOException
    {
        fReader.mark(readAheadLimit);
    }

    @Override
    public boolean markSupported()
    {
        return (fReader.markSupported());
    }

    @Override
    public int read() throws IOException
    {
        return (read(null, 0, 0, null));
    }

    @Override
    public int read(final char[] cbuf) throws IOException
    {
        return (read(cbuf, 0, cbuf.length, null));
    }

    @Override
    public int read(final char[] cbuf, final int offset, final int length) throws IOException
    {
        return (read(cbuf, offset, length, null));
    }

    @Override
    public int read(final CharBuffer target) throws IOException
    {
        return (read(null, 0, 0, target));
    }

    /**
     * <p>
     * Performs the appropriate read operation on the internal reader depending on which arguments have been supplied. Handles handles closure of the
     * {@link java.net.Socket Socket} both locally and remotely as if it is the end of the stream (returns -1).
     * </p>
     * 
     * @param cbuf The buffer in which to store the characters that have been read (optional).
     * @param offset The offset in the buffer at which characters will start being stored (only applicable if a value for <code>cbuf</code> is
     * supplied).
     * @param length The number of characters to read and store in the buffer (only applicable if a value for <code>cbuf</code> is supplied).
     * @param target The buffer in which to store the characters that have been read (optional).
     * 
     * @throws IOException Thrown if the read operation fails.
     * 
     * @return The number of characters that have been read (if a value for <code>cbuf</code> or <code>target</code> was supplied), the character read
     * (if values for <code>cbuf</code> and <code>target</code> were not supplied) or -1 if the end of the stream has been reached (this includes the
     * closure of the socket either locally or remotely).
     */
    private int read(final char[] cbuf, final int offset, final int length, final CharBuffer target) throws IOException
    {
        try
        {
            // Perform the appropriate read operation.
            int lengthRead = -1;
            if (cbuf != null)
            {
                lengthRead = fReader.read(cbuf, offset, length);
            }
            else if (target != null)
            {
                lengthRead = fReader.read(target);
            }
            else
            {
                lengthRead = fReader.read();
            }

            // If the socket has been closed remotely.
            if (lengthRead == -1)
            {
                fSocket.close();
            }

            return (lengthRead);
        }
        catch (SocketException e)
        {
            // If the socket has been closed locally.
            if (e.getMessage().equals(SOCKET_CLOSED_MESSAGE))
            {
                return (-1);
            }
            else
            {
                throw e;
            }
        }
    }

    @Override
    public boolean ready() throws IOException
    {
        return (fReader.ready() && fSocket.isConnected() && !fSocket.isInputShutdown());
    }

    @Override
    public void reset() throws IOException
    {
        fReader.reset();
    }

    @Override
    public long skip(final long n) throws IOException
    {
        return (fReader.skip(n));
    }
}
