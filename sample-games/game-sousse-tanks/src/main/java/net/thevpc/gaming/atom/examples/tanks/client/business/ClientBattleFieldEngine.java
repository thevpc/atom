/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.tanks.client.business;

import net.thevpc.gaming.atom.examples.tanks.client.dal.AbstractClientBattleFieldDAO;
import net.thevpc.gaming.atom.examples.tanks.client.dal.ClientBattleFieldListener;
import net.thevpc.gaming.atom.examples.tanks.shared.business.AbstractBattleFieldEngine;
import net.thevpc.gaming.atom.examples.tanks.shared.dal.DALData;
import net.thevpc.gaming.atom.examples.tanks.shared.business.MapManager;
import net.thevpc.gaming.atom.examples.tanks.client.dal.TCPClientBattleFieldDAO;

/**
 *
 * @author Taha Ben Salah
 */
public class ClientBattleFieldEngine extends AbstractBattleFieldEngine implements ClientBattleFieldListener {
    AbstractClientBattleFieldDAO dao;
    int playerId;
    public ClientBattleFieldEngine() {
        setModel(MapManager.getBattleFieldModel("eniso"));
    }

    @Override
    protected void sceneActivating() {
        dao=new TCPClientBattleFieldDAO();
        dao.start(this);
        playerId=dao.connect();
    }
    
    
    @Override
    public void rotateLeft(int playerId){
        dao.sendRotateLeft(this.playerId);
    }
    
    @Override
    public void rotateRight(int playerId){
        dao.sendRotateRight(this.playerId);
    }
    
    @Override
    public void fire(int playerId){
        dao.sendFire(this.playerId);
    }

    @Override
    public void modelChanged(DALData data) {
        
    }
    
}
