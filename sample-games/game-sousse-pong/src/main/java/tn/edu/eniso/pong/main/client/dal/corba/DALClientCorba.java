/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong.main.client.dal.corba;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import tn.edu.eniso.pong.main.client.dal.DALClient;
import tn.edu.eniso.pong.main.client.dal.DALClientListener;
import tn.edu.eniso.pong.main.shared.dal.corba.generated.*;

import java.rmi.RemoteException;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DALClientCorba implements DALClient, Runnable {

    private DALClientListener listener;
    private ServerOperationsCorba server;
    private ClientOperationsCorba client;
    private ORB orb;

    public DALClientCorba() throws RemoteException {
    }

    @Override
    public void start(String serverAddress, int serverPort, DALClientListener callback) {
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
// get the root naming context
            org.omg.CORBA.Object objRef =
                    orb.resolve_initial_references("NameService");
// Use NamingContextExt instead of NamingContext. This is
// part of the Interoperable naming Service.
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
// resolve the Object Reference in Naming
            String name = "SoPongCorba";
            server = ServerOperationsCorbaHelper.narrow(ncRef.resolve_str(name));

            POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            final ClientOperationsCorbaImpl cliImpl = new ClientOperationsCorbaImpl(listener);
            rootPOA.activate_object(cliImpl);
            client = ClientOperationsCorbaHelper.narrow(rootPOA.servant_to_reference(cliImpl));
            server.setClient(new ClientOperationsCorbaHolder(client));
            listener.connected();

            rootPOA.the_POAManager().activate();

            //Wait for messages
            orb.run();
        } catch (Exception ex) {
            ex.printStackTrace();
            //ignore error
        }
    }

    public void sendLeftKeyPressed() {
        server.keyLeft();
    }

    public void sendRightKeyPressed() {
        server.keyRight();
    }

    @Override
    public void sendSpacePressed() {
        server.keySpace();
    }
}
