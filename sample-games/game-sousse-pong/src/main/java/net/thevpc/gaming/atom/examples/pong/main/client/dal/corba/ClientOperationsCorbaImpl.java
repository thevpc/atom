/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.pong.main.client.dal.corba;

import net.thevpc.gaming.atom.examples.pong.main.client.dal.DALClientListener;
import net.thevpc.gaming.atom.examples.pong.main.shared.dal.DALDataModelUpdater;
import net.thevpc.gaming.atom.examples.pong.main.shared.dal.corba.DALUtilCorba;
import net.thevpc.gaming.atom.examples.pong.main.shared.dal.corba.generated.ClientOperationsCorbaPOA;
import net.thevpc.gaming.atom.examples.pong.main.shared.dal.corba.generated.ModelCorba;

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
