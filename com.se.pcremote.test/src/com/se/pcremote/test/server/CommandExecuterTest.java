/*
    This file is part of PC Remote.

    PC Remote is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    PC Remote is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with PC Remote. If not, see <http://www.gnu.org/licenses/>.
 */
package com.se.pcremote.test.server;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.io.IOException;

import org.junit.Test;

import com.se.pcremote.server.CommandExecuter;

/**
 * <p>
 * Unit tests for the class {@link com.se.pcremote.server.CommandExecuter CommandExecuter}.
 * </p>
 * 
 * @author Gary Buyn
 */
public class CommandExecuterTest
{
    /**
     * An instance of the class being unit tested.
     */
    private CommandExecuter fTestObject;

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.CommandExecuter#executeCommand(String) executeCommand(String)}.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     */
    @Test
    public void executeCommand() throws IOException
    {
        // Create dependencies.
        Robot mockRobot = createMock(Robot.class);

        // Dictate expected results.
        mockRobot.keyPress(0);
        mockRobot.keyRelease(0);
        mockRobot.mouseMove(0, 0);
        mockRobot.mouseMove(0, 0);
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        mockRobot.mouseMove(mouseLocation.x, mouseLocation.y);
        mockRobot.mouseMove(mouseLocation.x, mouseLocation.y);
        mockRobot.mousePress(0);
        mockRobot.mouseRelease(0);
        mockRobot.mouseWheel(0);
        replay(mockRobot);

        // Initialise test environment.
        fTestObject = new CommandExecuter(mockRobot);

        // Perform test.
        fTestObject.executeCommand("keyPress(0)");
        fTestObject.executeCommand("keyRelease(0)");
        fTestObject.executeCommand("mouseMove(0,0)");
        fTestObject.executeCommand("mouseMove(0.0,0.0)");
        fTestObject.executeCommand("mouseMoveRelative(0,0)");
        fTestObject.executeCommand("mouseMoveRelative(0.0,0.0)");
        fTestObject.executeCommand("mousePress(0)");
        fTestObject.executeCommand("mouseRelease(0)");
        fTestObject.executeCommand("mouseWheel(0)");

        // Verify test results.
        verify(mockRobot);
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.CommandExecuter#executeCommand(String) executeCommand(String)} with the special condition
     * that the command given has no arguments.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     * @throws AWTException Thrown if the {@link com.se.pcremote.server.CommandExecuter CommandExecuter} fails to be instantiated.
     */
    @Test(expected = IllegalArgumentException.class)
    public void executeCommandInvalidFormat() throws IOException, AWTException
    {
        // Initialise test environment.
        fTestObject = new CommandExecuter();

        // Perform test.
        fTestObject.executeCommand("noBrackets");
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.CommandExecuter#executeCommand(String) executeCommand(String)} with the special condition
     * that the command given has no arguments.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     * @throws AWTException Thrown if the {@link com.se.pcremote.server.CommandExecuter CommandExecuter} fails to be instantiated.
     */
    @Test(expected = IllegalArgumentException.class)
    public void executeCommandInvalidNumber() throws IOException, AWTException
    {
        // Initialise test environment.
        fTestObject = new CommandExecuter();

        // Perform test.
        fTestObject.executeCommand("mouseMove(zero,zero)");
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.CommandExecuter#executeCommand(String) executeCommand(String)} with the special condition
     * that the command given has no arguments.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     * @throws AWTException Thrown if the {@link com.se.pcremote.server.CommandExecuter CommandExecuter} fails to be instantiated.
     */
    @Test(expected = IllegalArgumentException.class)
    public void executeCommandNoArgs() throws IOException, AWTException
    {
        // Initialise test environment.
        fTestObject = new CommandExecuter();

        // Perform test.
        fTestObject.executeCommand("mouseMove()");
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.CommandExecuter#executeCommand(String) executeCommand(String)} with the special condition
     * that the command given is unknown.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs.
     * @throws AWTException Thrown if the {@link com.se.pcremote.server.CommandExecuter CommandExecuter} fails to be instantiated.
     */
    @Test(expected = IllegalArgumentException.class)
    public void executeCommandUnknown() throws IOException, AWTException
    {
        // Initialise test environment.
        fTestObject = new CommandExecuter();

        // Perform test.
        fTestObject.executeCommand("unknownCommand(0)");
    }
}
