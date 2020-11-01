/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.soussecraft.main.dal;

import net.vpc.gaming.atom.model.ModelPoint;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Taha Ben Salah
 */
public class DALServerRMIImpl extends UnicastRemoteObject implements DALServerRMI {

    private DALServer dal;
    private HashMap<Integer, DALClientRMI> clients = new HashMap<Integer, DALClientRMI>();

    public DALServerRMIImpl(DALServer dal) throws RemoteException {
        this.dal = dal;
    }

    public int connect(DALClientRMI client) {
        int c = dal.getListener().clientConnected();
        clients.put(c, client);
        return c;
    }

    public void modelChanged(DalModel dalModel) {
        for (DALClientRMI entry : clients.values()) {
            try {
                entry.modelChanged(dalModel);
            } catch (Exception ex) {
                ex.printStackTrace();
                //ignore
            }
        }
    }

    public void gameStarted(DalModel dalModel) {
        for (Map.Entry<Integer, DALClientRMI> entry : clients.entrySet()) {
            try {
                entry.getValue().gameStarted(dalModel, entry.getKey());
            } catch (Exception ex) {
                ex.printStackTrace();
                //ignore
            }
        }
    }

    public void moveSelectionToSprite(int playerId, ModelPoint point, Integer spriteId) throws RemoteException {
        dal.getListener().moveSelectionToSprite(playerId, point, spriteId);
    }

    public void moveSelectionToTile(int playerId, ModelPoint point, Integer tileId) throws RemoteException {
        dal.getListener().moveSelectionToTile(playerId, point, tileId);
    }

    public void selectSprite(int playerId, ModelPoint point, Integer spriteId, boolean appendSelection) throws RemoteException {
        dal.getListener().selectSprite(playerId, point, spriteId, appendSelection);
    }

    public void selectTile(int playerId, ModelPoint point, int idTile) throws RemoteException {
        dal.getListener().selectTile(playerId, point, idTile);
    }
}
