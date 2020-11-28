package net.thevpc.gaming.atom.examples.kombla.main.client.dal;

import net.thevpc.gaming.atom.examples.kombla.main.shared.model.StartGameInfo;

import net.thevpc.gaming.atom.examples.kombla.main.shared.engine.AppConfig;

/**
 * Created by vpc on 10/7/16.
 */
public interface MainClientDAO {
    /**
     * !!non blocking!! start method.
     * called once by Engine when game starts
     * @param listener dal listener
     * @param properties config properties
     */
    public void start(MainClientDAOListener listener, AppConfig properties);

    /**
     * !!blocking!! method to connect to server and retrieve game info
     * @return StartGameInfo
     */
    public StartGameInfo connect();

    public void sendMoveLeft();

    public void sendMoveRight();

    public void sendMoveUp();

    public void sendMoveDown();

    public void sendFire();
}
