/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong.main.server.dal.rmi;

import net.vpc.gaming.atom.engine.SceneEngine;
import tn.edu.eniso.pong.main.client.dal.rmi.ClientOperationsRMI;
import tn.edu.eniso.pong.main.server.dal.DALServer;
import tn.edu.eniso.pong.main.server.dal.DALServerListener;
import tn.edu.eniso.pong.main.shared.dal.DALUtil;
import tn.edu.eniso.pong.main.shared.dal.model.DALStructModel;
import tn.edu.eniso.pong.main.shared.model.MainModel;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DALServerRMI implements DALServer {

    private DALServerListener listener;
    private ServerOperationsRMIImpl server;

    public DALServerRMI() throws RemoteException {
    }

    @Override
    public void start(String serverAddress, int serverPort, DALServerListener callback) {
        try {
            this.listener = callback;
            Registry registry = LocateRegistry.createRegistry(serverPort);
            registry.bind("SoPongRMI", server = new ServerOperationsRMIImpl(listener));
        } catch (Exception ex) {
            throw new RuntimeException("Impossible de lancer le serveur");
        }
    }

    public void sendModelChanged(SceneEngine sceneEngine) {
        ClientOperationsRMI client = server.getClient();
        if (client != null) {
            DALStructModel data = DALUtil.toDALStructModel(sceneEngine);

            try {
                client.modelChanged(data);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                //ignore error
            }
        }
    }
}
