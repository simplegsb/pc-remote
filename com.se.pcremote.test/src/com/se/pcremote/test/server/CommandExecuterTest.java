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
import java.awt.event.InputEvent;

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
     * Unit test the method {@link com.se.pcremote.server.CommandExecuter#executeCommand() executeCommand()} with the special condition that a numeric
     * argument has a non-numeric value.
     * </p>
     * 
     * @throws AWTException Thrown if the {@link java.awt.Robot Robot} is not supported by the platform configuration.
     */
    @Test(expected = IllegalArgumentException.class)
    public void executeCommandInvliadNumericArgument() throws AWTException
    {
        // Initialise test environment.
        fTestObject = new CommandExecuter();

        // Perform test.
        fTestObject.executeCommand("keyPress(alphanumeric)");
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.CommandExecuter#executeCommand() executeCommand()} with the special condition that the
     * 'keyPress' command is to be executed.
     * </p>
     * 
     * @throws AWTException Thrown if the {@link java.awt.Robot Robot} is not supported by the platform configuration.
     */
    @Test
    public void executeCommandKeyPress() throws AWTException
    {
        // Create dependencies.
        Robot mockRobot = createMock(Robot.class);

        // Initialise test environment.
        fTestObject = new CommandExecuter(mockRobot);

        // Dictate correct results.
        mockRobot.keyPress(0);
        replay(mockRobot);

        // Perform test.
        fTestObject.executeCommand("keyPress(0)");

        // Verify test results.
        verify(mockRobot);
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.CommandExecuter#executeCommand() executeCommand()} with the special condition that the
     * 'keyRelease' command is to be executed.
     * </p>
     * 
     * @throws AWTException Thrown if the {@link java.awt.Robot Robot} is not supported by the platform configuration.
     */
    @Test
    public void executeCommandKeyRelease() throws AWTException
    {
        // Create dependencies.
        Robot mockRobot = createMock(Robot.class);

        // Initialise test environment.
        fTestObject = new CommandExecuter(mockRobot);

        // Dictate correct results.
        mockRobot.keyRelease(0);
        replay(mockRobot);

        // Perform test.
        fTestObject.executeCommand("keyRelease(0)");

        // Verify test results.
        verify(mockRobot);
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.CommandExecuter#executeCommand() executeCommand()} with the special condition that the
     * 'mouseMove' command is to be executed.
     * </p>
     * 
     * @throws AWTException Thrown if the {@link java.awt.Robot Robot} is not supported by the platform configuration.
     */
    @Test
    public void executeCommandMouseMove() throws AWTException
    {
        // Create dependencies.
        Robot mockRobot = createMock(Robot.class);

        // Initialise test environment.
        fTestObject = new CommandExecuter(mockRobot);

        // Dictate correct results.
        mockRobot.mouseMove(0, 0);
        replay(mockRobot);

        // Perform test.
        fTestObject.executeCommand("mouseMove(0,0)");

        // Verify test results.
        verify(mockRobot);
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.CommandExecuter#executeCommand() executeCommand()} with the special condition that the
     * 'mouseMoveRelative' command is to be executed.
     * </p>
     * 
     * @throws AWTException Thrown if the {@link java.awt.Robot Robot} is not supported by the platform configuration.
     */
    @Test
    public void executeCommandMouseMoveRelative() throws AWTException
    {
        // Create dependencies.
        Robot mockRobot = createMock(Robot.class);

        // Initialise test environment.
        fTestObject = new CommandExecuter(mockRobot);

        // Dictate correct results.
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        mockRobot.mouseMove(mouseLocation.x + -1 * 10, mouseLocation.y + -1 * 10);
        replay(mockRobot);

        // Perform test.
        fTestObject.executeCommand("mouseMoveRelative(10,10)");

        // Verify test results.
        verify(mockRobot);
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.CommandExecuter#executeCommand() executeCommand()} with the special condition that the
     * 'mousePress' command is to be executed.
     * </p>
     * 
     * @throws AWTException Thrown if the {@link java.awt.Robot Robot} is not supported by the platform configuration.
     */
    @Test
    public void executeCommandMousePress() throws AWTException
    {
        // Create dependencies.
        Robot mockRobot = createMock(Robot.class);

        // Initialise test environment.
        fTestObject = new CommandExecuter(mockRobot);

        // Dictate correct results.
        mockRobot.mousePress(InputEvent.BUTTON1_MASK);
        mockRobot.mousePress(InputEvent.BUTTON2_MASK);
        mockRobot.mousePress(InputEvent.BUTTON3_MASK);
        replay(mockRobot);

        // Perform test.
        fTestObject.executeCommand("mousePress(1)");
        fTestObject.executeCommand("mousePress(2)");
        fTestObject.executeCommand("mousePress(3)");

        // Verify test results.
        verify(mockRobot);
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.CommandExecuter#executeCommand() executeCommand()} with the special condition that the
     * 'mouseRelease' command is to be executed.
     * </p>
     * 
     * @throws AWTException Thrown if the {@link java.awt.Robot Robot} is not supported by the platform configuration.
     */
    @Test
    public void executeCommandMouseRelease() throws AWTException
    {
        // Create dependencies.
        Robot mockRobot = createMock(Robot.class);

        // Initialise test environment.
        fTestObject = new CommandExecuter(mockRobot);

        // Dictate correct results.
        mockRobot.mouseRelease(InputEvent.BUTTON1_MASK);
        mockRobot.mouseRelease(InputEvent.BUTTON2_MASK);
        mockRobot.mouseRelease(InputEvent.BUTTON3_MASK);
        replay(mockRobot);

        // Perform test.
        fTestObject.executeCommand("mouseRelease(1)");
        fTestObject.executeCommand("mouseRelease(2)");
        fTestObject.executeCommand("mouseRelease(3)");

        // Verify test results.
        verify(mockRobot);
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.CommandExecuter#executeCommand() executeCommand()} with the special condition that the
     * 'mouseWheel' command is to be executed.
     * </p>
     * 
     * @throws AWTException Thrown if the {@link java.awt.Robot Robot} is not supported by the platform configuration.
     */
    @Test
    public void executeCommandMouseWheel() throws AWTException
    {
        // Create dependencies.
        Robot mockRobot = createMock(Robot.class);

        // Initialise test environment.
        fTestObject = new CommandExecuter(mockRobot);

        // Dictate correct results.
        mockRobot.mouseWheel(0);
        replay(mockRobot);

        // Perform test.
        fTestObject.executeCommand("mouseWheel(0)");

        // Verify test results.
        verify(mockRobot);
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.CommandExecuter#executeCommand() executeCommand()} with the special condition that the
     * command has no brackets.
     * </p>
     * 
     * @throws AWTException Thrown if the {@link java.awt.Robot Robot} is not supported by the platform configuration.
     */
    @Test(expected = IllegalArgumentException.class)
    public void executeCommandNoBrackets() throws AWTException
    {
        // Initialise test environment.
        fTestObject = new CommandExecuter();

        // Perform test.
        fTestObject.executeCommand("keyPress");
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.CommandExecuter#executeCommand() executeCommand()} with the special condition that the
     * command has no opening bracket.
     * </p>
     * 
     * @throws AWTException Thrown if the {@link java.awt.Robot Robot} is not supported by the platform configuration.
     */
    @Test(expected = IllegalArgumentException.class)
    public void executeCommandNoOpeningBracket() throws AWTException
    {
        // Initialise test environment.
        fTestObject = new CommandExecuter();

        // Perform test.
        fTestObject.executeCommand("keyPress10,10)");
    }

    /**
     * <p>
     * Unit test the method {@link com.se.pcremote.server.CommandExecuter#executeCommand() executeCommand()} with the special condition that the
     * 'keyRelease' command is to be executed.
     * </p>
     * 
     * @throws AWTException Thrown if the {@link java.awt.Robot Robot} is not supported by the platform configuration.
     */
    @Test(expected = IllegalArgumentException.class)
    public void executeCommandUnknownCommand() throws AWTException
    {
        // Initialise test environment.
        fTestObject = new CommandExecuter();

        // Perform test.
        fTestObject.executeCommand("unknownCommand(parameter)");
    }
}
