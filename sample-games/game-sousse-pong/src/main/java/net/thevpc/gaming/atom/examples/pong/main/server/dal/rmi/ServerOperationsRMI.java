/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.pong.main.server.dal.rmi;

import net.thevpc.gaming.atom.examples.pong.main.client.dal.rmi.ClientOperationsRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface ServerOperationsRMI extends Remote {

    public void setClient(ClientOperationsRMI client) throws RemoteException;

    public void keyLeft() throws RemoteException;

    public void keyRight() throws RemoteException;

    public void keySpace() throws RemoteException;
}
