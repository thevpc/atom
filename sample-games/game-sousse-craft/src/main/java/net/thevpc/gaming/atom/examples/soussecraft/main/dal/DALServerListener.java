/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.soussecraft.main.dal;

import net.thevpc.gaming.atom.model.ModelPoint;

/**
 *
 * @author Taha Ben Salah
 */
public interface DALServerListener {
    int clientConnected();
    
    public void selectTile(int playerId, ModelPoint point, int idTile) ;

    public void selectSprite(int playerId, ModelPoint point, Integer spriteId, boolean appendSelection) ;

    public void moveSelectionToSprite(int playerId, ModelPoint point, Integer spriteId) ;

    public void moveSelectionToTile(int playerId, ModelPoint point, Integer tileId) ;
}
