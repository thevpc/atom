/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atomgames.pong.main.client.dal.tcp;

import net.vpc.gaming.atomgames.pong.main.client.dal.DALClient;
import net.vpc.gaming.atomgames.pong.main.client.dal.DALClientListener;
import net.vpc.gaming.atomgames.pong.main.shared.dal.DALDataModelUpdater;
import net.vpc.gaming.atomgames.pong.main.shared.dal.model.DALStructModel;
import net.vpc.gaming.atomgames.pong.main.shared.dal.sockets.DALUtilStream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DALClientSocketTCP implements DALClient, Runnable {

    private DALClientListener listener;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public DALClientSocketTCP() throws RemoteException {
    }

    @Override
    public void start(String serverAddress, int serverPort, DALClientListener callback) {
        this.listener = callback;
        try {
            socket = new Socket(serverAddress, serverPort);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        new Thread(this).start();
    }

    public void run() {
        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            listener.connected();
            while (true) {
                DALStructModel data = DALUtilStream.readDALData(in);
                listener.modelChanged(new DALDataModelUpdater(data));
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
            out.writeChar(c);
            out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
            //ignore error
        }
    }
}
