/**
 * This file is part of the Tanks project.
 * 
 * This project is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This project is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * This project. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package net.thevpc.gaming.atom.examples.kombla.welcome;

/**
 * Enumeration to define the execution mode of the game
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public enum AppRole {
    /**
     * game is played in local mode (no networking)
     */
    LOCAL_GAME,
    /**
     * game is played in server mode
     */
    HOST_GAME,
    /**
     * game is played in client mode
     */
    JOIN_GAME
}
