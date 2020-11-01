/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atomgames.pong.main.client.dal.rmi;

import net.vpc.gaming.atomgames.pong.main.client.dal.DALClient;
import net.vpc.gaming.atomgames.pong.main.client.dal.DALClientListener;
import net.vpc.gaming.atomgames.pong.main.server.dal.rmi.ServerOperationsRMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DALClientRMI implements DALClient {

    private DALClientListener listener;
    private ServerOperationsRMI server;
    private ClientOperationsRMI client;

    public DALClientRMI() throws RemoteException {
    }

    @Override
    public void start(String serverAddress, int serverPort, DALClientListener callback) {
        try {
            this.listener = callback;
            Registry registry = LocateRegistry.getRegistry(serverAddress, serverPort);
            server = (ServerOperationsRMI) registry.lookup("SoPongRMI");
            server.setClient(client = new ClientOperationsRMIImpl(listener));
            listener.connected();
        } catch (Exception ex) {
            ex.printStackTrace();
            //ignore error
        }
    }

    public void sendLeftKeyPressed() {
        try {
            server.keyLeft();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            //ignore error
        }
    }

    public void sendRightKeyPressed() {
        try {
            server.keyRight();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            //ignore error
        }
    }

    @Override
    public void sendSpacePressed() {
        try {
            server.keySpace();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            //ignore error
        }
    }
}
