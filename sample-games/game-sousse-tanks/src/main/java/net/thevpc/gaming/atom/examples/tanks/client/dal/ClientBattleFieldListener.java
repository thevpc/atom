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
public interface ClientBattleFieldListener {
    void modelChanged(DALData data);
}
