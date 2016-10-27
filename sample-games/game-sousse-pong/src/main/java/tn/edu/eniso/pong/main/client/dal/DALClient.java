/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong.main.client.dal;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface DALClient {

    public void start(String serverAddress, int serverPort, DALClientListener callback);

    public void sendLeftKeyPressed();

    public void sendRightKeyPressed();

    public void sendSpacePressed();
}
