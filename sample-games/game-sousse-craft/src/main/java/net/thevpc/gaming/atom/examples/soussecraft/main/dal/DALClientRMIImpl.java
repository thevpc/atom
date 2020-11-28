/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.soussecraft.main.dal;

import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.extension.strategy.players.StrategyGamePlayer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Taha Ben Salah
 */
public class DALClientRMIImpl extends UnicastRemoteObject implements DALClientRMI {

    private DALClient dal;

    public DALClientRMIImpl(DALClient dal) throws RemoteException {
        this.dal = dal;
    }

    public void gameStarted(DalModel model, final int player) throws RemoteException {
        dal.getListener().gameStarted(new ClienModelUpdater(model){

            @Override
            public void updateModel(SceneEngine engine) {
                for(int i=1;i<=getDalModel().getPlayersCount();i++){
                    engine.addPlayer(new StrategyGamePlayer("p"+i));
                }
//                ((MainModel)model).setControlPlayer(player);
                super.updateModel(engine);
            }
            
        });
    }

    public void modelChanged(DalModel model) throws RemoteException {
        dal.getListener().modelChanged(new ClienModelUpdater(model));
    }
}
