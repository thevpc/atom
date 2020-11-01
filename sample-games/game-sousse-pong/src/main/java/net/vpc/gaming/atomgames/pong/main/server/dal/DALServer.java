/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atomgames.pong.main.server.dal;

import net.vpc.gaming.atom.engine.SceneEngine;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface DALServer {

    public void start(String serverAddress, int serverPort, DALServerListener callback);

    public void sendModelChanged(SceneEngine sceneEngine);
}
