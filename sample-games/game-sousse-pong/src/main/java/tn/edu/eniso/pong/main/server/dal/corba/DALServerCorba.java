/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong.main.server.dal.corba;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import net.vpc.gaming.atom.engine.SceneEngine;
import tn.edu.eniso.pong.main.server.dal.DALServer;
import tn.edu.eniso.pong.main.server.dal.DALServerListener;
import tn.edu.eniso.pong.main.shared.dal.DALUtil;
import tn.edu.eniso.pong.main.shared.dal.corba.DALUtilCorba;
import tn.edu.eniso.pong.main.shared.dal.corba.generated.ClientOperationsCorba;
import tn.edu.eniso.pong.main.shared.dal.corba.generated.ServerOperationsCorba;
import tn.edu.eniso.pong.main.shared.dal.corba.generated.ServerOperationsCorbaHelper;
import tn.edu.eniso.pong.main.shared.model.MainModel;

import java.rmi.RemoteException;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DALServerCorba implements DALServer, Runnable {

    private DALServerListener listener;
    private ServerOperationsCorbaImpl server;
    private ORB orb;

    public DALServerCorba() throws RemoteException {
    }

    @Override
    public void start(String serverAddress, int serverPort, DALServerListener callback) {
        this.listener = callback;
// create and initialize the ORB
        orb = ORB.init(new String[]{
                "-ORBInitialPort", String.valueOf(serverPort),
                "-ORBInitialHost", serverAddress
        }, null);
        new Thread(this).start();

    }

    public void run() {

        try {
// get reference to rootpoa & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
// create servant and register it with the ORB
            server = new ServerOperationsCorbaImpl(listener);
// get object reference from the servant
            ServerOperationsCorba href = ServerOperationsCorbaHelper.narrow(rootpoa.servant_to_reference(server));
// get the root naming context
// Use NamingContextExt which is part of the Interoperable
// Naming Service (INS) specification.
            NamingContextExt ncRef = NamingContextExtHelper.narrow(orb.resolve_initial_references("NameService"));
// bind the Object Reference in Naming
            NameComponent path[] = ncRef.to_name("SoPongCorba");
            ncRef.rebind(path, href);
// wait for invocations from clients
            orb.run();
        } catch (Exception ex) {
            throw new RuntimeException("Impossible de lancer le serveur");
        }
    }

    public void sendModelChanged(SceneEngine sceneEngine) {
        if (server == null) {
            return;
        }
        ClientOperationsCorba client = server.getClient();
        if (client == null) {
            return;
        }
        client.modelChanged(DALUtilCorba.toModelCorba(DALUtil.toDALStructModel(sceneEngine)));
    }
}
