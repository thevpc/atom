/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.soussecraft.main.dal;

import net.vpc.gaming.atom.model.ModelPoint;

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
