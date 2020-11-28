/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.pong.main.shared.dal;

import net.thevpc.gaming.atom.examples.pong.main.client.dal.DALClient;
import net.thevpc.gaming.atom.examples.pong.main.client.dal.corba.DALClientCorba;
import net.thevpc.gaming.atom.examples.pong.main.client.dal.multicast.DALClientSocketMulticast;
import net.thevpc.gaming.atom.examples.pong.main.client.dal.rmi.DALClientRMI;
import net.thevpc.gaming.atom.examples.pong.main.client.dal.tcp.DALClientSocketTCP;
import net.thevpc.gaming.atom.examples.pong.main.client.dal.udp.DALClientSocketUDP;
import net.thevpc.gaming.atom.examples.pong.main.server.dal.DALServer;
import net.thevpc.gaming.atom.examples.pong.main.server.dal.corba.DALServerCorba;
import net.thevpc.gaming.atom.examples.pong.main.server.dal.multicast.DALServerSocketMulticast;
import net.thevpc.gaming.atom.examples.pong.main.server.dal.rmi.DALServerRMI;
import net.thevpc.gaming.atom.examples.pong.main.server.dal.tcp.DALServerSocketTCP;
import net.thevpc.gaming.atom.examples.pong.main.server.dal.udp.DALServerSocketUDP;
import net.thevpc.gaming.atom.examples.pong.main.shared.model.AppTransport;

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
