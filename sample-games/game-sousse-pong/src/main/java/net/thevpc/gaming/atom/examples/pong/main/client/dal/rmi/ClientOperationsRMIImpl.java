/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.pong.main.client.dal.rmi;

import net.thevpc.gaming.atom.examples.pong.main.client.dal.DALClientListener;
import net.thevpc.gaming.atom.examples.pong.main.shared.dal.DALDataModelUpdater;
import net.thevpc.gaming.atom.examples.pong.main.shared.dal.model.DALStructModel;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class ClientOperationsRMIImpl extends UnicastRemoteObject implements ClientOperationsRMI {
    private DALClientListener listener;

    public ClientOperationsRMIImpl(DALClientListener listener) throws RemoteException {
        this.listener = listener;
    }

    @Override
    public void modelChanged(DALStructModel data) throws RemoteException {
        listener.modelChanged(new DALDataModelUpdater(data));
    }

}
