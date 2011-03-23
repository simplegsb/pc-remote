/*
    This file is part of PC Remote.

    PC Remote is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    PC Remote is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with PC Remote. If not, see <http://www.gnu.org/licenses/>.
 */
package com.se.pcremote.test.server;

import static org.junit.Assert.assertNotNull;

import java.awt.AWTException;

import org.junit.Before;
import org.junit.Test;

import com.se.pcremote.server.PCRemoteServer;

/**
 * <p>
 * Unit tests for the class {@link com.se.pcremote.server.PCRemoteServer PCRemoteServer}.
 * </p>
 * 
 * @author Gary Buyn
 */
public class PCRemoteServerTest
{
    /**
     * An instance of the class being unit tested.
     */
    private PCRemoteServer fTestObject;

    /**
     * <p>
     * Setup to perform before each unit test.
     * </p>
     * 
     * @throws AWTException Thrown if the {@link com.se.pcremote.server.PCRemoteServer PCRemoteServer} fails to be instantiated.
     */
    @Before
    public void before() throws AWTException
    {
        fTestObject = new PCRemoteServer();
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.PCRemoteServer#start() start()}.
     * </p>
     */
    @Test
    public void start()
    {
        // Perform test.
        fTestObject.start();

        // Verify test results.
        assertNotNull(fTestObject.getTcpAcceptanceManager());
    }
}
