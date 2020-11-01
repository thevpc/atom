/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.games.atom.examples.tanks.client.dal;

import net.vpc.games.atom.examples.tanks.shared.dal.DALData;

/**
 *
 * @author vpc
 */
public interface ClientBattleFieldListener {
    void modelChanged(DALData data);
}
