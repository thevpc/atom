/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine.maze;

import net.thevpc.gaming.atom.model.CellDef;

/**
 * Tile Decorator called by Maze Generator to decorate generated Tiles
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface MazeDecorator {
    public void decorate(CellDef tile, boolean obstacle);
}
