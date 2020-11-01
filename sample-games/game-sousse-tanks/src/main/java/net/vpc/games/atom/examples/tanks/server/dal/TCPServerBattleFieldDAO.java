/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.games.atom.examples.tanks.server.dal;

import net.vpc.games.atom.examples.tanks.shared.dal.DALData;

/**
 *
 * @author vpc
 */
public class TCPServerBattleFieldDAO extends AbstractServerBattleFieldDAO{

    @Override
    public void start(ServerBattleFieldListener listener) {
        super.start(listener);
        //lancer la soket
    }

    
    @Override
    public void recieveConnect() {
        
    }

    @Override
    public void recieveRotateLeft(int player) {
        
    }

    @Override
    public void recieveRotateRight(int player) {
        
    }

    @Override
    public void recieveFire(int player) {
        
    }

    @Override
    public void modelChanged(DALData data) {
        
    }
    
}
