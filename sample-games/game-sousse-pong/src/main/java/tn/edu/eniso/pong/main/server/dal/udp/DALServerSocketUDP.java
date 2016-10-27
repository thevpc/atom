/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong.main.server.dal.udp;

import net.vpc.gaming.atom.engine.SceneEngine;
import tn.edu.eniso.pong.main.server.dal.DALServer;
import tn.edu.eniso.pong.main.server.dal.DALServerListener;
import tn.edu.eniso.pong.main.shared.dal.DALUtil;
import tn.edu.eniso.pong.main.shared.dal.model.DALStructModel;
import tn.edu.eniso.pong.main.shared.dal.sockets.DALUtilByteArray;
import tn.edu.eniso.pong.main.shared.dal.sockets.DALUtilByteArray.IntHolder;
import tn.edu.eniso.pong.main.shared.model.MainModel;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.RemoteException;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DALServerSocketUDP implements DALServer, Runnable {

    private DALServerListener listener;
    private DatagramSocket serverSocket;
    private InetAddress clientAddress;
    private int clientPort;

    public DALServerSocketUDP() throws RemoteException {
    }

    @Override
    public void start(String serverAddress, int serverPort, DALServerListener callback) {
        this.listener = callback;
        try {
            serverSocket = new DatagramSocket(1050);
        } catch (SocketException ex) {
            throw new RuntimeException(ex);
        }
        new Thread(this).start();
    }

    public void run() {
        try {
            byte[] buffer = new byte[32]; //max=6 : 4 pour frame et 2 pour char
            DatagramPacket p = new DatagramPacket(buffer, buffer.length);
            serverSocket.receive(p);
            clientAddress = p.getAddress();
            clientPort = p.getPort();
            listener.clientConnected();
            long lastFrame = -1;
            LOOP:
            while (true) {
                serverSocket.receive(p);
                IntHolder refInt = new IntHolder();
                long frame = DALUtilByteArray.readLong(buffer, refInt);
                if (lastFrame < frame) {
                    lastFrame = frame;
                    char c = DALUtilByteArray.readChar(buffer, refInt);
                    switch (c) {
                        case 'L': {
                            listener.recieveKeyLeft();
                            break;
                        }
                        case 'R': {
                            listener.recieveKeyRight();
                            break;
                        }
                        case ' ': {
                            listener.recieveKeySpace();
                            break;
//                        break;
                        }
                        case 'Q': {
                            break LOOP;
//                        break;
                        }
                    }
                }
            }
            serverSocket.close();
        } catch (Exception ex) {
            throw new RuntimeException("Impossible de lancer le serveur", ex);
        }
    }

    public void sendModelChanged(SceneEngine sceneEngine) {
        if (clientAddress != null) {
            DALStructModel data = DALUtil.toDALStructModel(sceneEngine);
            try {
                byte[] bytes = DALUtilByteArray.toByteArray(data);
                serverSocket.send(new DatagramPacket(bytes, bytes.length, clientAddress, clientPort));

            } catch (Exception ex) {
                ex.printStackTrace();
                //ignore error
            }
        }
    }
}
