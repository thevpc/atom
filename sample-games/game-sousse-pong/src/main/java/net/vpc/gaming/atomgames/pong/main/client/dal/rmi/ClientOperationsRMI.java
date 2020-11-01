/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atomgames.pong.main.client.dal.rmi;

import net.vpc.gaming.atomgames.pong.main.shared.dal.model.DALStructModel;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface ClientOperationsRMI extends Remote {

    public void modelChanged(DALStructModel data) throws RemoteException;
}
