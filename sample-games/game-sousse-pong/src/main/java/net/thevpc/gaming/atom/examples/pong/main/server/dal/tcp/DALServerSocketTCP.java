/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.pong.main.server.dal.tcp;

import net.thevpc.gaming.atom.examples.pong.main.server.dal.DALServer;
import net.thevpc.gaming.atom.examples.pong.main.server.dal.DALServerListener;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.examples.pong.main.shared.dal.DALUtil;
import net.thevpc.gaming.atom.examples.pong.main.shared.dal.model.DALStructModel;
import net.thevpc.gaming.atom.examples.pong.main.shared.dal.sockets.DALUtilStream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DALServerSocketTCP implements DALServer, Runnable {

    private DALServerListener listener;
    private ServerSocket serverSocket;
    private Socket client;
    private DataInputStream in;
    private DataOutputStream out;

    public DALServerSocketTCP() throws RemoteException {
    }

    @Override
    public void start(String serverAddress, int serverPort, DALServerListener callback) {
        this.listener = callback;
        try {
            serverSocket = new ServerSocket(1050);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        new Thread(this).start();
    }

    public void run() {
        try {
            client = serverSocket.accept();
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
            listener.clientConnected();
            LOOP:
            while (true) {
                char c = in.readChar();
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
            serverSocket.close();
        } catch (Exception ex) {
            throw new RuntimeException("Impossible de lancer le serveur");
        }
    }

    public void sendModelChanged(SceneEngine sceneEngine) {
        if (client != null && out != null) {
            DALStructModel data = DALUtil.toDALStructModel(sceneEngine);
            try {
                DALUtilStream.writeDALData(data, out);
                out.flush();
            } catch (IOException ex) {
                System.err.println("Unable to send data : "+ex.toString());
                //ex.printStackTrace();
                //ignore error
            }
        }
    }
}
