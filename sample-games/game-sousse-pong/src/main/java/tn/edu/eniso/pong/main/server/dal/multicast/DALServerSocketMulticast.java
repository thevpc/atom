/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong.main.server.dal.multicast;

import net.vpc.gaming.atom.engine.SceneEngine;
import tn.edu.eniso.pong.main.server.dal.DALServer;
import tn.edu.eniso.pong.main.server.dal.DALServerListener;
import tn.edu.eniso.pong.main.shared.dal.DALUtil;
import tn.edu.eniso.pong.main.shared.dal.model.DALStructModel;
import tn.edu.eniso.pong.main.shared.dal.sockets.DALUtilByteArray;
import tn.edu.eniso.pong.main.shared.dal.sockets.DALUtilByteArray.IntHolder;
import tn.edu.eniso.pong.main.shared.dal.sockets.DALUtilMulticast;
import tn.edu.eniso.pong.main.shared.model.MainModel;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.RemoteException;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DALServerSocketMulticast implements DALServer {

    private DALServerListener listener;
    private MulticastSocket serverSocket;
    private InetAddress groupAddress;
    private int groupPort = 1050;
    private boolean clientConnected = false;
//    private InetAddress clientAddress;
//    private int clientPort;

    public DALServerSocketMulticast() throws RemoteException {
    }

    @Override
    public void start(String serverAddress, int serverPort, DALServerListener callback) {
        this.listener = callback;
        this.groupPort = serverPort;
        try {
            serverSocket = new MulticastSocket();
            groupAddress = InetAddress.getByName(serverAddress);
            if (!groupAddress.isMulticastAddress()) {
                groupAddress = InetAddress.getByName(DALUtilMulticast.DEFAULT_ADDRESS);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Impossible de lancer le serveur", ex);
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                broadcast();
            }
        }).start();
        new Thread(new Runnable() {

            @Override
            public void run() {
                listen();
            }
        }).start();
    }

    public void broadcast() {
        byte[] buffer = DALUtilMulticast.createBurst(); //burst
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, groupAddress, groupPort);
        while (!clientConnected) {
            try {
                Thread.sleep(1000);
//                System.out.println("SEND BURST on " + packet.getSocketAddress());
                serverSocket.send(packet);
            } catch (Exception ex) {
                throw new RuntimeException("Erreur reseau", ex);
            }
        }
//        System.out.println("STOP SENDING BURST : Client Already connected");

    }

    private void listen() {
        try {
            byte[] buffer = new byte[DALUtilMulticast.BURST_SIZE]; //burst
            DatagramPacket p = new DatagramPacket(buffer, buffer.length);
            while (true) {
//                System.out.println("WAITING FOR BIRST");
                serverSocket.receive(p);
                if (DALUtilMulticast.isBurst(buffer)) {
//                    System.out.println("RECIEVED BIRST FROM " + p.getSocketAddress());
                    break;
                } else {
//                    System.out.println("RECIEVED ??? FROM " + p.getSocketAddress() + " Ignored");
                }
            }
            clientConnected = true;
            listener.clientConnected();
            long lastFrame = -1;
            LOOP:
            while (true) {
                System.out.println("WAITING FOR KEY");
                serverSocket.receive(p);
//                System.out.println("RECIEVED KEY FROM " + p.getSocketAddress());
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
        if (clientConnected) {
            DALStructModel data = DALUtil.toDALStructModel(sceneEngine);
            try {
                byte[] bytes = DALUtilByteArray.toByteArray(data);
                serverSocket.send(new DatagramPacket(bytes, bytes.length, groupAddress, groupPort));
            } catch (Exception ex) {
                ex.printStackTrace();
                //ignore error
            }
        }
    }
}
