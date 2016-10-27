/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong.main.client.dal.rmi;

import tn.edu.eniso.pong.main.shared.dal.model.DALStructModel;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface ClientOperationsRMI extends Remote {

    public void modelChanged(DALStructModel data) throws RemoteException;
}
