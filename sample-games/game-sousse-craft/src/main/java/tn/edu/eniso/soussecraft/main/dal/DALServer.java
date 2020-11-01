/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.soussecraft.main.dal;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.model.Sprite;
import tn.edu.eniso.soussecraft.main.ModelTracker;

/**
 *
 * @author Taha Ben Salah
 */
public class DALServer {

    private DALServerListener listener;
    private DALServerRMIImpl rmi;

    public DALServer(DALServerListener listener) {
        this.listener = listener;
    }

    DALServerListener getListener() {
        return listener;
    }

    public void start() {
        new Thread(new Runnable() {

            public void run() {
                startAsync();
            }
        }).start();
    }

    private void startAsync() {
        try {
            Registry r = LocateRegistry.createRegistry(9999);
            rmi = new DALServerRMIImpl(this);
            r.bind("Server", rmi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gameStarted(SceneEngine model, ModelTracker modelTracker) {
        rmi.gameStarted(convert(model,modelTracker));
    }

    public void modelChanged(SceneEngine model, ModelTracker modelTracker) {
        rmi.modelChanged(convert(model,modelTracker));
    }
    
    private DalModel convert(SceneEngine model, ModelTracker t){
        DalModel m=new DalModel();
        m.setPlayersCount(model.getPlayers().size());
        for (Sprite s : model.getSprites()) {
            m.add(s);
        }
//        for (Sprite s : t.getAddedSprites().values()) {
//            m.add(s);
//        }
//        for (Update s : t.getUpdatedSprites().values()) {
//            m.update(s.getSprite());
//        }
//        for (Sprite sprite : t.getRemovedSprites().values()) {
//            m.remove(sprite);
//        }
        return m;
    }
}
