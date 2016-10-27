/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong.main.server.dal.rmi;

import tn.edu.eniso.pong.main.client.dal.rmi.ClientOperationsRMI;
import tn.edu.eniso.pong.main.server.dal.DALServerListener;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class ServerOperationsRMIImpl extends UnicastRemoteObject implements ServerOperationsRMI {

    private DALServerListener listener;
    private ClientOperationsRMI client;

    public ServerOperationsRMIImpl(DALServerListener listener) throws RemoteException {
        this.listener = listener;
    }

    public ClientOperationsRMI getClient() {
        return client;
    }

    @Override
    public void keyLeft() throws RemoteException {
        listener.recieveKeyLeft();
    }

    @Override
    public void keyRight() throws RemoteException {
        listener.recieveKeyRight();
    }

    @Override
    public void keySpace() throws RemoteException {
        listener.recieveKeySpace();
    }

    @Override
    public void setClient(ClientOperationsRMI client) throws RemoteException {
        this.client = client;
        listener.clientConnected();
    }
}
