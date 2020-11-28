/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.tanks.client.dal;

import net.thevpc.gaming.atom.examples.tanks.shared.dal.DALData;

/**
 *
 * @author vpc
 */
public abstract class AbstractClientBattleFieldDAO {

    private ClientBattleFieldListener listener;

    public abstract int connect();

    public void start(ClientBattleFieldListener listener) {
        this.listener = listener;
    }

    public abstract void sendRotateLeft(int playerId);

    public abstract void sendRotateRight(int playerId);

    public abstract void sendFire(int playerId);

    protected void modelChanged(DALData data) {
        listener.modelChanged(data);
    }
}
