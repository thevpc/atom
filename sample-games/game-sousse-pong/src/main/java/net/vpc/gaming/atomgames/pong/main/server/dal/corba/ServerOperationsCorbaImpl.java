/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atomgames.pong.main.server.dal.corba;

import net.vpc.gaming.atomgames.pong.main.server.dal.DALServerListener;
import net.vpc.gaming.atomgames.pong.main.shared.dal.corba.generated.ClientOperationsCorba;
import net.vpc.gaming.atomgames.pong.main.shared.dal.corba.generated.ClientOperationsCorbaHolder;
import net.vpc.gaming.atomgames.pong.main.shared.dal.corba.generated.ServerOperationsCorbaPOA;

import java.rmi.RemoteException;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class ServerOperationsCorbaImpl extends ServerOperationsCorbaPOA {

    private DALServerListener listener;
    private ClientOperationsCorbaHolder client;

    public ServerOperationsCorbaImpl(DALServerListener listener) throws RemoteException {
        this.listener = listener;
    }

    public ClientOperationsCorba getClient() {
        return client == null ? null : client.value;
    }

    @Override
    public void keyLeft() {
        listener.recieveKeyLeft();
    }

    @Override
    public void keyRight() {
        listener.recieveKeyRight();
    }

    @Override
    public void keySpace() {
        listener.recieveKeySpace();
    }

    @Override
    public void setClient(ClientOperationsCorbaHolder client) {
        this.client = client;
        listener.clientConnected();
    }
}
