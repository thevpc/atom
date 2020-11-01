/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atomgames.pong.main.client.dal.udp;

import net.vpc.gaming.atomgames.pong.main.client.dal.DALClient;
import net.vpc.gaming.atomgames.pong.main.client.dal.DALClientListener;
import net.vpc.gaming.atomgames.pong.main.shared.dal.DALDataModelUpdater;
import net.vpc.gaming.atomgames.pong.main.shared.dal.model.DALStructModel;
import net.vpc.gaming.atomgames.pong.main.shared.dal.sockets.DALUtilByteArray;
import net.vpc.gaming.atomgames.pong.main.shared.dal.sockets.DALUtilByteArray.IntHolder;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.RemoteException;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DALClientSocketUDP implements DALClient, Runnable {

    private DALClientListener listener;
    private DatagramSocket socket;
    private long lastRecievedFrame = 0;
    private long lastSentFrame = 0;
    private String serverAddress = null;
    private int serverPort;

    public DALClientSocketUDP() throws RemoteException {
    }

    public void setListener(DALClientListener listener) {
        this.listener = listener;
    }

    @Override
    public void start(String serverAddress, int serverPort, DALClientListener callback) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.listener = callback;
        new Thread(this).start();
    }

    public void run() {
        try {
            socket = new DatagramSocket();
            byte[] buffer = new byte[4];
            DALUtilByteArray.writeInt(1, buffer, new IntHolder());
            socket.send(new DatagramPacket(buffer, buffer.length, InetAddress.getByName(serverAddress), serverPort));
            boolean connected = false;
            while (true) {
                byte[] buffer2 = new byte[DALUtilByteArray.DALDATA_SIZE];
                socket.receive(new DatagramPacket(buffer2, buffer2.length));
                DALStructModel d = DALUtilByteArray.toDALData(buffer2);
                if (d.frame > lastRecievedFrame) {
                    lastRecievedFrame = d.frame;
                    if (!connected) {
                        connected = true;
                        listener.connected();
                    }
                    listener.modelChanged(new DALDataModelUpdater(d));
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
            socket.send(new DatagramPacket(buffer, buffer.length, InetAddress.getByName(serverAddress), serverPort));
        } catch (IOException ex) {
            ex.printStackTrace();
            //ignore error
        }
    }
}
