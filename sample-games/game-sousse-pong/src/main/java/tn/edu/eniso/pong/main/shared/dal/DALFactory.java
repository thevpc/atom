/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong.main.shared.dal;

import tn.edu.eniso.pong.main.client.dal.DALClient;
import tn.edu.eniso.pong.main.client.dal.corba.DALClientCorba;
import tn.edu.eniso.pong.main.client.dal.multicast.DALClientSocketMulticast;
import tn.edu.eniso.pong.main.client.dal.rmi.DALClientRMI;
import tn.edu.eniso.pong.main.client.dal.tcp.DALClientSocketTCP;
import tn.edu.eniso.pong.main.client.dal.udp.DALClientSocketUDP;
import tn.edu.eniso.pong.main.server.dal.DALServer;
import tn.edu.eniso.pong.main.server.dal.corba.DALServerCorba;
import tn.edu.eniso.pong.main.server.dal.multicast.DALServerSocketMulticast;
import tn.edu.eniso.pong.main.server.dal.rmi.DALServerRMI;
import tn.edu.eniso.pong.main.server.dal.tcp.DALServerSocketTCP;
import tn.edu.eniso.pong.main.server.dal.udp.DALServerSocketUDP;
import tn.edu.eniso.pong.main.shared.model.AppTransport;

import java.rmi.RemoteException;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DALFactory {

    public static DALServer createServer(AppTransport transport) {
        try {
            switch (transport) {
                case TCP: {
                    return new DALServerSocketTCP();
                }
                case UDP: {
                    return new DALServerSocketUDP();
                }
                case MULTICAST: {
                    return new DALServerSocketMulticast();
                }
                case RMI: {
                    return new DALServerRMI();
                }
                case CORBA: {
                    return new DALServerCorba();
                }
                default: {
                    return new DALServerSocketTCP();
                }
            }
        } catch (RemoteException ex) {
            throw new RuntimeException("Impossible de creer linstance");
        }
    }

    public static DALClient createClient(AppTransport transport) {
        try {
            switch (transport) {
                case TCP: {
                    return new DALClientSocketTCP();
                }
                case UDP: {
                    return new DALClientSocketUDP();
                }
                case MULTICAST: {
                    return new DALClientSocketMulticast();
                }
                case RMI: {
                    return new DALClientRMI();
                }
                case CORBA: {
                    return new DALClientCorba();
                }
                default: {
                    return new DALClientSocketTCP();
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("Impossible de creer linstance");
        }
    }
}
