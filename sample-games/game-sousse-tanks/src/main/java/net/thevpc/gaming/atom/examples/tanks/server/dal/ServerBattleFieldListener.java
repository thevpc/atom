/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.tanks.server.dal;

/**
 *
 * @author vpc
 */
public interface ServerBattleFieldListener {

    void rotateLeft(int player);

    void rotateRight(int player);

    void fire(int player);

    int connect();
}
