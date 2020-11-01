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
public abstract class AbstractServerBattleFieldDAO {

    private ServerBattleFieldListener listener;

    public void start(ServerBattleFieldListener listener) {
        this.listener = listener;
    }

    //called by client
    public abstract void recieveConnect();

    public abstract void recieveRotateLeft(int player);

    public abstract void recieveRotateRight(int player);

    public abstract void recieveFire(int player);

    //called by server business
    public abstract void modelChanged(DALData data);
}
