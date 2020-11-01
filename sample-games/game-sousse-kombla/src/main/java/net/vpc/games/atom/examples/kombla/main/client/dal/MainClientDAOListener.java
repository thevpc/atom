package net.vpc.games.atom.examples.kombla.main.client.dal;

import net.vpc.games.atom.examples.kombla.main.shared.model.DynamicGameModel;

/**
 * Created by vpc on 10/7/16.
 */
public interface MainClientDAOListener {
    /**
     * called by DAO to inform engine of an incoming data
     * @param model
     */
    public void onModelChanged(DynamicGameModel model);
}
