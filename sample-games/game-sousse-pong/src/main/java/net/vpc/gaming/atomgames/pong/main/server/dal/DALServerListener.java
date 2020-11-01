/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atomgames.pong.main.server.dal;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface DALServerListener {

    public void clientConnected();

    public void recieveKeyLeft();

    public void recieveKeyRight();

    public void recieveKeySpace();
}
