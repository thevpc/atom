/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.soussecraft.main.dal;

/**
 *
 * @author Taha Ben Salah
 */
public interface DALClientListener {
    public void playerConnected(int playerId);
    public void gameStarted(ModelUpdater updater);
    public void modelChanged(ModelUpdater updater);
}
