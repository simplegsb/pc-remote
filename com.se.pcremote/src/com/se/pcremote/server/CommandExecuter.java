/*
    This file is part of PC Remote.

    PC Remote is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    PC Remote is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with PC Remote. If not, see <http://www.gnu.org/licenses/>.
 */
package com.se.pcremote.server;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

import org.apache.log4j.Logger;

/**
 * <p>
 * Executes commands related to control of user input e.g. the mouse and keyboard and requests for output e.g. the screen. Uses the
 * {@link java.awt.Robot Robot} class to actuate these commands.
 * </p>
 * 
 * <p>
 * The commands that can be executed are as follows:
 * </p>
 * 
 * <ul>
 * <li><code>mouseMove(x,y);</code> Moves the mouse to the designated location.</li>
 * <li><code>mouseMoveRelative(x,y);</code> Moves the mouse to the designated location relative to its current location.</li>
 * <li><code>mousePress(mouseButton);</code> Presses a mouse button (note that the command <code>mouseRelease(mouseButton);</code> must be received
 * <li><code>mouseRelease(mouseButton);</code> Releases a mouse button.</li>
 * <li><code>mouseWheel(notches);</code> Spins the mouse wheel.</li>
 * <li><code>keyPress(key);</code> Presses a key on the keyboard (note that the command <code>keyRelease(key);</code> must be received after this to
 * release the key).</li>
 * <li><code>keyRelease(key);</code> Releases a key on the keyboard.</li>
 * </ul>
 * 
 * @author Gary Buyn
 */
public class CommandExecuter
{
    /**
     * <p>
     * Logs messages associated with this class.
     * </p>
     */
    private Logger fLogger;

    /**
     * <p>
     * Actuates the commands.
     * </p>
     */
    private Robot fRobot;

    /**
     * <p>
     * Creates an instance of <code>CommandExecuter</code>.
     * </p>
     * 
     * @throws AWTException Thrown if the {@link java.awt.Robot Robot} is not supported by the platform configuration.
     */
    public CommandExecuter() throws AWTException
    {
        fLogger = Logger.getLogger(getClass());
        fRobot = new Robot();
    }

    /**
     * <p>
     * Creates an instance of <code>CommandExecuter</code>.
     * </p>
     * 
     * @param robot Actuates the commands.
     */
    public CommandExecuter(final Robot robot)
    {
        fRobot = robot;

        fLogger = Logger.getLogger(getClass());
    }

    /**
     * <p>
     * Executes the given command which must be of the form <code>commandName([n[,m]*])</code>.
     * </p>
     * 
     * @param command The command to execute.
     */
    public synchronized void executeCommand(final String command)
    {
        fLogger.debug("Executing command: " + command);

        // Validate command format.
        String[] commandParts = command.split("\\(");
        if (commandParts.length != 2 || commandParts[1].split("\\)").length != 1)
        {
            throw new IllegalArgumentException("Invalid command: Format must be <commandName>(<arg>[,<arg>]).");
        }
        String commandParameters = commandParts[1].split("\\)")[0];

        // Extract method parts from command.
        String methodName = commandParts[0];
        String[] methodParameters = commandParameters.split(",");

        try
        {
            // Execute appropriate command.
            if (methodName.equals("keyPress"))
            {
                fRobot.keyPress(Integer.parseInt(methodParameters[0]));
            }
            else if (methodName.equals("keyRelease"))
            {
                fRobot.keyRelease(Integer.parseInt(methodParameters[0]));
            }
            else if (methodName.equals("mouseMove"))
            {
                fRobot.mouseMove((int) Double.parseDouble(methodParameters[0]), (int) Double.parseDouble(methodParameters[1]));
            }
            else if (methodName.equals("mouseMoveRelative"))
            {
                Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
                fRobot.mouseMove(mouseLocation.x + -1 * (int) Double.parseDouble(methodParameters[0]), mouseLocation.y + -1
                        * (int) Double.parseDouble(methodParameters[1]));
            }
            else if (methodName.equals("mousePress"))
            {
                fRobot.mousePress(Integer.parseInt(methodParameters[0]));
            }
            else if (methodName.equals("mouseRelease"))
            {
                fRobot.mouseRelease(Integer.parseInt(methodParameters[0]));
            }
            else if (methodName.equals("mouseWheel"))
            {
                fRobot.mouseWheel(Integer.parseInt(methodParameters[0]));
            }
            else
            {
                throw new IllegalArgumentException("Unknown command: " + methodName);
            }
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException("Invalid command: Invalid numeric argument given.", e);
        }
    }
}
