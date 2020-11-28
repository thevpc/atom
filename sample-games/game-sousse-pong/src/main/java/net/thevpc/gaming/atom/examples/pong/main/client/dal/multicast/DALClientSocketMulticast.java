/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.pong.main.client.dal.multicast;

import net.thevpc.gaming.atom.examples.pong.main.client.dal.DALClient;
import net.thevpc.gaming.atom.examples.pong.main.client.dal.DALClientListener;
import net.thevpc.gaming.atom.examples.pong.main.shared.dal.DALDataModelUpdater;
import net.thevpc.gaming.atom.examples.pong.main.shared.dal.model.DALStructModel;
import net.thevpc.gaming.atom.examples.pong.main.shared.dal.sockets.DALUtilByteArray;
import net.thevpc.gaming.atom.examples.pong.main.shared.dal.sockets.DALUtilByteArray.IntHolder;
import net.thevpc.gaming.atom.examples.pong.main.shared.dal.sockets.DALUtilMulticast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.RemoteException;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DALClientSocketMulticast implements DALClient, Runnable {

    private DALClientListener listener;
    private MulticastSocket socket;
    private InetAddress groupAddress;
    private InetAddress serverListeningAddress;
    private int serverListeningPort;
    private long lastRecievedFrame = 0;
    private long lastSentFrame = 0;

    public DALClientSocketMulticast() throws RemoteException {
    }

    @Override
    public void start(String serverAddress, int serverPort, DALClientListener callback) {
        try {
            this.listener = callback;
            socket = new MulticastSocket(serverPort);
            groupAddress = InetAddress.getByName(serverAddress);
            if (!groupAddress.isMulticastAddress()) {
                groupAddress = InetAddress.getByName(DALUtilMulticast.DEFAULT_ADDRESS);
            }
            socket.joinGroup(groupAddress);
        } catch (Exception ex) {
            throw new RuntimeException("Impossible de lancer le serveur", ex);
        }
        new Thread(this).start();
    }

    public void run() {
        try {
            byte[] buffer = new byte[DALUtilMulticast.BURST_SIZE];
            //recive broadcast
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
//                System.out.println("WAITING FOR BIRST ON " + groupAddress + " : " + serverPort+" -- interface="+socket.getInterface()+"/networkInterface="+socket.getNetworkInterface()+"/localSocket="+socket.getLocalSocketAddress()+"/broadcast="+socket.getBroadcast()+"/receiveBufferSize="+socket.getReceiveBufferSize()+"/sendBufferSize="+socket.getSendBufferSize());
                socket.receive(packet);
                if (DALUtilMulticast.isBurst(buffer)) {
//                    System.out.println("RECIEVED BIRST FROM " + packet.getSocketAddress());
                    //send the same packed to connect
                    serverListeningPort = packet.getPort();
                    serverListeningAddress = packet.getAddress();
//                    System.out.println("SEND BIRST to " + packet.getSocketAddress());
                    socket.send(packet);
                    break;
                } else {
//                    System.out.println("RECIEVED NO BIRST ??? FROM " + packet.getSocketAddress() + ". Ignored!");
                }
            }

            boolean connected = false;
            while (true) {
                byte[] buffer2 = new byte[DALUtilByteArray.DALDATA_SIZE];
//                System.out.println("WAITING FOR MODEL ON " + groupAddress + " : " + serverPort);
                DatagramPacket packet2 = new DatagramPacket(buffer2, buffer2.length);
                socket.receive(packet2);
                if (!DALUtilMulticast.isBurst(buffer2)) {
//                    System.out.println("RECIEVED MODEL FROM " + packet2.getSocketAddress());
                    DALStructModel d = DALUtilByteArray.toDALData(buffer2);
                    if (d.frame > lastRecievedFrame) {
                        lastRecievedFrame = d.frame;
                        if (!connected) {
                            connected = true;
                            listener.connected();
                        }
                        listener.modelChanged(new DALDataModelUpdater(d));
                    }
                } else {
//                    System.out.println("RECIEVED ??? FROM " + packet2.getSocketAddress() + ". Ignored!");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //ignore error
        }
    }

    public void sendLeftKeyPressed() {
        sendChar('L');
    }

    public void sendRightKeyPressed() {
        sendChar('R');
    }

    @Override
    public void sendSpacePressed() {
        sendChar(' ');
    }

    private void sendChar(char c) {
        try {
            lastSentFrame++;
            byte[] buffer = new byte[8 + 2];
            IntHolder intref = new IntHolder();
            DALUtilByteArray.writeLong(lastSentFrame, buffer, intref);
            DALUtilByteArray.writeChar(c, buffer, intref);
            socket.send(new DatagramPacket(buffer, buffer.length, serverListeningAddress, serverListeningPort));
        } catch (IOException ex) {
            ex.printStackTrace();
            //ignore error
        }
    }
}
