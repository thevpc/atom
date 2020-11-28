/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.soussecraft.main.dal;

import java.rmi.Remote;
import java.rmi.RemoteException;

import net.thevpc.gaming.atom.model.ModelPoint;

/**
 *
 * @author Taha Ben Salah
 */
public interface DALServerRMI extends Remote {

    public int connect(DALClientRMI client) throws RemoteException;
    
    public void selectTile(int playerId, ModelPoint point, int idTile) throws RemoteException;

    public void selectSprite(int playerId, ModelPoint point, Integer spriteId, boolean appendSelection) throws RemoteException;

    public void moveSelectionToSprite(int playerId, ModelPoint point, Integer spriteId) throws RemoteException;

    public void moveSelectionToTile(int playerId, ModelPoint point, Integer tileId) throws RemoteException;
}
