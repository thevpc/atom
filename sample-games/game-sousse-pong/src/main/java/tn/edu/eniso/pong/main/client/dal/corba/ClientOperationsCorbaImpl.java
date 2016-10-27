/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong.main.client.dal.corba;

import tn.edu.eniso.pong.main.client.dal.DALClientListener;
import tn.edu.eniso.pong.main.shared.dal.DALDataModelUpdater;
import tn.edu.eniso.pong.main.shared.dal.corba.DALUtilCorba;
import tn.edu.eniso.pong.main.shared.dal.corba.generated.ClientOperationsCorbaPOA;
import tn.edu.eniso.pong.main.shared.dal.corba.generated.ModelCorba;

import java.rmi.RemoteException;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class ClientOperationsCorbaImpl extends ClientOperationsCorbaPOA {

    private DALClientListener listener;

    public ClientOperationsCorbaImpl(DALClientListener listener) throws RemoteException {
        this.listener = listener;
    }

    @Override
    public void modelChanged(final ModelCorba data) {
        listener.modelChanged(new DALDataModelUpdater(DALUtilCorba.toDALStructModel(data)));
    }
}
