package net.vpc.games.atom.examples.kombla.main.server.dal;

import net.vpc.games.atom.examples.kombla.main.shared.model.DynamicGameModel;

import net.vpc.games.atom.examples.kombla.main.shared.engine.AppConfig;

/**
 * Created by vpc on 10/7/16.
 */
public interface MainServerDAO {
    /**
     * stats in non blocking mode the DAO
     * @param listener dao listener to catch dal events
     * @param properties extra properties such as "serverPort", "serverAddress"
     */
    void start(MainServerDAOListener listener, AppConfig properties);

    void sendModelChanged(DynamicGameModel dynamicGameModel);
}
