/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.soussecraft.main.dal;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import net.thevpc.gaming.atom.model.ModelPoint;

/**
 *
 * @author Taha Ben Salah
 */
public class DALClient {

    private DALClientListener listener;
    private DALClientRMIImpl rmi;
    private DALServerRMI server;
    private int playerId;

    public DALClient(DALClientListener listener) {
        this.listener = listener;
    }

    DALClientListener getListener() {
        return listener;
    }


    public void selectTile(int playerId, ModelPoint point, int idTile) {
        try {
            server.selectTile(playerId, point, idTile);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    public void selectSprite(int playerId, ModelPoint point, Integer spriteId, boolean appendSelection) {
        try {
            server.selectSprite(playerId, point, spriteId, appendSelection);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    public void moveSelectionToSprite(int playerId, ModelPoint point, Integer spriteId) {
        try {
            server.moveSelectionToSprite(playerId, point, spriteId);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    public void moveSelectionToTile(int playerId, ModelPoint point, Integer tileId) {
        try {
            server.moveSelectionToTile(playerId, point, tileId);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    public void start() {
        new Thread(new Runnable() {

            public void run() {
                startAsync();
            }
        }).start();
    }

    private void startAsync() {
        try {
            Registry r = LocateRegistry.getRegistry("localhost", 9999);
            rmi = new DALClientRMIImpl(this);
            server = (DALServerRMI) r.lookup("Server");
            playerId = server.connect(rmi);
            listener.playerConnected(playerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
