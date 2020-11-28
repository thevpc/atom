/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.soussecraft.main.dal;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Taha Ben Salah
 */
public interface DALClientRMI extends Remote {

    void gameStarted(DalModel model, int player) throws RemoteException;

    void modelChanged(DalModel model) throws RemoteException;
}
