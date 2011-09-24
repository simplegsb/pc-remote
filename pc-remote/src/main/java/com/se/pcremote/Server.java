/*
    This file is part of PC Remote.

    PC Remote is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    PC Remote is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with PC Remote. If not, see <http://www.gnu.org/licenses/>.
 */
package com.se.pcremote;

import java.io.IOException;

/**
 * <p>
 * Continually listens for new connections and continually listens for data on those connections in separate threads.
 * </p>
 * 
 * @author Gary Buyn
 */
public interface Server
{
    /**
     * <p>
     * Stops listening for new connections and closes all existing connections.
     * </p>
     * 
     * @throws IOException Thrown if the <code>ServerSocket</code> closure fails.
     */
    void dispose() throws IOException;
}
